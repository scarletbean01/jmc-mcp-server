package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.HeapTrendsApplicationService;
import io.github.deplague.jmcmcp.domain.model.HeapBucketEntry;
import io.github.deplague.jmcmcp.domain.model.HeapTrendsResult;
import io.github.deplague.jmcmcp.domain.model.MetaspaceBucketEntry;
import io.github.deplague.jmcmcp.domain.model.MetricSummary;
import io.github.deplague.jmcmcp.domain.model.ThreadBucketEntry;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for heap, metaspace and thread trend analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class HeapTrendsTool implements McpTool {

    private static final String NAME = "heap_trends";

    private final HeapTrendsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze heap, metaspace, and thread count trends over time in a JFR recording. "
                                                + "Buckets memory usage by time intervals to detect memory leaks and growth patterns."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "start_time", SchemaUtil.startTimeProp(),
                                                        "end_time", SchemaUtil.endTimeProp(),
                                                        "bucket_size", SchemaUtil.stringProp(
                                                                "Interval bucket size (e.g., '10s', '1m', '5m'). Default is '1m'."
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String bucketSizeStr = SchemaUtil.getStringOrDefault(request.arguments(), "bucket_size", "1m");

                        HeapTrendsResult result = appService.analyze(filePath, startTimeStr, endTimeStr, bucketSizeStr);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String formatMarkdown(HeapTrendsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Heap & Memory Trends (Bucket Size: ").append(result.bucketSize()).append(")\n\n");

        sb.append("## Heap Usage Trend\n\n");
        sb.append("| Time | Min | Avg | Max |\n");
        sb.append("|------|-----|-----|-----|\n");
        for (HeapBucketEntry b : result.heapBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s |%n",
                    SchemaUtil.formatTime(b.bucketStartMs()),
                    b.minBytes() != null ? SchemaUtil.formatBytes(b.minBytes()) : "N/A",
                    b.avgBytes() != null ? SchemaUtil.formatBytes(b.avgBytes()) : "N/A",
                    b.maxBytes() != null ? SchemaUtil.formatBytes(b.maxBytes()) : "N/A"));
        }
        sb.append("\n");

        sb.append("## Metaspace Usage Trend\n\n");
        sb.append("| Time | Used Min | Used Avg | Used Max | Committed Min | Committed Avg | Committed Max |\n");
        sb.append("|------|----------|----------|----------|---------------|---------------|---------------|\n");
        for (MetaspaceBucketEntry b : result.metaspaceBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s | %s | %s | %s |%n",
                    SchemaUtil.formatTime(b.bucketStartMs()),
                    b.minUsedBytes() != null ? SchemaUtil.formatBytes(b.minUsedBytes()) : "N/A",
                    b.avgUsedBytes() != null ? SchemaUtil.formatBytes(b.avgUsedBytes()) : "N/A",
                    b.maxUsedBytes() != null ? SchemaUtil.formatBytes(b.maxUsedBytes()) : "N/A",
                    b.minCommittedBytes() != null ? SchemaUtil.formatBytes(b.minCommittedBytes()) : "N/A",
                    b.avgCommittedBytes() != null ? SchemaUtil.formatBytes(b.avgCommittedBytes()) : "N/A",
                    b.maxCommittedBytes() != null ? SchemaUtil.formatBytes(b.maxCommittedBytes()) : "N/A"));
        }
        sb.append("\n");

        sb.append("## Thread Count Trend\n\n");
        sb.append("| Time | Min | Avg | Max |\n");
        sb.append("|------|-----|-----|-----|\n");
        for (ThreadBucketEntry b : result.threadBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s |%n",
                    SchemaUtil.formatTime(b.bucketStartMs()),
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
        return value != null ? SchemaUtil.formatBytes(value) : "N/A";
    }
}
