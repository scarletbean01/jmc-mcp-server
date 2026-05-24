package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.deplague.jmcmcp.tools.SchemaUtil.formatBytes;

/**
 * MCP tool for providing per-thread allocation breakdown.
 */
public final class ThreadAllocationTool {

    private static final String NAME = "thread_allocation";

    private final JfrAnalysisService service;

    public ThreadAllocationTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Identify which threads are allocating the most memory based on thread allocation statistics.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top hot threads to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection allocStats = events.apply(ItemFilters.type("jdk.ThreadAllocationStatistics"));
        if (!allocStats.hasItems()) {
            return "# Thread Allocation Analysis\n\nNo thread allocation statistics found in the recording. JFR event 'jdk.ThreadAllocationStatistics' may not be enabled.";
        }

        Map<String, ThreadAllocStats> threadStatsMap = new HashMap<>();

        for (IItemIterable iterable : allocStats) {
            IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> allocatedAccessor = JfrItemUtils.getAccessor(iterable.getType(), "allocated");
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());

            if (threadAccessor != null && allocatedAccessor != null) {
                for (IItem item : iterable) {
                    Object threadObj = threadAccessor.getMember(item);
                    IQuantity allocatedQ = allocatedAccessor.getMember(item);
                    IQuantity timeQ = timeAccessor != null ? timeAccessor.getMember(item) : null;

                    if (threadObj != null && allocatedQ != null) {
                        String threadName = threadObj.toString();
                        long allocated = allocatedQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.BYTE);
                        long timeNanos = timeQ != null ? timeQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND) : 0;

                        ThreadAllocStats stats = threadStatsMap.computeIfAbsent(threadName, k -> new ThreadAllocStats());
                        stats.update(allocated, timeNanos);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Allocation Analysis\n\n");

        sb.append("## Top Allocating Threads\n\n");
        sb.append("| Thread Name | Total Allocated (Estimated) | Allocation Rate |\n");
        sb.append("|-------------|-----------------------------|-----------------|\n");

        threadStatsMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().maxAllocated - b.getValue().minAllocated, a.getValue().maxAllocated - a.getValue().minAllocated))
                .limit(topN)
                .forEach(e -> {
                    ThreadAllocStats s = e.getValue();
                    long diff = s.maxAllocated - s.minAllocated;
                    String rate = "N/A";
                    if (s.maxTime > s.minTime) {
                        double seconds = (s.maxTime - s.minTime) / 1_000_000_000.0;
                        if (seconds > 0) {
                            rate = formatBytes((long) (diff / seconds)) + "/s";
                        }
                    }
                    sb.append("| ").append(e.getKey()).append(" | ")
                            .append(formatBytes(diff)).append(" | ")
                            .append(rate).append(" |\n");
                });

        return sb.toString();
    }

    private static class ThreadAllocStats {
        long minAllocated = Long.MAX_VALUE;
        long maxAllocated = Long.MIN_VALUE;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;

        void update(long allocated, long timeNanos) {
            if (allocated < minAllocated) {
                minAllocated = allocated;
                minTime = timeNanos;
            }
            if (allocated > maxAllocated) {
                maxAllocated = allocated;
                maxTime = timeNanos;
            }
        }
    }
}
