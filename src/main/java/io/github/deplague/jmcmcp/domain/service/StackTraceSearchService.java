package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.model.StackTraceMatchEntry;
import io.github.deplague.jmcmcp.domain.model.StackTraceSearchResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.*;
import java.util.regex.Pattern;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatFullStackTrace;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.stackTraceMatches;
import static java.util.List.of;
import static java.util.regex.Pattern.compile;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;

/**
 * Pure domain service for full-text stack-trace search across all JFR event types.
 */
@ApplicationScoped
public final class StackTraceSearchService {

    private static final List<String> SEARCHABLE_EVENT_TYPES = of(
            "jdk.ExecutionSample", "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait",
            "jdk.ThreadPark", "jdk.SocketRead", "jdk.SocketWrite",
            "jdk.FileRead", "jdk.FileWrite", "jdk.JavaExceptionThrow",
            "jdk.JavaErrorThrow", "jdk.ObjectAllocationInNewTLAB",
            "jdk.ObjectAllocationOutsideTLAB", "jdk.Compilation"
    );

    private static final Map<String, List<String>> EVENT_DETAIL_FIELDS = Map.of(
            "jdk.JavaMonitorEnter", of("monitorClass"),
            "jdk.JavaMonitorWait", of("monitorClass"),
            "jdk.SocketRead", of("host", "port"),
            "jdk.SocketWrite", of("host", "port"),
            "jdk.FileRead", of("path"),
            "jdk.FileWrite", of("path"),
            "jdk.JavaExceptionThrow", of("thrownClass", "message"),
            "jdk.JavaErrorThrow", of("thrownClass", "message"),
            "jdk.ObjectAllocationInNewTLAB", of("objectClass", "tlabSize"),
            "jdk.ObjectAllocationOutsideTLAB", of("objectClass", "allocationSize")
    );

    public StackTraceSearchResult analyze(IItemCollection events, String classPattern, String eventType, int limit) {
        Pattern pattern;
        try {
            pattern = compile(classPattern);
        } catch (Exception e) {
            throw new AnalysisFailedException(
                    "Invalid regex pattern: " + classPattern + "\nError: " + e.getMessage()
            );
        }

        List<String> typesToSearch = "all".equals(eventType)
                ? SEARCHABLE_EVENT_TYPES
                : of(eventType);

        List<StackTraceMatchEntry> matches = new ArrayList<>();
        Map<String, Long> distribution = new LinkedHashMap<>();

        Map<IMCStackTrace, Boolean> matchCache = new IdentityHashMap<>();
        Map<IMCStackTrace, String> formattedTraceCache = new IdentityHashMap<>();

        for (String typeId : typesToSearch) {
            IItemCollection typeEvents = events.apply(type(typeId));
            if (!typeEvents.hasItems()) {
                continue;
            }

            long typeMatchCount = 0;
            for (IItemIterable iterable : typeEvents) {
                IType<?> type2 = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor =
                        getAccessor(type2, "stackTrace");
                if (stackAccessor == null) {
                    continue;
                }

                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> threadAccessor =
                        getAccessor(type1, "eventThread");
                if (threadAccessor == null) {
                    IType<?> type = iterable.getType();
                    threadAccessor = getAccessor(type, "sampledThread");
                }
                IMemberAccessor<IQuantity, IItem> startTimeAccessor =
                        START_TIME.getAccessor(iterable.getType());

                List<String> detailFields = EVENT_DETAIL_FIELDS.getOrDefault(typeId, of());
                Map<String, IMemberAccessor<Object, IItem>> detailAccessors = new HashMap<>();
                for (String field : detailFields) {
                    IType<?> type = iterable.getType();
                    IMemberAccessor<Object, IItem> acc =
                            getAccessor(type, field);
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
                        boolean isMatch = stackTraceMatches(stackTrace, pattern);
                        matchCache.put(stackTrace, isMatch);
                        cachedMatch = isMatch;
                        if (isMatch) {
                            formattedTraceCache.put(
                                    stackTrace,
                                    formatFullStackTrace(stackTrace)
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
        return quantity.displayUsing(AUTO);
    }
}
