package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.DiffStackTracesApplicationService;
import io.github.deplague.jmcmcp.domain.model.DiffStackTracesResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for method-level diff between two JFR recordings.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class DiffStackTracesTool {

    private final DiffStackTracesApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Compare hot methods between two JFR recordings at the method level. Shows new methods, disappeared methods, and changed prominence (>20% change).")
    public ToolResponse smartDiffStackTraces(
            @ToolArg(name = "baseline_jfr_path", description = "Absolute or relative path to the baseline .jfr recording file") String baselineJfrPath,
            @ToolArg(name = "target_jfr_path", description = "Absolute or relative path to the target .jfr recording file") String targetJfrPath,
            @ToolArg(name = "package_prefix", required = false, description = "Optional package prefix to filter (e.g., 'com.mycompany')") String packagePrefix,
            @ToolArg(name = "top_n", required = false, description = "Number of top methods per category (default 20)") Integer topN
    ) {
        try {
            DiffStackTracesResult result = appService.analyze(
                    baselineJfrPath, targetJfrPath, packagePrefix, topN != null ? topN : 20);
            String markdown = formatMarkdown(result, topN != null ? topN : 20);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    public String formatMarkdown(DiffStackTracesResult result, int topN) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Stack Trace Diff\n\n");

        sb.append("## Recording Context\n\n");
        sb.append("| Recording | Duration | Total Samples | Samples/sec |\n");
        sb.append("|-----------|----------|---------------|-------------|\n");
        sb.append(String.format("| Baseline | %.1fs | %d | %.1f/s |\n",
                result.baselineDurationSec(), result.baselineTotalSamples(),
                result.baselineTotalSamples() / result.baselineDurationSec()));
        sb.append(String.format("| Target | %.1fs | %d | %.1f/s |\n\n",
                result.targetDurationSec(), result.targetTotalSamples(),
                result.targetTotalSamples() / result.targetDurationSec()));

        sb.append("## New Methods (in target, not in baseline)\n\n");
        if (result.newMethods().isEmpty()) {
            sb.append("No new methods found.\n\n");
        } else {
            sb.append("| Method | Target Samples/s | Baseline Samples/s | Change |\n");
            sb.append("|--------|-----------------|-------------------|--------|\n");
            result.newMethods().stream().limit(topN)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | 0 | NEW |\n",
                            d.methodName(), d.targetRate())));
            sb.append("\n");
        }

        sb.append("## Disappeared Methods (in baseline, not in target)\n\n");
        if (result.disappearedMethods().isEmpty()) {
            sb.append("No disappeared methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | Change |\n");
            sb.append("|--------|-------------------|-----------------|--------|\n");
            result.disappearedMethods().stream().limit(topN)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | 0 | REMOVED |\n",
                            d.methodName(), d.baselineRate())));
            sb.append("\n");
        }

        sb.append("## Changed Prominence (>20% change, normalized)\n\n");
        if (result.changedMethods().isEmpty()) {
            sb.append("No significantly changed methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | Change | % Change |\n");
            sb.append("|--------|-------------------|-----------------|--------|----------|\n");
            result.changedMethods().stream().limit(topN)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | %.1f | %.1f | %.1f%% |\n",
                            d.methodName(), d.baselineRate(), d.targetRate(),
                            d.absoluteChange(), d.pctChange())));
            sb.append("\n");
        }

        sb.append("## Stable Methods (<20% change, top 10)\n\n");
        if (result.stableMethods().isEmpty()) {
            sb.append("No stable methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | % Change |\n");
            sb.append("|--------|-------------------|-----------------|----------|\n");
            result.stableMethods().stream().limit(10)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | %.1f | %.1f%% |\n",
                            d.methodName(), d.baselineRate(), d.targetRate(), d.pctChange())));
            sb.append("\n");
        }

        sb.append("<agent_hint>Significant changes detected between recordings. Consider `compare_recordings` for metric-level comparison or `correlate` for deeper analysis of the target recording.</agent_hint>\n");

        return sb.toString();
    }
}
