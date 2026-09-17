package com.pragma.riskengine;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import com.pragma.riskengine.disruptor.OrderEventHandler;
import com.pragma.riskengine.model.VaRModel;
import com.pragma.riskengine.model.VaRModel.WeightingScheme;
import com.pragma.riskengine.model.VaRModel.Position;
import com.pragma.riskengine.var.VaRCalculator;
import com.pragma.riskengine.limits.LimitService;
import com.pragma.riskengine.circuitbreaker.DynamicCircuitBreaker;
import com.pragma.riskengine.circuitbreaker.CircuitBreakerConfig;
import com.pragma.riskengine.killswitch.KillSwitchPolicy;
import com.pragma.riskengine.compliance.MiFIDIITracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RiskEngineMain {
    private static final Logger logger = LoggerFactory.getLogger(RiskEngineMain.class);
    private static final int RING_BUFFER_SIZE = 1 << 16;
    private static final int WORKER_THREADS = 8;
    private static final long STARTUP_TIMEOUT_SECONDS = 30;

    private final Disruptor<OrderEvent> disruptor;
    private final VaRModel varModel;
    private final VaRCalculator varCalculator;
    private final LimitService limitService;
    private final DynamicCircuitBreaker circuitBreaker;
    private final KillSwitchPolicy killSwitchPolicy;
    private final MiFIDIITracer tracer;
    private final AtomicBoolean running;
    private final ExecutorService executor;

    public RiskEngineMain() {
        this.running = new AtomicBoolean(false);
        this.executor = createWorkerExecutor();
        this.varModel = createVaRModel();
        this.varCalculator = new VaRCalculator(varModel);
        this.limitService = new LimitService();
        this.circuitBreaker = createCircuitBreaker();
        this.killSwitchPolicy = new KillSwitchPolicy();
        this.tracer = new MiFIDIITracer();
        this.disruptor = createDisruptor();
    }

    private VaRModel createVaRModel() {
        return new VaRModel(
            252,
            0.99,
            1440,
            0.94,
            30,
            WeightingScheme.EWMA
        );
    }

    private DynamicCircuitBreaker createCircuitBreaker() {
        CircuitBreakerConfig config = new CircuitBreakerConfig(
            100,
            0.5,
            30_000_000_000L,
            0.7,
            0.95,
            5
        );
        return new DynamicCircuitBreaker("risk-engine-cb", config);
    }

    private Disruptor<OrderEvent> createDisruptor() {
        WaitStrategy waitStrategy = new BlockingWaitStrategy();
        
        RingBuffer<OrderEvent> ringBuffer = RingBuffer.create(
            ProducerType.MULTI,
            OrderEvent::new,
            RING_BUFFER_SIZE,
            waitStrategy
        );

        SequenceBarrier barrier = ringBuffer.newBarrier();
        
        OrderEventHandler[] handlers = createEventHandlers();
        
        WorkerPool<OrderEvent> workerPool = new WorkerPool<>(
            ringBuffer,
            barrier,
            new IgnoreExceptionHandler(),
            handlers
        );

        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        
        return new Disruptor<>(
            new OrderEventFactory(),
            RING_BUFFER_SIZE,
            executor,
            ProducerType.MULTI,
            waitStrategy
        );
    }

    private OrderEventHandler[] createEventHandlers() {
        OrderEventHandler[] handlers = new OrderEventHandler[WORKER_THREADS];
        for (int i = 0; i < WORKER_THREADS; i++) {
            handlers[i] = new OrderEventHandler(
                varCalculator,
                limitService,
                circuitBreaker,
                killSwitchPolicy,
                tracer
            );
        }
        return handlers;
    }

    private ExecutorService createWorkerExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int counter = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "risk-engine-worker-" + counter++);
                t.setDaemon(true);
                t.setPriority(Thread.MAX_PRIORITY);
                return t;
            }
        };
        
        return new ThreadPoolExecutor(
            WORKER_THREADS,
            WORKER_THREADS * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void start() {
        if (!running.compareAndSet(false, true)) {
            logger.warn("Risk Engine ya está en ejecución");
            return;
        }

        disruptor.start();
        logger.info("Risk Engine iniciado con {} workers y buffer de {} eventos", 
            WORKER_THREADS, RING_BUFFER_SIZE);
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) {
            logger.warn("Risk Engine no está en ejecución");
            return;
        }

        logger.info("Deteniendo Risk Engine...");
        disruptor.shutdown(10, TimeUnit.SECONDS);
        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logger.info("Risk Engine detenido");
    }

    public void publishOrder(String traderId, String strategyId, String instrumentId,
                             String orderId, double quantity, double price, String side) {
        if (!running.get()) {
            logger.warn("Intento de publicar orden con engine detenido");
            return;
        }

        long sequence = disruptor.next();
        try {
            OrderEvent event = disruptor.get(sequence);
            event.setTraderId(traderId);
            event.setStrategyId(strategyId);
            event.setInstrumentId(instrumentId);
            event.setOrderId(orderId);
            event.setQuantity(quantity);
            event.setPrice(price);
            event.setSide(side);
            event.setReceivedTimeNanos(System.nanoTime());
        } finally {
            disruptor.publish(sequence);
        }
    }

    public VaRModel getVarModel() {
        return varModel;
    }

    public DynamicCircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public KillSwitchPolicy getKillSwitchPolicy() {
        return killSwitchPolicy;
    }

    public boolean isRunning() {
        return running.get();
    }

    public static void main(String[] args) {
        logger.info("Inicializando Risk Engine...");
        
        RiskEngineMain engine = new RiskEngineMain();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Signal de shutdown recibido");
            engine.stop();
        }));

        engine.start();

        engine.publishOrder("TRADER-001", "STRAT-ALPHA", "AAPL", 
            "ORD-001", 1000, 150.50, "BUY");
        engine.publishOrder("TRADER-002", "STRAT-BETA", "GOOGL", 
            "ORD-002", 500, 2800.00, "SELL");

        logger.info("Ordenes de prueba publicadas");
    }

    private static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }

    private static class IgnoreExceptionHandler implements ExceptionHandler<OrderEvent> {
        private static final Logger logger = LoggerFactory.getLogger(IgnoreExceptionHandler.class);
        
        @Override
        public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
            logger.error("Excepción procesando evento {}: {}", sequence, ex.getMessage());
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            logger.error("Error al iniciar disruptor: {}", ex.getMessage());
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            logger.error("Error al detener disruptor: {}", ex.getMessage());
        }
    }
}