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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ThreadPoolAnalysisTool {

    private static final String NAME = "thread_pool_analysis";

    private static final Pattern THREAD_POOL_PATTERN = Pattern.compile("^(.+?)-(\\d+)$");
    private static final Set<String> KNOWN_POOL_PREFIXES = Set.of(
            "http-nio", "https-nio", "pool", "ForkJoinPool", "worker", "task-",
            "exec-", "scheduler-", "async-", "db-", "HikariPool", "catalina-exec"
    );

    private final JfrAnalysisService service;

    public ThreadPoolAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread pool utilization and detect thread pool starvation. " +
                                "Groups threads by name prefix to identify pools, computes active ratios, " +
                                "and detects saturation patterns.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top pools to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection cpuSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        IItemCollection monitorEnter = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        IItemCollection monitorWait = events.apply(ItemFilters.type("jdk.JavaMonitorWait"));
        IItemCollection threadPark = events.apply(ItemFilters.type("jdk.ThreadPark"));
        IItemCollection threadSleep = events.apply(ItemFilters.type("jdk.ThreadSleep"));

        if (!cpuSamples.hasItems() && !monitorEnter.hasItems() && !threadPark.hasItems()) {
            return "# Thread Pool Analysis\n\nInsufficient thread activity events found for pool analysis.";
        }

        Map<String, PoolStats> poolStats = new HashMap<>();

        collectCpuSamples(cpuSamples, poolStats);
        collectBlockingTime(monitorEnter, "monitor_enter", poolStats);
        collectBlockingTime(monitorWait, "monitor_wait", poolStats);
        collectBlockingTime(threadPark, "thread_park", poolStats);
        collectBlockingTime(threadSleep, "thread_sleep", poolStats);

        if (poolStats.isEmpty()) {
            return "# Thread Pool Analysis\n\nNo identifiable thread pools found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Pool Analysis\n\n");

        List<String> warnings = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();

        sb.append("## Thread Pool Summary\n\n");
        sb.append("| Pool Prefix | Thread Count | CPU Samples | Blocked Time | Blocked Count | Active Ratio | Status |\n");
        sb.append("|-------------|-------------|-------------|-------------|---------------|-------------|--------|\n");

        poolStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().cpuSamples, a.getValue().cpuSamples))
                .limit(topN)
                .forEach(entry -> {
                    PoolStats s = entry.getValue();
                    double activeRatio = s.cpuSamples > 0 ? (double) s.cpuSamples / (s.cpuSamples + s.blockedCount) * 100 : 0;
                    String status = determineStatus(s, activeRatio);

                    sb.append(String.format("| `%s` | %d | %d | %s | %d | %.1f%% | %s |\n",
                            entry.getKey(),
                            s.threadCount,
                            s.cpuSamples,
                            SchemaUtil.formatDuration(s.blockedTimeMs),
                            s.blockedCount,
                            activeRatio,
                            status));

                    if (activeRatio > 95 && s.blockedCount > s.cpuSamples) {
                        warnings.add(String.format("Pool '%s' shows %.0f%% utilization with high blocking — pool may be saturated", entry.getKey(), activeRatio));
                        recommendations.add(String.format("For '%s': consider increasing max threads or reducing task duration", entry.getKey()));
                    }
                    if (s.blockedTimeMs > 0 && s.blockedCount > 0) {
                        long avgBlockMs = s.blockedTimeMs / s.blockedCount;
                        if (avgBlockMs > 1000) {
                            warnings.add(String.format("Pool '%s' has avg blocking time of %ds — tasks are waiting too long", entry.getKey(), avgBlockMs / 1000));
                        }
                    }
                });
        sb.append("\n");

        if (!warnings.isEmpty()) {
            sb.append("## ⚠️ Warnings\n\n");
            for (String warning : warnings) {
                sb.append("- ").append(warning).append("\n");
            }
            sb.append("\n");
        }

        if (!recommendations.isEmpty()) {
            sb.append("## Recommendations\n\n");
            for (int i = 0; i < recommendations.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, recommendations.get(i)));
            }
            sb.append("\n");
        }

        sb.append("## Blocking Breakdown by Type\n\n");
        sb.append("| Pool Prefix | Monitor Enter | Monitor Wait | Thread Park | Thread Sleep |\n");
        sb.append("|-------------|---------------|-------------|-------------|-------------|\n");
        poolStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().cpuSamples, a.getValue().cpuSamples))
                .limit(topN)
                .forEach(entry -> {
                    PoolStats s = entry.getValue();
                    sb.append(String.format("| `%s` | %d | %d | %d | %d |\n",
                            entry.getKey(),
                            s.monitorEnterCount,
                            s.monitorWaitCount,
                            s.parkCount,
                            s.sleepCount));
                });

        return sb.toString();
    }

    private String extractPoolPrefix(String threadName) {
        Matcher m = THREAD_POOL_PATTERN.matcher(threadName);
        if (m.matches()) {
            String prefix = m.group(1);
            for (String known : KNOWN_POOL_PREFIXES) {
                if (prefix.startsWith(known) || known.startsWith(prefix)) {
                    return prefix;
                }
            }
            return prefix;
        }

        int dashIdx = threadName.lastIndexOf('-');
        if (dashIdx > 0) {
            String prefix = threadName.substring(0, dashIdx);
            try {
                Integer.parseInt(threadName.substring(dashIdx + 1));
                return prefix;
            } catch (NumberFormatException ignored) {}
        }

        return threadName;
    }

    private void collectCpuSamples(IItemCollection events, Map<String, PoolStats> poolStats) {
        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            if (threadAccessor != null) {
                for (IItem item : iterable) {
                    Object thread = threadAccessor.getMember(item);
                    if (thread != null) {
                        String threadName = thread.toString();
                        String pool = extractPoolPrefix(threadName);
                        poolStats.computeIfAbsent(pool, k -> new PoolStats())
                                .cpuSamples++;
                        poolStats.get(pool).threadNames.add(threadName);
                    }
                }
            }
        }
    }

    private void collectBlockingTime(IItemCollection events, String blockType, Map<String, PoolStats> poolStats) {
        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> threadAccessor = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());

            if (threadAccessor != null) {
                for (IItem item : iterable) {
                    Object thread = threadAccessor.getMember(item);
                    if (thread == null) continue;

                    String threadName = thread.toString();
                    String pool = extractPoolPrefix(threadName);
                    PoolStats stats = poolStats.computeIfAbsent(pool, k -> new PoolStats());
                    stats.threadNames.add(threadName);
                    stats.blockedCount++;

                    if (durationAccessor != null) {
                        IQuantity duration = durationAccessor.getMember(item);
                        if (duration != null) {
                            stats.blockedTimeMs += duration.clampedLongValueIn(UnitLookup.MILLISECOND);
                        }
                    }

                    switch (blockType) {
                        case "monitor_enter" -> stats.monitorEnterCount++;
                        case "monitor_wait" -> stats.monitorWaitCount++;
                        case "thread_park" -> stats.parkCount++;
                        case "thread_sleep" -> stats.sleepCount++;
                    }
                }
            }
        }
    }

    private String determineStatus(PoolStats s, double activeRatio) {
        if (activeRatio > 95 && s.blockedCount > s.cpuSamples) return "⛔ SATURATED";
        if (activeRatio > 80) return "⚠️ HIGH";
        if (activeRatio > 50) return "🟡 MODERATE";
        return "✅ HEALTHY";
    }

    private static class PoolStats {
        Set<String> threadNames = new HashSet<>();
        int threadCount;
        long cpuSamples;
        long blockedCount;
        long blockedTimeMs;
        long monitorEnterCount;
        long monitorWaitCount;
        long parkCount;
        long sleepCount;

        int getThreadCount() {
            return threadNames.size();
        }
    }
}