package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Smart tool that detects JDBC N+1 query patterns by analyzing sequential short-duration
 * socket I/O events on the same thread, correlated with SQL/ORM stack traces.
 */
public final class SmartJdbcNPlusOneAnalyzerTool {

    private static final String NAME = "smart_jdbc_n_plus_one_analyzer";
    private final JfrAnalysisService service;

    private static final Pattern JDBC_PATTERN = Pattern.compile(
            "java\\.sql\\.|javax\\.sql\\.|oracle\\.jdbc|org\\.postgresql|com\\.mysql|org\\.h2|com\\.microsoft\\.sqlserver|com\\.ibm\\.db2");
    private static final Pattern ORM_PATTERN = Pattern.compile(
            "org\\.hibernate|org\\.eclipse\\.linkage|org\\.apache\\.openjpa|com\\.ibatis|org\\.mybatis|org\\.springframework\\.orm");
    private static final long SHORT_DURATION_NS = 1_000_000; // < 1ms
    private static final int MIN_BURST_SIZE = 5;
    private static final long MAX_BURST_GAP_NS = 10_000_000; // 10ms gap between events in a burst

    public SmartJdbcNPlusOneAnalyzerTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Smart tool that detects JDBC N+1 query anti-patterns by analyzing " +
                                "sequential short-duration socket I/O events correlated with SQL/ORM stack traces.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top N+1 patterns to return (default 5)", 5)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 5);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        // Collect socket read/write events
        List<SocketEvent> socketEvents = new ArrayList<>();
        collectSocketEvents(events, "jdk.SocketRead", socketEvents);
        collectSocketEvents(events, "jdk.SocketWrite", socketEvents);

        if (socketEvents.isEmpty()) {
            return "# Smart JDBC N+1 Analyzer\n\nNo socket I/O events found in the recording.\n";
        }

        // Sort by thread then by time
        socketEvents.sort(Comparator.comparing((SocketEvent e) -> e.threadName)
                .thenComparingLong(e -> e.startTimeNanos));

        // Detect bursts per thread
        List<NPlusOnePattern> patterns = detectBursts(socketEvents);

        if (patterns.isEmpty()) {
            return "# Smart JDBC N+1 Analyzer\n\nNo N+1 query patterns detected. No thread showed a sustained burst of short sequential DB socket reads.\n";
        }

        // Sort by confidence then by total reads
        patterns.sort((a, b) -> {
            int cmp = Double.compare(b.confidence, a.confidence);
            if (cmp != 0) return cmp;
            return Long.compare(b.totalReads, a.totalReads);
        });

        StringBuilder sb = new StringBuilder();
        sb.append("# Smart JDBC N+1 Analyzer\n\n");
        sb.append("Analyzed ").append(socketEvents.size()).append(" socket I/O events.\n");
        sb.append("Detected ").append(patterns.size()).append(" potential N+1 pattern(s).\n\n");

        for (int i = 0; i < Math.min(topN, patterns.size()); i++) {
            NPlusOnePattern p = patterns.get(i);
            sb.append("## Pattern #").append(i + 1).append("\n\n");
            sb.append("- **Triggering Method:** `").append(p.triggeringMethod).append("`\n");
            sb.append("- **Thread:** `").append(p.threadName).append("`\n");
            sb.append("- **Sequential Reads:** ").append(p.totalReads).append("\n");
            sb.append("- **Burst Window:** ").append(String.format("%.2f ms", p.burstWindowMs)).append("\n");
            sb.append("- **Avg Read Duration:** ").append(String.format("%.3f ms", p.avgDurationMs)).append("\n");
            sb.append("- **Confidence:** ").append(String.format("%.0f%%", p.confidence * 100)).append("\n");
            if (p.hasOrm) {
                sb.append("- **ORM Framework:** Detected in stack trace\n");
            }
            sb.append("\n**Sample Stack Trace:**\n\n");
            sb.append("```\n").append(p.sampleTrace).append("\n```\n\n");
        }

        // Agent hint
        NPlusOnePattern worst = patterns.get(0);
        StringBuilder hint = new StringBuilder();
        hint.append("Detected an N+1 query pattern in `").append(worst.triggeringMethod).append("`. ");
        hint.append(worst.totalReads).append(" sequential DB socket reads occurred in ");
        hint.append(String.format("%.0f ms", worst.burstWindowMs)).append(". ");
        if (worst.hasOrm) {
            hint.append("An ORM framework is involved. ");
        }
        hint.append("Consider refactoring to use a SQL JOIN, batch fetching (e.g., `@BatchSize` or `JOIN FETCH`), or an Entity Graph.");
        hint.append(" Use `smart_stack_trace_search` with `class_pattern` to find all occurrences of this method.");

        sb.append("<agent_hint>").append(hint).append("</agent_hint>\n");

        return sb.toString();
    }

    private void collectSocketEvents(IItemCollection events, String typeId, List<SocketEvent> result) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> startAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.START_TIME.getIdentifier());
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (threadAcc == null || startAcc == null) continue;

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                IQuantity startTime = startAcc.getMember(item);
                IQuantity duration = durationAcc != null ? durationAcc.getMember(item) : null;
                Object stackObj = stackAcc != null ? stackAcc.getMember(item) : null;

                if (threadObj == null || startTime == null) continue;

                String threadName = extractThreadName(threadObj);
                long startNanos = startTime.clampedLongValueIn(UnitLookup.NANOSECOND);
                long durationNanos = duration != null ? duration.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
                String fullTrace = JfrItemUtils.formatFullStackTrace(stackObj);

                // Only include DB-related socket events
                if (fullTrace != null && JDBC_PATTERN.matcher(fullTrace).find()) {
                    result.add(new SocketEvent(threadName, startNanos, durationNanos, fullTrace));
                }
            }
        }
    }

    private List<NPlusOnePattern> detectBursts(List<SocketEvent> events) {
        List<NPlusOnePattern> patterns = new ArrayList<>();

        // Group by thread
        Map<String, List<SocketEvent>> byThread = new HashMap<>();
        for (SocketEvent e : events) {
            byThread.computeIfAbsent(e.threadName, k -> new ArrayList<>()).add(e);
        }

        for (var entry : byThread.entrySet()) {
            List<SocketEvent> threadEvents = entry.getValue();
            if (threadEvents.size() < MIN_BURST_SIZE) continue;

            // Sliding window to find bursts
            int i = 0;
            while (i < threadEvents.size()) {
                // Start a potential burst
                int burstStart = i;
                long burstStartTime = threadEvents.get(i).startTimeNanos;
                long burstEndTime = burstStartTime;
                int shortCount = threadEvents.get(i).durationNanos < SHORT_DURATION_NS ? 1 : 0;

                int j = i + 1;
                while (j < threadEvents.size()) {
                    SocketEvent prev = threadEvents.get(j - 1);
                    SocketEvent curr = threadEvents.get(j);
                    long gap = curr.startTimeNanos - (prev.startTimeNanos + prev.durationNanos);
                    if (gap > MAX_BURST_GAP_NS) break;

                    burstEndTime = curr.startTimeNanos + curr.durationNanos;
                    if (curr.durationNanos < SHORT_DURATION_NS) shortCount++;
                    j++;
                }

                int burstSize = j - burstStart;
                if (burstSize >= MIN_BURST_SIZE) {
                    long totalReads = burstSize;
                    double windowMs = (burstEndTime - burstStartTime) / 1_000_000.0;
                    double avgDurationMs = threadEvents.subList(burstStart, j).stream()
                            .mapToLong(e -> e.durationNanos).average().orElse(0) / 1_000_000.0;

                    // Extract the triggering method from the most common stack trace
                    Map<String, Long> methodCounts = new HashMap<>();
                    String sampleTrace = null;
                    boolean hasOrm = false;

                    for (int k = burstStart; k < j; k++) {
                        SocketEvent e = threadEvents.get(k);
                        if (e.fullTrace != null) {
                            String method = extractTriggeringMethod(e.fullTrace);
                            methodCounts.merge(method, 1L, Long::sum);
                            if (sampleTrace == null) sampleTrace = e.fullTrace;
                            if (ORM_PATTERN.matcher(e.fullTrace).find()) hasOrm = true;
                        }
                    }

                    String triggeringMethod = methodCounts.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Unknown");

                    // Confidence scoring
                    double confidence = calculateConfidence(totalReads, windowMs, shortCount, burstSize, hasOrm);

                    patterns.add(new NPlusOnePattern(
                            entry.getKey(), triggeringMethod, totalReads, windowMs,
                            avgDurationMs, confidence, hasOrm, sampleTrace
                    ));
                }

                i = j > i + 1 ? j : i + 1;
            }
        }

        return patterns;
    }

    private double calculateConfidence(long totalReads, double windowMs, int shortCount, int burstSize, boolean hasOrm) {
        double score = 0.0;

        // More reads = higher confidence
        if (totalReads >= 100) score += 0.3;
        else if (totalReads >= 50) score += 0.2;
        else if (totalReads >= 20) score += 0.1;
        else score += 0.05;

        // Short duration ratio
        double shortRatio = (double) shortCount / burstSize;
        score += shortRatio * 0.2;

        // Tight window
        if (windowMs > 0 && totalReads / windowMs > 10) score += 0.2; // > 10 reads per ms
        else if (windowMs > 0 && totalReads / windowMs > 5) score += 0.1;

        // ORM involvement
        if (hasOrm) score += 0.2;

        // JDBC direct (slightly less confident than ORM)
        score += 0.1;

        return Math.min(1.0, score);
    }

    private static String extractTriggeringMethod(String fullTrace) {
        String[] lines = fullTrace.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("at ")) {
                String method = trimmed.substring(3).trim();
                // Skip JDBC/ORM infrastructure frames, find application frame
                if (!method.startsWith("java.sql.") &&
                        !method.startsWith("oracle.jdbc") &&
                        !method.startsWith("org.hibernate") &&
                        !method.startsWith("com.zaxxer.hikari") &&
                        !method.startsWith("java.net.") &&
                        !method.startsWith("sun.nio.") &&
                        !method.startsWith("jdk.internal")) {
                    // Return first non-infrastructure frame
                    int parenIdx = method.indexOf('(');
                    if (parenIdx > 0) {
                        return method.substring(0, parenIdx).trim();
                    }
                    return method;
                }
            }
        }
        // Fallback: return first frame
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("at ")) {
                String method = trimmed.substring(3).trim();
                int parenIdx = method.indexOf('(');
                if (parenIdx > 0) return method.substring(0, parenIdx).trim();
                return method;
            }
        }
        return "Unknown";
    }

    private static String extractThreadName(Object threadObj) {
        if (threadObj == null) return "unknown";
        String s = threadObj.toString();
        int start = s.indexOf("'");
        int end = s.lastIndexOf("'");
        if (start >= 0 && end > start) {
            return s.substring(start + 1, end);
        }
        return s;
    }

    private record SocketEvent(String threadName, long startTimeNanos, long durationNanos, String fullTrace) {}

    private record NPlusOnePattern(
            String threadName,
            String triggeringMethod,
            long totalReads,
            double burstWindowMs,
            double avgDurationMs,
            double confidence,
            boolean hasOrm,
            String sampleTrace
    ) {}
}
