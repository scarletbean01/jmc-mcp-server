package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.RequestWaterfallEvent;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallResult;
import io.github.deplague.jmcmcp.domain.model.WaterfallPhaseSummary;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.*;
import java.util.regex.Pattern;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatFullStackTrace;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static java.util.Comparator.comparingLong;
import static java.util.List.of;
import static java.util.regex.Pattern.compile;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS;
import static org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;

/**
 * Pure domain service for reconstructing a chronological waterfall of events
 * for a specific thread. Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class RequestWaterfallService {

    private static final List<String> WATERFALL_EVENT_TYPES = of(
            "jdk.ExecutionSample", "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait",
            "jdk.ThreadPark", "jdk.SocketRead", "jdk.SocketWrite",
            "jdk.FileRead", "jdk.FileWrite", "jdk.JavaExceptionThrow"
    );

    public RequestWaterfallResult analyze(
            IItemCollection events,
            String threadName,
            int maxEvents) {

        Pattern threadPattern = compile(threadName);

        List<RequestWaterfallEvent> waterfallEvents = new ArrayList<>();
        Map<String, Long> eventTypeCounts = new LinkedHashMap<>();

        for (String typeId : WATERFALL_EVENT_TYPES) {
            IItemCollection typeEvents = events.apply(type(typeId));
            if (!typeEvents.hasItems()) {
                continue;
            }

            for (IItemIterable iterable : typeEvents) {
                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> threadAccessor =
                        getAccessor(type1, "eventThread");
                if (threadAccessor == null) {
                    IType<?> type = iterable.getType();
                    threadAccessor = getAccessor(type, "sampledThread");
                }
                if (threadAccessor == null) {
                    continue;
                }

                IMemberAccessor<IQuantity, IItem> startTimeAccessor =
                        START_TIME.getAccessor(iterable.getType());
                IMemberAccessor<IQuantity, IItem> durationAccessor =
                        DURATION.getAccessor(iterable.getType());
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor =
                        getAccessor(type, "stackTrace");

                for (IItem item : iterable) {
                    Object threadObj = threadAccessor.getMember(item);
                    if (threadObj == null) {
                        continue;
                    }
                    String tName = threadObj.toString();
                    if (!threadPattern.matcher(tName).find() && !tName.equals(threadName)) {
                        continue;
                    }

                    long timeMs = 0;
                    if (startTimeAccessor != null) {
                        IQuantity startQ = startTimeAccessor.getMember(item);
                        if (startQ != null) {
                            timeMs = startQ.clampedLongValueIn(EPOCH_MS);
                        }
                    }

                    long durationMs = 0;
                    if (durationAccessor != null) {
                        IQuantity durQ = durationAccessor.getMember(item);
                        if (durQ != null) {
                            durationMs = durQ.clampedLongValueIn(MILLISECOND);
                        }
                    }

                    String topFrame = "";
                    String fullTrace = "";
                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) {
                            topFrame = extractTopFrame(st);
                            fullTrace = formatFullStackTrace(st);
                        }
                    }

                    String detail = extractDetail(iterable.getType(), item);
                    String phase = classifyPhase(typeId);

                    waterfallEvents.add(new RequestWaterfallEvent(
                            timeMs, typeId, phase, durationMs,
                            detail, topFrame, fullTrace, tName));
                    eventTypeCounts.merge(typeId, 1L, Long::sum);
                }
            }
        }

        if (waterfallEvents.isEmpty()) {
            return new RequestWaterfallResult(
                    of(), eventTypeCounts, of(),
                    Set.of(), 0, 0, false);
        }

        waterfallEvents.sort(comparingLong(RequestWaterfallEvent::timeMs));

        long baseTimeMs = waterfallEvents.get(0).timeMs();
        long endTimeMs = waterfallEvents.get(waterfallEvents.size() - 1).timeMs();

        if (waterfallEvents.size() > maxEvents) {
            waterfallEvents = waterfallEvents.subList(0, maxEvents);
        }

        Set<String> matchedThreads = new LinkedHashSet<>();
        Map<String, MutablePhaseSummary> phaseMap = new LinkedHashMap<>();
        for (RequestWaterfallEvent we : waterfallEvents) {
            matchedThreads.add(we.threadName());
            MutablePhaseSummary ps = phaseMap.computeIfAbsent(
                    we.phase(), k -> new MutablePhaseSummary(k));
            ps.totalTimeMs += we.durationMs();
            ps.eventCount++;
        }

        List<WaterfallPhaseSummary> phaseSummaries = phaseMap.values().stream()
                .map(ps -> new WaterfallPhaseSummary(ps.phaseName, ps.totalTimeMs, ps.eventCount))
                .toList();

        return new RequestWaterfallResult(
                waterfallEvents,
                eventTypeCounts,
                phaseSummaries,
                matchedThreads,
                baseTimeMs,
                endTimeMs,
                true);
    }

    private String classifyPhase(String typeId) {
        if (typeId.equals("jdk.JavaMonitorEnter") || typeId.equals("jdk.JavaMonitorWait")) {
            return "BLOCKED";
        }
        if (typeId.equals("jdk.ThreadPark")) {
            return "WAITING";
        }
        if (typeId.equals("jdk.SocketRead") || typeId.equals("jdk.SocketWrite")
                || typeId.equals("jdk.FileRead") || typeId.equals("jdk.FileWrite")) {
            return "IO";
        }
        if (typeId.equals("jdk.ExecutionSample")) {
            return "CPU";
        }
        if (typeId.equals("jdk.JavaExceptionThrow")) {
            return "EXCEPTION";
        }
        return "OTHER";
    }

    private String extractDetail(IType<IItem> type, IItem item) {
        String typeId = type.getIdentifier();
        StringBuilder detail = new StringBuilder();

        switch (typeId) {
            case "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait" -> {
                Object monitor = getMember(item, "monitorClass").orElse(null);
                if (monitor != null) {
                    detail.append(monitor.toString());
                }
            }
            case "jdk.SocketRead", "jdk.SocketWrite" -> {
                Object host = getMember(item, "host").orElse(null);
                Object port = getMember(item, "port").orElse(null);
                if (host != null) {
                    detail.append(host);
                    if (port != null) {
                        detail.append(":").append(port);
                    }
                }
                String bytesAttr = typeId.equals("jdk.SocketRead") ? "bytesRead" : "bytesWritten";
                Object bytes = getMember(item, bytesAttr).orElse(null);
                if (bytes != null) {
                    detail.append(" (").append(bytes).append("B)");
                }
            }
            case "jdk.FileRead", "jdk.FileWrite" -> {
                Object path = getMember(item, "path").orElse(null);
                if (path != null) {
                    detail.append(path);
                }
            }
            case "jdk.JavaExceptionThrow" -> {
                Object thrownClass = getMember(item, "thrownClass").orElse(null);
                Object message = getMember(item, "message").orElse(null);
                if (thrownClass != null) {
                    detail.append(thrownClass);
                }
                if (message != null) {
                    detail.append(": ").append(message);
                }
            }
        }

        return detail.toString();
    }

    private String extractTopFrame(Object stackTraceObj) {
        String full = formatStackTrace(stackTraceObj, 1);
        if (full.startsWith("at ")) {
            return full.substring(3).trim();
        }
        return full.trim();
    }

    private static class MutablePhaseSummary {
        final String phaseName;
        long totalTimeMs;
        int eventCount;

        MutablePhaseSummary(String phaseName) {
            this.phaseName = phaseName;
        }
    }
}
