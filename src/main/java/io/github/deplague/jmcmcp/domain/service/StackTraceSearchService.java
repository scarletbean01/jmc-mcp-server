package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.StackTraceMatchEntry;
import io.github.deplague.jmcmcp.domain.model.StackTraceSearchResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for full-text stack-trace search across all JFR event types.
 */
public final class StackTraceSearchService {

    private static final List<String> SEARCHABLE_EVENT_TYPES = List.of(
            "jdk.ExecutionSample", "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait",
            "jdk.ThreadPark", "jdk.SocketRead", "jdk.SocketWrite",
            "jdk.FileRead", "jdk.FileWrite", "jdk.JavaExceptionThrow",
            "jdk.JavaErrorThrow", "jdk.ObjectAllocationInNewTLAB",
            "jdk.ObjectAllocationOutsideTLAB", "jdk.Compilation"
    );

    private static final Map<String, List<String>> EVENT_DETAIL_FIELDS = Map.of(
            "jdk.JavaMonitorEnter", List.of("monitorClass"),
            "jdk.JavaMonitorWait", List.of("monitorClass"),
            "jdk.SocketRead", List.of("host", "port"),
            "jdk.SocketWrite", List.of("host", "port"),
            "jdk.FileRead", List.of("path"),
            "jdk.FileWrite", List.of("path"),
            "jdk.JavaExceptionThrow", List.of("thrownClass", "message"),
            "jdk.JavaErrorThrow", List.of("thrownClass", "message"),
            "jdk.ObjectAllocationInNewTLAB", List.of("objectClass", "tlabSize"),
            "jdk.ObjectAllocationOutsideTLAB", List.of("objectClass", "allocationSize")
    );

    public StackTraceSearchResult analyze(IItemCollection events, String classPattern, String eventType, int limit) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(classPattern);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid regex pattern: " + classPattern + "\nError: " + e.getMessage()
            );
        }

        List<String> typesToSearch = "all".equals(eventType)
                ? SEARCHABLE_EVENT_TYPES
                : List.of(eventType);

        List<StackTraceMatchEntry> matches = new ArrayList<>();
        Map<String, Long> distribution = new LinkedHashMap<>();

        Map<IMCStackTrace, Boolean> matchCache = new IdentityHashMap<>();
        Map<IMCStackTrace, String> formattedTraceCache = new IdentityHashMap<>();

        for (String typeId : typesToSearch) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            if (!typeEvents.hasItems()) {
                continue;
            }

            long typeMatchCount = 0;
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> stackAccessor =
                        JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (stackAccessor == null) {
                    continue;
                }

                IMemberAccessor<Object, IItem> threadAccessor =
                        JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
                if (threadAccessor == null) {
                    threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");
                }
                IMemberAccessor<IQuantity, IItem> startTimeAccessor =
                        JfrAttributes.START_TIME.getAccessor(iterable.getType());

                List<String> detailFields = EVENT_DETAIL_FIELDS.getOrDefault(typeId, List.of());
                Map<String, IMemberAccessor<Object, IItem>> detailAccessors = new HashMap<>();
                for (String field : detailFields) {
                    IMemberAccessor<Object, IItem> acc =
                            JfrItemUtils.getAccessor(iterable.getType(), field);
                    if (acc != null) {
                        detailAccessors.put(field, acc);
                    }
                }

                for (IItem item : iterable) {
                    Object stackObj = stackAccessor.getMember(item);
                    if (!(stackObj instanceof IMCStackTrace stackTrace)) {
                        continue;
                    }

                    Boolean cachedMatch = matchCache.get(stackTrace);
                    if (cachedMatch == null) {
                        boolean isMatch = JfrItemUtils.stackTraceMatches(stackTrace, pattern);
                        matchCache.put(stackTrace, isMatch);
                        cachedMatch = isMatch;
                        if (isMatch) {
                            formattedTraceCache.put(
                                    stackTrace,
                                    JfrItemUtils.formatFullStackTrace(stackTrace)
                            );
                        }
                    }

                    if (!cachedMatch) {
                        continue;
                    }
                    typeMatchCount++;
                    if (matches.size() >= limit) {
                        continue;
                    }

                    String fullTrace = formattedTraceCache.get(stackTrace);
                    String threadName = threadAccessor != null
                            ? threadAccessor.getMember(item).toString()
                            : "Unknown";
                    String timestamp = startTimeAccessor != null
                            ? display(startTimeAccessor.getMember(item))
                            : "N/A";

                    Map<String, String> details = new LinkedHashMap<>();
                    for (var entry : detailAccessors.entrySet()) {
                        Object val = entry.getValue().getMember(item);
                        if (val != null) {
                            details.put(entry.getKey(), val.toString());
                        }
                    }

                    matches.add(new StackTraceMatchEntry(
                            typeId, timestamp, threadName, fullTrace, details
                    ));
                }
            }

            if (typeMatchCount > 0) {
                distribution.put(typeId, typeMatchCount);
            }
        }

        return new StackTraceSearchResult(
                classPattern,
                eventType,
                matches,
                distribution,
                limit,
                matches.size() >= limit
        );
    }

    private static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(IDisplayable.AUTO);
    }
}
