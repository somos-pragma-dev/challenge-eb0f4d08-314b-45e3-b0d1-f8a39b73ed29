package com.pragma.riskengine.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.Builder;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CircuitBreakerConfig {
    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerConfig.class);
    
    private static final float DEFAULT_FAILURE_RATE_THRESHOLD = 50.0f;
    private static final float DEFAULT_SLOW_CALL_RATE_THRESHOLD = 50.0f;
    private static final int DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS = 1000;
    private static final int DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE = 10;
    private static final int DEFAULT_SLIDING_WINDOW_SIZE = 100;
    private static final int DEFAULT_MINIMUM_NUMBER_OF_CALLS = 10;
    private static final Duration DEFAULT_WAIT_DURATION_IN_OPEN_STATE = Duration.ofSeconds(30);
    
    private static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;
    private static final Duration DEFAULT_RETRY_WAIT_DURATION = Duration.ofMillis(100);
    private static final float DEFAULT_RETRY_MULTIPLIER = 2.0f;
    private static final Duration DEFAULT_RETRY_MAX_DURATION = Duration.ofSeconds(10);
    
    private static final int DEFAULT_BULKHEAD_MAX_CONCURRENT_CALLS = 100;
    private static final int DEFAULT_BULKHEAD_MAX_WAIT_DURATION_MS = 500;
    
    private final Map<String, io.github.resilience4j.circuitbreaker.CircuitBreakerConfig> circuitBreakerConfigs;
    private final Map<String, RetryConfig> retryConfigs;
    private final Map<String, BulkheadConfig> bulkheadConfigs;
    
    private CircuitBreakerConfig() {
        this.circuitBreakerConfigs = new HashMap<>();
        this.retryConfigs = new HashMap<>();
        this.bulkheadConfigs = new HashMap<>();
        initializeDefaultConfigs();
    }
    
    private void initializeDefaultConfigs() {
        circuitBreakerConfigs.put("default", createDefaultCircuitBreakerConfig());
        circuitBreakerConfigs.put("var-calculation", createVaRCalculationConfig());
        circuitBreakerConfigs.put("limit-check", createLimitCheckConfig());
        circuitBreakerConfigs.put("market-data", createMarketDataConfig());
        
        retryConfigs.put("default", createDefaultRetryConfig());
        retryConfigs.put("aggressive", createAggressiveRetryConfig());
        retryConfigs.put("conservative", createConservativeRetryConfig());
        
        bulkheadConfigs.put("default", createDefaultBulkheadConfig());
        bulkheadConfigs.put("high-throughput", createHighThroughputBulkheadConfig());
        
        log.info("Configuraciones de Resilience4j inicializadas");
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createDefaultCircuitBreakerConfig() {
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(DEFAULT_FAILURE_RATE_THRESHOLD)
                .slowCallRateThreshold(DEFAULT_SLOW_CALL_RATE_THRESHOLD)
                .slowCallDurationThreshold(Duration.ofMillis(DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS))
                .permittedNumberOfCallsInHalfOpenState(DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
                .minimumNumberOfCalls(DEFAULT_MINIMUM_NUMBER_OF_CALLS)
                .waitDurationInOpenState(DEFAULT_WAIT_DURATION_IN_OPEN_STATE)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createVaRCalculationConfig() {
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(60.0f)
                .slowCallRateThreshold(40.0f)
                .slowCallDurationThreshold(Duration.ofMillis(500))
                .permittedNumberOfCallsInHalfOpenState(5)
                .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(50)
                .minimumNumberOfCalls(5)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createLimitCheckConfig() {
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(30.0f)
                .slowCallRateThreshold(30.0f)
                .slowCallDurationThreshold(Duration.ofMillis(200))
                .permittedNumberOfCallsInHalfOpenState(15)
                .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(200)
                .minimumNumberOfCalls(20)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createMarketDataConfig() {
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(70.0f)
                .slowCallRateThreshold(60.0f)
                .slowCallDurationThreshold(Duration.ofMillis(2000))
                .permittedNumberOfCallsInHalfOpenState(3)
                .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(30)
                .minimumNumberOfCalls(3)
                .waitDurationInOpenState(Duration.ofSeconds(120))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private RetryConfig createDefaultRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(DEFAULT_MAX_RETRY_ATTEMPTS)
                .waitDuration(DEFAULT_RETRY_WAIT_DURATION)
                .retryExceptions(Exception.class)
                .build();
    }
    
    private RetryConfig createAggressiveRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(50))
                .retryExceptions(Exception.class)
                .build();
    }
    
    private RetryConfig createConservativeRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(Exception.class)
                .build();
    }
    
    private BulkheadConfig createDefaultBulkheadConfig() {
        return BulkheadConfig.custom()
                .maxConcurrentCalls(DEFAULT_BULKHEAD_MAX_CONCURRENT_CALLS)
                .maxWaitDuration(Duration.ofMillis(DEFAULT_BULKHEAD_MAX_WAIT_DURATION_MS))
                .build();
    }
    
    private BulkheadConfig createHighThroughputBulkheadConfig() {
        return BulkheadConfig.custom()
                .maxConcurrentCalls(500)
                .maxWaitDuration(Duration.ofMillis(100))
                .build();
    }
    
    public io.github.resilience4j.circuitbreaker.CircuitBreakerConfig getCircuitBreakerConfig(String name) {
        return circuitBreakerConfigs.getOrDefault(name, circuitBreakerConfigs.get("default"));
    }
    
    public RetryConfig getRetryConfig(String name) {
        return retryConfigs.getOrDefault(name, retryConfigs.get("default"));
    }
    
    public BulkheadConfig getBulkheadConfig(String name) {
        return bulkheadConfigs.getOrDefault(name, bulkheadConfigs.get("default"));
    }
    
    public static CircuitBreakerConfig create() {
        return new CircuitBreakerConfig();
    }
    
    public boolean validate() {
        boolean valid = true;
        
        for (Map.Entry<String, io.github.resilience4j.circuitbreaker.CircuitBreakerConfig> entry : 
                circuitBreakerConfigs.entrySet()) {
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = entry.getValue();
            
            float failureRateThreshold = config.getFailureRateThreshold();
            if (failureRateThreshold < 0 || failureRateThreshold > 100) {
                log.error("Configuración '{}' - failureRateThreshold inválido: {}",
                        entry.getKey(), failureRateThreshold);
                valid = false;
            }
            
            int slidingWindowSize = config.getSlidingWindowSize();
            int minimumNumberOfCalls = config.getMinimumNumberOfCalls();
            if (slidingWindowSize < minimumNumberOfCalls) {
                log.error("Configuración '{}' - slidingWindowSize menor que minimumNumberOfCalls",
                        entry.getKey());
                valid = false;
            }
        }
        
        return valid;
    }
    
    public Map<String, io.github.resilience4j.circuitbreaker.CircuitBreakerConfig> getAllCircuitBreakerConfigs() {
        return Map.copyOf(circuitBreakerConfigs);
    }
    
    public Map<String, RetryConfig> getAllRetryConfigs() {
        return Map.copyOf(retryConfigs);
    }
    
    public Map<String, BulkheadConfig> getAllBulkheadConfigs() {
        return Map.copyOf(bulkheadConfigs);
    }
}