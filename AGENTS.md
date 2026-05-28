# JMC MCP Server — Agent Guide

This document contains project-specific context for AI coding agents working on `jmc-mcp`.

---

## Project Overview

`jmc-mcp` is a [Model Context Protocol (MCP)](https://modelcontextprotocol.io) server that exposes **57 tools** for analyzing Java Flight Recorder (JFR) recordings. It uses the Java Mission Control (JMC) 9.1.1 core libraries to parse recordings and return structured Markdown reports to LLM agents.

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

- **`io.github.deplague.jmcmcp`** — Server bootstrap (`@QuarkusMain`).
- **`io.github.deplague.jmcmcp.domain`** — Pure domain layer. No framework annotations. Contains exceptions, records, and domain services.
- **`io.github.deplague.jmcmcp.application`** — Application layer. Orchestrates domain services, defines ports, handles caching of Records. CDI-friendly.
- **`io.github.deplague.jmcmcp.adapters.mcp`** — MCP driving adapters. Each adapter implements `McpTool`, defines schema, and formats domain results into Markdown.
- **`io.github.deplague.jmcmcp.adapters.infrastructure`** — Driven adapters (e.g., `JfrProviderImpl`). Concrete implementations of application ports.
- **`io.github.deplague.jmcmcp.adapters.http`** — Reserved for future REST endpoints.
- **`io.github.deplague.jmcmcp.jfr`** — JFR loading, caching, filtering, and low-level item utilities. No MCP-specific code here.
- **`io.github.deplague.jmcmcp.tools`** — Legacy MCP tools not yet refactored (Phase 2 will migrate them to `adapters.mcp`).

---

## How to Add a New Tool (Post-Refactor Pattern)

**For Phase 1+ tools, follow the Hexagonal Adapter pattern:**

1. **Create the Domain Service** in `domain/service/<Name>Service.java`.
   - Pure Java, no framework annotations, no Markdown.
   - Accepts `IItemCollection` and returns a domain Record.
2. **Create Domain Records** in `domain/model/<Name>Result.java` (and sub-records).
3. **Create an Application Service** in `application/service/<Name>ApplicationService.java`.
   - Orchestrates `JfrProvider` + domain service.
   - Add it to `DomainConfig.java` as a `@Produces` method if needed.
4. **Create the MCP Adapter** in `adapters/mcp/<Name>Tool.java`.
   - Implement `McpTool`.
   - Use `@ApplicationScoped` + `@Inject` for constructor injection.
   - `spec()` defines schema and call handler.
   - Call handler delegates to the application service and formats Markdown.
5. **Add a unit test** in `src/test/java/io/github/deplague/jmcmcp/adapters/mcp/<Name>ToolTest.java`.
   - Manually wire `JfrProviderImpl` + domain service + application service.
6. **No manual registration** in `JmcMcpServer.java` — CDI discovers `McpTool` beans automatically.

**Legacy tools** (pre-refactor) remain in `tools/` and are wired manually in `JmcMcpServer.java` until Phase 2 migration.

### Tool Conventions

- **Schema:** Use `SchemaUtil.objectSchema(SchemaUtil.props(...), SchemaUtil.required(...))`.
- **Common properties:** Most JFR analysis tools accept `jfr_file_path`, `start_time`, and `end_time` (ISO-8601). Reuse `SchemaUtil.commonJfrProps()`.
- **Optional args:** Use `SchemaUtil.getStringOrDefault`, `getIntOrDefault`, etc. The MCP client may send integers as strings.
- **Caching:** Application services may cache domain Records (not Markdown strings). Legacy tools still use `JfrAnalysisService` string caching.
- **Time filtering:** Application services call `jfrProvider.filterByTimeRange(events, startTimeStr, endTimeStr)` before analysis.
- **Error handling:** Catch exceptions in the call handler and return `CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build()`.
- **Output format:** Return Markdown. Start with `# Title`. Use tables where appropriate. End with an `<agent_hint>` block suggesting related tools.
- **Stack traces:** Use `JfrItemUtils.formatStackTrace(obj, maxFrames)` for truncated traces or `JfrItemUtils.formatFullStackTrace(obj)` for complete traces.
- **Quantity formatting:** Use `JfrAnalysisService.display(quantity)` for human-readable units.

### Agent Hints

Many tools append an `<agent_hint>` block at the end of their output to guide the LLM to the next most useful tool. Example from `HotMethodsTool`:

```markdown
<agent_hint>Top hot method is `SomeClass.someMethod()`. Consider `thread_cpu` to see which threads consume the most CPU, `smart_stack_trace_search` with `class_pattern` to find all events involving this class, or `smart_correlate` to see if this method is associated with lock contention or I/O.</agent_hint>
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

Two MCP servers are available that are SIGNIFICANTLY faster than shell commands
or the context engine. They use IntelliJ's live index (O(1) symbol lookup,
instant build feedback, direct file access). ALWAYS prefer them.

Available servers:
• intellij-mcpserver  — file I/O, search, build, run, debug
• mcp-steroid         — IntelliJ API execution, PSI analysis, advanced ops

═══════════════════════════════════════════════
📂 FILE DISCOVERY & READING
═══════════════════════════════════════════════

❌ AVOID: codebase-retrieval, view, shell find/ls/tree/dir
✅ USE instead:

• Find file by name keyword (fastest — O(1) indexed, name only):
find_files_by_name_keyword("UserService")

• Find files by glob pattern (recursive, filesystem-based):
find_files_by_glob("**/*.java")
find_files_by_glob("src/**/Service*.java")

• Find files by glob using IntelliJ's search engine (supports path filters and ! excludes):
search_file("UserService")                           ← treated as **/UserService
search_file("**/*.java", paths=["src/", "!**/test/**"])

• Read file content (project-relative path):
get_file_text_by_path — full file or truncated (truncateMode: START/MIDDLE/END/NONE)
read_file             — slice, lines, line_columns, offsets, or indentation mode

• List directory tree (replaces ls/tree/dir):
list_directory_tree(directoryPath, maxDepth)

• Get currently open files in the editor:
get_all_open_file_paths()  ← returns active editor + all open editor paths

• Open a specific file in the IDE editor:
open_file_in_editor(filePath)

═══════════════════════════════════════════════
🗂️ PROJECT STRUCTURE & VCS
═══════════════════════════════════════════════

• List all project modules (with module types):
get_project_modules()

• List all project library dependencies (names):
get_project_dependencies()

• List all VCS roots in the project:
get_repositories()

═══════════════════════════════════════════════
🔍 CODE SEARCH
═══════════════════════════════════════════════

❌ AVOID: codebase-retrieval, shell grep/ripgrep
✅ USE instead:

• Find class / method / field by name:
search_symbol("UserService")
search_symbol("getUserById", include_external=true)  ← for SDK symbols

• Search by text substring (with snippets + coordinates):
search_text("getAuthToken")
search_in_files_by_text("getAuthToken", fileMask="*.java")

• Search by regex (with snippets + coordinates):
search_regex("public\\s+\\w+\\s+get[A-Z]")
search_in_files_by_regex("\\bgetUser\\b", fileMask="*.java")

• Get symbol declaration / docs at a file position:
get_symbol_info(filePath, line, column)

═══════════════════════════════════════════════
✏️ FILE EDITING
═══════════════════════════════════════════════

• Targeted text replacement (preferred, auto-saves):
replace_text_in_file(pathInProject, oldText, newText)

• Create new file (auto-creates parent directories):
create_new_file(pathInProject, text)

• Rename symbol across entire project (updates ALL references):
rename_refactoring(pathInProject, symbolName, newName)

• Reformat file after edits:
reformat_file(path)

═══════════════════════════════════════════════
🏗️ BUILD & COMPILE
═══════════════════════════════════════════════

❌ AVOID: launch-process with gradle / mvn commands
✅ USE instead:

• Compile all modules and get errors immediately:
build_project()

• Compile specific files only (faster):
build_project(filesToRebuild=["src/com/imprivata/Foo.java"])

• Full rebuild:
build_project(rebuild=true)

• Analyze problems in a single file (inspections/errors/warnings):
get_file_problems(filePath)

═══════════════════════════════════════════════
🧪 TEST EXECUTION
═══════════════════════════════════════════════

❌ AVOID: launch-process with gradle test / browser URL launch (for run configs)
✅ USE instead:

• List all run configurations in the project:
get_run_configurations()

• Find runnable entry points (test methods, main) in a file:
get_run_configurations(filePath="src/.../FooTest.java")

• Run a named configuration or a specific test by file+line:
execute_run_configuration(configurationName="MyTest")
execute_run_configuration(filePath="...", line=42, waitForExit=true)

NOTE: The browser-based TestNG launcher (http://localhost:8080/sso/testng)
is still needed for container/unit groups when no run config exists.

═══════════════════════════════════════════════
💻 TERMINAL COMMANDS
═══════════════════════════════════════════════

Run a shell command in the IDE's integrated terminal:
execute_terminal_command("git status")
execute_terminal_command("./gradlew dependencies", timeout=60000, executeInShell=true)

Options:
executeInShell            — run in user's default shell (bash/zsh), preserves env
reuseExistingTerminalWindow — avoid opening multiple terminal tabs
maxLinesCount / truncateMode — control output length (START/MIDDLE/END/NONE)

⚠️ Prefer build_project / execute_run_configuration over terminal when possible.
Use terminal for tasks not covered by dedicated tools (e.g. git commands, scripts).

═══════════════════════════════════════════════
🔍 CUSTOM INSPECTIONS (InspectionKTS)
═══════════════════════════════════════════════

Write and run custom code inspections directly inside IntelliJ without plugins:

• Get InspectionKTS API documentation for Java or Kotlin:
generate_inspection_kts_api(language="Java")
generate_inspection_kts_api(language="Kotlin")

• Get inspection.kts template examples to guide script writing:
generate_inspection_kts_examples(language="Java", includeAdditionalExamples=true)

• Visualize the PSI tree of a code snippet (essential for writing inspections):
generate_psi_tree(code="public void foo() {}", language="Java")

• Compile and run an inspection script against a project file:
run_inspection_kts(inspectionKtsCode="...", contextPath="src/.../Foo.java")

Typical workflow:
generate_inspection_kts_api → generate_psi_tree → write script → run_inspection_kts


═══════════════════════════════════════════════
🔬 MCP-STEROID — EXCLUSIVE CAPABILITIES
═══════════════════════════════════════════════

Use mcp-steroid for operations that intellij-mcpserver CANNOT do.
Its core power is steroid_execute_code: arbitrary Kotlin code running
inside IntelliJ's own JVM with full API access.

ALWAYS fetch the skill guide before the first steroid_execute_code call:
steroid_fetch_resource("mcp-steroid://prompt/skill")          ← any IDE task
steroid_fetch_resource("mcp-steroid://prompt/test-skill")     ← build/test
steroid_fetch_resource("mcp-steroid://prompt/debugger-skill") ← debugging

Additional resource URIs (fetch before using these features):
steroid_fetch_resource("mcp-steroid://skill/apply-patch-tool-description") ← steroid_apply_patch usage
steroid_fetch_resource("mcp-steroid://ide/apply-patch")                    ← apply-patch DSL reference
steroid_fetch_resource("mcp-steroid://skill/execute-code-gradle")          ← Gradle test execution

steroid_execute_code options:
dialog_killer: true   ← force-enable the EDT dialog killer for this call (dismisses blocking DialogWrapper windows)
dialog_killer: false  ← force-disable (default: registry setting)
Note: DialogKiller replaces the old ModalityStateMonitor; Maven/Gradle test runs no longer get
cancelled mid-flight; SDK-lookup banners are no longer incorrectly flagged as blocking modals.

───────────────────────────────────────────────
🔎 PSI SEARCH & ANALYSIS (no intellij-mcpserver equivalent)
───────────────────────────────────────────────

intellij-mcpserver search_symbol finds by name only.
mcp-steroid can answer semantic questions: who calls this? who extends this?

• Find all subclasses / implementors of a class or interface:

     val scope = GlobalSearchScope.projectScope(project)
     val cls = readAction {
         JavaPsiFacade.getInstance(project)
             .findClass("com.imprivata.auth.AuthService", scope)
     }
     val subs = readAction {
         ClassInheritorsSearch.search(cls!!, scope, true).toList()
     }
     println(subs.map { it.qualifiedName })

• Find all call sites of a method (find usages):

     val method = readAction { cls!!.findMethodsByName("authenticate", false).first() }
     val usages = readAction {
         ReferencesSearch.search(method, scope).toList()
     }
     usages.forEach { println("${it.element.containingFile.name}:${it.element.textOffset}") }

• Analyse PSI tree of a file (inspect AST structure):

     val psiFile = readAction {
         PsiManager.getInstance(project).findFile(
             LocalFileSystem.getInstance().findFileByPath("/abs/path/Foo.java")!!
         )
     }
     readAction { println(psiFile?.text?.take(500)) }

• Batch file discovery via FilenameIndex (faster than glob for large trees):

     val files = readAction {
         FilenameIndex.getAllFilesByExt(
             project, "java", GlobalSearchScope.projectScope(project)
         )
     }
     println(files.map { it.path })

───────────────────────────────────────────────
⚡ ACTION DISCOVERY & INVOCATION AT A CARET
───────────────────────────────────────────────

intellij-mcpserver has rename_refactoring only.
mcp-steroid can discover and invoke ANY IDE action available at a caret position.

STEP 1 — Discover available actions at a file location:

steroid_action_discovery(
project_name = "eam-server",
file_path    = "dev/server/src1.8/com/imprivata/auth/AuthService.java",
caret_offset = 1423,     ← character offset in the file
task_id      = "my-task"
)

Returns:
• Quick-fix action IDs   (e.g. "Fix access modifier", "Add @Override")
• Intention action names  (e.g. "Extract Method", "Introduce Variable")
• Gutter icon actions     (Run, Debug — with exact Action IDs)
• Refactoring actions     (e.g. "Inline Method", "Pull Members Up")

STEP 2 — Invoke the action by ID via steroid_execute_code:

     import com.intellij.openapi.actionSystem.ActionManager
     import com.intellij.openapi.actionSystem.AnActionEvent
     val action = ActionManager.getInstance().getAction("ExtractMethod")
     // open file in editor first, then invoke
     action.actionPerformed(AnActionEvent.createFromDataContext(...))

Common use cases:
• Apply a quick-fix without knowing the exact API in advance
• Trigger "Extract Method" / "Introduce Variable" / "Pull Members Up"
• Run or debug a specific test via gutter action ID
• Invoke any action from Settings → Keymap by its ID

───────────────────────────────────────────────
📸 IDE SCREENSHOTS & GUI AUTOMATION
───────────────────────────────────────────────

intellij-mcpserver has no GUI visibility. mcp-steroid can see and drive the IDE UI.

• Take a screenshot of the IDE window:

steroid_take_screenshot(
project_name = "eam-server",
task_id      = "my-task",
reason       = "check dialog that appeared after refactoring"
)

Returns: PNG image + component tree (screenshot-tree.md) + metadata (screenshot-meta.json)
Saved under the execution folder for reference.
Use steroid_list_windows first when multiple IDE windows are open to get window_id.

• Send keyboard / mouse events to the IDE:

steroid_input(
project_name          = "eam-server",
task_id               = "my-task",
reason                = "dismiss trust project dialog",
screenshot_execution_id = "<id from steroid_take_screenshot>",
sequence              = "press:ENTER"
)

Sequence format (comma or newline separated):
stick:ALT              ← hold key until end of sequence
press:CTRL+SHIFT+F     ← press key combo
type:hello world       ← type text
click:Left@120,200     ← click at screenshot coords
click:Right@screen:400,300  ← click at screen coords
delay:400              ← wait 400 ms

Typical GUI automation workflow:
1. steroid_list_windows          → get window_id, check modalDialogShowing
2. steroid_take_screenshot       → see what is on screen
3. steroid_input (sequence)      → dismiss dialog / confirm action
4. steroid_take_screenshot again → verify the result

When to use GUI automation:
• A modal dialog is blocking (modalDialogShowing=true in steroid_list_windows)
• A refactoring opened a dialog that must be confirmed
• An action cannot be invoked programmatically (no Action ID available)
• Verifying IDE visual state during a complex multi-step operation

───────────────────────────────────────────────
🪟 MULTI-WINDOW & PROJECT MANAGEMENT
───────────────────────────────────────────────

• List all open projects:
steroid_list_projects()   ← returns project names for use in other steroid tools

• List open IDE windows with full readiness state:
steroid_list_windows()
Returns per window: modalDialogShowing, indexingInProgress, projectInitialized
Always check this before executing code after opening a project.

• Open a project and poll until ready:
steroid_open_project(project_path="/abs/path/to/project", task_id="my-task")
Then poll steroid_list_windows() until:
indexingInProgress  = false
projectInitialized  = true
modalDialogShowing  = false

───────────────────────────────────────────────
🔄 COMPLEX REFACTORING VIA INTELLIJ API
───────────────────────────────────────────────

Use steroid_execute_code for refactoring operations beyond rename_refactoring:

• Move class to another package
• Extract interface from a class
• Inline a method or field
• Pull members up / push members down in hierarchy
• Change method signature (add/remove/reorder parameters)

General pattern — always wrap reads in readAction { } and writes in writeAction { }:

     val psiClass = readAction {
         JavaPsiFacade.getInstance(project)
             .findClass("com.imprivata.OldClass", GlobalSearchScope.projectScope(project))
     }
     writeAction {
         // perform PSI mutation here
     }

───────────────────────────────────────────────
✏️ ATOMIC MULTI-FILE EDITS — steroid_apply_patch
───────────────────────────────────────────────

Use steroid_apply_patch for batch edits across multiple files in one atomic operation.
All hunks land in a single WriteCommandAction (one undo step). Pre-flight rejects
missing or non-unique old_string matches before touching any document.

Prefer this over repeated replace_text_in_file calls when editing 2+ files at once.

steroid_apply_patch(
project_name = "eam-server",
hunks = [
{ file_path: "src/com/imprivata/Foo.java", old_string: "old text", new_string: "new text" },
{ file_path: "src/com/imprivata/Bar.java", old_string: "old text", new_string: "new text" },
]
)

Rules:
• old_string must match exactly once in the file — ambiguous matches abort the entire patch
• Hunks within each file are applied in descending offset order automatically
• All touched documents are saved before the call returns
• Fetch the DSL reference first: steroid_fetch_resource("mcp-steroid://ide/apply-patch")

───────────────────────────────────────────────
📋 mcp-steroid DECISION MATRIX
───────────────────────────────────────────────

Task                                  → Approach
──────────────────────────────────────────────────────────────
Find all subclasses of a class        → execute_code (ClassInheritorsSearch)
Find all call sites of a method       → execute_code (ReferencesSearch)
Inspect PSI / AST of a file          → execute_code (PsiManager + PsiFile)
Batch file discovery (large project)  → execute_code (FilenameIndex)
Discover quick-fixes at a caret       → steroid_action_discovery
Invoke a quick-fix / refactoring      → action_discovery → execute_code
See what dialog appeared in IDE       → steroid_take_screenshot
Dismiss a blocking dialog             → take_screenshot → steroid_input
Run a gutter Run/Debug action         → action_discovery → execute_code
Open a new project and wait for index → steroid_open_project + steroid_list_windows
Extract method / inline / move class  → execute_code (IntelliJ Refactoring API)
Check if IDE is ready / indexing      → steroid_list_windows
Atomic batch edit across N files      → steroid_apply_patch

═══════════════════════════════════════════════
🐛 DEBUGGING
═══════════════════════════════════════════════

Use xdebug_* tools from intellij-mcpserver for full debug sessions:

Session lifecycle:
xdebug_get_debugger_status    — check active sessions BEFORE starting a new one
xdebug_start_debugger_session — start session by configurationName or filePath+line
xdebug_control_session        — STEP_INTO / STEP_OVER / STEP_OUT / RESUME / PAUSE /
STOP / WAIT_FOR_PAUSE / DRAIN_EVENTS

Breakpoints:
xdebug_set_breakpoint         — set line / conditional / tracepoint breakpoints;
check lineText in response to confirm placement
xdebug_list_breakpoints       — list all breakpoints (optionally filter by filePath)
xdebug_remove_breakpoint      — remove by owner + optional breakpointId / filePath+line

Inspection:
xdebug_get_stack              — view call stack (paginated with offset/limit)
xdebug_get_threads            — list all threads; active thread listed first
xdebug_get_frame_values       — inspect locals & fields at a stack frame (depth=0..N)
xdebug_get_value_by_path      — drill into nested objects: path=["obj","field","[0]"]
xdebug_evaluate_expression    — evaluate expression in current frame
xdebug_set_variable           — mutate a variable value during a paused session

Navigation:
xdebug_run_to_line            — resume to a target filePath+line without a breakpoint

Recommended flow:
1. xdebug_get_debugger_status        → confirm no stale session
2. xdebug_set_breakpoint             → set at least one breakpoint
3. xdebug_start_debugger_session     → start debug run
4. xdebug_control_session(WAIT_FOR_PAUSE)
5. xdebug_get_stack + xdebug_get_frame_values(depth=1)
6. xdebug_get_value_by_path / xdebug_evaluate_expression
7. xdebug_control_session(RESUME)    → repeat from step 4

═══════════════════════════════════════════════
📋 QUICK DECISION MATRIX
═══════════════════════════════════════════════

Task                              → Tool
──────────────────────────────────────────────────
Find file by name (keyword)       → find_files_by_name_keyword
Find files by glob (filesystem)   → find_files_by_glob
Find files by glob (IDE engine)   → search_file
Read a file                       → get_file_text_by_path / read_file
List directory contents           → list_directory_tree
Get currently open editors        → get_all_open_file_paths
Open file in IDE editor           → open_file_in_editor
Search text in code               → search_text / search_in_files_by_text
Search with regex                 → search_regex / search_in_files_by_regex
Find class / method / field       → search_symbol
Get symbol declaration/docs       → get_symbol_info
Edit a file (targeted)            → replace_text_in_file
Create a new file                 → create_new_file
Rename symbol everywhere          → rename_refactoring
Reformat a file                   → reformat_file
List project modules              → get_project_modules
List project dependencies         → get_project_dependencies
List VCS roots                    → get_repositories
Run shell command in terminal     → execute_terminal_command
Write & run custom inspection     → generate_inspection_kts_api → run_inspection_kts
Visualize PSI tree of code        → generate_psi_tree
Get inspection.kts examples       → generate_inspection_kts_examples
Compile / check errors            → build_project / get_file_problems
Run test or run configuration     → execute_run_configuration
Check active debug sessions       → xdebug_get_debugger_status
Set / update a breakpoint         → xdebug_set_breakpoint
List all breakpoints              → xdebug_list_breakpoints
Remove a breakpoint               → xdebug_remove_breakpoint
View call stack                   → xdebug_get_stack
List threads during debug         → xdebug_get_threads
Inspect frame variables           → xdebug_get_frame_values
Drill into nested debug value     → xdebug_get_value_by_path
Evaluate expression in debug      → xdebug_evaluate_expression
Mutate variable during debug      → xdebug_set_variable
Run to a specific line            → xdebug_run_to_line
PSI / advanced IntelliJ API       → steroid_execute_code
Find all subclasses of a class    → steroid_execute_code (ClassInheritorsSearch)
Find all call sites of a method   → steroid_execute_code (ReferencesSearch)
Discover quick-fixes at a caret   → steroid_action_discovery
Invoke quick-fix / refactoring    → steroid_action_discovery → steroid_execute_code
See current IDE screen            → steroid_take_screenshot
Dismiss a blocking dialog         → steroid_take_screenshot → steroid_input
Check IDE indexing / ready state  → steroid_list_windows
Open a project and wait for index → steroid_open_project → steroid_list_windows (poll)
List all open projects            → steroid_list_projects
Atomic batch edit across N files  → steroid_apply_patch
