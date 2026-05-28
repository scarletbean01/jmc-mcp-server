# Comprehensive Refactoring Plan: Hexagonal Architecture for JMC-MCP

## Objective
Refactor the `jmc-mcp` project to introduce a clean Ports and Adapters (Hexagonal) architecture. The goal is to decouple the core business logic from the 57 existing MCP Tool definitions, enabling the system to act as a backend server serving both existing MCP interfaces and future HTTP REST endpoints.

---

## ✅ Completed

### Phase 1: Foundation and Vertical Slice
- [x] Quarkus migration with `@QuarkusMain` and CDI discovery
- [x] `JfrProvider` port extracted and `JfrProviderImpl` implemented
- [x] HotMethodsTool vertical slice (domain service → application service → MCP adapter)
- [x] Package structure created (`domain`, `application`, `adapters.mcp`, `adapters.infrastructure`)

### Phase 2: Batch Refactoring
- [x] All 57 tools extracted to domain services + records + application services + MCP adapters
- [x] All legacy `tools.*` wrapper classes deleted
- [x] All tests migrated from `tools.*` to `adapters.mcp.*`
- [x] `ToolSchemaTest` updated to scan `adapters.mcp` only

### Phase 3: Infrastructure Consolidation
- [x] `JfrRecordingCache`, `CallTreeCache`, `JfrItemUtils` → `adapters.infrastructure.jfr`
- [x] `RecordingAccessController` → `adapters.infrastructure.security`
- [x] `JfrAnalysisService` god class deleted
- [x] `JfrItemUtils.display()` added to replace `JfrAnalysisService.display()`
- [x] Old `jfr/` and `security/` root packages emptied (AGENTS.md placeholders remain)

---

## Remaining Work

### 1. Wire Up Custom Exception Hierarchy
**Status:** Classes exist but are unused.

`JmcMcpDomainException`, `RecordingNotFoundException`, and `AnalysisFailedException` were created in `domain.exception` but:
- Domain services do not throw them
- `JfrProviderImpl` does not map `IOException` / JMC parse failures into them
- MCP adapters do not catch and translate them into `CallToolResult.isError(true)`

**Action:**
- Update `JfrProviderImpl.loadRecording()` to throw `RecordingNotFoundException` for missing files
- Update domain services to throw `AnalysisFailedException` on unexpected JMC errors
- Update all MCP adapters' `callHandler` to catch `JmcMcpDomainException` subclasses and return structured error `CallToolResult`

### 2. Application-Layer Record Caching
**Status:** Not implemented.

The plan specified "caching the structured Records before returning them to the adapters." Currently:
- `JfrRecordingCache` caches raw `IItemCollection` instances
- There is no cache for parsed analysis results (Records)

**Action:**
- Add a lightweight LRU cache (e.g., `Map<(filePath, toolName, args), Record>`) in each Application Service, or a shared `AnalysisResultCache` utility
- This would avoid re-running expensive JMC aggregations on repeated identical requests

### 3. HTTP REST Adapter (adapters.http)
**Status:** Package does not exist.

The plan mentioned `io.github.deplague.jmcmcp.adapters.http` for future REST controllers. This remains a future architectural enabler — no work required until an HTTP transport is actually needed.

### 4. Domain Purity Audit
**Status:** Partial.

Most domain services are pure, but some still import infrastructure utilities directly:
- `JfrItemUtils` is imported by ~50 domain services. While practical, the plan suggested "domain-aligned abstractions or clearly labeled technical helpers."

**Action:**
- Evaluate whether `JfrItemUtils` should be split into a `domain.spi` (service provider interface) vs. keeping it as a shared technical core
- No urgent change needed — current approach is pragmatic and performant

---

## Verification & Testing
- All existing tests (`*ToolTest.java`, `ToolSchemaTest`) must continue to pass.
- Ensure strictly that no domain service contains references to Markdown formatting strings or MCP-specific classes.

---

## Scope & Impact
- **Affected Files:** All 57 `*Tool.java` files, corresponding tests, and `JmcMcpServer.java`.
- **New Files:** ~57 new Domain Service classes and their Record structures.
