# Package: io.github.deplague.jmcmcp.adapters.mcp

This package contains the MCP driving adapters. Each class represents an MCP tool and is automatically discovered via CDI.

## Responsibilities
- **Schema Definition:** Define the tool name, description, and input schema using `SchemaUtil`.
- **Request Handling:** Extract arguments from the MCP request and delegate to an application service.
- **Output Formatting:** Format the domain results into Markdown for the LLM.

## Key Interface: `McpTool`
Every tool in this package must implement the `McpTool` interface:

```java
public interface McpTool {
    SyncToolSpecification spec();
}
```

The `spec()` method defines both the **Tool Metadata** (name, description, input schema) and the **Call Handler** (the logic that executes when the tool is called).

## Guidelines for Agents
- **CDI Discovery:** Tools should be annotated with `@ApplicationScoped`. They are automatically discovered by `JmcMcpServer` using `Instance<McpTool>`.
- **Constructor Injection:** Always use constructor injection for application services. Use `@Inject` on the constructor (often with Lombok's `@RequiredArgsConstructor(onConstructor_ = @Inject)`).
- **Markdown Formatting:** Use `StringBuilder` for constructing the output. Start with a clear `# Title`. Always include an `<agent_hint>` block at the end to guide the LLM to the next logical step.
- **Error Handling:** Catch exceptions in the call handler and return a `CallToolResult` with `isError(true)` and a descriptive message.
- **Schema Helper:** Use `io.github.deplague.jmcmcp.tools.SchemaUtil` for building JSON schemas. This ensures consistency across all tools.
- **Naming Convention:** Tool names (in `spec()`) should use snake_case (e.g., `hot_methods`, `smart_stack_trace_search`).
