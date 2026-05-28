# Package: io.github.deplague.jmcmcp.domain

This package contains the core domain logic of the application, following Hexagonal Architecture principles.

## Sub-packages
- **`model`**: Contains domain entities and records (e.g., `*Result` records returned by analysis).
- **`service`**: Pure Java services that perform JFR analysis.
- **`exception`**: Domain-specific exceptions.

## Guidelines for Agents
- **Purity:** This package MUST NOT depend on any frameworks (Quarkus, MCP SDK, etc.).
- **JMC Dependency:** While normally a domain layer is agnostic of all libraries, this layer is **explicitly allowed** to depend on JMC core libraries (`org.openjdk.jmc.common` and `org.openjdk.jmc.flightrecorder`). This avoids the overhead of translating millions of JFR events into POJOs.
- **Dependencies:** Only pure Java and JMC core libraries are allowed. Lombok is permitted as it is a compile-time utility.
- **Logic:** Business logic and analysis heuristics belong here. Do not include Markdown formatting or infrastructure concerns.
- **Null Safety:** Use `Optional` or explicit null checks where appropriate.
- **Thread Safety:** Domain services should be stateless or thread-safe.
