package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.BlockingReasonEntry;
import io.github.deplague.jmcmcp.domain.model.BlockingSummaryResult;
import io.github.deplague.jmcmcp.domain.model.CategoryDistributionEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadBlockingEntry;
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
 * Pure domain service for blocking event aggregation.
 */
@Slf4j
public final class BlockingSummaryService {

    public BlockingSummaryResult analyze(IItemCollection events, int topN) {
        Map<String, String> eventToCategory = Map.of(
                "jdk.JavaMonitorEnter", "MONITOR_ENTER",
                "jdk.JavaMonitorWait", "MONITOR_WAIT",
                "jdk.ThreadPark", "PARK",
                "jdk.ThreadSleep", "SLEEP",
                "jdk.SocketRead", "SOCKET_IO",
                "jdk.SocketWrite", "SOCKET_IO",
                "jdk.FileRead", "FILE_IO",
                "jdk.FileWrite", "FILE_IO"
        );

        Map<String, ThreadBlockingStats> threadStatsMap = new HashMap<>();
        Map<String, CategoryStats> categoryStatsMap = new HashMap<>();
        Map<BlockSite, SiteStats> siteStatsMap = new HashMap<>();

        long totalBlockedEvents = 0;
        long totalBlockedNanos = 0;

        for (Map.Entry<String, String> entry : eventToCategory.entrySet()) {
            String eventType = entry.getKey();
            String category = entry.getValue();

            IItemCollection filteredEvents = events.apply(ItemFilters.type(eventType));
            for (IItemIterable iterable : filteredEvents) {
                IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<Object, IItem> monitorClassAccessor = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
                IMemberAccessor<String, IItem> filePathAccessor = JfrItemUtils.getAccessor(iterable.getType(), "path");
                IMemberAccessor<String, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");

                if (durationAccessor != null) {
                    for (IItem item : iterable) {
                        IQuantity duration = durationAccessor.getMember(item);
                        if (duration == null) continue;

                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        totalBlockedNanos += nanos;
                        totalBlockedEvents++;

                        Object threadObj = threadAccessor != null ? threadAccessor.getMember(item) : null;
                        String threadName = threadObj != null ? threadObj.toString() : "Unknown";

                        ThreadBlockingStats ts = threadStatsMap.computeIfAbsent(threadName, k -> new ThreadBlockingStats(k));
                        ts.add(category, nanos);

                        CategoryStats cs = categoryStatsMap.computeIfAbsent(category, k -> new CategoryStats(k));
                        cs.add(nanos);

                        String detail = "Unknown";
                        if (monitorClassAccessor != null) {
                            Object mc = monitorClassAccessor.getMember(item);
                            if (mc != null) detail = mc.toString();
                        } else if (filePathAccessor != null) {
                            detail = filePathAccessor.getMember(item);
                        } else if (hostAccessor != null) {
                            detail = hostAccessor.getMember(item);
                        } else if (stackAccessor != null) {
                            Object st = stackAccessor.getMember(item);
                            if (st != null) detail = JfrItemUtils.formatStackTrace(st, 1).trim();
                        }

                        BlockSite site = new BlockSite(category, detail);
                        SiteStats ss = siteStatsMap.computeIfAbsent(site, k -> new SiteStats());
                        ss.add(nanos);
                    }
                }
            }
        }

        if (totalBlockedEvents == 0) {
            return new BlockingSummaryResult("0", 0, List.of(), List.of(), List.of(), false, false);
        }

        List<ThreadBlockingEntry> perThread = threadStatsMap.values().stream()
                .sorted((a, b) -> Long.compare(b.totalNanos, a.totalNanos))
                .limit(topN)
                .map(ts -> {
                    String topCategory = ts.categoryNanos.entrySet().stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                            .map(Map.Entry::getKey)
                            .findFirst().orElse("N/A");
                    return new ThreadBlockingEntry(
                            ts.name,
                            UnitLookup.NANOSECOND.quantity(ts.totalNanos)
                                    .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO),
                            ts.count,
                            topCategory,
                            ts.categoryNanos
                    );
                })
                .toList();

        List<BlockingReasonEntry> topReasons = siteStatsMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .map(e -> new BlockingReasonEntry(
                        e.getKey().category,
                        e.getKey().detail,
                        UnitLookup.NANOSECOND.quantity(e.getValue().totalNanos)
                                .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO),
                        e.getValue().count
                ))
                .toList();

        java.util.concurrent.atomic.AtomicBoolean monitorContentionDetected = new java.util.concurrent.atomic.AtomicBoolean(false);
        List<CategoryDistributionEntry> distribution = categoryStatsMap.values().stream()
                .sorted((a, b) -> Long.compare(b.totalNanos, a.totalNanos))
                .map(cs -> {
                    if (cs.category.equals("MONITOR_ENTER") || cs.category.equals("MONITOR_WAIT")) {
                        monitorContentionDetected.set(true);
                    }
                    return new CategoryDistributionEntry(
                            cs.category,
                            UnitLookup.NANOSECOND.quantity(cs.totalNanos)
                                    .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO),
                            cs.count,
                            UnitLookup.NANOSECOND.quantity(cs.totalNanos / cs.count)
                                    .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)
                    );
                })
                .toList();

        return new BlockingSummaryResult(
                UnitLookup.NANOSECOND.quantity(totalBlockedNanos)
                        .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO),
                totalBlockedEvents,
                perThread,
                topReasons,
                distribution,
                monitorContentionDetected.get(),
                true
        );
    }

    private record BlockSite(String category, String detail) {
    }

    private static class ThreadBlockingStats {
        final String name;
        long totalNanos = 0;
        long count = 0;
        final Map<String, Long> categoryNanos = new HashMap<>();

        ThreadBlockingStats(String name) {
            this.name = name;
        }

        void add(String category, long nanos) {
            totalNanos += nanos;
            count++;
            categoryNanos.merge(category, nanos, Long::sum);
        }
    }

    private static class CategoryStats {
        final String category;
        long totalNanos = 0;
        long count = 0;

        CategoryStats(String category) {
            this.category = category;
        }

        void add(long nanos) {
            totalNanos += nanos;
            count++;
        }
    }

    private static class SiteStats {
        long totalNanos = 0;
        long count = 0;

        void add(long nanos) {
            totalNanos += nanos;
            count++;
        }
    }
}
