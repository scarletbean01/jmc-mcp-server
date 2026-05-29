# Package: io.github.deplague.jmcmcp.infrastructure

This is the technical implementation layer of the hexagonal architecture. It contains both **Driving** (Inbound) and **Driven** (Outbound) adapters, as well as technical guards.

## Responsibilities

- **Inbound Adapters (mcp):** Declarative MCP tool and resource definitions that drive the application.
- **Outbound Adapters (jfr):** Implementation of application ports for JFR loading, parsing, and caching.
- **Security:** Technical access control and path validation.
- **Low-level JMC Access:** Optimized attribute extraction and aggregation utilities.

## Sub-packages

- **`mcp`**: The Model Context Protocol delivery mechanism.
- **`jfr`**: Technical JFR infrastructure, port implementations (`JfrProvider`), and low-level caches.
- **`security`**: File system access control and JFR path validation.

## Guidelines for Agents

- **Separation of Concerns:** Keep technical details (JMC internal classes, MCP protocol specifics) strictly within this package. They must never leak into the `domain`.
- **Resource Management:** Use `JfrRecordingCache` and `CallTreeCache` to manage heavy JFR objects and prevent memory leaks.
- **Concurrency:** Most inbound methods must be annotated with `@RunOnVirtualThread`. Outbound components should be thread-safe for concurrent access.
- **Exception Mapping:** Map low-level technical exceptions (I/O, JMC parsing errors) into domain exceptions before propagating them.
