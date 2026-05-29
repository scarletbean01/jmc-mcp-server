package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.GcCauseEntry;
import io.github.deplague.jmcmcp.domain.model.GcCauseResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static java.lang.Long.compare;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for analyzing GC causes.
 */
@Slf4j
@ApplicationScoped
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
        IItemCollection gcEvents = events.apply(type(typeId));
        for (IItemIterable iterable : gcEvents) {
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> causeAccessor = getAccessor(type, "cause");
            IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());

            if (causeAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String cause = causeAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (cause != null && duration != null) {
                        long nanos = duration.clampedLongValueIn(NANOSECOND);
                        genStats.computeIfAbsent(cause, k -> new CauseStats()).add(nanos);
                        overallStats.computeIfAbsent(cause, k -> new CauseStats()).add(nanos);
                    }
                }
            }
        }
    }

    private List<GcCauseEntry> toEntries(Map<String, CauseStats> stats) {
        return stats.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .map(e -> {
                    CauseStats s = e.getValue();
                    return new GcCauseEntry(
                            e.getKey(),
                            s.count,
                            NANOSECOND.quantity(s.totalNanos)
                                    .displayUsing(AUTO),
                            NANOSECOND.quantity(s.totalNanos / s.count)
                                    .displayUsing(AUTO)
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
