package com.pragma.riskengine.var;

import com.pragma.riskengine.model.VaRModel;
import com.pragma.riskengine.model.VaRModel.WeightingScheme;
import com.pragma.riskengine.model.Position;
import com.pragma.riskengine.model.InstrumentPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VaRCalculator - Tests del modelo de VaR con escenarios históricos")
class VaRCalculatorTest {

    private VaRModel varModel;
    private VaRCalculator calculator;

    @BeforeEach
    void setUp() {
        varModel = new VaRModel(
            252,
            0.99,
            1440,
            0.94,
            30,
            WeightingScheme.EWMA
        );
        calculator = new VaRCalculator(varModel);
    }

    @Nested
    @DisplayName("Escenarios de Mercado Históricos")
    class HistoricalMarketScenarios {

        @Test
        @DisplayName("Debe calcular VaR para mercado lateral estable")
        void shouldCalculateVarForStableMarket() {
            String instrumentId = "STABLE_STOCK";
            double notional = 1_000_000;

            for (int i = 0; i < 100; i++) {
                double returnPct = (Math.random() - 0.5) * 0.02;
                varModel.updateReturns(instrumentId, returnPct, Instant.now().minusSeconds(i * 300));
            }

            double var = varModel.calculateVaR(instrumentId, notional);

            assertTrue(var > 0, "VaR debe ser positivo para cualquier escenario");
            assertTrue(var < notional * 0.5, 
                "VaR para mercado estable no debe exceder 50% del notional");
        }

        @Test
        @DisplayName("Debe calcular VaR para mercado volátil con crisis de 2008")
        void shouldCalculateVarFor2008CrisisScenario() {
            String instrumentId = "CRISIS_2008";
            double notional = 1_000_000;

            double[] crisisReturns = {
                -0.05, -0.08, -0.12, -0.15, -0.10, -0.07, -0.09, -0.11,
                -0.06, -0.04, -0.03, -0.02, -0.05, -0.07, -0.09, -0.13
            };

            for (int i = 0; i < crisisReturns.length; i++) {
                varModel.updateReturns(instrumentId, crisisReturns[i], 
                    Instant.now().minusSeconds(i * 300));
            }

            double var = varModel.calculateVaR(instrumentId, notional);

            assertTrue(var > notional * 0.05, 
                "VaR debe reflejar la alta volatilidad del escenario de crisis");
        }

        @Test
        @DisplayName("Debe calcular VaR para mercado bullish con tendencias alcistas")
        void shouldCalculateVarForBullishMarket() {
            String instrumentId = "BULL_MARKET";
            double notional = 1_000_000;

            for (int i = 0; i < 50; i++) {
                double returnPct = 0.01 + Math.random() * 0.02;
                varModel.updateReturns(instrumentId, returnPct, 
                    Instant.now().minusSeconds(i * 300));
            }

            double var = varModel.calculateVaR(instrumentId, notional);

            assertTrue(var > 0, "VaR debe ser positivo incluso en mercados alcistas");
            assertTrue(var < notional * 0.15, 
                "VaR en mercado bullish debe ser menor que en mercados laterales");
        }
    }

    @Nested
    @DisplayName("Escenarios de Volatilidad Extrema")
    class ExtremeVolatilityScenarios {

        @Test
        @DisplayName("Debe calcular VaR con volatilidad extrema tipo flash crash")
        void shouldCalculateVarForFlashCrashScenario() {
            String instrumentId = "FLASH_CRASH";
            double notional = 1_000_000;

            double[] flashCrashReturns = {
                -0.20, -0.25, -0.18, -0.15, -0.10, -0.08, -0.05, -0.03
            };

            for (int i = 0; i < flashCrashReturns.length; i++) {
                varModel.updateReturns(instrumentId, flashCrashReturns[i], 
                    Instant.now().minusSeconds(i * 60));
            }

            double var = varModel.calculateVaR(instrumentId, notional);

            assertTrue(var > notional * 0.10, 
                "VaR debe capturar el riesgo extremo del flash crash");
            assertTrue(varModel.getCurrentVolatility(instrumentId) > 0.30, 
                "Volatilidad должна быть выше 30% para flash crash");
        }

        @Test
        @DisplayName("Debe manejar volatilidad extremadamente baja (cero riesgo implícito)")
        void shouldHandleExtremelyLowVolatility() {
            String instrumentId = "ZERO_VOL";
            double notional = 1_000_000;

            for (int i = 0; i < 30; i++) {
                varModel.updateReturns(instrumentId, 0.0001, 
                    Instant.now().minusSeconds(i * 3600));
            }

            double var = varModel.calculateVaR(instrumentId, notional);
            double volatility = varModel.getCurrentVolatility(instrumentId);

            assertTrue(var > 0, "VaR nunca puede ser cero o negativo");
            assertTrue(volatility < 0.02, 
                "Volatilidad должна быть очень baja para este escenario");
        }

        @Test
        @DisplayName("Debe calcular VaR con gaps de mercado (datos faltantes)")
        void shouldCalculateVarWithMarketGaps() {
            String instrumentId = "GAP_MARKET";
            double notional = 500_000;

            varModel.updateReturns(instrumentId, 0.02, Instant.now().minusSeconds(3600 * 24));
            varModel.updateReturns(instrumentId, -0.015, Instant.now().minusSeconds(3600 * 12));
            varModel.updateReturns(instrumentId, 0.025, Instant.now());

            double var = varModel.calculateVaR(instrumentId, notional);

            assertTrue(var > 0, "VaR debe calcularse incluso con gaps de datos");
        }
    }

    @Nested
    @DisplayName("Portfolio VaR Tests")
    class PortfolioVarTests {

        @Test
        @DisplayName("Debe calcular VaR de portfolio diversificado")
        void shouldCalculateDiversifiedPortfolioVar() {
            String instrument1 = "STOCK_A";
            String instrument2 = "STOCK_B";
            String instrument3 = "STOCK_C";

            for (int i = 0; i < 60; i++) {
                double ret1 = (Math.random() - 0.5) * 0.03;
                double ret2 = (Math.random() - 0.5) * 0.025;
                double ret3 = (Math.random() - 0.5) * 0.035;
                Instant ts = Instant.now().minusSeconds(i * 300);

                varModel.updateReturns(instrument1, ret1, ts);
                varModel.updateReturns(instrument2, ret2, ts);
                varModel.updateReturns(instrument3, ret3, ts);
            }

            List<InstrumentPosition> positions = List.of(
                new InstrumentPosition(instrument1, 500_000, 150.0),
                new InstrumentPosition(instrument2, 300_000, 75.0),
                new InstrumentPosition(instrument3, 200_000, 200.0)
            );

            Position portfolio = new Position("TRADER_1", "STRATEGY_1", positions);
            double portfolioVaR = varModel.calculatePortfolioVaR(portfolio);

            assertTrue(portfolioVaR > 0, "Portfolio VaR debe ser positivo");
            assertTrue(portfolioVaR < 1_000_000, 
                "Portfolio VaR no debe exceder el notional total");
        }

        @Test
        @DisplayName("Debe calcular VaR de portfolio concentrado (alto riesgo)")
        void shouldCalculateConcentratedPortfolioVar() {
            String instrumentId = "CONCENTRATED";

            for (int i = 0; i < 40; i++) {
                double ret = (Math.random() - 0.5) * 0.04;
                varModel.updateReturns(instrumentId, ret, 
                    Instant.now().minusSeconds(i * 300));
            }

            List<InstrumentPosition> positions = List.of(
                new InstrumentPosition(instrumentId, 1_000_000, 100.0)
            );

            Position portfolio = new Position("TRADER_2", "CONCENTRATED_STRAT", positions);
            double portfolioVaR = varModel.calculatePortfolioVaR(portfolio);

            assertTrue(portfolioVaR > 50_000, 
                "Portfolio concentrado debe tener VaR significativo");
        }
    }

    @Nested
    @DisplayName("Peso del Order Book en Volatilidad")
    class OrderBookVolatilityTests {

        @Test
        @DisplayName("Debe integrar volatilidad del order book en VaR")
        void shouldIntegrateOrderBookVolatility() {
            String instrumentId = "ORDERBOOK_TEST";
            double[][] orderBook = generateMockOrderBook();

            for (int i = 0; i < 30; i++) {
                varModel.updateReturns(instrumentId, 
                    (Math.random() - 0.5) * 0.02, 
                    Instant.now().minusSeconds(i * 300));
            }

            double orderBookVol = varModel.getOrderBookVolatility(instrumentId, orderBook);
            double currentVol = varModel.getCurrentVolatility(instrumentId);

            assertTrue(orderBookVol > 0, 
                "Volatilidad del order book debe ser calculada");
            assertTrue(currentVol > 0, 
                "Volatilidad histórica debe existir");
        }

        private double[][] generateMockOrderBook() {
            double[][] orderBook = new double[20][4];
            double basePrice = 150.0;

            for (int i = 0; i < 10; i++) {
                orderBook[i][0] = basePrice - i * 0.01;
                orderBook[i][1] = Math.random() * 1000;
                orderBook[i][2] = basePrice + i * 0.01;
                orderBook[i][3] = Math.random() * 1000;
            }

            return orderBook;
        }
    }

    @Nested
    @DisplayName("Tests de Consistencia del Modelo")
    class ModelConsistencyTests {

        @Test
        @DisplayName("VaR debe aumentar con el nivel de confianza")
        void varShouldIncreaseWithConfidenceLevel() {
            String instrumentId = "CONFIDENCE_TEST";

            VaRModel model95 = new VaRModel(252, 0.95, 1440, 0.94, 30, WeightingScheme.EWMA);
            VaRModel model99 = new VaRModel(252, 0.99, 1440, 0.94, 30, WeightingScheme.EWMA);

            for (int i = 0; i < 50; i++) {
                double ret = (Math.random() - 0.5) * 0.03;
                model95.updateReturns(instrumentId, ret, Instant.now().minusSeconds(i * 300));
                model99.updateReturns(instrumentId, ret, Instant.now().minusSeconds(i * 300));
            }

            double var95 = model95.calculateVaR(instrumentId, 1_000_000);
            double var99 = model99.calculateVaR(instrumentId, 1_000_000);

            assertTrue(var99 > var95, 
                "VaR al 99% debe ser mayor que VaR al 95%");
        }

        @Test
        @DisplayName("VaR debe aumentar con el horizonte temporal")
        void varShouldIncreaseWithTimeHorizon() {
            String instrumentId = "HORIZON_TEST";

            VaRModel intraday = new VaRModel(252, 0.99, 60, 0.94, 30, WeightingScheme.EWMA);
            VaRModel daily = new VaRModel(252, 0.99, 1440, 0.94, 30, WeightingScheme.EWMA);

            for (int i = 0; i < 50; i++) {
                double ret = (Math.random() - 0.5) * 0.03;
                intraday.updateReturns(instrumentId, ret, Instant.now().minusSeconds(i * 300));
                daily.updateReturns(instrumentId, ret, Instant.now().minusSeconds(i * 300));
            }

            double varIntraday = intraday.calculateVaR(instrumentId, 1_000_000);
            double varDaily = daily.calculateVaR(instrumentId, 1_000_000);

            assertTrue(varDaily > varIntraday, 
                "VaR diario debe ser mayor que VaR intradia para el mismo instrumento");
        }
    }
}