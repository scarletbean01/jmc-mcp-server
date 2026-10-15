package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.SearchEventsApplicationService;
import io.github.deplague.jmcmcp.domain.model.SearchEventsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for searching specific JFR events.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class SearchEventsTool {

    private final SearchEventsApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.SEARCH_EVENTS,
            title = "Search JFR Events",
            description = "Search for specific JFR events by type ID.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse searchEvents(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "event_type", description = "JFR event type ID (e.g., jdk.CPULoad)") String eventType,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "limit", required = false, description = "Maximum number of events to return (default 20)") Integer limit
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                SearchEventsResult result = appService.search(
                            jfrFilePath,
                            startTime,
                            endTime,
                            eventType,
                            limit != null ? limit : 20
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(SearchEventsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Search Results: ")
                .append(result.eventType())
                .append(" (")
                .append(result.displayName())
                .append(")\n\n");

        if (!result.hasData()) {
            sb.append("No events found in the specified time range.");
            return sb.toString();
        }

        for (var entry : result.events()) {
            sb.append("### Event ").append(entry.index()).append("\n");
            for (var field : entry.fields().entrySet()) {
                sb.append("- **")
                        .append(field.getKey())
                        .append(":** ")
                        .append(field.getValue())
                        .append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
