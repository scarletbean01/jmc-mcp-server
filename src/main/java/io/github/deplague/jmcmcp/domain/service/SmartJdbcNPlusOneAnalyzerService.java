package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JdbcNPlusOnePattern;
import io.github.deplague.jmcmcp.domain.model.JdbcNPlusOneResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Pure domain service that detects JDBC N+1 query patterns by analyzing
 * sequential short-duration socket I/O events correlated with SQL/ORM stack traces.
 */
public final class SmartJdbcNPlusOneAnalyzerService {

    private static final Pattern JDBC_PATTERN = Pattern.compile(
            "java\\.sql\\.|javax\\.sql\\.|oracle\\.jdbc|org\\.postgresql|com\\.mysql|org\\.h2|com\\.microsoft\\.sqlserver|com\\.ibm\\.db2");
    private static final Pattern ORM_PATTERN = Pattern.compile(
            "org\\.hibernate|org\\.eclipse\\.linkage|org\\.apache\\.openjpa|com\\.ibatis|org\\.mybatis|org\\.springframework\\.orm");
    private static final long SHORT_DURATION_NS = 1_000_000; // < 1ms
    private static final int MIN_BURST_SIZE = 5;
    private static final long MAX_BURST_GAP_NS = 10_000_000; // 10ms gap between events in a burst

    public JdbcNPlusOneResult analyze(IItemCollection events, int topN) {
        List<SocketEvent> socketEvents = new ArrayList<>();
        collectSocketEvents(events, "jdk.SocketRead", socketEvents);
        collectSocketEvents(events, "jdk.SocketWrite", socketEvents);

        if (socketEvents.isEmpty()) {
            return new JdbcNPlusOneResult(List.of(), 0, false);
        }

        socketEvents.sort(Comparator.comparing((SocketEvent e) -> e.threadName)
                .thenComparingLong(e -> e.startTimeNanos));

        List<JdbcNPlusOnePattern> patterns = detectBursts(socketEvents);

        if (patterns.isEmpty()) {
            return new JdbcNPlusOneResult(List.of(), socketEvents.size(), false);
        }

        patterns.sort((a, b) -> {
            int cmp = Double.compare(b.confidence(), a.confidence());
            if (cmp != 0) {
                return cmp;
            }
            return Long.compare(b.totalReads(), a.totalReads());
        });

        List<JdbcNPlusOnePattern> limited = patterns.stream()
                .limit(topN)
                .toList();

        return new JdbcNPlusOneResult(limited, socketEvents.size(), true);
    }

    private void collectSocketEvents(IItemCollection events, String typeId, List<SocketEvent> result) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> threadAcc = JfrItemUtils.getAccessor(iterable.getType(), "eventThread");
            IMemberAccessor<IQuantity, IItem> startAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.START_TIME.getIdentifier());
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAcc = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (threadAcc == null || startAcc == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object threadObj = threadAcc.getMember(item);
                IQuantity startTime = startAcc.getMember(item);
                IQuantity duration = durationAcc != null ? durationAcc.getMember(item) : null;
                Object stackObj = stackAcc != null ? stackAcc.getMember(item) : null;

                if (threadObj == null || startTime == null) {
                    continue;
                }

                String threadName = extractThreadName(threadObj);
                long startNanos = startTime.clampedLongValueIn(UnitLookup.EPOCH_NS);
                long durationNanos = duration != null ? duration.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;

                if (JfrItemUtils.stackTraceMatches(stackObj, JDBC_PATTERN)) {
                    String fullTrace = JfrItemUtils.formatFullStackTrace(stackObj);
                    result.add(new SocketEvent(threadName, startNanos, durationNanos, fullTrace));
                }
            }
        }
    }

    private List<JdbcNPlusOnePattern> detectBursts(List<SocketEvent> events) {
        List<JdbcNPlusOnePattern> patterns = new ArrayList<>();

        Map<String, List<SocketEvent>> byThread = new HashMap<>();
        for (SocketEvent e : events) {
            byThread.computeIfAbsent(e.threadName, k -> new ArrayList<>()).add(e);
        }

        for (var entry : byThread.entrySet()) {
            List<SocketEvent> threadEvents = entry.getValue();
            if (threadEvents.size() < MIN_BURST_SIZE) {
                continue;
            }

            int i = 0;
            while (i < threadEvents.size()) {
                int burstStart = i;
                long burstStartTime = threadEvents.get(i).startTimeNanos;
                long burstEndTime = burstStartTime;
                int shortCount = threadEvents.get(i).durationNanos < SHORT_DURATION_NS ? 1 : 0;

                int j = i + 1;
                while (j < threadEvents.size()) {
                    SocketEvent prev = threadEvents.get(j - 1);
                    SocketEvent curr = threadEvents.get(j);
                    long gap = curr.startTimeNanos - (prev.startTimeNanos + prev.durationNanos);
                    if (gap > MAX_BURST_GAP_NS) {
                        break;
                    }

                    burstEndTime = curr.startTimeNanos + curr.durationNanos;
                    if (curr.durationNanos < SHORT_DURATION_NS) {
                        shortCount++;
                    }
                    j++;
                }

                int burstSize = j - burstStart;
                if (burstSize >= MIN_BURST_SIZE) {
                    long totalReads = burstSize;
                    double windowMs = (burstEndTime - burstStartTime) / 1_000_000.0;
                    double avgDurationMs = threadEvents.subList(burstStart, j).stream()
                            .mapToLong(e -> e.durationNanos).average().orElse(0) / 1_000_000.0;

                    Map<String, Long> methodCounts = new HashMap<>();
                    String sampleTrace = null;
                    boolean hasOrm = false;

                    for (int k = burstStart; k < j; k++) {
                        SocketEvent e = threadEvents.get(k);
                        if (e.fullTrace != null) {
                            String method = extractTriggeringMethod(e.fullTrace);
                            methodCounts.merge(method, 1L, Long::sum);
                            if (sampleTrace == null) {
                                sampleTrace = e.fullTrace;
                            }
                            if (ORM_PATTERN.matcher(e.fullTrace).find()) {
                                hasOrm = true;
                            }
                        }
                    }

                    String triggeringMethod = methodCounts.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Unknown");

                    double confidence = calculateConfidence(totalReads, windowMs, shortCount, burstSize, hasOrm);

                    patterns.add(new JdbcNPlusOnePattern(
                            entry.getKey(),
                            triggeringMethod,
                            totalReads,
                            windowMs,
                            avgDurationMs,
                            confidence,
                            hasOrm,
                            sampleTrace
                    ));
                }

                i = Math.max(j, i + 1);
            }
        }

        return patterns;
    }

    private double calculateConfidence(long totalReads, double windowMs, int shortCount, int burstSize, boolean hasOrm) {
        double score = 0.0;

        if (totalReads >= 100) {
            score += 0.3;
        } else if (totalReads >= 50) {
            score += 0.2;
        } else if (totalReads >= 20) {
            score += 0.1;
        } else {
            score += 0.05;
        }

        double shortRatio = (double) shortCount / burstSize;
        score += shortRatio * 0.2;

        if (windowMs > 0 && totalReads / windowMs > 10) {
            score += 0.2;
        } else if (windowMs > 0 && totalReads / windowMs > 5) {
            score += 0.1;
        }

        if (hasOrm) {
            score += 0.2;
        }

        score += 0.1;

        return Math.min(1.0, score);
    }

    private static String extractTriggeringMethod(String fullTrace) {
        String[] lines = fullTrace.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("at ")) {
                String method = trimmed.substring(3).trim();
                if (!method.startsWith("java.sql.")
                        && !method.startsWith("oracle.jdbc")
                        && !method.startsWith("org.hibernate")
                        && !method.startsWith("com.zaxxer.hikari")
                        && !method.startsWith("java.net.")
                        && !method.startsWith("sun.nio.")
                        && !method.startsWith("jdk.internal")) {
                    int parenIdx = method.indexOf('(');
                    if (parenIdx > 0) {
                        return method.substring(0, parenIdx).trim();
                    }
                    return method;
                }
            }
        }
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("at ")) {
                String method = trimmed.substring(3).trim();
                int parenIdx = method.indexOf('(');
                if (parenIdx > 0) {
                    return method.substring(0, parenIdx).trim();
                }
                return method;
            }
        }
        return "Unknown";
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

    private record SocketEvent(String threadName, long startTimeNanos, long durationNanos, String fullTrace) {
    }
}
