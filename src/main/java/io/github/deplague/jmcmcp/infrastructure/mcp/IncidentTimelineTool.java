package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.IncidentTimelineApplicationService;
import io.github.deplague.jmcmcp.domain.model.IncidentTimelineResult;
import io.github.deplague.jmcmcp.domain.model.TimelineEventEntry;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for incident timeline analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class IncidentTimelineTool {

    private final IncidentTimelineApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Recreate an incident timeline by locating an anchor event (or timestamp) and dumping a chronological list of high-impact events surrounding it.")
    public ToolResponse smartIncidentTimeline(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "anchor_event", required = false, description = "Optional event type to use as anchor (e.g., jdk.JavaErrorThrow)") String anchorEvent,
            @ToolArg(name = "anchor_time", required = false, description = "Optional ISO-8601 anchor time (used if anchor_event is omitted)") String anchorTime,
            @ToolArg(name = "window_ms", required = false, description = "Time window in milliseconds to inspect around the anchor (default 2000)") Integer windowMs
    ) {
        try {
            IncidentTimelineResult result = appService.analyze(
                    jfrFilePath, anchorEvent, anchorTime, windowMs != null ? windowMs : 2000
            );
            String markdown = formatMarkdown(result, anchorEvent);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(IncidentTimelineResult result, String anchorEvent) {
        StringBuilder sb = new StringBuilder();

        if (result.anchorTime() == null && result.events().isEmpty()) {
            if (anchorEvent != null) {
                return "# Incident Timeline\n\nCould not find anchor event: " + anchorEvent;
            }
            return "# Incident Timeline\n\nMust provide either anchor_event or anchor_time.";
        }

        sb.append("# Incident Timeline\n\n");
        sb.append("**Anchor Time:** ").append(result.anchorTime()).append("\n");
        sb.append("**Window:** +/- ").append(result.windowMs()).append("ms\n\n");

        if (result.events().isEmpty()) {
            sb.append("No significant events found in the window.\n");
            return sb.toString();
        }

        if (result.truncated()) {
            sb.append("> **Note:** Timeline truncated to 1000 events.\n\n");
        }

        for (TimelineEventEntry te : result.events()) {
            if (te.isAnchor()) {
                sb.append("- ").append(te.description()).append("  **<-- ANCHOR**\n");
            } else {
                sb.append("- ").append(te.description()).append("\n");
            }
        }

        return sb.toString();
    }
}
