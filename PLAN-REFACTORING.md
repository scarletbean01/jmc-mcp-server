# Comprehensive Refactoring Plan: Hexagonal Architecture for JMC-MCP

## Objective
Refactor the `jmc-mcp` project to introduce a clean Ports and Adapters (Hexagonal) architecture. The goal is to decouple the core business logic from the 57 existing MCP Tool definitions, enabling the system to act as a backend server serving both existing MCP interfaces and future HTTP REST endpoints.

## Architectural Objectives

1. **Isolate the Core Domain into Domain Services**
   - **Action:** Extract business logic from the `analyze()` methods of all 57 `*Tool.java` files.
   - **Structure:** Relocate logic into Domain Services (e.g., `HotMethodsService`).
   - **Data Flow:** Services must return pure, structured Java objects (Java 21 Records acting as Value Objects) containing raw JFR analysis data, completely devoid of UI-specific formatting.
   - **Error Handling:** Define a custom exception hierarchy within the domain module (e.g., `JmcMcpDomainException` as the base, with specific types like `RecordingNotFoundException`, `AnalysisFailedException`). Domain Services will throw these exceptions, and the Adapters will translate them into protocol-specific error responses (e.g., `CallToolResult.isError(true)` for MCP, or HTTP `4xx/5xx` statuses).

2. **Establish Strict Modularization**
   - **Domain Module:** Create `io.github.deplague.jmcmcp.domain`. This module must have **zero** dependencies on MCP SDKs or HTTP/web infrastructure. It will house pure Domain Services and Records. *Note: For performance and memory efficiency, this layer is explicitly allowed to depend on JMC core libraries (`org.openjdk.jmc.common` and `org.openjdk.jmc.flightrecorder`) to operate directly on `IItemCollection` rather than translating millions of events into POJOs.*
   - **Application Module:** Create `io.github.deplague.jmcmcp.application`. This module will contain Application Services. These services will orchestrate calls to the Domain Services, define port interfaces (e.g., `JfrProvider`), and handle cross-cutting concerns like caching the structured Records before returning them to the adapters.
   - **Adapter Modules:** 
     - **Primary (Driving) Adapters:** 
       - Create `io.github.deplague.jmcmcp.adapters.mcp` for existing tools.
       - Create `io.github.deplague.jmcmcp.adapters.http` for future REST controllers.
     - **Secondary (Driven) Adapters:**
       - Create `io.github.deplague.jmcmcp.adapters.infrastructure` to house concrete implementations of ports defined in the Application layer (e.g., file I/O, JFR parsing, and specific JMC caches).

3. **Dismantle the God Class (`JfrAnalysisService`)**
   - **Action:** Break down the monolithic `JfrAnalysisService` into decoupled, single-responsibility components.
   - **Ports:** Define a `JfrProvider` interface in the Application/Domain layer (e.g., `IItemCollection getEvents(String path, TimeRange range)`).
   - **Infrastructure Adapter:** Move the concrete file loading (`JfrRecordingCache`), validation, and time-filtering logic into the `infrastructure` adapter.
   - **Application Services:** Extract the current caching logic (modified to cache Records instead of Strings) and async job execution into distinct Application Services.

4. **Refactor MCP Tools into Primary (Driving) Adapters**
   - **Action:** Keep existing 57 MCP tool definitions (schemas, caching, time-filtering, naming conventions) intact.
   - **Responsibility:** Modify `*Tool.java` classes to function strictly as driving adapters.
     - Parse inbound MCP JSON-RPC arguments.
     - Invoke the corresponding Application Service.
     - Format the returned Java Record into required Markdown or JSON string (preserving formatting, tables, and `<agent_hint>` tags).
     - Return the standard `CallToolResult`.

5. **Adopt Quarkus for Dependency Injection and Runtime**
   - **Action:** Transition from manual instantiation ("Poor Man's DI") in `JmcMcpServer.main()` to Quarkus CDI (Contexts and Dependency Injection).
   - **Constraint:** To maintain Hexagonal Architecture purity, the `domain` module must remain entirely free of framework annotations (no `@ApplicationScoped` or `@Inject`).
   - **Strategy:** Use standard CDI annotations in the `application` and `adapters` modules. For `domain` services, use Quarkus Producer Methods (e.g., `@Produces` in a `DomainConfig` class located in the application layer) to instantiate and inject pure Java domain objects into the CDI container.

## Implementation Strategy

### Phase 1: Foundation and Vertical Slice (Proof of Concept)
To mitigate risk, we will start with a single representative vertical slice.
1. **Quarkus Migration & Scaffolding:** 
   - Add Quarkus dependencies to the project (`pom.xml` / build tool).
   - **Logging:** Configure Quarkus logging (`application.properties`) to strictly use `stderr`. **Critical:** Quarkus must never log to `stdout`, as this will corrupt the JSON-RPC messages used by the MCP Stdio transport.
   - **Lifecycle & Transport:** Implement `@QuarkusMain` to manage the `StdioServerTransportProvider`. Use CDI (`@Inject @Any Instance<SyncToolSpecification>`) to dynamically discover and register all Tool Adapters, eliminating the massive manual wiring block.
   - Create the new package structure (`domain`, `application`, `adapters.mcp`, `adapters.http`, `adapters.infrastructure`).
2. **God Class Refactoring:** Extract `JfrProvider` and create the initial `infrastructure` adapter for file loading, managed via CDI.
3. **Vertical Slice Extraction:** 
   - Select `HotMethodsTool.java`.
   - Create `HotMethodsService` (Domain Service) and `HotMethodsResult` (Record) in the `domain` package.
   - Create an Application Service to orchestrate the `JfrProvider` and `HotMethodsService`, handling record caching.
   - Refactor `HotMethodsTool` to act as an MCP adapter, calling the Application Service and handling all Markdown formatting.
4. **Verification:** Ensure `ToolSchemaTest` and `HotMethodsToolTest` pass perfectly.

### Phase 2: Batch Refactoring
Iteratively apply the validated vertical slice pattern to the remaining 56 tools.
1. **Group Tools:** Batch tools by logical domains (e.g., Memory, Threads, GC, System) for systematic refactoring.
2. **Execution:** For each tool:
   - Extract logic to a corresponding domain service.
   - Define necessary Record structures.
   - Update the tool class to act as an adapter and format output.
3. **Continuous Verification:** Run tests after every batch to ensure no regressions occur.

## Verification & Testing
- All existing tests (e.g., `*ToolTest.java`, `ToolSchemaTest`) must continue to pass.
- Ensure strictly that no domain service contains references to Markdown formatting strings or MCP-specific classes.

## Scope & Impact
- **Affected Files:** All 57 `*Tool.java` files, corresponding tests, and `JmcMcpServer.java` (for potential package updates).
- **New Files:** ~57 new Domain Service classes and their Record structures.
- **Rollback Strategy:** Commits should be granular (Phase 1, then batched Phase 2) to allow safe rollback if any issues arise.