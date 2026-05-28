package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.SafepointAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.SafepointAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.SafepointCauseEntry;
import io.github.deplague.jmcmcp.domain.model.TopSafepointEntry;
import io.github.deplague.jmcmcp.domain.model.TtspSummary;
import io.github.deplague.jmcmcp.domain.model.VmOperationSummary;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.unit.UnitLookup;

/**
 * MCP tool adapter for safepoint and STW pause analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class SafepointAnalysisTool implements McpTool {

    private static final String NAME = "safepoint_analysis";

    private final SafepointAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Analyze safepoint events and stop-the-world pauses outside of GC.")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "start_time", SchemaUtil.startTimeProp(),
                                                        "end_time", SchemaUtil.endTimeProp(),
                                                        "top_n", SchemaUtil.intProp("Number of top results (default 10)", 10)
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        SafepointAnalysisResult result = appService.analyze(
                                filePath, startTimeStr, endTimeStr, topN
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

    private String formatMarkdown(SafepointAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Safepoint & STW Pause Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No safepoint events found in the recording.\n");
            return sb.toString();
        }

        sb.append("## Safepoint Summary\n");
        sb.append("- **Total Safepoints:** ").append(result.safepointCount()).append("\n");
        sb.append("- **Total STW Time:** ").append(displayNanos(result.totalNanos())).append("\n");
        sb.append("- **Average Duration:** ").append(displayNanos(result.avgNanos())).append("\n");
        sb.append("- **Max Duration:** ").append(displayNanos(result.maxNanos())).append("\n");
        sb.append("- **P95 Duration:** ").append(displayNanos(result.p95Nanos())).append("\n\n");

        if (!result.causeDistribution().isEmpty()) {
            sb.append("## Safepoint Cause Distribution\n");
            sb.append("| Cause | Count | Total Duration | Avg Duration | Max Duration |\n");
            sb.append("|-------|-------|----------------|--------------|--------------|\n");
            for (SafepointCauseEntry e : result.causeDistribution()) {
                sb.append("| ").append(e.cause()).append(" | ")
                        .append(e.count()).append(" | ")
                        .append(displayNanos(e.totalNanos())).append(" | ")
                        .append(displayNanos(e.avgNanos())).append(" | ")
                        .append(displayNanos(e.maxNanos())).append(" |\n");
            }
            sb.append("\n");
        }

        if (!result.topSafepoints().isEmpty()) {
            sb.append("## Top Longest Safepoints\n");
            sb.append("| Duration | Cause | Start Time |\n");
            sb.append("|----------|-------|------------|\n");
            for (TopSafepointEntry e : result.topSafepoints()) {
                sb.append("| ").append(displayNanos(e.durationNanos())).append(" | ")
                        .append(e.cause()).append(" | ")
                        .append(e.startTime()).append(" |\n");
            }
            sb.append("\n");
        }

        if (result.vmOperationSummary() != null) {
            VmOperationSummary vm = result.vmOperationSummary();
            sb.append("## VM Operations Summary\n");
            sb.append("- **Total VM Operations:** ").append(vm.count()).append("\n");
            sb.append("- **Average Duration:** ").append(displayNanos(vm.avgNanos())).append("\n");
            sb.append("- **Max Duration:** ").append(displayNanos(vm.maxNanos())).append("\n\n");
        }

        if (result.ttspSummary() != null) {
            TtspSummary ttsp = result.ttspSummary();
            sb.append("## TTSP (Time-To-Safepoint)\n");
            sb.append("- **Average TTSP:** ").append(displayNanos(ttsp.avgNanos())).append("\n");
            sb.append("- **Max TTSP:** ").append(displayNanos(ttsp.maxNanos())).append("\n");
            sb.append("- **P95 TTSP:** ").append(displayNanos(ttsp.p95Nanos())).append("\n\n");
        }

        return sb.toString();
    }

    private static String displayNanos(long nanos) {
        if (nanos == 0) {
            return "0";
        }
        return JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(nanos));
    }
}
