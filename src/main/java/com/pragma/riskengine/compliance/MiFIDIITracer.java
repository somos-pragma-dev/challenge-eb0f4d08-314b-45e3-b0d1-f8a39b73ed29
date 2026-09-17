package com.pragma.riskengine.compliance;

import com.pragma.riskengine.disruptor.OrderEvent;
import com.pragma.riskengine.disruptor.MarketDataEvent;
import com.pragma.riskengine.model.VaRModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class MiFIDIITracer {
    private static final Logger log = LoggerFactory.getLogger(MiFIDIITracer.class);
    private static final String COMPLIANCE_LOG_PREFIX = "mifidi_trace_";
    private static final String COMPLIANCE_LOG_EXT = ".log";
    private static final int MAX_TRACE_ENTRIES = 1_000_000;
    private static final int FLUSH_INTERVAL_MS = 5000;
    private static final int MAX_PAYLOAD_SIZE = 4096;
    
    private final Path traceLogPath;
    private final ConcurrentLinkedQueue<TraceEntry> traceBuffer;
    private final ConcurrentHashMap<String, DecisionContext> activeContexts;
    private final ScheduledExecutorService flushExecutor;
    private final AtomicLong traceSequence;
    private final AtomicBoolean running;
    private final VaRModel varModel;
    private final MarketDataSnapshot lastMarketSnapshot;
    private PrintWriter traceWriter;
    private volatile long lastFlushTime;
    
    public MiFIDIITracer(Path traceLogDir, VaRModel varModel) {
        this.traceLogDir = traceLogDir;
        this.varModel = varModel;
        this.traceBuffer = new ConcurrentLinkedQueue<>();
        this.activeContexts = new ConcurrentHashMap<>();
        this.traceSequence = new AtomicLong(0);
        this.running = new AtomicBoolean(false);
        this.lastMarketSnapshot = new MarketDataSnapshot();
        
        this.traceLogPath = traceLogDir.resolve(
            COMPLIANCE_LOG_PREFIX + 
            LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
            COMPLIANCE_LOG_EXT
        );
        
        this.flushExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "mifidi-flush-executor");
            t.setDaemon(true);
            return t;
        });
    }
    
    private final Path traceLogDir;
    
    public void initialize() throws IOException {
        Files.createDirectories(traceLogDir);
        traceWriter = new PrintWriter(new BufferedWriter(
            new FileWriter(traceLogPath.toFile()), 8192
        ));
        running.set(true);
        
        flushExecutor.scheduleAtFixedRate(
            this::flushBuffer,
            FLUSH_INTERVAL_MS,
            FLUSH_INTERVAL_MS,
            TimeUnit.MILLISECONDS
        );
        
        log.info("MiFID II Tracer inicializado. Log: {}", traceLogPath);
    }
    
    public String beginRiskDecision(String orderId, String traderId, String instrumentId, 
                                    double notionalValue, String decisionType) {
        String contextId = UUID.randomUUID().toString();
        long timestampNanos = System.nanoTime();
        
        DecisionContext context = new DecisionContext(
            contextId,
            orderId,
            traderId,
            instrumentId,
            notionalValue,
            decisionType,
            timestampNanos,
            captureMarketContext(instrumentId)
        );
        
        activeContexts.put(contextId, context);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "DECISION_START",
            context.toMap()
        );
        traceBuffer.offer(entry);
        
        log.debug("Inicio de decisión de riesgo: contextId={}, orderId={}", contextId, orderId);
        return contextId;
    }
    
    public void recordRiskCheck(String contextId, String checkType, boolean passed, 
                                double measuredValue, double threshold, String details) {
        DecisionContext ctx = activeContexts.get(contextId);
        if (ctx == null) {
            log.warn("Contexto no encontrado: {}", contextId);
            return;
        }
        
        long timestampNanos = System.nanoTime();
        Map<String, Object> data = new HashMap<>();
        data.put("contextId", contextId);
        data.put("checkType", checkType);
        data.put("passed", passed);
        data.put("measuredValue", measuredValue);
        data.put("threshold", threshold);
        data.put("details", details);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "RISK_CHECK",
            data
        );
        traceBuffer.offer(entry);
        
        ctx.addRiskCheck(checkType, passed, measuredValue);
    }
    
    public void completeRiskDecision(String contextId, boolean approved, String reason) {
        DecisionContext ctx = activeContexts.remove(contextId);
        if (ctx == null) {
            log.warn("Contexto no encontrado al completar: {}", contextId);
            return;
        }
        
        long timestampNanos = System.nanoTime();
        ctx.complete(approved, reason, timestampNanos);
        
        Map<String, Object> data = new HashMap<>(ctx.toMap());
        data.put("reason", reason);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            approved ? "DECISION_APPROVED" : "DECISION_REJECTED",
            data
        );
        traceBuffer.offer(entry);
        
        log.info("Decisión de riesgo completada: contextId={}, approved={}, reason={}", 
            contextId, approved, reason);
    }
    
    public void recordCircuitBreakerEvent(String instrumentId, String state, 
                                          String reason, double currentExposure, double threshold) {
        long timestampNanos = System.nanoTime();
        
        Map<String, Object> data = new HashMap<>();
        data.put("instrumentId", instrumentId);
        data.put("state", state);
        data.put("reason", reason);
        data.put("currentExposure", currentExposure);
        data.put("threshold", threshold);
        data.put("timestampNanos", timestampNanos);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "CIRCUIT_BREAKER_EVENT",
            data
        );
        traceBuffer.offer(entry);
    }
    
    public void recordKillSwitchEvent(String traderId, String strategyId, 
                                      String triggerReason, Map<String, Object> metadata) {
        long timestampNanos = System.nanoTime();
        
        Map<String, Object> data = new HashMap<>();
        data.put("traderId", traderId);
        data.put("strategyId", strategyId);
        data.put("triggerReason", triggerReason);
        data.put("timestampNanos", timestampNanos);
        if (metadata != null) {
            data.putAll(metadata);
        }
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "KILL_SWITCH_TRIGGERED",
            data
        );
        traceBuffer.offer(entry);
        
        log.warn("Kill switch activado: traderId={}, reason={}", traderId, triggerReason);
    }
    
    public void recordMarketDataContext(String instrumentId, double bid, double ask, 
                                        long bidSize, long askSize, double volatility) {
        lastMarketSnapshot.update(instrumentId, bid, ask, bidSize, askSize, volatility);
        
        long timestampNanos = System.nanoTime();
        
        Map<String, Object> data = new HashMap<>();
        data.put("instrumentId", instrumentId);
        data.put("bid", bid);
        data.put("ask", ask);
        data.put("bidSize", bidSize);
        data.put("askSize", askSize);
        data.put("volatility", volatility);
        data.put("timestampNanos", timestampNanos);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "MARKET_CONTEXT",
            data
        );
        traceBuffer.offer(entry);
    }
    
    private MarketContext captureMarketContext(String instrumentId) {
        double volatility = 0.0;
        try {
            volatility = varModel.getCurrentVolatility(instrumentId);
        } catch (Exception e) {
            log.debug("No se pudo obtener volatilidad para {}", instrumentId);
        }
        
        MarketDataSnapshot snapshot = lastMarketSnapshot.getSnapshot(instrumentId);
        return new MarketContext(
            snapshot.bid,
            snapshot.ask,
            snapshot.bidSize,
            snapshot.askSize,
            volatility,
            System.nanoTime()
        );
    }
    
    private void flushBuffer() {
        if (traceBuffer.isEmpty()) {
            return;
        }
        
        int flushed = 0;
        StringBuilder batch = new StringBuilder();
        
        TraceEntry entry;
        while ((entry = traceBuffer.poll()) != null && flushed < 10000) {
            batch.append(formatTraceEntry(entry)).append("\n");
            flushed++;
        }
        
        if (batch.length() > 0 && traceWriter != null) {
            traceWriter.print(batch);
            traceWriter.flush();
            lastFlushTime = System.currentTimeMillis();
        }
        
        log.debug("Flushed {} entradas de trace", flushed);
    }
    
    private String formatTraceEntry(TraceEntry entry) {
        return String.format(
            "{\"seq\":%d,\"tsNanos\":%d,\"type\":\"%s\",\"data\":%s}",
            entry.sequence,
            entry.timestampNanos,
            entry.eventType,
            serializeData(entry.data)
        );
    }
    
    private String serializeData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> e : data.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(e.getKey()).append("\":");
            Object v = e.getValue();
            if (v instanceof Number) {
                sb.append(v);
            } else if (v instanceof String) {
                sb.append("\"").append(escapeJson((String)v)).append("\"");
            } else if (v instanceof Boolean) {
                sb.append(v);
            } else if (v == null) {
                sb.append("null");
            } else {
                sb.append("\"").append(escapeJson(v.toString())).append("\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    public void shutdown() {
        running.set(false);
        flushBuffer();
        
        flushExecutor.shutdown();
        try {
            if (!flushExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                flushExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            flushExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        if (traceWriter != null) {
            traceWriter.flush();
            traceWriter.close();
        }
        
        log.info("MiFID II Tracer cerrado. Trace entries: {}", traceSequence.get());
    }
    
    public long getTraceEntryCount() {
        return traceSequence.get();
    }
    
    public Path getTraceLogPath() {
        return traceLogPath;
    }
    
    public static class TraceEntry {
        public final long sequence;
        public final long timestampNanos;
        public final String eventType;
        public final Map<String, Object> data;
        
        public TraceEntry(long sequence, long timestampNanos, String eventType, 
                         Map<String, Object> data) {
            this.sequence = sequence;
            this.timestampNanos = timestampNanos;
            this.eventType = eventType;
            this.data = data;
        }
    }
    
    public static class DecisionContext {
        public final String contextId;
        public final String orderId;
        public final String traderId;
        public final String instrumentId;
        public final double notionalValue;
        public final String decisionType;
        public final long startTimestampNanos;
        public final MarketContext marketContext;
        public final List<RiskCheckResult> riskChecks;
        
        private volatile boolean approved;
        private volatile String reason;
        private volatile long completionTimestampNanos;
        
        public DecisionContext(String contextId, String orderId, String traderId, 
                              String instrumentId, double notionalValue, String decisionType,
                              long startTimestampNanos, MarketContext marketContext) {
            this.contextId = contextId;
            this.orderId = orderId;
            this.traderId = traderId;
            this.instrumentId = instrumentId;
            this.notionalValue = notionalValue;
            this.decisionType = decisionType;
            this.startTimestampNanos = startTimestampNanos;
            this.marketContext = marketContext;
            this.riskChecks = new CopyOnWriteArrayList<>();
        }
        
        public void addRiskCheck(String checkType, boolean passed, double value) {
            riskChecks.add(new RiskCheckResult(checkType, passed, value));
        }
        
        public void complete(boolean approved, String reason, long completionTimestampNanos) {
            this.approved = approved;
            this.reason = reason;
            this.completionTimestampNanos = completionTimestampNanos;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("contextId", contextId);
            map.put("orderId", orderId);
            map.put("traderId", traderId);
            map.put("instrumentId", instrumentId);
            map.put("notionalValue", notionalValue);
            map.put("decisionType", decisionType);
            map.put("startTimestampNanos", startTimestampNanos);
            map.put("approved", approved);
            map.put("reason", reason);
            map.put("completionTimestampNanos", completionTimestampNanos);
            map.put("durationNanos", completionTimestampNanos - startTimestampNanos);
            map.put("marketContext", marketContext.toMap());
            map.put("riskChecks", riskChecks.stream().map(RiskCheckResult::toMap).toList());
            return map;
        }
    }
    
    public static class RiskCheckResult {
        public final String checkType;
        public final boolean passed;
        public final double value;
        
        public RiskCheckResult(String checkType, boolean passed, double value) {
            this.checkType = checkType;
            this.passed = passed;
            this.value = value;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("checkType", checkType);
            map.put("passed", passed);
            map.put("value", value);
            return map;
        }
    }
    
    public static class MarketContext {
        public final double bid;
        public final double ask;
        public final long bidSize;
        public final long askSize;
        public final double volatility;
        public final long timestampNanos;
        
        public MarketContext(double bid, double ask, long bidSize, long askSize, 
                           double volatility, long timestampNanos) {
            this.bid = bid;
            this.ask = ask;
            this.bidSize = bidSize;
            this.askSize = askSize;
            this.volatility = volatility;
            this.timestampNanos = timestampNanos;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("bid", bid);
            map.put("ask", ask);
            map.put("bidSize", bidSize);
            map.put("askSize", askSize);
            map.put("volatility", volatility);
            map.put("timestampNanos", timestampNanos);
            return map;
        }
    }
    
    private static class MarketDataSnapshot {
        private final ConcurrentHashMap<String, SnapshotEntry> snapshots = new ConcurrentHashMap<>();
        
        public void update(String instrumentId, double bid, double ask, 
                         long bidSize, long askSize, double volatility) {
            snapshots.put(instrumentId, new SnapshotEntry(bid, ask, bidSize, askSize, volatility));
        }
        
        public SnapshotEntry getSnapshot(String instrumentId) {
            return snapshots.getOrDefault(instrumentId, 
                new SnapshotEntry(0.0, 0.0, 0, 0, 0.0));
        }
        
        public record SnapshotEntry(double bid, double ask, long bidSize, long askSize, double volatility) {}
    }
}