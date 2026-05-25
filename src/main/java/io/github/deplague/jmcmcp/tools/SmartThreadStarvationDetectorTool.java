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

/**
 * Smart tool that detects thread starvation by correlating CPU load, thread counts,
 * blocking events, and connection pool activity.
 */
public final class SmartThreadStarvationDetectorTool {

    private static final String NAME = "smart_thread_starvation_detector";
    private final JfrAnalysisService service;

    private static final Pattern POOL_PATTERN = Pattern.compile(
            "com\\.zaxxer\\.hikari|org\\.apache\\.tomcat\\.jdbc|com\\.mchange\\.v2\\.c3p0|org\\.apache\\.commons\\.dbcp|oracle\\.ucp");
    private static final Pattern THREAD_STATE_PATTERN = Pattern.compile("java\\.lang\\.Thread\\.State:\\s*(\\w+)");

    public SmartThreadStarvationDetectorTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Smart tool that detects thread starvation patterns: connection pool exhaustion, " +
                                "CPU starvation, or virtual thread pinning by correlating CPU load, thread states, " +
                                "and blocking events.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top starvation issues (default 5)", 5),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 5);
                    return analyze(filePath, startTimeStr, endTimeStr, topN);
                }))
                .build();
    }

    String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        // 1. CPU Load analysis
        CpuLoadSummary cpuSummary = analyzeCpuLoad(events);

        // 2. Thread counts from execution samples
        Set<String> activeThreads = new HashSet<>();
        IItemCollection execSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        for (IItemIterable iterable : execSamples) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            if (threadAcc != null) {
                for (IItem item : iterable) {
                    Object thread = threadAcc.getMember(item);
                    if (thread != null) activeThreads.add(extractThreadName(thread));
                }
            }
        }

        // 3. Blocked thread analysis (MonitorEnter merged with pool detection)
        Map<String, BlockedStats> blockedStats = new HashMap<>();
        ConnectionPoolSummary poolSummary = new ConnectionPoolSummary();
        analyzeMonitorEnterBlocking(events, blockedStats, poolSummary);
        analyzeBlocking(events, "jdk.ThreadPark", blockedStats);
        analyzeBlocking(events, "jdk.JavaMonitorWait", blockedStats);

        if (poolSummary.blockEvents > 10) {
            poolSummary.poolDetected = true;
            poolSummary.threadsWaiting = poolSummary.waitingThreads.size();
            poolSummary.confidence = Math.min(1.0, poolSummary.blockEvents / 100.0);
        }

        // 4. Thread dump state analysis
        ThreadDumpSummary dumpSummary = analyzeThreadDumps(events);

        // Build diagnosis
        StringBuilder sb = new StringBuilder();
        sb.append("# Smart Thread Starvation Detector\n\n");

        // Metrics overview
        sb.append("## Overview\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        if (cpuSummary.sampleCount > 0) {
            sb.append(String.format("| Avg JVM User CPU | %.1f%% |%n", cpuSummary.avgJvmUser * 100));
            sb.append(String.format("| Avg Machine CPU | %.1f%% |%n", cpuSummary.avgMachineTotal * 100));
            sb.append(String.format("| CPU Efficiency (JVM/Machine) | %.1f%% |%n", cpuSummary.efficiency * 100));
        }
        sb.append("| Active Threads (samples) | ").append(activeThreads.size()).append(" |\n");
        sb.append("| Blocked Thread Events | ").append(blockedStats.values().stream().mapToLong(s -> s.blockCount).sum()).append(" |\n");
        if (dumpSummary.totalThreads > 0) {
            sb.append("| Threads in BLOCKED state (dumps) | ").append(dumpSummary.blockedCount).append(" |\n");
            sb.append("| Threads in WAITING state (dumps) | ").append(dumpSummary.waitingCount).append(" |\n");
        }
        sb.append("\n");

        // Diagnosis
        List<String> findings = new ArrayList<>();
        String primaryDiagnosis = diagnose(cpuSummary, activeThreads.size(), blockedStats, dumpSummary, poolSummary, findings);

        sb.append("## Primary Diagnosis: ").append(primaryDiagnosis).append("\n\n");

        if (!findings.isEmpty()) {
            sb.append("### Supporting Evidence\n\n");
            for (String finding : findings) {
                sb.append("- ").append(finding).append("\n");
            }
            sb.append("\n");
        }

        // Top blocked pools / threads
        if (!blockedStats.isEmpty()) {
            sb.append("## Top Blocked Thread Pools\n\n");
            sb.append("| Pool / Thread | Block Events | Total Block Time | Avg Block Time |\n");
            sb.append("|---------------|-------------:|-----------------:|---------------:|\n");

            blockedStats.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue().totalBlockedNanos, a.getValue().totalBlockedNanos))
                    .limit(topN)
                    .forEach(e -> {
                        BlockedStats s = e.getValue();
                        long avgNanos = s.blockCount > 0 ? s.totalBlockedNanos / s.blockCount : 0;
                        sb.append(String.format("| `%s` | %d | %s | %s |%n",
                                e.getKey(),
                                s.blockCount,
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(s.totalBlockedNanos)),
                                JfrAnalysisService.display(UnitLookup.NANOSECOND.quantity(avgNanos))));
                    });
            sb.append("\n");
        }

        // Connection pool specific
        if (poolSummary.poolDetected) {
            sb.append("## Connection Pool Analysis\n\n");
            sb.append("- **Pool Detected:** `").append(poolSummary.poolName).append("`\n");
            sb.append("- **Threads Waiting on Pool:** ").append(poolSummary.threadsWaiting).append("\n");
            sb.append("- **Pool Block Events:** ").append(poolSummary.blockEvents).append("\n");
            sb.append("- **Confidence:** ").append(String.format("%.0f%%", poolSummary.confidence * 100)).append("\n\n");
        }

        // Agent hint
        StringBuilder hint = new StringBuilder();
        hint.append(primaryDiagnosis).append(" ");
        if (poolSummary.poolDetected && poolSummary.confidence > 0.7) {
            hint.append("Connection pool `").append(poolSummary.poolName).append("` is exhausted with ")
                    .append(poolSummary.threadsWaiting).append(" threads waiting. ");
            hint.append("Check for slow SQL queries or increase `maximumPoolSize`.");
        } else if (cpuSummary.efficiency < 0.3 && cpuSummary.avgMachineTotal > 0.8) {
            hint.append("Machine CPU is saturated (").append(String.format("%.0f%%", cpuSummary.avgMachineTotal * 100))
                    .append("). The JVM is not getting enough CPU time. Consider scaling horizontally or vertically.");
        } else if (dumpSummary.blockedCount > activeThreads.size() * 0.5) {
            hint.append("More than 50% of threads are BLOCKED. Heavy lock contention is starving the application. ");
            hint.append("Use `smart_lock_resolver` to identify the offending locks.");
        } else {
            hint.append("Monitor thread states over time and correlate with application throughput.");
        }
        hint.append(" Use `smart_correlate` to cross-reference locks, I/O, and CPU.");

        sb.append("<agent_hint>").append(hint).append("</agent_hint>\n");

        return sb.toString();
    }

    private CpuLoadSummary analyzeCpuLoad(IItemCollection events) {
        IItemCollection cpuLoads = events.apply(ItemFilters.type("jdk.CPULoad"));
        double totalJvmUser = 0;
        double totalJvmSystem = 0;
        double totalMachine = 0;
        int count = 0;

        for (IItemIterable iterable : cpuLoads) {
            IMemberAccessor<IQuantity, IItem> jvmUserAcc = JfrItemUtils.getAccessor(iterable.getType(), "jvmUser");
            IMemberAccessor<IQuantity, IItem> jvmSysAcc = JfrItemUtils.getAccessor(iterable.getType(), "jvmSystem");
            IMemberAccessor<IQuantity, IItem> machineAcc = JfrItemUtils.getAccessor(iterable.getType(), "machineTotal");

            for (IItem item : iterable) {
                if (jvmUserAcc != null) {
                    IQuantity q = jvmUserAcc.getMember(item);
                    if (q != null) totalJvmUser += q.doubleValue();
                }
                if (jvmSysAcc != null) {
                    IQuantity q = jvmSysAcc.getMember(item);
                    if (q != null) totalJvmSystem += q.doubleValue();
                }
                if (machineAcc != null) {
                    IQuantity q = machineAcc.getMember(item);
                    if (q != null) totalMachine += q.doubleValue();
                }
                count++;
            }
        }

        if (count == 0) return new CpuLoadSummary(0, 0, 0, 0, 0);

        double avgJvmUser = totalJvmUser / count;
        double avgMachine = totalMachine / count;
        double efficiency = avgMachine > 0 ? avgJvmUser / avgMachine : 0;

        return new CpuLoadSummary(avgJvmUser, totalJvmSystem / count, avgMachine, efficiency, count);
    }

    private void analyzeBlocking(IItemCollection events, String typeId, Map<String, BlockedStats> stats) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            if (threadAcc == null) continue;

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                if (threadObj == null) continue;
                String threadName = extractThreadName(threadObj);
                String pool = extractPoolPrefix(threadName);

                BlockedStats s = stats.computeIfAbsent(pool, k -> new BlockedStats());
                s.blockCount++;
                if (durationAcc != null) {
                    IQuantity duration = durationAcc.getMember(item);
                    if (duration != null) {
                        s.totalBlockedNanos += duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                    }
                }
            }
        }
    }

    private ThreadDumpSummary analyzeThreadDumps(IItemCollection events) {
        IItemCollection dumps = events.apply(ItemFilters.type("jdk.ThreadDump"));
        int totalThreads = 0;
        int blockedCount = 0;
        int waitingCount = 0;

        for (IItemIterable iterable : dumps) {
            IMemberAccessor<Object, IItem> resultAcc = JfrItemUtils.getAccessor(iterable.getType(), "result");
            if (resultAcc == null) continue;

            for (IItem item : iterable) {
                Object result = resultAcc.getMember(item);
                if (result == null) continue;
                String text = result.toString();

                // Single-pass thread state counting using compiled regex
                Matcher m = THREAD_STATE_PATTERN.matcher(text);
                while (m.find()) {
                    totalThreads++;
                    String state = m.group(1);
                    if ("BLOCKED".equals(state)) {
                        blockedCount++;
                    } else if ("WAITING".equals(state) || "TIMED_WAITING".equals(state)) {
                        waitingCount++;
                    }
                }
            }
        }

        return new ThreadDumpSummary(totalThreads, blockedCount, waitingCount);
    }

    private void analyzeMonitorEnterBlocking(IItemCollection events,
                                               Map<String, BlockedStats> blockedStats,
                                               ConnectionPoolSummary poolSummary) {
        IItemCollection filtered = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (threadAcc == null) continue;

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                if (threadObj == null) continue;
                String threadName = extractThreadName(threadObj);
                String pool = extractPoolPrefix(threadName);

                // Blocking stats
                BlockedStats s = blockedStats.computeIfAbsent(pool, k -> new BlockedStats());
                s.blockCount++;
                if (durationAcc != null) {
                    IQuantity duration = durationAcc.getMember(item);
                    if (duration != null) {
                        s.totalBlockedNanos += duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                    }
                }

                // Connection pool detection (filter before formatting to avoid string allocation)
                if (stackAcc != null) {
                    Object stackObj = stackAcc.getMember(item);
                    if (stackObj != null && JfrItemUtils.stackTraceMatches(stackObj, POOL_PATTERN)) {
                        String trace = JfrItemUtils.formatFullStackTrace(stackObj);
                        if (trace != null) {
                            poolSummary.blockEvents++;
                            poolSummary.waitingThreads.add(threadName);
                            if (poolSummary.poolName == null) {
                                poolSummary.poolName = extractPoolName(trace);
                            }
                        }
                    }
                }
            }
        }
    }

    private String diagnose(CpuLoadSummary cpu, int activeThreads,
                            Map<String, BlockedStats> blockedStats,
                            ThreadDumpSummary dumpSummary,
                            ConnectionPoolSummary poolSummary,
                            List<String> findings) {
        long totalBlocked = blockedStats.values().stream().mapToLong(s -> s.blockCount).sum();

        if (poolSummary.poolDetected && poolSummary.confidence > 0.6) {
            findings.add(String.format("Detected %d threads blocked on connection pool `%s`",
                    poolSummary.threadsWaiting, poolSummary.poolName));
            return "🔴 Connection Pool Exhaustion";
        }

        if (cpu.sampleCount > 0 && cpu.avgMachineTotal > 0.85 && cpu.efficiency < 0.3) {
            findings.add(String.format("Machine CPU at %.0f%% but JVM only using %.0f%%",
                    cpu.avgMachineTotal * 100, cpu.avgJvmUser * 100));
            return "🔴 CPU Saturation (External)";
        }

        if (dumpSummary.totalThreads > 0 && dumpSummary.blockedCount > dumpSummary.totalThreads * 0.4) {
            findings.add(String.format("%.0f%% of threads are BLOCKED (%d / %d)",
                    (double) dumpSummary.blockedCount / dumpSummary.totalThreads * 100,
                    dumpSummary.blockedCount, dumpSummary.totalThreads));
            return "🔴 Lock Contention Starvation";
        }

        if (activeThreads > 50 && cpu.sampleCount > 0 && cpu.avgJvmUser < 0.2) {
            findings.add(String.format("High thread count (%d) with low JVM CPU (%.0f%%)",
                    activeThreads, cpu.avgJvmUser * 100));
            return "🟡 Thread Over-Provisioning";
        }

        if (totalBlocked > 100) {
            findings.add(String.format("High blocking event count: %d", totalBlocked));
            return "🟡 Significant Thread Blocking";
        }

        return "✅ No Thread Starvation Detected";
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

    private static String extractPoolPrefix(String threadName) {
        int dashIdx = threadName.lastIndexOf('-');
        if (dashIdx > 0) {
            try {
                Integer.parseInt(threadName.substring(dashIdx + 1));
                return threadName.substring(0, dashIdx);
            } catch (NumberFormatException ignored) {}
        }
        return threadName;
    }

    private static String extractPoolName(String stackTrace) {
        if (stackTrace.contains("HikariPool")) return "HikariCP";
        if (stackTrace.contains("tomcat.jdbc")) return "Tomcat JDBC Pool";
        if (stackTrace.contains("c3p0")) return "c3p0";
        if (stackTrace.contains("commons.dbcp")) return "Apache DBCP";
        if (stackTrace.contains("oracle.ucp")) return "Oracle UCP";
        return "Unknown Pool";
    }



    private record CpuLoadSummary(double avgJvmUser, double avgJvmSystem, double avgMachineTotal,
                                   double efficiency, int sampleCount) {
        CpuLoadSummary(double avgJvmUser, double avgJvmSystem, double avgMachineTotal, double efficiency, int sampleCount) {
            this.avgJvmUser = avgJvmUser;
            this.avgJvmSystem = avgJvmSystem;
            this.avgMachineTotal = avgMachineTotal;
            this.efficiency = efficiency;
            this.sampleCount = sampleCount;
        }
    }

    private record ThreadDumpSummary(int totalThreads, int blockedCount, int waitingCount) {}

    private static class BlockedStats {
        long blockCount;
        long totalBlockedNanos;
    }

    private static class ConnectionPoolSummary {
        boolean poolDetected;
        String poolName;
        int threadsWaiting;
        int blockEvents;
        double confidence;
        Set<String> waitingThreads = new HashSet<>();
    }
}
