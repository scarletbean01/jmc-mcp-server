package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ThreadAllocEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadAllocationResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for per-thread allocation breakdown.
 */
@Slf4j
public final class ThreadAllocationService {

    public ThreadAllocationResult analyze(IItemCollection events, int topN) {
        IItemCollection allocStats = events.apply(ItemFilters.type("jdk.ThreadAllocationStatistics"));
        if (!allocStats.hasItems()) {
            return new ThreadAllocationResult(List.of(), false, false);
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
                        long allocated = allocatedQ.clampedLongValueIn(UnitLookup.BYTE);
                        long timeNanos = timeQ != null ? timeQ.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;

                        ThreadAllocStats stats = threadStatsMap.computeIfAbsent(threadName, k -> new ThreadAllocStats());
                        stats.update(allocated, timeNanos);
                    }
                }
            }
        }

        boolean heavyAllocation = false;
        List<ThreadAllocEntry> entries = new ArrayList<>();

        var sortedEntries = threadStatsMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(
                        b.getValue().maxAllocated - b.getValue().minAllocated,
                        a.getValue().maxAllocated - a.getValue().minAllocated
                ))
                .limit(topN)
                .toList();

        for (var e : sortedEntries) {
            ThreadAllocStats s = e.getValue();
            long diff = s.maxAllocated - s.minAllocated;
            boolean isHeavy = diff > 10L * 1024 * 1024;

            String rate = "N/A";
            if (s.maxTime > s.minTime) {
                double seconds = (s.maxTime - s.minTime) / 1_000_000_000.0;
                if (seconds > 0) {
                    long rateBytes = (long) (diff / seconds);
                    rate = formatBytes(rateBytes) + "/s";
                    if (rateBytes > 10L * 1024 * 1024) {
                        isHeavy = true;
                    }
                }
            }

            if (isHeavy) {
                heavyAllocation = true;
            }

            entries.add(new ThreadAllocEntry(
                    e.getKey(),
                    formatBytes(diff),
                    rate,
                    isHeavy
            ));
        }

        return new ThreadAllocationResult(entries, true, heavyAllocation);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
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
