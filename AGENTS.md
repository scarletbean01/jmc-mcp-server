---

## 🏗️ Architectural Vision: Hexagonal (Ports & Adapters)

The project follows a strict **Hexagonal Architecture** (Clean Architecture) distributed across a **Maven multi-module** setup. This ensures the core JFR analysis logic remains decoupled from delivery mechanisms (MCP, REST) and external frameworks (Quarkus).

### Dependency Rule
**Dependencies point inward.** Domain has zero knowledge of Application, and both have zero knowledge of Infrastructure.

| Module | Layer | Responsibility | Constraints |
|:---|:---|:---|:---|
| **`01-domain`** | **Domain** | Pure business logic, JFR heuristics, models. | **Zero frameworks.** No Quarkus/CDI. |
| **`02-application`** | **Application** | Use case orchestration, Port definitions. | CDI-aware (`@ApplicationScoped`). Protocol agnostic. |
| **`03-infrastructure`** | **Infrastructure** | Driving Adapters (MCP, REST) & Driven (JFR Cache). | Framework specific (Quarkus, Caffeine, OTEL). |
| **`00-bom`** | **BOM** | Centralized Dependency Management. | No code. POM only. |

---

## 📦 Project Structure

```
.
├── 00-bom/                   # Centralized versions (Bill of Materials)
├── 01-domain/                # CORE: Pure logic & JMC heuristics
│   └── src/main/java/.../domain/
│       ├── model/            # Pure Java Records (Result types)
│       ├── service/          # Core analysis logic (Pure Java + JMC)
│       └── exception/        # Domain-specific exceptions
├── 02-application/           # USE CASES: Orchestration
│   └── src/main/java/.../application/
│       ├── port/             # SPIs (e.g., JfrProvider)
│       └── service/          # Orchestrators (Async jobs, Storage)
└── 03-infrastructure/        # TECHNICAL: Adapters & Frameworks
    └── src/main/java/.../infrastructure/
        ├── mcp/              # DRIVING: MCP Tools (@Tool)
        ├── api/              # DRIVING: REST API (Quarkus REST)
        ├── jfr/              # DRIVEN: JMC Parsing & Caffeine Cache
        ├── security/         # Technical Guards (Access Control)
        └── observability/    # Metrics (Micrometer) & Tracing (OTEL)
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
7.  **Time Parameters:** All time parameters passed to the backend (`start-time` and `end-time`) must be absolute (ISO-8601 strings or epoch millisecond/second integers). Relative second offsets (e.g., `0` to `30`) are converted to absolute ISO-8601 values in the frontend using the recording's start epoch.
