package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.SearchEventsApplicationService;
import io.github.deplague.jmcmcp.domain.model.SearchEventsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for searching specific JFR events.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class SearchEventsTool implements McpTool {

    private static final String NAME = "search_events";

    private final SearchEventsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Search for specific JFR events by type ID.")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "event_type",
                                                        SchemaUtil.stringProp("JFR event type ID (e.g., jdk.CPULoad)"),
                                                        "limit",
                                                        SchemaUtil.intProp(
                                                                "Maximum number of events to return (default 20)",
                                                                20
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path", "event_type")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "start_time",
                                null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "end_time",
                                null
                        );
                        String eventType = SchemaUtil.getString(
                                request.arguments(),
                                "event_type"
                        );
                        int limit = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "limit",
                                20
                        );

                        SearchEventsResult result = appService.search(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                eventType,
                                limit
                        );
                        String markdown = formatMarkdown(result);
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
