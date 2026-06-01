package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.DirectBuffersApplicationService;
import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.github.deplague.jmcmcp.domain.model.DirectBufferResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for off-heap direct buffer statistics analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class DirectBuffersTool {

    private final DirectBuffersApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze off-heap direct buffer statistics to detect potential memory leaks.")
    public ToolResponse directBuffers(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            DirectBufferResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(DirectBufferResult result) {
        if (!result.hasData()) {
            return "# Direct Buffer Statistics\n\nNo direct buffer statistics events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Direct Buffer Statistics\n\n");

        sb.append("## Direct Buffer Summary\n\n");
        sb.append("- **Buffer Count:** ")
                .append(result.minCount().orElse("N/A"))
                .append(" (Min) / ")
                .append(result.avgCount().orElse("N/A"))
                .append(" (Avg) / ")
                .append(result.maxCount().orElse("N/A"))
                .append(" (Max)\n");
        sb.append("- **Max Total Capacity:** ").append(result.maxCapacity().orElse("N/A")).append("\n");
        sb.append("- **Max Memory Used:** ").append(result.maxUsed().orElse("N/A")).append("\n\n");

        if (result.maxDirectMemorySize().isPresent()) {
            sb.append("## Memory Pressure Warning\n\n");
            sb.append("- **Configured MaxDirectMemorySize:** ")
                    .append(result.maxDirectMemorySize().get())
                    .append("\n");
            result.maxUtilizationPercent().ifPresent(util -> {
                sb.append(String.format("- **Max Utilization:** %.2f%%\n", util));
                if (util > 90) {
                    sb.append("\n**⚠️ WARNING:** Direct memory utilization exceeded 90%. Risk of OutOfMemoryError: Direct buffer memory.\n");
                }
            });
            sb.append("\n");
        }

        if (!result.trend().isEmpty()) {
            sb.append("## Direct Buffer Trend\n\n");
            sb.append("| Time | Buffer Count | Total Capacity | Memory Used |\n");
            sb.append("|------|--------------|----------------|-------------|\n");

            int maxRows = 20;
            int step = Math.max(1, result.trend().size() / maxRows);
            int i = 0;
            for (var sample : result.trend()) {
                if (i % step == 0 || i == result.trend().size() - 1) {
                    sb.append("| ")
                            .append(FormatUtil.formatTime(sample.timestampMs()))
                            .append(" | ")
                            .append(sample.count())
                            .append(" | ")
                            .append(formatBytes(sample.capacity()))
                            .append(" | ")
                            .append(formatBytes(sample.used()))
                            .append(" |\n");
                }
                i++;
            }
        }

        return sb.toString();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
