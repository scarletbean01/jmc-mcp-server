# Package: io.github.deplague.jmcmcp

This is the root package of the JMC MCP Server, responsible for bootstrapping and lifecycle management.

## Responsibilities
- **Bootstrap:** `JmcMcpServer.java` initializes the Quarkus container and the MCP Sync Server.
- **Discovery:** Leverages CDI `Instance<McpTool>` to automatically register refactored tools.
- **Graceful Shutdown:** Manages the cleanup of executors and caches via a shutdown hook.

## Guidelines for Agents
- **Hexagonal Context:** This package sits in the **Infrastructure** layer (it's the main entry point that wires everything together).
- **Tool Registration:** For new tools, simply annotate with `@ApplicationScoped` and implement `McpTool`. No changes to `JmcMcpServer` are required.
- **Phase 3 Alignment:** As part of the technical consolidation, ensure that `JmcMcpServer` only interacts with application services or high-level infrastructure ports.
