# Package: io.github.deplague.jmcmcp.adapters.infrastructure

This package contains the **Driven Adapters** (Secondary Adapters) of the hexagonal architecture. It bridges the application to the outside world (filesystem, JMX, JMC libraries).

## Responsibilities
- **Data Ingestion:** Implementing `JfrProvider` to load and filter recordings.
- **Port Implementation:** Fulfilling contracts defined in `io.github.deplague.jmcmcp.application.port`.
- **Infrastructure State:** Managing low-level caches (`JfrRecordingCache`, `CallTreeCache`).

## Sub-packages (Phase 3 Targets)
- **`jfr`**: Technical JFR utilities and caching (relocating from root `jfr` package).
- **`security`**: Access control and path validation (relocating from `io.github.deplague.jmcmcp.security`).

## Guidelines for Agents
- **Adapter Logic:** Keep logic strictly focused on technical implementation. Do not leak infrastructure details (like JMC internal classes) into the return types of public methods if they are used by the Domain.
- **Resource Management:** Ensure `IItemCollection` instances are managed via the `JfrRecordingCache` to prevent memory leaks and redundant parsing.
- **Exception Mapping:** Map low-level I/O or JMC exceptions into Domain exceptions (e.g., `JmcMcpDomainException`) before they propagate to the Application layer.
- **Async Integration:** Coordinate with `AsyncJobService` for background tasks, ensuring that infrastructure resources remain available during long-running jobs.
