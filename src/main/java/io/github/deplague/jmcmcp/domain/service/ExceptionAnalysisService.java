package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ExceptionAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.ExceptionEntry;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static java.lang.String.valueOf;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for analyzing Java exceptions.
 */
@Slf4j
@ApplicationScoped
public final class ExceptionAnalysisService {

    public ExceptionAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection exceptionEvents = events.apply(type("jdk.JavaExceptionThrow"));
        IItemCollection errorEvents = events.apply(type("jdk.JavaErrorThrow"));

        Map<ExceptionKey, Long> counts = new HashMap<>();
        long exceptionCount = processThrowEvents(exceptionEvents, counts);
        long errorCount = processThrowEvents(errorEvents, counts);

        if (counts.isEmpty()) {
            return new ExceptionAnalysisResult(0, 0, of(), false);
        }

        List<ExceptionEntry> topExceptions = counts.entrySet().stream()
                .sorted(Entry.<ExceptionKey, Long>comparingByValue().reversed())
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
            IType<?> type2 = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = getAccessor(type2, "thrownClass");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> msgAccessor = getAccessor(type1, "message");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");

            if (classAccessor != null) {
                for (IItem item : iterable) {
                    total++;
                    String className = classAccessor.getMember(item).toString();
                    String message = msgAccessor != null ? valueOf(msgAccessor.getMember(item)) : "";
                    String trace;
                    if (stackAccessor != null) {
                        Object stackTraceObj = stackAccessor.getMember(item);
                        trace = formatStackTrace(stackTraceObj, 5);
                    } else {
                        trace = "No trace";
                    }

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
