package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.PredictiveLeakAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.LeakSuspectEntry;
import io.github.deplague.jmcmcp.domain.model.OomProjection;
import io.github.deplague.jmcmcp.domain.model.PredictiveLeakResult;
import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

/**
 * MCP tool adapter for predictive leak analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class PredictiveLeakAnalysisTool {

    private final PredictiveLeakAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Mathematically detect memory leaks using linear regression on post-GC heap usage. Projects time to OutOfMemoryError and cross-references Old Object Samples to identify leaking classes.")
    public ToolResponse smartPredictiveLeakAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "r_squared_threshold", required = false, description = "Minimum R² correlation to confirm a leak (default 0.85)") Double rSquaredThreshold
    ) {
        try {
            PredictiveLeakResult result = appService.analyze(
                    jfrFilePath, startTime, endTime, rSquaredThreshold != null ? rSquaredThreshold : 0.85);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
            sb.append(String.format("| Recording Duration | %s |%n", FormatUtil.formatDuration(result.recordingDurationMs())));
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
