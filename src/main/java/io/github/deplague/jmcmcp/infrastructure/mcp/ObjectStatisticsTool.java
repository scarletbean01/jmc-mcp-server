package io.github.deplague.jmcmcp.infrastructure.mcp;

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
    @Tool(description = "Analyze object statistics and heap occupancy in a JFR recording. Identifies classes with highest instance counts and total size.")
    public ToolResponse objectStatistics(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top classes to return (default 10)") int topN
    ) {
        try {
            ObjectStatisticsResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime,
                    topN
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
