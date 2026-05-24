package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static io.github.deplague.jmcmcp.tools.SchemaUtil.formatBytes;

/**
 * MCP tool for off-heap direct buffer statistics analysis.
 */
public final class DirectBuffersTool {

    private static final String NAME = "direct_buffers";

    private final JfrAnalysisService service;

    public DirectBuffersTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze off-heap direct buffer statistics to detect potential memory leaks.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.commonJfrProps(),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection dbEvents = events.apply(ItemFilters.type("jdk.DirectBufferStatistics"));

        if (!dbEvents.hasItems()) {
            return "# Direct Buffer Statistics\n\nNo direct buffer statistics events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Direct Buffer Statistics\n\n");

        IQuantity maxCount = JfrItemUtils.maxQuantity(dbEvents, "directBufferCount");
        IQuantity minCount = JfrItemUtils.minQuantity(dbEvents, "directBufferCount");
        IQuantity avgCount = JfrItemUtils.avgQuantity(dbEvents, "directBufferCount");

        IQuantity maxCapacity = JfrItemUtils.maxQuantity(dbEvents, "directTotalCapacity");
        IQuantity maxUsed = JfrItemUtils.maxQuantity(dbEvents, "directMemoryUsed");

        sb.append("## Direct Buffer Summary\n\n");
        sb.append("- **Buffer Count:** ").append(JfrAnalysisService.display(minCount))
                .append(" (Min) / ").append(JfrAnalysisService.display(avgCount))
                .append(" (Avg) / ").append(JfrAnalysisService.display(maxCount))
                .append(" (Max)\n");
        sb.append("- **Max Total Capacity:** ").append(JfrAnalysisService.display(maxCapacity)).append("\n");
        sb.append("- **Max Memory Used:** ").append(JfrAnalysisService.display(maxUsed)).append("\n\n");

        // Try to get MaxDirectMemorySize
        IItemCollection props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        long maxDirectMemorySize = -1;
        for (IItemIterable iterable : props) {
            var keyAcc = JfrItemUtils.getAccessor(iterable.getType(), "key");
            var valueAcc = JfrItemUtils.getAccessor(iterable.getType(), "value");
            if (keyAcc != null && valueAcc != null) {
                for (IItem item : iterable) {
                    Object key = keyAcc.getMember(item);
                    if ("sun.nio.MaxDirectMemorySize".equals(key)) {
                        Object val = valueAcc.getMember(item);
                        if (val != null) {
                            try {
                                maxDirectMemorySize = Long.parseLong(val.toString());
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        }

        if (maxDirectMemorySize > 0) {
            sb.append("## Memory Pressure Warning\n\n");
            sb.append("- **Configured MaxDirectMemorySize:** ").append(formatBytes(maxDirectMemorySize)).append("\n");
            if (maxUsed != null) {
                long usedBytes = maxUsed.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.BYTE);
                double util = (double) usedBytes / maxDirectMemorySize * 100.0;
                sb.append(String.format("- **Max Utilization:** %.2f%%\n", util));
                if (util > 90) {
                    sb.append("\n**⚠️ WARNING:** Direct memory utilization exceeded 90%. Risk of OutOfMemoryError: Direct buffer memory.\n");
                }
            }
            sb.append("\n");
        }

        sb.append("## Direct Buffer Trend\n\n");
        sb.append("| Time | Buffer Count | Total Capacity | Memory Used |\n");
        sb.append("|------|--------------|----------------|-------------|\n");

        Map<Long, BufferSample> trend = new TreeMap<>();
        for (IItemIterable iterable : dbEvents) {
            var timeAcc = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            var countAcc = JfrItemUtils.getAccessor(iterable.getType(), "directBufferCount");
            var capAcc = JfrItemUtils.getAccessor(iterable.getType(), "directTotalCapacity");
            var usedAcc = JfrItemUtils.getAccessor(iterable.getType(), "directMemoryUsed");

            if (timeAcc != null) {
                for (IItem item : iterable) {
                    IQuantity time = timeAcc.getMember(item);
                    if (time != null) {
                        long ms = time.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS);
                        long count = countAcc != null ? ((IQuantity)countAcc.getMember(item)).longValue() : 0;
                        long cap = capAcc != null ? ((IQuantity)capAcc.getMember(item)).clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.BYTE) : 0;
                        long used = usedAcc != null ? ((IQuantity)usedAcc.getMember(item)).clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.BYTE) : 0;
                        trend.put(ms, new BufferSample(count, cap, used));
                    }
                }
            }
        }

        // Output samples (bucketed or all if not too many)
        int maxRows = 20;
        int step = Math.max(1, trend.size() / maxRows);
        int i = 0;
        for (Map.Entry<Long, BufferSample> e : trend.entrySet()) {
            if (i % step == 0 || i == trend.size() - 1) {
                BufferSample s = e.getValue();
                sb.append("| ").append(SchemaUtil.formatTime(e.getKey())).append(" | ")
                        .append(s.count).append(" | ")
                        .append(formatBytes(s.capacity)).append(" | ")
                        .append(formatBytes(s.used)).append(" |\n");
            }
            i++;
        }

        return sb.toString();
    }

    private record BufferSample(long count, long capacity, long used) {}
}
