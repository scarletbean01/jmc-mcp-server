package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrValueConverter;
import io.github.deplague.jmcmcp.application.service.SafepointAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.*;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
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
@HandleToolError
@ApplicationScoped
public final class SafepointAnalysisTool {

    private final SafepointAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze safepoint events and stop-the-world pauses outside of GC.")
    public ToolResponse safepointAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top results (default 10)") Integer topN
    ) {
        try {
            SafepointAnalysisResult result = appService.analyze(
                    jfrFilePath, startTime, endTime, topN != null ? topN : 10
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
        return JfrValueConverter.display(UnitLookup.NANOSECOND.quantity(nanos));
    }
}
