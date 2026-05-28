package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.VirtualThreadsApplicationService;
import io.github.deplague.jmcmcp.domain.model.VirtualThreadsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for virtual thread analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class VirtualThreadsTool implements McpTool {

    private static final String NAME = "virtual_threads";

    private final VirtualThreadsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Analyze virtual thread pinning sites and execution failures (Java 21+).")
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
                                                                "Number of top pinning sites to return (default 10)",
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

                        VirtualThreadsResult result = appService.analyze(
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

    private String formatMarkdown(VirtualThreadsResult result) {
        if (!result.hasData()) {
            return "# Virtual Thread Analysis\n\nNo virtual thread pinning or failure events found in the recording. Virtual threads may not be in use, or JFR events are not enabled.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Virtual Thread Analysis\n\n");

        if (result.pinnedCount() > 0) {
            sb.append("## Pinning Summary\n\n");
            sb.append("- **Total Pinned Events:** ").append(result.pinnedCount()).append("\n\n");

            sb.append("### Top Pinning Sites\n\n");
            sb.append("| Stack Trace (top 5 frames) | Count | Percentage |\n");
            sb.append("|----------------------------|-------|------------|\n");
            for (var site : result.pinningSites()) {
                sb.append("| `")
                        .append(site.stackTrace().replace("\n", "`<br>`"))
                        .append("` | ")
                        .append(site.count())
                        .append(" | ")
                        .append(String.format("%.2f%%", site.percentage()))
                        .append(" |\n");
            }
            sb.append("\n");
        }

        if (result.submitFailedCount() > 0) {
            sb.append("## Submission Failures (Carrier Pool Exhaustion)\n\n");
            sb.append("| Exception | Count |\n");
            sb.append("|-----------|-------|\n");
            for (var failure : result.submitFailures()) {
                sb.append("| ").append(failure.exception()).append(" | ").append(failure.count()).append(" |\n");
            }
            sb.append("\n");
        }

        if (result.sleepFailedCount() > 0) {
            sb.append("## Sleep Failures\n\n");
            sb.append("| Exception | Count |\n");
            sb.append("|-----------|-------|\n");
            for (var failure : result.sleepFailures()) {
                sb.append("| ").append(failure.exception()).append(" | ").append(failure.count()).append(" |\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
