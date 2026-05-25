package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * MCP tool for end-to-end request tracing via thread waterfall view.
 * Reconstructs the chronological sequence of events for a specific thread,
 * showing lock acquisitions, I/O operations, CPU samples, and exceptions.
 */
public final class RequestWaterfallTool {

    private static final String NAME = "request_waterfall";

    private static final List<String> WATERFALL_EVENT_TYPES = List.of(
            "jdk.ExecutionSample", "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait",
            "jdk.ThreadPark", "jdk.SocketRead", "jdk.SocketWrite",
            "jdk.FileRead", "jdk.FileWrite", "jdk.JavaExceptionThrow"
    );

    private final JfrAnalysisService service;

    public RequestWaterfallTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Trace a single request end-to-end by reconstructing the chronological " +
                                "sequence of events (locks, I/O, CPU, exceptions) for a specific thread.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "thread_name", SchemaUtil.stringProp("Exact thread name or regex pattern to match"),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "max_events", SchemaUtil.intProp("Maximum events in waterfall (default 100)", 100),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path", "thread_name")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String threadName = SchemaUtil.getString(request.arguments(), "thread_name");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    int maxEvents = SchemaUtil.getIntOrDefault(request.arguments(), "max_events", 100);
                    return analyze(filePath, threadName, startTimeStr, endTimeStr, maxEvents);
                }))
                .build();
    }

    public String analyze(String filePath, String threadName, String startTimeStr, String endTimeStr, int maxEvents) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        Pattern threadPattern;
        try {
            threadPattern = Pattern.compile(threadName);
        } catch (Exception e) {
            return "# Request Waterfall\n\nInvalid thread name regex: " + threadName;
        }

        List<WaterfallEvent> waterfallEvents = new ArrayList<>();
        Map<String, Long> eventTypeCounts = new LinkedHashMap<>();

        for (String typeId : WATERFALL_EVENT_TYPES) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            if (!typeEvents.hasItems()) continue;

            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
                if (threadAccessor == null) {
                    threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");
                }
                if (threadAccessor == null) continue;

                IMemberAccessor<IQuantity, IItem> startTimeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

                for (IItem item : iterable) {
                    Object threadObj = threadAccessor.getMember(item);
                    if (threadObj == null) continue;
                    String tName = threadObj.toString();
                    if (!threadPattern.matcher(tName).find() && !tName.equals(threadName)) continue;

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

                    waterfallEvents.add(new WaterfallEvent(timeMs, typeId, phase, durationMs, detail, topFrame, fullTrace, tName));
                    eventTypeCounts.merge(typeId, 1L, Long::sum);
                }
            }
        }

        if (waterfallEvents.isEmpty()) {
            return "# Request Waterfall\n\nNo events found for thread pattern: `" + threadName + "`";
        }

        waterfallEvents.sort(Comparator.comparingLong(WaterfallEvent::timeMs));

        long baseTimeMs = waterfallEvents.get(0).timeMs;
        long endTimeMs = waterfallEvents.get(waterfallEvents.size() - 1).timeMs;

        if (waterfallEvents.size() > maxEvents) {
            waterfallEvents = waterfallEvents.subList(0, maxEvents);
        }

        Map<String, PhaseSummary> phaseMap = new LinkedHashMap<>();
        for (WaterfallEvent we : waterfallEvents) {
            PhaseSummary ps = phaseMap.computeIfAbsent(we.phase, k -> new PhaseSummary(k));
            ps.totalTimeMs += we.durationMs;
            ps.eventCount++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Request Waterfall\n\n");

        sb.append("## Thread Summary\n\n");
        Set<String> matchedThreads = new LinkedHashSet<>();
        for (WaterfallEvent we : waterfallEvents) {
            matchedThreads.add(we.threadName);
        }
        sb.append("- **Matched Thread(s):** ").append(String.join(", ", matchedThreads)).append("\n");
        sb.append("- **Total Events:** ").append(waterfallEvents.size()).append("\n");
        sb.append("- **Time Span:** ").append(SchemaUtil.formatDuration(endTimeMs - baseTimeMs)).append("\n\n");

        sb.append("| Event Type | Count |\n|------------|-------|\n");
        eventTypeCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> sb.append("| `").append(e.getKey()).append("` | ").append(e.getValue()).append(" |\n"));
        sb.append("\n");

        sb.append("## Waterfall Timeline\n\n");
        sb.append("| Time | Event Type | Phase | Duration | Detail | Top Frame |\n");
        sb.append("|------|------------|-------|----------|--------|-----------|\n");
        for (WaterfallEvent we : waterfallEvents) {
            long offsetMs = we.timeMs - baseTimeMs;
            sb.append(String.format("| +%dms | `%s` | %s | %s | %s | `%s` |\n",
                    offsetMs,
                    we.eventType,
                    we.phase,
                    we.durationMs > 0 ? we.durationMs + "ms" : "—",
                    we.detail.isEmpty() ? "—" : we.detail,
                    we.topFrame.isEmpty() ? "—" : we.topFrame
            ));
        }
        sb.append("\n");

        sb.append("## Phase Breakdown\n\n");
        sb.append("| Phase | Total Time | % of Recorded | Event Count |\n");
        sb.append("|-------|-----------|---------------|-------------|\n");
        long totalRecordedMs = endTimeMs - baseTimeMs;
        for (PhaseSummary ps : phaseMap.values()) {
            double pct = totalRecordedMs > 0 ? (ps.totalTimeMs * 100.0 / totalRecordedMs) : 0;
            sb.append(String.format("| %s | %s | %.1f%% | %d |\n",
                    ps.phaseName,
                    ps.totalTimeMs > 0 ? SchemaUtil.formatDuration(ps.totalTimeMs) : "—",
                    pct,
                    ps.eventCount));
        }
        sb.append("\n");

        String dominantPhase = phaseMap.values().stream()
                .max(Comparator.comparingLong(PhaseSummary::getTotalTimeMs))
                .map(ps -> ps.phaseName).orElse("");
        sb.append("<agent_hint>");
        if ("BLOCKED".equals(dominantPhase)) {
            sb.append("Thread spends most time blocked. Consider `thread_contention` for lock details or `correlate` to see what I/O happens under locks.");
        } else if ("IO".equals(dominantPhase)) {
            sb.append("Thread spends most time in I/O. Consider `io_hotspots` for endpoint-level analysis.");
        } else if ("CPU".equals(dominantPhase)) {
            sb.append("Thread spends most time on CPU. Consider `hot_methods` with `package_prefix` for application-level hot spots.");
        } else {
            sb.append("Waterfall trace complete. Consider `correlate` for cross-dimensional analysis or `stack_trace_search` to find specific methods.");
        }
        sb.append("</agent_hint>\n");

        return sb.toString();
    }

    private String classifyPhase(String typeId) {
        if (typeId.equals("jdk.JavaMonitorEnter") || typeId.equals("jdk.JavaMonitorWait")) return "BLOCKED";
        if (typeId.equals("jdk.ThreadPark")) return "WAITING";
        if (typeId.equals("jdk.SocketRead") || typeId.equals("jdk.SocketWrite") ||
                typeId.equals("jdk.FileRead") || typeId.equals("jdk.FileWrite")) return "IO";
        if (typeId.equals("jdk.ExecutionSample")) return "CPU";
        if (typeId.equals("jdk.JavaExceptionThrow")) return "EXCEPTION";
        return "OTHER";
    }

    private String extractDetail(IType<IItem> type, IItem item) {
        String typeId = type.getIdentifier();
        StringBuilder detail = new StringBuilder();

        switch (typeId) {
            case "jdk.JavaMonitorEnter", "jdk.JavaMonitorWait" -> {
                Object monitor = JfrItemUtils.getMember(item, "monitorClass").orElse(null);
                if (monitor != null) detail.append(monitor.toString());
            }
            case "jdk.SocketRead", "jdk.SocketWrite" -> {
                Object host = JfrItemUtils.getMember(item, "host").orElse(null);
                Object port = JfrItemUtils.getMember(item, "port").orElse(null);
                if (host != null) {
                    detail.append(host);
                    if (port != null) detail.append(":").append(port);
                }
                String bytesAttr = typeId.equals("jdk.SocketRead") ? "bytesRead" : "bytesWritten";
                Object bytes = JfrItemUtils.getMember(item, bytesAttr).orElse(null);
                if (bytes != null) detail.append(" (").append(bytes).append("B)");
            }
            case "jdk.FileRead", "jdk.FileWrite" -> {
                Object path = JfrItemUtils.getMember(item, "path").orElse(null);
                if (path != null) detail.append(path);
            }
            case "jdk.JavaExceptionThrow" -> {
                Object thrownClass = JfrItemUtils.getMember(item, "thrownClass").orElse(null);
                Object message = JfrItemUtils.getMember(item, "message").orElse(null);
                if (thrownClass != null) detail.append(thrownClass);
                if (message != null) detail.append(": ").append(message);
            }
        }

        return detail.toString();
    }

    private String extractTopFrame(Object stackTraceObj) {
        String full = JfrItemUtils.formatStackTrace(stackTraceObj, 1);
        if (full.startsWith("at ")) return full.substring(3).trim();
        return full.trim();
    }

    private record WaterfallEvent(long timeMs, String eventType, String phase, long durationMs,
                                  String detail, String topFrame, String fullTrace, String threadName) {
    }

    private static class PhaseSummary {
        final String phaseName;
        long totalTimeMs;
        int eventCount;

        PhaseSummary(String phaseName) {
            this.phaseName = phaseName;
        }

        long getTotalTimeMs() {
            return totalTimeMs;
        }
    }
}