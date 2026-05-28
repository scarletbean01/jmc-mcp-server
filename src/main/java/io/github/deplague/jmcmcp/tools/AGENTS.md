# Package: io.github.deplague.jmcmcp.tools

This package contains legacy tool implementations and the core schema utility.

## Key Components
- **`SchemaUtil`**: **MANDATORY** utility for all MCP tools. Use it to build JSON schemas, parse arguments, and handle standard JFR properties (`jfr_file_path`, `start_time`, etc.).
- **Legacy Tools**: ~10 remaining tools that have not yet been fully migrated to the `adapters.mcp` + `application` + `domain` pattern.

## Guidelines for Agents
- **Refactoring Requirement:** If you touch a tool in this package, you should attempt to migrate it to the **Hexagonal Pattern** (`adapters.mcp`).
- **Schema Consistency:** Even refactored tools must use `SchemaUtil` to ensure the MCP client receives a consistent API.
- **Wiring:** Legacy tools are still manually wired in `JmcMcpServer.java` while refactored tools are auto-discovered.
