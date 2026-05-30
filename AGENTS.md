# JMC MCP Server — Java Architect's Guide

This document defines the architectural vision, engineering standards, and structural blueprints for the `jmc-mcp` project. It is the primary context for AI agents working on this codebase.

---

## 🏗️ Architectural Vision: Hexagonal (Ports & Adapters)

The project follows a strict **Hexagonal Architecture** (also known as Clean Architecture) to ensure that the core JFR analysis logic remains decoupled from the delivery mechanisms (MCP, CLI) and external frameworks (Quarkus, JMC).

### Dependency Rule
**Dependencies point inward.** The Domain layer has zero knowledge of the Application layer, which in turn has zero knowledge of the Adapters.

| Layer | Responsibility | Constraints |
|:---|:---|:---|
| **Domain** | Pure business logic, JFR metric computation, models. | **Zero frameworks.** No Quarkus/Jakarta annotations. Lombok is permitted as a compile-only tool. |
| **Application** | Orchestrates use cases. Defines ports (interfaces) for infrastructure. | CDI-aware (`@ApplicationScoped`). Agnostic of driving protocol (MCP/REST). |
| **Adapters** | Driving (Inbound): MCP Tools, REST API. Driven (Outbound): JfrProvider, Caches. | Protocol/Framework specific. Uses `@Tool`, `@POST`, etc. |

---

## 📦 Project Structure

```
src/main/java/io/github/deplague/jmcmcp/
  ├── infrastructure/         # TECHNICAL: The implementation layer
  │   ├── mcp/                # DRIVING: MCP Adapters (@Tool, @Resource)
  │   ├── api/                # DRIVING: REST API Adapters (Quarkus REST)
  │   │   └── model/          # API Request/Response wrappers
  │   ├── jfr/                # OUTBOUND: Port implementations (JFR Loading, Caching)
  │   │   └── util/           # Low-level JMC access (AccessorRepo, Aggregators)
  │   └── security/           # Technical Guards (Access Control)
  ├── application/
  │   ├── port/               # Interface definitions for Outbound adapters
  │   └── service/            # Use case orchestrators (return Domain Records)
  ├── domain/
  │   ├── model/              # Pure Java Records (Result types)
  │   ├── service/            # Core analysis logic (Pure Java + JMC Core)
  │   └── util/               # Math and logic utilities
  └── JmcMcpServer.java       # Infrastructure: Quarkus bootstrap & lifecycle
```

---

## 🚀 Driving Adapters (MCP & REST)

We use Quarkus to expose JFR analysis via multiple protocols. All driving logic reuses the same **Application Services**.

### MCP Tools (Declarative)
All tools are declarative methods within `@ApplicationScoped` adapters in `infrastructure.mcp`.
- **Annotate with `@Tool`** and **`@HandleToolError`**.
- **Use `@RunOnVirtualThread`** for all analysis tasks.
- **Return `ToolResponse`** (Markdown formatted).

### REST API (Hexagonal Driving Adapter)
The public REST API lives in `infrastructure.api`. All endpoints use `@RunOnVirtualThread`, return JSON wrapped in `ApiResponse<T>`, and support CORS (`quarkus.http.cors.enabled=true`).

#### Recording Management
| Endpoint | Method | Description |
|----------|--------|-------------|
| `POST /api/v1/recordings/upload` | Upload | Upload a JFR file via multipart, get `recordingId` |
| `GET /api/v1/recordings/{id}` | Status | Get recording metadata (filename, size, event count) |
| `DELETE /api/v1/recordings/{id}` | Cleanup | Remove recording from disk and index |

#### Analysis (Synchronous)
| Endpoint | Method | Description |
|----------|--------|-------------|
| `POST /api/v1/recordings/{id}/analyze/{type}` | Analysis | Run analysis synchronously. Body: `AnalysisRequest` |

#### Analysis (Asynchronous)
| Endpoint | Method | Description |
|----------|--------|-------------|
| `POST /api/v1/recordings/{id}/analyze/{type}/async` | Analysis | Returns `202 Accepted` with `jobId` |
| `GET /api/v1/recordings/{id}/analyze/jobs/{jobId}` | Poll | Get async job status, result, and error |
| `GET /api/v1/recordings/{id}/analyze/jobs/{jobId}/stream` | SSE | Stream job progress/completion via Server-Sent Events |

#### Comparison & Health
| Endpoint | Method | Description |
|----------|--------|-------------|
| `POST /api/v1/compare` | Comparison | Compare two recordings. Body: `CompareRequest` |
| `GET /api/v1/health` | Health | JVM uptime, heap/non-heap memory, processor count |

#### Call Tree Expansion
| Endpoint | Method | Description |
|----------|--------|-------------|
| `POST /api/v1/recordings/{id}/analyze/call-tree/{treeId}/expand?nodeId={nodeId}` | Expand | Expand a cached call-tree node |

#### Key Patterns
- **Request Body:** `AnalysisRequest` — optional `startTime`, `endTime`, and a `params` map for analysis-specific filters (`topN`, `packagePrefix`, `threadName`, etc.).
- **Response Wrapper:** `ApiResponse<T>` — `{ "success": true/false, "data": ..., "error": ..., "timestamp": ... }`.
- **Persistence:** Recordings are stored in a configurable `storage.path` (default: `uploads/`) and cleaned up by a background `@Scheduled(every = "1h")` task after 24 hours.
- **SSE:** The stream endpoint sends `job-update` events as JSON. It auto-closes when the job reaches `COMPLETED` or `FAILED`.

---

## 🛠️ JFR Infrastructure (The JfrItemUtils Facade)

Low-level JMC interactions are modularized in `infrastructure.jfr`. Use the **`JfrItemUtils`** facade as your entry point for item processing.

### Component Breakdown
- **`JfrAccessorRepository`:** High-performance caching of `IMemberAccessor`. Avoids O(N) scans of event attributes.
- **`JfrQuantityAggregator`:** Batch statistics (sum, avg, max, min, percentiles). Efficiently processes `IItemCollection`.
- **`JfrStackTraceService`:** Regex-aware frame matching and optimized formatting (identity-based caching).
- **`JfrValueConverter`:** Safe conversion between JMC `IQuantity`, numbers, and Markdown-friendly display strings.
- **`RecordingStorageService`:** Manages persistent JFR storage and lifecycle (24h retention policy).

---

## 💎 Engineering Standards & Style

- **Modern Java:** Use **Java 25** features (Records, Pattern Matching).
- **Lombok (Mandatory):** Use `@Slf4j`, `@RequiredArgsConstructor(onConstructor_ = @Inject)`.
- **Logging:** All diagnostic output MUST go to **stderr** via SLF4J. stdout is reserved for MCP JSON-RPC. REST API logs are also stderr.
- **Markdown:** MCP tool responses must be well-formatted Markdown. REST API responses are raw JSON domain records.

---

## 🧪 Testing Strategy

- **Architectural Integrity:** `ArchTest` ensures no dependency leaks into the Domain layer.
- **Tool Integration:** Every tool must have a corresponding test class using real JFR fixtures.
- **REST Integration:** Use `@QuarkusTest` and RestAssured to verify API endpoints.

```bash
# Run the full suite including integration tests
mvn test
```

---

## 📋 Tool & API Decision Matrix

| If you need to... | Use this |
|:---|:---|
| Discover file contents | `intellij-mcpserver:get_file_text_by_path` |
| Execute IDE logic | `mcp-steroid:steroid_execute_code` |
| **Analyze JFR (MCP)** | **Use `jmc-mcp` tools** |
| **Analyze JFR (REST)** | **Use `POST /api/v1/recordings/{id}/analyze/{type}`** |
| Check Server Health | `GET /api/v1/health` or `HealthCheckTool` |
| Compare Recordings | `POST /api/v1/compare` or `CompareRecordingsTool` |


---

## 🏁 How to Contribute a Feature

1.  **Domain:** Define the result Record and the `DomainService` (logic). Annotate the service with `@ApplicationScoped` for auto-discovery.
2.  **Application:** Define the `Port` (if infrastructure needed) and the `ApplicationService` (orchestration).
3.  **Adapter:** Create the `@HandleToolError` `@ApplicationScoped` tool class with `@Tool` and `@RunOnVirtualThread`.
4.  **Infra:** Implement any new ports in `adapters.infrastructure`.
5.  **Docs:** Update `README.md` and this guide if architectural patterns shift.
