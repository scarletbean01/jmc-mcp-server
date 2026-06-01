package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.batchStats;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.count;
import static java.lang.Long.compare;
import static java.lang.Math.*;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for analyzing socket network events.
 */
@ApplicationScoped
public final class NetworkAnalysisService {

    public NetworkAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection connectEvents = events.apply(type("jdk.SocketConnect"));
        IItemCollection readEvents = events.apply(type("jdk.SocketRead"));
        IItemCollection writeEvents = events.apply(type("jdk.SocketWrite"));

        long connectCount = count(connectEvents);
        long readCount = count(readEvents);
        long writeCount = count(writeEvents);

        if (connectCount == 0 && readCount == 0 && writeCount == 0) {
            return new NetworkAnalysisResult(
                    0, 0, 0,
                    empty(), empty(), empty(),
                    of(), of(), of(), of(), false
            );
        }

        Optional<String> avgConnectDuration = empty();
        Optional<String> maxConnectDuration = empty();
        Optional<String> p95ConnectDuration = empty();

        if (connectCount > 0) {
            var stats = batchStats(connectEvents, DURATION.getIdentifier(), 95);
            avgConnectDuration = ofNullable(stats.get("avg")).map(q -> q.displayUsing(AUTO));
            maxConnectDuration = ofNullable(stats.get("max")).map(q -> q.displayUsing(AUTO));
            p95ConnectDuration = ofNullable(stats.get("p95")).map(q -> q.displayUsing(AUTO));
        }

        Map<HostPortKey, ConnectStats> connectStats = processConnects(connectEvents);
        Map<HostPortKey, ReadStats> readStats = processReads(readEvents);
        Map<HostPortKey, WriteStats> writeStats = processWrites(writeEvents);

        List<NetworkConnectEntry> topConnections = connectStats.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().count, a.getValue().count))
                .limit(topN)
                .map(entry -> {
                    ConnectStats s = entry.getValue();
                    return new NetworkConnectEntry(
                            entry.getKey().toString(),
                            s.count,
                            displayNanos(s.totalDurationNanos / s.count),
                            displayNanos(s.maxDurationNanos)
                    );
                })
                .toList();

        List<NetworkReadEntry> topReads = readStats.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalBytes, a.getValue().totalBytes))
                .limit(topN)
                .map(entry -> {
                    ReadStats s = entry.getValue();
                    return new NetworkReadEntry(
                            entry.getKey().toString(),
                            s.count,
                            formatBytes(s.totalBytes),
                            displayNanos(s.totalDurationNanos / s.count)
                    );
                })
                .toList();

        List<NetworkWriteEntry> topWrites = writeStats.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalBytes, a.getValue().totalBytes))
                .limit(topN)
                .map(entry -> {
                    WriteStats s = entry.getValue();
                    return new NetworkWriteEntry(
                            entry.getKey().toString(),
                            s.count,
                            formatBytes(s.totalBytes),
                            displayNanos(s.totalDurationNanos / s.count)
                    );
                })
                .toList();

        List<NetworkLatencyPercentile> percentiles = of(
                computePercentile("Socket Connect", connectEvents),
                computePercentile("Socket Read", readEvents),
                computePercentile("Socket Write", writeEvents)
        );

        return new NetworkAnalysisResult(
                connectCount,
                readCount,
                writeCount,
                avgConnectDuration,
                maxConnectDuration,
                p95ConnectDuration,
                topConnections,
                topReads,
                topWrites,
                percentiles,
                true
        );
    }

    private Map<HostPortKey, ConnectStats> processConnects(IItemCollection events) {
        Map<HostPortKey, ConnectStats> stats = new HashMap<>();
        for (IItemIterable iterable : events) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> hostAccessor = getAccessor(type1, "host");
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> portAccessor = getAccessor(type, "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());

            if (hostAccessor != null) {
                for (IItem item : iterable) {
                    Object hostObj = hostAccessor.getMember(item);
                    if (hostObj == null) {
                        continue;
                    }
                    String host = hostObj.toString();
                    int port = 0;
                    if (portAccessor != null) {
                        IQuantity portQ = portAccessor.getMember(item);
                        if (portQ != null) {
                            port = (int) portQ.longValue();
                        }
                    }

                    HostPortKey key = new HostPortKey(host, port);
                    ConnectStats s = stats.computeIfAbsent(key, k -> new ConnectStats());
                    s.count++;
                    if (durationAccessor != null) {
                        IQuantity dur = durationAccessor.getMember(item);
                        if (dur != null) {
                            long nanos = dur.clampedLongValueIn(NANOSECOND);
                            s.totalDurationNanos += nanos;
                            s.maxDurationNanos = max(s.maxDurationNanos, nanos);
                        }
                    }
                }
            }
        }
        return stats;
    }

    private Map<HostPortKey, ReadStats> processReads(IItemCollection events) {
        Map<HostPortKey, ReadStats> stats = new HashMap<>();
        for (IItemIterable iterable : events) {
            IType<?> type2 = iterable.getType();
            IMemberAccessor<Object, IItem> hostAccessor = getAccessor(type2, "host");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> portAccessor = getAccessor(type1, "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> bytesAccessor = getAccessor(type, "bytesRead");

            if (hostAccessor != null) {
                for (IItem item : iterable) {
                    Object hostObj = hostAccessor.getMember(item);
                    if (hostObj == null) {
                        continue;
                    }
                    String host = hostObj.toString();
                    int port = 0;
                    if (portAccessor != null) {
                        IQuantity portQ = portAccessor.getMember(item);
                        if (portQ != null) {
                            port = (int) portQ.longValue();
                        }
                    }

                    HostPortKey key = new HostPortKey(host, port);
                    ReadStats s = stats.computeIfAbsent(key, k -> new ReadStats());
                    s.count++;
                    if (durationAccessor != null) {
                        IQuantity dur = durationAccessor.getMember(item);
                        if (dur != null) {
                            s.totalDurationNanos += dur.clampedLongValueIn(NANOSECOND);
                        }
                    }
                    if (bytesAccessor != null) {
                        IQuantity bytes = bytesAccessor.getMember(item);
                        if (bytes != null) {
                            s.totalBytes += bytes.longValue();
                        }
                    }
                }
            }
        }
        return stats;
    }

    private Map<HostPortKey, WriteStats> processWrites(IItemCollection events) {
        Map<HostPortKey, WriteStats> stats = new HashMap<>();
        for (IItemIterable iterable : events) {
            IType<?> type2 = iterable.getType();
            IMemberAccessor<Object, IItem> hostAccessor = getAccessor(type2, "host");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> portAccessor = getAccessor(type1, "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> bytesAccessor = getAccessor(type, "bytesWritten");

            if (hostAccessor != null) {
                for (IItem item : iterable) {
                    Object hostObj = hostAccessor.getMember(item);
                    if (hostObj == null) {
                        continue;
                    }
                    String host = hostObj.toString();
                    int port = 0;
                    if (portAccessor != null) {
                        IQuantity portQ = portAccessor.getMember(item);
                        if (portQ != null) {
                            port = (int) portQ.longValue();
                        }
                    }

                    HostPortKey key = new HostPortKey(host, port);
                    WriteStats s = stats.computeIfAbsent(key, k -> new WriteStats());
                    s.count++;
                    if (durationAccessor != null) {
                        IQuantity dur = durationAccessor.getMember(item);
                        if (dur != null) {
                            s.totalDurationNanos += dur.clampedLongValueIn(NANOSECOND);
                        }
                    }
                    if (bytesAccessor != null) {
                        IQuantity bytes = bytesAccessor.getMember(item);
                        if (bytes != null) {
                            s.totalBytes += bytes.longValue();
                        }
                    }
                }
            }
        }
        return stats;
    }

    private NetworkLatencyPercentile computePercentile(String name, IItemCollection events) {
        if (!events.hasItems()) {
            return new NetworkLatencyPercentile(name, "N/A", "N/A", "N/A", "N/A");
        }
        var stats = batchStats(events, DURATION.getIdentifier(), 50, 95, 99);
        return new NetworkLatencyPercentile(
                name, display(stats.get("p50")), display(stats.get("p95")), display(stats.get("p99")), display(stats.get("max"))
        );
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(AUTO);
    }

    private static String displayNanos(long nanos) {
        return NANOSECOND.quantity(nanos).displayUsing(AUTO);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (log(bytes) / log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return format("%.2f %sB", bytes / pow(1024, exp), pre);
    }

    private record HostPortKey(String host, int port) {
        @Override
        public String toString() {
            return host + ":" + port;
        }
    }

    private static class ConnectStats {
        long count;
        long totalDurationNanos;
        long maxDurationNanos;
    }

    private static class ReadStats {
        long count;
        long totalDurationNanos;
        long totalBytes;
    }

    private static class WriteStats {
        long count;
        long totalDurationNanos;
        long totalBytes;
    }
}
