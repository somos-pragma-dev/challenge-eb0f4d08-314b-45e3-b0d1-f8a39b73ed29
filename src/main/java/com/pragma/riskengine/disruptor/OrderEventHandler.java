package com.pragma.riskengine.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventSpinPolicy;
import com.pragma.riskengine.var.VaRCalculator;
import com.pragma.riskengine.limits.LimitService;
import com.pragma.riskengine.circuitbreaker.DynamicCircuitBreaker;
import com.pragma.riskengine.killswitch.KillSwitchPolicy;
import com.pragma.riskengine.compliance.MiFIDIITracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderEventHandler implements EventHandler<OrderEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);
    private static final double DEFAULT_NOTIONAL = 100000.0;
    private static final double MAX_ACCEPTABLE_LATENCY_MICROS = 500.0;

    private final VaRCalculator varCalculator;
    private final LimitService limitService;
    private final DynamicCircuitBreaker circuitBreaker;
    private final KillSwitchPolicy killSwitchPolicy;
    private final MiFIDIITracer tracer;
    private long eventsProcessed = 0;
    private long totalLatencyNanos = 0;

    public OrderEventHandler(VaRCalculator varCalculator, LimitService limitService,
                             DynamicCircuitBreaker circuitBreaker, KillSwitchPolicy killSwitchPolicy,
                             MiFIDIITracer tracer) {
        this.varCalculator = varCalculator;
        this.limitService = limitService;
        this.circuitBreaker = circuitBreaker;
        this.killSwitchPolicy = killSwitchPolicy;
        this.tracer = tracer;
    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        event.setProcessingStartNanos(System.nanoTime());
        
        try {
            validateOrderPreconditions(event);
            
            if (circuitBreaker.isOpen()) {
                handleCircuitBreakerOpen(event);
                return;
            }

            if (killSwitchPolicy.shouldStop(event.getTraderId())) {
                handleKillSwitchTriggered(event);
                return;
            }

            double varResult = calculateRiskMetrics(event);
            event.setCalculatedVaR(varResult);

            boolean withinLimits = applyRiskLimits(event);
            if (!withinLimits) {
                return;
            }

            double exposure = calculateExposure(event);
            event.setExposureAfterOrder(exposure);

            updateRiskModel(event);

            circuitBreaker.recordSuccess();
            
            event.setDecision(OrderEvent.RiskDecision.APPROVED);
            tracer.traceDecision(event.getTraceId(), "APPROVED", 
                "Risk metrics within limits: VaR=" + varResult);
            
            logApproval(event);
            
        } catch (Exception e) {
            handleProcessingError(event, e);
        } finally {
            event.setProcessingEndNanos(System.nanoTime());
            updatePerformanceMetrics(event.getProcessingLatencyNanos());
        }
    }

    private void validateOrderPreconditions(OrderEvent event) {
        if (event.getTraderId() == null || event.getTraderId().isEmpty()) {
            throw new IllegalArgumentException("Trader ID no puede ser nulo o vacío");
        }
        if (event.getInstrumentId() == null || event.getInstrumentId().isEmpty()) {
            throw new IllegalArgumentException("Instrument ID no puede ser nulo o vacío");
        }
        if (event.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity debe ser mayor que cero");
        }
        if (event.getPrice() <= 0) {
            throw new IllegalArgumentException("Price debe ser mayor que cero");
        }
        if (!isValidSide(event.getSide())) {
            throw new IllegalArgumentException("Side debe ser BUY o SELL");
        }
    }

    private boolean isValidSide(String side) {
        return "BUY".equalsIgnoreCase(side) || "SELL".equalsIgnoreCase(side);
    }

    private void handleCircuitBreakerOpen(OrderEvent event) {
        event.setCircuitBreakerTriggered(true);
        event.setDecision(OrderEvent.RiskDecision.CIRCUIT_BREAKER_OPEN);
        event.setRejectionReason("Circuit breaker abierto - demasiados rechazos recientes");
        circuitBreaker.recordFailure();
        tracer.traceDecision(event.getTraceId(), "REJECTED", 
            "Circuit breaker open");
        logger.warn("Orden {} rechazada - circuit breaker abierto", event.getOrderId());
    }

    private void handleKillSwitchTriggered(OrderEvent event) {
        event.setKillSwitchTriggered(true);
        event.setDecision(OrderEvent.RiskDecision.KILL_SWITCH_TRIGGERED);
        event.setRejectionReason("Kill switch activado para trader: " + event.getTraderId());
        tracer.traceDecision(event.getTraceId(), "REJECTED", 
            "Kill switch triggered for trader " + event.getTraderId());
        logger.error("Orden {} rechazada - kill switch activado para trader {}", 
            event.getOrderId(), event.getTraderId());
    }

    private double calculateRiskMetrics(OrderEvent event) {
        double notionalValue = event.getQuantity() * event.getPrice();
        double var = varCalculator.calculateVaR(event.getInstrumentId(), notionalValue);
        return var;
    }

    private boolean applyRiskLimits(OrderEvent event) {
        double notional = event.getQuantity() * event.getPrice();
        
        boolean traderLimitOk = limitService.checkTraderLimit(
            event.getTraderId(), notional);
        
        boolean strategyLimitOk = limitService.checkStrategyLimit(
            event.getStrategyId(), notional);
        
        boolean instrumentLimitOk = limitService.checkInstrumentLimit(
            event.getInstrumentId(), notional);
        
        if (!traderLimitOk) {
            rejectOrder(event, "Límite de trader excedido: " + event.getTraderId());
            return false;
        }
        
        if (!strategyLimitOk) {
            rejectOrder(event, "Límite de estrategia excedido: " + event.getStrategyId());
            return false;
        }
        
        if (!instrumentLimitOk) {
            rejectOrder(event, "Límite de instrumento excedido: " + event.getInstrumentId());
            return false;
        }
        
        return true;
    }

    private void rejectOrder(OrderEvent event, String reason) {
        event.setDecision(OrderEvent.RiskDecision.REJECTED);
        event.setRejectionReason(reason);
        circuitBreaker.recordFailure();
        tracer.traceDecision(event.getTraceId(), "REJECTED", reason);
        logger.warn("Orden {} rechazada: {}", event.getOrderId(), reason);
    }

    private double calculateExposure(OrderEvent event) {
        double notional = event.getQuantity() * event.getPrice();
        double currentExposure = limitService.getCurrentExposure(event.getTraderId(), 
            event.getInstrumentId());
        
        if ("BUY".equalsIgnoreCase(event.getSide())) {
            return currentExposure + notional;
        } else {
            return currentExposure - notional;
        }
    }

    private void updateRiskModel(OrderEvent event) {
        double returnPct = calculateReturnFromOrder(event);
        varCalculator.updateReturns(event.getInstrumentId(), returnPct);
    }

    private double calculateReturnFromOrder(OrderEvent event) {
        double notional = event.getQuantity() * event.getPrice();
        return notional / DEFAULT_NOTIONAL;
    }

    private void handleProcessingError(OrderEvent event, Exception e) {
        logger.error("Error procesando orden {}: {}", event.getOrderId(), e.getMessage(), e);
        event.setDecision(OrderEvent.RiskDecision.REJECTED);
        event.setRejectionReason("Error interno: " + e.getMessage());
        circuitBreaker.recordFailure();
        tracer.traceDecision(event.getTraceId(), "ERROR", e.getMessage());
    }

    private void logApproval(OrderEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Orden {} aprobada - VaR: {}, Latencia: {}μs",
                event.getOrderId(), 
                String.format("%.2f", event.getCalculatedVaR()),
                String.format("%.2f", event.getProcessingLatencyMicros()));
        } else {
            logger.info("Orden {} aprobada - Latencia: {}μs",
                event.getOrderId(), 
                String.format("%.2f", event.getProcessingLatencyMicros()));
        }
    }

    private void updatePerformanceMetrics(long latencyNanos) {
        eventsProcessed++;
        totalLatencyNanos += latencyNanos;
        
        if (eventsProcessed % 10000 == 0) {
            double avgLatencyMicros = (totalLatencyNanos / eventsProcessed) / 1000.0;
            logger.info("Métricas de rendimiento - Eventos: {}, Latencia promedio: {}μs",
                eventsProcessed, String.format("%.2f", avgLatencyMicros));
            
            if (avgLatencyMicros > MAX_ACCEPTABLE_LATENCY_MICROS) {
                logger.warn("Latencia promedio excede threshold: {}μs > {}μs",
                    avgLatencyMicros, MAX_ACCEPTABLE_LATENCY_MICROS);
            }
        }
    }

    public long getEventsProcessed() {
        return eventsProcessed;
    }

    public double getAverageLatencyMicros() {
        if (eventsProcessed == 0) return 0;
        return (totalLatencyNanos / eventsProcessed) / 1000.0;
    }
}