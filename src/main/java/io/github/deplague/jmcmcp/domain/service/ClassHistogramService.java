package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ClassAllocEntry;
import io.github.deplague.jmcmcp.domain.model.ClassHistogramResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for class allocation histogram.
 */
@Slf4j
public final class ClassHistogramService {

    public ClassHistogramResult analyze(IItemCollection events, int topN) {
        Map<String, ClassAllocStats> stats = new HashMap<>();
        processAllocations(events, "jdk.ObjectAllocationInNewTLAB", stats);
        processAllocations(events, "jdk.ObjectAllocationOutsideTLAB", stats);

        if (stats.isEmpty()) {
            return new ClassHistogramResult(List.of(), false);
        }

        List<ClassAllocEntry> entries = stats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalBytes, a.getValue().totalBytes))
                .limit(topN)
                .map(e -> {
                    ClassAllocStats s = e.getValue();
                    long avg = s.count > 0 ? s.totalBytes / s.count : 0;
                    return new ClassAllocEntry(
                            e.getKey(),
                            s.count,
                            formatBytes(s.totalBytes),
                            formatBytes(avg)
                    );
                })
                .toList();

        return new ClassHistogramResult(entries, true);
    }

    private void processAllocations(IItemCollection events, String typeId, Map<String, ClassAllocStats> stats) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            IMemberAccessor<IQuantity, IItem> allocAccessor = JfrItemUtils.getAccessor(iterable.getType(), "allocationSize");
            if (classAccessor != null && allocAccessor != null) {
                for (IItem item : iterable) {
                    Object clazzObj = classAccessor.getMember(item);
                    IQuantity sizeQ = allocAccessor.getMember(item);
                    if (clazzObj != null && sizeQ != null) {
                        String className = clazzObj.toString();
                        ClassAllocStats s = stats.computeIfAbsent(className, k -> new ClassAllocStats());
                        s.count++;
                        s.totalBytes += sizeQ.longValue();
                    }
                }
            }
        }
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    private static class ClassAllocStats {
        long count;
        long totalBytes;
    }
}
