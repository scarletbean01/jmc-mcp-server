package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ContentionEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadContentionResult;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.domain.service.JfrStackTraceService.formatStackTrace;
import static java.util.List.of;
import static java.util.Map.Entry;
import static java.util.Map.Entry.comparingByValue;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for thread contention analysis.
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class ThreadContentionService {
    private final JfrAccessorRepository jfrAccessorRepository;

    public ThreadContentionResult analyze(IItemCollection events, int topN) {
        Map<ContentionKey, Long> durationMap = new HashMap<>();

        processContention(events, "jdk.JavaMonitorEnter", durationMap);
        processContention(events, "jdk.JavaMonitorWait", durationMap);

        if (durationMap.isEmpty()) {
            return new ThreadContentionResult(of(), null, null, false);
        }

        List<ContentionEntry> topContentions = durationMap.entrySet().stream()
                .sorted(Entry.<ContentionKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new ContentionEntry(
                        displayNanos(e.getValue()),
                        e.getKey().monitorClass,
                        e.getKey().stackTrace
                ))
                .toList();

        Entry<ContentionKey, Long> topEntry = durationMap.entrySet().stream()
                .max(comparingByValue())
                .orElse(null);
        String topLock = topEntry != null ? topEntry.getKey().monitorClass : "unknown";
        long topDuration = durationMap.values().stream().max(Long::compare).orElse(0L);

        return new ThreadContentionResult(topContentions, topLock, displayNanos(topDuration), true);
    }

    private void processContention(IItemCollection events, String typeId, Map<ContentionKey, Long> map) {
        IItemCollection filtered = events.apply(type(typeId));
        for (IItemIterable iterable : filtered) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> monitorAccessor = jfrAccessorRepository.getAccessor(type, "monitorClass");
            IMemberAccessor<IQuantity, IItem> durationAccessor = jfrAccessorRepository.getAccessor(type, DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAccessor = jfrAccessorRepository.getAccessor(type, "stackTrace");

            if (monitorAccessor != null && durationAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    Object monitorObj = monitorAccessor.getMember(item);
                    IQuantity durationQ = durationAccessor.getMember(item);
                    Object stackObj = stackAccessor.getMember(item);

                    if (monitorObj != null && durationQ != null && stackObj != null) {
                        String monitorClass = monitorObj.toString();
                        String trace = formatStackTrace(stackObj, 5);
                        ContentionKey key = new ContentionKey(monitorClass, trace);
                        map.merge(key, durationQ.clampedLongValueIn(NANOSECOND), Long::sum);
                    }
                }
            }
        }
    }

    private static String displayNanos(long nanos) {
        return NANOSECOND.quantity(nanos).displayUsing(AUTO);
    }

    private record ContentionKey(String monitorClass, String stackTrace) {
    }
}
