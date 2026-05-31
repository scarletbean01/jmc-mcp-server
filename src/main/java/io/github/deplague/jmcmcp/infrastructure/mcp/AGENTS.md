# Package: io.github.deplague.jmcmcp.infrastructure.mcp

This package contains the MCP driving adapters using the Quarkus MCP server extension.

## Key Classes
- **`*Tool`**: Dozens of specialized MCP tool classes (e.g., `AllocationFlameTool`, `GcDetailTool`, `ThreadCpuTool`).
- **`ToolErrorInterceptor`**: A CDI interceptor that provides global error handling and MDC logging (tool name and file name) for all tools.
- **`HandleToolError`**: The binding annotation for the error interceptor.

## Responsibilities
- **Tool Definition:** Using `@Tool` and `@ToolArg` to define the MCP schema.
- **Result Formatting:** Formatting domain records into human-readable Markdown.
- **Context Injection:** Leveraging MDC to provide better logging context during analysis.

## Declarative Pattern
Tools are defined as `@ApplicationScoped` beans. Each tool method delegates to an application service:

```java
@HandleToolError
@ApplicationScoped
public final class ExampleTool {
    @RunOnVirtualThread
    @Tool(description = "Description")
    public ToolResponse execute(@ToolArg(name = "jfr_file_path") String path) {
        var result = service.analyze(path);
        return ToolResponse.success(formatMarkdown(result));
    }
}
```

## Patterns Used
- **AOP Error Handling:** `@HandleToolError` centralizes the conversion of exceptions to `ToolResponse.error()`.
- **Markdown Templates:** Most tools implement a private `formatMarkdown` method for presentation logic.
- **MDC Logging:** `ToolErrorInterceptor` automatically populates logging context.

## Guidelines for Agents
- **Metadata:** Provide clear, detailed descriptions in `@Tool` and `@ToolArg` annotations to help the LLM understand how to use the tool.
- **Virtual Threads:** **ALWAYS** use `@RunOnVirtualThread` on tool methods.
- **Discovery:** Ensure the class is `@ApplicationScoped` and annotated with `@HandleToolError`.
- **Brevity:** Tools should return concise but informative Markdown summaries.
