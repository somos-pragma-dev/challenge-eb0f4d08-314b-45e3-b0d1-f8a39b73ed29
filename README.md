# Evaluación de riesgo en tiempo real para trading algorítmico

El sistema de trading necesita un motor de risk scoring que evalúe el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos |
| **Nivel** | master-l2 |
| **Tipo** | mixed |
| **Tiempo estimado** | 4 semanas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Diseño del motor de risk scoring

**Objetivo:** Definir la arquitectura y el flujo de trabajo del motor de risk scoring.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Identificar los componentes principales del motor de risk scoring.
- Describir el flujo de datos desde la recepción de la orden hasta la decisión de riesgo.
- Justificar la elección de las tecnologías y estructuras de datos a utilizar.

**Entregable:** Diagrama de arquitectura y descripción del flujo de trabajo.

<details>
<summary>Pistas de conocimiento</summary>

- Considera las latencias y throughput requeridos por el dominio.
- Evalúa las ventajas y desventajas de diferentes tecnologías y estructuras de datos.

</details>

### Fase 2: Implementación del modelo de VaR intraday

**Objetivo:** Implementar y validar el modelo de VaR intraday.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Definir los parámetros y variables del modelo de VaR intraday.
- Implementar el cálculo del VaR intraday.
- Validar el modelo con datos históricos.

**Entregable:** Modelo de VaR intraday implementado y validado.

<details>
<summary>Pistas de conocimiento</summary>

- Considera la volatilidad y los movimientos del mercado.
- Valida el modelo con diferentes escenarios de mercado.

</details>

### Fase 3: Aplicación de límites y circuit breakers

**Objetivo:** Aplicar límites por trader/estrategia/instrumento y disparar circuit breakers dinámicos.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Definir los límites por trader/estrategia/instrumento.
- Implementar la lógica para aplicar los límites.
- Diseñar y disparar circuit breakers dinámicos cuando la exposición supera los thresholds.

**Entregable:** Lógica de aplicación de límites y circuit breakers implementada.

<details>
<summary>Pistas de conocimiento</summary>

- Considera la volatilidad y los movimientos del mercado.
- Evalúa diferentes estrategias para disparar circuit breakers.

</details>

### Fase 4: Política de kill switch y estrategia de replay determinístico

**Objetivo:** Definir la política de kill switch y la estrategia de replay determinístico.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Definir la política de kill switch para algoritmos anómalos.
- Diseñar la estrategia de replay determinístico para post-mortem de incidents.
- Implementar y validar la política de kill switch y la estrategia de replay determinístico.

**Entregable:** Política de kill switch y estrategia de replay determinístico implementadas y validadas.

<details>
<summary>Pistas de conocimiento</summary>

- Considera los impactos de la política de kill switch en el sistema.
- Evalúa diferentes estrategias para el replay determinístico.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es un motor de risk scoring y cuáles son sus componentes principales?
- **paraQueSirve**: ¿Para qué sirve un motor de risk scoring en el contexto de trading algorítmico?
- **comoSeUsa**: ¿Cómo se usa un motor de risk scoring para evaluar el riesgo de una orden?
- **erroresComunes**: ¿Cuáles son los errores comunes al implementar un motor de risk scoring?
- **queDecisionesImplica**: ¿Qué decisiones implica la implementación de un motor de risk scoring en términos de tecnología y arquitectura?

## Criterios de Evaluacion

- Definir la arquitectura y el flujo de trabajo del motor de risk scoring.
- Implementar y validar el modelo de VaR intraday.
- Aplicar límites por trader/estrategia/instrumento y disparar circuit breakers dinámicos.
- Definir la política de kill switch y la estrategia de replay determinístico.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
el comando de build o arranque canonico del stack elegido
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
