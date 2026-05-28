package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.NetworkAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.NetworkConnectEntry;
import io.github.deplague.jmcmcp.domain.model.NetworkLatencyPercentile;
import io.github.deplague.jmcmcp.domain.model.NetworkReadEntry;
import io.github.deplague.jmcmcp.domain.model.NetworkWriteEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for analyzing socket network events.
 */
public final class NetworkAnalysisService {

    public NetworkAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection connectEvents = events.apply(ItemFilters.type("jdk.SocketConnect"));
        IItemCollection readEvents = events.apply(ItemFilters.type("jdk.SocketRead"));
        IItemCollection writeEvents = events.apply(ItemFilters.type("jdk.SocketWrite"));

        long connectCount = JfrItemUtils.count(connectEvents);
        long readCount = JfrItemUtils.count(readEvents);
        long writeCount = JfrItemUtils.count(writeEvents);

        if (connectCount == 0 && readCount == 0 && writeCount == 0) {
            return new NetworkAnalysisResult(
                    0, 0, 0,
                    Optional.empty(), Optional.empty(), Optional.empty(),
                    List.of(), List.of(), List.of(), List.of(), false
            );
        }

        Optional<String> avgConnectDuration = Optional.empty();
        Optional<String> maxConnectDuration = Optional.empty();
        Optional<String> p95ConnectDuration = Optional.empty();

        if (connectCount > 0) {
            IQuantity avgConn = JfrItemUtils.avgQuantity(connectEvents, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxConn = JfrItemUtils.maxQuantity(connectEvents, JfrAttributes.DURATION.getIdentifier());
            IQuantity p95Conn = JfrItemUtils.percentileQuantity(connectEvents, JfrAttributes.DURATION.getIdentifier(), 95);
            avgConnectDuration = Optional.ofNullable(avgConn).map(q -> q.displayUsing(IDisplayable.AUTO));
            maxConnectDuration = Optional.ofNullable(maxConn).map(q -> q.displayUsing(IDisplayable.AUTO));
            p95ConnectDuration = Optional.ofNullable(p95Conn).map(q -> q.displayUsing(IDisplayable.AUTO));
        }

        Map<HostPortKey, ConnectStats> connectStats = processConnects(connectEvents);
        Map<HostPortKey, ReadStats> readStats = processReads(readEvents);
        Map<HostPortKey, WriteStats> writeStats = processWrites(writeEvents);

        List<NetworkConnectEntry> topConnections = connectStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
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
                .sorted((a, b) -> Long.compare(b.getValue().totalBytes, a.getValue().totalBytes))
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
                .sorted((a, b) -> Long.compare(b.getValue().totalBytes, a.getValue().totalBytes))
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

        List<NetworkLatencyPercentile> percentiles = List.of(
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
            IMemberAccessor<Object, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");
            IMemberAccessor<IQuantity, IItem> portAccessor = JfrItemUtils.getAccessor(iterable.getType(), "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());

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
                            long nanos = dur.clampedLongValueIn(UnitLookup.NANOSECOND);
                            s.totalDurationNanos += nanos;
                            s.maxDurationNanos = Math.max(s.maxDurationNanos, nanos);
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
            IMemberAccessor<Object, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");
            IMemberAccessor<IQuantity, IItem> portAccessor = JfrItemUtils.getAccessor(iterable.getType(), "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), "bytesRead");

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
                            s.totalDurationNanos += dur.clampedLongValueIn(UnitLookup.NANOSECOND);
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
            IMemberAccessor<Object, IItem> hostAccessor = JfrItemUtils.getAccessor(iterable.getType(), "host");
            IMemberAccessor<IQuantity, IItem> portAccessor = JfrItemUtils.getAccessor(iterable.getType(), "port");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
            IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), "bytesWritten");

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
                            s.totalDurationNanos += dur.clampedLongValueIn(UnitLookup.NANOSECOND);
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
        IQuantity p50 = JfrItemUtils.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), 50);
        IQuantity p95 = JfrItemUtils.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), 95);
        IQuantity p99 = JfrItemUtils.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), 99);
        IQuantity max = JfrItemUtils.maxQuantity(events, JfrAttributes.DURATION.getIdentifier());
        return new NetworkLatencyPercentile(
                name, display(p50), display(p95), display(p99), display(max)
        );
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(IDisplayable.AUTO);
    }

    private static String displayNanos(long nanos) {
        return UnitLookup.NANOSECOND.quantity(nanos).displayUsing(IDisplayable.AUTO);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
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
