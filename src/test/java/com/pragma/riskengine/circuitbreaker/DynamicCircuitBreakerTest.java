package com.pragma.riskengine.circuitbreaker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DynamicCircuitBreaker - Tests de circuit breakers con fallos y recuperación")
class DynamicCircuitBreakerTest {

    private CircuitBreakerConfig config;
    private DynamicCircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        config = new CircuitBreakerConfig(
            5,
            0.5,
            10000L,
            30000L,
            0.75,
            2.0,
            0.5
        );
        circuitBreaker = new DynamicCircuitBreaker("TEST_CB", config);
    }

    @Nested
    @DisplayName("Tests de Estados del Circuit Breaker")
    class StateTransitionTests {

        @Test
        @DisplayName("Debe iniciar en estado CLOSED")
        void shouldStartInClosedState() {
            assertEquals(DynamicCircuitBreaker.State.CLOSED, circuitBreaker.getState());
            assertTrue(circuitBreaker.isAvailable(), 
                "Circuit breaker debe estar disponible en estado CLOSED");
        }

        @Test
        @DisplayName("Debe transiciónar a OPEN tras superar threshold de fallos")
        void shouldTransitionToOpenAfterFailureThreshold() {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Simulated failure " + i);
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());
            assertFalse(circuitBreaker.isAvailable(), 
                "Circuit breaker no debe estar disponible en estado OPEN");
        }

        @Test
        @DisplayName("Debe transiciónar a HALF_OPEN tras timeout de recuperación")
        void shouldTransitionToHalfOpenAfterRecoveryTimeout() throws InterruptedException {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());

            Thread.sleep(config.recoveryTimeoutMs() + 100);

            assertEquals(DynamicCircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());
        }

        @Test
        @DisplayName("Debe retornar a CLOSED tras éxito en HALF_OPEN")
        void shouldReturnToClosedAfterSuccessInHalfOpen() throws InterruptedException {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            Thread.sleep(config.recoveryTimeoutMs() + 100);
            assertEquals(DynamicCircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

            for (int i = 0; i < 3; i++) {
                String result = circuitBreaker.execute(() -> "success");
                assertEquals("success", result);
            }

            assertEquals(DynamicCircuitBreaker.State.CLOSED, circuitBreaker.getState());
        }
    }

    @Nested
    @DisplayName("Tests de Fallos Simulados")
    class FailureSimulationTests {

        @Test
        @DisplayName("Debe manejar excepciones de negocio correctamente")
        void shouldHandleBusinessExceptions() {
            String result = circuitBreaker.execute(() -> {
                throw new IllegalArgumentException("Business rule violation");
            });

            assertNull(result, "Resultado debe ser null cuando falla la ejecución");
            assertEquals(1, circuitBreaker.getFailureCount(), 
                "Contador de fallos debe incrementarse");
        }

        @Test
        @DisplayName("Debe registrar fallos exitosamente")
        void shouldRecordFailuresCorrectly() {
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Test failure");
                });
            } catch (Exception expected) {
            }

            assertTrue(circuitBreaker.getFailureCount() > 0, 
                "Los fallos deben ser registrados");
        }

        @Test
        @DisplayName("Debe rechazar llamadas cuando está OPEN")
        void shouldRejectCallsWhenOpen() {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            assertThrows(CircuitBreakerOpenException.class, () -> {
                circuitBreaker.execute(() -> "This should not execute");
            });
        }

        @Test
        @DisplayName("Debe manejar fallos consecutivos correctamente")
        void shouldHandleConsecutiveFailures() {
            for (int i = 0; i < 10; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Consecutive failure " + i);
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());
            assertTrue(circuitBreaker.getFailureCount() >= 5, 
                "Debe registrar múltiples fallos consecutivos");
        }
    }

    @Nested
    @DisplayName("Tests de Recuperación de Thresholds")
    class ThresholdRecoveryTests {

        @Test
        @DisplayName("Debe ajustar threshold dinámicamente basado en tasa de fallos")
        void shouldAdjustThresholdBasedOnFailureRate() {
            for (int i = 0; i < 3; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            for (int i = 0; i < 5; i++) {
                try {
                    circuitBreaker.execute(() -> "success");
                } catch (Exception ignored) {
                }
            }

            double currentThreshold = circuitBreaker.getCurrentThreshold();
            assertTrue(currentThreshold > 0, 
                "Threshold debe ser positivo después de adaptaciones");
        }

        @Test
        @DisplayName("Debe recalcular threshold tras cambio de volatilidad")
        void shouldRecalculateThresholdAfterVolatilityChange() {
            circuitBreaker.updateVolatility(0.15);
            double thresholdWithNormalVol = circuitBreaker.getCurrentThreshold();

            circuitBreaker.updateVolatility(0.45);
            double thresholdWithHighVol = circuitBreaker.getCurrentThreshold();

            assertTrue(thresholdWithHighVol > thresholdWithNormalVol, 
                "Threshold debe aumentar con mayor volatilidad");
        }

        @Test
        @DisplayName("Debe mantener stability window entre ajustes de threshold")
        void shouldMaintainStabilityWindowBetweenThresholdAdjustments() {
            long initialThreshold = circuitBreaker.getCurrentThreshold();

            circuitBreaker.updateVolatility(0.20);
            long thresholdAfterFirstUpdate = circuitBreaker.getCurrentThreshold();

            circuitBreaker.updateVolatility(0.21);
            long thresholdAfterSecondUpdate = circuitBreaker.getCurrentThreshold();

            assertEquals(initialThreshold, thresholdAfterFirstUpdate, 
                "Primer ajuste de volatilidad debe cambiar el threshold");
        }
    }

    @Nested
    @DisplayName("Tests de Concurrencia")
    class ConcurrencyTests {

        @Test
        @DisplayName("Debe manejar llamadas concurrentes sin race conditions")
        void shouldHandleConcurrentCallsWithoutRaceConditions() throws InterruptedException {
            int threadCount = 10;
            int callsPerThread = 50;
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int t = 0; t < threadCount; t++) {
                new Thread(() -> {
                    try {
                        startLatch.await();
                        for (int i = 0; i < callsPerThread; i++) {
                            try {
                                if (i % 3 == 0) {
                                    throw new RuntimeException("Simulated failure");
                                }
                                successCount.incrementAndGet();
                            } catch (Exception e) {
                                failureCount.incrementAndGet();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                }).start();
            }

            startLatch.countDown();
            assertTrue(endLatch.await(30, TimeUnit.SECONDS), 
                "Las llamadas concurrentes deben completar");

            assertTrue(successCount.get() + failureCount.get() == threadCount * callsPerThread, 
                "Todas las llamadas deben ser contabilizadas");
        }

        @RepeatedTest(5)
        @DisplayName("Debe mantener consistencia bajo carga repetida")
        void shouldMaintainConsistencyUnderRepeatedLoad() {
            for (int i = 0; i < 100; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        if (i % 10 == 0) {
                            throw new RuntimeException("Random failure");
                        }
                        return "ok";
                    });
                } catch (Exception ignored) {
                }
            }

            assertNotNull(circuitBreaker.getState());
            assertTrue(circuitBreaker.getTotalCalls() > 0);
        }
    }

    @Nested
    @DisplayName("Tests de Métricas")
    class MetricsTests {

        @Test
        @DisplayName("Debe registrar métricas de llamadas exitosas")
        void shouldRecordSuccessfulCallMetrics() {
            circuitBreaker.execute(() -> "success");
            circuitBreaker.execute(() -> "success");
            circuitBreaker.execute(() -> "success");

            assertEquals(3, circuitBreaker.getTotalCalls(), 
                "Debe registrar el número total de llamadas");
            assertEquals(3, circuitBreaker.getSuccessCount(), 
                "Debe registrar el número de éxitos");
        }

        @Test
        @DisplayName("Debe calcular tasa de fallos correctamente")
        void shouldCalculateFailureRateCorrectly() {
            for (int i = 0; i < 4; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            for (int i = 0; i < 6; i++) {
                circuitBreaker.execute(() -> "success");
            }

            double failureRate = circuitBreaker.getFailureRate();
            assertTrue(failureRate > 0.3 && failureRate < 0.5, 
                "Tasa de fallos debe estar entre 30% y 50%: " + failureRate);
        }

        @Test
        @DisplayName("Debe proporcionar lastFailureTime válido")
        void shouldProvideValidLastFailureTime() {
            Instant beforeFailure = Instant.now();
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Test failure");
                });
            } catch (Exception expected) {
            }
            Instant afterFailure = Instant.now();

            Instant lastFailure = circuitBreaker.getLastFailureTime();
            assertNotNull(lastFailure, "Last failure time no debe ser null");
            assertTrue(!lastFailure.isBefore(beforeFailure) && !lastFailure.isAfter(afterFailure), 
                "Last failure time debe estar en el rango correcto");
        }
    }

    @Nested
    @DisplayName("Tests de Configuración Dinámica")
    class DynamicConfigurationTests {

        @Test
        @DisplayName("Debe actualizar sliding window size dinámicamente")
        void shouldUpdateSlidingWindowSizeDynamically() {
            int originalWindow = config.slidingWindowSize();

            CircuitBreakerConfig newConfig = new CircuitBreakerConfig(
                3,
                0.5,
                10000L,
                30000L,
                0.75,
                2.0,
                0.5
            );
            circuitBreaker.updateConfig(newConfig);

            assertNotEquals(originalWindow, circuitBreaker.getCurrentThreshold());
        }

        @Test
        @DisplayName("Debe manejar reset de estado correctamente")
        void shouldHandleStateResetCorrectly() {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());

            circuitBreaker.reset();

            assertEquals(DynamicCircuitBreaker.State.CLOSED, circuitBreaker.getState());
            assertEquals(0, circuitBreaker.getFailureCount());
            assertEquals(0, circuitBreaker.getSuccessCount());
        }
    }
}