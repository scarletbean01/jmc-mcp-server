package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.TimeSeriesBucketEntry;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

/**
 * Pure domain service for time-series performance trend analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class TimeSeriesService {

    public TimeSeriesResult analyze(
            IItemCollection events,
            String bucketSizeStr,
            String metricFilter) {

        IQuantity startQ = RulesToolkit.getEarliestStartTime(events);
        IQuantity endQ = RulesToolkit.getLatestEndTime(events);

        if (startQ == null || endQ == null) {
            return new TimeSeriesResult(
                    formatDuration(60_000L),
                    "",
                    List.of(),
                    false,
                    false,
                    false
            );
        }

        long startMillis = startQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
        long endMillis = endQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
        long bucketMillis = parseDuration(bucketSizeStr).toMillis();

        if (bucketMillis <= 0) {
            bucketMillis = 60_000L;
        }

        String warning = "";
        int numBuckets = (int) Math.ceil(
                (double) (endMillis - startMillis) / bucketMillis
        );
        if (numBuckets > 500) {
            long newBucketMillis = (endMillis - startMillis) / 500;
            warning = String.format(
                    "> **Warning:** The requested bucket size '%s' would result in %d buckets, "
                            + "exceeding the maximum limit of 500. The bucket size has been "
                            + "automatically adjusted to %s to maintain performance and readability.\n\n",
                    bucketSizeStr,
                    numBuckets,
                    formatDuration(newBucketMillis)
            );
            bucketMillis = newBucketMillis;
            numBuckets = 500;
        }

        Bucket[] buckets = new Bucket[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new Bucket(startMillis + i * bucketMillis);
        }

        boolean showAll = "all".equals(metricFilter);
        boolean showCpu = showAll || "cpu".equals(metricFilter);
        boolean showGc = showAll || "gc".equals(metricFilter);
        boolean showAlloc = showAll || "alloc".equals(metricFilter);

        if (showCpu) {
            processMetric(
                    events,
                    "jdk.CPULoad",
                    "machineTotal",
                    startMillis,
                    bucketMillis,
                    buckets,
                    MetricType.AVERAGE
            );
        }
        if (showGc) {
            processMetric(
                    events,
                    "jdk.GCPhasePause",
                    JfrAttributes.DURATION.getIdentifier(),
                    startMillis,
                    bucketMillis,
                    buckets,
                    MetricType.SUM
            );
        }
        if (showAlloc) {
            processMetric(
                    events,
                    "jdk.ObjectAllocationInNewTLAB",
                    "tlabSize",
                    startMillis,
                    bucketMillis,
                    buckets,
                    MetricType.SUM
            );
            processMetric(
                    events,
                    "jdk.ObjectAllocationOutsideTLAB",
                    "allocationSize",
                    startMillis,
                    bucketMillis,
                    buckets,
                    MetricType.SUM
            );
        }

        List<TimeSeriesBucketEntry> entries = new ArrayList<>();
        for (Bucket b : buckets) {
            Double cpuAvg = showCpu
                    ? (b.cpuCount == 0 ? 0.0 : b.cpuSum / b.cpuCount)
                    : null;
            Long gcPauseSum = showGc ? b.gcPauseSum : null;
            Long allocSum = showAlloc ? b.allocSum : null;
            entries.add(new TimeSeriesBucketEntry(
                    b.startTime,
                    cpuAvg,
                    gcPauseSum,
                    allocSum
            ));
        }

        return new TimeSeriesResult(
                formatDuration(bucketMillis),
                warning,
                entries,
                showCpu,
                showGc,
                showAlloc
        );
    }

    private void processMetric(
            IItemCollection events,
            String typeId,
            String attrId,
            long startMillis,
            long bucketMillis,
            Bucket[] buckets,
            MetricType mType) {

        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable itemIterable : filtered) {
            IMemberAccessor<IQuantity, IItem> timeAccessor =
                    JfrAttributes.START_TIME.getAccessor(itemIterable.getType());
            IMemberAccessor<IQuantity, IItem> valAccessor =
                    JfrItemUtils.getAccessor(itemIterable.getType(), attrId);

            if (timeAccessor != null && valAccessor != null) {
                for (IItem item : itemIterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    IQuantity valQ = valAccessor.getMember(item);
                    if (timeQ != null && valQ != null) {
                        long time = timeQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                        int bucketIdx = (int) ((time - startMillis) / bucketMillis);
                        if (bucketIdx >= 0 && bucketIdx < buckets.length) {
                            double val = valQ.doubleValue();
                            if (mType == MetricType.SUM) {
                                if (typeId.contains("Allocation")) {
                                    buckets[bucketIdx].allocSum += (long) val;
                                } else if (typeId.contains("GC")) {
                                    buckets[bucketIdx].gcPauseSum += (long) val;
                                }
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

    private static Duration parseDuration(String s) {
        if (s == null || s.isEmpty()) {
            return Duration.ofMinutes(1);
        }
        try {
            long value = Long.parseLong(s.substring(0, s.length() - 1));
            if (s.endsWith("s")) {
                return Duration.ofSeconds(value);
            }
            if (s.endsWith("m")) {
                return Duration.ofMinutes(value);
            }
            if (s.endsWith("h")) {
                return Duration.ofHours(value);
            }
            return Duration.ofMinutes(Long.parseLong(s));
        } catch (Exception e) {
            return Duration.ofMinutes(1);
        }
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        }
        return (millis / 1000.0) + "s";
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

    enum MetricType {
        SUM, AVERAGE
    }
}
