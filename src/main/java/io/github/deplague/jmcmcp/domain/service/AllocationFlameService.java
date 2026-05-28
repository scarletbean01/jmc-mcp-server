package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.AllocationFlameEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationFlameResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for allocation flame graph analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class AllocationFlameService {

    public AllocationFlameResult analyze(
            IItemCollection events,
            String packagePrefix,
            int topN) {

        Map<String, Long> pathDist = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();
        long totalBytes = 0;

        for (String typeId : new String[] {
                "jdk.ObjectAllocationInNewTLAB",
                "jdk.ObjectAllocationOutsideTLAB",
        }) {
            IItemCollection allocs = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : allocs) {
                IMemberAccessor<Object, IItem> stackAccessor =
                        JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<IQuantity, IItem> sizeAccessor =
                        JfrItemUtils.getAccessor(
                                iterable.getType(),
                                typeId.contains("Outside") ? "allocationSize" : "tlabSize"
                        );

                if (stackAccessor != null && sizeAccessor != null) {
                    for (IItem item : iterable) {
                        Object stackObj = stackAccessor.getMember(item);
                        IQuantity size = sizeAccessor.getMember(item);
                        if (stackObj != null && size != null) {
                            long bytes = size.longValue();
                            totalBytes += bytes;
                            String path = stCache.formatFocusingOn(stackObj, 10, packagePrefix);
                            pathDist.merge(path, bytes, Long::sum);
                        }
                    }
                }
            }
        }

        if (totalBytes == 0) {
            return new AllocationFlameResult(0, formatBytes(0), List.of());
        }

        long finalTotal = totalBytes;
        List<AllocationFlameEntry> entries = pathDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new AllocationFlameEntry(
                        e.getKey(),
                        e.getValue(),
                        formatBytes(e.getValue()),
                        (e.getValue() * 100.0) / finalTotal
                ))
                .toList();

        return new AllocationFlameResult(totalBytes, formatBytes(totalBytes), entries);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
