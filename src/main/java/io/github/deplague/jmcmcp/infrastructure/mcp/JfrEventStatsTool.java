package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.JfrEventStatsApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrEventStatsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for per-event-type statistical summaries.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class JfrEventStatsTool {

    private final JfrEventStatsApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Provide statistical summaries for specific event types (e.g. jdk.GCPhasePause).")
    public ToolResponse jfrEventStats(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "event_type", description = "Event type ID to analyze") String eventType,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            JfrEventStatsResult result = appService.analyze(
                    jfrFilePath,
                    eventType,
                    startTime,
                    endTime
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(JfrEventStatsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Event Statistics: ").append(result.eventType()).append("\n\n");
        sb.append("- **Total Events:** ").append(result.totalEvents()).append("\n\n");

        if (!result.hasData()) {
            sb.append("No events found of this type.");
            return sb.toString();
        }

        if (!result.numericFields().isEmpty()) {
            sb.append("## Numeric Field Statistics\n");
            sb.append("| Field | Min | Avg | Max | P95 |\n");
            sb.append("|---|---|---|---|---|\n");
            for (var field : result.numericFields()) {
                sb.append(String.format(
                        "| `%s` | %s | %s | %s | %s |%n",
                        field.field(), field.min(), field.avg(), field.max(), field.p95()
                ));
            }
            sb.append("\n");
        }

        if (!result.categoricalFields().isEmpty()) {
            sb.append("## Categorical Field Distribution (Top 5)\n");
            for (var catField : result.categoricalFields()) {
                sb.append("### Field: `").append(catField.field()).append("`\n");
                sb.append("| Value | Count |\n");
                sb.append("|---|---|\n");
                for (var val : catField.values()) {
                    sb.append("| `").append(val.value()).append("` | ").append(val.count()).append(" |\n");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
