package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.BlockedPoolEntry;
import io.github.deplague.jmcmcp.domain.model.ConnectionPoolSummary;
import io.github.deplague.jmcmcp.domain.model.CpuLoadSummary;
import io.github.deplague.jmcmcp.domain.model.ThreadDumpSummary;
import io.github.deplague.jmcmcp.domain.model.ThreadStarvationResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
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
 * Pure domain service that detects thread starvation patterns.
 */
public final class SmartThreadStarvationDetectorService {

    private static final Pattern POOL_PATTERN = Pattern.compile(
            "com\\.zaxxer\\.hikari|org\\.apache\\.tomcat\\.jdbc|com\\.mchange\\.v2\\.c3p0|org\\.apache\\.commons\\.dbcp|oracle\\.ucp");
    private static final Pattern THREAD_STATE_PATTERN = Pattern.compile("java\\.lang\\.Thread\\.State:\\s*(\\w+)");

    public ThreadStarvationResult analyze(IItemCollection events, int topN) {
        CpuLoadSummary cpuSummary = analyzeCpuLoad(events);

        Set<String> activeThreads = new HashSet<>();
        IItemCollection execSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        for (IItemIterable iterable : execSamples) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            if (threadAcc != null) {
                for (IItem item : iterable) {
                    Object thread = threadAcc.getMember(item);
                    if (thread != null) {
                        activeThreads.add(extractThreadName(thread));
                    }
                }
            }
        }

        Map<String, BlockedStats> blockedStats = new HashMap<>();
        MutablePoolSummary poolSummary = new MutablePoolSummary();
        analyzeMonitorEnterBlocking(events, blockedStats, poolSummary);
        analyzeBlocking(events, "jdk.ThreadPark", blockedStats);
        analyzeBlocking(events, "jdk.JavaMonitorWait", blockedStats);

        if (poolSummary.blockEvents > 10) {
            poolSummary.poolDetected = true;
            poolSummary.threadsWaiting = poolSummary.waitingThreads.size();
            poolSummary.confidence = Math.min(1.0, poolSummary.blockEvents / 100.0);
        }

        ThreadDumpSummary dumpSummary = analyzeThreadDumps(events);

        List<String> findings = new ArrayList<>();
        String primaryDiagnosis = diagnose(cpuSummary, activeThreads.size(), blockedStats, dumpSummary, poolSummary, findings);

        String agentHint = buildAgentHint(primaryDiagnosis, cpuSummary, activeThreads.size(), blockedStats, dumpSummary, poolSummary);

        List<BlockedPoolEntry> topBlockedPools = blockedStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalBlockedNanos, a.getValue().totalBlockedNanos))
                .limit(topN)
                .map(e -> new BlockedPoolEntry(e.getKey(), e.getValue().blockCount, e.getValue().totalBlockedNanos))
                .toList();

        ConnectionPoolSummary connectionPool = new ConnectionPoolSummary(
                poolSummary.poolDetected,
                poolSummary.poolName,
                poolSummary.threadsWaiting,
                poolSummary.blockEvents,
                poolSummary.confidence
        );

        return new ThreadStarvationResult(
                cpuSummary,
                activeThreads.size(),
                blockedStats.values().stream().mapToLong(s -> s.blockCount).sum(),
                topBlockedPools,
                dumpSummary,
                connectionPool,
                primaryDiagnosis,
                findings,
                agentHint
        );
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
                    if (q != null) {
                        totalJvmUser += q.doubleValue();
                    }
                }
                if (jvmSysAcc != null) {
                    IQuantity q = jvmSysAcc.getMember(item);
                    if (q != null) {
                        totalJvmSystem += q.doubleValue();
                    }
                }
                if (machineAcc != null) {
                    IQuantity q = machineAcc.getMember(item);
                    if (q != null) {
                        totalMachine += q.doubleValue();
                    }
                }
                count++;
            }
        }

        if (count == 0) {
            return new CpuLoadSummary(0, 0, 0, 0, 0);
        }

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
            if (threadAcc == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                if (threadObj == null) {
                    continue;
                }
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
            if (resultAcc == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object result = resultAcc.getMember(item);
                if (result == null) {
                    continue;
                }
                String text = result.toString();

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
                                               MutablePoolSummary poolSummary) {
        IItemCollection filtered = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (threadAcc == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                if (threadObj == null) {
                    continue;
                }
                String threadName = extractThreadName(threadObj);
                String pool = extractPoolPrefix(threadName);

                BlockedStats s = blockedStats.computeIfAbsent(pool, k -> new BlockedStats());
                s.blockCount++;
                if (durationAcc != null) {
                    IQuantity duration = durationAcc.getMember(item);
                    if (duration != null) {
                        s.totalBlockedNanos += duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                    }
                }

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
                            MutablePoolSummary poolSummary,
                            List<String> findings) {
        long totalBlocked = blockedStats.values().stream().mapToLong(s -> s.blockCount).sum();

        if (poolSummary.poolDetected && poolSummary.confidence > 0.6) {
            findings.add(String.format("Detected %d threads blocked on connection pool `%s`",
                    poolSummary.threadsWaiting, poolSummary.poolName));
            return "🔴 Connection Pool Exhaustion";
        }

        if (cpu.sampleCount() > 0 && cpu.avgMachineTotal() > 0.85 && cpu.efficiency() < 0.3) {
            findings.add(String.format("Machine CPU at %.0f%% but JVM only using %.0f%%",
                    cpu.avgMachineTotal() * 100, cpu.avgJvmUser() * 100));
            return "🔴 CPU Saturation (External)";
        }

        if (dumpSummary.totalThreads() > 0 && dumpSummary.blockedCount() > dumpSummary.totalThreads() * 0.4) {
            findings.add(String.format("%.0f%% of threads are BLOCKED (%d / %d)",
                    (double) dumpSummary.blockedCount() / dumpSummary.totalThreads() * 100,
                    dumpSummary.blockedCount(), dumpSummary.totalThreads()));
            return "🔴 Lock Contention Starvation";
        }

        if (activeThreads > 50 && cpu.sampleCount() > 0 && cpu.avgJvmUser() < 0.2) {
            findings.add(String.format("High thread count (%d) with low JVM CPU (%.0f%%)",
                    activeThreads, cpu.avgJvmUser() * 100));
            return "🟡 Thread Over-Provisioning";
        }

        if (totalBlocked > 100) {
            findings.add(String.format("High blocking event count: %d", totalBlocked));
            return "🟡 Significant Thread Blocking";
        }

        return "✅ No Thread Starvation Detected";
    }

    private String buildAgentHint(String primaryDiagnosis, CpuLoadSummary cpu,
                                  int activeThreads, Map<String, BlockedStats> blockedStats,
                                  ThreadDumpSummary dumpSummary, MutablePoolSummary poolSummary) {
        StringBuilder hint = new StringBuilder();
        hint.append(primaryDiagnosis).append(" ");
        if (poolSummary.poolDetected && poolSummary.confidence > 0.7) {
            hint.append("Connection pool `").append(poolSummary.poolName).append("` is exhausted with ")
                    .append(poolSummary.threadsWaiting).append(" threads waiting. ");
            hint.append("Check for slow SQL queries or increase `maximumPoolSize`.");
        } else if (cpu.efficiency() < 0.3 && cpu.avgMachineTotal() > 0.8) {
            hint.append("Machine CPU is saturated (").append(String.format("%.0f%%", cpu.avgMachineTotal() * 100))
                    .append("). The JVM is not getting enough CPU time. Consider scaling horizontally or vertically.");
        } else if (dumpSummary.blockedCount() > activeThreads * 0.5) {
            hint.append("More than 50% of threads are BLOCKED. Heavy lock contention is starving the application. ");
            hint.append("Use `smart_lock_resolver` to identify the offending locks.");
        } else {
            hint.append("Monitor thread states over time and correlate with application throughput.");
        }
        hint.append(" Use `smart_correlate` to cross-reference locks, I/O, and CPU.");
        return hint.toString();
    }

    private static String extractThreadName(Object threadObj) {
        if (threadObj == null) {
            return "unknown";
        }
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
            } catch (NumberFormatException ignored) {
            }
        }
        return threadName;
    }

    private static String extractPoolName(String stackTrace) {
        if (stackTrace.contains("HikariPool")) {
            return "HikariCP";
        }
        if (stackTrace.contains("tomcat.jdbc")) {
            return "Tomcat JDBC Pool";
        }
        if (stackTrace.contains("c3p0")) {
            return "c3p0";
        }
        if (stackTrace.contains("commons.dbcp")) {
            return "Apache DBCP";
        }
        if (stackTrace.contains("oracle.ucp")) {
            return "Oracle UCP";
        }
        return "Unknown Pool";
    }

    private static class BlockedStats {
        long blockCount;
        long totalBlockedNanos;
    }

    private static class MutablePoolSummary {
        boolean poolDetected;
        String poolName;
        int threadsWaiting;
        int blockEvents;
        double confidence;
        Set<String> waitingThreads = new HashSet<>();
    }
}
