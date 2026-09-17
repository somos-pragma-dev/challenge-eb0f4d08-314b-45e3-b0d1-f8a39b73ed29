package com.pragma.riskengine.disruptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MarketDataEvent(
    String eventId,
    String instrumentId,
    Instant timestamp,
    MarketDataType dataType,
    double[][] orderBookLevels,
    List<Trade> recentTrades,
    BigDecimal lastPrice,
    BigDecimal bidPrice,
    BigDecimal askPrice,
    long bidSize,
    long askSize,
    double volatility,
    double bidAskSpread,
    long sequenceNumber
) {
    public MarketDataEvent {
        if (eventId == null || eventId.isBlank()) {
            eventId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    public static MarketDataEvent createOrderBookEvent(
            String instrumentId,
            double[][] orderBookLevels,
            BigDecimal bidPrice,
            BigDecimal askPrice,
            long bidSize,
            long askSize
    ) {
        double totalBidVolume = 0.0;
        double totalAskVolume = 0.0;
        for (int i = 0; i < Math.min(orderBookLevels.length, 10); i++) {
            if (orderBookLevels[i].length >= 2) {
                totalBidVolume += orderBookLevels[i][1];
            }
        }
        for (int i = 0; i < Math.min(orderBookLevels.length, 10); i++) {
            if (orderBookLevels[i].length >= 2) {
                totalAskVolume += orderBookLevels[i][1];
            }
        }
        double volatility = calculateImpliedVolatility(bidPrice, askPrice, totalBidVolume, totalAskVolume);
        double spread = askPrice.subtract(bidPrice).doubleValue();
        return new MarketDataEvent(
            UUID.randomUUID().toString(),
            instrumentId,
            Instant.now(),
            MarketDataType.ORDER_BOOK,
            orderBookLevels,
            List.of(),
            bidPrice.add(askPrice).divide(BigDecimal.valueOf(2)),
            bidPrice,
            askPrice,
            bidSize,
            askSize,
            volatility,
            spread,
            System.nanoTime()
        );
    }

    public static MarketDataEvent createTradeEvent(
            String instrumentId,
            List<Trade> trades,
            BigDecimal lastPrice
    ) {
        double totalVolume = trades.stream().mapToDouble(Trade::volume).sum();
        double vwap = trades.stream()
            .mapToDouble(t -> t.price().doubleValue() * t.volume())
            .sum() / (totalVolume > 0 ? totalVolume : 1.0);
        double volatility = calculateTradeVolatility(trades);
        return new MarketDataEvent(
            UUID.randomUUID().toString(),
            instrumentId,
            Instant.now(),
            MarketDataType.TRADE,
            new double[0][],
            trades,
            lastPrice,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0L,
            0L,
            volatility,
            0.0,
            System.nanoTime()
        );
    }

    private static double calculateImpliedVolatility(
            BigDecimal bid, BigDecimal ask, double bidVolume, double askVolume
    ) {
        if (bid.compareTo(BigDecimal.ZERO) <= 0 || ask.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }
        double midPrice = bid.add(ask).divide(BigDecimal.valueOf(2)).doubleValue();
        double spread = ask.subtract(bid).doubleValue();
        double normalizedSpread = spread / midPrice;
        double volumeImbalance = (bidVolume - askVolume) / (bidVolume + askVolume + 1.0);
        return Math.abs(normalizedSpread) * (1.0 + Math.abs(volumeImbalance) * 0.5);
    }

    private static double calculateTradeVolatility(List<Trade> trades) {
        if (trades == null || trades.size() < 2) {
            return 0.0;
        }
        double[] prices = trades.stream()
            .mapToDouble(t -> t.price().doubleValue())
            .toArray();
        double mean = java.util.Arrays.stream(prices).average().orElse(0.0);
        double variance = java.util.Arrays.stream(prices)
            .map(p -> (p - mean) * (p - mean))
            .average()
            .orElse(0.0);
        return Math.sqrt(variance) / (mean > 0 ? mean : 1.0);
    }

    public boolean isLiquid() {
        return bidSize > 1000 && askSize > 1000 && bidAskSpread < 0.002;
    }

    public double getMidPrice() {
        if (bidPrice.compareTo(BigDecimal.ZERO) > 0 && askPrice.compareTo(BigDecimal.ZERO) > 0) {
            return bidPrice.add(askPrice).divide(BigDecimal.valueOf(2)).doubleValue();
        }
        return lastPrice != null ? lastPrice.doubleValue() : 0.0;
    }

    public enum MarketDataType {
        ORDER_BOOK,
        TRADE,
        SNAPSHOT
    }

    public record Trade(
        BigDecimal price,
        double volume,
        Instant tradeTime,
        boolean isAggressor
    ) {
        public Trade {
            if (price == null) {
                price = BigDecimal.ZERO;
            }
            if (tradeTime == null) {
                tradeTime = Instant.now();
            }
        }
    }
}