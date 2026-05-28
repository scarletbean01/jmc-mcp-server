package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ObjectStatisticsApplicationService;
import io.github.deplague.jmcmcp.domain.model.ObjectStatisticsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for object statistics and heap occupancy analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ObjectStatisticsTool implements McpTool {

    private static final String NAME = "object_statistics";

    private final ObjectStatisticsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze object statistics and heap occupancy in a JFR recording. "
                                                + "Identifies classes with highest instance counts and total size."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "top_n",
                                                        SchemaUtil.intProp(
                                                                "Number of top classes to return (default 10)",
                                                                10
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
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
                        int topN = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "top_n",
                                10
                        );

                        ObjectStatisticsResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                topN
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

    private String formatMarkdown(ObjectStatisticsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Object Statistics Analysis\n\n");

        if (!result.hasData()) {
            sb.append(
                    "No object count events found. Make sure -XX:StartFlightRecording:settings=profile is used or the 'Object Count' event is enabled.\n"
            );
            return sb.toString();
        }

        sb.append("## Heap Occupancy (Live Objects)\n");
        sb.append("| Class | Count | Total Size |\n");
        sb.append("|-------|-------|------------|\n");
        for (var entry : result.entries()) {
            sb.append(String.format(
                    "| `%s` | %s | %s |%n",
                    entry.className(), entry.count(), entry.totalSize()
            ));
        }

        return sb.toString();
    }
}
