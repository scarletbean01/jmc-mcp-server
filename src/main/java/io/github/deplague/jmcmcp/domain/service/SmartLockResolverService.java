package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.BlockedTraceEntry;
import io.github.deplague.jmcmcp.domain.model.HolderActivity;
import io.github.deplague.jmcmcp.domain.model.LockHolderIssue;
import io.github.deplague.jmcmcp.domain.model.SmartLockResolverResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for smart lock resolver analysis.
 */
public final class SmartLockResolverService {

    private static final Pattern IO_PATTERN = Pattern.compile(
            "java\\.net\\.Socket|java\\.io\\.File|java\\.nio\\.channels|sun\\.nio\\.ch");
    private static final Pattern SQL_PATTERN = Pattern.compile(
            "java\\.sql\\.|oracle\\.jdbc|org\\.postgresql|com\\.mysql|org\\.h2|com\\.microsoft\\.sqlserver");

    public SmartLockResolverResult analyze(IItemCollection events, int topN) {
        IItemCollection monitorEnters = events.apply(ItemFilters.type("jdk.JavaMonitorEnter"));
        if (!monitorEnters.hasItems()) {
            return new SmartLockResolverResult(false, 0, List.of());
        }

        Map<LockHolderKey, LockHolderStats> holderStats = new HashMap<>();
        Map<IMCStackTrace, String> traceCache = new IdentityHashMap<>();
        for (IItemIterable iterable : monitorEnters) {
            IMemberAccessor<Object, IItem> monitorClassAcc = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
            IMemberAccessor<Object, IItem> prevOwnerAcc = JfrItemUtils.getAccessor(iterable.getType(), "previousOwner");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (monitorClassAcc == null || prevOwnerAcc == null || durationAcc == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object monitorClassObj = monitorClassAcc.getMember(item);
                Object prevOwnerObj = prevOwnerAcc.getMember(item);
                IQuantity duration = durationAcc.getMember(item);
                Object threadObj = threadAcc != null ? threadAcc.getMember(item) : null;
                Object stackObj = stackAcc != null ? stackAcc.getMember(item) : null;

                if (monitorClassObj == null || prevOwnerObj == null || duration == null) {
                    continue;
                }

                String monitorClass = monitorClassObj.toString();
                String holderName = extractThreadName(prevOwnerObj);
                String blockedThread = threadObj != null ? extractThreadName(threadObj) : "unknown";
                long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);

                String blockedTrace;
                if (stackObj instanceof IMCStackTrace st) {
                    blockedTrace = traceCache.computeIfAbsent(st, k -> JfrItemUtils.formatStackTrace(k, 5));
                } else {
                    blockedTrace = JfrItemUtils.formatStackTrace(stackObj, 5);
                }

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
            return new SmartLockResolverResult(true, 0, List.of());
        }

        List<Map.Entry<LockHolderKey, LockHolderStats>> sorted = holderStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalBlockedNanos, a.getValue().totalBlockedNanos))
                .limit(topN)
                .toList();

        IItemCollection execSamples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        Map<String, ActivityAccumulator> holderActivities = analyzeHolderActivities(execSamples, sorted);

        List<LockHolderIssue> topIssues = sorted.stream()
                .map(entry -> {
                    LockHolderKey key = entry.getKey();
                    LockHolderStats stats = entry.getValue();
                    ActivityAccumulator acc = holderActivities.get(key.holderName());

                    List<BlockedTraceEntry> topTraces = stats.blockedTraces.entrySet().stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                            .limit(3)
                            .map(e -> new BlockedTraceEntry(e.getKey(), e.getValue()))
                            .toList();

                    HolderActivity activity = null;
                    if (acc != null) {
                        activity = new HolderActivity(
                                acc.description,
                                acc.topFrame,
                                acc.hasIo,
                                acc.hasSql
                        );
                    }

                    return new LockHolderIssue(
                            key.monitorClass(),
                            key.holderName(),
                            stats.blockedThreads.size(),
                            stats.blockedCount,
                            displayNanos(stats.totalBlockedNanos),
                            activity,
                            topTraces
                    );
                })
                .toList();

        return new SmartLockResolverResult(true, holderStats.size(), topIssues);
    }

    private Map<String, ActivityAccumulator> analyzeHolderActivities(IItemCollection execSamples,
                                                                     List<Map.Entry<LockHolderKey, LockHolderStats>> holders) {
        Map<String, ActivityAccumulator> result = new HashMap<>();
        Set<String> holderNames = new HashSet<>();
        for (var entry : holders) {
            holderNames.add(entry.getKey().holderName());
        }

        Map<IMCStackTrace, String> fullTraceCache = new IdentityHashMap<>();
        for (IItemIterable iterable : execSamples) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (threadAcc == null || stackAcc == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                if (threadObj == null) {
                    continue;
                }
                String threadName = extractThreadName(threadObj);
                if (!holderNames.contains(threadName)) {
                    continue;
                }

                Object stackObj = stackAcc.getMember(item);
                String fullTrace;
                if (stackObj instanceof IMCStackTrace st) {
                    fullTrace = fullTraceCache.computeIfAbsent(st, JfrItemUtils::formatFullStackTrace);
                } else {
                    fullTrace = JfrItemUtils.formatFullStackTrace(stackObj);
                }
                if (fullTrace == null || fullTrace.isEmpty()) {
                    continue;
                }

                ActivityAccumulator activity = result.computeIfAbsent(threadName, k -> new ActivityAccumulator());
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

    private static String displayNanos(long nanos) {
        return UnitLookup.NANOSECOND.quantity(nanos).displayUsing(IDisplayable.AUTO);
    }

    private record LockHolderKey(String monitorClass, String holderName) {
    }

    private static class LockHolderStats {
        long totalBlockedNanos;
        long blockedCount;
        Set<String> blockedThreads = new HashSet<>();
        Map<String, Long> blockedTraces = new HashMap<>();
    }

    private static class ActivityAccumulator {
        int sampleCount;
        String topFrame;
        boolean hasIo;
        boolean hasSql;
        String description = "unknown";
    }
}
