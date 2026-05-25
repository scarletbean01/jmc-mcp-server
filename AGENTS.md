# JMC MCP Server — Agent Guide

This document contains project-specific context for AI coding agents working on `jmc-mcp`.

---

## Project Overview

`jmc-mcp` is a [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server that exposes **57 tools** for analyzing Java Flight Recorder (JFR) recordings. It uses the Java Mission Control (JMC) 9.1.1 core libraries to parse recordings and return structured Markdown reports to LLM agents.

The server communicates over **stdio** (stdin/stdout) using MCP's JSON-RPC protocol. stdout is reserved exclusively for MCP messages; all logging goes to stderr via logback.

### Key Facts

- **Language:** Java 21
- **Build Tool:** Maven 3.9+
- **Main Class:** `io.github.deplague.jmcmcp.JmcMcpServer`
- **Artifact:** `target/jmc-mcp-1.0.0-SNAPSHOT.jar` (fat JAR via maven-shade-plugin)
- **Transport:** StdioServerTransportProvider with Jackson 3 JSON mapper
- **MCP SDK Version:** 1.1.2 (`io.modelcontextprotocol.sdk:mcp`)
- **License:** MIT

---

## Build and Test Commands

```bash
# Compile and package the fat JAR
mvn clean package

# Run tests only
mvn test

# The fat JAR is produced at:
# target/jmc-mcp-1.0.0-SNAPSHOT.jar
```

The Maven build uses:
- `maven-compiler-plugin` 3.14.0 (release 21)
- `maven-surefire-plugin` 3.5.3
- `maven-shade-plugin` 3.6.0 (packages all dependencies into the executable JAR)

---

## Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| MCP SDK | 1.1.2 | MCP server framework, JSON-RPC, stdio transport |
| JMC (common, flightrecorder, rules, rules.jdk) | 9.1.1 | JFR parsing, rules engine, aggregations |
| Jackson | 2.18.3 | JSON serialization for MCP messages |
| SLF4J | 2.0.17 | Logging facade |
| Logback | 1.5.18 | Logging implementation (stderr only) |
| JUnit Jupiter | 5.12.1 | Unit testing |
| AssertJ | 3.27.3 | Fluent assertions |

---

## Code Organization

```
src/main/java/io/github/deplague/jmcmcp/
  JmcMcpServer.java          # Entry point — bootstraps MCP server and registers all tools
  jfr/
    JfrAnalysisService.java  # Core service: load recordings, time-range filtering,
                             # recording overview, per-tool result caching
    JfrRecordingCache.java   # In-memory ConcurrentHashMap cache of parsed IItemCollection
    JfrItemUtils.java        # Reflection-free attribute extraction, quantity aggregation
                             # (sum, avg, min, max, percentile), stack trace formatting
  tools/
    SchemaUtil.java          # Helpers for building McpSchema.JsonSchema and parsing args
    *Tool.java               # One class per MCP tool (~57 files)

src/test/java/io/github/deplague/jmcmcp/
  tools/                    # Unit & integration tests for individual tools
  jfr/                      # Tests for JfrRecordingCache and JfrAnalysisService
  JmcApiDiscoverer.java     # Utility for discovering JMC API surfaces

src/main/resources/
  logback.xml               # Logging config: root WARN to stderr; mcp/jmc suppressed to WARN

# Test fixtures (real JFR recordings, ~38MB and ~46MB)
before.jfr
after.jfr
```

### Package Structure

- **`io.github.deplague.jmcmcp`** — Server bootstrap only.
- **`io.github.deplague.jmcmcp.jfr`** — JFR loading, caching, filtering, and low-level item utilities. No MCP-specific code here.
- **`io.github.deplague.jmcmcp.tools`** — MCP tool implementations. Each tool defines its schema, call handler, and analysis logic.

---

## How to Add a New Tool

1. Create a new class in `src/main/java/io/github/deplague/jmcmcp/tools/<Name>Tool.java`.
2. Follow the existing pattern:
   - `private static final String NAME = "snake_case_tool_name";`
   - Constructor takes `JfrAnalysisService` (or no args for special tools like `LiveRecordingTool`).
   - `public SyncToolSpecification spec()` defines the MCP schema and call handler.
   - The call handler parses arguments with `SchemaUtil`, checks the cache, calls `analyze(...)`, caches the result, and returns `CallToolResult`.
   - `analyze(...)` returns a Markdown string.
3. Register the tool in `JmcMcpServer.java` inside the `tools` list.
4. Add a unit test in `src/test/java/io/github/deplague/jmcmcp/tools/<Name>ToolTest.java`.
5. Update `ToolSchemaTest` if tool-name uniqueness is being asserted there.

### Tool Conventions

- **Schema:** Use `SchemaUtil.objectSchema(SchemaUtil.props(...), SchemaUtil.required(...))`.
- **Common properties:** Most JFR analysis tools accept `jfr_file_path`, `start_time`, and `end_time` (ISO-8601). Reuse `SchemaUtil.commonJfrProps()`.
- **Optional args:** Use `SchemaUtil.getStringOrDefault`, `getIntOrDefault`, etc. The MCP client may send integers as strings.
- **Caching:** Wrap analysis with `service.getCachedResult(filePath, NAME, request.arguments())` and `service.cacheResult(...)`.
- **Time filtering:** Call `service.filterByTimeRange(events, startTimeStr, endTimeStr)` before analysis.
- **Error handling:** Catch exceptions in the call handler and return `CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build()`.
- **Output format:** Return Markdown. Start with `# Title`. Use tables where appropriate. End with an `<agent_hint>` block suggesting related tools.
- **Stack traces:** Use `JfrItemUtils.formatStackTrace(obj, maxFrames)` for truncated traces or `JfrItemUtils.formatFullStackTrace(obj)` for complete traces.
- **Quantity formatting:** Use `JfrAnalysisService.display(quantity)` for human-readable units.

### Agent Hints

Many tools append an `<agent_hint>` block at the end of their output to guide the LLM to the next most useful tool. Example from `HotMethodsTool`:

```markdown
<agent_hint>Top hot method is `SomeClass.someMethod()`. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>
```

When modifying existing tools, preserve or enhance these hints.

---

## Testing Strategy

### Test Framework

- **JUnit 5** (`junit-jupiter` 5.12.1)
- **AssertJ** (`assertj-core` 3.27.3)
- Tests run via `maven-surefire-plugin`.

### Test Fixtures

Two real JFR recordings sit in the project root and are used as integration-test fixtures:

- `before.jfr` (~46 MB)
- `after.jfr` (~38 MB)

Tests resolve them via a helper pattern:

```java
private static String resolveJfr(String name) {
    File file = new File(name);
    if (!file.exists()) {
        file = new File(System.getProperty("user.dir"), name);
    }
    assertThat(file).exists();
    return file.getAbsolutePath();
}
```

### Test Patterns

- **Schema validation:** `ToolSchemaTest` sanity-checks that tool names are unique.
- **Happy path:** Verify Markdown output contains expected sections and tables.
- **Empty data:** Verify graceful handling when no events match (e.g., impossible time range).
- **Parameter validation:** Verify missing required args produce `isError(true)` with "Missing required argument".
- **Missing files:** Verify nonexistent JFR paths produce `isError(true)`.
- **Caching:** Call the same request twice and assert identical output.
- **Filtering:** Verify `top_n`, `thread_name`, `package_prefix`, and time ranges limit or alter results.

### Running Tests

```bash
mvn test
```

If you add a new tool, add a corresponding `*ToolTest.java` and run the suite to ensure no regressions.

---

## Code Style Guidelines

The codebase follows a consistent but lightweight style:

- **Indentation:** 4 spaces (no tabs).
- **Imports:** `java.*` first, then third-party, then project-local. No wildcard imports.
- **Class design:** Tool classes are `public final`. Utility classes (`SchemaUtil`, `JfrItemUtils`) have private constructors.
- **Records:** Freely use Java records for internal DTOs (e.g., `WaterfallEvent`, `RecordingOverview`).
- **Logging:** Use SLF4J `LoggerFactory.getLogger(ClassName.class)`. Log at `INFO` for lifecycle events, `DEBUG` for cache hits, `WARN` for parse failures.
- **String building:** Use `StringBuilder` for large Markdown outputs.
- **No external JSON libraries** for schema building — use `SchemaUtil` and `Map<String, Object>`.
- **Null safety:** Prefer `Optional` for accessor lookups in `JfrItemUtils`; tools should handle missing events gracefully.

---

## Runtime Architecture

1. `JmcMcpServer.main` starts the server.
2. A single `JfrRecordingCache` and `JfrAnalysisService` are instantiated.
3. All tool instances are created and registered with the `McpSyncServer`.
4. `StdioServerTransportProvider` blocks on stdin, dispatching JSON-RPC `tools/call` requests to the appropriate tool handler.
5. The handler loads the JFR recording (cached), optionally filters by time range, runs analysis, and returns Markdown text content.

### Caching Layers

1. **Recording cache (`JfrRecordingCache`):** Maps absolute file paths to parsed `IItemCollection` objects. Thread-safe via `ConcurrentHashMap`.
2. **Result cache (`JfrAnalysisService`):** Maps `(filePath, toolName, args)` to the resulting Markdown string. LRU eviction (max 50 entries) via synchronized `LinkedHashMap`.

### Important Runtime Constraint

**Never write to stdout.** stdout is owned by the MCP transport. All diagnostic output must go through SLF4J → logback → stderr. The `logback.xml` explicitly targets `System.err`.

---

## Security Considerations

- **Sensitive data in JFR recordings:** JFR files can contain exception messages, SQL queries, system properties, environment variables, and file paths. Treat recordings as potentially sensitive.
- **PII masking is NOT currently implemented.** `PLAN-ENTERPRISE.md` identifies this as a critical future requirement. If you are adding tools that emit exception messages, system properties, or command-line arguments, consider whether a sanitization pass is appropriate.
- **Live JVM access:** `LiveRecordingTool` connects to remote JVMs via JMX. It requires the target JVM to expose JFR MBeans. There is no authentication layer inside this tool; it relies on the JMX URL configuration.
- **File system access:** Tools read arbitrary file paths provided by the MCP client. There is no sandboxing — the server runs with the privileges of the launching process.

---

## Dependency Notes

- **JMC libraries** (`org.openjdk.jmc`) are not modularized in the same way as typical Maven artifacts. They are pulled directly from Maven Central.
- **MCP SDK** uses `tools.jackson` (Jackson 3) rather than `com.fasterxml.jackson` (Jackson 2). Do not mix the two Jackson namespaces.
- The shade plugin merges everything into a single executable JAR with the correct `Main-Class` manifest.

---

## Useful References

- `README.md` — Full tool catalog with descriptions, arguments, and client configuration examples (Claude Desktop, VS Code).
- `PLAN.md` — Detailed implementation plan for Phase 1 diagnostic tools (`stack_trace_search`, `request_waterfall`, `correlate`, `quick_analysis`, `diff_stack_traces`).
- `PLAN-ENTERPRISE.md` — Strategic roadmap for cloud-native features, PII sanitization, Kubernetes integration, and advanced heuristics.
