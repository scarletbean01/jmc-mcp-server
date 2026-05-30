# Package: io.github.deplague.jmcmcp.application

This package coordinates the application's use cases and defines the ports for infrastructure.

## Sub-packages
- **`port`**: Inbound and Outbound interfaces.
    - `JfrProvider`: Outbound port for loading/filtering recordings.
- **`service`**: Orchestrators (e.g., `CallTreeApplicationService`, `AsyncJobService`, `RecordingStorageService`) that use ports and domain services.

## Guidelines for Agents
- **Hexagonal Integrity:** Application services must remain agnostic of the driving protocol (MCP vs REST/HTTP). They return structured Java Records.
- **Shared Orchestration:** Services in this package are the source of truth for both MCP tools and REST endpoints.
- **Async & Jobs:** `AsyncJobService` manages the state and lifecycle of background analysis tasks.
- **Storage Lifecycle:** `RecordingStorageService` manages JFR persistence and scheduled cleanup.
- **Dependency Injection:** Use `@ApplicationScoped` and constructor-based injection.
- **Result Caching:** This layer is responsible for caching high-level analysis results (domain records) before they are formatted for the UI.
- **Error Handling:** Translate domain exceptions into application-level responses.
- **Concurrency:** We leverage **Java Virtual Threads** via the `@RunOnVirtualThread` annotation in the adapter layer. Application services should be thread-safe but generally don't need manual thread management.
