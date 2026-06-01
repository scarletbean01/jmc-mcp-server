package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.MethodSampleEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.github.deplague.jmcmcp.domain.model.ThreadStateEntry;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.compare;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for thread CPU analysis.
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class ThreadCpuService {
    private final JfrAccessorRepository jfrAccessorRepository;

    public ThreadCpuResult analyze(IItemCollection events, String packagePrefix, int topN) {
        IItemCollection samples = events.apply(type("jdk.ExecutionSample"));
        if (!samples.hasItems()) {
            return new ThreadCpuResult(0, of(), of());
        }

        Map<String, ThreadStats> threadStatsMap = new HashMap<>();
        Map<String, Long> stateCounts = new HashMap<>();
        long totalSamples = 0;
        JfrStackTraceService.StackTraceFormatCache stCache = new JfrStackTraceService.StackTraceFormatCache();

        for (IItemIterable iterable : samples) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> threadAccessor = jfrAccessorRepository.getAccessor(type, "sampledThread");
            IMemberAccessor<Object, IItem> stackAccessor = jfrAccessorRepository.getAccessor(type, "stackTrace");
            IMemberAccessor<String, IItem> stateAccessor = jfrAccessorRepository.getAccessor(type, "state");

            if (threadAccessor != null) {
                for (IItem item : iterable) {
                    Object threadObj = threadAccessor.getMember(item);
                    String threadName = threadObj != null ? threadObj.toString() : "Unknown";

                    ThreadStats stats = threadStatsMap.computeIfAbsent(threadName, ThreadStats::new);
                    stats.samples++;
                    totalSamples++;

                    if (stateAccessor != null) {
                        String state = stateAccessor.getMember(item);
                        if (state != null) {
                            stats.stateCounts.merge(state, 1L, Long::sum);
                            stateCounts.merge(state, 1L, Long::sum);
                        }
                    }

                    if (stackAccessor != null) {
                        Object st = stackAccessor.getMember(item);
                        if (st != null) {
                            String topFrame = stCache.formatFocusingOn(st, 1, packagePrefix).trim();
                            if (!topFrame.isEmpty()) {
                                stats.methodCounts.merge(topFrame, 1L, Long::sum);
                            }
                        }
                    }
                }
            }
        }

        long finalTotalSamples = totalSamples;
        List<ThreadCpuEntry> sortedThreads = threadStatsMap.values().stream()
                .sorted((a, b) -> compare(b.samples, a.samples))
                .limit(topN)
                .map(stats -> {
                    double pct = finalTotalSamples > 0 ? (stats.samples * 100.0) / finalTotalSamples : 0;
                    List<MethodSampleEntry> topMethods = stats.methodCounts.entrySet().stream()
                            .sorted(Entry.<String, Long>comparingByValue().reversed())
                            .limit(5)
                            .map(e -> new MethodSampleEntry(e.getKey(), e.getValue()))
                            .toList();
                    return new ThreadCpuEntry(stats.name, stats.samples, pct, stats.stateCounts, topMethods);
                })
                .toList();

        List<ThreadStateEntry> stateDistribution = stateCounts.entrySet().stream()
                .sorted(Entry.<String, Long>comparingByValue().reversed())
                .map(e -> {
                    double pct = finalTotalSamples > 0 ? (e.getValue() * 100.0) / finalTotalSamples : 0;
                    return new ThreadStateEntry(e.getKey(), e.getValue(), pct);
                })
                .toList();

        return new ThreadCpuResult(totalSamples, sortedThreads, stateDistribution);
    }

    private static class ThreadStats {
        final String name;
        long samples = 0;
        final Map<String, Long> stateCounts = new HashMap<>();
        final Map<String, Long> methodCounts = new HashMap<>();

        ThreadStats(String name) {
            this.name = name;
        }
    }
}
