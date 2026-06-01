package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.github.deplague.jmcmcp.application.service.TimeSeriesApplicationService;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesBucketEntry;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for time-series performance trend analysis.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class TimeSeriesTool {

    private final TimeSeriesApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze performance trends over time in a JFR recording. " +
            "Buckets metrics like CPU load, GC pause duration, and allocation rate by time intervals.")
    public ToolResponse timeSeriesTool(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format (e.g., 2023-10-27T10:00:00Z)") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format (e.g., 2023-10-27T10:05:00Z)") String endTime,
            @ToolArg(name = "bucket_size", required = false, description = "Interval bucket size (e.g., '10s', '1m', '5m'). Default is '1m'.") String bucketSize,
            @ToolArg(name = "metric", required = false, description = "Metric to filter by (cpu, gc, alloc, all). Default is 'all'.") String metric
    ) {
        try {
            String bucketSizeStr = bucketSize != null ? bucketSize : "1m";
            String metricFilter = metric != null ? metric.toLowerCase() : "all";

            TimeSeriesResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime,
                    bucketSizeStr,
                    metricFilter
            );

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(TimeSeriesResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Performance Trends (Bucket Size: ")
                .append(result.bucketSize())
                .append(")\n\n");
        sb.append(result.warning());

        if (result.buckets().isEmpty()) {
            sb.append("No events found in the specified time range.\n");
            return sb.toString();
        }

        sb.append("| Time ");
        if (result.showCpu()) {
            sb.append("| Avg CPU Load ");
        }
        if (result.showGc()) {
            sb.append("| GC Pause Sum ");
        }
        if (result.showAlloc()) {
            sb.append("| Total Allocation ");
        }
        sb.append("|\n");

        sb.append("|------");
        if (result.showCpu()) {
            sb.append("|--------------");
        }
        if (result.showGc()) {
            sb.append("|--------------");
        }
        if (result.showAlloc()) {
            sb.append("|------------------");
        }
        sb.append("|\n");

        for (TimeSeriesBucketEntry b : result.buckets()) {
            sb.append("| ")
                    .append(FormatUtil.formatTime(b.bucketStartMs()))
                    .append(" ");
            if (result.showCpu()) {
                double cpu = b.cpuAvg() != null ? b.cpuAvg() * 100 : 0.0;
                sb.append(String.format("| %.2f%% ", cpu));
            }
            if (result.showGc()) {
                long gcMs = b.gcPauseSumNs() != null
                        ? b.gcPauseSumNs() / 1_000_000L
                        : 0L;
                sb.append("| ")
                        .append(FormatUtil.formatDuration(gcMs))
                        .append(" ");
            }
            if (result.showAlloc()) {
                long alloc = b.allocSumBytes() != null
                        ? b.allocSumBytes()
                        : 0L;
                sb.append("| ")
                        .append(FormatUtil.formatBytes(alloc))
                        .append(" ");
            }
            sb.append("|\n");
        }

        return sb.toString();
    }
}
