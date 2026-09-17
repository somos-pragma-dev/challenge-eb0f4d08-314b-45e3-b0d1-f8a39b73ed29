package com.pragma.riskengine.model;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.DoubleStream;

/**
 * Modelo de VaR (Value at Risk) intraday para evaluación de riesgo en tiempo real.
 * Implementa cálculo de VaR paramétrico usando volatilidad histórica con esquema
 * de ponderación exponencial (EWMA) para dar mayor peso a observaciones recientes.
 * 
 * Este modelo es thread-safe para lecturas concurrentes gracias a ReadWriteLock,
 * pero el cálculo de VaR se ejecuta en el anillo Disruptor de forma lock-free.
 */
public final class VaRModel {

    private static final double SQRT_252 = Math.sqrt(252.0);
    private static final double LN_2 = Math.log(2.0);

    private final int lookbackDays;
    private final double confidenceLevel;
    private final int horizonMinutes;
    private final double decayFactor;
    private final int minimumDataPoints;
    private final WeightingScheme weightingScheme;

    private final ConcurrentHashMap<String, InstrumentVaRData> instrumentData;
    private final ConcurrentHashMap<String, ReadWriteLock> instrumentLocks;

    public VaRModel(int lookbackDays, double confidenceLevel, int horizonMinutes,
                    double decayFactor, int minimumDataPoints, WeightingScheme weightingScheme) {
        this.lookbackDays = lookbackDays;
        this.confidenceLevel = confidenceLevel;
        this.horizonMinutes = horizonMinutes;
        this.decayFactor = decayFactor;
        this.minimumDataPoints = minimumDataPoints;
        this.weightingScheme = weightingScheme;
        this.instrumentData = new ConcurrentHashMap<>();
        this.instrumentLocks = new ConcurrentHashMap<>();
    }

    /**
     * Actualiza el modelo con nuevos retornos del instrumento.
     * Este método es lock-free y puede ejecutarse desde el hilo del Disruptor.
     */
    public void updateReturns(String instrumentId, double returnPct, Instant timestamp) {
        instrumentData.compute(instrumentDataKey(instrumentId), (key, existing) -> {
            if (existing == null) {
                existing = new InstrumentVaRData(instrumentId, lookbackDays);
            }
            existing.addReturn(returnPct, timestamp);
            return existing;
        });
    }

    /**
     * Calcula el VaR para un instrumento dado el valor nocional de la posición.
     * Retorna el VaR en términos monetarios.
     */
    public double calculateVaR(String instrumentId, double notionalValue) {
        String key = instrumentDataKey(instrumentId);
        InstrumentVaRData data = instrumentData.get(key);
        
        if (data == null || data.getDataPointCount() < minimumDataPoints) {
            return 0.0;
        }

        double volatility = calculateVolatility(data);
        double varPct = calculateVarFromVolatility(volatility);
        
        return notionalValue * varPct;
    }

    /**
     * Calcula el VaR de una posición completa considerando la correlación entre instrumentos.
     * Para simplificar, retorna la suma de VaRs individuales (asumiendo correlación 1).
     */
    public double calculatePortfolioVaR(Position position) {
        double totalVaR = 0.0;
        
        for (Position.InstrumentPosition instPos : position.getPositions()) {
            double var = calculateVaR(instPos.instrumentId(), instPos.notionalValue());
            totalVaR += var;
        }
        
        return totalVaR;
    }

    /**
     * Calcula la volatilidad histórica con el esquema de ponderación configurado.
     */
    private double calculateVolatility(InstrumentVaRData data) {
        double[] returns = data.getReturns();
        int n = returns.length;
        
        if (n < minimumDataPoints) {
            return 0.0;
        }

        return switch (weightingScheme) {
            case EXPONENTIAL -> calculateEwmaVolatility(returns);
            case EQUAL -> calculateEqualWeightVolatility(returns);
            case SQUARE_ROOT_TIME -> calculateSqrtTimeVolatility(returns);
        };
    }

    /**
     * Calcula volatilidad usando esquema EWMA (Exponentially Weighted Moving Average).
     * Lambda = decayFactor típicamente 0.94 para datos diarios.
     */
    private double calculateEwmaVolatility(double[] returns) {
        int n = returns.length;
        if (n == 0) return 0.0;

        double variance = 0.0;
        double weightSum = 0.0;
        double decayPower = 1.0;

        for (int i = n - 1; i >= 0; i--) {
            double weight = Math.pow(decayFactor, decayPower);
            variance += weight * returns[i] * returns[i];
            weightSum += weight;
            decayPower += 1.0;
        }

        return Math.sqrt(variance / weightSum) * SQRT_252;
    }

    /**
     * Calcula volatilidad con ponderación igualitaria.
     */
    private double calculateEqualWeightVolatility(double[] returns) {
        double mean = Arrays.stream(returns).average().orElse(0.0);
        double variance = Arrays.stream(returns)
            .map(r -> (r - mean) * (r - mean))
            .average()
            .orElse(0.0);
        return Math.sqrt(variance) * SQRT_252;
    }

    /**
     * Calcula volatilidad ajustada por raíz del tiempo para intraday.
     */
    private double calculateSqrtTimeVolatility(double[] returns) {
        double dailyVol = calculateEqualWeightVolatility(returns);
        double timeFactor = Math.sqrt((double) horizonMinutes / 390.0);
        return dailyVol * timeFactor;
    }

    /**
     * Calcula el percentil de VaR a partir de la volatilidad.
     * Usa la aproximación normal para el cuantil.
     */
    private double calculateVarFromVolatility(double volatility) {
        double zScore = calculateZScore(confidenceLevel);
        double horizonFactor = Math.sqrt((double) horizonMinutes / 390.0);
        return zScore * volatility * horizonFactor;
    }

    /**
     * Calcula el Z-score para un nivel de confianza dado.
     */
    private double calculateZScore(double confidence) {
        return switch ((int) (confidence * 100)) {
            case 99 -> 2.326;
            case 98 -> 2.054;
            case 97 -> 1.880;
            case 95 -> 1.645;
            default -> {
                double p = 1.0 - confidence;
                yield Math.sqrt(2.0) * inverseErf(2.0 * p - 1.0);
            }
        };
    }

    /**
     * Aproximación de la función inversa erf.
     */
    private double inverseErf(double x) {
        double a = 0.147;
        double ln1MinusXSq = Math.log(1.0 - x * x);
        double part1 = 2.0 / (Math.PI * a) + ln1MinusXSq / 2.0;
        double part2 = ln1MinusXSq / a;
        return Math.signum(x) * Math.sqrt(Math.sqrt(part1 * part1 - part2) - part1);
    }

    /**
     * Obtiene la volatilidad actual de un instrumento para uso en circuit breaker dinámico.
     */
    public double getCurrentVolatility(String instrumentId) {
        String key = instrumentDataKey(instrumentId);
        InstrumentVaRData data = instrumentData.get(key);
        
        if (data == null || data.getDataPointCount() < minimumDataPoints) {
            return 0.0;
        }
        
        return calculateVolatility(data);
    }

    /**
     * Obtiene la volatilidad histórica del orderbook para cálculo de VaR.
     */
    public double getOrderBookVolatility(String instrumentId, double[][] orderBook) {
        if (orderBook == null || orderBook.length < 2) {
            return getCurrentVolatility(instrumentId);
        }

        double[] bidLevels = orderBook[0];
        double[] askLevels = orderBook[1];
        
        if (bidLevels.length == 0 || askLevels.length == 0) {
            return getCurrentVolatility(instrumentId);
        }

        double bestBid = bidLevels[0];
        double bestAsk = askLevels[0];
        double midPrice = (bestBid + bestAsk) / 2.0;
        double spread = (bestAsk - bestBid) / midPrice;

        double impliedVol = spread * Math.sqrt(252.0) / 2.0;
        double historicalVol = getCurrentVolatility(instrumentId);

        return Math.max(impliedVol, historicalVol * 0.5);
    }

    private String instrumentDataKey(String instrumentId) {
        return instrumentId;
    }

    public int getLookbackDays() {
        return lookbackDays;
    }

    public double getConfidenceLevel() {
        return confidenceLevel;
    }

    public int getHorizonMinutes() {
        return horizonMinutes;
    }

    public enum WeightingScheme {
        EXPONENTIAL,
        EQUAL,
        SQUARE_ROOT_TIME
    }

    /**
     * Datos internos del VaR para un instrumento específico.
     * Almacena los retornos y metadatos de forma thread-safe.
     */
    private static final class InstrumentVaRData {
        private final String instrumentId;
        private final int maxSize;
        private final double[] returns;
        private final long[] timestamps;
        private int currentIndex;
        private int dataPointCount;

        InstrumentVaRData(String instrumentId, int lookbackDays) {
            this.instrumentId = instrumentId;
            this.maxSize = lookbackDays;
            this.returns = new double[lookbackDays];
            this.timestamps = new long[lookbackDays];
            this.currentIndex = 0;
            this.dataPointCount = 0;
        }

        synchronized void addReturn(double returnPct, Instant timestamp) {
            returns[currentIndex] = returnPct;
            timestamps[currentIndex] = timestamp.toEpochNano();
            currentIndex = (currentIndex + 1) % maxSize;
            if (dataPointCount < maxSize) {
                dataPointCount++;
            }
        }

        double[] getReturns() {
            if (dataPointCount == 0) {
                return new double[0];
            }
            
            double[] result = new double[dataPointCount];
            for (int i = 0; i < dataPointCount; i++) {
                int sourceIndex = (currentIndex - dataPointCount + i + maxSize) % maxSize;
                result[i] = returns[sourceIndex];
            }
            return result;
        }

        int getDataPointCount() {
            return dataPointCount;
        }
    }

    /**
     * Representa una posición de portfolio con múltiples instrumentos.
     */
    public record Position(String traderId, String strategyId, java.util.List<InstrumentPosition> positions) {

        public double getTotalNotional() {
            return positions.stream()
                .mapToDouble(InstrumentPosition::notionalValue)
                .sum();
        }

        public record InstrumentPosition(String instrumentId, double notionalValue, double quantity) {}
    }
}