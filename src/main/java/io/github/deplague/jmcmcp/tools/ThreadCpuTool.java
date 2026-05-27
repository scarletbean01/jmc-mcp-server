package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.openjdk.jmc.common.item.*;

/**
 * MCP tool for providing per-thread CPU breakdown from execution samples.
 */
public final class ThreadCpuTool {

    private static final String NAME = "thread_cpu";

    private final JfrAnalysisService service;

    public ThreadCpuTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
            .tool(
                McpSchema.Tool.builder()
                    .name(NAME)
                    .description(
                        "Identify which threads are consuming the most CPU based on execution samples."
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
                                "package_prefix",
                                SchemaUtil.stringProp(
                                    "Optional package prefix to filter stack traces (e.g., 'com.mycompany')"
                                ),
                                "top_n",
                                SchemaUtil.intProp(
                                    "Number of top hot threads to return (default 10)",
                                    10
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
                    String packagePrefix = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "package_prefix",
                        null
                    );
                    int topN = SchemaUtil.getIntOrDefault(
                        request.arguments(),
                        "top_n",
                        10
                    );

                    String cached = service.getCachedResult(
                        filePath,
                        NAME,
                        request.arguments()
                    );
                    if (cached != null) {
                        return CallToolResult.builder()
                            .addTextContent(cached)
                            .isError(false)
                            .build();
                    }

                    String result = analyze(
                        filePath,
                        startTimeStr,
                        endTimeStr,
                        packagePrefix,
                        topN
                    );
                    service.cacheResult(
                        filePath,
                        NAME,
                        request.arguments(),
                        result
                    );
                    return CallToolResult.builder()
                        .addTextContent(result)
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

    public String analyze(
        String filePath,
        String startTimeStr,
        String endTimeStr,
        String packagePrefix,
        int topN
    ) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(
            allEvents,
            startTimeStr,
            endTimeStr
        );

        IItemCollection samples = events.apply(
            ItemFilters.type("jdk.ExecutionSample")
        );
        if (!samples.hasItems()) {
            return "# Thread CPU Analysis\n\nNo execution samples found in the recording.";
        }

        Map<String, ThreadStats> threadStatsMap = new HashMap<>();
        Map<String, Long> stateCounts = new HashMap<>();
        long totalSamples = 0;
        JfrItemUtils.StackTraceFormatCache stCache =
            JfrItemUtils.newStackTraceFormatCache();

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> threadAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");
            IMemberAccessor<Object, IItem> stackAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            IMemberAccessor<String, IItem> stateAccessor =
                JfrItemUtils.getAccessor(iterable.getType(), "state");

            if (threadAccessor != null) {
                for (IItem item : iterable) {
                    Object threadObj = threadAccessor.getMember(item);
                    String threadName =
                        threadObj != null ? threadObj.toString() : "Unknown";

                    ThreadStats stats = threadStatsMap.computeIfAbsent(
                        threadName,
                        k -> new ThreadStats(k)
                    );
                    stats.samples++;
                    totalSamples++;

                    if (stateAccessor != null) {
                        String state = stateAccessor.getMember(item);
                        if (state != null) {
                            stats.stateCounts.merge(state, 1L, Long::sum);
                            stateCounts.merge(state, 1L, Long::sum);
                        }
                    }

                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) {
                            String topFrame = stCache
                                .formatFocusingOn(st, 1, packagePrefix)
                                .trim();
                            if (!topFrame.isEmpty()) {
                                stats.methodCounts.merge(
                                    topFrame,
                                    1L,
                                    Long::sum
                                );
                            }
                        }
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread CPU Analysis\n\n");

        sb.append("Total samples: ").append(totalSamples).append("\n\n");

        sb.append("## Per-Thread CPU Summary\n\n");
        sb.append("| Thread Name | Samples | CPU % | Primary States |\n");
        sb.append("|-------------|---------|-------|----------------|\n");

        long finalTotalSamples = totalSamples;
        var sortedThreads = threadStatsMap
            .values()
            .stream()
            .sorted((a, b) -> Long.compare(b.samples, a.samples))
            .limit(topN)
            .collect(Collectors.toList());

        for (ThreadStats stats : sortedThreads) {
            double pct = (stats.samples * 100.0) / finalTotalSamples;
            String states = stats.stateCounts
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(2)
                .map(e -> String.format("%s (%d)", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", "));

            sb.append("| ")
                .append(stats.name)
                .append(" | ")
                .append(stats.samples)
                .append(" | ")
                .append(String.format("%.2f%%", pct))
                .append(" | ")
                .append(states)
                .append(" |\n");
        }

        sb.append("\n## Top Methods per Thread\n\n");
        for (ThreadStats stats : sortedThreads) {
            sb.append("### ").append(stats.name).append("\n\n");
            sb.append("| Samples | Method (Top Frame) |\n");
            sb.append("|---------|--------------------|\n");

            stats.methodCounts
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    sb.append("| ")
                        .append(entry.getValue())
                        .append(" | `")
                        .append(entry.getKey())
                        .append("` |\n");
                });
            sb.append("\n");
        }

        sb.append("## Thread State Distribution\n\n");
        sb.append("| State | Samples | Percentage |\n");
        sb.append("|-------|---------|------------|\n");
        stateCounts
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> {
                double pct = (entry.getValue() * 100.0) / finalTotalSamples;
                sb.append("| ")
                    .append(entry.getKey())
                    .append(" | ")
                    .append(entry.getValue())
                    .append(" | ")
                    .append(String.format("%.2f%%", pct))
                    .append(" |\n");
            });

        return sb.toString();
    }

    private static class ThreadStats {

        final String name;
        long samples = 0;
        final Map<String, Long> stateCounts = new HashMap<>();
        final Map<String, Long> methodCounts = new HashMap<>();

        ThreadStats(String name) {
            this.name = name;
        }
    }
}
