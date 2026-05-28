package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.GcCauseEntry;
import io.github.deplague.jmcmcp.domain.model.GcCauseResult;
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
 * Pure domain service for analyzing GC causes.
 */
@Slf4j
public final class GcCauseService {

    public GcCauseResult analyze(IItemCollection events) {
        Map<String, CauseStats> overallStats = new HashMap<>();
        Map<String, CauseStats> youngStats = new HashMap<>();
        Map<String, CauseStats> oldStats = new HashMap<>();

        processGcEvents(events, "jdk.YoungGarbageCollection", youngStats, overallStats);
        processGcEvents(events, "jdk.OldGarbageCollection", oldStats, overallStats);

        return new GcCauseResult(
                toEntries(overallStats),
                toEntries(youngStats),
                toEntries(oldStats),
                !overallStats.isEmpty()
        );
    }

    private void processGcEvents(IItemCollection events, String typeId,
                                 Map<String, CauseStats> genStats,
                                 Map<String, CauseStats> overallStats) {
        IItemCollection gcEvents = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : gcEvents) {
            IMemberAccessor<String, IItem> causeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "cause");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());

            if (causeAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String cause = causeAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (cause != null && duration != null) {
                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        genStats.computeIfAbsent(cause, k -> new CauseStats()).add(nanos);
                        overallStats.computeIfAbsent(cause, k -> new CauseStats()).add(nanos);
                    }
                }
            }
        }
    }

    private List<GcCauseEntry> toEntries(Map<String, CauseStats> stats) {
        return stats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .map(e -> {
                    CauseStats s = e.getValue();
                    return new GcCauseEntry(
                            e.getKey(),
                            s.count,
                            UnitLookup.NANOSECOND.quantity(s.totalNanos)
                                    .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO),
                            UnitLookup.NANOSECOND.quantity(s.totalNanos / s.count)
                                    .displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)
                    );
                })
                .toList();
    }

    private static class CauseStats {
        long count = 0;
        long totalNanos = 0;

        void add(long nanos) {
            count++;
            totalNanos += nanos;
        }
    }
}
