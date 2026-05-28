# JMC MCP Server — Agent Guide

This document contains project-specific context for AI coding agents working on `jmc-mcp`.

---

## Project Overview

`jmc-mcp` is a [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server that exposes **69 tools** for analyzing Java Flight Recorder (JFR) recordings. It uses the Java Mission Control (JMC) 9.1.1 core libraries to parse recordings and return structured Markdown reports to LLM agents.

The server communicates over **stdio** (stdin/stdout) using MCP's JSON-RPC protocol. stdout is reserved exclusively for MCP messages; all logging goes to stderr via logback.

### Key Facts

- **Language:** Java 25
- **Build Tool:** Maven 3.9+
- **Main Class:** `io.github.deplague.jmcmcp.JmcMcpServer` (`@QuarkusMain`)
- **Artifact:** `target/jmc-mcp-1.0.0-SNAPSHOT.jar` (fat JAR via maven-shade-plugin) and `target/quarkus-run.jar`
- **Transport:** StdioServerTransportProvider with Jackson 3 JSON mapper
- **MCP SDK Version:** 1.1.2 (`io.modelcontextprotocol.sdk:mcp`)
- **Runtime Framework:** Quarkus 3.36.0 with CDI (`quarkus-arc`)
- **Lombok:** 1.18.46 (use `@Slf4j`, `@Getter`, `@RequiredArgsConstructor`, `@Value`, `@Builder` everywhere)
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
- `maven-compiler-plugin` 3.15.0 (release 25)
- `maven-surefire-plugin` 3.5.4
- `maven-shade-plugin` 3.6.0 (packages all dependencies into the executable JAR)
- `quarkus-maven-plugin` 3.36.0 (CDI build and Quarkus runner generation)

---

## Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| MCP SDK | 1.1.2 | MCP server framework, JSON-RPC, stdio transport |
| JMC (common, flightrecorder, rules, rules.jdk) | 9.1.1 | JFR parsing, rules engine, aggregations |
| Jackson | 2.18.3 | JSON serialization for MCP messages |
| SLF4J | 2.0.17 | Logging facade |
| Logback | 1.5.18 | Logging implementation (stderr only) |
| Quarkus | 3.36.0 | CDI container, lifecycle, REST (future) |
| Lombok | 1.18.46 | Boilerplate reduction (`@Slf4j`, `@Getter`, `@RequiredArgsConstructor`) |
| JUnit Jupiter | 5.12.1 | Unit testing |
| AssertJ | 3.27.3 | Fluent assertions |

---

## Code Organization

```
src/main/java/io/github/deplague/jmcmcp/
  JmcMcpServer.java          # Entry point — bootstraps MCP server and registers all tools
  jfr/
    JfrAnalysisService.java  # Legacy service (deprecated). Being phased out.
    JfrRecordingCache.java   # In-memory ConcurrentHashMap cache of parsed IItemCollection
    JfrItemUtils.java        # Reflection-free attribute extraction, quantity aggregation
                             # (sum, avg, min, max, percentile), stack trace formatting
  domain/                    # Hexagonal Core: Pure Java, no framework dependencies
  application/               # Hexagonal Orchestration: Ports and Application Services
  adapters/                  # Hexagonal Adapters: MCP Tools and Infrastructure
  tools/
    SchemaUtil.java          # Helpers for building McpSchema.JsonSchema and parsing args
    *Tool.java               # Legacy wrappers delegating to adapters.mcp

src/test/java/io/github/deplague/jmcmcp/
  tools/                    # Unit & integration tests for individual tools
  jfr/                      # Tests for JfrRecordingCache and JfrAnalysisService
  JmcApiDiscoverer.java     # Utility for discovering JMC API surfaces

src/main/resources/
  logback.xml               # Logging config: root WARN to stderr; mcp/jmc suppressed to WARN
```

### Package Structure

- **`io.github.deplague.jmcmcp`** — Server bootstrap (`@QuarkusMain`).
- **`io.github.deplague.jmcmcp.domain`** — Pure domain layer. No framework annotations. Contains exceptions, records, and domain services.
- **`io.github.deplague.jmcmcp.application`** — Application layer. Orchestrates domain services, defines ports, handles caching of Records. CDI-friendly.
- **`io.github.deplague.jmcmcp.adapters.mcp`** — MCP driving adapters. Each adapter implements `McpTool`, defines schema, and formats domain results into Markdown.
- **`io.github.deplague.jmcmcp.adapters.infrastructure`** — Driven adapters (e.g., `JfrProviderImpl`). Concrete implementations of application ports.
- **`io.github.deplague.jmcmcp.jfr`** — JFR loading, caching, filtering, and low-level item utilities.
- **`io.github.deplague.jmcmcp.tools`** — Legacy wrappers (maintained for test compatibility).

---

## Localized Agent Guides

Each major package contains its own `AGENTS.md` file with specific instructions and constraints for that layer:

- [`src/main/java/io/github/deplague/jmcmcp/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/AGENTS.md) — Bootstrapping & registration.
- [`src/main/java/io/github/deplague/jmcmcp/domain/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/domain/AGENTS.md) — Pure domain logic & analysis.
- [`src/main/java/io/github/deplague/jmcmcp/application/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/application/AGENTS.md) — Use case orchestration & ports.
- [`src/main/java/io/github/deplague/jmcmcp/adapters/mcp/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/adapters/mcp/AGENTS.md) — MCP tool adapters & formatting.
- [`src/main/java/io/github/deplague/jmcmcp/adapters/infrastructure/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/adapters/infrastructure/AGENTS.md) — Port implementations.
- [`src/main/java/io/github/deplague/jmcmcp/jfr/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/jfr/AGENTS.md) — JFR utilities & caching.
- [`src/main/java/io/github/deplague/jmcmcp/async/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/async/AGENTS.md) — Async job management.
- [`src/main/java/io/github/deplague/jmcmcp/security/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/security/AGENTS.md) — Access control.
- [`src/main/java/io/github/deplague/jmcmcp/tools/AGENTS.md`](src/main/java/io/github/deplague/jmcmcp/tools/AGENTS.md) — Legacy tools & SchemaUtil.

---

## How to Add a New Tool (Hexagonal Pattern)

**All new tools must follow the Hexagonal Adapter pattern:**

1. **Create the Domain Service** in `domain/service/<Name>Service.java`.
   - Pure Java, no framework annotations, no Markdown.
   - Accepts `IItemCollection` and returns a domain Record.
2. **Create Domain Records** in `domain/model/<Name>Result.java` (and sub-records).
3. **Create an Application Service** in `application/service/<Name>ApplicationService.java`.
   - Orchestrates `JfrProvider` + domain service.
   - Add it to `DomainConfig.java` as a `@Produces` method if it needs special instantiation.
4. **Create the MCP Adapter** in `adapters/mcp/<Name>Tool.java`.
   - Implement `McpTool`.
   - Use `@ApplicationScoped` + `@Inject` for constructor injection.
   - `spec()` defines schema and call handler.
   - Call handler delegates to the application service and formats Markdown.
5. **Add a unit test** in `src/test/java/io/github/deplague/jmcmcp/adapters/mcp/<Name>ToolTest.java`.
6. **No manual registration** in `JmcMcpServer.java` — CDI discovers `McpTool` beans automatically.

### Tool Conventions

- **Schema:** Use `SchemaUtil.objectSchema(SchemaUtil.props(...), SchemaUtil.required(...))`.
- **Common properties:** Most JFR analysis tools accept `jfr_file_path`, `start_time`, and `end_time` (ISO-8601). Reuse `SchemaUtil.commonJfrProps()`.
- **Optional args:** Use `SchemaUtil.getStringOrDefault`, `getIntOrDefault`, etc.
- **Caching:** Application services handle results caching (often delegating to specialized caches like `CallTreeCache`).
- **Error handling:** Catch exceptions in the call handler and return `CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build()`.
- **Output format:** Return Markdown. Start with `# Title`. Use tables where appropriate. End with an `<agent_hint>` block.
- **Stack traces:** Use `JfrItemUtils.formatStackTrace(obj, maxFrames)` for truncated traces or `JfrItemUtils.formatFullStackTrace(obj)` for complete traces.

### Agent Hints

Append an `<agent_hint>` block at the end of output to guide the LLM. Example:

```markdown
<agent_hint>Top hot method is `SomeClass.someMethod()`. Consider `thread_cpu` to see thread impacts, or `smart_correlate` to find contention causes.</agent_hint>
```

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
- **Lombok (MANDATORY):** Use Lombok annotations everywhere to reduce boilerplate:
  - `@Slf4j` on every class that logs (replaces `LoggerFactory.getLogger(...)`).
  - `@RequiredArgsConstructor` on CDI beans and domain services with final fields.
  - `@Getter` / `@Value` on domain records if additional accessors are needed.
  - `@Builder` for complex configuration objects.
  - **Domain layer rule:** Domain classes (`domain.*`) must remain free of Quarkus/Jakarta annotations, but Lombok annotations are **allowed** because they are compile-only and do not couple to the runtime framework.
- **Logging:** Use `@Slf4j` + `log.info(...)`, `log.debug(...)`, `log.warn(...)`. Never use `System.out`.
- **String building:** Use `StringBuilder` for large Markdown outputs.
- **No external JSON libraries** for schema building — use `SchemaUtil` and `Map<String, Object>`.
- **Null safety:** Prefer `Optional` for accessor lookups in `JfrItemUtils`; tools should handle missing events gracefully.

---

## Runtime Architecture

1. `JmcMcpServer` is a `@QuarkusMain` implementing `QuarkusApplication`.
2. Quarkus CDI bootstraps the container and injects all `@ApplicationScoped` beans.
3. `JmcMcpServer.run()` discovers refactored tools via `Instance<McpTool>` and manually wires legacy tools.
4. All tools are registered with the `McpSyncServer`.
5. `StdioServerTransportProvider` blocks on stdin, dispatching JSON-RPC `tools/call` requests to the appropriate tool handler.
6. Refactored tool handlers delegate to application services, which orchestrate domain services and infrastructure adapters.
7. The handler returns Markdown text content.

### Caching Layers

1. **Recording cache (`JfrRecordingCache`):** Maps absolute file paths to parsed `IItemCollection` objects. Thread-safe via `ConcurrentHashMap`.
2. **Legacy result cache (`JfrAnalysisService`):** Maps `(filePath, toolName, args)` to the resulting Markdown string. LRU eviction via synchronized `LinkedHashMap`.
3. **Future:** Application services will cache domain Records before formatting.

### Important Runtime Constraints

- **Never write to stdout.** stdout is owned by the MCP transport. All diagnostic output must go through SLF4J → logback → stderr.
- **Quarkus logging** (`application.properties`) is configured to write to `stderr` only.
- **Domain purity:** No class under `domain.*` may import `io.modelcontextprotocol.*`, `jakarta.*`, `io.quarkus.*`, or HTTP/web framework classes. Lombok is the only allowed compile-time dependency in the domain layer.

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
- `PLAN.md` — Detailed implementation plan for Phase 1 diagnostic tools (`smart_stack_trace_search`, `smart_request_waterfall`, `smart_correlate`, `smart_quick_analysis`, `smart_diff_stack_traces`).
- `PLAN-ENTERPRISE.md` — Strategic roadmap for cloud-native features, PII sanitization, Kubernetes integration, and advanced heuristics.

🛠️ MCP Tool Priority Rules (FASTER — Use These First)

Two MCP servers are available that are SIGNIFICANTLY faster than shell commands or the context engine. They use IntelliJ's live index (O(1) symbol lookup, instant build feedback, direct file access). ALWAYS prefer them.

**Available Servers:**
- **intellij-mcpserver:** File I/O, search, build, run, debug.
- **mcp-steroid:** IntelliJ API execution, PSI analysis, advanced operations.

### 📂 File Discovery & Reading
*Avoid: codebase-retrieval, view, shell find/ls/tree/dir*

| Tool | Usage Example | Description |
| :--- | :--- | :--- |
| `find_files_by_name_keyword` | `("UserService")` | Fastest discovery (O(1) indexed, name only). |
| `find_files_by_glob` | `("**/*.java")` | Recursive filesystem-based glob search. |
| `search_file` | `("**/*.java", paths=["src/"])` | IntelliJ engine search with path filters and excludes (`!`). |
| `get_file_text_by_path` | `(pathInProject, truncateMode="NONE")` | Read full or truncated file content. |
| `read_file` | `(file_path, mode="lines")` | Precise reads: slices, lines, columns, offsets, or indentation. |
| `list_directory_tree` | `(directoryPath, maxDepth=2)` | Pseudo-graphic tree representation of directory contents. |
| `get_all_open_file_paths` | `()` | Returns paths of all currently open files in the IDE. |
| `open_file_in_editor` | `(filePath)` | Focuses and opens a specific file in the IDE editor. |

### 🗂️ Project Structure & VCS

| Tool | Description |
| :--- | :--- |
| `get_project_modules` | List all modules and their types. |
| `get_project_dependencies` | List all library dependencies by name. |
| `get_repositories` | List all VCS (Git) roots in the project. |

### 🔍 Code Search
*Avoid: codebase-retrieval, shell grep/ripgrep*

| Tool | Usage Example | Description |
| :--- | :--- | :--- |
| `search_symbol` | `("UserService", include_external=true)` | Find class/method/field by name (supports SDK symbols). |
| `search_text` | `("getAuthToken")` | Fast text substring search with snippets and coordinates. |
| `search_in_files_by_text` | `("token", fileMask="*.java")` | Scoped text search within specific file masks. |
| `search_regex` | `("public\\s+\\w+\\s+get[A-Z]")` | Regex search with snippets and coordinates. |
| `search_in_files_by_regex` | `("\\bgetUser\\b")` | Scoped regex search within project files. |
| `get_symbol_info` | `(filePath, line, column)` | Quick Documentation: declaration, signature, and docs. |

### ✏️ File Editing

| Tool | Usage Example | Description |
| :--- | :--- | :--- |
| `replace_text_in_file` | `(path, oldText, newText)` | Targeted text replacement (auto-saves). Preferred for single edits. |
| `create_new_file` | `(path, text)` | Creates new file and parent directories. |
| `rename_refactoring` | `(path, symbolName, newName)` | Semantic rename: updates ALL references across the project. |
| `reformat_file` | `(path)` | Applies IDE code formatting rules to the file. |

### 🏗️ Build & Compile
*Avoid: launch-process with gradle / mvn commands*

| Tool | Usage Example | Description |
| :--- | :--- | :--- |
| `build_project` | `()` | Compiles all modules and returns errors immediately. |
| `build_project` | `(filesToRebuild=["Path.java"])` | Incremental build: compiles specific files only (faster). |
| `build_project` | `(rebuild=true)` | Performs a full project rebuild. |
| `get_file_problems` | `(filePath)` | Runs IDE inspections (errors/warnings) on a specific file. |

### 🧪 Test Execution
*Avoid: launch-process with gradle test / browser URL launch*

| Tool | Usage Example | Description |
| :--- | :--- | :--- |
| `get_run_configurations` | `()` | Lists existing run configurations (JUnit, Maven, etc.). |
| `get_run_configurations` | `(filePath="Test.java")` | Discovers runnable entry points (gutter icons) in a file. |
| `execute_run_configuration` | `(configurationName="MyTest")` | Runs a named configuration. |
| `execute_run_configuration` | `(filePath="...", line=42)` | Runs a specific test from code context. |

*Note: The browser-based TestNG launcher is a legacy fallback for container groups.*

### 💻 Terminal Commands

| Tool | Usage Example | Description |
| :--- | :--- | :--- |
| `execute_terminal_command` | `("git status")` | Runs command in integrated terminal. Supports `executeInShell`. |

*⚠️ Prefer `build_project` / `execute_run_configuration` over terminal when possible.*

### 🔍 Custom Inspections (InspectionKTS)

| Tool | Description |
| :--- | :--- |
| `generate_inspection_kts_api` | Get API documentation for Java/Kotlin inspections. |
| `generate_psi_tree` | Visualize the PSI tree of a code snippet (essential for scripts). |
| `run_inspection_kts` | Compile and run a custom inspection script against project files. |

---

## 🔬 MCP-Steroid — Exclusive Capabilities

Use `mcp-steroid` for operations that `intellij-mcpserver` cannot perform. Its core power is `steroid_execute_code`: arbitrary Kotlin code running inside IntelliJ's JVM.

**Mandatory Pre-flight:** ALWAYS fetch the skill guide before the first `steroid_execute_code` call:
- `steroid_fetch_resource("mcp-steroid://prompt/skill")` (General)
- `steroid_fetch_resource("mcp-steroid://prompt/test-skill")` (Build/Test)
- `steroid_fetch_resource("mcp-steroid://prompt/debugger-skill")` (Debugging)

### 🔎 PSI Search & Analysis

| Task | Approach (Kotlin snippet via `steroid_execute_code`) |
| :--- | :--- |
| **Find Subclasses** | `ClassInheritorsSearch.search(psiClass, scope, true).toList()` |
| **Find Call Sites** | `ReferencesSearch.search(method, scope).toList()` (Find Usages) |
| **Analyze PSI Tree** | `PsiManager.getInstance(project).findFile(virtualFile)` |
| **Indexed Discovery** | `FilenameIndex.getAllFilesByExt(project, "java", scope)` |

### ⚡ Action Discovery & Invocation
*Discover and invoke ANY IDE action (Quick-fix, Intention, Refactoring, Gutter).*

| Step | Tool | Description |
| :--- | :--- | :--- |
| **1. Discover** | `steroid_action_discovery` | Returns action IDs, intention names, and gutter actions at caret. |
| **2. Invoke** | `steroid_execute_code` | `ActionManager.getInstance().getAction(id).actionPerformed(...)` |

### 📸 IDE Screenshots & GUI Automation

| Tool | Description |
| :--- | :--- |
| `steroid_take_screenshot` | Captures PNG + component tree + metadata. Use for debugging modals. |
| `steroid_input` | Sends sequence: `stick`, `press`, `type`, `click`, `delay`. |
| `steroid_list_windows` | Checks `modalDialogShowing`, `indexingInProgress`, `projectInitialized`. |

### 🪟 Multi-Window & Project Management

| Tool | Description |
| :--- | :--- |
| `steroid_list_projects` | Lists open project names for use in other steroid tools. |
| `steroid_open_project` | Initiates project opening (asynchronous). |

### 🔄 Complex Refactoring (IntelliJ API)
*Use `steroid_execute_code` for operations beyond simple renames.*

| Operation | Requirement |
| :--- | :--- |
| **Move / Extract / Pull** | Use IntelliJ Refactoring API (e.g., `MoveClassesOrPackagesProcessor`). |
| **PSI Mutation** | Always wrap reads in `readAction { }` and writes in `writeAction { }`. |

### ✏️ Atomic Multi-File Edits

| Tool | Description |
| :--- | :--- |
| `steroid_apply_patch` | Applies N `old_string → new_string` substitutions atomically. Rejects ambiguous matches. |

---

## 📋 Decision Matrices

### mcp-steroid Decision Matrix

| Task | Approach |
| :--- | :--- |
| Find all subclasses of a class | `execute_code` (ClassInheritorsSearch) |
| Find all call sites of a method | `execute_code` (ReferencesSearch) |
| Inspect PSI / AST of a file | `execute_code` (PsiManager + PsiFile) |
| Batch file discovery (large project) | `execute_code` (FilenameIndex) |
| Discover quick-fixes at a caret | `steroid_action_discovery` |
| Invoke a quick-fix / refactoring | `action_discovery` → `execute_code` |
| See what dialog appeared in IDE | `steroid_take_screenshot` |
| Dismiss a blocking dialog | `take_screenshot` → `steroid_input` |
| Run a gutter Run/Debug action | `action_discovery` → `execute_code` |
| Open project and wait for index | `steroid_open_project` + `steroid_list_windows` |
| Extract method / inline / move class | `execute_code` (IntelliJ Refactoring API) |
| Check if IDE is ready / indexing | `steroid_list_windows` |
| Atomic batch edit across N files | `steroid_apply_patch` |

### 🐛 Debugging (intellij-mcpserver)

| Tool Category | Tools | Description |
| :--- | :--- | :--- |
| **Lifecycle** | `xdebug_get_debugger_status`, `xdebug_start_debugger_session`, `xdebug_control_session` | Start, stop, step, resume, and wait for pause. |
| **Breakpoints** | `xdebug_set_breakpoint`, `xdebug_list_breakpoints`, `xdebug_remove_breakpoint` | Line, conditional, and tracepoints. |
| **Inspection** | `xdebug_get_stack`, `xdebug_get_threads`, `xdebug_get_frame_values`, `xdebug_get_value_by_path`, `xdebug_evaluate_expression` | Inspect call stack, locals, fields, and evaluate code. |
| **Mutation** | `xdebug_set_variable` | Change variable values during a paused session. |
| **Navigation** | `xdebug_run_to_line` | Resume execution until a target line is reached. |

### 📋 Quick Decision Matrix

| Task | Recommended Tool |
| :--- | :--- |
| Find file by name (keyword) | `find_files_by_name_keyword` |
| Find files by glob (filesystem) | `find_files_by_glob` |
| Find files by glob (IDE engine) | `search_file` |
| Read a file | `get_file_text_by_path` / `read_file` |
| List directory contents | `list_directory_tree` |
| Get currently open editors | `get_all_open_file_paths` |
| Search text in code | `search_text` / `search_in_files_by_text` |
| Search with regex | `search_regex` / `search_in_files_by_regex` |
| Find class / method / field | `search_symbol` |
| Edit a file (targeted) | `replace_text_in_file` |
| Rename symbol everywhere | `rename_refactoring` |
| Compile / check errors | `build_project` / `get_file_problems` |
| Run test or run configuration | `execute_run_configuration` |
| PSI / advanced IntelliJ API | `steroid_execute_code` |
| Discover/Invoke IDE actions | `steroid_action_discovery` → `execute_code` |
| GUI / Modal handling | `steroid_take_screenshot` → `steroid_input` |
| Atomic multi-file edit | `steroid_apply_patch` |
