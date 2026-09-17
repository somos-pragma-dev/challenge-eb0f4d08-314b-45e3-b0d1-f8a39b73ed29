package com.pragma.riskengine.var;

import com.pragma.riskengine.model.VaRModel;
import com.pragma.riskengine.model.VaRModel.InstrumentVaRData;
import com.pragma.riskengine.model.VaRModel.WeightingScheme;
import com.pragma.riskengine.model.VaRModel.Position;
import com.pragma.riskengine.model.VaRModel.InstrumentPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VaRCalculator {
    private static final Logger log = LoggerFactory.getLogger(VaRCalculator.class);
    private static final int DEFAULT_LOOKBACK_DAYS = 252;
    private static final double DEFAULT_CONFIDENCE_LEVEL = 0.99;
    private static final int DEFAULT_HORIZON_MINUTES = 1;
    private static final double MIN_VOLATILITY = 0.0001;
    private static final double MAX_VOLATILITY = 5.0;
    private static final int MIN_DATA_POINTS = 30;

    private final VaRModel varModel;
    private final AtomicLong calculationCount;
    private final AtomicLong totalCalculationTimeNanos;
    private final ConcurrentHashMap<String, AtomicLong> instrumentCalculationCounts;
    private final ConcurrentHashMap<String, AtomicLong> instrumentCalculationTimes;
    private final ConcurrentHashMap<String, VolatilityCache> volatilityCache;

    public VaRCalculator(VaRModel varModel) {
        this.varModel = varModel;
        this.calculationCount = new AtomicLong(0);
        this.totalCalculationTimeNanos = new AtomicLong(0);
        this.instrumentCalculationCounts = new ConcurrentHashMap<>();
        this.instrumentCalculationTimes = new ConcurrentHashMap<>();
        this.volatilityCache = new ConcurrentHashMap<>();
    }

    public VaRCalculationResult calculateVaRForPosition(Position position) {
        long startTime = System.nanoTime();
        try {
            if (position == null || position.positions().isEmpty()) {
                return VaRCalculationResult.empty();
            }
            double totalVaR = 0.0;
            List<InstrumentVarDetail> details = new ArrayList<>();
            for (InstrumentPosition instPos : position.positions()) {
                double notional = instPos.notional().doubleValue();
                double instVaR = varModel.calculateVaR(instPos.instrumentId(), notional);
                double volatility = varModel.getCurrentVolatility(instPos.instrumentId());
                double weight = notional / calculateTotalNotional(position);
                totalVaR += instVaR * weight;
                details.add(new InstrumentVarDetail(
                    instPos.instrumentId(),
                    notional,
                    instVaR,
                    volatility
                ));
            }
            double portfolioVaR = varModel.calculatePortfolioVaR(position);
            double diversificationBenefit = totalVaR - portfolioVaR;
            long calcTime = System.nanoTime() - startTime;
            recordCalculation(position.traderId(), calcTime);
            return new VaRCalculationResult(
                portfolioVaR,
                diversificationBenefit,
                details,
                calcTime,
                Instant.now()
            );
        } catch (Exception e) {
            log.error("Error calculating VaR for position: {}", position, e);
            return VaRCalculationResult.error(e.getMessage());
        }
    }

    public double calculateIncrementalVaR(String instrumentId, double additionalNotional) {
        double currentVolatility = varModel.getCurrentVolatility(instrumentId);
        if (currentVolatility < MIN_VOLATILITY) {
            currentVolatility = MIN_VOLATILITY;
        }
        double currentVaR = varModel.calculateVaR(instrumentId, additionalNotional);
        double marginalVaR = currentVaR / (additionalNotional > 0 ? additionalNotional : 1.0);
        return marginalVaR * additionalNotional;
    }

    public void updateMarketData(String instrumentId, double returnPct, Instant timestamp) {
        varModel.updateReturns(instrumentId, returnPct, timestamp);
        invalidateVolatilityCache(instrumentId);
    }

    public void updateOrderBookVolatility(String instrumentId, double[][] orderBook) {
        double orderBookVol = varModel.getOrderBookVolatility(instrumentId, orderBook);
        double currentVol = varModel.getCurrentVolatility(instrumentId);
        double blendedVol = 0.7 * currentVol + 0.3 * orderBookVol;
        double clampedVol = Math.max(MIN_VOLATILITY, Math.min(MAX_VOLATILITY, blendedVol));
        varModel.updateReturns(instrumentId, clampedVol - currentVol, Instant.now());
    }

    public VaRSummary getVaRSummary(String instrumentId) {
        double volatility = varModel.getCurrentVolatility(instrumentId);
        double var95 = varModel.calculateVaR(instrumentId, 1000000.0);
        double var99 = varModel.calculateVaR(instrumentId, 1000000.0) * 1.28;
        AtomicLong count = instrumentCalculationCounts.get(instrumentId);
        AtomicLong time = instrumentCalculationTimes.get(instrumentId);
        long calcCount = count != null ? count.get() : 0;
        long totalTime = time != null ? time.get() : 0;
        double avgTimeNanos = calcCount > 0 ? (double) totalTime / calcCount : 0;
        return new VaRSummary(
            instrumentId,
            volatility,
            var95,
            var99,
            calcCount,
            avgTimeNanos
        );
    }

    private double calculateTotalNotional(Position position) {
        return position.positions().stream()
            .mapToDouble(p -> p.notional().doubleValue())
            .sum();
    }

    private void recordCalculation(String instrumentId, long timeNanos) {
        calculationCount.incrementAndGet();
        totalCalculationTimeNanos.addAndGet(timeNanos);
        instrumentCalculationCounts.computeIfAbsent(
            instrumentId,
            k -> new AtomicLong(0)
        ).incrementAndGet();
        instrumentCalculationTimes.computeIfAbsent(
            instrumentId,
            k -> new AtomicLong(0)
        ).addAndGet(timeNanos);
    }

    private void invalidateVolatilityCache(String instrumentId) {
        volatilityCache.remove(instrumentId);
    }

    public record VaRCalculationResult(
        double portfolioVaR,
        double diversificationBenefit,
        List<InstrumentVarDetail> instrumentDetails,
        long calculationTimeNanos,
        Instant timestamp,
        boolean isError,
        String errorMessage
    ) {
        public VaRCalculationResult(double portfolioVaR, double diversificationBenefit,
                List<InstrumentVarDetail> instrumentDetails, long calculationTimeNanos, Instant timestamp) {
            this(portfolioVaR, diversificationBenefit, instrumentDetails, calculationTimeNanos,
                 timestamp, false, null);
        }

        public static VaRCalculationResult empty() {
            return new VaRCalculationResult(0.0, 0.0, List.of(), 0, Instant.now(), false, null);
        }

        public static VaRCalculationResult error(String message) {
            return new VaRCalculationResult(0.0, 0.0, List.of(), 0, Instant.now(), true, message);
        }
    }

    public record InstrumentVarDetail(
        String instrumentId,
        double notional,
        double var,
        double volatility
    ) {}

    public record VaRSummary(
        String instrumentId,
        double currentVolatility,
        double var95,
        double var99,
        long calculationCount,
        double averageCalculationTimeNanos
    ) {}

    private static class VolatilityCache {
        private final double volatility;
        private final Instant timestamp;
        private final long validForNanos;

        VolatilityCache(double volatility, long validForNanos) {
            this.volatility = volatility;
            this.timestamp = Instant.now();
            this.validForNanos = validForNanos;
        }

        boolean isValid() {
            return System.nanoTime() - timestamp.toEpochMilli() * 1_000_000 < validForNanos;
        }

        double getVolatility() {
            return volatility;
        }
    }
}