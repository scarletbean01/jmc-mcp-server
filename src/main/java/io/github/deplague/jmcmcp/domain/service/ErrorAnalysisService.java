package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ErrorAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.ErrorEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for error analysis.
 */
public final class ErrorAnalysisService {

    public ErrorAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection errorEvents = events.apply(ItemFilters.type("jdk.JavaErrorThrow"));
        IItemCollection exceptionEvents = events.apply(ItemFilters.type("jdk.JavaExceptionThrow"));

        long errorCount = JfrItemUtils.count(errorEvents);
        long exceptionCount = JfrItemUtils.count(exceptionEvents);

        if (errorCount == 0) {
            return new ErrorAnalysisResult(0, exceptionCount, List.of(), false);
        }

        Map<ErrorKey, Long> counts = new HashMap<>();
        for (IItemIterable iterable : errorEvents) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "thrownClass");
            IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "message");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (classAccessor != null) {
                for (IItem item : iterable) {
                    String className = classAccessor.getMember(item).toString();
                    String message = msgAccessor != null ? String.valueOf(msgAccessor.getMember(item)) : "";
                    String trace = stackAccessor != null
                            ? JfrItemUtils.formatStackTrace(stackAccessor.getMember(item), 5)
                            : "No trace";

                    ErrorKey key = new ErrorKey(className, message, trace);
                    counts.merge(key, 1L, Long::sum);
                }
            }
        }

        List<ErrorEntry> topErrors = counts.entrySet().stream()
                .sorted(Map.Entry.<ErrorKey, Long>comparingByValue().reversed())
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
