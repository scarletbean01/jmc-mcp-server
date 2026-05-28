package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.IncidentTimelineApplicationService;
import io.github.deplague.jmcmcp.domain.model.IncidentTimelineResult;
import io.github.deplague.jmcmcp.domain.model.TimelineEventEntry;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for incident timeline analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class IncidentTimelineTool implements McpTool {

    private static final String NAME = "smart_incident_timeline";

    private final IncidentTimelineApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Recreate an incident timeline by locating an anchor event "
                                                + "(or timestamp) and dumping a chronological list of "
                                                + "high-impact events surrounding it."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "anchor_event", SchemaUtil.stringProp(
                                                                "Optional event type to use as anchor (e.g., jdk.JavaErrorThrow)"
                                                        ),
                                                        "anchor_time", SchemaUtil.stringProp(
                                                                "Optional ISO-8601 anchor time (used if anchor_event is omitted)"
                                                        ),
                                                        "window_ms", SchemaUtil.intProp(
                                                                "Time window in milliseconds to inspect around the anchor (default 2000)",
                                                                2000
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String anchorEvent = SchemaUtil.getStringOrDefault(request.arguments(), "anchor_event", null);
                        String anchorTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "anchor_time", null);
                        int windowMs = SchemaUtil.getIntOrDefault(request.arguments(), "window_ms", 2000);

                        IncidentTimelineResult result = appService.analyze(
                                filePath, anchorEvent, anchorTimeStr, windowMs
                        );
                        String markdown = formatMarkdown(result, anchorEvent);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
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
