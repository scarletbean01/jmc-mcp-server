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
 * Smart tool that resolves lock contention by identifying the lock holder thread
 * and analyzing what the holder is doing while other threads are blocked.
 */
public final class SmartLockResolverTool {

    private static final String NAME = "smart_lock_resolver";
    private final JfrAnalysisService service;

    private static final Pattern IO_PATTERN = Pattern.compile(
            "java\\.net\\.Socket|java\\.io\\.File|java\\.nio\\.channels|sun\\.nio\\.ch");
    private static final Pattern SQL_PATTERN = Pattern.compile(
            "java\\.sql\\.|oracle\\.jdbc|org\\.postgresql|com\\.mysql|org\\.h2|com\\.microsoft\\.sqlserver");

    public SmartLockResolverTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Smart tool that identifies lock holder threads causing contention, " +
                                "analyzes what the holder is doing while blocking others, and suggests remedies.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top lock issues to return (default 5)", 5)
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

        IItemCollection monitorEnters = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        if (!monitorEnters.hasItems()) {
            return "# Smart Lock Resolver\n\nNo monitor contention events (jdk.JavaMonitorEnter) found in the recording.\n";
        }

        // Step 1: Aggregate blocked threads per lock holder
        Map<LockHolderKey, LockHolderStats> holderStats = new HashMap<>();
        for (IItemIterable iterable : monitorEnters) {
            IMemberAccessor<Object, IItem> monitorClassAcc = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
            IMemberAccessor<Object, IItem> prevOwnerAcc = JfrItemUtils.getAccessor(iterable.getType(), "previousOwner");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (monitorClassAcc == null || prevOwnerAcc == null || durationAcc == null) continue;

            for (IItem item : iterable) {
                Object monitorClassObj = monitorClassAcc.getMember(item);
                Object prevOwnerObj = prevOwnerAcc.getMember(item);
                IQuantity duration = durationAcc.getMember(item);
                Object threadObj = threadAcc != null ? threadAcc.getMember(item) : null;
                Object stackObj = stackAcc != null ? stackAcc.getMember(item) : null;

                if (monitorClassObj == null || prevOwnerObj == null || duration == null) continue;

                String monitorClass = monitorClassObj.toString();
                String holderName = extractThreadName(prevOwnerObj);
                String blockedThread = threadObj != null ? extractThreadName(threadObj) : "unknown";
                long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                String blockedTrace = JfrItemUtils.formatStackTrace(stackObj, 5);

                LockHolderKey key = new LockHolderKey(monitorClass, holderName);
                LockHolderStats stats = holderStats.computeIfAbsent(key, k -> new LockHolderStats());
                stats.totalBlockedNanos += nanos;
                stats.blockedCount++;
                stats.blockedThreads.add(blockedThread);
                if (!blockedTrace.isEmpty() && !blockedTrace.startsWith("No stack") && !blockedTrace.startsWith("Empty")) {
                    stats.blockedTraces.merge(blockedTrace, 1L, Long::sum);
                }
            }
        }

        if (holderStats.isEmpty()) {
            return "# Smart Lock Resolver\n\nNo lock holder data could be extracted from the recording.\n";
        }

        // Step 2: For top holders, analyze what the holder was doing
        List<Map.Entry<LockHolderKey, LockHolderStats>> sorted = holderStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalBlockedNanos, a.getValue().totalBlockedNanos))
                .limit(topN)
                .toList();

        // Pre-load execution samples for holder analysis
        IItemCollection execSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        Map<String, HolderActivity> holderActivities = analyzeHolderActivities(execSamples, sorted);

        StringBuilder sb = new StringBuilder();
        sb.append("# Smart Lock Resolver\n\n");
        sb.append("Found ").append(holderStats.size()).append(" distinct lock holder patterns.\n\n");

        for (int i = 0; i < sorted.size(); i++) {
            var entry = sorted.get(i);
            LockHolderKey key = entry.getKey();
            LockHolderStats stats = entry.getValue();
            HolderActivity activity = holderActivities.get(key.holderName());

            sb.append("## Issue #").append(i + 1).append("\n\n");
            sb.append("- **Monitor:** `").append(key.monitorClass()).append("`\n");
            sb.append("- **Holder Thread:** `").append(key.holderName()).append("`\n");
            sb.append("- **Threads Blocked:** ").append(stats.blockedThreads.size()).append("\n");
            sb.append("- **Total Block Events:** ").append(stats.blockedCount).append("\n");
            sb.append("- **Total Blocked Duration:** ")
                    .append(JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(stats.totalBlockedNanos))).append("\n");

            if (activity != null) {
                sb.append("- **Holder Activity:** ").append(activity.description).append("\n");
                if (activity.topFrame != null) {
                    sb.append("- **Holder Top Frame:** `").append(activity.topFrame).append("`\n");
                }
                if (activity.hasIo) {
                    sb.append("- **⚠️ Holder is performing I/O while holding the lock**\n");
                }
                if (activity.hasSql) {
                    sb.append("- **⚠️ Holder is performing SQL while holding the lock**\n");
                }
            }

            // Show top blocked traces
            if (!stats.blockedTraces.isEmpty()) {
                sb.append("\n**Top Blocked Call Sites:**\n\n");
                stats.blockedTraces.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(3)
                        .forEach(e -> sb.append("- `").append(e.getKey().replace("\n", "`<br>`")).append("` (").append(e.getValue()).append(" times)\n"));
            }
            sb.append("\n");
        }

        // Build agent hint from the worst offender
        var worst = sorted.get(0);
        LockHolderKey worstKey = worst.getKey();
        LockHolderStats worstStats = worst.getValue();
        HolderActivity worstActivity = holderActivities.get(worstKey.holderName());

        StringBuilder hint = new StringBuilder();
        hint.append("Lock contention resolved: Thread `").append(worstKey.holderName()).append("` is holding `")
                .append(worstKey.monitorClass()).append("` and blocking ").append(worstStats.blockedThreads.size())
                .append(" other threads. ");
        if (worstActivity != null && worstActivity.hasIo) {
            hint.append("The holder is performing I/O while holding the lock — a critical anti-pattern. ");
            hint.append("Consider reducing lock scope or using `ReentrantReadWriteLock`.");
        } else if (worstActivity != null && worstActivity.hasSql) {
            hint.append("The holder is performing SQL while holding the lock. ");
            hint.append("Consider moving the SQL outside the synchronized block.");
        } else {
            hint.append("Consider using `ReentrantLock` with timeout or `StampedLock` to reduce contention.");
        }
        hint.append(" Use `smart_correlate` to see if this lock is associated with I/O hotspots.");

        sb.append("<agent_hint>").append(hint).append("</agent_hint>\n");

        return sb.toString();
    }

    private Map<String, HolderActivity> analyzeHolderActivities(IItemCollection execSamples,
                                                                 List<Map.Entry<LockHolderKey, LockHolderStats>> holders) {
        Map<String, HolderActivity> result = new HashMap<>();
        Set<String> holderNames = new HashSet<>();
        for (var entry : holders) {
            holderNames.add(entry.getKey().holderName());
        }

        for (IItemIterable iterable : execSamples) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (threadAcc == null || stackAcc == null) continue;

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                if (threadObj == null) continue;
                String threadName = extractThreadName(threadObj);
                if (!holderNames.contains(threadName)) continue;

                Object stackObj = stackAcc.getMember(item);
                String fullTrace = JfrItemUtils.formatFullStackTrace(stackObj);
                if (fullTrace == null || fullTrace.isEmpty()) continue;

                HolderActivity activity = result.computeIfAbsent(threadName, k -> new HolderActivity());
                activity.sampleCount++;
                if (activity.topFrame == null) {
                    String[] lines = fullTrace.split("\n");
                    for (String line : lines) {
                        String trimmed = line.trim();
                        if (trimmed.startsWith("at ")) {
                            activity.topFrame = trimmed.substring(3).trim();
                            break;
                        }
                    }
                }
                if (IO_PATTERN.matcher(fullTrace).find()) {
                    activity.hasIo = true;
                }
                if (SQL_PATTERN.matcher(fullTrace).find()) {
                    activity.hasSql = true;
                }
            }
        }

        for (var activity : result.values()) {
            if (activity.hasIo && activity.hasSql) {
                activity.description = "I/O + SQL operations";
            } else if (activity.hasIo) {
                activity.description = "I/O operations";
            } else if (activity.hasSql) {
                activity.description = "SQL/database operations";
            } else {
                activity.description = "CPU/compute operations";
            }
        }

        return result;
    }

    private static String extractThreadName(Object threadObj) {
        if (threadObj == null) return "unknown";
        String s = threadObj.toString();
        // JMC Thread objects often have format like "Thread 'name'"
        int start = s.indexOf("'");
        int end = s.lastIndexOf("'");
        if (start >= 0 && end > start) {
            return s.substring(start + 1, end);
        }
        return s;
    }

    private record LockHolderKey(String monitorClass, String holderName) {}

    private static class LockHolderStats {
        long totalBlockedNanos;
        long blockedCount;
        Set<String> blockedThreads = new HashSet<>();
        Map<String, Long> blockedTraces = new HashMap<>();
    }

    private static class HolderActivity {
        int sampleCount;
        String topFrame;
        boolean hasIo;
        boolean hasSql;
        String description = "unknown";
    }
}
