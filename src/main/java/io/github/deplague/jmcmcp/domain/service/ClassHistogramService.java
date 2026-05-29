package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ClassAllocEntry;
import io.github.deplague.jmcmcp.domain.model.ClassHistogramResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static java.lang.Long.compare;
import static java.lang.String.format;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for class allocation histogram.
 */
@Slf4j
@ApplicationScoped
public final class ClassHistogramService {

    public ClassHistogramResult analyze(IItemCollection events, int topN) {
        Map<String, ClassAllocStats> stats = new HashMap<>();
        processAllocations(events, "jdk.ObjectAllocationInNewTLAB", stats);
        processAllocations(events, "jdk.ObjectAllocationOutsideTLAB", stats);

        if (stats.isEmpty()) {
            return new ClassHistogramResult(of(), false);
        }

        List<ClassAllocEntry> entries = stats.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalBytes, a.getValue().totalBytes))
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
        IItemCollection filtered = events.apply(type(typeId));
        for (IItemIterable iterable : filtered) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = getAccessor(type1, "objectClass");
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> allocAccessor = getAccessor(type, "allocationSize");
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
            return format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    private static class ClassAllocStats {
        long count;
        long totalBytes;
    }
}
