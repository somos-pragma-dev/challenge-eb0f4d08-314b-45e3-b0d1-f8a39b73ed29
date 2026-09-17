package com.pragma.riskengine.replay;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class DeterministicReplay {
    private static final Logger log = LoggerFactory.getLogger(DeterministicReplay.class);
    private static final int RING_BUFFER_SIZE = 1024;
    private static final String EVENT_LOG_MAGIC = "RPE1";
    private static final int MAGIC_LENGTH = 4;
    private static final int VERSION_LENGTH = 4;
    private static final int HEADER_SIZE = MAGIC_LENGTH + VERSION_LENGTH + 8 + 8;
    
    private final Path replayLogPath;
    private final Disruptor<OrderEvent> replayDisruptor;
    private final RingBuffer<OrderEvent> replayBuffer;
    private final AtomicBoolean replaying;
    private final AtomicLong eventsReplayed;
    private final AtomicLong replayStartTimeNanos;
    private final EventHandler<OrderEvent> replayHandler;
    private final ConcurrentSkipListMap<Instant, ReplayableEvent> eventIndex;
    private final ExecutorService replayExecutor;
    private volatile boolean running;
    
    public DeterministicReplay(Path replayLogPath, EventHandler<OrderEvent> handler) {
        this.replayLogPath = replayLogPath;
        this.replayHandler = handler;
        this.replaying = new AtomicBoolean(false);
        this.eventsReplayed = new AtomicLong(0);
        this.replayStartTimeNanos = new AtomicLong(0);
        this.eventIndex = new ConcurrentSkipListMap<>();
        this.replayExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "deterministic-replay-executor");
            t.setDaemon(true);
            return t;
        });
        
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "replay-disruptor-" + counter.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };
        
        this.replayDisruptor = new Disruptor<>(
            new OrderEventFactory(),
            RING_BUFFER_SIZE,
            threadFactory,
            ProducerType.SINGLE,
            new BlockingWaitStrategy()
        );
        this.replayBuffer = replayDisruptor.getRingBuffer();
    }
    
    public void startReplay(Instant from, Instant to) {
        if (!replaying.compareAndSet(false, true)) {
            throw new IllegalStateException("Replay ya está en ejecución");
        }
        log.info("Iniciando replay determinístico desde {} hasta {}", from, to);
        running = true;
        
        replayDisruptor.handleEventsWith(replayHandler);
        replayDisruptor.start();
        
        replayExecutor.submit(() -> executeReplay(from, to));
    }
    
    private void executeReplay(Instant from, Instant to) {
        try {
            List<ReplayableEvent> events = loadEventsFromLog(from, to);
            if (events.isEmpty()) {
                log.warn("No se encontraron eventos para el rango especificado");
                return;
            }
            
            log.info("Cargados {} eventos para replay", events.size());
            replayStartTimeNanos.set(System.nanoTime());
            
            long baseTimestamp = events.get(0).timestampNanos;
            
            for (ReplayableEvent event : events) {
                if (!running) {
                    log.info("Replay detenido manualmente");
                    break;
                }
                
                long relativeTimeNanos = event.timestampNanos - baseTimestamp;
                long targetElapsed = relativeTimeNanos;
                long actualElapsed = System.nanoTime() - replayStartTimeNanos.get();
                
                if (targetElapsed > actualElapsed) {
                    long sleepNanos = targetElapsed - actualElapsed;
                    if (sleepNanos > 1_000_000) {
                        Thread.sleep(sleepNanos / 1_000_000, (int) (sleepNanos % 1_000_000));
                    } else {
                        Thread.yield();
                    }
                }
                
                publishEventToBuffer(event);
                eventsReplayed.incrementAndGet();
                
                if (eventsReplayed.get() % 1000 == 0) {
                    log.debug("Replayed {} eventos", eventsReplayed.get());
                }
            }
            
            log.info("Replay completado. Total eventos: {}", eventsReplayed.get());
        } catch (Exception e) {
            log.error("Error durante replay determinístico", e);
        } finally {
            completeReplay();
        }
    }
    
    private List<ReplayableEvent> loadEventsFromLog(Instant from, Instant to) throws IOException {
        List<ReplayableEvent> events = new ArrayList<>();
        
        if (!Files.exists(replayLogPath)) {
            log.warn("Archivo de log de replay no encontrado: {}", replayLogPath);
            return events;
        }
        
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(replayLogPath))) {
            byte[] magic = new byte[MAGIC_LENGTH];
            dis.readFully(magic);
            String magicStr = new String(magic);
            if (!EVENT_LOG_MAGIC.equals(magicStr)) {
                throw new IOException("Formato de log inválido: magic incorrecto");
            }
            
            dis.readFully(new byte[VERSION_LENGTH]);
            long startTimeNanos = dis.readLong();
            long endTimeNanos = dis.readLong();
            
            log.debug("Log couvre desde {} hasta {}", 
                Instant.ofEpochSecond(0, startTimeNanos), 
                Instant.ofEpochSecond(0, endTimeNanos));
            
            while (dis.available() > 0) {
                ReplayableEvent event = readEvent(dis);
                if (event != null) {
                    Instant eventInstant = Instant.ofEpochSecond(0, event.timestampNanos);
                    if (!eventInstant.isBefore(from) && !eventInstant.isAfter(to)) {
                        events.add(event);
                        eventIndex.put(eventInstant, event);
                    }
                }
            }
        }
        
        events.sort(Comparator.comparingLong(e -> e.timestampNanos));
        return events;
    }
    
    private ReplayableEvent readEvent(DataInputStream dis) throws IOException {
        try {
            long timestampNanos = dis.readLong();
            byte eventType = dis.readByte();
            int payloadLength = dis.readInt();
            byte[] payload = new byte[payloadLength];
            dis.readFully(payload);
            
            return new ReplayableEvent(timestampNanos, eventType, payload);
        } catch (EOFException e) {
            return null;
        }
    }
    
    private void publishEventToBuffer(ReplayableEvent event) {
        long sequence = replayBuffer.next();
        try {
            OrderEvent orderEvent = replayBuffer.get(sequence);
            orderEvent.setOrderId(new String(event.payload, 0, Math.min(36, event.payload.length)));
            orderEvent.setReceivedTimeNanos(event.timestampNanos);
        } finally {
            replayBuffer.publish(sequence);
        }
    }
    
    public void recordEvent(long timestampNanos, byte eventType, byte[] payload) {
        if (!running) {
            throw new IllegalStateException("Recorder no está inicializado");
        }
        ReplayableEvent event = new ReplayableEvent(timestampNanos, eventType, payload);
        eventIndex.put(Instant.ofEpochSecond(0, timestampNanos), event);
    }
    
    public void writeEventLog(Path outputPath) throws IOException {
        log.info("Escribiendo log de eventos a {}", outputPath);
        
        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(outputPath))) {
            dos.writeBytes(EVENT_LOG_MAGIC);
            dos.writeBytes("0001");
            
            long firstTimestamp = eventIndex.isEmpty() ? System.nanoTime() : 
                eventIndex.firstKey().toEpochMilli() * 1_000_000;
            long lastTimestamp = eventIndex.isEmpty() ? firstTimestamp : 
                eventIndex.lastKey().toEpochMilli() * 1_000_000;
            
            dos.writeLong(firstTimestamp);
            dos.writeLong(lastTimestamp);
            
            for (var entry : eventIndex.entrySet()) {
                ReplayableEvent event = entry.getValue();
                dos.writeLong(event.timestampNanos);
                dos.writeByte(event.eventType);
                dos.writeInt(event.payload.length);
                dos.write(event.payload);
            }
        }
        
        log.info("Log escrito con {} eventos", eventIndex.size());
    }
    
    public void stopReplay() {
        running = false;
        log.info("Solicitando parada de replay");
    }
    
    private void completeReplay() {
        if (replaying.compareAndSet(true, false)) {
            replayDisruptor.shutdown(5, TimeUnit.SECONDS);
            log.info("Replay detenido. Eventos procesados: {}", eventsReplayed.get());
        }
    }
    
    public long getEventsReplayedCount() {
        return eventsReplayed.get();
    }
    
    public boolean isReplaying() {
        return replaying.get();
    }
    
    public Map<Instant, ReplayableEvent> getEventIndex() {
        return Collections.unmodifiableMap(eventIndex);
    }
    
    public void shutdown() {
        stopReplay();
        replayExecutor.shutdown();
        try {
            if (!replayExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                replayExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            replayExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public static class ReplayableEvent {
        public final long timestampNanos;
        public final byte eventType;
        public final byte[] payload;
        
        public ReplayableEvent(long timestampNanos, byte eventType, byte[] payload) {
            this.timestampNanos = timestampNanos;
            this.eventType = eventType;
            this.payload = payload;
        }
    }
    
    private static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }
}