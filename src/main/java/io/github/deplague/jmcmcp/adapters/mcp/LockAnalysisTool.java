package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.LockAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.LockAnalysisResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for advanced lock analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class LockAnalysisTool implements McpTool {

    private static final String NAME = "lock_analysis";

    private final LockAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze ThreadPark and Biased Lock Revocation events for advanced lock contention.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top sites to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        LockAnalysisResult result = appService.analyze(filePath, startTimeStr, endTimeStr, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(LockAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Advanced Lock Analysis\n\n");

        if (result.threadParkSummary().isPresent()) {
            var park = result.threadParkSummary().get();
            sb.append("## Thread Park Summary (LockSupport.park)\n");
            sb.append("- **Total Park Events:** ").append(park.count()).append("\n");
            sb.append("- **Avg Park Duration:** ").append(park.avgDuration()).append("\n");
            sb.append("- **Max Park Duration:** ").append(park.maxDuration()).append("\n\n");

            if (!park.topSites().isEmpty()) {
                sb.append("### Top Park Sites\n");
                sb.append("| Stack Trace | Count | Avg Duration | Max Duration |\n");
                sb.append("|---|---|---|---|\n");
                for (var site : park.topSites()) {
                    sb.append(String.format("| `%s` | %d | %s | %s |%n",
                            site.stackTrace().replace("\n", "`<br>`"),
                            site.count(),
                            site.avgDuration(),
                            site.maxDuration()));
                }
                sb.append("\n");
            }
        } else {
            sb.append("No Thread Park events found.\n\n");
        }

        if (result.biasedLockSummary().isPresent()) {
            var biased = result.biasedLockSummary().get();
            sb.append("## Biased Lock Revocations\n");
            sb.append("- **Single Revocations:** ").append(biased.singleRevocations()).append("\n");
            sb.append("- **Class/Bulk Revocations:** ").append(biased.classRevocations()).append("\n");
            sb.append("- **Self Revocations:** ").append(biased.selfRevocations()).append("\n\n");

            if (!biased.topClasses().isEmpty()) {
                sb.append("### Revoked Lock Classes\n");
                sb.append("| Lock Class | Revocation Count |\n");
                sb.append("|---|---|\n");
                for (var entry : biased.topClasses()) {
                    sb.append(String.format("| `%s` | %d |%n", entry.lockClass(), entry.count()));
                }
            }
        } else {
            sb.append("No Biased Lock Revocation events found.\n");
        }

        sb.append("\n<agent_hint>Lock contention detected. Consider `correlate` to see if I/O is performed under contended locks (a critical anti-pattern), or `deadlock_detection` to check for deadlock cycles.</agent_hint>\n");

        return sb.toString();
    }
}
