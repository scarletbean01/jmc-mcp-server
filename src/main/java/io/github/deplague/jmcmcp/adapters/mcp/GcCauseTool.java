package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.GcCauseApplicationService;
import io.github.deplague.jmcmcp.domain.model.GcCauseResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing GC causes.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class GcCauseTool implements McpTool {

    private static final String NAME = "gc_cause";

    private final GcCauseApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Analyze GC causes to understand what triggers garbage collections.")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp()
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

                        GcCauseResult result = appService.analyze(
                                filePath,
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

    private String formatMarkdown(GcCauseResult result) {
        if (!result.hasData()) {
            return "# GC Cause Analysis\n\nNo young or old garbage collection events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# GC Cause Analysis\n\n");

        sb.append("## GC Cause Distribution (Overall)\n\n");
        sb.append("| Cause | Count | Total Pause | Avg Pause |\n");
        sb.append("|-------|-------|-------------|-----------|\n");
        for (var entry : result.overall()) {
            sb.append(String.format(
                    "| %s | %d | %s | %s |%n",
                    entry.cause(), entry.count(), entry.totalPause(), entry.avgPause()
            ));
        }

        if (!result.youngGen().isEmpty()) {
            sb.append("\n## Young Generation GC Causes\n\n");
            sb.append("| Cause | Count | Total Pause |\n");
            sb.append("|-------|-------|-------------|\n");
            for (var entry : result.youngGen()) {
                sb.append(String.format(
                        "| %s | %d | %s |%n",
                        entry.cause(), entry.count(), entry.totalPause()
                ));
            }
        }

        if (!result.oldGen().isEmpty()) {
            sb.append("\n## Old Generation GC Causes\n\n");
            sb.append("| Cause | Count | Total Pause |\n");
            sb.append("|-------|-------|-------------|\n");
            for (var entry : result.oldGen()) {
                sb.append(String.format(
                        "| %s | %d | %s |%n",
                        entry.cause(), entry.count(), entry.totalPause()
                ));
            }
        }

        return sb.toString();
    }
}
