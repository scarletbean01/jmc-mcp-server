package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.IoEndpointEntry;
import io.github.deplague.jmcmcp.domain.model.IoHotspotsResult;
import io.github.deplague.jmcmcp.domain.model.IoLatencyPercentile;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Pure domain service for I/O hotspots analysis.
 */
public final class IoHotspotsService {

    public IoHotspotsResult analyze(IItemCollection events, String endpointFilter, int topN) {
        boolean showFile = "all".equals(endpointFilter) || "file".equals(endpointFilter);
        boolean showSocket = "all".equals(endpointFilter) || "socket".equals(endpointFilter);

        List<IoEndpointEntry> fileEndpoints = showFile
                ? analyzeIoSection(events, List.of("jdk.FileRead", "jdk.FileWrite"), "path", "bytesRead", "bytesWritten", topN)
                : List.of();
        List<IoEndpointEntry> socketEndpoints = showSocket
                ? analyzeIoSection(events, List.of("jdk.SocketRead", "jdk.SocketWrite"), "host", "bytesRead", "bytesWritten", topN)
                : List.of();

        List<IoLatencyPercentile> percentiles = List.of(
                computePercentile("File Read", events, "jdk.FileRead"),
                computePercentile("File Write", events, "jdk.FileWrite"),
                computePercentile("Socket Read", events, "jdk.SocketRead"),
                computePercentile("Socket Write", events, "jdk.SocketWrite")
        );

        return new IoHotspotsResult(
                fileEndpoints,
                socketEndpoints,
                percentiles,
                !fileEndpoints.isEmpty(),
                !socketEndpoints.isEmpty()
        );
    }

    private List<IoEndpointEntry> analyzeIoSection(IItemCollection events, List<String> types,
                                                    String targetAttr, String readAttr, String writeAttr, int topN) {
        Map<IoKey, IoStats> statsMap = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();
        boolean isSocket = types.get(0).contains("Socket");

        for (String typeId : types) {
            IItemCollection typeEvents = events.apply(ItemFilters.type(typeId));
            boolean isRead = typeId.contains("Read");
            String bytesAttr = isRead ? readAttr : writeAttr;

            for (IItemIterable iterable : typeEvents) {
                IMemberAccessor<Object, IItem> targetAccessor = JfrItemUtils.getAccessor(iterable.getType(), targetAttr);
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
                IMemberAccessor<IQuantity, IItem> bytesAccessor = JfrItemUtils.getAccessor(iterable.getType(), bytesAttr);
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

                if (targetAccessor != null && durationAccessor != null) {
                    for (IItem item : iterable) {
                        Object targetObj = targetAccessor.getMember(item);
                        if (targetObj == null) continue;

                        String target = targetObj.toString();
                        if (isSocket) {
                            Object port = JfrItemUtils.getMember(item, "port").orElse("");
                            target = target + ":" + port;
                        }

                        IQuantity duration = durationAccessor.getMember(item);
                        IQuantity bytes = bytesAccessor != null ? bytesAccessor.getMember(item) : null;
                        Object stackObj = stackAccessor != null ? stackAccessor.getMember(item) : null;
                        String trace = stCache.format(stackObj, 5);

                        IoKey key = new IoKey(target, trace);
                        IoStats stats = statsMap.computeIfAbsent(key, k -> new IoStats());
                        stats.count++;
                        if (duration != null) {
                            long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                            stats.totalDurationNanos += nanos;
                            if (nanos > stats.maxDurationNanos) {
                                stats.maxDurationNanos = nanos;
                            }
                        }
                        if (bytes != null) {
                            stats.totalBytes += bytes.clampedLongValueIn(UnitLookup.BYTE);
                        }
                    }
                }
            }
        }

        return statsMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().maxDurationNanos, a.getValue().maxDurationNanos))
                .limit(topN)
                .map(e -> {
                    IoStats s = e.getValue();
                    return new IoEndpointEntry(
                            e.getKey().target,
                            displayNanos(s.maxDurationNanos),
                            s.count,
                            formatBytes(s.totalBytes),
                            e.getKey().stackTrace
                    );
                })
                .toList();
    }

    private IoLatencyPercentile computePercentile(String name, IItemCollection events, String typeId) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        if (!filtered.hasItems()) {
            return new IoLatencyPercentile(name, "N/A", "N/A", "N/A", "N/A");
        }
        IQuantity p50 = JfrItemUtils.percentileQuantity(filtered, JfrAttributes.DURATION.getIdentifier(), 50);
        IQuantity p95 = JfrItemUtils.percentileQuantity(filtered, JfrAttributes.DURATION.getIdentifier(), 95);
        IQuantity p99 = JfrItemUtils.percentileQuantity(filtered, JfrAttributes.DURATION.getIdentifier(), 99);
        IQuantity max = JfrItemUtils.maxQuantity(filtered, JfrAttributes.DURATION.getIdentifier());
        return new IoLatencyPercentile(name, display(p50), display(p95), display(p99), display(max));
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

    private record IoKey(String target, String stackTrace) {
    }

    private static class IoStats {
        long count;
        long totalDurationNanos;
        long maxDurationNanos;
        long totalBytes;
    }
}
