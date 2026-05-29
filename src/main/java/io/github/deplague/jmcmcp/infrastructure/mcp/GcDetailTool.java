package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.GcDetailApplicationService;
import io.github.deplague.jmcmcp.domain.model.*;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * MCP tool adapter for detailed GC analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class GcDetailTool {

    private final GcDetailApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Detailed GC analysis: per-phase pause breakdowns, GC cause distribution, heap trends, and configuration.")
    public ToolResponse gcDetail(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "detail_level", required = false, description = "Detail level: summary, phases, heap_trends, all (default)") String detailLevel
    ) {
        try {
            GcDetailResult result = appService.analyze(jfrFilePath, startTime, endTime, detailLevel != null ? detailLevel : "all");
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(GcDetailResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Detailed GC Analysis\n\n");

        if (result.config() != null) {
            appendConfig(sb, result.config());
        }

        if (result.generationalSummary() != null) {
            appendGenerationalSummary(sb, result.generationalSummary());
        }

        if (result.referenceStats() != null && !result.referenceStats().isEmpty()) {
            appendReferenceStats(sb, result.referenceStats(), result.referenceOverheadPct());
        }

        if (result.causeDistribution() != null && !result.causeDistribution().isEmpty()) {
            appendCauseDistribution(sb, result.causeDistribution());
        }

        if (result.phaseBreakdown() != null && !result.phaseBreakdown().isEmpty()) {
            appendPhaseBreakdown(sb, result.phaseBreakdown());
        }

        if (result.heapTrendSummary() != null) {
            appendHeapTrends(sb, result.heapTrendSummary(), result.gcCycles());
        }

        return sb.toString();
    }

    private void appendConfig(StringBuilder sb, GcConfiguration config) {
        sb.append("## GC Configuration\n");
        sb.append("- **Young Collector:** ").append(config.youngCollector()).append("\n");
        sb.append("- **Old Collector:** ").append(config.oldCollector()).append("\n");
        sb.append("- **Parallel GC Threads:** ").append(config.parallelGcThreads()).append("\n");
        sb.append("- **Concurrent GC Threads:** ").append(config.concurrentGcThreads()).append("\n");
        sb.append("- **Min Heap Size:** ").append(config.minHeapSize()).append("\n");
        sb.append("- **Max Heap Size:** ").append(config.maxHeapSize()).append("\n");
        sb.append("- **Initial Heap Size:** ").append(config.initialHeapSize()).append("\n");
        sb.append("- **Max Tenuring Threshold:** ").append(config.maxTenuringThreshold()).append("\n\n");
    }

    private void appendGenerationalSummary(StringBuilder sb, GenerationalSummary summary) {
        sb.append("## Generational Summary\n");
        sb.append("| Generation | Count | Total Duration | Avg Duration |\n");
        sb.append("|------------|-------|----------------|--------------|\n");
        sb.append("| Young | ").append(summary.youngCount()).append(" | ")
                .append(summary.youngTotalDuration()).append(" | ")
                .append(summary.youngAvgDuration()).append(" |\n");
        sb.append("| Old/Full | ").append(summary.oldCount()).append(" | ")
                .append(summary.oldTotalDuration()).append(" | ")
                .append(summary.oldAvgDuration()).append(" |\n\n");
    }

    private void appendReferenceStats(StringBuilder sb, List<ReferenceStatEntry> stats, Double overhead) {
        sb.append("### GC Reference Statistics & Processing\n");
        sb.append("| Reference Type / Phase | Count | Total Processing Time |\n");
        sb.append("|------------------------|-------|-----------------------|\n");
        for (ReferenceStatEntry entry : stats) {
            sb.append("| ").append(entry.type())
                    .append(" | ").append(entry.count())
                    .append(" | ").append(entry.processingTime()).append(" |\n");
        }
        if (overhead != null) {
            sb.append(String.format("%n**Reference Processing Overhead:** %.1f%% of total GC pause time%n", overhead));
        }
        sb.append("\n");
    }

    private void appendCauseDistribution(StringBuilder sb, List<GcCauseEntry> causes) {
        sb.append("### GC Cause Distribution\n");
        sb.append("| Cause | Count |\n");
        sb.append("|-------|-------|\n");
        for (GcCauseEntry entry : causes) {
            sb.append("| ").append(entry.cause()).append(" | ").append(entry.count()).append(" |\n");
        }
        sb.append("\n");
    }

    private void appendPhaseBreakdown(StringBuilder sb, List<GcPhaseEntry> phases) {
        sb.append("## Pause Phase Breakdown\n");
        sb.append("| Phase Name | Count | Avg | P95 | P99 | Max |\n");
        sb.append("|------------|-------|-----|-----|-----|-----|\n");
        for (GcPhaseEntry entry : phases) {
            sb.append("| ").append(entry.name())
                    .append(" | ").append(entry.count())
                    .append(" | ").append(entry.avg())
                    .append(" | ").append(entry.p95())
                    .append(" | ").append(entry.p99())
                    .append(" | ").append(entry.max()).append(" |\n");
        }
        sb.append("\n");
    }

    private void appendHeapTrends(StringBuilder sb, HeapTrendSummary trend, List<GcCycleEntry> cycles) {
        sb.append("## Heap Trends\n");
        sb.append("- **Min Heap Used:** ").append(trend.minHeapUsed()).append("\n");
        sb.append("- **Max Heap Used:** ").append(trend.maxHeapUsed()).append("\n");
        sb.append("- **Avg Heap Used:** ").append(trend.avgHeapUsed()).append("\n");
        sb.append("- **P95 Heap Used:** ").append(trend.p95HeapUsed()).append("\n\n");

        if (cycles != null && !cycles.isEmpty()) {
            sb.append("### GC Cycle Heap Usage\n");
            sb.append("| GC ID | Heap Used | Heap Size |\n");
            sb.append("|-------|-----------|-----------|\n");
            for (GcCycleEntry entry : cycles) {
                sb.append("| ").append(entry.gcId())
                        .append(" | ").append(entry.heapUsed())
                        .append(" | ").append(entry.heapSize()).append(" |\n");
            }
            sb.append("\n");
        }
    }
}
