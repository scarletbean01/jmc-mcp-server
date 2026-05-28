package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ExceptionEntry;
import io.github.deplague.jmcmcp.domain.model.ExceptionAnalysisResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
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

/**
 * Pure domain service for analyzing Java exceptions.
 */
@Slf4j
public final class ExceptionAnalysisService {

    public ExceptionAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection exceptionEvents = events.apply(ItemFilters.type("jdk.JavaExceptionThrow"));
        IItemCollection errorEvents = events.apply(ItemFilters.type("jdk.JavaErrorThrow"));

        Map<ExceptionKey, Long> counts = new HashMap<>();
        long exceptionCount = processThrowEvents(exceptionEvents, counts);
        long errorCount = processThrowEvents(errorEvents, counts);

        if (counts.isEmpty()) {
            return new ExceptionAnalysisResult(0, 0, List.of(), false);
        }

        List<ExceptionEntry> topExceptions = counts.entrySet().stream()
                .sorted(Map.Entry.<ExceptionKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new ExceptionEntry(
                        e.getKey().className,
                        e.getKey().message,
                        e.getKey().stackTrace,
                        e.getValue()
                ))
                .toList();

        return new ExceptionAnalysisResult(exceptionCount, errorCount, topExceptions, true);
    }

    private long processThrowEvents(IItemCollection throwEvents, Map<ExceptionKey, Long> counts) {
        long total = 0;
        for (IItemIterable iterable : throwEvents) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "thrownClass");
            IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "message");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (classAccessor != null) {
                for (IItem item : iterable) {
                    total++;
                    String className = classAccessor.getMember(item).toString();
                    String message = msgAccessor != null ? String.valueOf(msgAccessor.getMember(item)) : "";
                    String trace = stackAccessor != null
                            ? JfrItemUtils.formatStackTrace(stackAccessor.getMember(item), 5)
                            : "No trace";

                    ExceptionKey key = new ExceptionKey(className, message, trace);
                    counts.merge(key, 1L, Long::sum);
                }
            }
        }
        return total;
    }

    private record ExceptionKey(String className, String message, String stackTrace) {
    }
}
