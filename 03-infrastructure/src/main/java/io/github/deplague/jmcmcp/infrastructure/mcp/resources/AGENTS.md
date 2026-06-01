# Package: io.github.deplague.jmcmcp.infrastructure.mcp.resources

This package contains declarative MCP resource definitions.

## Key Classes
- **`JdkBugDatabaseResource`**: Exposes the JDK bug reference database as an MCP resource.

## Responsibilities
- **Static Data Exposure:** Providing reference datasets to the LLM.
- **Dynamic Content:** Exposing JFR-derived data as read-only resources when appropriate.

## Patterns Used
- **Declarative Resources:** Using `@Resource` from the Quarkus MCP extension.

## Guidelines for Agents
- **URI Scheme:** Use `mcp-jmc://` for all resource URIs.
- **Scope:** Classes MUST be `@ApplicationScoped`.
- **Read-Only:** Resources should be used for data that is "read-only" in nature; use tools for operations that require parameters or computation.
