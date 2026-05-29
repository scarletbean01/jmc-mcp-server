package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.GcRecommendationsApplicationService;
import io.github.deplague.jmcmcp.domain.model.*;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for GC recommendations.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class GcRecommendationsTool {

    private final GcRecommendationsApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze GC patterns and provide tuning recommendations. Evaluates pause distribution, GC cause patterns, heap utilization, and metaspace pressure to generate actionable JVM tuning advice.")
    public ToolResponse gcRecommendations(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            GcRecommendationsResult result = appService.analyze(jfrFilePath, startTime, endTime);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
