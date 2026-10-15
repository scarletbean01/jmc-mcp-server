package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.github.deplague.jmcmcp.application.service.HeapTrendsApplicationService;
import io.github.deplague.jmcmcp.domain.model.*;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for heap, metaspace and thread trend analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class HeapTrendsTool {

    private final HeapTrendsApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.HEAP_TRENDS,
            title = "Heap Trends Analysis",
            description = "Analyze heap, metaspace, and thread count trends over time in a JFR recording. Buckets memory usage by time intervals to detect memory leaks and growth patterns.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse heapTrends(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "bucket_size", required = false, description = "Interval bucket size (e.g., '10s', '1m', '5m'). Default is '1m'.") String bucketSize
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                HeapTrendsResult result = appService.analyze(jfrFilePath, startTime, endTime, bucketSize != null ? bucketSize : "1m");
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(HeapTrendsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Heap & Memory Trends (Bucket Size: ").append(result.bucketSize()).append(")\n\n");

        sb.append("## Heap Usage Trend\n\n");
        sb.append("| Time | Min | Avg | Max |\n");
        sb.append("|------|-----|-----|-----|\n");
        for (HeapBucketEntry b : result.heapBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s |%n",
                    FormatUtil.formatTime(b.bucketStartMs()),
                    b.minBytes() != null ? FormatUtil.formatBytes(b.minBytes()) : "N/A",
                    b.avgBytes() != null ? FormatUtil.formatBytes(b.avgBytes()) : "N/A",
                    b.maxBytes() != null ? FormatUtil.formatBytes(b.maxBytes()) : "N/A"));
        }
        sb.append("\n");

        sb.append("## Metaspace Usage Trend\n\n");
        sb.append("| Time | Used Min | Used Avg | Used Max | Committed Min | Committed Avg | Committed Max |\n");
        sb.append("|------|----------|----------|----------|---------------|---------------|---------------|\n");
        for (MetaspaceBucketEntry b : result.metaspaceBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s | %s | %s | %s |%n",
                    FormatUtil.formatTime(b.bucketStartMs()),
                    b.minUsedBytes() != null ? FormatUtil.formatBytes(b.minUsedBytes()) : "N/A",
                    b.avgUsedBytes() != null ? FormatUtil.formatBytes(b.avgUsedBytes()) : "N/A",
                    b.maxUsedBytes() != null ? FormatUtil.formatBytes(b.maxUsedBytes()) : "N/A",
                    b.minCommittedBytes() != null ? FormatUtil.formatBytes(b.minCommittedBytes()) : "N/A",
                    b.avgCommittedBytes() != null ? FormatUtil.formatBytes(b.avgCommittedBytes()) : "N/A",
                    b.maxCommittedBytes() != null ? FormatUtil.formatBytes(b.maxCommittedBytes()) : "N/A"));
        }
        sb.append("\n");

        sb.append("## Thread Count Trend\n\n");
        sb.append("| Time | Min | Avg | Max |\n");
        sb.append("|------|-----|-----|-----|\n");
        for (ThreadBucketEntry b : result.threadBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s |%n",
                    FormatUtil.formatTime(b.bucketStartMs()),
                    b.minCount() != null ? b.minCount() : "N/A",
                    b.avgCount() != null ? b.avgCount() : "N/A",
                    b.maxCount() != null ? b.maxCount() : "N/A"));
        }
        sb.append("\n");

        sb.append("## Summary Statistics\n\n");
        sb.append("| Metric | Min | Avg | Max |\n");
        sb.append("|--------|-----|-----|-----|\n");
        if (result.heapSummary() != null) {
            MetricSummary s = result.heapSummary();
            sb.append(String.format("| Heap Used | %s | %s | %s |%n",
                    formatSummary(s.min()), formatSummary(s.avg()), formatSummary(s.max())));
        }
        if (result.metaspaceSummary() != null) {
            MetricSummary s = result.metaspaceSummary();
            sb.append(String.format("| Metaspace Used | %s | %s | %s |%n",
                    formatSummary(s.min()), formatSummary(s.avg()), formatSummary(s.max())));
        }
        if (result.threadSummary() != null) {
            MetricSummary s = result.threadSummary();
            sb.append(String.format("| Active Threads | %s | %s | %s |%n",
                    formatSummary(s.min()), formatSummary(s.avg()), formatSummary(s.max())));
        }
        sb.append("\n");

        return sb.toString();
    }

    private static String formatSummary(Long value) {
        return value != null ? FormatUtil.formatBytes(value) : "N/A";
    }
}
