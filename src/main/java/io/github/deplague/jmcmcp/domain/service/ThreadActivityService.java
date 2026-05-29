package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import io.github.deplague.jmcmcp.infrastructure.jfr.StackTraceKey;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.*;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static java.lang.Long.compare;
import static java.util.Map.Entry;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for thread activity analysis.
 */
@ApplicationScoped
public final class ThreadActivityService {

    public ThreadActivityResult analyze(IItemCollection events, int topN) {
        Optional<ThreadStats> threadStats = analyzeThreadStats(events);
        ThreadLifecycle lifecycle = analyzeThreadLifecycle(events, topN);
        List<SleepHotspot> sleepHotspots = analyzeSleepHotspots(events, topN);
        return new ThreadActivityResult(threadStats, lifecycle, sleepHotspots);
    }

    private Optional<ThreadStats> analyzeThreadStats(IItemCollection events) {
        IItemCollection threadStats = events.apply(type("jdk.JavaThreadStatistics"));
        if (!threadStats.hasItems()) {
            return empty();
        }

        var activeStats = batchStats(threadStats, "activeCount");
        IQuantity peak = maxQuantity(threadStats, "peakCount");
        IQuantity daemon = maxQuantity(threadStats, "daemonCount");
        IQuantity total = maxQuantity(threadStats, "accumulatedCount");

        return of(new ThreadStats(
                display(peak),
                display(activeStats.get("min")),
                display(activeStats.get("avg")),
                display(activeStats.get("max")),
                display(daemon),
                display(total)
        ));
    }

    private ThreadLifecycle analyzeThreadLifecycle(IItemCollection events, int topN) {
        IItemCollection starts = events.apply(type("jdk.ThreadStart"));
        IItemCollection ends = events.apply(type("jdk.ThreadEnd"));
        long startedCount = count(starts);
        long endedCount = count(ends);

        Map<StackTraceKey, Long> startSites = new HashMap<>();
        for (IItemIterable iterable : starts) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");
            if (stackAccessor != null) {
                for (IItem item : iterable) {
                    Object stack = stackAccessor.getMember(item);
                    if (stack != null) {
                        StackTraceKey trace = new StackTraceKey(stack, 5);
                        startSites.merge(trace, 1L, Long::sum);
                    }
                }
            }
        }

        List<ThreadCreationSite> creationSites = startSites.entrySet().stream()
                .sorted(Entry.<StackTraceKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new ThreadCreationSite(e.getValue(), formatStackTrace(e.getKey().getStackTraceObj(), 5)))
                .toList();

        return new ThreadLifecycle(startedCount, endedCount, startedCount - endedCount, creationSites);
    }

    private List<SleepHotspot> analyzeSleepHotspots(IItemCollection events, int topN) {
        IItemCollection sleeps = events.apply(type("jdk.ThreadSleep"));
        if (!sleeps.hasItems()) {
            return List.of();
        }

        Map<StackTraceKey, SleepStats> sleepMap = new HashMap<>();
        for (IItemIterable iterable : sleeps) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> durationAccessor = getAccessor(type1, DURATION.getIdentifier());
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");
            if (durationAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    IQuantity duration = durationAccessor.getMember(item);
                    Object stack = stackAccessor.getMember(item);
                    if (duration != null && stack != null) {
                        StackTraceKey trace = new StackTraceKey(stack, 5);
                        SleepStats stats = sleepMap.computeIfAbsent(trace, k -> new SleepStats());
                        stats.count++;
                        stats.totalNanos += duration.clampedLongValueIn(NANOSECOND);
                    }
                }
            }
        }

        return sleepMap.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .map(e -> {
                    SleepStats s = e.getValue();
                    return new SleepHotspot(
                            display(NANOSECOND.quantity(s.totalNanos)),
                            s.count,
                            formatStackTrace(e.getKey().getStackTraceObj(), 5)
                    );
                })
                .toList();
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(AUTO);
    }

    private static class SleepStats {
        long count;
        long totalNanos;
    }
}
