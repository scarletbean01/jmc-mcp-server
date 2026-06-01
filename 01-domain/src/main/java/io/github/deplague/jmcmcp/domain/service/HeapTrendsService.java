package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrValueConverter.toLong;
import static java.lang.Long.parseLong;
import static java.lang.Math.*;
import static java.time.Duration.*;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;
import static org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.getEarliestStartTime;
import static org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.getLatestEndTime;

/**
 * Pure domain service for heap, metaspace and thread trend analysis.
 */
@ApplicationScoped
public final class HeapTrendsService {

    public HeapTrendsResult analyze(IItemCollection events, String bucketSizeStr) {
        IQuantity startQ = getEarliestStartTime(events);
        IQuantity endQ = getLatestEndTime(events);

        if (startQ == null || endQ == null) {
            return new HeapTrendsResult(bucketSizeStr, of(), of(), of(), null, null, null);
        }

        long startMillis = startQ.clampedLongValueIn(EPOCH_MS);
        long endMillis = endQ.clampedLongValueIn(EPOCH_MS);
        long bucketMillis = parseDuration(bucketSizeStr).toMillis();
        if (bucketMillis <= 0) bucketMillis = 60_000L;

        int numBuckets = (int) ceil((double) (endMillis - startMillis) / bucketMillis);
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

        List<HeapBucketEntry> heapBuckets = new ArrayList<>();
        List<MetaspaceBucketEntry> metaspaceBuckets = new ArrayList<>();
        List<ThreadBucketEntry> threadBuckets = new ArrayList<>();

        for (Bucket b : buckets) {
            if (b.heapCount > 0) {
                heapBuckets.add(new HeapBucketEntry(
                        b.startTime,
                        b.heapMin,
                        b.heapCount > 0 ? (long) (b.heapUsedSum / b.heapCount) : null,
                        b.heapMax
                ));
            }
            if (b.metaCount > 0) {
                metaspaceBuckets.add(new MetaspaceBucketEntry(
                        b.startTime,
                        b.metaUsedMin,
                        b.metaCount > 0 ? (long) (b.metaUsedSum / b.metaCount) : null,
                        b.metaUsedMax,
                        b.metaCommittedMin,
                        b.metaCount > 0 ? (long) (b.metaCommittedSum / b.metaCount) : null,
                        b.metaCommittedMax
                ));
            }
            if (b.threadCount > 0) {
                threadBuckets.add(new ThreadBucketEntry(
                        b.startTime,
                        b.threadMin,
                        b.threadCount > 0 ? b.activeThreadSum / b.threadCount : null,
                        b.threadMax
                ));
            }
        }

        MetricSummary heapSummary = computeSummary(events, "jdk.GCHeapSummary", "heapUsed");
        MetricSummary metaspaceSummary = computeSummary(events, "jdk.MetaspaceSummary", "metaspace.used");
        MetricSummary threadSummary = computeSummary(events, "jdk.JavaThreadStatistics", "activeCount");

        return new HeapTrendsResult(
                formatDuration(bucketMillis),
                heapBuckets,
                metaspaceBuckets,
                threadBuckets,
                heapSummary,
                metaspaceSummary,
                threadSummary
        );
    }

    private void processHeapSummary(IItemCollection events, long startMillis, long bucketMillis, Bucket[] buckets) {
        IItemCollection heapEvents = events.apply(type("jdk.GCHeapSummary"));
        for (IItemIterable iterable : heapEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = START_TIME.getAccessor(iterable.getType());
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> usedAccessor = getAccessor(type, "heapUsed");
            if (timeAccessor != null && usedAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    if (timeQ == null) continue;
                    long time = timeQ.clampedLongValueIn(EPOCH_MS);
                    int idx = (int) ((time - startMillis) / bucketMillis);
                    if (idx >= 0 && idx < buckets.length) {
                        IQuantity used = usedAccessor.getMember(item);
                        if (used != null) {
                            long val = used.longValue();
                            buckets[idx].heapUsedSum += val;
                            buckets[idx].heapMin = buckets[idx].heapMin == null ? val : min(buckets[idx].heapMin, val);
                            buckets[idx].heapMax = buckets[idx].heapMax == null ? val : max(buckets[idx].heapMax, val);
                            buckets[idx].heapCount++;
                        }
                    }
                }
            }
        }
    }

    private void processMetaspaceSummary(IItemCollection events, long startMillis, long bucketMillis, Bucket[] buckets) {
        IItemCollection metaEvents = events.apply(type("jdk.MetaspaceSummary"));
        for (IItemIterable iterable : metaEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = START_TIME.getAccessor(iterable.getType());
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> usedAccessor = getAccessor(type1, "metaspace.used");
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> committedAccessor = getAccessor(type, "metaspace.committed");
            if (timeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    if (timeQ == null) continue;
                    long time = timeQ.clampedLongValueIn(EPOCH_MS);
                    int idx = (int) ((time - startMillis) / bucketMillis);
                    if (idx >= 0 && idx < buckets.length) {
                        if (usedAccessor != null) {
                            IQuantity used = usedAccessor.getMember(item);
                            if (used != null) {
                                long val = used.longValue();
                                buckets[idx].metaUsedSum += val;
                                buckets[idx].metaUsedMin = buckets[idx].metaUsedMin == null ? val : min(buckets[idx].metaUsedMin, val);
                                buckets[idx].metaUsedMax = buckets[idx].metaUsedMax == null ? val : max(buckets[idx].metaUsedMax, val);
                            }
                        }
                        if (committedAccessor != null) {
                            IQuantity committed = committedAccessor.getMember(item);
                            if (committed != null) {
                                long val = committed.longValue();
                                buckets[idx].metaCommittedSum += val;
                                buckets[idx].metaCommittedMin = buckets[idx].metaCommittedMin == null ? val : min(buckets[idx].metaCommittedMin, val);
                                buckets[idx].metaCommittedMax = buckets[idx].metaCommittedMax == null ? val : max(buckets[idx].metaCommittedMax, val);
                            }
                        }
                        buckets[idx].metaCount++;
                    }
                }
            }
        }
    }

    private void processThreadStats(IItemCollection events, long startMillis, long bucketMillis, Bucket[] buckets) {
        IItemCollection threadEvents = events.apply(type("jdk.JavaThreadStatistics"));
        for (IItemIterable iterable : threadEvents) {
            IMemberAccessor<IQuantity, IItem> timeAccessor = START_TIME.getAccessor(iterable.getType());
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> activeAccessor = getAccessor(type, "activeCount");
            if (timeAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity timeQ = timeAccessor.getMember(item);
                    if (timeQ == null) continue;
                    long time = timeQ.clampedLongValueIn(EPOCH_MS);
                    int idx = (int) ((time - startMillis) / bucketMillis);
                    if (idx >= 0 && idx < buckets.length) {
                        if (activeAccessor != null) {
                            Object active = activeAccessor.getMember(item);
                            if (active != null) {
                                long val = toLong(active);
                                buckets[idx].activeThreadSum += val;
                                buckets[idx].threadMin = buckets[idx].threadMin == null ? val : min(buckets[idx].threadMin, val);
                                buckets[idx].threadMax = buckets[idx].threadMax == null ? val : max(buckets[idx].threadMax, val);
                            }
                        }
                        buckets[idx].threadCount++;
                    }
                }
            }
        }
    }

    private MetricSummary computeSummary(IItemCollection events, String typeId, String attribute) {
        IItemCollection items = events.apply(type(typeId));
        if (!items.hasItems()) {
            return null;
        }
        Long min = null;
        Long max = null;
        long sum = 0;
        long count = 0;
        for (IItemIterable iterable : items) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> accessor = getAccessor(type, attribute);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        long val = toLong(raw);
                        min = min == null ? val : min(min, val);
                        max = max == null ? val : max(max, val);
                        sum += val;
                        count++;
                    }
                }
            }
        }
        if (count == 0) return null;
        return new MetricSummary(min, sum / count, max);
    }

    private static Duration parseDuration(String s) {
        if (s == null || s.isEmpty()) return ofMinutes(1);
        try {
            long value = parseLong(s.substring(0, s.length() - 1));
            if (s.endsWith("s")) return ofSeconds(value);
            if (s.endsWith("m")) return ofMinutes(value);
            if (s.endsWith("h")) return ofHours(value);
            return ofMinutes(parseLong(s));
        } catch (Exception e) {
            return ofMinutes(1);
        }
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        return (millis / 1000.0) + "s";
    }

    private static class Bucket {
        final long startTime;
        double heapUsedSum = 0;
        Long heapMin = null;
        Long heapMax = null;
        int heapCount = 0;
        double metaUsedSum = 0;
        double metaCommittedSum = 0;
        Long metaUsedMin = null;
        Long metaUsedMax = null;
        Long metaCommittedMin = null;
        Long metaCommittedMax = null;
        int metaCount = 0;
        long activeThreadSum = 0;
        Long threadMin = null;
        Long threadMax = null;
        int threadCount = 0;

        Bucket(long startTime) {
            this.startTime = startTime;
        }
    }
}
