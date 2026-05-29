package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.IoEndpointEntry;
import io.github.deplague.jmcmcp.domain.model.IoHotspotsResult;
import io.github.deplague.jmcmcp.domain.model.IoLatencyPercentile;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.maxQuantity;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.percentileQuantity;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.StackTraceFormatCache;
import static java.lang.Long.compare;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.String.format;
import static java.util.List.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.BYTE;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for I/O hotspots analysis.
 */
@ApplicationScoped
public final class IoHotspotsService {

    public IoHotspotsResult analyze(IItemCollection events, String endpointFilter, int topN) {
        boolean showFile = "all".equals(endpointFilter) || "file".equals(endpointFilter);
        boolean showSocket = "all".equals(endpointFilter) || "socket".equals(endpointFilter);

        List<IoEndpointEntry> fileEndpoints = showFile
                ? analyzeIoSection(events, of("jdk.FileRead", "jdk.FileWrite"), "path", "bytesRead", "bytesWritten", topN)
                : of();
        List<IoEndpointEntry> socketEndpoints = showSocket
                ? analyzeIoSection(events, of("jdk.SocketRead", "jdk.SocketWrite"), "host", "bytesRead", "bytesWritten", topN)
                : of();

        List<IoLatencyPercentile> percentiles = of(
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
        StackTraceFormatCache stCache = new StackTraceFormatCache();
        boolean isSocket = types.getFirst().contains("Socket");

        for (String typeId : types) {
            IItemCollection typeEvents = events.apply(type(typeId));
            boolean isRead = typeId.contains("Read");
            String bytesAttr = isRead ? readAttr : writeAttr;

            for (IItemIterable iterable : typeEvents) {
                IType<?> type3 = iterable.getType();
                IMemberAccessor<Object, IItem> targetAccessor = getAccessor(type3, targetAttr);
                IType<?> type2 = iterable.getType();
                IMemberAccessor<IQuantity, IItem> durationAccessor = getAccessor(type2, DURATION.getIdentifier());
                IType<?> type1 = iterable.getType();
                IMemberAccessor<IQuantity, IItem> bytesAccessor = getAccessor(type1, bytesAttr);
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");

                if (targetAccessor != null && durationAccessor != null) {
                    for (IItem item : iterable) {
                        Object targetObj = targetAccessor.getMember(item);
                        if (targetObj == null) continue;

                        String target = targetObj.toString();
                        if (isSocket) {
                            Object port = getMember(item, "port").orElse("");
                            target = target + ":" + port;
                        }

                        IQuantity duration = durationAccessor.getMember(item);
                        IQuantity bytes = bytesAccessor != null ? bytesAccessor.getMember(item) : null;
                        Object stackObj = stackAccessor != null ? stackAccessor.getMember(item) : null;
                        String trace = stCache.format(stackObj, 5);

                        IoKey key = new IoKey(target, trace);
                        IoStats stats = statsMap.computeIfAbsent(key, _ -> new IoStats());
                        stats.count++;
                        if (duration != null) {
                            long nanos = duration.clampedLongValueIn(NANOSECOND);
                            stats.totalDurationNanos += nanos;
                            if (nanos > stats.maxDurationNanos) {
                                stats.maxDurationNanos = nanos;
                            }
                        }
                        if (bytes != null) {
                            stats.totalBytes += bytes.clampedLongValueIn(BYTE);
                        }
                    }
                }
            }
        }

        return statsMap.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().maxDurationNanos, a.getValue().maxDurationNanos))
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
        IItemCollection filtered = events.apply(type(typeId));
        if (!filtered.hasItems()) {
            return new IoLatencyPercentile(name, "N/A", "N/A", "N/A", "N/A");
        }
        String identifier2 = DURATION.getIdentifier();
        IQuantity p50 = percentileQuantity(filtered, identifier2, 50);
        String identifier1 = DURATION.getIdentifier();
        IQuantity p95 = percentileQuantity(filtered, identifier1, 95);
        String identifier = DURATION.getIdentifier();
        IQuantity p99 = percentileQuantity(filtered, identifier, 99);
        IQuantity max = maxQuantity(filtered, DURATION.getIdentifier());
        return new IoLatencyPercentile(name, display(p50), display(p95), display(p99), display(max));
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

    private record IoKey(String target, String stackTrace) {
    }

    private static class IoStats {
        long count;
        long totalDurationNanos;
        long maxDurationNanos;
        long totalBytes;
    }
}
