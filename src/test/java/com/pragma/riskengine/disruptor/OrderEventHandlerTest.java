package com.pragma.riskengine.disruptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("OrderEventHandler - Tests de latencia y throughput")
class OrderEventHandlerTest {

    private OrderEventHandler handler;
    private OrderEvent event;
    private static final String TEST_INSTRUMENT = "AAPL";
    private static final String TEST_TRADER = "TRADER_001";
    private static final String TEST_STRATEGY = "MOMENTUM";

    @BeforeEach
    void setUp() {
        handler = new OrderEventHandler();
        event = new OrderEvent(
            "order_001",
            TEST_INSTRUMENT,
            TEST_TRADER,
            TEST_STRATEGY,
            "BUY",
            1000,
            150.25,
            Instant.now()
        );
    }

    @Test
    @DisplayName("Debe procesar evento de orden correctamente")
    void onEvent_shouldProcessOrderEventSuccessfully() {
        long initialSequence = 0L;
        boolean endOfBatch = true;

        handler.onEvent(event, initialSequence, endOfBatch);

        assertNotNull(event);
        assertEquals("order_001", event.orderId());
        assertEquals(TEST_INSTRUMENT, event.instrumentId());
    }

    @Test
    @DisplayName("Debe manejar eventos de compra y venta")
    void onEvent_shouldHandleBothBuyAndSellOrders() {
        OrderEvent buyEvent = new OrderEvent(
            "order_buy", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "BUY", 500, 150.00, Instant.now()
        );
        OrderEvent sellEvent = new OrderEvent(
            "order_sell", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "SELL", 500, 151.00, Instant.now()
        );

        handler.onEvent(buyEvent, 0L, true);
        handler.onEvent(sellEvent, 1L, true);

        assertEquals("BUY", buyEvent.side());
        assertEquals("SELL", sellEvent.side());
    }

    @Test
    @DisplayName("Debe validar quantities positivos")
    void onEvent_shouldRejectZeroOrNegativeQuantities() {
        OrderEvent zeroQtyEvent = new OrderEvent(
            "order_zero", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "BUY", 0, 150.00, Instant.now()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            handler.onEvent(zeroQtyEvent, 0L, true);
        });
    }

    @RepeatedTest(100)
    @DisplayName("Debe mantener latencia consistente bajo carga repetida")
    void onEvent_shouldMaintainConsistentLatencyUnderRepeatedLoad() {
        long startTime = System.nanoTime();
        handler.onEvent(event, 0L, true);
        long endTime = System.nanoTime();
        long latencyNanos = endTime - startTime;

        assertTrue(latencyNanos < 1_000_000, 
            "Latencia debe ser menor a 1ms, pero fue: " + latencyNanos + " ns");
    }

    @Test
    @DisplayName("Debe medir throughput con múltiples eventos Concurrentes")
    void shouldMeasureThroughputWithMultipleConcurrentEvents() throws InterruptedException {
        int eventCount = 10_000;
        CountDownLatch latch = new CountDownLatch(1);
        AtomicLong totalTime = new AtomicLong(0);
        AtomicReference<Exception> exceptionRef = new AtomicReference<>();

        Thread producerThread = new Thread(() -> {
            try {
                long start = System.nanoTime();
                for (int i = 0; i < eventCount; i++) {
                    OrderEvent ev = new OrderEvent(
                        "order_" + i,
                        TEST_INSTRUMENT,
                        TEST_TRADER,
                        TEST_STRATEGY,
                        i % 2 == 0 ? "BUY" : "SELL",
                        100 + (i % 500),
                        150.0 + (i % 100) * 0.01,
                        Instant.now()
                    );
                    handler.onEvent(ev, i, i == eventCount - 1);
                }
                totalTime.set(System.nanoTime() - start);
                latch.countDown();
            } catch (Exception e) {
                exceptionRef.set(e);
                latch.countDown();
            }
        });

        producerThread.start();
        boolean completed = latch.await(30, TimeUnit.SECONDS);

        assertTrue(completed, "El procesamiento no completó en 30 segundos");
        assertNull(exceptionRef.get(), "Excepción durante el procesamiento: " + exceptionRef.get());

        long totalTimeMs = totalTime.get() / 1_000_000;
        double throughput = (double) eventCount / totalTimeMs * 1000;

        assertTrue(throughput > 100_000, 
            "Throughput debe superar 100K eventos/segundo, pero fue: " + 
            String.format("%.2f", throughput));
    }

    @Test
    @DisplayName("Debe manejar eventos con timestamps en nanosegundos")
    void onEvent_shouldHandleNanosTimestampPrecision() {
        Instant preciseTimestamp = Instant.now();
        OrderEvent preciseEvent = new OrderEvent(
            "order_precise", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "BUY", 100, 150.50, preciseTimestamp
        );

        handler.onEvent(preciseEvent, 0L, true);

        assertNotNull(preciseEvent.timestamp());
        assertTrue(preciseEvent.timestamp().getEpochSecond() > 0);
    }

    @Test
    @DisplayName("Debe validar side de orden válido")
    void onEvent_shouldRejectInvalidSide() {
        OrderEvent invalidSideEvent = new OrderEvent(
            "order_invalid", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "INVALID_SIDE", 100, 150.00, Instant.now()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            handler.onEvent(invalidSideEvent, 0L, true);
        });
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    @DisplayName("Debe procesar eventos concurrentemente sin race conditions")
    void onEvent_shouldHandleConcurrentEventsWithoutRaceConditions() throws InterruptedException {
        int threads = 4;
        int eventsPerThread = 2500;
        CountDownLatch barrier = new CountDownLatch(threads);
        AtomicReference<Exception> error = new AtomicReference<>();

        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            new Thread(() -> {
                try {
                    for (int i = 0; i < eventsPerThread; i++) {
                        OrderEvent ev = new OrderEvent(
                            "order_t" + threadId + "_i" + i,
                            TEST_INSTRUMENT + "_" + (i % 10),
                            TEST_TRADER + "_" + threadId,
                            TEST_STRATEGY,
                            i % 2 == 0 ? "BUY" : "SELL",
                            100 + i,
                            150.0 + i,
                            Instant.now()
                        );
                        handler.onEvent(ev, i, i == eventsPerThread - 1);
                    }
                } catch (Exception e) {
                    error.set(e);
                } finally {
                    barrier.countDown();
                }
            }).start();
        }

        assertTrue(barrier.await(60, TimeUnit.SECONDS), "Timeout en procesamiento concurrente");
        assertNull(error.get(), "Race condition detectada: " + error.get());
    }
}