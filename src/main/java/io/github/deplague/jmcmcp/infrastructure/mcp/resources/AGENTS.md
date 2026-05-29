# Package: io.github.deplague.jmcmcp.infrastructure.mcp.resources

This package contains declarative MCP resource definitions.

## Responsibilities
- **Data Exposure:** Provide read-only access to reference data (e.g., JDK bug database) or dynamic JFR-derived data.
- **URI Mapping:** Define unique URIs using the `mcp-jmc://` scheme.

## Guidelines for Agents
- **Annotations:** Use `@Resource` from `io.quarkiverse.mcp.server`.
- **Return Types:** Resources typically return a `String` (Markdown) or `ResourceResponse`.
- **Scope:** Classes MUST be `@ApplicationScoped`.
