package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static io.github.deplague.jmcmcp.tools.SchemaUtil.formatBytes;

/**
 * MCP tool for time-series trend analysis (CPU, GC, Allocation).
 */
public final class TimeSeriesTool {

    private static final String NAME = "time_series";

    private final JfrAnalysisService service;

    public TimeSeriesTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze performance trends over time in a JFR recording. " +
                                "Buckets metrics like CPU load, GC pause duration, and allocation rate by time intervals.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "bucket_size", SchemaUtil.stringProp("Interval bucket size (e.g., '10s', '1m', '5m'). Default is '1m'.")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String bucketSizeStr = SchemaUtil.getStringOrDefault(request.arguments(), "bucket_size", "1m");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, bucketSizeStr);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, String bucketSizeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IQuantity startQ = RulesToolkit.getEarliestStartTime(events);
        IQuantity endQ = RulesToolkit.getLatestEndTime(events);

        if (startQ == null || endQ == null) {
            return "No events found in the specified time range.";
        }

        long startMillis = startQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS);
        long endMillis = endQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS);
        long bucketMillis = parseDuration(bucketSizeStr).toMillis();

        if (bucketMillis <= 0) bucketMillis = 60_000L; // default 1m

        int numBuckets = (int) Math.ceil((double) (endMillis - startMillis) / bucketMillis);
        if (numBuckets > 500) {
            // Cap at 500 buckets to prevent massive output
            bucketMillis = (endMillis - startMillis) / 500;
            numBuckets = 500;
        }

        Bucket[] buckets = new Bucket[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new Bucket(startMillis + i * bucketMillis);
        }

        // Process CPU Load
        processMetric(events, "jdk.CPULoad", "machineTotal", startMillis, bucketMillis, buckets, MetricType.AVERAGE);
        // Process GC Pauses
        processMetric(events, "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), startMillis, bucketMillis, buckets, MetricType.SUM);
        // Process Allocations
        processMetric(events, "jdk.ObjectAllocationInNewTLAB", "tlabSize", startMillis, bucketMillis, buckets, MetricType.SUM);
        processMetric(events, "jdk.ObjectAllocationOutsideTLAB", "allocationSize", startMillis, bucketMillis, buckets, MetricType.SUM);

        StringBuilder sb = new StringBuilder();
        sb.append("# Performance Trends (Bucket Size: ").append(formatDuration(bucketMillis)).append(")\n\n");
        sb.append("| Time | Avg CPU Load | GC Pause Sum | Total Allocation |\n");
        sb.append("|------|--------------|--------------|------------------|\n");

        for (Bucket b : buckets) {
            sb.append(String.format("| %s | %.2f%% | %s | %s |%n",
                    formatTime(b.startTime),
                    b.cpuSum / (b.cpuCount == 0 ? 1 : b.cpuCount) * 100,
                    formatDuration(b.gcPauseSum / 1_000_000L),
                    formatBytes(b.allocSum)));
        }

        return sb.toString();
    }

    private void processMetric(IItemCollection events, String typeId, String attrId, long startMillis, long bucketMillis, Bucket[] buckets, MetricType mType) {
        var filtered = events.apply(ItemFilters.type(typeId));
        for (var itemIterable : filtered) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(itemIterable.getType());
            IMemberAccessor<IQuantity, IItem> valAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), attrId);

            if (timeAccessor != null && valAccessor != null) {
                for (IItem item : itemIterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    IQuantity valQ = valAccessor.getMember(item);
                    if (timeQ != null && valQ != null) {
                        long time = timeQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS);
                        int bucketIdx = (int) ((time - startMillis) / bucketMillis);
                        if (bucketIdx >= 0 && bucketIdx < buckets.length) {
                            double val = valQ.doubleValue();
                            if (mType == MetricType.SUM) {
                                if (typeId.contains("Allocation")) buckets[bucketIdx].allocSum += (long) val;
                                else if (typeId.contains("GC")) buckets[bucketIdx].gcPauseSum += (long) val;
                            } else {
                                buckets[bucketIdx].cpuSum += val;
                                buckets[bucketIdx].cpuCount++;
                            }
                        }
                    }
                }
            }
        }
    }

    private static class Bucket {
        final long startTime;
        double cpuSum = 0;
        int cpuCount = 0;
        long gcPauseSum = 0;
        long allocSum = 0;

        Bucket(long startTime) {
            this.startTime = startTime;
        }
    }

    enum MetricType {SUM, AVERAGE}

    private static Duration parseDuration(String s) {
        if (s == null || s.isEmpty()) return Duration.ofMinutes(1);
        try {
            if (s.endsWith("s")) return Duration.ofSeconds(Long.parseLong(s.substring(0, s.length() - 1)));
            if (s.endsWith("m")) return Duration.ofMinutes(Long.parseLong(s.substring(0, s.length() - 1)));
            if (s.endsWith("h")) return Duration.ofHours(Long.parseLong(s.substring(0, s.length() - 1)));
            return Duration.ofMinutes(Long.parseLong(s));
        } catch (Exception e) {
            return Duration.ofMinutes(1);
        }
    }

    private static String formatTime(long millis) {
        return Instant.ofEpochMilli(millis).toString().substring(11, 19);
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        return (millis / 1000.0) + "s";
    }


}
