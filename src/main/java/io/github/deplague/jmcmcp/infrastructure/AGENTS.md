# Package: io.github.deplague.jmcmcp.infrastructure

This is the technical implementation layer of the hexagonal architecture. It contains both **Driving** (Inbound) and **Driven** (Outbound) adapters, as well as technical guards.

## Responsibilities

- **Inbound Adapters (mcp):** Declarative MCP tool and resource definitions.
- **Inbound Adapters (api):** Public REST API implementation for external integration.
- **Outbound Adapters (jfr):** Implementation of application ports for JFR loading, parsing, and caching.
- **Security:** Technical guards for path validation and access control.

## Sub-packages

- **`mcp`**: Model Context Protocol delivery.
- **`api`**: Public REST API (Quarkus REST, SSE).
- **`jfr`**: Technical JFR infrastructure and persistence.
- **`security`**: Access control logic.


## Guidelines for Agents

- **Separation of Concerns:** Keep technical details (JMC internal classes, MCP protocol specifics) strictly within this package. They must never leak into the `domain`.
- **Resource Management:** Use `JfrRecordingCache` and `CallTreeCache` to manage heavy JFR objects and prevent memory leaks.
- **Concurrency:** Most inbound methods must be annotated with `@RunOnVirtualThread`. Outbound components should be thread-safe for concurrent access.
- **Exception Mapping:** Map low-level technical exceptions (I/O, JMC parsing errors) into domain exceptions before propagating them.
