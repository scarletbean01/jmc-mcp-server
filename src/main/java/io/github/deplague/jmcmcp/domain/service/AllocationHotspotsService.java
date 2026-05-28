package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.AllocationHotspotEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;

/**
 * Pure domain service for identifying memory allocation hotspots.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class AllocationHotspotsService {

    public AllocationHotspotsResult analyze(
            IItemCollection events,
            String packagePrefix,
            int topN) {

        Map<AllocationKey, Long> allocationMap = new HashMap<>();

        processAllocations(events, "jdk.ObjectAllocationInNewTLAB", "tlabSize", allocationMap, packagePrefix);
        processAllocations(events, "jdk.ObjectAllocationOutsideTLAB", "allocationSize", allocationMap, packagePrefix);

        if (allocationMap.isEmpty()) {
            return new AllocationHotspotsResult(false, List.of());
        }

        List<AllocationHotspotEntry> entries = allocationMap.entrySet().stream()
                .sorted(Map.Entry.<AllocationKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new AllocationHotspotEntry(
                        e.getKey().className,
                        e.getKey().stackTrace,
                        e.getValue(),
                        formatBytes(e.getValue())
                ))
                .toList();

        return new AllocationHotspotsResult(true, entries);
    }

    private void processAllocations(
            IItemCollection events,
            String typeId,
            String sizeAttr,
            Map<AllocationKey, Long> map,
            String packagePrefix) {

        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), sizeAttr);
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (classAccessor != null && sizeAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    Object classObj = classAccessor.getMember(item);
                    IQuantity sizeQ = sizeAccessor.getMember(item);
                    Object stackObj = stackAccessor.getMember(item);

                    if (classObj != null && sizeQ != null && stackObj != null) {
                        String className = classObj.toString();
                        String trace = JfrItemUtils.formatStackTraceFocusingOn(stackObj, 5, packagePrefix);
                        AllocationKey key = new AllocationKey(className, trace);
                        map.merge(key, sizeQ.clampedLongValueIn(UnitLookup.BYTE), Long::sum);
                    }
                }
            }
        }
    }

    private record AllocationKey(String className, String stackTrace) {
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
