package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

import java.io.IOException;

public final class HeapTrendsTool {

    private static final String NAME = "heap_trends";

    private final JfrAnalysisService service;

    public HeapTrendsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze heap, metaspace, and thread count trends over time in a JFR recording. " +
                                "Buckets memory usage by time intervals to detect memory leaks and growth patterns.")
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

        long startMillis = startQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
        long endMillis = endQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
        long bucketMillis = SchemaUtil.parseDuration(bucketSizeStr).toMillis();

        if (bucketMillis <= 0) bucketMillis = 60_000L;

        int numBuckets = (int) Math.ceil((double) (endMillis - startMillis) / bucketMillis);
        if (numBuckets > 500) {
            bucketMillis = (endMillis - startMillis) / 500;
            numBuckets = 500;
        }

        Bucket[] buckets = new Bucket[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new Bucket(startMillis + i * bucketMillis);
        }

        processHeapSummary(events, startMillis, bucketMillis, buckets);
        processMetaspaceSummary(events, startMillis, bucketMillis, buckets);
        processThreadStats(events, startMillis, bucketMillis, buckets);

        StringBuilder sb = new StringBuilder();
        sb.append("# Heap & Memory Trends (Bucket Size: ").append(SchemaUtil.formatDuration(bucketMillis)).append(")\n\n");

        sb.append("## Heap Usage Trend\n\n");
        sb.append("| Time | Heap Used | Heap Size | Utilization |\n");
        sb.append("|------|-----------|-----------|-------------|\n");
        for (Bucket b : buckets) {
            if (b.heapCount > 0) {
                double util = b.heapSizeSum > 0 ? (b.heapUsedSum * 100.0 / b.heapSizeSum) : 0;
                sb.append(String.format("| %s | %s | %s | %.1f%% |%n",
                        SchemaUtil.formatTime(b.startTime),
                        SchemaUtil.formatBytes((long) (b.heapUsedSum / b.heapCount)),
                        SchemaUtil.formatBytes((long) (b.heapSizeSum / b.heapCount)),
                        util));
            }
        }
        sb.append("\n");

        sb.append("## Metaspace Usage Trend\n\n");
        sb.append("| Time | Metaspace Used | Metaspace Committed |\n");
        sb.append("|------|----------------|---------------------|\n");
        for (Bucket b : buckets) {
            if (b.metaCount > 0) {
                sb.append(String.format("| %s | %s | %s |%n",
                        SchemaUtil.formatTime(b.startTime),
                        SchemaUtil.formatBytes((long) (b.metaUsedSum / b.metaCount)),
                        SchemaUtil.formatBytes((long) (b.metaCommittedSum / b.metaCount))));
            }
        }
        sb.append("\n");

        sb.append("## Thread Count Trend\n\n");
        sb.append("| Time | Active | Peak | Daemon |\n");
        sb.append("|------|--------|------|--------|\n");
        for (Bucket b : buckets) {
            if (b.threadCount > 0) {
                sb.append(String.format("| %s | %d | %d | %d |%n",
                        SchemaUtil.formatTime(b.startTime),
                        b.activeThreadSum / b.threadCount,
                        b.peakThreadMax,
                        b.daemonThreadSum / b.threadCount));
            }
        }
        sb.append("\n");

        appendSummaryStats(sb, events);

        return sb.toString();
    }

    private void processHeapSummary(IItemCollection events, long startMillis, long bucketMillis, Bucket[] buckets) {
        IItemCollection heapEvents = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        for (IItemIterable iterable : heapEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> usedAccessor = JfrItemUtils.getAccessor(iterable.getType(), "heapUsed");
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "heapSize");

            if (timeAccessor != null && usedAccessor != null && sizeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    if (timeQ == null) continue;
                    long time = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                    int idx = (int) ((time - startMillis) / bucketMillis);
                    if (idx >= 0 && idx < buckets.length) {
                        IQuantity used = usedAccessor.getMember(item);
                        IQuantity size = sizeAccessor.getMember(item);
                        if (used != null) buckets[idx].heapUsedSum += used.longValue();
                        if (size != null) buckets[idx].heapSizeSum += size.longValue();
                        buckets[idx].heapCount++;
                    }
                }
            }
        }
    }

    private void processMetaspaceSummary(IItemCollection events, long startMillis, long bucketMillis, Bucket[] buckets) {
        IItemCollection metaEvents = events.apply(ItemFilters.type("jdk.MetaspaceSummary"));
        for (IItemIterable iterable : metaEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> usedAccessor = JfrItemUtils.getAccessor(iterable.getType(), "metaspace.used");
            IMemberAccessor<IQuantity, IItem> committedAccessor = JfrItemUtils.getAccessor(iterable.getType(), "metaspace.committed");

            if (timeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    if (timeQ == null) continue;
                    long time = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                    int idx = (int) ((time - startMillis) / bucketMillis);
                    if (idx >= 0 && idx < buckets.length) {
                        if (usedAccessor != null) {
                            IQuantity used = usedAccessor.getMember(item);
                            if (used != null) buckets[idx].metaUsedSum += used.longValue();
                        }
                        if (committedAccessor != null) {
                            IQuantity committed = committedAccessor.getMember(item);
                            if (committed != null) buckets[idx].metaCommittedSum += committed.longValue();
                        }
                        buckets[idx].metaCount++;
                    }
                }
            }
        }
    }

    private void processThreadStats(IItemCollection events, long startMillis, long bucketMillis, Bucket[] buckets) {
        IItemCollection threadEvents = events.apply(ItemFilters.type("jdk.JavaThreadStatistics"));
        for (IItemIterable iterable : threadEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> activeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "activeCount");
            IMemberAccessor<IQuantity, IItem> peakAccessor = JfrItemUtils.getAccessor(iterable.getType(), "peakCount");
            IMemberAccessor<IQuantity, IItem> daemonAccessor = JfrItemUtils.getAccessor(iterable.getType(), "daemonCount");

            if (timeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    if (timeQ == null) continue;
                    long time = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                    int idx = (int) ((time - startMillis) / bucketMillis);
                    if (idx >= 0 && idx < buckets.length) {
                        if (activeAccessor != null) {
                            IQuantity active = activeAccessor.getMember(item);
                            if (active != null) buckets[idx].activeThreadSum += active.longValue();
                        }
                        if (peakAccessor != null) {
                            IQuantity peak = peakAccessor.getMember(item);
                            if (peak != null) buckets[idx].peakThreadMax = Math.max(buckets[idx].peakThreadMax, peak.longValue());
                        }
                        if (daemonAccessor != null) {
                            IQuantity daemon = daemonAccessor.getMember(item);
                            if (daemon != null) buckets[idx].daemonThreadSum += daemon.longValue();
                        }
                        buckets[idx].threadCount++;
                    }
                }
            }
        }
    }

    private void appendSummaryStats(StringBuilder sb, IItemCollection events) {
        sb.append("## Summary Statistics\n\n");
        sb.append("| Metric | Min | Avg | Max | P95 |\n");
        sb.append("|--------|-----|-----|-----|-----|\n");

        IItemCollection heapEvents = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        if (heapEvents.hasItems()) {
            IQuantity minHeap = JfrItemUtils.minQuantity(heapEvents, "heapUsed");
            IQuantity avgHeap = JfrItemUtils.avgQuantity(heapEvents, "heapUsed");
            IQuantity maxHeap = JfrItemUtils.maxQuantity(heapEvents, "heapUsed");
            IQuantity p95Heap = JfrItemUtils.percentileQuantity(heapEvents, "heapUsed", 95);
            sb.append(String.format("| Heap Used | %s | %s | %s | %s |%n",
                    JfrAnalysisService.display(minHeap), JfrAnalysisService.display(avgHeap),
                    JfrAnalysisService.display(maxHeap), JfrAnalysisService.display(p95Heap)));
        }

        IItemCollection metaEvents = events.apply(ItemFilters.type("jdk.MetaspaceSummary"));
        if (metaEvents.hasItems()) {
            IQuantity minMeta = JfrItemUtils.minQuantity(metaEvents, "metaspace.used");
            IQuantity avgMeta = JfrItemUtils.avgQuantity(metaEvents, "metaspace.used");
            IQuantity maxMeta = JfrItemUtils.maxQuantity(metaEvents, "metaspace.used");
            IQuantity p95Meta = JfrItemUtils.percentileQuantity(metaEvents, "metaspace.used", 95);
            sb.append(String.format("| Metaspace Used | %s | %s | %s | %s |%n",
                    JfrAnalysisService.display(minMeta), JfrAnalysisService.display(avgMeta),
                    JfrAnalysisService.display(maxMeta), JfrAnalysisService.display(p95Meta)));
        }

        IItemCollection threadEvents = events.apply(ItemFilters.type("jdk.JavaThreadStatistics"));
        if (threadEvents.hasItems()) {
            IQuantity minThreads = JfrItemUtils.minQuantity(threadEvents, "activeCount");
            IQuantity avgThreads = JfrItemUtils.avgQuantity(threadEvents, "activeCount");
            IQuantity maxThreads = JfrItemUtils.maxQuantity(threadEvents, "activeCount");
            IQuantity p95Threads = JfrItemUtils.percentileQuantity(threadEvents, "activeCount", 95);
            sb.append(String.format("| Active Threads | %s | %s | %s | %s |%n",
                    JfrAnalysisService.display(minThreads), JfrAnalysisService.display(avgThreads),
                    JfrAnalysisService.display(maxThreads), JfrAnalysisService.display(p95Threads)));
        }
    }

    private static class Bucket {
        final long startTime;
        double heapUsedSum = 0;
        double heapSizeSum = 0;
        int heapCount = 0;
        double metaUsedSum = 0;
        double metaCommittedSum = 0;
        int metaCount = 0;
        long activeThreadSum = 0;
        long peakThreadMax = 0;
        long daemonThreadSum = 0;
        int threadCount = 0;

        Bucket(long startTime) {
            this.startTime = startTime;
        }
    }
}