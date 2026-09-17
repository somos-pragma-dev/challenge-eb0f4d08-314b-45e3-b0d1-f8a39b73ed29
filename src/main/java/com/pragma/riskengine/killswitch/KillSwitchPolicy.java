package com.pragma.riskengine.killswitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class KillSwitchPolicy {
    private static final Logger log = LoggerFactory.getLogger(KillSwitchPolicy.class);
    
    private static final double DEFAULT_ANOMALY_THRESHOLD = 3.0;
    private static final int DEFAULT_CONSECUTIVE_ANOMALIES = 5;
    private static final Duration DEFAULT_ANOMALY_WINDOW = Duration.ofMinutes(5);
    private static final Duration DEFAULT_COOLDOWN_PERIOD = Duration.ofMinutes(15);
    private static final int DEFAULT_MAX_ORDERS_PER_SECOND = 100;
    private static final double DEFAULT_MAX_POSITION_CHANGE_RATE = 0.5;
    
    private final Map<String, TraderKillSwitchState> traderStates;
    private final Map<String, StrategyKillSwitchState> strategyStates;
    private final Consumer<KillSwitchEvent> eventHandler;
    private final double anomalyThreshold;
    private final int consecutiveAnomalyLimit;
    private final Duration anomalyWindow;
    private final Duration cooldownPeriod;
    private final int maxOrdersPerSecond;
    private final double maxPositionChangeRate;
    
    private final AtomicBoolean globalKillSwitchActive;
    
    public KillSwitchPolicy(Consumer<KillSwitchEvent> eventHandler) {
        this(eventHandler, DEFAULT_ANOMALY_THRESHOLD, DEFAULT_CONSECUTIVE_ANOMALIES,
                DEFAULT_ANOMALY_WINDOW, DEFAULT_COOLDOWN_PERIOD,
                DEFAULT_MAX_ORDERS_PER_SECOND, DEFAULT_MAX_POSITION_CHANGE_RATE);
    }
    
    public KillSwitchPolicy(Consumer<KillSwitchEvent> eventHandler, double anomalyThreshold,
                            int consecutiveAnomalyLimit, Duration anomalyWindow,
                            Duration cooldownPeriod, int maxOrdersPerSecond,
                            double maxPositionChangeRate) {
        this.traderStates = new ConcurrentHashMap<>();
        this.strategyStates = new ConcurrentHashMap<>();
        this.eventHandler = eventHandler;
        this.anomalyThreshold = anomalyThreshold;
        this.consecutiveAnomalyLimit = consecutiveAnomalyLimit;
        this.anomalyWindow = anomalyWindow;
        this.cooldownPeriod = cooldownPeriod;
        this.maxOrdersPerSecond = maxOrdersPerSecond;
        this.maxPositionChangeRate = maxPositionChangeRate;
        this.globalKillSwitchActive = new AtomicBoolean(false);
        
        log.info("KillSwitchPolicy inicializado con thresholds: anomaly={}, consecutive={}, window={}",
                anomalyThreshold, consecutiveAnomalyLimit, anomalyWindow);
    }
    
    public boolean evaluateOrder(String traderId, String strategyId, String instrumentId,
                                  double orderValue, double currentPosition, int ordersInLastSecond) {
        if (globalKillSwitchActive.get()) {
            triggerKillSwitch(traderId, strategyId, KillSwitchReason.GLOBAL_KILL_SWITCH_ACTIVE,
                    "Orden bloqueada por kill switch global activo");
            return false;
        }
        
        TraderKillSwitchState traderState = traderStates.get(traderId);
        if (traderState != null && traderState.isInCooldown()) {
            log.warn("Trader '{}' en cooldown hasta {}", traderId, traderState.getCooldownEnd());
            return false;
        }
        
        if (ordersInLastSecond > maxOrdersPerSecond) {
            triggerKillSwitch(traderId, strategyId, KillSwitchReason.RATE_LIMIT_EXCEEDED,
                    String.format("Rate limit excedido: %d órdenes/segundo", ordersInLastSecond));
            return false;
        }
        
        if (traderState != null) {
            double positionChangeRate = calculatePositionChangeRate(traderState, currentPosition);
            if (positionChangeRate > maxPositionChangeRate) {
                triggerKillSwitch(traderId, strategyId, KillSwitchReason.POSITION_CHANGE_TOO_FAST,
                        String.format("Cambio de posición muy rápido: %.2f%%/segundo", positionChangeRate * 100));
                return false;
            }
        }
        
        StrategyKillSwitchState strategyState = strategyStates.get(strategyId);
        if (strategyState != null && strategyState.isKilled()) {
            log.warn("Estrategia '{}' ha sido desactivada por KillSwitch", strategyId);
            return false;
        }
        
        return true;
    }
    
    public void recordAnomaly(String traderId, String strategyId, AnomalyType type, double deviation) {
        if (deviation < anomalyThreshold) {
            return;
        }
        
        TraderKillSwitchState traderState = traderStates.computeIfAbsent(traderId,
                k -> new TraderKillSwitchState(traderId));
        StrategyKillSwitchState strategyState = strategyStates.computeIfAbsent(strategyId,
                k -> new StrategyKillSwitchState(strategyId));
        
        Instant now = Instant.now();
        traderState.recordAnomaly(now, type, deviation);
        strategyState.recordAnomaly(now, type, deviation);
        
        log.warn("Anomalía detectada - Trader: {}, Estrategia: {}, Tipo: {}, Desviación: {}",
                traderId, strategyId, type, deviation);
        
        if (traderState.getConsecutiveAnomalyCount() >= consecutiveAnomalyLimit) {
            triggerKillSwitch(traderId, strategyId, KillSwitchReason.EXCESSIVE_ANOMALIES,
                    String.format("%d anomalías consecutivas detectadas", 
                            traderState.getConsecutiveAnomalyCount()));
        }
        
        if (strategyState.getConsecutiveAnomalyCount() >= consecutiveAnomalyLimit) {
            triggerKillSwitch(traderId, strategyId, KillSwitchReason.EXCESSIVE_ANOMALIES,
                    String.format("Estrategia con %d anomalías consecutivas", 
                            strategyState.getConsecutiveAnomalyCount()));
        }
    }
    
    public double evaluateTraderRisk(String traderId, String strategyId) {
        TraderKillSwitchState traderState = traderStates.get(traderId);
        StrategyKillSwitchState strategyState = strategyStates.get(strategyId);
        
        if (traderState == null && strategyState == null) {
            return 0.0;
        }
        
        double riskFactor = 0.0;
        
        if (traderState != null) {
            int anomalies = traderState.getConsecutiveAnomalyCount();
            riskFactor += (double) anomalies / consecutiveAnomalyLimit;
        }
        
        if (strategyState != null) {
            int anomalies = strategyState.getConsecutiveAnomalyCount();
            riskFactor += (double) anomalies / consecutiveAnomalyLimit;
        }
        
        return Math.min(riskFactor, 1.0);
    }
    
    private double calculatePositionChangeRate(TraderKillSwitchState state, double currentPosition) {
        if (state.getLastPosition() == 0.0) {
            return 0.0;
        }
        double change = Math.abs(currentPosition - state.getLastPosition()) / Math.abs(state.getLastPosition());
        Duration timeElapsed = Duration.between(state.getLastPositionUpdate(), Instant.now());
        if (timeElapsed.isZero()) {
            return 0.0;
        }
        return change / timeElapsed.toSeconds();
    }
    
    private void triggerKillSwitch(String traderId, String strategyId, KillSwitchReason reason, String detail) {
        log.error("KILL SWITCH ACTIVADO - Trader: {}, Estrategia: {}, Razón: {}, Detalle: {}",
                traderId, strategyId, reason, detail);
        
        TraderKillSwitchState traderState = traderStates.get(traderId);
        if (traderState != null) {
            traderState.activateKillSwitch(cooldownPeriod);
        }
        
        StrategyKillSwitchState strategyState = strategyStates.get(strategyId);
        if (strategyState != null) {
            strategyState.activateKillSwitch();
        }
        
        KillSwitchEvent event = new KillSwitchEvent(
                Instant.now(), traderId, strategyId, reason, detail);
        
        if (eventHandler != null) {
            eventHandler.accept(event);
        }
    }
    
    public void activateGlobalKillSwitch(String reason) {
        globalKillSwitchActive.set(true);
        log.error("KILL SWITCH GLOBAL ACTIVADO: {}", reason);
    }
    
    public void deactivateGlobalKillSwitch() {
        globalKillSwitchActive.set(false);
        log.info("KILL SWITCH GLOBAL DESACTIVADO");
    }
    
    public void resetTrader(String traderId) {
        traderStates.remove(traderId);
        log.info("Estado de KillSwitch reseteado para trader '{}'", traderId);
    }
    
    public void resetStrategy(String strategyId) {
        strategyStates.remove(strategyId);
        log.info("Estado de KillSwitch reseteado para estrategia '{}'", strategyId);
    }
    
    public boolean isGlobalKillSwitchActive() {
        return globalKillSwitchActive.get();
    }
    
    public boolean isTraderKilled(String traderId) {
        TraderKillSwitchState state = traderStates.get(traderId);
        return state != null && state.isKilled();
    }
    
    public boolean isStrategyKilled(String strategyId) {
        StrategyKillSwitchState state = strategyStates.get(strategyId);
        return state != null && state.isKilled();
    }
    
    public enum KillSwitchReason {
        EXCESSIVE_ANOMALIES,
        RATE_LIMIT_EXCEEDED,
        POSITION_CHANGE_TOO_FAST,
        GLOBAL_KILL_SWITCH_ACTIVE,
        MANUAL_TRIGGER,
        CIRCUIT_BREAKER_OPEN
    }
    
    public enum AnomalyType {
        VOLATILITY_SPIKE,
        UNUSUAL_ORDER_SIZE,
        RAPID_POSITION_CHANGE,
        TRADING_AT_UNUSUAL_HOURS,
        EXCESSIVE_CANCELLED_ORDERS
    }
    
    public record KillSwitchEvent(
            Instant timestamp,
            String traderId,
            String strategyId,
            KillSwitchReason reason,
            String detail
    ) {}
    
    private static class TraderKillSwitchState {
        private final String traderId;
        private final AtomicInteger consecutiveAnomalyCount;
        private volatile Instant lastAnomalyTime;
        private volatile Instant cooldownEnd;
        private volatile double lastPosition;
        private volatile Instant lastPositionUpdate;
        private final AtomicBoolean killed;
        
        TraderKillSwitchState(String traderId) {
            this.traderId = traderId;
            this.consecutiveAnomalyCount = new AtomicInteger(0);
            this.lastPosition = 0.0;
            this.lastPositionUpdate = Instant.now();
            this.killed = new AtomicBoolean(false);
        }
        
        void recordAnomaly(Instant timestamp, AnomalyType type, double deviation) {
            lastAnomalyTime = timestamp;
            consecutiveAnomalyCount.incrementAndGet();
        }
        
        void activateKillSwitch(Duration cooldown) {
            killed.set(true);
            cooldownEnd = Instant.now().plus(cooldown);
        }
        
        void activateKillSwitch() {
            activateKillSwitch(Duration.ofMinutes(15));
        }
        
        boolean isKilled() {
            return killed.get();
        }
        
        boolean isInCooldown() {
            return cooldownEnd != null && Instant.now().isBefore(cooldownEnd);
        }
        
        Instant getCooldownEnd() {
            return cooldownEnd;
        }
        
        int getConsecutiveAnomalyCount() {
            return consecutiveAnomalyCount.get();
        }
        
        double getLastPosition() {
            return lastPosition;
        }
        
        Instant getLastPositionUpdate() {
            return lastPositionUpdate;
        }
    }
    
    private static class StrategyKillSwitchState {
        private final String strategyId;
        private final AtomicInteger consecutiveAnomalyCount;
        private volatile Instant lastAnomalyTime;
        private final AtomicBoolean killed;
        private volatile double lastPosition;
        private volatile Instant lastPositionUpdate;
        
        StrategyKillSwitchState(String strategyId) {
            this.strategyId = strategyId;
            this.consecutiveAnomalyCount = new AtomicInteger(0);
            this.killed = new AtomicBoolean(false);
            this.lastPosition = 0.0;
            this.lastPositionUpdate = Instant.now();
        }
        
        void recordAnomaly(Instant timestamp, AnomalyType type, double deviation) {
            lastAnomalyTime = timestamp;
            consecutiveAnomalyCount.incrementAndGet();
        }
        
        void activateKillSwitch() {
            killed.set(true);
        }
        
        boolean isKilled() {
            return killed.get();
        }
        
        int getConsecutiveAnomalyCount() {
            return consecutiveAnomalyCount.get();
        }
        
        double getLastPosition() {
            return lastPosition;
        }
        
        Instant getLastPositionUpdate() {
            return lastPositionUpdate;
        }
    }
}