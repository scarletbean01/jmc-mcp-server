package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ObjectStatisticsApplicationService;
import io.github.deplague.jmcmcp.domain.model.ObjectStatisticsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for object statistics and heap occupancy analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ObjectStatisticsTool {

    private final ObjectStatisticsApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.OBJECT_STATISTICS,
            title = "Object Allocation Statistics",
            description = "Analyze object statistics and heap occupancy in a JFR recording. Identifies classes with highest instance counts and total size.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse objectStatistics(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
        ObjectStatisticsResult result = appService.analyze(
                jfrFilePath,
                startTime,
                endTime,
                topN != null ? topN : 10
        );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(ObjectStatisticsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Object Statistics Analysis\n\n");

        if (!result.hasData()) {
            sb.append(
                    "No object count events found. Make sure -XX:StartFlightRecording:settings=profile is used or the 'Object Count' event is enabled.\n"
            );
            return sb.toString();
        }

        sb.append("## Heap Occupancy (Live Objects)\n");
        sb.append("| Class | Count | Total Size |\n");
        sb.append("|-------|-------|------------|\n");
        for (var entry : result.entries()) {
            sb.append(String.format(
                    "| `%s` | %s | %s |%n",
                    entry.className(), entry.count(), entry.totalSize()
            ));
        }

        return sb.toString();
    }
}
