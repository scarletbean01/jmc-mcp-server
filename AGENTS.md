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
| **Application** | Orchestrates use cases. Defines ports (interfaces) for infrastructure. | CDI-aware (`@ApplicationScoped`). Agnostic of MCP protocol specifics. |
| **Adapters** | Driving (Inbound): MCP Tools. Driven (Outbound): JfrProvider, Caches. | Protocol/Framework specific. Uses `@Tool`, `@Resource`, etc. |

---

## 📦 Project Structure

```
src/main/java/io/github/deplague/jmcmcp/
  ├── infrastructure/         # TECHNICAL: The implementation layer
  │   ├── mcp/                # DRIVING: Declarative adapters (@Tool, @Resource)
  │   │   └── resources/      # MCP Resource definitions
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

## 🚀 Declarative Tool Pattern (Quarkus MCP)

We use the **Quarkus MCP Server Extension**. All tools are declarative methods within `@ApplicationScoped` adapters.

### Engineering Standard: Tool Implementation
1.  **Annotate with `@Tool`:** Provide a concise but comprehensive description.
2.  **Use `@ToolArg`:** Use `snake_case` names. Mark `required = false` for optional parameters.
3.  **OPTIMIZE with `@RunOnVirtualThread`:** ALWAYS use this. JFR analysis is CPU-heavy and I/O bound; virtual threads ensure maximum throughput without blocking the event loop.
4.  **Return `ToolResponse`:** Leverage the factory methods `success()` and `error()`.

**Example Pattern:**
```java
@HandleToolError
@ApplicationScoped
public final class MyAnalysisTool {
    private final MyApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Perform deep JFR analysis")
    public ToolResponse doAnalysis(
            @ToolArg(name = "jfr_file_path") String path,
            @ToolArg(name = "top_n", required = false) Integer topN
    ) {
        var result = appService.execute(path, topN != null ? topN : 10);
        return ToolResponse.success(formatMarkdown(result));
    }
}
```

---

## 🛠️ JFR Infrastructure (The JfrItemUtils Facade)

Low-level JMC interactions are modularized in `adapters.infrastructure.jfr`. Use the **`JfrItemUtils`** facade as your entry point for item processing.

### Component Breakdown
- **`JfrAccessorRepository`:** High-performance caching of `IMemberAccessor`. Avoids O(N) scans of event attributes.
- **`JfrQuantityAggregator`:** Batch statistics (sum, avg, max, min, percentiles). Efficiently processes `IItemCollection`.
- **`JfrStackTraceService`:** Regex-aware frame matching and optimized formatting (identity-based caching).
- **`JfrValueConverter`:** Safe conversion between JMC `IQuantity`, numbers, and Markdown-friendly display strings.

---

## 💎 Engineering Standards & Style

- **Modern Java:** Use **Java 25** features (Records, Pattern Matching, Scoped Values if applicable).
- **Lombok (Mandatory):** Use `@Slf4j`, `@RequiredArgsConstructor(onConstructor_ = @Inject)`, `@Value`, and `@Builder`.
- **Naming:**
    - MCP Tool Names: `snake_case` (standardized).
    - Java Classes: `PascalCase`.
    - Variables/Methods: `camelCase`.
- **Logging & Observability:** All diagnostic output MUST go to **stderr** via SLF4J. stdout is reserved for MCP JSON-RPC. A CDI interceptor injects Contextual MDC logging (`tool` and `file`) into all log statements.
- **Markdown:** All tool responses must be well-formatted Markdown. Use `# H1` titles and GitHub-flavored tables. Always append an `<agent_hint>` block.

---

## 🧪 Testing Strategy

- **Architectural Integrity:** `ArchTest` (future) ensures no dependency leaks into the Domain layer.
- **Tool Integration:** Every tool in `adapters.mcp` must have a corresponding test class using real JFR fixtures (`before.jfr`, `after.jfr`).
- **Domain Unit:** Test `domain.service` logic with mocked `IItemCollection` or focused item iterables.

```bash
# Run the full suite including integration tests
mvn test
```

---

## 📋 MCP Tool Decision Matrix

| If you need to... | Use this tool |
|:---|:---|
| Discover file contents | `intellij-mcpserver:get_file_text_by_path` |
| Execute IDE logic | `mcp-steroid:steroid_execute_code` |
| **Analyze JFR Performance** | **Use `jmc-mcp` tools (this project)** |
| Modify code | `intellij-mcpserver:replace_text_in_file` |
| Fix many files | `mcp-steroid:steroid_apply_patch` |

---

## 🏁 How to Contribute a Feature

1.  **Domain:** Define the result Record and the `DomainService` (logic). Annotate the service with `@ApplicationScoped` for auto-discovery.
2.  **Application:** Define the `Port` (if infrastructure needed) and the `ApplicationService` (orchestration).
3.  **Adapter:** Create the `@HandleToolError` `@ApplicationScoped` tool class with `@Tool` and `@RunOnVirtualThread`.
4.  **Infra:** Implement any new ports in `adapters.infrastructure`.
5.  **Docs:** Update `README.md` and this guide if architectural patterns shift.
