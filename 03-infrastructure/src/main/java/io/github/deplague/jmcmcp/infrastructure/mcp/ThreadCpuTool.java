package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ThreadCpuApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
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
    @Tool(
            name = ToolNames.THREAD_CPU,
            title = "Thread CPU Analysis",
            description = "Identify which threads are consuming the most CPU based on execution samples.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse threadCpu(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "package_prefix", required = false, description = ToolDocs.PACKAGE_PREFIX) String packagePrefix,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                ThreadCpuResult result = appService.analyze(jfrFilePath, startTime, endTime, packagePrefix, topN != null ? topN : 10);
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
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
