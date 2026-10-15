package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ClassLoadingApplicationService;
import io.github.deplague.jmcmcp.domain.model.ClassLoadingResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for analyzing class loading events and statistics.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ClassLoadingTool {

    private final ClassLoadingApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.CLASS_LOADING,
            title = "Class Loading Analysis",
            description = "Analyze class loading events and statistics in a JFR recording. Identifies longest-loading classes and metaspace pressure.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse classLoading(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                ClassLoadingResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            topN != null ? topN : 10
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(ClassLoadingResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Class Loading Analysis\n\n");

        if (!result.hasAnyData()) {
            sb.append("No class loading events found in the recording.\n");
            return sb.toString();
        }

        if (result.hasClassLoadEvents()) {
            sb.append("## Longest Class Loads\n");
            sb.append("| Class | Duration | Initiating Loader |\n");
            sb.append("|-------|----------|-------------------|\n");
            for (var entry : result.longestLoads()) {
                sb.append(String.format(
                        "| `%s` | %s | %s |%n",
                        entry.className(), entry.duration(), entry.loader()
                ));
            }
            sb.append("\n");
        }

        if (result.hasStatsEvents()) {
            sb.append("## Class Loading Statistics\n");
            result.stats().maxLoadedCount().ifPresent(v ->
                    sb.append(String.format("- **Max Loaded Class Count:** %s%n", v))
            );
            result.stats().maxUnloadedCount().ifPresent(v ->
                    sb.append(String.format("- **Max Unloaded Class Count:** %s%n", v))
            );
            sb.append("\n");
        }

        return sb.toString();
    }
}
