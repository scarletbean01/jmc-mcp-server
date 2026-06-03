# Module: 03-infrastructure (jmc-mcp-infrastructure)

This is the technical implementation layer, containing adapters for external protocols (MCP, REST), monitoring, and JFR parsing. It depends on `02-application`.

## Sub-packages
- **`mcp`**: DRIVING: Model Context Protocol adapters (Tools and Resources).
- **`api`**: DRIVING: REST API, Metrics (`AnalysisMetrics`), and Health Checks (`JmcReadinessCheck`).
- **`jfr`**: DRIVEN: JMC-based parsing, high-performance caches, and stack trace utilities.
- **`security`**: Technical guards (`RecordingAccessController`) for path validation.

## Key Responsibilities
- **Protocol Translation:** Converting MCP/REST requests to domain calls and formatting responses.
- **Persistence & Caching:** Implementing the `JfrProvider` port and managing heavy object lifecycles with Caffeine.
- **Observability:** Providing Prometheus metrics via Micrometer and health status via SmallRye.
- **System Safety:** Validating file paths and access permissions.

## Patterns Used
- **Adapter Pattern:** Implementing domain ports for specific technical providers.
- **Interceptors:** Using CDI Interceptors (e.g., `ToolErrorInterceptor`) for cross-cutting concerns like error handling and MDC logging.
- **Caching:** Multi-layered caching (Recordings, Analysis Results, Call Trees) using Caffeine.

## Guidelines for Agents
- **Separation:** Ensure technical details (JMC internals, JSON-RPC, Caffeine configs) do not leak into the domain layer.
- **Concurrency:** Use `@RunOnVirtualThread` for all entry points to leverage Java 25's lightweight concurrency.
- **Tracing & Metrics:** Use `@WithSpan` (OpenTelemetry) and `AnalysisMetrics` to ensure operations are observable.
- **Security:** Always invoke `RecordingAccessController.validate()` before reading user-provided file paths.
- **Static Assets Caching & Shadowing:** Do not check compiled static assets (`js/compiled/`, `index.html`, or `css/`) into `src/main/resources/META-INF/resources/`. They will shadow target resources and cause stale caching issues in Quarkus dev mode. Let the Maven build and resources plugins copy them automatically from the `frontend/resources/public/` directory into `target/classes`.
