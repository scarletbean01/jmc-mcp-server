package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.JfrEventStatsApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrEventStatsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for per-event-type statistical summaries.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class JfrEventStatsTool implements McpTool {

    private static final String NAME = "jfr_event_stats";

    private final JfrEventStatsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Provide statistical summaries for specific event types (e.g. jdk.GCPhasePause)."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "event_type",
                                                        SchemaUtil.stringProp("Event type ID to analyze"),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp()
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
                        String eventType = SchemaUtil.getString(
                                request.arguments(),
                                "event_type"
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

                        JfrEventStatsResult result = appService.analyze(
                                filePath,
                                eventType,
                                startTimeStr,
                                endTimeStr
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
