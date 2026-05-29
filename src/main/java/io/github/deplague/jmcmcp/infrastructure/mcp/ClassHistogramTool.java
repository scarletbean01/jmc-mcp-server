package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ClassHistogramApplicationService;
import io.github.deplague.jmcmcp.domain.model.ClassHistogramResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for class allocation histogram.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ClassHistogramTool {

    private final ClassHistogramApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Provide a class instance allocation histogram and top allocating classes.")
    public ToolResponse classHistogram(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top classes (default 20)") Integer topN
    ) {
        try {
            ClassHistogramResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime,
                    topN != null ? topN : 20
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ClassHistogramResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Class Allocation Histogram\n\n");

        if (!result.hasData()) {
            sb.append("No allocation events found in the recording.");
            return sb.toString();
        }

        sb.append("| Class | Count | Total Bytes | Avg Size |\n");
        sb.append("|-------|-------|-------------|----------|\n");

        for (var entry : result.entries()) {
            sb.append("| `")
                    .append(entry.className())
                    .append("` | ")
                    .append(entry.count())
                    .append(" | ")
                    .append(entry.totalBytes())
                    .append(" | ")
                    .append(entry.avgSize())
                    .append(" |\n");
        }

        return sb.toString();
    }
}
