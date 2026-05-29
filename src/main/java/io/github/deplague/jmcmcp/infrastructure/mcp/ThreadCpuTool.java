package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadCpuApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * MCP tool adapter for thread CPU analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ThreadCpuTool {

    private final ThreadCpuApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Identify which threads are consuming the most CPU based on execution samples.")
    public ToolResponse threadCpu(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "package_prefix", required = false, description = "Optional package prefix to filter stack traces (e.g., 'com.mycompany')") String packagePrefix,
            @ToolArg(name = "top_n", required = false, description = "Number of top hot threads to return (default 10)") int topN
    ) {
        try {
            ThreadCpuResult result = appService.analyze(jfrFilePath, startTime, endTime, packagePrefix, topN);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ThreadCpuResult result) {
        if (result.totalSamples() == 0) {
            return "# Thread CPU Analysis\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread CPU Analysis\n\n");
        sb.append("Total samples: ").append(result.totalSamples()).append("\n\n");

        sb.append("## Per-Thread CPU Summary\n\n");
        sb.append("| Thread Name | Samples | CPU % | Primary States |\n");
        sb.append("|-------------|---------|-------|----------------|\n");
        for (var thread : result.threads()) {
            String states = thread.stateCounts().entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(2)
                    .map(e -> String.format("%s (%d)", e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format("| %s | %d | %.2f%% | %s |%n",
                    thread.threadName(), thread.samples(), thread.cpuPercent(), states));
        }

        sb.append("\n## Top Methods per Thread\n\n");
        for (var thread : result.threads()) {
            sb.append("### ").append(thread.threadName()).append("\n\n");
            sb.append("| Samples | Method (Top Frame) |\n");
            sb.append("|---------|--------------------|\n");
            for (var method : thread.topMethods()) {
                sb.append(String.format("| %d | `%s` |%n", method.samples(), method.method()));
            }
            sb.append("\n");
        }

        sb.append("## Thread State Distribution\n\n");
        sb.append("| State | Samples | Percentage |\n");
        sb.append("|-------|---------|------------|\n");
        for (var state : result.stateDistribution()) {
            sb.append(String.format("| %s | %d | %.2f%% |%n", state.state(), state.samples(), state.percent()));
        }

        return sb.toString();
    }
}
