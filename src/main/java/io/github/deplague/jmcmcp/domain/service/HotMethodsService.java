package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.HotMethodEntry;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for identifying hot methods from execution samples.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class HotMethodsService {

    public HotMethodsResult analyze(
            IItemCollection events,
            String threadName,
            String packagePrefix,
            int topN) {

        IItemCollection samples = events.apply(
                ItemFilters.type("jdk.ExecutionSample")
        );

        if (!samples.hasItems()) {
            return new HotMethodsResult(List.of(), null);
        }

        Map<String, Long> traceCounts = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache =
                JfrItemUtils.newStackTraceFormatCache();

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stackAccessor =
                    JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            IMemberAccessor<Object, IItem> threadAccessor =
                    JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");

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
                        String formatted = stCache.formatFocusingOn(
                                st,
                                5,
                                packagePrefix
                        );
                        traceCounts.merge(formatted, 1L, Long::sum);
                    }
                }
            }
        }

        List<HotMethodEntry> entries = traceCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new HotMethodEntry(e.getKey(), e.getValue()))
                .toList();

        String topMethod = traceCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry ->
                        entry.getKey().split("\n")[0].trim().replace("at ", "")
                )
                .orElse(null);

        return new HotMethodsResult(entries, topMethod);
    }
}
