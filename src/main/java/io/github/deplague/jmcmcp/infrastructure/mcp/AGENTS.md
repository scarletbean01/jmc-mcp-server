# Package: io.github.deplague.jmcmcp.infrastructure.mcp

This package contains the MCP driving adapters. Each class represents a set of MCP tools or resources and is automatically discovered via CDI.

## Responsibilities
- **Tool & Resource Definition:** Use `@Tool` and `@Resource` annotations to define metadata.
- **Concurrency Management:** Every tool method MUST be annotated with `@RunOnVirtualThread` to leverage Java's lightweight concurrency for heavy JFR analysis.
- **Request Delegation:** Tool methods delegate execution to the appropriate application service.
- **Output Formatting:** Format domain results into Markdown and wrap them in a `ToolResponse` or return a plain string for resources.

## Declarative Pattern
The server uses the Quarkus MCP server extension. Tools and resources are defined as simple methods within `@ApplicationScoped` beans:

```java
@HandleToolError
@ApplicationScoped
public final class ExampleTool {

    @RunOnVirtualThread
    @Tool(description = "Analyze something in JFR")
    public ToolResponse analyzeSomething(
            @ToolArg(name = "jfr_file_path", description = "Path to recording") String path,
            @ToolArg(name = "limit", required = false) Integer limit
    ) {
        var result = appService.execute(path, limit != null ? limit : 10);
        return ToolResponse.success(formatMarkdown(result));
    }

    @Resource(uri = "mcp-jmc://example", name = "Example Resource")
    public String getExample() {
        return "Resource content";
    }
}
```

## Guidelines for Agents
- **CDI Discovery:** Tools and resources MUST be annotated with `@ApplicationScoped`.
- **Error Handling:** Tools MUST be annotated with `@HandleToolError`. This global CDI interceptor automatically handles exceptions, logs them with MDC context (`tool` and `file`), and returns a `ToolResponse.error()`. Do not use boilerplate `try-catch` blocks in tool methods.
