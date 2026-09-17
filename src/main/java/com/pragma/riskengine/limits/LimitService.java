package com.pragma.riskengine.limits;

import com.pragma.riskengine.model.VaRModel;
import com.pragma.riskengine.model.VaRModel.Position;
import com.pragma.riskengine.model.VaRModel.InstrumentPosition;
import com.pragma.riskengine.disruptor.MarketDataEvent;
import com.pragma.riskengine.circuitbreaker.DynamicCircuitBreaker;
import com.pragma.riskengine.killswitch.KillSwitchPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LimitService {
    private static final Logger log = LoggerFactory.getLogger(LimitService.class);
    private static final double DEFAULT_VOLATILITY_MULTIPLIER = 2.5;
    private static final double MIN_LIMIT_MULTIPLIER = 1.5;
    private static final double MAX_LIMIT_MULTIPLIER = 5.0;
    private static final double CONCENTRATION_LIMIT_PCT = 0.25;

    private final VaRModel varModel;
    private final DynamicCircuitBreaker circuitBreaker;
    private final KillSwitchPolicy killSwitchPolicy;
    private final ConcurrentHashMap<String, TraderLimits> traderLimits;
    private final ConcurrentHashMap<String, StrategyLimits> strategyLimits;
    private final ConcurrentHashMap<String, InstrumentLimits> instrumentLimits;
    private final ConcurrentHashMap<String, ReentrantReadWriteLock> limitLocks;
    private final AtomicLong limitChecksTotal;
    private final AtomicLong limitViolationsTotal;

    public LimitService(VaRModel varModel, DynamicCircuitBreaker circuitBreaker, KillSwitchPolicy killSwitchPolicy) {
        this.varModel = varModel;
        this.circuitBreaker = circuitBreaker;
        this.killSwitchPolicy = killSwitchPolicy;
        this.traderLimits = new ConcurrentHashMap<>();
        this.strategyLimits = new ConcurrentHashMap<>();
        this.instrumentLimits = new ConcurrentHashMap<>();
        this.limitLocks = new ConcurrentHashMap<>();
        this.limitChecksTotal = new AtomicLong(0);
        this.limitViolationsTotal = new AtomicLong(0);
    }

    public LimitCheckResult checkOrderLimits(Position position, String instrumentId, BigDecimal orderValue) {
        limitChecksTotal.incrementAndGet();
        ReentrantReadWriteLock lock = limitLocks.computeIfAbsent(
            position.traderId(),
            k -> new ReentrantReadWriteLock()
        );
        lock.writeLock().lock();
        try {
            TraderLimits trader = getOrCreateTraderLimits(position.traderId());
            StrategyLimits strategy = getOrCreateStrategyLimits(position.strategyId());
            InstrumentLimits instrument = getOrCreateInstrumentLimits(instrumentId);
            List<LimitViolation> violations = new ArrayList<>();
            double currentVolatility = varModel.getCurrentVolatility(instrumentId);
            double calibratedThreshold = calibrateThresholdByVolatility(currentVolatility);
            if (!checkTraderDailyLimit(trader, orderValue, calibratedThreshold)) {
                violations.add(new LimitViolation(
                    LimitType.TRADER_DAILY_LOSS,
                    position.traderId(),
                    orderValue.doubleValue(),
                    trader.currentDailyLoss,
                    "Trader daily loss limit exceeded"
                ));
            }
            if (!checkStrategyDailyLimit(strategy, orderValue, calibratedThreshold)) {
                violations.add(new LimitViolation(
                    LimitType.STRATEGY_DAILY_LOSS,
                    position.strategyId(),
                    orderValue.doubleValue(),
                    strategy.currentDailyLoss,
                    "Strategy daily loss limit exceeded"
                ));
            }
            if (!checkInstrumentConcentration(position, instrumentId, orderValue)) {
                violations.add(new LimitViolation(
                    LimitType.INSTRUMENT_CONCENTRATION,
                    instrumentId,
                    orderValue.doubleValue(),
                    calculateConcentration(position, instrumentId),
                    "Instrument concentration limit exceeded"
                ));
            }
            if (!checkInstrumentLimit(instrument, orderValue, calibratedThreshold)) {
                violations.add(new LimitViolation(
                    LimitType.INSTRUMENT_LOSS,
                    instrumentId,
                    orderValue.doubleValue(),
                    instrument.currentLoss,
                    "Instrument loss limit exceeded"
                ));
            }
            if (!violations.isEmpty()) {
                limitViolationsTotal.incrementAndGet();
                handleLimitViolation(position, violations);
                return LimitCheckResult.rejected(violations);
            }
            updateLimits(position, orderValue);
            return LimitCheckResult.approved();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void calibrateThresholds(MarketDataEvent marketData) {
        String instrumentId = marketData.instrumentId();
        double currentVolatility = marketData.volatility();
        InstrumentLimits limits = instrumentLimits.get(instrumentId);
        if (limits != null) {
            double newThreshold = calibrateThresholdByVolatility(currentVolatility);
            double oldThreshold = limits.volatilityMultiplier;
            limits.volatilityMultiplier = newThreshold;
            log.info("Calibrated instrument {} threshold from {} to {} based on volatility {}",
                instrumentId, oldThreshold, newThreshold, currentVolatility);
        }
    }

    private double calibrateThresholdByVolatility(double volatility) {
        double multiplier = DEFAULT_VOLATILITY_MULTIPLIER * (1.0 + volatility);
        return Math.max(MIN_LIMIT_MULTIPLIER, Math.min(MAX_LIMIT_MULTIPLIER, multiplier));
    }

    private boolean checkTraderDailyLimit(TraderLimits limits, BigDecimal orderValue, double threshold) {
        double potentialLoss = limits.currentDailyLoss + orderValue.doubleValue();
        return potentialLoss <= limits.maxDailyLoss * threshold;
    }

    private boolean checkStrategyDailyLimit(StrategyLimits limits, BigDecimal orderValue, double threshold) {
        double potentialLoss = limits.currentDailyLoss + orderValue.doubleValue();
        return potentialLoss <= limits.maxDailyLoss * threshold;
    }

    private boolean checkInstrumentConcentration(Position position, String instrumentId, BigDecimal orderValue) {
        double concentration = calculateConcentration(position, instrumentId);
        double potentialConcentration = concentration + orderValue.doubleValue() / calculateTotalNotional(position);
        return potentialConcentration <= CONCENTRATION_LIMIT_PCT;
    }

    private boolean checkInstrumentLimit(InstrumentLimits limits, BigDecimal orderValue, double threshold) {
        double potentialLoss = limits.currentLoss + orderValue.doubleValue();
        return potentialLoss <= limits.maxLoss * threshold;
    }

    private double calculateConcentration(Position position, String instrumentId) {
        double totalNotional = calculateTotalNotional(position);
        if (totalNotional <= 0) return 0.0;
        double instrumentNotional = position.positions().stream()
            .filter(p -> p.instrumentId().equals(instrumentId))
            .mapToDouble(p -> p.notional().doubleValue())
            .sum();
        return instrumentNotional / totalNotional;
    }

    private double calculateTotalNotional(Position position) {
        return position.positions().stream()
            .mapToDouble(p -> p.notional().doubleValue())
            .sum();
    }

    private void updateLimits(Position position, BigDecimal orderValue) {
        TraderLimits trader = traderLimits.get(position.traderId());
        if (trader != null) {
            trader.currentDailyLoss += orderValue.doubleValue();
        }
        StrategyLimits strategy = strategyLimits.get(position.strategyId());
        if (strategy != null) {
            strategy.currentDailyLoss += orderValue.doubleValue();
        }
        for (InstrumentPosition instPos : position.positions()) {
            InstrumentLimits limits = instrumentLimits.get(instPos.instrumentId());
            if (limits != null) {
                limits.currentLoss += instPos.notional().doubleValue();
            }
        }
    }

    private void handleLimitViolation(Position position, List<LimitViolation> violations) {
        log.warn("Limit violations for trader {}: {}", position.traderId(), violations);
        if (violations.size() >= 3 || isEscalatingPattern(position.traderId())) {
            boolean killSwitchTriggered = killSwitchPolicy.evaluateOrder(
                position.traderId(),
                position.strategyId(),
                null,
                null,
                violations.stream().map(LimitViolation::message).toList()
            );
            if (killSwitchTriggered) {
                log.error("Kill switch triggered for trader {} due to limit violations", position.traderId());
            }
        }
    }

    private boolean isEscalatingPattern(String traderId) {
        return limitViolationsTotal.get() > 10;
    }

    private TraderLimits getOrCreateTraderLimits(String traderId) {
        return traderLimits.computeIfAbsent(traderId, k -> new TraderLimits(traderId, 1000000.0));
    }

    private StrategyLimits getOrCreateStrategyLimits(String strategyId) {
        return strategyLimits.computeIfAbsent(strategyId, k -> new StrategyLimits(strategyId, 500000.0));
    }

    private InstrumentLimits getOrCreateInstrumentLimits(String instrumentId) {
        return instrumentLimits.computeIfAbsent(instrumentId, k -> new InstrumentLimits(instrumentId, 250000.0));
    }

    public void resetDailyLimits() {
        traderLimits.values().forEach(l -> l.currentDailyLoss = 0.0);
        strategyLimits.values().forEach(l -> l.currentDailyLoss = 0.0);
        instrumentLimits.values().forEach(l -> l.currentLoss = 0.0);
        log.info("Daily limits reset");
    }

    public LimitServiceStats getStats() {
        return new LimitServiceStats(
            limitChecksTotal.get(),
            limitViolationsTotal.get(),
            traderLimits.size(),
            strategyLimits.size(),
            instrumentLimits.size()
        );
    }

    private static class TraderLimits {
        final String traderId;
        final double maxDailyLoss;
        double currentDailyLoss;

        TraderLimits(String traderId, double maxDailyLoss) {
            this.traderId = traderId;
            this.maxDailyLoss = maxDailyLoss;
            this.currentDailyLoss = 0.0;
        }
    }

    private static class StrategyLimits {
        final String strategyId;
        final double maxDailyLoss;
        double currentDailyLoss;

        StrategyLimits(String strategyId, double maxDailyLoss) {
            this.strategyId = strategyId;
            this.maxDailyLoss = maxDailyLoss;
            this.currentDailyLoss = 0.0;
        }
    }

    private static class InstrumentLimits {
        final String instrumentId;
        final double maxLoss;
        double currentLoss;
        double volatilityMultiplier;

        InstrumentLimits(String instrumentId, double maxLoss) {
            this.instrumentId = instrumentId;
            this.maxLoss = maxLoss;
            this.currentLoss = 0.0;
            this.volatilityMultiplier = DEFAULT_VOLATILITY_MULTIPLIER;
        }
    }

    public enum LimitType {
        TRADER_DAILY_LOSS,
        STRATEGY_DAILY_LOSS,
        INSTRUMENT_CONCENTRATION,
        INSTRUMENT_LOSS
    }

    public record LimitViolation(
        LimitType type,
        String entityId,
        double attemptedValue,
        double currentValue,
        String message
    ) {}

    public record LimitCheckResult(
        boolean approved,
        List<LimitViolation> violations,
        Instant timestamp
    ) {
        public static LimitCheckResult approved() {
            return new LimitCheckResult(true, List.of(), Instant.now());
        }

        public static LimitCheckResult rejected(List<LimitViolation> violations) {
            return new LimitCheckResult(false, violations, Instant.now());
        }
    }

    public record LimitServiceStats(
        long totalChecks,
        long totalViolations,
        int activeTraders,
        int activeStrategies,
        int activeInstruments
    ) {}
}