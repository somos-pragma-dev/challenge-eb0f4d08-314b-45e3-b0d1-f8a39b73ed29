# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Evaluación de riesgo en tiempo real para trading algorítmico**.

| | |
|---|---|
| Tema | motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos |
| Nivel | master-l2 |
| Chapter | Generico |
| Especialidad | Inferido del contexto |
| Stack | Java 21 / LMAX Disruptor 4.0 |
| Patron arquitectonico | microservicio reactivo con anillo disruptor lock-free y sharding por instrumento |
| Tiempo estimado | 4 semanas |

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `el comando de build o arranque canonico del stack elegido` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `el comando de build o arranque canonico del stack elegido` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Diseño del motor de risk scoring**: Diagrama de arquitectura y descripción del flujo de trabajo.
- **Fase 2 — Implementación del modelo de VaR intraday**: Modelo de VaR intraday implementado y validado.
- **Fase 3 — Aplicación de límites y circuit breakers**: Lógica de aplicación de límites y circuit breakers implementada.
- **Fase 4 — Política de kill switch y estrategia de replay determinístico**: Política de kill switch y estrategia de replay determinístico implementadas y validadas.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Boilerplate del stack (1)

Sin esto el proyecto no compila ni arranca. **Es tu trabajo crearlo**, y no toca nada de lo pedagogico: es andamiaje del stack.

- [ ] **Punto de entrada del stack elegido** — Sin un punto de entrada reconocible, el runtime no tiene por donde arrancar la aplicacion.

### 2. Referencias colgando (56)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `OrderEvent`
      El import com.pragma.riskengine.disruptor.OrderEvent no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketDataEvent`
      El import com.pragma.riskengine.disruptor.MarketDataEvent no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/pragma/riskengine/model/VaRModel.java` — `InstrumentVaRData.getDataPointCount`
      Se invoca `getDataPointCount` sobre `InstrumentVaRData`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/model/VaRModel.java` — `Position.getPositions`
      Se invoca `getPositions` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/model/VaRModel.java` — `InstrumentVaRData.getReturns`
      Se invoca `getReturns` sobre `InstrumentVaRData`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `DynamicCircuitBreaker.isOpen`
      Se invoca `isOpen` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `KillSwitchPolicy.shouldStop`
      Se invoca `shouldStop` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `DynamicCircuitBreaker.recordSuccess`
      Se invoca `recordSuccess` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `MiFIDIITracer.traceDecision`
      Se invoca `traceDecision` sobre `MiFIDIITracer`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `DynamicCircuitBreaker.recordFailure`
      Se invoca `recordFailure` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `VaRCalculator.calculateVaR`
      Se invoca `calculateVaR` sobre `VaRCalculator`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `LimitService.checkTraderLimit`
      Se invoca `checkTraderLimit` sobre `LimitService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `LimitService.checkStrategyLimit`
      Se invoca `checkStrategyLimit` sobre `LimitService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `LimitService.getCurrentExposure`
      Se invoca `getCurrentExposure` sobre `LimitService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `VaRCalculator.updateReturns`
      Se invoca `updateReturns` sobre `VaRCalculator`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/var/VaRCalculator.java` — `Position.positions`
      Se invoca `positions` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/var/VaRCalculator.java` — `Position.traderId`
      Se invoca `traderId` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `Position.traderId`
      Se invoca `traderId` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `Position.strategyId`
      Se invoca `strategyId` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `MarketDataEvent.instrumentId`
      Se invoca `instrumentId` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `MarketDataEvent.volatility`
      Se invoca `volatility` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `Position.positions`
      Se invoca `positions` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java` — `CircuitBreakerConfig.getFailureRateThreshold`
      Se invoca `getFailureRateThreshold` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java` — `CircuitBreakerConfig.getSlidingWindowSize`
      Se invoca `getSlidingWindowSize` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java` — `CircuitBreakerConfig.getMinimumNumberOfCalls`
      Se invoca `getMinimumNumberOfCalls` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.isInCooldown`
      Se invoca `isInCooldown` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.getCooldownEnd`
      Se invoca `getCooldownEnd` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.isKilled`
      Se invoca `isKilled` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.getConsecutiveAnomalyCount`
      Se invoca `getConsecutiveAnomalyCount` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.getConsecutiveAnomalyCount`
      Se invoca `getConsecutiveAnomalyCount` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.getLastPosition`
      Se invoca `getLastPosition` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.getLastPositionUpdate`
      Se invoca `getLastPositionUpdate` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.activateKillSwitch`
      Se invoca `activateKillSwitch` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.activateKillSwitch`
      Se invoca `activateKillSwitch` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `DecisionContext.toMap`
      Se invoca `toMap` sobre `DecisionContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `DecisionContext.addRiskCheck`
      Se invoca `addRiskCheck` sobre `DecisionContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `DecisionContext.complete`
      Se invoca `complete` sobre `DecisionContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketDataSnapshot.update`
      Se invoca `update` sobre `MarketDataSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketDataSnapshot.getSnapshot`
      Se invoca `getSnapshot` sobre `MarketDataSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketContext.toMap`
      Se invoca `toMap` sobre `MarketContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.orderId`
      Se invoca `orderId` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.instrumentId`
      Se invoca `instrumentId` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.side`
      Se invoca `side` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.timestamp`
      Se invoca `timestamp` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.isAvailable`
      Se invoca `isAvailable` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.execute`
      Se invoca `execute` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `CircuitBreakerConfig.recoveryTimeoutMs`
      Se invoca `recoveryTimeoutMs` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getFailureCount`
      Se invoca `getFailureCount` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getCurrentThreshold`
      Se invoca `getCurrentThreshold` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.updateVolatility`
      Se invoca `updateVolatility` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getTotalCalls`
      Se invoca `getTotalCalls` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getSuccessCount`
      Se invoca `getSuccessCount` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getLastFailureTime`
      Se invoca `getLastFailureTime` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `CircuitBreakerConfig.slidingWindowSize`
      Se invoca `slidingWindowSize` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.updateConfig`
      Se invoca `updateConfig` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.reset`
      Se invoca `reset` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (17)

- `pom.xml`
- `src/main/resources/application.properties`
- `src/main/java/com/pragma/riskengine/model/VaRModel.java`
- `src/main/java/com/pragma/riskengine/RiskEngineMain.java`
- `src/main/java/com/pragma/riskengine/disruptor/OrderEvent.java`
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java`
- `src/main/java/com/pragma/riskengine/disruptor/MarketDataEvent.java`
- `src/main/java/com/pragma/riskengine/var/VaRCalculator.java`
- `src/main/java/com/pragma/riskengine/limits/LimitService.java`
- `src/main/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreaker.java`
- `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java`
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java`
- `src/main/java/com/pragma/riskengine/replay/DeterministicReplay.java`
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java`
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java`
- `src/test/java/com/pragma/riskengine/var/VaRCalculatorTest.java`
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/pragma/riskengine`
- `src/main/java/com/pragma/riskengine/disruptor`
- `src/main/java/com/pragma/riskengine/model`
- `src/main/java/com/pragma/riskengine/var`
- `src/main/java/com/pragma/riskengine/limits`
- `src/main/java/com/pragma/riskengine/circuitbreaker`
- `src/main/java/com/pragma/riskengine/killswitch`
- `src/main/java/com/pragma/riskengine/replay`
- `src/main/java/com/pragma/riskengine/compliance`
- `src/main/resources`
- `src/test/java/com/pragma/riskengine`

## Verificacion

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **microservicio reactivo con anillo disruptor lock-free y sharding por instrumento**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Brecha que el reto ataca: Sistema master-l2 que evalúa el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
