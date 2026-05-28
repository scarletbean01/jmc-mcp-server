package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.GcRecommendationsApplicationService;
import io.github.deplague.jmcmcp.domain.model.GcCauseEntry;
import io.github.deplague.jmcmcp.domain.model.GcRecommendationsResult;
import io.github.deplague.jmcmcp.domain.model.HeapUtilization;
import io.github.deplague.jmcmcp.domain.model.MetaspaceUtilization;
import io.github.deplague.jmcmcp.domain.model.PauseDistribution;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for GC recommendations.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class GcRecommendationsTool implements McpTool {

    private static final String NAME = "gc_recommendations";

    private final GcRecommendationsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze GC patterns and provide tuning recommendations. "
                                                + "Evaluates pause distribution, GC cause patterns, heap utilization, and metaspace pressure "
                                                + "to generate actionable JVM tuning advice."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "start_time", SchemaUtil.startTimeProp(),
                                                        "end_time", SchemaUtil.endTimeProp()
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

                        GcRecommendationsResult result = appService.analyze(filePath, startTimeStr, endTimeStr);
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

    private String formatMarkdown(GcRecommendationsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# GC Tuning Recommendations\n\n");

        if (result.gcAlgorithm() != null) {
            sb.append("**GC Algorithm:** ").append(result.gcAlgorithm()).append("\n\n");
        }

        if (result.pauseDistribution() != null) {
            PauseDistribution pd = result.pauseDistribution();
            sb.append("## Pause Distribution\n\n");
            sb.append("| Metric | Value |\n");
            sb.append("|--------|-------|\n");
            sb.append(String.format("| Total Pauses | %d |%n", pd.count()));
            sb.append(String.format("| Avg Pause | %s |%n", pd.avg()));
            sb.append(String.format("| P50 Pause | %s |%n", pd.p50()));
            sb.append(String.format("| P95 Pause | %s |%n", pd.p95()));
            sb.append(String.format("| P99 Pause | %s |%n", pd.p99()));
            sb.append(String.format("| Max Pause | %s |%n", pd.max()));
            sb.append("\n");
        } else {
            sb.append("No GC pause events found. Cannot provide recommendations.\n\n");
        }

        sb.append("## GC Cause Analysis\n\n");
        if (result.youngCauses() != null && !result.youngCauses().isEmpty()) {
            sb.append("### Young GC Causes\n\n");
            sb.append("| Cause | Count |\n|------|-------|\n");
            for (GcCauseEntry entry : result.youngCauses()) {
                sb.append(String.format("| %s | %d |%n", entry.cause(), entry.count()));
            }
            sb.append("\n");
        }
        if (result.oldCauses() != null && !result.oldCauses().isEmpty()) {
            sb.append("### Old GC Causes\n\n");
            sb.append("| Cause | Count |\n|------|-------|\n");
            for (GcCauseEntry entry : result.oldCauses()) {
                sb.append(String.format("| %s | %d |%n", entry.cause(), entry.count()));
            }
            sb.append("\n");
        }

        if (result.heapUtilization() != null) {
            HeapUtilization h = result.heapUtilization();
            sb.append("## Heap Utilization\n\n");
            sb.append("| Metric | Value |\n");
            sb.append("|--------|-------|\n");
            sb.append(String.format("| Min Heap Used | %s |%n", h.minHeapUsed()));
            sb.append(String.format("| Avg Heap Used | %s |%n", h.avgHeapUsed()));
            sb.append(String.format("| Max Heap Used | %s |%n", h.maxHeapUsed()));
            sb.append(String.format("| Heap Amplitude | %.1f%% |%n", h.heapAmplitudePct()));
            sb.append("\n");
        }

        if (result.metaspaceUtilization() != null) {
            MetaspaceUtilization m = result.metaspaceUtilization();
            sb.append("## Metaspace Utilization\n\n");
            sb.append(String.format("- **Used:** %.1f MB%n", m.usedMB()));
            sb.append(String.format("- **Committed:** %.1f MB%n", m.committedMB()));
            sb.append(String.format("- **Utilization:** %.1f%%%n%n", m.utilizationPct()));
        }

        if (result.warnings() != null && !result.warnings().isEmpty()) {
            sb.append("## ⚠️ Warnings\n\n");
            for (String warning : result.warnings()) {
                sb.append("- ").append(warning).append("\n");
            }
            sb.append("\n");
        }

        sb.append("## Recommendations\n\n");
        if (result.recommendations() != null && !result.recommendations().isEmpty()) {
            for (int i = 0; i < result.recommendations().size(); i++) {
                sb.append(String.format("%d. %s%n", i + 1, result.recommendations().get(i)));
            }
        } else {
            sb.append("GC behavior appears healthy. No specific tuning recommendations.\n");
        }

        return sb.toString();
    }
}
