package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.SleepHotspot;
import io.github.deplague.jmcmcp.domain.model.ThreadActivityResult;
import io.github.deplague.jmcmcp.domain.model.ThreadCreationSite;
import io.github.deplague.jmcmcp.domain.model.ThreadLifecycle;
import io.github.deplague.jmcmcp.domain.model.ThreadStats;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for thread activity analysis.
 */
public final class ThreadActivityService {

    public ThreadActivityResult analyze(IItemCollection events, int topN) {
        Optional<ThreadStats> threadStats = analyzeThreadStats(events);
        ThreadLifecycle lifecycle = analyzeThreadLifecycle(events, topN);
        List<SleepHotspot> sleepHotspots = analyzeSleepHotspots(events, topN);
        return new ThreadActivityResult(threadStats, lifecycle, sleepHotspots);
    }

    private Optional<ThreadStats> analyzeThreadStats(IItemCollection events) {
        IItemCollection threadStats = events.apply(ItemFilters.type("jdk.JavaThreadStatistics"));
        if (!threadStats.hasItems()) {
            return Optional.empty();
        }

        IQuantity peak = JfrItemUtils.maxQuantity(threadStats, "peakCount");
        IQuantity maxActive = JfrItemUtils.maxQuantity(threadStats, "activeCount");
        IQuantity minActive = JfrItemUtils.minQuantity(threadStats, "activeCount");
        IQuantity avgActive = JfrItemUtils.avgQuantity(threadStats, "activeCount");
        IQuantity daemon = JfrItemUtils.maxQuantity(threadStats, "daemonCount");
        IQuantity total = JfrItemUtils.maxQuantity(threadStats, "accumulatedCount");

        return Optional.of(new ThreadStats(
                display(peak),
                display(minActive),
                display(avgActive),
                display(maxActive),
                display(daemon),
                display(total)
        ));
    }

    private ThreadLifecycle analyzeThreadLifecycle(IItemCollection events, int topN) {
        IItemCollection starts = events.apply(ItemFilters.type("jdk.ThreadStart"));
        IItemCollection ends = events.apply(ItemFilters.type("jdk.ThreadEnd"));
        long startedCount = JfrItemUtils.count(starts);
        long endedCount = JfrItemUtils.count(ends);

        Map<String, Long> startSites = new HashMap<>();
        for (IItemIterable iterable : starts) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (stackAccessor != null) {
                for (IItem item : iterable) {
                    Object stack = stackAccessor.getMember(item);
                    String trace = JfrItemUtils.formatStackTrace(stack, 5);
                    startSites.merge(trace, 1L, Long::sum);
                }
            }
        }

        List<ThreadCreationSite> creationSites = startSites.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new ThreadCreationSite(e.getValue(), e.getKey()))
                .toList();

        return new ThreadLifecycle(startedCount, endedCount, startedCount - endedCount, creationSites);
    }

    private List<SleepHotspot> analyzeSleepHotspots(IItemCollection events, int topN) {
        IItemCollection sleeps = events.apply(ItemFilters.type("jdk.ThreadSleep"));
        if (!sleeps.hasItems()) {
            return List.of();
        }

        Map<String, SleepStats> sleepMap = new HashMap<>();
        for (IItemIterable iterable : sleeps) {
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(
                    iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (durationAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity duration = durationAccessor.getMember(item);
                    Object stack = stackAccessor.getMember(item);
                    if (duration != null && stack != null) {
                        String trace = JfrItemUtils.formatStackTrace(stack, 5);
                        SleepStats stats = sleepMap.computeIfAbsent(trace, k -> new SleepStats());
                        stats.count++;
                        stats.totalNanos += duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                    }
                }
            }
        }

        return sleepMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .map(e -> {
                    SleepStats s = e.getValue();
                    return new SleepHotspot(
                            display(UnitLookup.NANOSECOND.quantity(s.totalNanos)),
                            s.count,
                            e.getKey()
                    );
                })
                .toList();
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(IDisplayable.AUTO);
    }

    private static class SleepStats {
        long count;
        long totalNanos;
    }
}
