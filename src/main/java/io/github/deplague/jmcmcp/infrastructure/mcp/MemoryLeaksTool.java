package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.MemoryLeaksApplicationService;
import io.github.deplague.jmcmcp.domain.model.LeakSiteEntry;
import io.github.deplague.jmcmcp.domain.model.LeakingClassEntry;
import io.github.deplague.jmcmcp.domain.model.MemoryLeaksResult;
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
 * MCP tool adapter for memory leak analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class MemoryLeaksTool {

    private final MemoryLeaksApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze old object samples to identify potential memory leaks, leaking classes, and allocation sites.")
    public ToolResponse memoryLeaks(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top leaking classes/sites (default 20)") Integer topN
    ) {
        try {
            MemoryLeaksResult result = appService.analyze(
                    jfrFilePath, startTime, endTime, topN != null ? topN : 20
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(MemoryLeaksResult result) {
        if (!result.hasData()) {
            return "# Memory Leaks Analysis\n\nNo jdk.OldObjectSample events found. "
                    + "Make sure you are using a profile that enables Old Object Sample events "
                    + "(e.g., -XX:StartFlightRecording:settings=profile).";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Memory Leaks Analysis (Old Object Samples)\n\n");
        sb.append("- **Total Sampled Objects:** ").append(result.totalSampledObjects()).append("\n\n");

        sb.append("## Top Leaking Classes\n");
        sb.append("| Class | Sample Count |\n|---|---|\n");
        for (LeakingClassEntry e : result.leakingClasses()) {
            sb.append(String.format("| `%s` | %d |\n", e.className(), e.sampleCount()));
        }
        sb.append("\n");

        sb.append("## Top Leak Allocation Sites\n");
        sb.append("| Sample Count | Allocation Site |\n|---|---|\n");
        for (LeakSiteEntry e : result.leakSites()) {
            sb.append(String.format(
                    "| %d | `%s` |\n",
                    e.sampleCount(),
                    e.siteKey().replace("\n", "`<br>`")
            ));
        }

        sb.append("\n<agent_hint>Leak suspects identified. Consider `predictive_leak_analysis` "
                + "for mathematical leak confirmation and OOM time projection, or `heap_trends` "
                + "for memory growth visualization.</agent_hint>\n");

        return sb.toString();
    }
}
