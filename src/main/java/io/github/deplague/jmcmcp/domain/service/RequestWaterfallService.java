package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.RequestWaterfallEvent;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallResult;
import io.github.deplague.jmcmcp.domain.model.WaterfallPhaseSummary;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for reconstructing a chronological waterfall of events
 * for a specific thread. Contains no MCP-specific or UI formatting logic.
 */
public final class RequestWaterfallService {

    private static final List<String> WATERFALL_EVENT_TYPES = List.of(
            "jdk.ExecutionSample", "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait",
            "jdk.ThreadPark", "jdk.SocketRead", "jdk.SocketWrite",
            "jdk.FileRead", "jdk.FileWrite", "jdk.JavaExceptionThrow"
    );

    public RequestWaterfallResult analyze(
            IItemCollection events,
            String threadName,
            int maxEvents) {

        Pattern threadPattern = Pattern.compile(threadName);

        List<RequestWaterfallEvent> waterfallEvents = new ArrayList<>();
        Map<String, Long> eventTypeCounts = new LinkedHashMap<>();

        for (String typeId : WATERFALL_EVENT_TYPES) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            if (!typeEvents.hasItems()) {
                continue;
            }

            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> threadAccessor =
                        JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
                if (threadAccessor == null) {
                    threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");
                }
                if (threadAccessor == null) {
                    continue;
                }

                IMemberAccessor<IQuantity, IItem> startTimeAccessor =
                        JfrAttributes.START_TIME.getAccessor(iterable.getType());
                IMemberAccessor<IQuantity, IItem> durationAccessor =
                        JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor =
                        JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

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
                            timeMs = startQ.clampedLongValueIn(UnitLookup.EPOCH_MS);
                        }
                    }

                    long durationMs = 0;
                    if (durationAccessor != null) {
                        IQuantity durQ = durationAccessor.getMember(item);
                        if (durQ != null) {
                            durationMs = durQ.clampedLongValueIn(UnitLookup.MILLISECOND);
                        }
                    }

                    String topFrame = "";
                    String fullTrace = "";
                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) {
                            topFrame = extractTopFrame(st);
                            fullTrace = JfrItemUtils.formatFullStackTrace(st);
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
                    List.of(), eventTypeCounts, List.of(),
                    Set.of(), 0, 0, false);
        }

        waterfallEvents.sort(Comparator.comparingLong(RequestWaterfallEvent::timeMs));

        long baseTimeMs = waterfallEvents.get(0).timeMs();
        long endTimeMs = waterfallEvents.get(waterfallEvents.size() - 1).timeMs();

        if (waterfallEvents.size() > maxEvents) {
            waterfallEvents = waterfallEvents.subList(0, maxEvents);
        }

        Set<String> matchedThreads = new java.util.LinkedHashSet<>();
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

    private String extractDetail(org.openjdk.jmc.common.item.IType<IItem> type, IItem item) {
        String typeId = type.getIdentifier();
        StringBuilder detail = new StringBuilder();

        switch (typeId) {
            case "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait" -> {
                Object monitor = JfrItemUtils.getMember(item, "monitorClass").orElse(null);
                if (monitor != null) {
                    detail.append(monitor.toString());
                }
            }
            case "jdk.SocketRead", "jdk.SocketWrite" -> {
                Object host = JfrItemUtils.getMember(item, "host").orElse(null);
                Object port = JfrItemUtils.getMember(item, "port").orElse(null);
                if (host != null) {
                    detail.append(host);
                    if (port != null) {
                        detail.append(":").append(port);
                    }
                }
                String bytesAttr = typeId.equals("jdk.SocketRead") ? "bytesRead" : "bytesWritten";
                Object bytes = JfrItemUtils.getMember(item, bytesAttr).orElse(null);
                if (bytes != null) {
                    detail.append(" (").append(bytes).append("B)");
                }
            }
            case "jdk.FileRead", "jdk.FileWrite" -> {
                Object path = JfrItemUtils.getMember(item, "path").orElse(null);
                if (path != null) {
                    detail.append(path);
                }
            }
            case "jdk.JavaExceptionThrow" -> {
                Object thrownClass = JfrItemUtils.getMember(item, "thrownClass").orElse(null);
                Object message = JfrItemUtils.getMember(item, "message").orElse(null);
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
        String full = JfrItemUtils.formatStackTrace(stackTraceObj, 1);
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
