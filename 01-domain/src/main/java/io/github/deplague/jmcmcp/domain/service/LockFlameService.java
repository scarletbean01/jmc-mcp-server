package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LockFlameEntry;
import io.github.deplague.jmcmcp.domain.model.LockFlameResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.StackTraceFormatCache;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for lock contention flame graph analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class LockFlameService {

    public LockFlameResult analyze(IItemCollection events, int topN) {
        Map<String, Long> pathDist = new HashMap<>();
        StackTraceFormatCache stCache = new StackTraceFormatCache();
        long totalNanos = 0;

        for (String typeId : new String[]{
                "jdk.JavaMonitorEnter",
                "jdk.JavaMonitorWait",
                "jdk.ThreadPark",
        }) {
            IItemCollection locks = events.apply(type(typeId));
            for (IItemIterable iterable : locks) {
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor =
                        getAccessor(type, "stackTrace");
                IMemberAccessor<IQuantity, IItem> durAccessor =
                        DURATION.getAccessor(iterable.getType());

                if (stackAccessor != null && durAccessor != null) {
                    for (IItem item : iterable) {
                        Object stackObj = stackAccessor.getMember(item);
                        IQuantity dur = durAccessor.getMember(item);
                        if (stackObj != null && dur != null) {
                            long nanos = dur.clampedLongValueIn(NANOSECOND);
                            totalNanos += nanos;
                            String path = stCache.format(stackObj, 10);
                            pathDist.merge(path, nanos, Long::sum);
                        }
                    }
                }
            }
        }

        if (totalNanos == 0) {
            return new LockFlameResult(0, formatDuration(0), of());
        }

        long finalTotal = totalNanos;
        List<LockFlameEntry> entries = pathDist.entrySet().stream()
                .sorted(Entry.<String, Long>comparingByValue().reversed())
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
