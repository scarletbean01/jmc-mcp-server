package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ContentionEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadContentionResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Pure domain service for thread contention analysis.
 */
public final class ThreadContentionService {

    public ThreadContentionResult analyze(IItemCollection events, int topN) {
        Map<ContentionKey, Long> durationMap = new HashMap<>();

        processContention(events, "jdk.JavaMonitorEnter", durationMap);
        processContention(events, "jdk.JavaMonitorWait", durationMap);

        if (durationMap.isEmpty()) {
            return new ThreadContentionResult(List.of(), null, null, false);
        }

        List<ContentionEntry> topContentions = durationMap.entrySet().stream()
                .sorted(Map.Entry.<ContentionKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new ContentionEntry(
                        displayNanos(e.getValue()),
                        e.getKey().monitorClass,
                        e.getKey().stackTrace
                ))
                .toList();

        Map.Entry<ContentionKey, Long> topEntry = durationMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        String topLock = topEntry != null ? topEntry.getKey().monitorClass : "unknown";
        long topDuration = durationMap.values().stream().max(Long::compare).orElse(0L);

        return new ThreadContentionResult(topContentions, topLock, displayNanos(topDuration), true);
    }

    private void processContention(IItemCollection events, String typeId, Map<ContentionKey, Long> map) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> monitorAccessor = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (monitorAccessor != null && durationAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    Object monitorObj = monitorAccessor.getMember(item);
                    IQuantity durationQ = durationAccessor.getMember(item);
                    Object stackObj = stackAccessor.getMember(item);

                    if (monitorObj != null && durationQ != null && stackObj != null) {
                        String monitorClass = monitorObj.toString();
                        String trace = JfrItemUtils.formatStackTrace(stackObj, 5);
                        ContentionKey key = new ContentionKey(monitorClass, trace);
                        map.merge(key, durationQ.clampedLongValueIn(UnitLookup.NANOSECOND), Long::sum);
                    }
                }
            }
        }
    }

    private static String displayNanos(long nanos) {
        return UnitLookup.NANOSECOND.quantity(nanos).displayUsing(IDisplayable.AUTO);
    }

    private record ContentionKey(String monitorClass, String stackTrace) {
    }
}
