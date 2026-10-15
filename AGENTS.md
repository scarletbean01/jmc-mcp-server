---

## 🏗️ Architecture & Structure
**Hexagonal (Clean) Architecture** via Maven multi-module. Dependencies point inward (`01-domain` <- `02-application` <- `03-infrastructure`).
- **`01-domain`**: Pure business logic, JFR heuristics. **Zero frameworks** (No Quarkus/CDI).
- **`02-application`**: Orchestration, Ports. CDI-aware (`@ApplicationScoped`), Protocol-agnostic.
- **`03-infrastructure`**: Driving Adapters (MCP, REST via Quarkus) & Driven Adapters (Caffeine, OTEL).

## 🚀 API & MCP Tools
- **REST Endpoints**: JFR management (`/upload`, `/{id}`), synchronous/async analysis (`/analyze/*`), A/B comparisons (`/compare/*`), and observability (`/metrics`, `/health/*`).
- **MCP Pattern**: Tools are `@Tool` annotated methods in `@ApplicationScoped` adapters.
  - **Tool Registry**: Tool names must be explicitly defined snake_case constants in `ToolNames` (e.g., `@Tool(name = ToolNames.HOT_METHODS)`). All tool names must be registered in `ToolNames.ALL`.
  - **Parameter Standards**:
    - Use `ToolDocs` constants for shared parameter descriptions (`JFR_FILE_PATH`, `HEAP_DUMP_PATH`, `START_TIME`, `END_TIME`, `PACKAGE_PREFIX`, `TOP_N_10`, `TOP_N_20`, `TOP_N_50`).
    - Use wrapper types (`Integer`, `Long`, `Double`, `Boolean`) for optional parameters with `@ToolArg(defaultValue = "...")`, never primitives (`int`, `long`), to prevent unboxing NPEs. Apply defensive defaults: `topN != null ? topN : 10`.
    - Validate time ranges via `TimeRangeArgs.validateRange(startTime, endTime)`.
  - **Error Handling & Interceptor Contract**:
    - Do NOT catch exceptions inline in MCP tools. Declare `throws Exception` and annotate tool classes with `@HandleToolError`.
    - `ToolErrorInterceptor` sets MDC `tool` to `@Tool.name()` (snake_case).
    - Records OpenTelemetry span errors and times executions via Micrometer `jmc.mcp.tool.duration` tagged with `tool` and `outcome` (`success`/`error`).
    - Maps exceptions to clean, safe MCP error responses (`ToolResponse.error(message)`):
      - `PathAccessDeniedException` (allowlist violations): Fixed message `"Access denied: path not allowed under JMC_MCP_ALLOWED_PATHS"` (avoids leaking server paths or allowlists).
      - Any other `SecurityException`: its message, prefixed with `"Access denied: "`. These messages reach the client, so never put server paths or allowlists in them.
      - `RecordingNotFoundException`: `"Recording not found: " + msg`.
      - `IllegalArgumentException`: `"Invalid argument: " + msg`.
      - `ValidationException` (incl. `ConstraintViolationException`): `"Validation error: " + msg`.
      - `JmcMcpDomainException`: the exception's message, unchanged.
      - Null messages: `"Error: " + e.getClass().getSimpleName()`.
  - **Output Bounds**: All markdown responses must pass through `ToolOutput.limit(markdown)` (configurable via `jmc.mcp.max-output-chars`, default 40,000 characters) to safeguard LLM context windows. `ToolOutput` automatically reserves budget for notices, closes unclosed markdown code fences (`\n````), and preserves trailing `<agent_hint>` instructions.
  - **Agent Hints**: Always include actionable `<agent_hint>...</agent_hint>` guidance suggesting next-step tools using canonical backticked tool names (e.g., `` `correlate` ``, `` `hot_methods` ``).
  - **Contract Enforcement**: Enforced by `ToolContractTest` via reflection:
    - Verifies all tool classes are `@ApplicationScoped` and annotated with `@HandleToolError`.
    - Verifies all `@Tool` methods have `@RunOnVirtualThread` and never `@Blocking`.
    - Verifies tool names are snake_case and present in `ToolNames.ALL`.
    - Verifies optional arguments use wrapper types, not primitives.
    - Verifies descriptions match `ToolDocs` constants.
    - Verifies `<agent_hint>` blocks reference valid tools registered in `ToolNames.ALL`.

## ⚡ Infrastructure & Observability
- **Concurrency**: All MCP and REST entry points use `@RunOnVirtualThread` (never `@Blocking`). CPU-heavy JMC parsing is offloaded to `WorkStealingPool` (`CompletableFuture`) to avoid pinning Virtual Thread carriers.
- **Security & Path Control**:
  - `RecordingAccessController`:
    - Validates recording and heap dump paths against configured `allowed-paths` (default `.` / cwd) with real-path canonicalization (`toRealPath()`).
    - Resolves the deepest existing ancestor for non-existent target paths (e.g., write/dump destinations) to prevent symlink bypasses.
    - Path traversal (`..`) and invalid characters throw `SecurityException`.
    - `validate(path, allowedExtensions)` returns the canonical path it checked; use that, not the raw input, for the subsequent I/O. Extensions are enforced on the resolved target and only where a caller passes them (heap dumps: `.hprof`, `.phd`, `.dump`; live-recording dumps: `.jfr`). Allowed URI schemes default to `file`.
    - Dangling symlinks are rejected (not followed), because resolving their targets textually can disagree with the kernel.
  - Live Recording:
    - Host validation restricts targets to loopback hosts (`localhost`, `127.0.0.1`, `[::1]`, `::1`) by default, configurable via `jmc.live.allowed-jmx-hosts`.
    - Enforces `rmi` protocol and a `/jndi/rmi://<allowed host>/…` URL path with `JMXServiceURL` parsing; `/stub/`, `/ior/`, other JNDI schemes and empty paths are rejected.
    - Enforces `Action` enum (`list`, `start`, `stop`, `dump`), validates settings (`default`, `profile`), and ensures dump files are written to safe destinations.
- **Caching**: `JfrRecordingCache` (weight-based, 3.5x multiplier), `HeapDumpCache`, `AnalysisResultCache` (domain results, 15m TTL), `CallTreeCache`.
- **Observability**: Micrometer (`AnalysisMetrics`, `jmc.mcp.tool.duration`) and OpenTelemetry (`@WithSpan` on `AnalysisDispatcher`, span error recording in `ToolErrorInterceptor`).

## 💎 Engineering Standards
- **Java 25**, Lombok (`@Slf4j`, `@RequiredArgsConstructor(onConstructor_ = @Inject)`), Jakarta Validation.
- Log exclusively to **stderr**; stdout is strictly reserved for the MCP protocol.
- **Strict Layering**: Never import `infrastructure` classes into `domain` or `application`.

## 📋 AI Agent Guidelines
1. **Tool Usage & Performance**: Leverage `steroid_execute_code` for project manipulation. **CRITICAL**: You must load the `intellij-mcp` skill and read its reference guides before performing edits or semantic searches.
2. **Resource Safety**: Always use try-with-resources for I/O and JFR streams.
3. **Cache Awareness**: Register new analysis types in `AnalysisDispatcher` and cache results in `AnalysisResultCache`.
4. **JMC Performance**: Prefer `IItemCollection` filtering over manual iteration. Use `JfrQuantityAggregator`.
5. **Time Parameters**: Passed backend times (`start-time`/`end-time`) must be absolute (ISO-8601 strings or epoch integers).
6. **Frontend**: Do NOT commit compiled static assets (`js/compiled/`, `index.html`, `css/`) to the source tree (`src/.../resources/`); Maven handles this to prevent stale shadowing in dev mode.

