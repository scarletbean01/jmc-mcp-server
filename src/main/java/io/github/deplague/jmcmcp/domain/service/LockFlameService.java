package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LockFlameEntry;
import io.github.deplague.jmcmcp.domain.model.LockFlameResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for lock contention flame graph analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class LockFlameService {

    public LockFlameResult analyze(IItemCollection events, int topN) {
        Map<String, Long> pathDist = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();
        long totalNanos = 0;

        for (String typeId : new String[] {
                "jdk.JavaMonitorEnter",
                "jdk.JavaMonitorWait",
                "jdk.ThreadPark",
        }) {
            IItemCollection locks = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : locks) {
                IMemberAccessor<Object, IItem> stackAccessor =
                        JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<IQuantity, IItem> durAccessor =
                        JfrAttributes.DURATION.getAccessor(iterable.getType());

                if (stackAccessor != null && durAccessor != null) {
                    for (IItem item : iterable) {
                        Object stackObj = stackAccessor.getMember(item);
                        IQuantity dur = durAccessor.getMember(item);
                        if (stackObj != null && dur != null) {
                            long nanos = dur.clampedLongValueIn(UnitLookup.NANOSECOND);
                            totalNanos += nanos;
                            String path = stCache.format(stackObj, 10);
                            pathDist.merge(path, nanos, Long::sum);
                        }
                    }
                }
            }
        }

        if (totalNanos == 0) {
            return new LockFlameResult(0, formatDuration(0), List.of());
        }

        long finalTotal = totalNanos;
        List<LockFlameEntry> entries = pathDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new LockFlameEntry(
                        e.getKey(),
                        e.getValue(),
                        formatDuration(e.getValue() / 1_000_000L),
                        (e.getValue() * 100.0) / finalTotal
                ))
                .toList();

        return new LockFlameResult(totalNanos, formatDuration(totalNanos / 1_000_000L), entries);
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        return (millis / 1000.0) + "s";
    }
}
