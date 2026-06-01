# Module: 02-application (jmc-mcp-application)

This module coordinates the application's use cases and defines the ports (SPIs) for infrastructure. It depends on `01-domain`.

## Sub-packages
- **`port`**: Outbound interfaces (SPIs) that the infrastructure must implement.
    - `JfrProvider`: Port for loading, filtering, and interacting with JFR recordings.
- **`service`**: Orchestrators that bridge ports and domain services.

## Key Classes
- **`AsyncJobService`**: Manages the state and lifecycle of background analysis tasks, including backpressure via `Semaphore` and SSE event broadcasting.
- **`RecordingStorageService`**: Handles JFR file uploads, persistent storage, and scheduled cleanup (e.g., removing recordings older than 24h).
- **`HighCpuDiagnosticApplicationService`**: A complex orchestrator that combines multiple domain services (Thread CPU, Profiling, GC) to diagnose performance issues.
- **`*ApplicationService`**: Standard orchestrators for individual analysis tools (e.g., `AllocationFlameApplicationService`, `GcDetailApplicationService`).

## Responsibilities
- **Use Case Orchestration:** Mapping user requests to domain logic and infrastructure ports.
- **Asynchronous Execution:** Managing long-running jobs and their status.
- **File Lifecycle:** Managing the storage and eviction of JFR recordings.
- **Data Formatting:** Using `FormatUtil` for common formatting tasks across services.

## Patterns Used
- **Dependency Injection:** `@ApplicationScoped` beans with constructor injection.
- **Backpressure:** Using `Semaphore` in `AsyncJobService` to prevent resource exhaustion.
- **Scheduled Tasks:** Using `@Scheduled` for background maintenance.
- **Managed Executors:** Leveraging Quarkus managed executors for async work.

## Guidelines for Agents
- **Layer Integrity:** Application services must return structured data (Java Records). Formatting for specific protocols (Markdown for MCP, JSON for REST) is the adapter's responsibility.
- **Agility:** Keep services protocol-agnostic. They should work equally well for MCP tools and REST endpoints.
- **Validation:** Application services should perform higher-level validation (e.g., checking if a recording exists) before calling domain services.
- **Virtual Threads:** While services are thread-safe, the *calling* adapter is usually responsible for the `@RunOnVirtualThread` annotation.
