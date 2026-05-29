package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.infrastructure.jfr.StackTraceKey;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotEntry;
import io.github.deplague.jmcmcp.domain.model.AllocationHotspotsResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTraceFocusingOn;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.BYTE;

/**
 * Pure domain service for identifying memory allocation hotspots.
 * Contains no MCP-specific or UI formatting logic.
 */
@ApplicationScoped
public final class AllocationHotspotsService {

    public AllocationHotspotsResult analyze(
            IItemCollection events,
            String packagePrefix,
            int topN) {

        Map<AllocationKey, Long> allocationMap = new HashMap<>();

        processAllocations(events, "jdk.ObjectAllocationInNewTLAB", "tlabSize", allocationMap, packagePrefix);
        processAllocations(events, "jdk.ObjectAllocationOutsideTLAB", "allocationSize", allocationMap, packagePrefix);

        if (allocationMap.isEmpty()) {
            return new AllocationHotspotsResult(false, of());
        }

        List<AllocationHotspotEntry> entries = allocationMap.entrySet().stream()
                .sorted(Entry.<AllocationKey, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new AllocationHotspotEntry(
                        e.getKey().className,
                        formatStackTraceFocusingOn(e.getKey().stackTraceKey.getStackTraceObj(), 5, packagePrefix),
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

        IItemCollection filtered = events.apply(type(typeId));
        for (IItemIterable iterable : filtered) {
            IType<?> type2 = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = getAccessor(type2, "objectClass");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> sizeAccessor = getAccessor(type1, sizeAttr);
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");

            if (classAccessor != null && sizeAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    Object classObj = classAccessor.getMember(item);
                    IQuantity sizeQ = sizeAccessor.getMember(item);
                    Object stackObj = stackAccessor.getMember(item);

                    if (classObj != null && sizeQ != null && stackObj != null) {
                        String className = classObj.toString();
                        StackTraceKey keyObj = new StackTraceKey(stackObj, 5, packagePrefix);
                        AllocationKey key = new AllocationKey(className, keyObj);
                        map.merge(key, sizeQ.clampedLongValueIn(BYTE), Long::sum);
                    }
                }
            }
        }
    }

    private record AllocationKey(String className, StackTraceKey stackTraceKey) {
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (log(bytes) / log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return format("%.2f %sB", bytes / pow(1024, exp), pre);
    }
}
