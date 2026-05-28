package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.PredictiveLeakAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.LeakSuspectEntry;
import io.github.deplague.jmcmcp.domain.model.OomProjection;
import io.github.deplague.jmcmcp.domain.model.PredictiveLeakResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for predictive leak analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class PredictiveLeakAnalysisTool implements McpTool {

    private static final String NAME = "smart_predictive_leak_analysis";

    private final PredictiveLeakAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Mathematically detect memory leaks using linear regression on post-GC heap usage. "
                                                + "Projects time to OutOfMemoryError and cross-references Old Object Samples to identify leaking classes."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "start_time", SchemaUtil.startTimeProp(),
                                                        "end_time", SchemaUtil.endTimeProp(),
                                                        "r_squared_threshold", SchemaUtil.numberProp(
                                                                "Minimum R² correlation to confirm a leak (default 0.85)", 0.85
                                                        ),
                                                        "async", SchemaUtil.boolProp(
                                                                "Run analysis asynchronously and return a job ID", false
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
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        double rSquaredThreshold = SchemaUtil.getNumberOrDefault(request.arguments(), "r_squared_threshold", 0.85);

                        PredictiveLeakResult result = appService.analyze(filePath, startTimeStr, endTimeStr, rSquaredThreshold);
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

    private String formatMarkdown(PredictiveLeakResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Predictive Leak Analysis\n\n");

        if (result.verdict().startsWith("No GC heap")) {
            sb.append(result.verdict()).append("\n");
            return sb.toString();
        }

        sb.append("## Verdict: ").append(result.verdict()).append("\n\n");

        if (result.verdict().contains("NO MEMORY LEAK")) {
            sb.append("Post-GC heap usage is stable or declining. No leak pattern found.\n\n");
        } else if (result.verdict().contains("INCONCLUSIVE")) {
            sb.append("Heap growth does not follow a linear pattern. Possible causes: bursty allocation, GC tuning needed.\n\n");
        }

        sb.append("## Leak Metrics\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Post-GC Heap Growth Rate | %.2f KB/min |%n", result.growthRateKBPerMin()));
        sb.append(String.format("| R² Correlation | %.4f |%n", result.rSquared()));
        sb.append(String.format("| Current Post-GC Heap | %.1f MB |%n", result.currentHeapMB()));
        if (result.maxHeapMB() != null) {
            sb.append(String.format("| Max Heap Size | %.1f MB |%n", result.maxHeapMB()));
            sb.append(String.format("| Heap Utilization | %.1f%% |%n", result.heapUtilizationPct()));
        }
        sb.append(String.format("| Data Points | %d (post-GC samples) |%n", result.dataPointCount()));
        sb.append("\n");

        if (result.oomProjection() != null) {
            OomProjection oom = result.oomProjection();
            sb.append("## OutOfMemoryError Projection\n\n");
            sb.append("| Metric | Value |\n");
            sb.append("|--------|-------|\n");
            sb.append(String.format("| Projected OOM Time | %s |%n", Instant.ofEpochMilli(oom.projectedOomTimeMs())));
            if (oom.minutesToOom() != null) {
                sb.append(String.format("| Time to OOM from End of Recording | ~%.1f minutes |%n", oom.minutesToOom()));
            } else {
                sb.append("| Time to OOM from End of Recording | **ALREADY EXCEEDED** ⛔ |\n");
            }
            sb.append("\n");
        }

        sb.append("## Regression Details\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Slope (bytes/ms) | %.2f |%n", result.slope()));
        sb.append(String.format("| Intercept (bytes) | %.1f |%n", result.intercept()));
        sb.append(String.format("| R² | %.4f |%n", result.rSquared()));
        if (result.recordingDurationMs() != null) {
            sb.append(String.format("| Recording Duration | %s |%n", SchemaUtil.formatDuration(result.recordingDurationMs())));
        }
        sb.append("\n");

        if (result.leakSuspects() != null && !result.leakSuspects().isEmpty()) {
            sb.append("## Leak Suspects (Old Object Samples)\n\n");
            sb.append("Total sampled objects: ").append(
                    result.leakSuspects().stream().mapToLong(LeakSuspectEntry::sampleCount).sum()).append("\n\n");
            sb.append("| Class | Sample Count | % of Total |\n");
            sb.append("|-------|-------------|------------|\n");
            for (LeakSuspectEntry entry : result.leakSuspects()) {
                sb.append(String.format("| `%s` | %d | %.1f%% |%n", entry.className(), entry.sampleCount(), entry.percentage()));
            }
            sb.append("\n");
        } else {
            sb.append("## Leak Suspects\n\n");
            sb.append("No `jdk.OldObjectSample` events found. Enable Old Object Sampling with ");
            sb.append("`-XX:StartFlightRecording:settings=profile` for leak suspect identification.\n\n");
        }

        return sb.toString();
    }
}
