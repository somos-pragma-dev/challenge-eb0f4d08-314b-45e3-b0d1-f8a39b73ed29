# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Boilerplate del stack que falta

Sin esto no compila ni arranca. Es andamiaje, no toca nada de lo pedagogico:

- **Punto de entrada del stack elegido** — Sin un punto de entrada reconocible, el runtime no tiene por donde arrancar la aplicacion.

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `OrderEvent`: El import com.pragma.riskengine.disruptor.OrderEvent no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketDataEvent`: El import com.pragma.riskengine.disruptor.MarketDataEvent no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/pragma/riskengine/model/VaRModel.java` — `InstrumentVaRData.getDataPointCount`: Se invoca `getDataPointCount` sobre `InstrumentVaRData`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/model/VaRModel.java` — `Position.getPositions`: Se invoca `getPositions` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/model/VaRModel.java` — `InstrumentVaRData.getReturns`: Se invoca `getReturns` sobre `InstrumentVaRData`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `DynamicCircuitBreaker.isOpen`: Se invoca `isOpen` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `KillSwitchPolicy.shouldStop`: Se invoca `shouldStop` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `DynamicCircuitBreaker.recordSuccess`: Se invoca `recordSuccess` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `MiFIDIITracer.traceDecision`: Se invoca `traceDecision` sobre `MiFIDIITracer`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `DynamicCircuitBreaker.recordFailure`: Se invoca `recordFailure` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `VaRCalculator.calculateVaR`: Se invoca `calculateVaR` sobre `VaRCalculator`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `LimitService.checkTraderLimit`: Se invoca `checkTraderLimit` sobre `LimitService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `LimitService.checkStrategyLimit`: Se invoca `checkStrategyLimit` sobre `LimitService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `LimitService.getCurrentExposure`: Se invoca `getCurrentExposure` sobre `LimitService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java` — `VaRCalculator.updateReturns`: Se invoca `updateReturns` sobre `VaRCalculator`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/var/VaRCalculator.java` — `Position.positions`: Se invoca `positions` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/var/VaRCalculator.java` — `Position.traderId`: Se invoca `traderId` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `Position.traderId`: Se invoca `traderId` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `Position.strategyId`: Se invoca `strategyId` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `MarketDataEvent.instrumentId`: Se invoca `instrumentId` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `MarketDataEvent.volatility`: Se invoca `volatility` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/limits/LimitService.java` — `Position.positions`: Se invoca `positions` sobre `Position`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java` — `CircuitBreakerConfig.getFailureRateThreshold`: Se invoca `getFailureRateThreshold` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java` — `CircuitBreakerConfig.getSlidingWindowSize`: Se invoca `getSlidingWindowSize` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java` — `CircuitBreakerConfig.getMinimumNumberOfCalls`: Se invoca `getMinimumNumberOfCalls` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.isInCooldown`: Se invoca `isInCooldown` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.getCooldownEnd`: Se invoca `getCooldownEnd` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.isKilled`: Se invoca `isKilled` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.getConsecutiveAnomalyCount`: Se invoca `getConsecutiveAnomalyCount` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.getConsecutiveAnomalyCount`: Se invoca `getConsecutiveAnomalyCount` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.getLastPosition`: Se invoca `getLastPosition` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.getLastPositionUpdate`: Se invoca `getLastPositionUpdate` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `TraderKillSwitchState.activateKillSwitch`: Se invoca `activateKillSwitch` sobre `TraderKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java` — `StrategyKillSwitchState.activateKillSwitch`: Se invoca `activateKillSwitch` sobre `StrategyKillSwitchState`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `DecisionContext.toMap`: Se invoca `toMap` sobre `DecisionContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `DecisionContext.addRiskCheck`: Se invoca `addRiskCheck` sobre `DecisionContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `DecisionContext.complete`: Se invoca `complete` sobre `DecisionContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketDataSnapshot.update`: Se invoca `update` sobre `MarketDataSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketDataSnapshot.getSnapshot`: Se invoca `getSnapshot` sobre `MarketDataSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java` — `MarketContext.toMap`: Se invoca `toMap` sobre `MarketContext`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.orderId`: Se invoca `orderId` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.instrumentId`: Se invoca `instrumentId` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.side`: Se invoca `side` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java` — `OrderEvent.timestamp`: Se invoca `timestamp` sobre `OrderEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.isAvailable`: Se invoca `isAvailable` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.execute`: Se invoca `execute` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `CircuitBreakerConfig.recoveryTimeoutMs`: Se invoca `recoveryTimeoutMs` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getFailureCount`: Se invoca `getFailureCount` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getCurrentThreshold`: Se invoca `getCurrentThreshold` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.updateVolatility`: Se invoca `updateVolatility` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getTotalCalls`: Se invoca `getTotalCalls` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getSuccessCount`: Se invoca `getSuccessCount` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.getLastFailureTime`: Se invoca `getLastFailureTime` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `CircuitBreakerConfig.slidingWindowSize`: Se invoca `slidingWindowSize` sobre `CircuitBreakerConfig`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.updateConfig`: Se invoca `updateConfig` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java` — `DynamicCircuitBreaker.reset`: Se invoca `reset` sobre `DynamicCircuitBreaker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

## Como saber que terminaste

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Contexto técnico original
Sistema master-l2 que evalúa el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

### Reto
- Tema: motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos
- Seniority: master-l2
- Tipo: mixed
- Título: Evaluación de riesgo en tiempo real para trading algorítmico
- Tiempo estimado: 4 semanas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Diseño del motor de risk scoring — objetivo: Definir la arquitectura y el flujo de trabajo del motor de risk scoring. — entregable (NO resolver): Diagrama de arquitectura y descripción del flujo de trabajo.
- Fase 2: Implementación del modelo de VaR intraday — objetivo: Implementar y validar el modelo de VaR intraday. — entregable (NO resolver): Modelo de VaR intraday implementado y validado.
- Fase 3: Aplicación de límites y circuit breakers — objetivo: Aplicar límites por trader/estrategia/instrumento y disparar circuit breakers dinámicos. — entregable (NO resolver): Lógica de aplicación de límites y circuit breakers implementada.
- Fase 4: Política de kill switch y estrategia de replay determinístico — objetivo: Definir la política de kill switch y la estrategia de replay determinístico. — entregable (NO resolver): Política de kill switch y estrategia de replay determinístico implementadas y validadas.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.pragma</groupId>
    <artifactId>risk-engine</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>Risk Engine</name>
    <description>Real-time risk scoring engine for algorithmic trading with LMAX Disruptor</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <disruptor.version>4.0.0</disruptor.version>
        <resilience4j.version>2.1.0</resilience4j.version>
        <junit.version>5.10.0</junit.version>
        <mockito.version>5.5.0</mockito.version>
    </properties>

    <dependencies>
        <!-- LMAX Disruptor for lock-free ring buffer processing -->
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>${disilience4j.version}</version>
        </dependency>

        <!-- Resilience4j for circuit breaker, retry and bulkhead patterns -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-circuitbreaker</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-retry</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-bulkhead</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>

        <!-- SLF4J for logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>

        <!-- Test dependencies -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <release>21</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/resources/application.properties ===
# Risk Engine Configuration
# All times in microseconds unless otherwise specified

# Disruptor Ring Buffer Configuration
disruptor.ring-buffer.size=16384
disruptor.wait-strategy=BlockingWaitStrategy
disruptor.thread-count=8
disruptor.batch-size=256

# VaR Model Configuration
var.lookback-period-days=250
var.confidence-level=0.99
var.horizon-minutes=1
var.minimum-data-points=30
var.decay-factor=0.94
var.weighting-scheme=EXPONENTIAL

# Risk Limits Configuration
limits.position.max-notional-per-instrument=5000000
limits.position.max-notional-per-trader=20000000
limits.position.max-notional-per-strategy=10000000
limits.order.max-size=1000000
limits.order.max-leverage=10
limits.daily.loss-threshold=-500000
limits.daily.profit-threshold=2000000

# Circuit Breaker Configuration
circuit-breaker.enabled=true
circuit-breaker.failure-rate-threshold=50
circuit-breaker.wait-duration-in-open-state=30000
circuit-breaker.sliding-window-size=100
circuit-breaker.minimum-number-of-calls=10
circuit-breaker.permitted-number-of-calls-in-half-open-state=3
circuit-breaker.auto-transition-from-open-to-half-open-enabled=true
circuit-breaker.slow-call-duration-threshold=5000
circuit-breaker.slow-call-rate-threshold=80

# Dynamic Circuit Breaker Thresholds (based on volatility)
circuit-breaker.volatility.low-threshold=0.15
circuit-breaker.volatility.medium-threshold=0.30
circuit-breaker.volatility.high-threshold=0.50
circuit-breaker.volatility.extreme-threshold=0.80
circuit-breaker.volatility.scale-factor=1.5

# Kill Switch Configuration
kill-switch.enabled=true
kill-switch.max-consecutive-rejections=5
kill-switch.max-latency-us=500
kill-switch.observation-window-ms=1000
kill-switch.anomaly-threshold-std-dev=3.0
kill-switch.cooldown-period-ms=60000

# Replay Configuration
replay.enabled=true
replay.log-directory=/var/log/risk-engine/events
replay.max-replay-duration-hours=24
replay.parallel-threads=4
replay.checkpoint-interval-seconds=60

# MiFID II Compliance
mifid.trace-enabled=true
trace.timestamp-precision=NANOS
trace.max-trace-depth=1000
trace.context-fields=TRADER_ID,STRATEGY_ID,INSTRUMENT_ID,ORDER_ID,SESSION_ID

# Market Data Configuration
market-data.orderbook-levels=10
market-data.snapshot-interval-ms=100
market-data.max-latency-us=100
market-data.stale-data-threshold-ms=500

# Performance Tuning
gc.algorithm=ZGC
thread-pool.size=16
thread-pool.queue-capacity=2048
metrics.enabled=true
metrics.interval-ms=1000

// === ARCHIVO: src/main/java/com/pragma/riskengine/model/VaRModel.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/RiskEngineMain.java ===
package com.pragma.riskengine;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import com.pragma.riskengine.disruptor.OrderEventHandler;
import com.pragma.riskengine.model.VaRModel;
import com.pragma.riskengine.model.VaRModel.WeightingScheme;
import com.pragma.riskengine.model.VaRModel.Position;
import com.pragma.riskengine.var.VaRCalculator;
import com.pragma.riskengine.limits.LimitService;
import com.pragma.riskengine.circuitbreaker.DynamicCircuitBreaker;
import com.pragma.riskengine.circuitbreaker.CircuitBreakerConfig;
import com.pragma.riskengine.killswitch.KillSwitchPolicy;
import com.pragma.riskengine.compliance.MiFIDIITracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RiskEngineMain {
    private static final Logger logger = LoggerFactory.getLogger(RiskEngineMain.class);
    private static final int RING_BUFFER_SIZE = 1 << 16;
    private static final int WORKER_THREADS = 8;
    private static final long STARTUP_TIMEOUT_SECONDS = 30;

    private final Disruptor<OrderEvent> disruptor;
    private final VaRModel varModel;
    private final VaRCalculator varCalculator;
    private final LimitService limitService;
    private final DynamicCircuitBreaker circuitBreaker;
    private final KillSwitchPolicy killSwitchPolicy;
    private final MiFIDIITracer tracer;
    private final AtomicBoolean running;
    private final ExecutorService executor;

    public RiskEngineMain() {
        this.running = new AtomicBoolean(false);
        this.executor = createWorkerExecutor();
        this.varModel = createVaRModel();
        this.varCalculator = new VaRCalculator(varModel);
        this.limitService = new LimitService();
        this.circuitBreaker = createCircuitBreaker();
        this.killSwitchPolicy = new KillSwitchPolicy();
        this.tracer = new MiFIDIITracer();
        this.disruptor = createDisruptor();
    }

    private VaRModel createVaRModel() {
        return new VaRModel(
            252,
            0.99,
            1440,
            0.94,
            30,
            WeightingScheme.EWMA
        );
    }

    private DynamicCircuitBreaker createCircuitBreaker() {
        CircuitBreakerConfig config = new CircuitBreakerConfig(
            100,
            0.5,
            30_000_000_000L,
            0.7,
            0.95,
            5
        );
        return new DynamicCircuitBreaker("risk-engine-cb", config);
    }

    private Disruptor<OrderEvent> createDisruptor() {
        WaitStrategy waitStrategy = new BlockingWaitStrategy();
        
        RingBuffer<OrderEvent> ringBuffer = RingBuffer.create(
            ProducerType.MULTI,
            OrderEvent::new,
            RING_BUFFER_SIZE,
            waitStrategy
        );

        SequenceBarrier barrier = ringBuffer.newBarrier();
        
        OrderEventHandler[] handlers = createEventHandlers();
        
        WorkerPool<OrderEvent> workerPool = new WorkerPool<>(
            ringBuffer,
            barrier,
            new IgnoreExceptionHandler(),
            handlers
        );

        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        
        return new Disruptor<>(
            new OrderEventFactory(),
            RING_BUFFER_SIZE,
            executor,
            ProducerType.MULTI,
            waitStrategy
        );
    }

    private OrderEventHandler[] createEventHandlers() {
        OrderEventHandler[] handlers = new OrderEventHandler[WORKER_THREADS];
        for (int i = 0; i < WORKER_THREADS; i++) {
            handlers[i] = new OrderEventHandler(
                varCalculator,
                limitService,
                circuitBreaker,
                killSwitchPolicy,
                tracer
            );
        }
        return handlers;
    }

    private ExecutorService createWorkerExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int counter = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "risk-engine-worker-" + counter++);
                t.setDaemon(true);
                t.setPriority(Thread.MAX_PRIORITY);
                return t;
            }
        };
        
        return new ThreadPoolExecutor(
            WORKER_THREADS,
            WORKER_THREADS * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void start() {
        if (!running.compareAndSet(false, true)) {
            logger.warn("Risk Engine ya está en ejecución");
            return;
        }

        disruptor.start();
        logger.info("Risk Engine iniciado con {} workers y buffer de {} eventos", 
            WORKER_THREADS, RING_BUFFER_SIZE);
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) {
            logger.warn("Risk Engine no está en ejecución");
            return;
        }

        logger.info("Deteniendo Risk Engine...");
        disruptor.shutdown(10, TimeUnit.SECONDS);
        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logger.info("Risk Engine detenido");
    }

    public void publishOrder(String traderId, String strategyId, String instrumentId,
                             String orderId, double quantity, double price, String side) {
        if (!running.get()) {
            logger.warn("Intento de publicar orden con engine detenido");
            return;
        }

        long sequence = disruptor.next();
        try {
            OrderEvent event = disruptor.get(sequence);
            event.setTraderId(traderId);
            event.setStrategyId(strategyId);
            event.setInstrumentId(instrumentId);
            event.setOrderId(orderId);
            event.setQuantity(quantity);
            event.setPrice(price);
            event.setSide(side);
            event.setReceivedTimeNanos(System.nanoTime());
        } finally {
            disruptor.publish(sequence);
        }
    }

    public VaRModel getVarModel() {
        return varModel;
    }

    public DynamicCircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public KillSwitchPolicy getKillSwitchPolicy() {
        return killSwitchPolicy;
    }

    public boolean isRunning() {
        return running.get();
    }

    public static void main(String[] args) {
        logger.info("Inicializando Risk Engine...");
        
        RiskEngineMain engine = new RiskEngineMain();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Signal de shutdown recibido");
            engine.stop();
        }));

        engine.start();

        engine.publishOrder("TRADER-001", "STRAT-ALPHA", "AAPL", 
            "ORD-001", 1000, 150.50, "BUY");
        engine.publishOrder("TRADER-002", "STRAT-BETA", "GOOGL", 
            "ORD-002", 500, 2800.00, "SELL");

        logger.info("Ordenes de prueba publicadas");
    }

    private static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }

    private static class IgnoreExceptionHandler implements ExceptionHandler<OrderEvent> {
        private static final Logger logger = LoggerFactory.getLogger(IgnoreExceptionHandler.class);
        
        @Override
        public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
            logger.error("Excepción procesando evento {}: {}", sequence, ex.getMessage());
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            logger.error("Error al iniciar disruptor: {}", ex.getMessage());
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            logger.error("Error al detener disruptor: {}", ex.getMessage());
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/OrderEvent.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/OrderEventHandler.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/MarketDataEvent.java ===
package com.pragma.riskengine.disruptor;

import com.pragma.riskengine.model.VaRModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/var/VaRCalculator.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/limits/LimitService.java ===
package com.pragma.riskengine.limits;

import com.pragma.riskengine.model.VaRModel;
import com.pragma.riskengine.model.VaRModel.Position;
import com.pragma.riskengine.model.VaRModel.InstrumentPosition;
import com.pragma.riskengine.disruptor.MarketDataEvent;
import com.pragma.riskengine.circuitbreaker.DynamicCircuitBreaker;
import com.pragma.riskengine.circuitbreaker.CircuitBreakerConfig;
import com.pragma.riskengine.killswitch.KillSwitchPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LimitService {
    private static final Logger log = LoggerFactory.getLogger(LimitService.class);
    private static final double DEFAULT_VOLATILITY_MULTIPLIER = 2.5;
    private static final double MIN_LIMIT_MULTIPLIER = 1.5;
    private static final double MAX_LIMIT_MULTIPLIER = 5.0;
    private static final double CONCENTRATION_LIMIT_PCT = 0.25;
    private static final double TRADING_THRESHOLD_PCT = 0.80;

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
            boolean killSwitchTriggered = killSwitchPolicy.evaluate(position, violations);
            if (killSwitchTriggered) {
                log.error("Kill switch triggered for trader {} due to limit violations", position.traderId());
                circuitBreaker.recordFailure(position.traderId());
            }
        }
        circuitBreaker.recordFailure(position.traderId());
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreaker.java ===
package com.pragma.riskengine.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.StateTransition;
import io.github.resilience4j.circuitbreaker.CircuitBreakerTransitionListener;
import io.github.resilience4j.circuitbreaker.CircuitBreakerEvent;
import io.github.resilience4j.circuitbreaker.CircuitBreakerEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Circuit breaker dinámico con thresholds adaptativos basados en volatilidad del instrumento.
 * Implementa la estrategia de sharding por instrumento para evitar consenso distribuido.
 */
public class DynamicCircuitBreaker {
    private static final Logger log = LoggerFactory.getLogger(DynamicCircuitBreaker.class);
    
    private static final double DEFAULT_FAILURE_RATE_THRESHOLD = 50.0;
    private static final double DEFAULT_SLOW_CALL_RATE_THRESHOLD = 50.0;
    private static final int DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS = 1000;
    private static final int DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE = 10;
    private static final int DEFAULT_SLIDING_WINDOW_SIZE = 100;
    private static final double VOLATILITY_SCALING_FACTOR = 2.0;
    private static final double MIN_THRESHOLD = 20.0;
    private static final double MAX_THRESHOLD = 80.0;
    
    private final CircuitBreakerRegistry registry;
    private final Map<String, CircuitBreaker> circuitBreakers;
    private final Map<String, CircuitBreakerMetrics> metricsCache;
    private final AtomicReference<Instant> lastRecalculation;
    private final Duration recalculationInterval;
    private volatile double globalFailureThreshold;
    private volatile double globalSlowCallThreshold;
    
    public DynamicCircuitBreaker(CircuitBreakerConfig baseConfig) {
        this.circuitBreakers = new ConcurrentHashMap<>();
        this.metricsCache = new ConcurrentHashMap<>();
        this.lastRecalculation = new AtomicReference<>(Instant.now());
        this.recalculationInterval = Duration.ofSeconds(30);
        this.globalFailureThreshold = DEFAULT_FAILURE_RATE_THRESHOLD;
        this.globalSlowCallThreshold = DEFAULT_SLOW_CALL_RATE_THRESHOLD;
        
        CircuitBreakerConfig defaultConfig = buildDefaultConfig();
        this.registry = CircuitBreakerRegistry.of(defaultConfig);
        registerGlobalTransitionListener();
        log.info("DynamicCircuitBreaker inicializado con thresholds adaptativos");
    }
    
    private CircuitBreakerConfig buildDefaultConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold((float) DEFAULT_FAILURE_RATE_THRESHOLD)
                .slowCallRateThreshold((float) DEFAULT_SLOW_CALL_RATE_THRESHOLD)
                .slowCallDurationThreshold(Duration.ofMillis(DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS))
                .permittedNumberOfCallsInHalfOpenState(DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
                .minimumNumberOfCalls(10)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private void registerGlobalTransitionListener() {
        registry.getEventPublisher()
                .onStateTransition(this::handleStateTransition);
    }
    
    private void handleStateTransition(StateTransition transition) {
        String breakerName = transition.getStateTransitionEvent().getCircuitBreakerName();
        State fromState = transition.getFromState();
        State toState = transition.getToState();
        
        log.warn("CircuitBreaker '{}' transición: {} -> {}", 
                breakerName, fromState, toState);
        
        if (toState == State.OPEN) {
            notifyKillSwitchIfNeeded(breakerName);
        }
    }
    
    private void notifyKillSwitchIfNeeded(String breakerName) {
        log.error("Circuit breaker '{}' abierto - notificando al sistema de KillSwitch", breakerName);
    }
    
    /**
     * Obtiene o crea un circuit breaker para un instrumento específico.
     * Los thresholds se adaptan automáticamente según la volatilidad del instrumento.
     */
    public CircuitBreaker getCircuitBreaker(String instrumentId, double currentVolatility) {
        return circuitBreakers.computeIfAbsent(instrumentId, 
                id -> createCircuitBreakerForInstrument(id, currentVolatility));
    }
    
    private CircuitBreaker createCircuitBreakerForInstrument(String instrumentId, double volatility) {
        double adjustedFailureThreshold = calculateAdjustedThreshold(volatility, globalFailureThreshold);
        double adjustedSlowCallThreshold = calculateAdjustedThreshold(volatility, globalSlowCallThreshold);
        
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold((float) adjustedFailureThreshold)
                .slowCallRateThreshold((float) adjustedSlowCallThreshold)
                .slowCallDurationThreshold(Duration.ofMillis(
                        calculateAdjustedSlowCallDuration(volatility)))
                .permittedNumberOfCallsInHalfOpenState(DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
                .minimumNumberOfCalls(10)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        
        CircuitBreaker breaker = registry.circuitBreaker(instrumentId, config);
        registerInstrumentListeners(breaker, instrumentId);
        
        log.info("CircuitBreaker creado para instrumento '{}' con failureThreshold={}%, slowCallThreshold={}%",
                instrumentId, adjustedFailureThreshold, adjustedSlowCallThreshold);
        
        return breaker;
    }
    
    private double calculateAdjustedThreshold(double volatility, double baseThreshold) {
        double scaledVolatility = Math.min(volatility * VOLATILITY_SCALING_FACTOR, MAX_THRESHOLD);
        double adjusted = baseThreshold - scaledVolatility;
        return Math.max(adjusted, MIN_THRESHOLD);
    }
    
    private int calculateAdjustedSlowCallDuration(double volatility) {
        int baseDuration = DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS;
        int adjusted = (int) (baseDuration * (1 + volatility));
        return Math.min(adjusted, 5000);
    }
    
    private void registerInstrumentListeners(CircuitBreaker breaker, String instrumentId) {
        breaker.getEventPublisher()
                .onFailureRateExceeded(event -> log.warn(
                        "Instrumento '{}' - Tasa de fallo {}% excede threshold {}%",
                        instrumentId, event.getFailureRate(), event.getCircuitBreakerConfig().getFailureRateThreshold()))
                .onSlowCallRateExceeded(event -> log.warn(
                        "Instrumento '{}' - Tasa de llamadas lentas {}% excede threshold {}%",
                        instrumentId, event.getSlowCallRate(), event.getCircuitBreakerConfig().getSlowCallRateThreshold()))
                .onStateTransition(transition -> log.info(
                        "Instrumento '{}' - Transición de estado: {} -> {}",
                        instrumentId, transition.getFromState(), transition.getToState()));
    }
    
    /**
     * Ejecuta una operación protegida por circuit breaker.
     * Utiliza el patrón de sharding por instrumento para evitar bloqueo.
     */
    public <T> T executeWithCircuitBreaker(String instrumentId, double volatility,
                                            Supplier<T> operation, Supplier<T> fallback) {
        CircuitBreaker breaker = getCircuitBreaker(instrumentId, volatility);
        
        if (breaker.getState() == State.OPEN) {
            log.debug("CircuitBreaker '{}' abierto - ejecutando fallback", instrumentId);
            return fallback.get();
        }
        
        return CircuitBreaker.decorateSupplier(breaker, () -> {
            T result = operation.get();
            updateMetrics(instrumentId, result);
            return result;
        }).get();
    }
    
    private void updateMetrics(String instrumentId, Object result) {
        metricsCache.computeIfAbsent(instrumentId, k -> new CircuitBreakerMetrics())
                .recordSuccess();
    }
    
    /**
     * Recalcula los thresholds globales basándose en métricas acumuladas.
     */
    public void recalculateThresholdsIfNeeded() {
        Instant now = Instant.now();
        if (Duration.between(lastRecalculation.get(), now).compareTo(recalculationInterval) > 0) {
            recalculateGlobalThresholds();
            lastRecalculation.set(now);
        }
    }
    
    private void recalculateGlobalThresholds() {
        double totalFailureRate = 0;
        double totalSlowCallRate = 0;
        int breakerCount = circuitBreakers.size();
        
        if (breakerCount == 0) return;
        
        for (Map.Entry<String, CircuitBreaker> entry : circuitBreakers.entrySet()) {
            CircuitBreaker cb = entry.getValue();
            totalFailureRate += cb.getMetrics().getFailureRate();
            totalSlowCallRate += cb.getMetrics().getSlowCallRate();
        }
        
        globalFailureThreshold = Math.min(totalFailureRate / breakerCount + 10, MAX_THRESHOLD);
        globalSlowCallThreshold = Math.min(totalSlowCallRate / breakerCount + 10, MAX_THRESHOLD);
        
        log.info("Thresholds globales recalculados: failure={}%, slowCall={}%",
                globalFailureThreshold, globalSlowCallThreshold);
    }
    
    /**
     * Fuerza la apertura de un circuit breaker específico (para testing o emergencia).
     */
    public void forceOpenCircuitBreaker(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        if (breaker != null) {
            breaker.transitionToOpenState();
            log.warn("CircuitBreaker '{}'_forzadamente abierto", instrumentId);
        }
    }
    
    /**
     * Fuerza el cierre de un circuit breaker específico.
     */
    public void forceCloseCircuitBreaker(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        if (breaker != null) {
            breaker.transitionToClosedState();
            log.info("CircuitBreaker '{}'_forzadamente cerrado", instrumentId);
        }
    }
    
    public CircuitBreakerRegistry getRegistry() {
        return registry;
    }
    
    public Map<String, CircuitBreaker> getCircuitBreakers() {
        return Map.copyOf(circuitBreakers);
    }
    
    public State getState(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        return breaker != null ? breaker.getState() : State.CLOSED;
    }
    
    public double getFailureRate(String instrumentId) {
        CircuitBreaker breaker = circuitBreakers.get(instrumentId);
        return breaker != null ? breaker.getMetrics().getFailureRate() : 0.0;
    }
    
    private static class CircuitBreakerMetrics {
        private int successCount = 0;
        private int failureCount = 0;
        
        synchronized void recordSuccess() {
            successCount++;
        }
        
        synchronized void recordFailure() {
            failureCount++;
        }
        
        public int getSuccessCount() {
            return successCount;
        }
        
        public int getFailureCount() {
            return failureCount;
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java ===
package com.pragma.riskengine.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuración centralizada de Resilience4j para el motor de risk scoring.
 * Optimizada para latencia ultra-baja (< 500 microsegundos p99).
 */
public class CircuitBreakerConfig {
    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerConfig.class);
    
    // Configuración de Circuit Breaker
    private static final float DEFAULT_FAILURE_RATE_THRESHOLD = 50.0f;
    private static final float DEFAULT_SLOW_CALL_RATE_THRESHOLD = 50.0f;
    private static final int DEFAULT_SLOW_CALL_DURATION_THRESHOLD_MS = 1000;
    private static final int DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE = 10;
    private static final int DEFAULT_SLIDING_WINDOW_SIZE = 100;
    private static final int DEFAULT_MINIMUM_NUMBER_OF_CALLS = 10;
    private static final Duration DEFAULT_WAIT_DURATION_IN_OPEN_STATE = Duration.ofSeconds(30);
    
    // Configuración de Retry
    private static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;
    private static final Duration DEFAULT_RETRY_WAIT_DURATION = Duration.ofMillis(100);
    private static final float DEFAULT_RETRY_MULTIPLIER = 2.0f;
    private static final Duration DEFAULT_RETRY_MAX_DURATION = Duration.ofSeconds(10);
    
    // Configuración de Bulkhead
    private static final int DEFAULT_BULKHEAD_MAX_CONCURRENT_CALLS = 100;
    private static final int DEFAULT_BULKHEAD_MAX_WAIT_DURATION_MS = 500;
    
    private final Map<String, CircuitBreakerConfig> circuitBreakerConfigs;
    private final Map<String, RetryConfig> retryConfigs;
    private final Map<String, BulkheadConfig> bulkheadConfigs;
    
    private CircuitBreakerConfig() {
        this.circuitBreakerConfigs = new HashMap<>();
        this.retryConfigs = new HashMap<>();
        this.bulkheadConfigs = new HashMap<>();
        initializeDefaultConfigs();
    }
    
    private void initializeDefaultConfigs() {
        // Configuración por defecto para risk calculations
        circuitBreakerConfigs.put("default", createDefaultCircuitBreakerConfig());
        circuitBreakerConfigs.put("var-calculation", createVaRCalculationConfig());
        circuitBreakerConfigs.put("limit-check", createLimitCheckConfig());
        circuitBreakerConfigs.put("market-data", createMarketDataConfig());
        
        // Configuraciones de retry
        retryConfigs.put("default", createDefaultRetryConfig());
        retryConfigs.put("aggressive", createAggressiveRetryConfig());
        retryConfigs.put("conservative", createConservativeRetryConfig());
        
        // Configuraciones de bulkhead
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
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
                .minimumNumberOfCalls(DEFAULT_MINIMUM_NUMBER_OF_CALLS)
                .waitDurationInOpenState(DEFAULT_WAIT_DURATION_IN_OPEN_STATE)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createVaRCalculationConfig() {
        // VaR calculation es más tolerante a fallos parciales
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(60.0f)
                .slowCallRateThreshold(40.0f)
                .slowCallDurationThreshold(Duration.ofMillis(500))
                .permittedNumberOfCallsInHalfOpenState(5)
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(50)
                .minimumNumberOfCalls(5)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createLimitCheckConfig() {
        // Limit check debe ser muy sensible - latencia crítica
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(30.0f)
                .slowCallRateThreshold(30.0f)
                .slowCallDurationThreshold(Duration.ofMillis(200))
                .permittedNumberOfCallsInHalfOpenState(15)
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(200)
                .minimumNumberOfCalls(20)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }
    
    private io.github.resilience4j.circuitbreaker.CircuitBreakerConfig createMarketDataConfig() {
        // Market data puede ser más tolerante - datos históricos
        return io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                .failureRateThreshold(70.0f)
                .slowCallRateThreshold(60.0f)
                .slowCallDurationThreshold(Duration.ofMillis(2000))
                .permittedNumberOfCallsInHalfOpenState(3)
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
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
    
    /**
     * Obtiene la configuración de circuit breaker por nombre.
     */
    public io.github.resilience4j.circuitbreaker.CircuitBreakerConfig getCircuitBreakerConfig(String name) {
        return circuitBreakerConfigs.getOrDefault(name, circuitBreakerConfigs.get("default"));
    }
    
    /**
     * Obtiene la configuración de retry por nombre.
     */
    public RetryConfig getRetryConfig(String name) {
        return retryConfigs.getOrDefault(name, retryConfigs.get("default"));
    }
    
    /**
     * Obtiene la configuración de bulkhead por nombre.
     */
    public BulkheadConfig getBulkheadConfig(String name) {
        return bulkheadConfigs.getOrDefault(name, bulkheadConfigs.get("default"));
    }
    
    /**
     * Crea una instancia singleton de configuración.
     */
    public static CircuitBreakerConfig create() {
        return new CircuitBreakerConfig();
    }
    
    /**
     * Valida que todas las configuraciones sean consistentes.
     */
    public boolean validate() {
        boolean valid = true;
        
        for (Map.Entry<String, io.github.resilience4j.circuitbreaker.CircuitBreakerConfig> entry : 
                circuitBreakerConfigs.entrySet()) {
            io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = entry.getValue();
            
            if (config.getFailureRateThreshold() < 0 || config.getFailureRateThreshold() > 100) {
                log.error("Configuración '{}' - failureRateThreshold inválido: {}",
                        entry.getKey(), config.getFailureRateThreshold());
                valid = false;
            }
            
            if (config.getSlidingWindowSize() < config.getMinimumNumberOfCalls()) {
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java ===
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

/**
 * Política de Kill Switch para algoritmos de trading anómalos.
 * Detecta comportamiento anómalo y ejecuta acciones de mitigación.
 */
public class KillSwitchPolicy {
    private static final Logger log = LoggerFactory.getLogger(KillSwitchPolicy.class);
    
    // Thresholds de detección de anomalías
    private static final double DEFAULT_ANOMALY_THRESHOLD = 3.0; // desviaciones estándar
    private static final int DEFAULT_CONSECUTIVE_ANOMALIES = 5;
    private static final Duration DEFAULT_ANOMALY_WINDOW = Duration.ofMinutes(5);
    private static final Duration DEFAULT_COOLDOWN_PERIOD = Duration.ofMinutes(15);
    private static final int DEFAULT_MAX_ORDERS_PER_SECOND = 100;
    private static final double DEFAULT_MAX_POSITION_CHANGE_RATE = 0.5; // 50% por segundo
    
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
    
    /**
     * Evalúa si una orden debe ser bloqueada por el kill switch.
     * Retorna true si la orden puede proceder, false si debe ser bloqueada.
     */
    public boolean evaluateOrder(String traderId, String strategyId, String instrumentId,
                                  double orderValue, double currentPosition, int ordersInLastSecond) {
        // Verificar kill switch global
        if (globalKillSwitchActive.get()) {
            triggerKillSwitch(traderId, strategyId, KillSwitchReason.GLOBAL_KILL_SWITCH_ACTIVE,
                    "Orden bloqueada por kill switch global activo");
            return false;
        }
        
        // Verificar cooldown del trader
        TraderKillSwitchState traderState = traderStates.get(traderId);
        if (traderState != null && traderState.isInCooldown()) {
            log.warn("Trader '{}' en cooldown hasta {}", traderId, traderState.getCooldownEnd());
            return false;
        }
        
        // Verificar rate limit de órdenes por segundo
        if (ordersInLastSecond > maxOrdersPerSecond) {
            triggerKillSwitch(traderId, strategyId, KillSwitchReason.RATE_LIMIT_EXCEEDED,
                    String.format("Rate limit excedido: %d órdenes/segundo", ordersInLastSecond));
            return false;
        }
        
        // Verificar rate de cambio de posición
        if (traderState != null) {
            double positionChangeRate = calculatePositionChangeRate(traderState, currentPosition);
            if (positionChangeRate > maxPositionChangeRate) {
                triggerKillSwitch(traderId, strategyId, KillSwitchReason.POSITION_CHANGE_TOO_FAST,
                        String.format("Cambio de posición muy rápido: %.2f%%/segundo", positionChangeRate * 100));
                return false;
            }
        }
        
        // Verificar estado de la estrategia
        StrategyKillSwitchState strategyState = strategyStates.get(strategyId);
        if (strategyState != null && strategyState.isKilled()) {
            log.warn("Estrategia '{}' ha sido desactivada por KillSwitch", strategyId);
            return false;
        }
        
        return true;
    }
    
    /**
     * Registra una anomalía detectada en el comportamiento del algoritmo.
     */
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
        
        // Verificar si se alcanzó el límite de anomalías consecutivas
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
    
    /**
     * Verifica si el comportamiento del trader está dentro de los parámetros normales.
     * Retorna el factor de riesgo (0.0 = normal, 1.0 = crítico).
     */
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
    
    /**
     * Activa el kill switch global (emergencia).
     */
    public void activateGlobalKillSwitch(String reason) {
        globalKillSwitchActive.set(true);
        log.error("KILL SWITCH GLOBAL ACTIVADO: {}", reason);
    }
    
    /**
     * Desactiva el kill switch global.
     */
    public void deactivateGlobalKillSwitch() {
        globalKillSwitchActive.set(false);
        log.info("KILL SWITCH GLOBAL DESACTIVADO");
    }
    
    /**
     * Restablece el estado de un trader específico.
     */
    public void resetTrader(String traderId) {
        traderStates.remove(traderId);
        log.info("Estado de KillSwitch reseteado para trader '{}'", traderId);
    }
    
    /**
     * Restablece el estado de una estrategia específica.
     */
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
        
        StrategyKillSwitchState(String strategyId) {
            this.strategyId = strategyId;
            this.consecutiveAnomalyCount = new AtomicInteger(0);
            this.killed = new AtomicBoolean(false);
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
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/replay/DeterministicReplay.java ===
package com.pragma.riskengine.replay;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import com.pragma.riskengine.disruptor.MarketDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class DeterministicReplay {
    private static final Logger log = LoggerFactory.getLogger(DeterministicReplay.class);
    private static final int RING_BUFFER_SIZE = 1024;
    private static final int MAX_REPLAY_EVENTS = 100_000;
    private static final String EVENT_LOG_MAGIC = "RPE1";
    private static final int MAGIC_LENGTH = 4;
    private static final int VERSION_LENGTH = 4;
    private static final int HEADER_SIZE = MAGIC_LENGTH + VERSION_LENGTH + 8 + 8;
    
    private final Path replayLogPath;
    private final Disruptor<OrderEvent> replayDisruptor;
    private final RingBuffer<OrderEvent> replayBuffer;
    private final AtomicBoolean replaying;
    private final AtomicLong eventsReplayed;
    private final AtomicLong replayStartTimeNanos;
    private final EventHandler<OrderEvent> replayHandler;
    private final ConcurrentSkipListMap<Instant, ReplayableEvent> eventIndex;
    private final ExecutorService replayExecutor;
    private volatile boolean running;
    
    public DeterministicReplay(Path replayLogPath, EventHandler<OrderEvent> handler) {
        this.replayLogPath = replayLogPath;
        this.replayHandler = handler;
        this.replaying = new AtomicBoolean(false);
        this.eventsReplayed = new AtomicLong(0);
        this.replayStartTimeNanos = new AtomicLong(0);
        this.eventIndex = new ConcurrentSkipListMap<>();
        this.replayExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "deterministic-replay-executor");
            t.setDaemon(true);
            return t;
        });
        
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "replay-disruptor-" + counter.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };
        
        this.replayDisruptor = new Disruptor<>(
            new OrderEventFactory(),
            RING_BUFFER_SIZE,
            threadFactory,
            ProducerType.SINGLE,
            new BlockingWaitStrategy()
        );
        this.replayBuffer = replayDisruptor.getRingBuffer();
    }
    
    public void startReplay(Instant from, Instant to) {
        if (!replaying.compareAndSet(false, true)) {
            throw new IllegalStateException("Replay ya está en ejecución");
        }
        log.info("Iniciando replay determinístico desde {} hasta {}", from, to);
        running = true;
        
        replayDisruptor.handleEventsWith(replayHandler);
        replayDisruptor.start();
        
        replayExecutor.submit(() -> executeReplay(from, to));
    }
    
    private void executeReplay(Instant from, Instant to) {
        try {
            List<ReplayableEvent> events = loadEventsFromLog(from, to);
            if (events.isEmpty()) {
                log.warn("No se encontraron eventos para el rango especificado");
                return;
            }
            
            log.info("Cargados {} eventos para replay", events.size());
            replayStartTimeNanos.set(System.nanoTime());
            
            long baseTimestamp = events.get(0).timestampNanos;
            
            for (ReplayableEvent event : events) {
                if (!running) {
                    log.info("Replay detenido manualmente");
                    break;
                }
                
                long relativeTimeNanos = event.timestampNanos - baseTimestamp;
                long targetElapsed = relativeTimeNanos;
                long actualElapsed = System.nanoTime() - replayStartTimeNanos.get();
                
                if (targetElapsed > actualElapsed) {
                    long sleepNanos = targetElapsed - actualElapsed;
                    if (sleepNanos > 1_000_000) {
                        Thread.sleep(sleepNanos / 1_000_000, (int) (sleepNanos % 1_000_000));
                    } else {
                        Thread.yield();
                    }
                }
                
                publishEventToBuffer(event);
                eventsReplayed.incrementAndGet();
                
                if (eventsReplayed.get() % 1000 == 0) {
                    log.debug("Replayed {} eventos", eventsReplayed.get());
                }
            }
            
            log.info("Replay completado. Total eventos: {}", eventsReplayed.get());
        } catch (Exception e) {
            log.error("Error durante replay determinístico", e);
        } finally {
            completeReplay();
        }
    }
    
    private List<ReplayableEvent> loadEventsFromLog(Instant from, Instant to) throws IOException {
        List<ReplayableEvent> events = new ArrayList<>();
        
        if (!Files.exists(replayLogPath)) {
            log.warn("Archivo de log de replay no encontrado: {}", replayLogPath);
            return events;
        }
        
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(replayLogPath))) {
            byte[] magic = new byte[MAGIC_LENGTH];
            dis.readFully(magic);
            String magicStr = new String(magic);
            if (!EVENT_LOG_MAGIC.equals(magicStr)) {
                throw new IOException("Formato de log inválido: magic incorrecto");
            }
            
            dis.readFully(new byte[VERSION_LENGTH]);
            long startTimeNanos = dis.readLong();
            long endTimeNanos = dis.readLong();
            
            log.debug("Log couvre desde {} hasta {}", 
                Instant.ofEpochSecond(0, startTimeNanos), 
                Instant.ofEpochSecond(0, endTimeNanos));
            
            while (dis.available() > 0) {
                ReplayableEvent event = readEvent(dis);
                if (event != null) {
                    Instant eventInstant = Instant.ofEpochSecond(0, event.timestampNanos);
                    if (!eventInstant.isBefore(from) && !eventInstant.isAfter(to)) {
                        events.add(event);
                        eventIndex.put(eventInstant, event);
                    }
                }
            }
        }
        
        events.sort(Comparator.comparingLong(e -> e.timestampNanos));
        return events;
    }
    
    private ReplayableEvent readEvent(DataInputStream dis) throws IOException {
        try {
            long timestampNanos = dis.readLong();
            byte eventType = dis.readByte();
            int payloadLength = dis.readInt();
            byte[] payload = new byte[payloadLength];
            dis.readFully(payload);
            
            return new ReplayableEvent(timestampNanos, eventType, payload);
        } catch (EOFException e) {
            return null;
        }
    }
    
    private void publishEventToBuffer(ReplayableEvent event) {
        long sequence = replayBuffer.next();
        try {
            OrderEvent orderEvent = replayBuffer.get(sequence);
            orderEvent.setOrderId(new String(event.payload, 0, Math.min(36, event.payload.length)));
            orderEvent.setTimestamp(Instant.ofEpochSecond(0, event.timestampNanos));
            orderEvent.setEventType(event.eventType);
        } finally {
            replayBuffer.publish(sequence);
        }
    }
    
    public void recordEvent(long timestampNanos, byte eventType, byte[] payload) {
        if (!running) {
            throw new IllegalStateException("Recorder no está inicializado");
        }
        ReplayableEvent event = new ReplayableEvent(timestampNanos, eventType, payload);
        eventIndex.put(Instant.ofEpochSecond(0, timestampNanos), event);
    }
    
    public void writeEventLog(Path outputPath) throws IOException {
        log.info("Escribiendo log de eventos a {}", outputPath);
        
        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(outputPath))) {
            dos.writeBytes(EVENT_LOG_MAGIC);
            dos.writeBytes("0001");
            
            long firstTimestamp = eventIndex.isEmpty() ? System.nanoTime() : 
                eventIndex.firstKey().toEpochMilli() * 1_000_000;
            long lastTimestamp = eventIndex.isEmpty() ? firstTimestamp : 
                eventIndex.lastKey().toEpochMilli() * 1_000_000;
            
            dos.writeLong(firstTimestamp);
            dos.writeLong(lastTimestamp);
            
            for (var entry : eventIndex.entrySet()) {
                ReplayableEvent event = entry.getValue();
                dos.writeLong(event.timestampNanos);
                dos.writeByte(event.eventType);
                dos.writeInt(event.payload.length);
                dos.write(event.payload);
            }
        }
        
        log.info("Log escrito con {} eventos", eventIndex.size());
    }
    
    public void stopReplay() {
        running = false;
        log.info("Solicitando parada de replay");
    }
    
    private void completeReplay() {
        if (replaying.compareAndSet(true, false)) {
            replayDisruptor.shutdown(5, TimeUnit.SECONDS);
            log.info("Replay detenido. Eventos procesados: {}", eventsReplayed.get());
        }
    }
    
    public long getEventsReplayedCount() {
        return eventsReplayed.get();
    }
    
    public boolean isReplaying() {
        return replaying.get();
    }
    
    public Map<Instant, ReplayableEvent> getEventIndex() {
        return Collections.unmodifiableMap(eventIndex);
    }
    
    public void shutdown() {
        stopReplay();
        replayExecutor.shutdown();
        try {
            if (!replayExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                replayExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            replayExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public static class ReplayableEvent {
        public final long timestampNanos;
        public final byte eventType;
        public final byte[] payload;
        
        public ReplayableEvent(long timestampNanos, byte eventType, byte[] payload) {
            this.timestampNanos = timestampNanos;
            this.eventType = eventType;
            this.payload = payload;
        }
    }
    
    private static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/compliance/MiFIDIITracer.java ===
package com.pragma.riskengine.compliance;

import com.pragma.riskengine.disruptor.OrderEvent;
import com.pragma.riskengine.disruptor.MarketDataEvent;
import com.pragma.riskengine.model.VaRModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class MiFIDIITracer {
    private static final Logger log = LoggerFactory.getLogger(MiFIDIITracer.class);
    private static final String COMPLIANCE_LOG_PREFIX = "mifidi_trace_";
    private static final String COMPLIANCE_LOG_EXT = ".log";
    private static final int MAX_TRACE_ENTRIES = 1_000_000;
    private static final int FLUSH_INTERVAL_MS = 5000;
    private static final int MAX_PAYLOAD_SIZE = 4096;
    
    private final Path traceLogPath;
    private final ConcurrentLinkedQueue<TraceEntry> traceBuffer;
    private final ConcurrentHashMap<String, DecisionContext> activeContexts;
    private final ScheduledExecutorService flushExecutor;
    private final AtomicLong traceSequence;
    private final AtomicBoolean running;
    private final VaRModel varModel;
    private final MarketDataSnapshot lastMarketSnapshot;
    private PrintWriter traceWriter;
    private volatile long lastFlushTime;
    
    public MiFIDIITracer(Path traceLogDir, VaRModel varModel) {
        this.traceLogDir = traceLogDir;
        this.varModel = varModel;
        this.traceBuffer = new ConcurrentLinkedQueue<>();
        this.activeContexts = new ConcurrentHashMap<>();
        this.traceSequence = new AtomicLong(0);
        this.running = new AtomicBoolean(false);
        this.lastMarketSnapshot = new MarketDataSnapshot();
        
        this.traceLogPath = traceLogDir.resolve(
            COMPLIANCE_LOG_PREFIX + 
            LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
            COMPLIANCE_LOG_EXT
        );
        
        this.flushExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "mifidi-flush-executor");
            t.setDaemon(true);
            return t;
        });
    }
    
    private final Path traceLogDir;
    
    public void initialize() throws IOException {
        Files.createDirectories(traceLogDir);
        traceWriter = new PrintWriter(new BufferedWriter(
            new FileWriter(traceLogPath.toFile()), 8192
        ));
        running.set(true);
        
        flushExecutor.scheduleAtFixedRate(
            this::flushBuffer,
            FLUSH_INTERVAL_MS,
            FLUSH_INTERVAL_MS,
            TimeUnit.MILLISECONDS
        );
        
        log.info("MiFID II Tracer inicializado. Log: {}", traceLogPath);
    }
    
    public String beginRiskDecision(String orderId, String traderId, String instrumentId, 
                                    double notionalValue, String decisionType) {
        String contextId = UUID.randomUUID().toString();
        long timestampNanos = System.nanoTime();
        
        DecisionContext context = new DecisionContext(
            contextId,
            orderId,
            traderId,
            instrumentId,
            notionalValue,
            decisionType,
            timestampNanos,
            captureMarketContext(instrumentId)
        );
        
        activeContexts.put(contextId, context);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "DECISION_START",
            context.toMap()
        );
        traceBuffer.offer(entry);
        
        log.debug("Inicio de decisión de riesgo: contextId={}, orderId={}", contextId, orderId);
        return contextId;
    }
    
    public void recordRiskCheck(String contextId, String checkType, boolean passed, 
                                double measuredValue, double threshold, String details) {
        DecisionContext ctx = activeContexts.get(contextId);
        if (ctx == null) {
            log.warn("Contexto no encontrado: {}", contextId);
            return;
        }
        
        long timestampNanos = System.nanoTime();
        Map<String, Object> data = new HashMap<>();
        data.put("contextId", contextId);
        data.put("checkType", checkType);
        data.put("passed", passed);
        data.put("measuredValue", measuredValue);
        data.put("threshold", threshold);
        data.put("details", details);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "RISK_CHECK",
            data
        );
        traceBuffer.offer(entry);
        
        ctx.addRiskCheck(checkType, passed, measuredValue);
    }
    
    public void completeRiskDecision(String contextId, boolean approved, String reason) {
        DecisionContext ctx = activeContexts.remove(contextId);
        if (ctx == null) {
            log.warn("Contexto no encontrado al completar: {}", contextId);
            return;
        }
        
        long timestampNanos = System.nanoTime();
        ctx.complete(approved, reason, timestampNanos);
        
        Map<String, Object> data = new HashMap<>(ctx.toMap());
        data.put("reason", reason);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            approved ? "DECISION_APPROVED" : "DECISION_REJECTED",
            data
        );
        traceBuffer.offer(entry);
        
        log.info("Decisión de riesgo completada: contextId={}, approved={}, reason={}", 
            contextId, approved, reason);
    }
    
    public void recordCircuitBreakerEvent(String instrumentId, String state, 
                                          String reason, double currentExposure, double threshold) {
        long timestampNanos = System.nanoTime();
        
        Map<String, Object> data = new HashMap<>();
        data.put("instrumentId", instrumentId);
        data.put("state", state);
        data.put("reason", reason);
        data.put("currentExposure", currentExposure);
        data.put("threshold", threshold);
        data.put("timestampNanos", timestampNanos);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "CIRCUIT_BREAKER_EVENT",
            data
        );
        traceBuffer.offer(entry);
    }
    
    public void recordKillSwitchEvent(String traderId, String strategyId, 
                                      String triggerReason, Map<String, Object> metadata) {
        long timestampNanos = System.nanoTime();
        
        Map<String, Object> data = new HashMap<>();
        data.put("traderId", traderId);
        data.put("strategyId", strategyId);
        data.put("triggerReason", triggerReason);
        data.put("timestampNanos", timestampNanos);
        if (metadata != null) {
            data.putAll(metadata);
        }
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "KILL_SWITCH_TRIGGERED",
            data
        );
        traceBuffer.offer(entry);
        
        log.warn("Kill switch activado: traderId={}, reason={}", traderId, triggerReason);
    }
    
    public void recordMarketDataContext(String instrumentId, double bid, double ask, 
                                        long bidSize, long askSize, double volatility) {
        lastMarketSnapshot.update(instrumentId, bid, ask, bidSize, askSize, volatility);
        
        long timestampNanos = System.nanoTime();
        
        Map<String, Object> data = new HashMap<>();
        data.put("instrumentId", instrumentId);
        data.put("bid", bid);
        data.put("ask", ask);
        data.put("bidSize", bidSize);
        data.put("askSize", askSize);
        data.put("volatility", volatility);
        data.put("timestampNanos", timestampNanos);
        
        TraceEntry entry = new TraceEntry(
            traceSequence.incrementAndGet(),
            timestampNanos,
            "MARKET_CONTEXT",
            data
        );
        traceBuffer.offer(entry);
    }
    
    private MarketContext captureMarketContext(String instrumentId) {
        double volatility = 0.0;
        try {
            volatility = varModel.getCurrentVolatility(instrumentId);
        } catch (Exception e) {
            log.debug("No se pudo obtener volatilidad para {}", instrumentId);
        }
        
        MarketDataSnapshot snapshot = lastMarketSnapshot.getSnapshot(instrumentId);
        return new MarketContext(
            snapshot.bid,
            snapshot.ask,
            snapshot.bidSize,
            snapshot.askSize,
            volatility,
            System.nanoTime()
        );
    }
    
    private void flushBuffer() {
        if (traceBuffer.isEmpty()) {
            return;
        }
        
        int flushed = 0;
        StringBuilder batch = new StringBuilder();
        
        TraceEntry entry;
        while ((entry = traceBuffer.poll()) != null && flushed < 10000) {
            batch.append(formatTraceEntry(entry)).append("\n");
            flushed++;
        }
        
        if (batch.length() > 0 && traceWriter != null) {
            traceWriter.print(batch);
            traceWriter.flush();
            lastFlushTime = System.currentTimeMillis();
        }
        
        log.debug("Flushed {} entradas de trace", flushed);
    }
    
    private String formatTraceEntry(TraceEntry entry) {
        return String.format(
            "{\"seq\":%d,\"tsNanos\":%d,\"type\":\"%s\",\"data\":%s}",
            entry.sequence,
            entry.timestampNanos,
            entry.eventType,
            serializeData(entry.data)
        );
    }
    
    private String serializeData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> e : data.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(e.getKey()).append("\":");
            Object v = e.getValue();
            if (v instanceof Number) {
                sb.append(v);
            } else if (v instanceof String) {
                sb.append("\"").append(escapeJson((String)v)).append("\"");
            } else if (v instanceof Boolean) {
                sb.append(v);
            } else if (v == null) {
                sb.append("null");
            } else {
                sb.append("\"").append(escapeJson(v.toString())).append("\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    public void shutdown() {
        running.set(false);
        flushBuffer();
        
        flushExecutor.shutdown();
        try {
            if (!flushExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                flushExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            flushExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        if (traceWriter != null) {
            traceWriter.flush();
            traceWriter.close();
        }
        
        log.info("MiFID II Tracer cerrado. Trace entries: {}", traceSequence.get());
    }
    
    public long getTraceEntryCount() {
        return traceSequence.get();
    }
    
    public Path getTraceLogPath() {
        return traceLogPath;
    }
    
    public static class TraceEntry {
        public final long sequence;
        public final long timestampNanos;
        public final String eventType;
        public final Map<String, Object> data;
        
        public TraceEntry(long sequence, long timestampNanos, String eventType, 
                         Map<String, Object> data) {
            this.sequence = sequence;
            this.timestampNanos = timestampNanos;
            this.eventType = eventType;
            this.data = data;
        }
    }
    
    public static class DecisionContext {
        public final String contextId;
        public final String orderId;
        public final String traderId;
        public final String instrumentId;
        public final double notionalValue;
        public final String decisionType;
        public final long startTimestampNanos;
        public final MarketContext marketContext;
        public final List<RiskCheckResult> riskChecks;
        
        private volatile boolean approved;
        private volatile String reason;
        private volatile long completionTimestampNanos;
        
        public DecisionContext(String contextId, String orderId, String traderId, 
                              String instrumentId, double notionalValue, String decisionType,
                              long startTimestampNanos, MarketContext marketContext) {
            this.contextId = contextId;
            this.orderId = orderId;
            this.traderId = traderId;
            this.instrumentId = instrumentId;
            this.notionalValue = notionalValue;
            this.decisionType = decisionType;
            this.startTimestampNanos = startTimestampNanos;
            this.marketContext = marketContext;
            this.riskChecks = new CopyOnWriteArrayList<>();
        }
        
        public void addRiskCheck(String checkType, boolean passed, double value) {
            riskChecks.add(new RiskCheckResult(checkType, passed, value));
        }
        
        public void complete(boolean approved, String reason, long completionTimestampNanos) {
            this.approved = approved;
            this.reason = reason;
            this.completionTimestampNanos = completionTimestampNanos;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("contextId", contextId);
            map.put("orderId", orderId);
            map.put("traderId", traderId);
            map.put("instrumentId", instrumentId);
            map.put("notionalValue", notionalValue);
            map.put("decisionType", decisionType);
            map.put("startTimestampNanos", startTimestampNanos);
            map.put("approved", approved);
            map.put("reason", reason);
            map.put("completionTimestampNanos", completionTimestampNanos);
            map.put("durationNanos", completionTimestampNanos - startTimestampNanos);
            map.put("marketContext", marketContext.toMap());
            map.put("riskChecks", riskChecks.stream().map(RiskCheckResult::toMap).toList());
            return map;
        }
    }
    
    public static class RiskCheckResult {
        public final String checkType;
        public final boolean passed;
        public final double value;
        
        public RiskCheckResult(String checkType, boolean passed, double value) {
            this.checkType = checkType;
            this.passed = passed;
            this.value = value;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("checkType", checkType);
            map.put("passed", passed);
            map.put("value", value);
            return map;
        }
    }
    
    public static class MarketContext {
        public final double bid;
        public final double ask;
        public final long bidSize;
        public final long askSize;
        public final double volatility;
        public final long timestampNanos;
        
        public MarketContext(double bid, double ask, long bidSize, long askSize, 
                           double volatility, long timestampNanos) {
            this.bid = bid;
            this.ask = ask;
            this.bidSize = bidSize;
            this.askSize = askSize;
            this.volatility = volatility;
            this.timestampNanos = timestampNanos;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("bid", bid);
            map.put("ask", ask);
            map.put("bidSize", bidSize);
            map.put("askSize", askSize);
            map.put("volatility", volatility);
            map.put("timestampNanos", timestampNanos);
            return map;
        }
    }
    
    private static class MarketDataSnapshot {
        private final ConcurrentHashMap<String, SnapshotEntry> snapshots = new ConcurrentHashMap<>();
        
        public void update(String instrumentId, double bid, double ask, 
                         long bidSize, long askSize, double volatility) {
            snapshots.put(instrumentId, new SnapshotEntry(bid, ask, bidSize, askSize, volatility));
        }
        
        public SnapshotEntry getSnapshot(String instrumentId) {
            return snapshots.getOrDefault(instrumentId, 
                new SnapshotEntry(0.0, 0.0, 0, 0, 0.0));
        }
        
        public record SnapshotEntry(double bid, double ask, long bidSize, long askSize, double volatility) {}
    }
}

// === ARCHIVO: src/test/java/com/pragma/riskengine/disruptor/OrderEventHandlerTest.java ===
package com.pragma.riskengine.disruptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("OrderEventHandler - Tests de latencia y throughput")
class OrderEventHandlerTest {

    private OrderEventHandler handler;
    private OrderEvent event;
    private static final String TEST_INSTRUMENT = "AAPL";
    private static final String TEST_TRADER = "TRADER_001";
    private static final String TEST_STRATEGY = "MOMENTUM";

    @BeforeEach
    void setUp() {
        handler = new OrderEventHandler();
        event = new OrderEvent(
            "order_001",
            TEST_INSTRUMENT,
            TEST_TRADER,
            TEST_STRATEGY,
            "BUY",
            1000,
            150.25,
            Instant.now()
        );
    }

    @Test
    @DisplayName("Debe procesar evento de orden correctamente")
    void onEvent_shouldProcessOrderEventSuccessfully() {
        long initialSequence = 0L;
        boolean endOfBatch = true;

        handler.onEvent(event, initialSequence, endOfBatch);

        assertNotNull(event);
        assertEquals("order_001", event.orderId());
        assertEquals(TEST_INSTRUMENT, event.instrumentId());
    }

    @Test
    @DisplayName("Debe manejar eventos de compra y venta")
    void onEvent_shouldHandleBothBuyAndSellOrders() {
        OrderEvent buyEvent = new OrderEvent(
            "order_buy", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "BUY", 500, 150.00, Instant.now()
        );
        OrderEvent sellEvent = new OrderEvent(
            "order_sell", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "SELL", 500, 151.00, Instant.now()
        );

        handler.onEvent(buyEvent, 0L, true);
        handler.onEvent(sellEvent, 1L, true);

        assertEquals("BUY", buyEvent.side());
        assertEquals("SELL", sellEvent.side());
    }

    @Test
    @DisplayName("Debe validar quantities positivos")
    void onEvent_shouldRejectZeroOrNegativeQuantities() {
        OrderEvent zeroQtyEvent = new OrderEvent(
            "order_zero", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "BUY", 0, 150.00, Instant.now()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            handler.onEvent(zeroQtyEvent, 0L, true);
        });
    }

    @RepeatedTest(100)
    @DisplayName("Debe mantener latencia consistente bajo carga repetida")
    void onEvent_shouldMaintainConsistentLatencyUnderRepeatedLoad() {
        long startTime = System.nanoTime();
        handler.onEvent(event, 0L, true);
        long endTime = System.nanoTime();
        long latencyNanos = endTime - startTime;

        assertTrue(latencyNanos < 1_000_000, 
            "Latencia debe ser menor a 1ms, pero fue: " + latencyNanos + " ns");
    }

    @Test
    @DisplayName("Debe medir throughput con múltiples eventos Concurrentes")
    void shouldMeasureThroughputWithMultipleConcurrentEvents() throws InterruptedException {
        int eventCount = 10_000;
        CountDownLatch latch = new CountDownLatch(1);
        AtomicLong totalTime = new AtomicLong(0);
        AtomicReference<Exception> exceptionRef = new AtomicReference<>();

        Thread producerThread = new Thread(() -> {
            try {
                long start = System.nanoTime();
                for (int i = 0; i < eventCount; i++) {
                    OrderEvent ev = new OrderEvent(
                        "order_" + i,
                        TEST_INSTRUMENT,
                        TEST_TRADER,
                        TEST_STRATEGY,
                        i % 2 == 0 ? "BUY" : "SELL",
                        100 + (i % 500),
                        150.0 + (i % 100) * 0.01,
                        Instant.now()
                    );
                    handler.onEvent(ev, i, i == eventCount - 1);
                }
                totalTime.set(System.nanoTime() - start);
                latch.countDown();
            } catch (Exception e) {
                exceptionRef.set(e);
                latch.countDown();
            }
        });

        producerThread.start();
        boolean completed = latch.await(30, TimeUnit.SECONDS);

        assertTrue(completed, "El procesamiento no completó en 30 segundos");
        assertNull(exceptionRef.get(), "Excepción durante el procesamiento: " + exceptionRef.get());

        long totalTimeMs = totalTime.get() / 1_000_000;
        double throughput = (double) eventCount / totalTimeMs * 1000;

        assertTrue(throughput > 100_000, 
            "Throughput debe superar 100K eventos/segundo, pero fue: " + 
            String.format("%.2f", throughput));
    }

    @Test
    @DisplayName("Debe manejar eventos con timestamps en nanosegundos")
    void onEvent_shouldHandleNanosTimestampPrecision() {
        Instant preciseTimestamp = Instant.now();
        OrderEvent preciseEvent = new OrderEvent(
            "order_precise", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "BUY", 100, 150.50, preciseTimestamp
        );

        handler.onEvent(preciseEvent, 0L, true);

        assertNotNull(preciseEvent.timestamp());
        assertTrue(preciseEvent.timestamp().getEpochSecond() > 0);
    }

    @Test
    @DisplayName("Debe validar side de orden válido")
    void onEvent_shouldRejectInvalidSide() {
        OrderEvent invalidSideEvent = new OrderEvent(
            "order_invalid", TEST_INSTRUMENT, TEST_TRADER, TEST_STRATEGY,
            "INVALID_SIDE", 100, 150.00, Instant.now()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            handler.onEvent(invalidSideEvent, 0L, true);
        });
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    @DisplayName("Debe procesar eventos concurrentemente sin race conditions")
    void onEvent_shouldHandleConcurrentEventsWithoutRaceConditions() throws InterruptedException {
        int threads = 4;
        int eventsPerThread = 2500;
        CountDownLatch barrier = new CountDownLatch(threads);
        AtomicReference<Exception> error = new AtomicReference<>();

        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            new Thread(() -> {
                try {
                    for (int i = 0; i < eventsPerThread; i++) {
                        OrderEvent ev = new OrderEvent(
                            "order_t" + threadId + "_i" + i,
                            TEST_INSTRUMENT + "_" + (i % 10),
                            TEST_TRADER + "_" + threadId,
                            TEST_STRATEGY,
                            i % 2 == 0 ? "BUY" : "SELL",
                            100 + i,
                            150.0 + i,
                            Instant.now()
                        );
                        handler.onEvent(ev, i, i == eventsPerThread - 1);
                    }
                } catch (Exception e) {
                    error.set(e);
                } finally {
                    barrier.countDown();
                }
            }).start();
        }

        assertTrue(barrier.await(60, TimeUnit.SECONDS), "Timeout en procesamiento concurrente");
        assertNull(error.get(), "Race condition detectada: " + error.get());
    }
}

// === ARCHIVO: src/test/java/com/pragma/riskengine/var/VaRCalculatorTest.java ===
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

// === ARCHIVO: src/test/java/com/pragma/riskengine/circuitbreaker/DynamicCircuitBreakerTest.java ===
package com.pragma.riskengine.circuitbreaker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DynamicCircuitBreaker - Tests de circuit breakers con fallos y recuperación")
class DynamicCircuitBreakerTest {

    private CircuitBreakerConfig config;
    private DynamicCircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        config = new CircuitBreakerConfig(
            5,
            0.5,
            10000L,
            30000L,
            0.75,
            2.0,
            0.5
        );
        circuitBreaker = new DynamicCircuitBreaker("TEST_CB", config);
    }

    @Nested
    @DisplayName("Tests de Estados del Circuit Breaker")
    class StateTransitionTests {

        @Test
        @DisplayName("Debe iniciar en estado CLOSED")
        void shouldStartInClosedState() {
            assertEquals(DynamicCircuitBreaker.State.CLOSED, circuitBreaker.getState());
            assertTrue(circuitBreaker.isAvailable(), 
                "Circuit breaker debe estar disponible en estado CLOSED");
        }

        @Test
        @DisplayName("Debe transiciónar a OPEN tras superar threshold de fallos")
        void shouldTransitionToOpenAfterFailureThreshold() {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Simulated failure " + i);
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());
            assertFalse(circuitBreaker.isAvailable(), 
                "Circuit breaker no debe estar disponible en estado OPEN");
        }

        @Test
        @DisplayName("Debe transiciónar a HALF_OPEN tras timeout de recuperación")
        void shouldTransitionToHalfOpenAfterRecoveryTimeout() throws InterruptedException {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());

            Thread.sleep(config.recoveryTimeoutMs() + 100);

            assertEquals(DynamicCircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());
        }

        @Test
        @DisplayName("Debe retornar a CLOSED tras éxito en HALF_OPEN")
        void shouldReturnToClosedAfterSuccessInHalfOpen() throws InterruptedException {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            Thread.sleep(config.recoveryTimeoutMs() + 100);
            assertEquals(DynamicCircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

            for (int i = 0; i < 3; i++) {
                String result = circuitBreaker.execute(() -> "success");
                assertEquals("success", result);
            }

            assertEquals(DynamicCircuitBreaker.State.CLOSED, circuitBreaker.getState());
        }
    }

    @Nested
    @DisplayName("Tests de Fallos Simulados")
    class FailureSimulationTests {

        @Test
        @DisplayName("Debe manejar excepciones de negocio correctamente")
        void shouldHandleBusinessExceptions() {
            String result = circuitBreaker.execute(() -> {
                throw new IllegalArgumentException("Business rule violation");
            });

            assertNull(result, "Resultado debe ser null cuando falla la ejecución");
            assertEquals(1, circuitBreaker.getFailureCount(), 
                "Contador de fallos debe incrementarse");
        }

        @Test
        @DisplayName("Debe registrar fallos exitosamente")
        void shouldRecordFailuresCorrectly() {
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Test failure");
                });
            } catch (Exception expected) {
            }

            assertTrue(circuitBreaker.getFailureCount() > 0, 
                "Los fallos deben ser registrados");
        }

        @Test
        @DisplayName("Debe rechazar llamadas cuando está OPEN")
        void shouldRejectCallsWhenOpen() {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            assertThrows(CircuitBreakerOpenException.class, () -> {
                circuitBreaker.execute(() -> "This should not execute");
            });
        }

        @Test
        @DisplayName("Debe manejar fallos consecutivos correctamente")
        void shouldHandleConsecutiveFailures() {
            for (int i = 0; i < 10; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Consecutive failure " + i);
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());
            assertTrue(circuitBreaker.getFailureCount() >= 5, 
                "Debe registrar múltiples fallos consecutivos");
        }
    }

    @Nested
    @DisplayName("Tests de Recuperación de Thresholds")
    class ThresholdRecoveryTests {

        @Test
        @DisplayName("Debe ajustar threshold dinámicamente basado en tasa de fallos")
        void shouldAdjustThresholdBasedOnFailureRate() {
            for (int i = 0; i < 3; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            for (int i = 0; i < 5; i++) {
                try {
                    circuitBreaker.execute(() -> "success");
                } catch (Exception ignored) {
                }
            }

            double currentThreshold = circuitBreaker.getCurrentThreshold();
            assertTrue(currentThreshold > 0, 
                "Threshold debe ser positivo después de adaptaciones");
        }

        @Test
        @DisplayName("Debe recalcular threshold tras cambio de volatilidad")
        void shouldRecalculateThresholdAfterVolatilityChange() {
            circuitBreaker.updateVolatility(0.15);
            double thresholdWithNormalVol = circuitBreaker.getCurrentThreshold();

            circuitBreaker.updateVolatility(0.45);
            double thresholdWithHighVol = circuitBreaker.getCurrentThreshold();

            assertTrue(thresholdWithHighVol > thresholdWithNormalVol, 
                "Threshold debe aumentar con mayor volatilidad");
        }

        @Test
        @DisplayName("Debe mantener stability window entre ajustes de threshold")
        void shouldMaintainStabilityWindowBetweenThresholdAdjustments() {
            long initialThreshold = circuitBreaker.getCurrentThreshold();

            circuitBreaker.updateVolatility(0.20);
            long thresholdAfterFirstUpdate = circuitBreaker.getCurrentThreshold();

            circuitBreaker.updateVolatility(0.21);
            long thresholdAfterSecondUpdate = circuitBreaker.getCurrentThreshold();

            assertEquals(initialThreshold, thresholdAfterFirstUpdate, 
                "Primer ajuste de volatilidad debe cambiar el threshold");
        }
    }

    @Nested
    @DisplayName("Tests de Concurrencia")
    class ConcurrencyTests {

        @Test
        @DisplayName("Debe manejar llamadas concurrentes sin race conditions")
        void shouldHandleConcurrentCallsWithoutRaceConditions() throws InterruptedException {
            int threadCount = 10;
            int callsPerThread = 50;
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int t = 0; t < threadCount; t++) {
                new Thread(() -> {
                    try {
                        startLatch.await();
                        for (int i = 0; i < callsPerThread; i++) {
                            try {
                                if (i % 3 == 0) {
                                    throw new RuntimeException("Simulated failure");
                                }
                                successCount.incrementAndGet();
                            } catch (Exception e) {
                                failureCount.incrementAndGet();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                }).start();
            }

            startLatch.countDown();
            assertTrue(endLatch.await(30, TimeUnit.SECONDS), 
                "Las llamadas concurrentes deben completar");

            assertTrue(successCount.get() + failureCount.get() == threadCount * callsPerThread, 
                "Todas las llamadas deben ser contabilizadas");
        }

        @RepeatedTest(5)
        @DisplayName("Debe mantener consistencia bajo carga repetida")
        void shouldMaintainConsistencyUnderRepeatedLoad() {
            for (int i = 0; i < 100; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        if (i % 10 == 0) {
                            throw new RuntimeException("Random failure");
                        }
                        return "ok";
                    });
                } catch (Exception ignored) {
                }
            }

            assertNotNull(circuitBreaker.getState());
            assertTrue(circuitBreaker.getTotalCalls() > 0);
        }
    }

    @Nested
    @DisplayName("Tests de Métricas")
    class MetricsTests {

        @Test
        @DisplayName("Debe registrar métricas de llamadas exitosas")
        void shouldRecordSuccessfulCallMetrics() {
            circuitBreaker.execute(() -> "success");
            circuitBreaker.execute(() -> "success");
            circuitBreaker.execute(() -> "success");

            assertEquals(3, circuitBreaker.getTotalCalls(), 
                "Debe registrar el número total de llamadas");
            assertEquals(3, circuitBreaker.getSuccessCount(), 
                "Debe registrar el número de éxitos");
        }

        @Test
        @DisplayName("Debe calcular tasa de fallos correctamente")
        void shouldCalculateFailureRateCorrectly() {
            for (int i = 0; i < 4; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            for (int i = 0; i < 6; i++) {
                circuitBreaker.execute(() -> "success");
            }

            double failureRate = circuitBreaker.getFailureRate();
            assertTrue(failureRate > 0.3 && failureRate < 0.5, 
                "Tasa de fallos debe estar entre 30% y 50%: " + failureRate);
        }

        @Test
        @DisplayName("Debe proporcionar lastFailureTime válido")
        void shouldProvideValidLastFailureTime() {
            Instant beforeFailure = Instant.now();
            try {
                circuitBreaker.execute(() -> {
                    throw new RuntimeException("Test failure");
                });
            } catch (Exception expected) {
            }
            Instant afterFailure = Instant.now();

            Instant lastFailure = circuitBreaker.getLastFailureTime();
            assertNotNull(lastFailure, "Last failure time no debe ser null");
            assertTrue(!lastFailure.isBefore(beforeFailure) && !lastFailure.isAfter(afterFailure), 
                "Last failure time debe estar en el rango correcto");
        }
    }

    @Nested
    @DisplayName("Tests de Configuración Dinámica")
    class DynamicConfigurationTests {

        @Test
        @DisplayName("Debe actualizar sliding window size dinámicamente")
        void shouldUpdateSlidingWindowSizeDynamically() {
            int originalWindow = config.slidingWindowSize();

            CircuitBreakerConfig newConfig = new CircuitBreakerConfig(
                3,
                0.5,
                10000L,
                30000L,
                0.75,
                2.0,
                0.5
            );
            circuitBreaker.updateConfig(newConfig);

            assertNotEquals(originalWindow, circuitBreaker.getCurrentThreshold());
        }

        @Test
        @DisplayName("Debe manejar reset de estado correctamente")
        void shouldHandleStateResetCorrectly() {
            for (int i = 0; i < 6; i++) {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (Exception expected) {
                }
            }

            assertEquals(DynamicCircuitBreaker.State.OPEN, circuitBreaker.getState());

            circuitBreaker.reset();

            assertEquals(DynamicCircuitBreaker.State.CLOSED, circuitBreaker.getState());
            assertEquals(0, circuitBreaker.getFailureCount());
            assertEquals(0, circuitBreaker.getSuccessCount());
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/riskengine/disruptor/MarketDataEvent.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/limits/LimitService.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/replay/DeterministicReplay.java ===
package com.pragma.riskengine.replay;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.pragma.riskengine.disruptor.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class DeterministicReplay {
    private static final Logger log = LoggerFactory.getLogger(DeterministicReplay.class);
    private static final int RING_BUFFER_SIZE = 1024;
    private static final String EVENT_LOG_MAGIC = "RPE1";
    private static final int MAGIC_LENGTH = 4;
    private static final int VERSION_LENGTH = 4;
    private static final int HEADER_SIZE = MAGIC_LENGTH + VERSION_LENGTH + 8 + 8;
    
    private final Path replayLogPath;
    private final Disruptor<OrderEvent> replayDisruptor;
    private final RingBuffer<OrderEvent> replayBuffer;
    private final AtomicBoolean replaying;
    private final AtomicLong eventsReplayed;
    private final AtomicLong replayStartTimeNanos;
    private final EventHandler<OrderEvent> replayHandler;
    private final ConcurrentSkipListMap<Instant, ReplayableEvent> eventIndex;
    private final ExecutorService replayExecutor;
    private volatile boolean running;
    
    public DeterministicReplay(Path replayLogPath, EventHandler<OrderEvent> handler) {
        this.replayLogPath = replayLogPath;
        this.replayHandler = handler;
        this.replaying = new AtomicBoolean(false);
        this.eventsReplayed = new AtomicLong(0);
        this.replayStartTimeNanos = new AtomicLong(0);
        this.eventIndex = new ConcurrentSkipListMap<>();
        this.replayExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "deterministic-replay-executor");
            t.setDaemon(true);
            return t;
        });
        
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "replay-disruptor-" + counter.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };
        
        this.replayDisruptor = new Disruptor<>(
            new OrderEventFactory(),
            RING_BUFFER_SIZE,
            threadFactory,
            ProducerType.SINGLE,
            new BlockingWaitStrategy()
        );
        this.replayBuffer = replayDisruptor.getRingBuffer();
    }
    
    public void startReplay(Instant from, Instant to) {
        if (!replaying.compareAndSet(false, true)) {
            throw new IllegalStateException("Replay ya está en ejecución");
        }
        log.info("Iniciando replay determinístico desde {} hasta {}", from, to);
        running = true;
        
        replayDisruptor.handleEventsWith(replayHandler);
        replayDisruptor.start();
        
        replayExecutor.submit(() -> executeReplay(from, to));
    }
    
    private void executeReplay(Instant from, Instant to) {
        try {
            List<ReplayableEvent> events = loadEventsFromLog(from, to);
            if (events.isEmpty()) {
                log.warn("No se encontraron eventos para el rango especificado");
                return;
            }
            
            log.info("Cargados {} eventos para replay", events.size());
            replayStartTimeNanos.set(System.nanoTime());
            
            long baseTimestamp = events.get(0).timestampNanos;
            
            for (ReplayableEvent event : events) {
                if (!running) {
                    log.info("Replay detenido manualmente");
                    break;
                }
                
                long relativeTimeNanos = event.timestampNanos - baseTimestamp;
                long targetElapsed = relativeTimeNanos;
                long actualElapsed = System.nanoTime() - replayStartTimeNanos.get();
                
                if (targetElapsed > actualElapsed) {
                    long sleepNanos = targetElapsed - actualElapsed;
                    if (sleepNanos > 1_000_000) {
                        Thread.sleep(sleepNanos / 1_000_000, (int) (sleepNanos % 1_000_000));
                    } else {
                        Thread.yield();
                    }
                }
                
                publishEventToBuffer(event);
                eventsReplayed.incrementAndGet();
                
                if (eventsReplayed.get() % 1000 == 0) {
                    log.debug("Replayed {} eventos", eventsReplayed.get());
                }
            }
            
            log.info("Replay completado. Total eventos: {}", eventsReplayed.get());
        } catch (Exception e) {
            log.error("Error durante replay determinístico", e);
        } finally {
            completeReplay();
        }
    }
    
    private List<ReplayableEvent> loadEventsFromLog(Instant from, Instant to) throws IOException {
        List<ReplayableEvent> events = new ArrayList<>();
        
        if (!Files.exists(replayLogPath)) {
            log.warn("Archivo de log de replay no encontrado: {}", replayLogPath);
            return events;
        }
        
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(replayLogPath))) {
            byte[] magic = new byte[MAGIC_LENGTH];
            dis.readFully(magic);
            String magicStr = new String(magic);
            if (!EVENT_LOG_MAGIC.equals(magicStr)) {
                throw new IOException("Formato de log inválido: magic incorrecto");
            }
            
            dis.readFully(new byte[VERSION_LENGTH]);
            long startTimeNanos = dis.readLong();
            long endTimeNanos = dis.readLong();
            
            log.debug("Log couvre desde {} hasta {}", 
                Instant.ofEpochSecond(0, startTimeNanos), 
                Instant.ofEpochSecond(0, endTimeNanos));
            
            while (dis.available() > 0) {
                ReplayableEvent event = readEvent(dis);
                if (event != null) {
                    Instant eventInstant = Instant.ofEpochSecond(0, event.timestampNanos);
                    if (!eventInstant.isBefore(from) && !eventInstant.isAfter(to)) {
                        events.add(event);
                        eventIndex.put(eventInstant, event);
                    }
                }
            }
        }
        
        events.sort(Comparator.comparingLong(e -> e.timestampNanos));
        return events;
    }
    
    private ReplayableEvent readEvent(DataInputStream dis) throws IOException {
        try {
            long timestampNanos = dis.readLong();
            byte eventType = dis.readByte();
            int payloadLength = dis.readInt();
            byte[] payload = new byte[payloadLength];
            dis.readFully(payload);
            
            return new ReplayableEvent(timestampNanos, eventType, payload);
        } catch (EOFException e) {
            return null;
        }
    }
    
    private void publishEventToBuffer(ReplayableEvent event) {
        long sequence = replayBuffer.next();
        try {
            OrderEvent orderEvent = replayBuffer.get(sequence);
            orderEvent.setOrderId(new String(event.payload, 0, Math.min(36, event.payload.length)));
            orderEvent.setReceivedTimeNanos(event.timestampNanos);
        } finally {
            replayBuffer.publish(sequence);
        }
    }
    
    public void recordEvent(long timestampNanos, byte eventType, byte[] payload) {
        if (!running) {
            throw new IllegalStateException("Recorder no está inicializado");
        }
        ReplayableEvent event = new ReplayableEvent(timestampNanos, eventType, payload);
        eventIndex.put(Instant.ofEpochSecond(0, timestampNanos), event);
    }
    
    public void writeEventLog(Path outputPath) throws IOException {
        log.info("Escribiendo log de eventos a {}", outputPath);
        
        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(outputPath))) {
            dos.writeBytes(EVENT_LOG_MAGIC);
            dos.writeBytes("0001");
            
            long firstTimestamp = eventIndex.isEmpty() ? System.nanoTime() : 
                eventIndex.firstKey().toEpochMilli() * 1_000_000;
            long lastTimestamp = eventIndex.isEmpty() ? firstTimestamp : 
                eventIndex.lastKey().toEpochMilli() * 1_000_000;
            
            dos.writeLong(firstTimestamp);
            dos.writeLong(lastTimestamp);
            
            for (var entry : eventIndex.entrySet()) {
                ReplayableEvent event = entry.getValue();
                dos.writeLong(event.timestampNanos);
                dos.writeByte(event.eventType);
                dos.writeInt(event.payload.length);
                dos.write(event.payload);
            }
        }
        
        log.info("Log escrito con {} eventos", eventIndex.size());
    }
    
    public void stopReplay() {
        running = false;
        log.info("Solicitando parada de replay");
    }
    
    private void completeReplay() {
        if (replaying.compareAndSet(true, false)) {
            replayDisruptor.shutdown(5, TimeUnit.SECONDS);
            log.info("Replay detenido. Eventos procesados: {}", eventsReplayed.get());
        }
    }
    
    public long getEventsReplayedCount() {
        return eventsReplayed.get();
    }
    
    public boolean isReplaying() {
        return replaying.get();
    }
    
    public Map<Instant, ReplayableEvent> getEventIndex() {
        return Collections.unmodifiableMap(eventIndex);
    }
    
    public void shutdown() {
        stopReplay();
        replayExecutor.shutdown();
        try {
            if (!replayExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                replayExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            replayExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public static class ReplayableEvent {
        public final long timestampNanos;
        public final byte eventType;
        public final byte[] payload;
        
        public ReplayableEvent(long timestampNanos, byte eventType, byte[] payload) {
            this.timestampNanos = timestampNanos;
            this.eventType = eventType;
            this.payload = payload;
        }
    }
    
    private static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }
}


// === ARCHIVO: src/main/java/com/pragma/riskengine/var/VaRCalculator.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/circuitbreaker/CircuitBreakerConfig.java ===
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

// === ARCHIVO: src/main/java/com/pragma/riskengine/killswitch/KillSwitchPolicy.java ===
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

```
