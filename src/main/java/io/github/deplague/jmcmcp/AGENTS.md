# Package: io.github.deplague.jmcmcp

This is the root package of the JMC MCP Server, responsible for bootstrapping and lifecycle management.

## Responsibilities
- **Bootstrap:** `JmcMcpServer.java` is a `@QuarkusMain` entry point.
- **Discovery:** The server uses the Quarkus MCP server extension, which automatically discovers tools and resources via CDI annotations.
- **Configuration:** Runtime configuration is managed through `application.properties`.

## Guidelines for Agents
- **Infrastructure Layer:** This package is part of the Infrastructure layer in our hexagonal architecture.
- **Declarative Approach:** We have migrated from the imperative `McpTool` interface to a declarative approach using `@Tool` and `@Resource` annotations.
- **Registration:** New tools and resources no longer require manual registration. Simply annotate your classes with `@ApplicationScoped` and your methods with `@Tool` or `@Resource`.
- **Virtual Threads:** Ensure all heavy-lifting methods in adapters are annotated with `@RunOnVirtualThread`.
