package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.TimeSeriesBucketEntry;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.domain.service.TimeSeriesService.MetricType.AVERAGE;
import static io.github.deplague.jmcmcp.domain.service.TimeSeriesService.MetricType.SUM;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static java.lang.Long.parseLong;
import static java.lang.Math.ceil;
import static java.lang.String.format;
import static java.time.Duration.*;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;
import static org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.getEarliestStartTime;
import static org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.getLatestEndTime;

/**
 * Pure domain service for time-series performance trend analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class TimeSeriesService {

    public TimeSeriesResult analyze(
            IItemCollection events,
            String bucketSizeStr,
            String metricFilter) {

        IQuantity startQ = getEarliestStartTime(events);
        IQuantity endQ = getLatestEndTime(events);

        if (startQ == null || endQ == null) {
            return new TimeSeriesResult(
                    formatDuration(60_000L),
                    "",
                    of(),
                    false,
                    false,
                    false
            );
        }

        long startMillis = startQ.clampedLongValueIn(EPOCH_MS);
        long endMillis = endQ.clampedLongValueIn(EPOCH_MS);
        long bucketMillis = parseDuration(bucketSizeStr).toMillis();

        if (bucketMillis <= 0) {
            bucketMillis = 60_000L;
        }

        String warning = "";
        int numBuckets = (int) ceil(
                (double) (endMillis - startMillis) / bucketMillis
        );
        if (numBuckets > 500) {
            long newBucketMillis = (endMillis - startMillis) / 500;
            warning = format(
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
                    AVERAGE
            );
        }
        if (showGc) {
            processMetric(
                    events,
                    "jdk.GCPhasePause",
                    DURATION.getIdentifier(),
                    startMillis,
                    bucketMillis,
                    buckets,
                    SUM
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
                    SUM
            );
            processMetric(
                    events,
                    "jdk.ObjectAllocationOutsideTLAB",
                    "allocationSize",
                    startMillis,
                    bucketMillis,
                    buckets,
                    SUM
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

        IItemCollection filtered = events.apply(type(typeId));
        for (IItemIterable itemIterable : filtered) {
            IMemberAccessor<IQuantity, IItem> timeAccessor =
                    START_TIME.getAccessor(itemIterable.getType());
            IType<?> type = itemIterable.getType();
            IMemberAccessor<IQuantity, IItem> valAccessor =
                    getAccessor(type, attrId);

            if (timeAccessor != null && valAccessor != null) {
                for (IItem item : itemIterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    IQuantity valQ = valAccessor.getMember(item);
                    if (timeQ != null && valQ != null) {
                        long time = timeQ.clampedLongValueIn(EPOCH_MS);
                        int bucketIdx = (int) ((time - startMillis) / bucketMillis);
                        if (bucketIdx >= 0 && bucketIdx < buckets.length) {
                            double val = valQ.doubleValue();
                            if (mType == SUM) {
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
            return ofMinutes(1);
        }
        try {
            long value = parseLong(s.substring(0, s.length() - 1));
            if (s.endsWith("s")) {
                return ofSeconds(value);
            }
            if (s.endsWith("m")) {
                return ofMinutes(value);
            }
            if (s.endsWith("h")) {
                return ofHours(value);
            }
            return ofMinutes(parseLong(s));
        } catch (Exception e) {
            return ofMinutes(1);
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
