---

## 🏗️ Architectural Vision: Hexagonal (Ports & Adapters)

The project follows a strict **Hexagonal Architecture** (Clean Architecture) to ensure the core JFR analysis logic remains decoupled from delivery mechanisms (MCP, REST) and external frameworks (Quarkus, JMC).

### Dependency Rule
**Dependencies point inward.** The Domain layer has zero knowledge of the Application layer, which in turn has zero knowledge of the Adapters.

| Layer | Responsibility | Constraints |
|:---|:---|:---|
| **Domain** | Pure business logic, JFR metric computation, models. | **Zero frameworks.** No Quarkus/Jakarta annotations. |
| **Application** | Orchestrates use cases. Defines ports (interfaces) for infrastructure. | CDI-aware (`@ApplicationScoped`). Agnostic of protocol. |
| **Infrastructure** | Driving: MCP Tools, REST API. Driven: JFR Loading, Caching, Metrics. | Protocol/Framework specific. Uses `@Tool`, `@POST`, etc. |

---

## 📦 Project Structure

```
src/main/java/io/github/deplague/jmcmcp/
  ├── domain/                 # CORE: Pure logic & models
  │   ├── model/              # Pure Java Records (Result types)
  │   ├── service/            # Core analysis logic (Pure Java + JMC Core)
  │   └── exception/          # Domain-specific exceptions
  ├── application/            # USE CASES: Orchestration
  │   ├── port/               # Interface definitions for Outbound adapters (e.g., JfrProvider)
  │   └── service/            # Use case orchestrators (returning Domain Records)
  ├── infrastructure/         # TECHNICAL: Implementation layer
  │   ├── mcp/                # DRIVING: MCP Adapters (@Tool, @McpTool)
  │   ├── api/                # DRIVING: REST API Adapters (Quarkus REST)
  │   │   ├── health/         # Observability: Liveness/Readiness checks
  │   │   ├── metrics/        # Observability: Micrometer metrics
  │   │   └── model/          # DTOs for REST layer (ApiResponse, etc.)
  │   ├── jfr/                # OUTBOUND: JFR Loading & Advanced Caching
  │   └── security/           # Technical Guards (Access Control)
  └── JmcMcpServer.java       # Bootstrap: Quarkus entry point
```

---

## 🚀 REST API Reference

The server exposes a comprehensive REST API for JFR management and analysis.

### Recording Management
- `POST /api/v1/recordings/upload` — Upload a JFR file (multipart/form-data).
- `GET /api/v1/recordings` — List all uploaded recordings.
- `GET /api/v1/recordings/{id}` — Get metadata for a recording.
- `DELETE /api/v1/recordings/{id}` — Delete a recording.

### Analysis Endpoints
- `POST /api/v1/recordings/{id}/analyze/{type}` — Synchronous analysis.
- `POST /api/v1/recordings/{id}/analyze/{type}/async` — Asynchronous analysis (returns `jobId`).
- `GET /api/v1/recordings/{id}/analyze/jobs/{jobId}` — Poll async job status.
- `GET /api/v1/recordings/{id}/analyze/jobs/{jobId}/stream` — SSE stream for job updates.
- `POST /api/v1/compare/call-tree/{treeId}/expand` — Interactive differential call tree expansion.

### Comparison (A/B Deep-Dive)
- `POST /api/v1/compare` — Compare two recordings (textual/markdown).
- `POST /api/v1/compare/structured` — Compare two recordings (JSON model) encompassing KPIs, Rule changes, and top hotspots.
- `POST /api/v1/compare/call-tree` — Generate a differential call tree.
- `POST /api/v1/compare/stack-traces` — Generate a flattened method-level performance shift analysis.

### Observability
- `GET /metrics` — Prometheus metrics (Micrometer).
- `GET /health/live` — Liveness probe.
- `GET /health/ready` — Readiness probe (checks storage and cache sanity).
- `GET /api/v1/health` — Internal JVM health and memory metrics.

---

## 🛠️ MCP Tools Pattern

All tools are methods within `@ApplicationScoped` adapters in `infrastructure.mcp`.

- **Declarative Tools:** Annotate methods with `@Tool` and specify parameters using `@RequestParam`.
- **Error Handling:** Use `@HandleToolError` to map domain exceptions to MCP-compatible errors.
- **Specification:** Each tool implements the `McpTool` interface to expose its `SyncToolSpecification`.
- **Backpressure:** Long-running tools should check for cancellation where possible.

---

## ⚡ JFR Infrastructure & Performance

The server is optimized for high-volume JFR analysis using advanced caching and concurrency models.

### Advanced Caching (Caffeine)
- **`JfrRecordingCache`:** Weight-based cache for parsed `IItemCollection` objects. Estimates heap impact using a multiplier (default 3.5x file size) to prevent OOM.
- **`AnalysisResultCache`:** Caches expensive domain-level analysis results with TTL (15m default).
- **`CallTreeCache`:** Specialized cache for `StacktraceTreeModel` instances to support UI/Tool pagination.

### Concurrency & Virtual Threads
- **Virtual Threads:** All entry points (REST/MCP) use `@RunOnVirtualThread`.
- **Pinning Avoidance:** CPU-heavy JFR parsing in `JfrRecordingCache` is offloaded to a dedicated **Platform Thread Pool** (`WorkStealingPool`) via `CompletableFuture` to avoid pinning VT carriers during native code execution or intensive JMC parsing.

---

## 📊 Observability

- **Metrics:** `AnalysisMetrics` tracks active analyses, cache hit/miss/eviction rates, upload sizes, and job queue depth.
- **Health:** `/health/ready` validates that the `uploads` directory is writable and the Caffeine caches are operational.
- **Tracing:** OpenTelemetry integration. `AnalysisDispatcher.dispatch` is annotated with `@WithSpan` to track the full lifecycle of analysis requests.

---

## 💎 Engineering Standards & Style

- **Modern Java:** **Java 25** (Records, Pattern Matching, Sealed Classes).
- **Lombok:** Use `@Slf4j` and `@RequiredArgsConstructor(onConstructor_ = @Inject)`.
- **Validation:** Use Jakarta Validation (`@NotBlank`, `@Min`, etc.) on model records.
- **Logging:** Log exclusively to **stderr** via SLF4J (Quarkus configuration). Reserve stdout for the MCP protocol.
- **Error Handling:** Use `domain.exception` hierarchy. `ToolErrorInterceptor` or `@HandleToolError` handles the mapping.

---

## 📋 AI Agent Guidelines

1.  **Strict Layering:** Never import `infrastructure` classes into `domain` or `application`.
2.  **Virtual Threads:** Always use `@RunOnVirtualThread` for entry points.
3.  **Cache Awareness:** When adding new analysis types, ensure they are registered in `AnalysisDispatcher` and results are cached in `AnalysisResultCache`.
4.  **Resource Safety:** Always use try-with-resources for I/O and JFR streams.
5.  **Tests:** Add tool integration tests in `src/test/java/.../mcp` using real JFR fixtures located in `src/test/resources`.
6.  **Performance:** Prefer `IItemCollection` filtering over manual iteration where possible. Use `JfrQuantityAggregator` for efficient data reduction.
