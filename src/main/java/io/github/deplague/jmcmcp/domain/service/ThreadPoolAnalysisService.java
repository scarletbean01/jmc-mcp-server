package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ThreadPoolAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.ThreadPoolEntry;
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
 * Pure domain service for thread pool analysis.
 */
public final class ThreadPoolAnalysisService {

    private static final Pattern THREAD_POOL_PATTERN = Pattern.compile("^(.+?)-(\\d+)$");
    private static final Set<String> KNOWN_POOL_PREFIXES = Set.of(
            "http-nio", "https-nio", "pool", "ForkJoinPool", "worker", "task-",
            "exec-", "scheduler-", "async-", "db-", "HikariPool", "catalina-exec"
    );

    public ThreadPoolAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection cpuSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        IItemCollection monitorEnter = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        IItemCollection monitorWait = events.apply(ItemFilters.type("jdk.JavaMonitorWait"));
        IItemCollection threadPark = events.apply(ItemFilters.type("jdk.ThreadPark"));
        IItemCollection threadSleep = events.apply(ItemFilters.type("jdk.ThreadSleep"));

        if (!cpuSamples.hasItems() && !monitorEnter.hasItems() && !threadPark.hasItems()) {
            return new ThreadPoolAnalysisResult(false, List.of(), List.of(), List.of());
        }

        Map<String, PoolStats> poolStats = new HashMap<>();

        collectCpuSamples(cpuSamples, poolStats);
        collectBlockingTime(monitorEnter, "monitor_enter", poolStats);
        collectBlockingTime(monitorWait, "monitor_wait", poolStats);
        collectBlockingTime(threadPark, "thread_park", poolStats);
        collectBlockingTime(threadSleep, "thread_sleep", poolStats);

        if (poolStats.isEmpty()) {
            return new ThreadPoolAnalysisResult(false, List.of(), List.of(), List.of());
        }

        List<String> warnings = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        List<ThreadPoolEntry> pools = new ArrayList<>();

        poolStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().cpuSamples, a.getValue().cpuSamples))
                .limit(topN)
                .forEach(entry -> {
                    PoolStats s = entry.getValue();
                    double activeRatio = s.cpuSamples > 0
                            ? (double) s.cpuSamples / (s.cpuSamples + s.blockedCount) * 100
                            : 0;
                    String status = determineStatus(s, activeRatio);

                    pools.add(new ThreadPoolEntry(
                            entry.getKey(),
                            s.threadNames.size(),
                            s.cpuSamples,
                            s.blockedTimeMs,
                            s.blockedCount,
                            s.monitorEnterCount,
                            s.monitorWaitCount,
                            s.parkCount,
                            s.sleepCount,
                            activeRatio,
                            status
                    ));

                    if (activeRatio > 95 && s.blockedCount > s.cpuSamples) {
                        warnings.add(String.format(
                                "Pool '%s' shows %.0f%% utilization with high blocking — pool may be saturated",
                                entry.getKey(), activeRatio));
                        recommendations.add(String.format(
                                "For '%s': consider increasing max threads or reducing task duration",
                                entry.getKey()));
                    }
                    if (s.blockedTimeMs > 0 && s.blockedCount > 0) {
                        long avgBlockMs = s.blockedTimeMs / s.blockedCount;
                        if (avgBlockMs > 1000) {
                            warnings.add(String.format(
                                    "Pool '%s' has avg blocking time of %ds — tasks are waiting too long",
                                    entry.getKey(), avgBlockMs / 1000));
                        }
                    }
                });

        return new ThreadPoolAnalysisResult(true, pools, warnings, recommendations);
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
            } catch (NumberFormatException ignored) {
            }
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
                    if (thread == null) {
                        continue;
                    }

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
        if (activeRatio > 95 && s.blockedCount > s.cpuSamples) {
            return "⛔ SATURATED";
        }
        if (activeRatio > 80) {
            return "⚠️ HIGH";
        }
        if (activeRatio > 50) {
            return "🟡 MODERATE";
        }
        return "✅ HEALTHY";
    }

    private static class PoolStats {
        Set<String> threadNames = new HashSet<>();
        long cpuSamples;
        long blockedCount;
        long blockedTimeMs;
        long monitorEnterCount;
        long monitorWaitCount;
        long parkCount;
        long sleepCount;
    }
}
