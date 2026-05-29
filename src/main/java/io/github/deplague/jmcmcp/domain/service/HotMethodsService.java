package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.infrastructure.jfr.StackTraceKey;
import io.github.deplague.jmcmcp.domain.model.HotMethodEntry;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTraceFocusingOn;
import static java.util.List.of;
import static java.util.Map.Entry;
import static java.util.Map.Entry.comparingByValue;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for identifying hot methods from execution samples.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class HotMethodsService {

    public HotMethodsResult analyze(
            IItemCollection events,
            String threadName,
            String packagePrefix,
            int topN) {

        IItemCollection samples = events.apply(
                type("jdk.ExecutionSample")
        );

        if (!samples.hasItems()) {
            return new HotMethodsResult(of(), null);
        }

        Map<StackTraceKey, Long> traceCounts = new HashMap<>();

        for (IItemIterable iterable : samples) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor =
                    getAccessor(type1, "stackTrace");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> threadAccessor =
                    getAccessor(type, "sampledThread");

            if (stackAccessor != null) {
                for (IItem item : iterable) {
                    if (threadName != null && threadAccessor != null) {
                        Object threadObj = threadAccessor.getMember(item);
                        if (threadObj == null ||
                                !threadObj.toString().contains(threadName)) {
                            continue;
                        }
                    }

                    Object st = stackAccessor.getMember(item);
                    if (st != null) {
                        StackTraceKey key = new StackTraceKey(st, 5, packagePrefix);
                        traceCounts.merge(key, 1L, Long::sum);
                    }
                }
            }
        }

        List<HotMethodEntry> entries = traceCounts.entrySet().stream()
                .sorted(Entry.<StackTraceKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new HotMethodEntry(formatStackTraceFocusingOn(e.getKey().getStackTraceObj(), 5, packagePrefix), e.getValue()))
                .toList();

        String topMethod = entries.stream()
                .findFirst()
                .map(entry ->
                        entry.stackTrace().split("\n")[0].trim().replace("at ", "")
                )
                .orElse(null);

        return new HotMethodsResult(entries, topMethod);
    }
}
