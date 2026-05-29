package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.BlockingReasonEntry;
import io.github.deplague.jmcmcp.domain.model.BlockingSummaryResult;
import io.github.deplague.jmcmcp.domain.model.CategoryDistributionEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadBlockingEntry;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static java.lang.Long.compare;
import static java.util.Map.Entry;
import static java.util.Map.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for blocking event aggregation.
 */
@Slf4j
@ApplicationScoped
public final class BlockingSummaryService {

    public BlockingSummaryResult analyze(IItemCollection events, int topN) {
        Map<String, String> eventToCategory = of(
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

        for (Entry<String, String> entry : eventToCategory.entrySet()) {
            String eventType = entry.getKey();
            String category = entry.getValue();

            IItemCollection filteredEvents = events.apply(type(eventType));
            for (IItemIterable iterable : filteredEvents) {
                IType<?> type4 = iterable.getType();
                IMemberAccessor<Object, IItem> threadAccessor = getAccessor(type4, "eventThread");
                IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());
                IType<?> type3 = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type3, "stackTrace");
                IType<?> type2 = iterable.getType();
                IMemberAccessor<Object, IItem> monitorClassAccessor = getAccessor(type2, "monitorClass");
                IType<?> type1 = iterable.getType();
                IMemberAccessor<String, IItem> filePathAccessor = getAccessor(type1, "path");
                IType<?> type = iterable.getType();
                IMemberAccessor<String, IItem> hostAccessor = getAccessor(type, "host");

                if (durationAccessor != null) {
                    for (IItem item : iterable) {
                        IQuantity duration = durationAccessor.getMember(item);
                        if (duration == null) continue;

                        long nanos = duration.clampedLongValueIn(NANOSECOND);
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
                            if (st != null) detail = formatStackTrace(st, 1).trim();
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
                .sorted((a, b) -> compare(b.totalNanos, a.totalNanos))
                .limit(topN)
                .map(ts -> {
                    String topCategory = ts.categoryNanos.entrySet().stream()
                            .sorted(Entry.<String, Long>comparingByValue().reversed())
                            .map(Entry::getKey)
                            .findFirst().orElse("N/A");
                    return new ThreadBlockingEntry(
                            ts.name,
                            NANOSECOND.quantity(ts.totalNanos)
                                    .displayUsing(AUTO),
                            ts.count,
                            topCategory,
                            ts.categoryNanos
                    );
                })
                .toList();

        List<BlockingReasonEntry> topReasons = siteStatsMap.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .map(e -> new BlockingReasonEntry(
                        e.getKey().category,
                        e.getKey().detail,
                        NANOSECOND.quantity(e.getValue().totalNanos)
                                .displayUsing(AUTO),
                        e.getValue().count
                ))
                .toList();

        AtomicBoolean monitorContentionDetected = new AtomicBoolean(false);
        List<CategoryDistributionEntry> distribution = categoryStatsMap.values().stream()
                .sorted((a, b) -> compare(b.totalNanos, a.totalNanos))
                .map(cs -> {
                    if (cs.category.equals("MONITOR_ENTER") || cs.category.equals("MONITOR_WAIT")) {
                        monitorContentionDetected.set(true);
                    }
                    return new CategoryDistributionEntry(
                            cs.category,
                            NANOSECOND.quantity(cs.totalNanos)
                                    .displayUsing(AUTO),
                            cs.count,
                            NANOSECOND.quantity(cs.totalNanos / cs.count)
                                    .displayUsing(AUTO)
                    );
                })
                .toList();

        return new BlockingSummaryResult(
                NANOSECOND.quantity(totalBlockedNanos)
                        .displayUsing(AUTO),
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
