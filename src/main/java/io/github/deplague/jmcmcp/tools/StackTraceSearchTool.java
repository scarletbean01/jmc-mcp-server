package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * MCP tool for full-text stack trace search across all JFR event types.
 * Searches for a class/method pattern in stack traces across lock, I/O, exception,
 * allocation, and execution sample events with full (non-truncated) traces.
 */
public final class StackTraceSearchTool {

    private static final String NAME = "smart_stack_trace_search";

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

    private final JfrAnalysisService service;

    public StackTraceSearchTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Search for a class/method pattern across all JFR event types that contain stack traces. " +
                                "Returns full (non-truncated) stack traces with event-specific details.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "class_pattern", SchemaUtil.stringProp("Regex pattern to match against class names in stack traces (e.g., '.*TenantService.*', '.*DAO.*')"),
                                        "event_type", SchemaUtil.stringProp("Filter to specific event type (e.g., 'jdk.JavaMonitorEnter'), or 'all'"),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "limit", SchemaUtil.intProp("Maximum results to return (default 20)", 20),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path", "class_pattern")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String classPattern = SchemaUtil.getString(request.arguments(), "class_pattern");
                    String eventType = SchemaUtil.getStringOrDefault(request.arguments(), "event_type", "all");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    int limit = SchemaUtil.getIntOrDefault(request.arguments(), "limit", 20);
                    return analyze(filePath, classPattern, eventType, startTimeStr, endTimeStr, limit);
                }))
                .build();
    }

    public String analyze(String filePath, String classPattern, String eventType, String startTimeStr, String endTimeStr, int limit) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        Pattern pattern;
        try {
            pattern = Pattern.compile(classPattern);
        } catch (Exception e) {
            return "# Stack Trace Search\n\nInvalid regex pattern: " + classPattern + "\nError: " + e.getMessage();
        }

        List<String> typesToSearch = "all".equals(eventType) ? SEARCHABLE_EVENT_TYPES : List.of(eventType);

        List<MatchResult> matches = new ArrayList<>();
        Map<String, Long> distribution = new LinkedHashMap<>();

        // Caches leveraging JFR stack-trace deduplication (same IMCStackTrace instance reused)
        Map<IMCStackTrace, Boolean> matchCache = new IdentityHashMap<>();
        Map<IMCStackTrace, String> formattedTraceCache = new IdentityHashMap<>();

        for (String typeId : typesToSearch) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            if (!typeEvents.hasItems()) continue;

            long typeMatchCount = 0;
            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (stackAccessor == null) continue;

                IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
                if (threadAccessor == null) {
                    threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "sampledThread");
                }
                IMemberAccessor<IQuantity, IItem> startTimeAccessor = JfrAttributes.START_TIME.getAccessor(iterable.getType());

                List<String> detailFields = EVENT_DETAIL_FIELDS.getOrDefault(typeId, List.of());
                Map<String, IMemberAccessor<Object, IItem>> detailAccessors = new HashMap<>();
                for (String field : detailFields) {
                    IMemberAccessor<Object, IItem> acc = JfrItemUtils.getAccessor(iterable.getType(), field);
                    if (acc != null) {
                        detailAccessors.put(field, acc);
                    }
                }

                for (IItem item : iterable) {
                    Object stackObj = stackAccessor.getMember(item);
                    if (!(stackObj instanceof IMCStackTrace stackTrace)) continue;

                    Boolean cachedMatch = matchCache.get(stackTrace);
                    if (cachedMatch == null) {
                        boolean isMatch = JfrItemUtils.stackTraceMatches(stackTrace, pattern);
                        matchCache.put(stackTrace, isMatch);
                        cachedMatch = isMatch;
                        if (isMatch) {
                            formattedTraceCache.put(stackTrace, JfrItemUtils.formatFullStackTrace(stackTrace));
                        }
                    }

                    if (!cachedMatch) continue;
                    typeMatchCount++;
                    if (matches.size() >= limit) continue;

                    String fullTrace = formattedTraceCache.get(stackTrace);

                    String threadName = threadAccessor != null ? threadAccessor.getMember(item).toString() : "Unknown";
                    String timestamp = startTimeAccessor != null ? JfrAnalysisService.display(startTimeAccessor.getMember(item)) : "N/A";

                    Map<String, String> details = new LinkedHashMap<>();
                    for (var entry : detailAccessors.entrySet()) {
                        Object val = entry.getValue().getMember(item);
                        if (val != null) {
                            details.put(entry.getKey(), val.toString());
                        }
                    }

                    matches.add(new MatchResult(typeId, timestamp, threadName, fullTrace, details));
                }
            }

            if (typeMatchCount > 0) {
                distribution.put(typeId, typeMatchCount);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Stack Trace Search\n\n");
        sb.append("**Pattern:** `").append(classPattern).append("`  \n");
        sb.append("**Event types searched:** ").append("all".equals(eventType) ? "all" : eventType).append("  \n");
        sb.append("**Total matches found:** ").append(matches.size() >= limit ? limit + "+" : matches.size()).append("\n\n");

        sb.append("## Matching Stack Traces\n\n");
        for (int i = 0; i < matches.size(); i++) {
            MatchResult m = matches.get(i);
            sb.append("### Match ").append(i + 1).append("\n");
            sb.append("- **Event Type:** `").append(m.eventType).append("`\n");
            sb.append("- **Timestamp:** ").append(m.timestamp).append("\n");
            sb.append("- **Thread:** ").append(m.threadName).append("\n");
            if (!m.details.isEmpty()) {
                for (var entry : m.details.entrySet()) {
                    sb.append("- **").append(entry.getKey()).append(":** ").append(entry.getValue()).append("\n");
                }
            }
            sb.append("- **Stack Trace:**\n```\n").append(m.fullTrace).append("\n```\n\n");
        }

        sb.append("## Class Distribution\n\n");
        sb.append("| Event Type | Matches |\n");
        sb.append("|------------|--------|\n");
        distribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> sb.append("| `").append(e.getKey()).append("` | ").append(e.getValue()).append(" |\n"));
        sb.append("\n");

        if (!distribution.isEmpty()) {
            String topType = distribution.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse("");
            long topCount = distribution.values().stream().max(Long::compare).orElse(0L);

            sb.append("<agent_hint>Found ").append(topCount).append(" matches in `").append(topType).append("`.");
            if (topType.contains("Monitor") || topType.contains("Park")) {
                sb.append(" Consider `thread_contention` for detailed lock analysis.");
            } else if (topType.contains("Socket") || topType.contains("File")) {
                sb.append(" Consider `io_hotspots` for I/O performance details.");
            } else if (topType.contains("Exception") || topType.contains("Error")) {
                sb.append(" Consider `error_analysis` or `exception_analysis` for exception details.");
            } else if (topType.contains("Allocation")) {
                sb.append(" Consider `allocation_hotspots` for memory allocation analysis.");
            } else if (topType.contains("ExecutionSample")) {
                sb.append(" Consider `hot_methods` for CPU hot spot analysis.");
            }
            sb.append("</agent_hint>\n");
        }

        return sb.toString();
    }

    private record MatchResult(String eventType, String timestamp, String threadName, String fullTrace, Map<String, String> details) {
    }
}