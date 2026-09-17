package com.pragma.riskengine.disruptor;

import com.lmax.disruptor.EventAccessor;
import com.lmax.disruptor.EventMutator;

public final class OrderEvent {
    private String traderId;
    private String strategyId;
    private String instrumentId;
    private String orderId;
    private double quantity;
    private double price;
    private String side;
    private long receivedTimeNanos;
    private long processingStartNanos;
    private long processingEndNanos;
    private RiskDecision decision;
    private String rejectionReason;
    private double calculatedVaR;
    private double exposureAfterOrder;
    private boolean circuitBreakerTriggered;
    private boolean killSwitchTriggered;
    private String traceId;

    public OrderEvent() {
        this.receivedTimeNanos = System.nanoTime();
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public long getReceivedTimeNanos() {
        return receivedTimeNanos;
    }

    public void setReceivedTimeNanos(long receivedTimeNanos) {
        this.receivedTimeNanos = receivedTimeNanos;
    }

    public long getProcessingStartNanos() {
        return processingStartNanos;
    }

    public void setProcessingStartNanos(long processingStartNanos) {
        this.processingStartNanos = processingStartNanos;
    }

    public long getProcessingEndNanos() {
        return processingEndNanos;
    }

    public void setProcessingEndNanos(long processingEndNanos) {
        this.processingEndNanos = processingEndNanos;
    }

    public RiskDecision getDecision() {
        return decision;
    }

    public void setDecision(RiskDecision decision) {
        this.decision = decision;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public double getCalculatedVaR() {
        return calculatedVaR;
    }

    public void setCalculatedVaR(double calculatedVaR) {
        this.calculatedVaR = calculatedVaR;
    }

    public double getExposureAfterOrder() {
        return exposureAfterOrder;
    }

    public void setExposureAfterOrder(double exposureAfterOrder) {
        this.exposureAfterOrder = exposureAfterOrder;
    }

    public boolean isCircuitBreakerTriggered() {
        return circuitBreakerTriggered;
    }

    public void setCircuitBreakerTriggered(boolean circuitBreakerTriggered) {
        this.circuitBreakerTriggered = circuitBreakerTriggered;
    }

    public boolean isKillSwitchTriggered() {
        return killSwitchTriggered;
    }

    public void setKillSwitchTriggered(boolean killSwitchTriggered) {
        this.killSwitchTriggered = killSwitchTriggered;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void reset() {
        this.traderId = null;
        this.strategyId = null;
        this.instrumentId = null;
        this.orderId = null;
        this.quantity = 0.0;
        this.price = 0.0;
        this.side = null;
        this.receivedTimeNanos = System.nanoTime();
        this.processingStartNanos = 0;
        this.processingEndNanos = 0;
        this.decision = null;
        this.rejectionReason = null;
        this.calculatedVaR = 0.0;
        this.exposureAfterOrder = 0.0;
        this.circuitBreakerTriggered = false;
        this.killSwitchTriggered = false;
        this.traceId = null;
    }

    public long getProcessingLatencyNanos() {
        if (processingStartNanos > 0 && processingEndNanos > 0) {
            return processingEndNanos - processingStartNanos;
        }
        return 0;
    }

    public double getProcessingLatencyMicros() {
        return getProcessingLatencyNanos() / 1000.0;
    }

    public enum RiskDecision {
        APPROVED,
        REJECTED,
        PENDING_REVIEW,
        CIRCUIT_BREAKER_OPEN,
        KILL_SWITCH_TRIGGERED
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId='" + orderId + '\'' +
                ", traderId='" + traderId + '\'' +
                ", instrumentId='" + instrumentId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side='" + side + '\'' +
                ", decision=" + decision +
                ", processingLatencyMicros=" + getProcessingLatencyMicros() +
                '}';
    }
}