package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.StackTraceSearchApplicationService;
import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.model.StackTraceSearchResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * MCP tool adapter for full-text stack-trace search across all JFR event types.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class StackTraceSearchTool {

    private final StackTraceSearchApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Search for a class/method pattern across all JFR event types that contain stack traces. Returns full (non-truncated) stack traces with event-specific details.")
    public ToolResponse smartStackTraceSearch(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "class_pattern", description = "Regex pattern to match against class names in stack traces (e.g., '.*TenantService.*', '.*DAO.*')") String classPattern,
            @ToolArg(name = "event_type", required = false, description = "Filter to specific event type (e.g., 'jdk.JavaMonitorEnter'), or 'all' (default)") String eventType,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "limit", required = false, description = "Maximum results to return (default 20)") Integer limit
    ) {
        try {
            StackTraceSearchResult result = appService.analyze(
                    jfrFilePath, startTime, endTime, classPattern, eventType != null ? eventType : "all", limit != null ? limit : 20
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (AnalysisFailedException e) {
            String msg = e.getMessage();
            if (msg != null && msg.startsWith("Invalid regex pattern")) {
                return ToolResponse.error("# Stack Trace Search\n\n" + msg);
            }
            return ToolResponse.error("Error: " + msg);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
