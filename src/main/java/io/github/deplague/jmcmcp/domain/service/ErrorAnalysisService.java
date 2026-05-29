package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ErrorAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.ErrorEntry;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.count;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static java.lang.String.valueOf;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for error analysis.
 */
@ApplicationScoped
public final class ErrorAnalysisService {

    public ErrorAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection errorEvents = events.apply(type("jdk.JavaErrorThrow"));
        IItemCollection exceptionEvents = events.apply(type("jdk.JavaExceptionThrow"));

        long errorCount = count(errorEvents);
        long exceptionCount = count(exceptionEvents);

        if (errorCount == 0) {
            return new ErrorAnalysisResult(0, exceptionCount, of(), false);
        }

        Map<ErrorKey, Long> counts = new HashMap<>();
        for (IItemIterable iterable : errorEvents) {
            IType<?> type2 = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = getAccessor(type2, "thrownClass");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> msgAccessor = getAccessor(type1, "message");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");

            if (classAccessor != null) {
                for (IItem item : iterable) {
                    String className = classAccessor.getMember(item).toString();
                    String message = msgAccessor != null ? valueOf(msgAccessor.getMember(item)) : "";
                    String trace;
                    if (stackAccessor != null) {
                        Object stackTraceObj = stackAccessor.getMember(item);
                        trace = formatStackTrace(stackTraceObj, 5);
                    } else {
                        trace = "No trace";
                    }

                    ErrorKey key = new ErrorKey(className, message, trace);
                    counts.merge(key, 1L, Long::sum);
                }
            }
        }

        List<ErrorEntry> topErrors = counts.entrySet().stream()
                .sorted(Entry.<ErrorKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new ErrorEntry(
                        e.getKey().className,
                        e.getKey().message,
                        e.getKey().stackTrace,
                        e.getValue(),
                        classifySeverity(e.getKey().className)
                ))
                .toList();

        return new ErrorAnalysisResult(errorCount, exceptionCount, topErrors, true);
    }

    private static String classifySeverity(String className) {
        if (className.contains("OutOfMemoryError")) return "CRITICAL";
        if (className.contains("StackOverflowError")) return "HIGH";
        if (className.contains("InternalError") || className.contains("UnknownError")) return "HIGH";
        if (className.contains("ThreadDeath")) return "MEDIUM";
        return "MEDIUM";
    }

    private record ErrorKey(String className, String message, String stackTrace) {
    }
}
