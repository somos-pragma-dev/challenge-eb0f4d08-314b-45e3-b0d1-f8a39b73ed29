package com.pragma.riskengine.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.StateTransition;
import io.github.resilience4j.circuitbreaker.CircuitBreakerTransitionListener;
import io.github.resilience4j.circuitbreaker.CircuitBreakerEvent;
import io.github.resilience4j.circuitbreaker.CircuitBreakerEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Circuit breaker dinámico con thresholds adaptativos basados en volatilidad del instrumento.
 * Implementa la estrategia de sharding por instrumento para evitar consenso distribuido.
 */
public class DynamicCircuitBreaker {
    private static final Logger log = LoggerFactory.getLogger(DynamicCircuitBreaker.class);
    
    private static final double DEFAULT_FAILURE_RATE_THRESHOLD = 50.0;
    private static final double DEFAULT_SLOW_CALL_RATE_THRESHOLD = 50.0;
    private static final int DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS = 1000;
    private static final int DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE = 10;
    private static final int DEFAULT_SLIDING_WINDOW_SIZE = 100;
    private static final double VOLATILITY_SCALING_FACTOR = 2.0;
    private static final double MIN_THRESHOLD = 20.0;
    private static final double MAX_THRESHOLD = 80.0;
    
    private final CircuitBreakerRegistry registry;
    private final Map<String, CircuitBreaker> circuitBreakers;
    private final Map<String, CircuitBreakerMetrics> metricsCache;
    private final AtomicReference<Instant> lastRecalculation;
    private final Duration recalculationInterval;
    private volatile double globalFailureThreshold;
    private volatile double globalSlowCallThreshold;
    
    public DynamicCircuitBreaker(CircuitBreakerConfig baseConfig) {
        this.circuitBreakers = new ConcurrentHashMap<>();
        this.metricsCache = new ConcurrentHashMap<>();
        this.lastRecalculation = new AtomicReference<>(Instant.now());
        this.recalculationInterval = Duration.ofSeconds(30);
        this.globalFailureThreshold = DEFAULT_FAILURE_RATE_THRESHOLD;
        this.globalSlowCallThreshold = DEFAULT_SLOW_CALL_RATE_THRESHOLD;
        
        CircuitBreakerConfig defaultConfig = buildDefaultConfig();
        this.registry = CircuitBreakerRegistry.of(defaultConfig);
        registerGlobalTransitionListener();
        log.info("DynamicCircuitBreaker inicializado con thresholds adaptativos");
    }
    
    private CircuitBreakerConfig buildDefaultConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold((float) DEFAULT_FAILURE_RATE_THRESHOLD)
                .slowCallRateThreshold((float) DEFAULT_SLOW_CALL_RATE_THRESHOLD)
                .slowCallDurationThreshold(Duration.ofMillis(DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS))
                .permittedNumberOfCallsInHalfOpenState(DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
                .minimumNumberOfCalls(10)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private void registerGlobalTransitionListener() {
        registry.getEventPublisher()
                .onStateTransition(this::handleStateTransition);
    }
    
    private void handleStateTransition(StateTransition transition) {
        String breakerName = transition.getStateTransitionEvent().getCircuitBreakerName();
        State fromState = transition.getFromState();
        State toState = transition.getToState();
        
        log.warn("CircuitBreaker '{}' transición: {} -> {}", 
                breakerName, fromState, toState);
        
        if (toState == State.OPEN) {
            notifyKillSwitchIfNeeded(breakerName);
        }
    }
    
    private void notifyKillSwitchIfNeeded(String breakerName) {
        log.error("Circuit breaker '{}' abierto - notificando al sistema de KillSwitch", breakerName);
    }
    
    /**
     * Obtiene o crea un circuit breaker para un instrumento específico.
     * Los thresholds se adaptan automáticamente según la volatilidad del instrumento.
     */
    public CircuitBreaker getCircuitBreaker(String instrumentId, double currentVolatility) {
        return circuitBreakers.computeIfAbsent(instrumentId, 
                id -> createCircuitBreakerForInstrument(id, currentVolatility));
    }
    
    private CircuitBreaker createCircuitBreakerForInstrument(String instrumentId, double volatility) {
        double adjustedFailureThreshold = calculateAdjustedThreshold(volatility, globalFailureThreshold);
        double adjustedSlowCallThreshold = calculateAdjustedThreshold(volatility, globalSlowCallThreshold);
        
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold((float) adjustedFailureThreshold)
                .slowCallRateThreshold((float) adjustedSlowCallThreshold)
                .slowCallDurationThreshold(Duration.ofMillis(
                        calculateAdjustedSlowCallDuration(volatility)))
                .permittedNumberOfCallsInHalfOpenState(DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
                .minimumNumberOfCalls(10)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        
        CircuitBreaker breaker = registry.circuitBreaker(instrumentId, config);
        registerInstrumentListeners(breaker, instrumentId);
        
        log.info("CircuitBreaker creado para instrumento '{}' con failureThreshold={}%, slowCallThreshold={}%",
                instrumentId, adjustedFailureThreshold, adjustedSlowCallThreshold);
        
        return breaker;
    }
    
    private double calculateAdjustedThreshold(double volatility, double baseThreshold) {
        double scaledVolatility = Math.min(volatility * VOLATILITY_SCALING_FACTOR, MAX_THRESHOLD);
        double adjusted = baseThreshold - scaledVolatility;
        return Math.max(adjusted, MIN_THRESHOLD);
    }
    
    private int calculateAdjustedSlowCallDuration(double volatility) {
        int baseDuration = DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS;
        int adjusted = (int) (baseDuration * (1 + volatility));
        return Math.min(adjusted, 5000);
    }
    
    private void registerInstrumentListeners(CircuitBreaker breaker, String instrumentId) {
        breaker.getEventPublisher()
                .onFailureRateExceeded(event -> log.warn(
                        "Instrumento '{}' - Tasa de fallo {}% excede threshold {}%",
                        instrumentId, event.getFailureRate(), event.getCircuitBreakerConfig().getFailureRateThreshold()))
                .onSlowCallRateExceeded(event -> log.warn(
                        "Instrumento '{}' - Tasa de llamadas lentas {}% excede threshold {}%",
                        instrumentId, event.getSlowCallRate(), event.getCircuitBreakerConfig().getSlowCallRateThreshold()))
                .onStateTransition(transition -> log.info(
                        "Instrumento '{}' - Transición de estado: {} -> {}",
                        instrumentId, transition.getFromState(), transition.getToState()));
    }
    
    /**
     * Ejecuta una operación protegida por circuit breaker.
     * Utiliza el patrón de sharding por instrumento para evitar bloqueo.
     */
    public <T> T executeWithCircuitBreaker(String instrumentId, double volatility,
                                            Supplier<T> operation, Supplier<T> fallback) {
        CircuitBreaker breaker = getCircuitBreaker(instrumentId, volatility);
        
        if (breaker.getState() == State.OPEN) {
            log.debug("CircuitBreaker '{}' abierto - ejecutando fallback", instrumentId);
            return fallback.get();
        }
        
        return CircuitBreaker.decorateSupplier(breaker, () -> {
            T result = operation.get();
            updateMetrics(instrumentId, result);
            return result;
        }).get();
    }
    
    private void updateMetrics(String instrumentId, Object result) {
        metricsCache.computeIfAbsent(instrumentId, k -> new CircuitBreakerMetrics())
                .recordSuccess();
    }
    
    /**
     * Recalcula los thresholds globales basándose en métricas acumuladas.
     */
    public void recalculateThresholdsIfNeeded() {
        Instant now = Instant.now();
        if (Duration.between(lastRecalculation.get(), now).compareTo(recalculationInterval) > 0) {
            recalculateGlobalThresholds();
            lastRecalculation.set(now);
        }
    }
    
    private void recalculateGlobalThresholds() {
        double totalFailureRate = 0;
        double totalSlowCallRate = 0;
        int breakerCount = circuitBreakers.size();
        
        if (breakerCount == 0) return;
        
        for (Map.Entry<String, CircuitBreaker> entry : circuitBreakers.entrySet()) {
            CircuitBreaker cb = entry.getValue();
            totalFailureRate += cb.getMetrics().getFailureRate();
            totalSlowCallRate += cb.getMetrics().getSlowCallRate();
        }
        
        globalFailureThreshold = Math.min(totalFailureRate / breakerCount + 10, MAX_THRESHOLD);
        globalSlowCallThreshold = Math.min(totalSlowCallRate / breakerCount + 10, MAX_THRESHOLD);
        
        log.info("Thresholds globales recalculados: failure={}%, slowCall={}%",
                globalFailureThreshold, globalSlowCallThreshold);
    }
    
    /**
     * Fuerza la apertura de un circuit breaker específico (para testing o emergencia).
     */
    public void forceOpenCircuitBreaker(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        if (breaker != null) {
            breaker.transitionToOpenState();
            log.warn("CircuitBreaker '{}'_forzadamente abierto", instrumentId);
        }
    }
    
    /**
     * Fuerza el cierre de un circuit breaker específico.
     */
    public void forceCloseCircuitBreaker(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        if (breaker != null) {
            breaker.transitionToClosedState();
            log.info("CircuitBreaker '{}'_forzadamente cerrado", instrumentId);
        }
    }
    
    public CircuitBreakerRegistry getRegistry() {
        return registry;
    }
    
    public Map<String, CircuitBreaker> getCircuitBreakers() {
        return Map.copyOf(circuitBreakers);
    }
    
    public State getState(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        return breaker != null ? breaker.getState() : State.CLOSED;
    }
    
    public double getFailureRate(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        return breaker != null ? breaker.getMetrics().getFailureRate() : 0.0;
    }
    
    private static class CircuitBreakerMetrics {
        private int successCount = 0;
        private int failureCount = 0;
        
        synchronized void recordSuccess() {
            successCount++;
        }
        
        synchronized void recordFailure() {
            failureCount++;
        }
        
        public int getSuccessCount() {
            return successCount;
        }
        
        public int getFailureCount() {
            return failureCount;
        }
    }
}