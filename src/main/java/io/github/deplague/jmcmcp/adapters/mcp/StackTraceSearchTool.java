package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.StackTraceSearchApplicationService;
import io.github.deplague.jmcmcp.domain.model.StackTraceSearchResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for full-text stack-trace search across all JFR event types.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class StackTraceSearchTool implements McpTool {

    private static final String NAME = "smart_stack_trace_search";

    private final StackTraceSearchApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description(
                                "Search for a class/method pattern across all JFR event types that contain stack traces. "
                                        + "Returns full (non-truncated) stack traces with event-specific details."
                        )
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "class_pattern", SchemaUtil.stringProp(
                                                "Regex pattern to match against class names in stack traces (e.g., '.*TenantService.*', '.*DAO.*')"
                                        ),
                                        "event_type", SchemaUtil.stringProp(
                                                "Filter to specific event type (e.g., 'jdk.JavaMonitorEnter'), or 'all'"
                                        ),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "limit", SchemaUtil.intProp(
                                                "Maximum results to return (default 20)", 20
                                        ),
                                        "async", SchemaUtil.boolProp(
                                                "Run analysis asynchronously and return a job ID", false
                                        )
                                ),
                                SchemaUtil.required("jfr_file_path", "class_pattern")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String classPattern = SchemaUtil.getString(request.arguments(), "class_pattern");
                        String eventType = SchemaUtil.getStringOrDefault(
                                request.arguments(), "event_type", "all"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(), "start_time", null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(), "end_time", null
                        );
                        int limit = SchemaUtil.getIntOrDefault(request.arguments(), "limit", 20);

                        StackTraceSearchResult result = appService.analyze(
                                filePath, startTimeStr, endTimeStr, classPattern, eventType, limit
                        );
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (IllegalArgumentException e) {
                        String msg = e.getMessage();
                        if (msg != null && msg.startsWith("Invalid regex pattern")) {
                            return CallToolResult.builder()
                                    .addTextContent("# Stack Trace Search\n\n" + msg)
                                    .isError(false)
                                    .build();
                        }
                        return CallToolResult.builder()
                                .addTextContent("Error: " + msg)
                                .isError(true)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    public String formatMarkdown(StackTraceSearchResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Stack Trace Search\n\n");
        sb.append("**Pattern:** `").append(result.classPattern()).append("`  \n");
        sb.append("**Event types searched:** ")
                .append("all".equals(result.eventType()) ? "all" : result.eventType())
                .append("  \n");
        sb.append("**Total matches found:** ")
                .append(result.limited() ? result.limit() + "+" : result.matches().size())
                .append("\n\n");

        sb.append("## Matching Stack Traces\n\n");
        for (int i = 0; i < result.matches().size(); i++) {
            var m = result.matches().get(i);
            sb.append("### Match ").append(i + 1).append("\n");
            sb.append("- **Event Type:** `").append(m.eventType()).append("`\n");
            sb.append("- **Timestamp:** ").append(m.timestamp()).append("\n");
            sb.append("- **Thread:** ").append(m.threadName()).append("\n");
            if (!m.details().isEmpty()) {
                for (var entry : m.details().entrySet()) {
                    sb.append("- **").append(entry.getKey()).append(":** ")
                            .append(entry.getValue()).append("\n");
                }
            }
            sb.append("- **Stack Trace:**\n```\n")
                    .append(m.fullTrace()).append("\n```\n\n");
        }

        sb.append("## Class Distribution\n\n");
        sb.append("| Event Type | Matches |\n");
        sb.append("|------------|--------|\n");
        result.distribution().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> sb.append("| `").append(e.getKey())
                        .append("` | ").append(e.getValue()).append(" |\n"));
        sb.append("\n");

        if (!result.distribution().isEmpty()) {
            String topType = result.distribution().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse("");
            long topCount = result.distribution().values().stream()
                    .max(Long::compare).orElse(0L);

            sb.append("<agent_hint>Found ").append(topCount)
                    .append(" matches in `").append(topType).append("`.");
            if (topType.contains("Monitor") || topType.contains("Park")) {
                sb.append(" Consider `thread_contention` for detailed lock analysis.");
            } else if (topType.contains("Socket") || topType.contains("File")) {
                sb.append(" Consider `io_hotspots` for I/O performance details.");
            } else if (topType.contains("Exception") || topType.contains("Error")) {
                sb.append(" Consider `error_analysis` or `exception_analysis` for exception details.");
            } else if (topType.contains("Allocation")) {
                sb.append(" Consider `allocation_hotspots` for memory allocation analysis.");
            } else if (topType.contains("ExecutionSample")) {
                sb.append(" Consider `hot_methods` for CPU hot spot analysis.");
            }
            sb.append("</agent_hint>\n");
        }

        return sb.toString();
    }
}
