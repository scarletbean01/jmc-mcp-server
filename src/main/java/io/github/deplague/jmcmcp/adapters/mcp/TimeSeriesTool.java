package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.TimeSeriesApplicationService;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesBucketEntry;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * MCP tool adapter for time-series performance trend analysis.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class TimeSeriesTool implements McpTool {

    private static final String NAME = "time_series";

    private final TimeSeriesApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze performance trends over time in a JFR recording. "
                                                + "Buckets metrics like CPU load, GC pause duration, "
                                                + "and allocation rate by time intervals."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "bucket_size",
                                                        SchemaUtil.stringProp(
                                                                "Interval bucket size (e.g., '10s', '1m', '5m'). Default is '1m'."
                                                        ),
                                                        "metric",
                                                        SchemaUtil.stringProp(
                                                                "Metric to filter by (cpu, gc, alloc, all). Default is 'all'."
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "start_time",
                                null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "end_time",
                                null
                        );
                        String bucketSizeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "bucket_size",
                                "1m"
                        );
                        String metricFilter = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "metric",
                                "all"
                        ).toLowerCase();

                        TimeSeriesResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                bucketSizeStr,
                                metricFilter
                        );

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
                    .append(SchemaUtil.formatTime(b.bucketStartMs()))
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
                        .append(SchemaUtil.formatDuration(gcMs))
                        .append(" ");
            }
            if (result.showAlloc()) {
                long alloc = b.allocSumBytes() != null
                        ? b.allocSumBytes()
                        : 0L;
                sb.append("| ")
                        .append(SchemaUtil.formatBytes(alloc))
                        .append(" ");
            }
            sb.append("|\n");
        }

        return sb.toString();
    }
}
