package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LeakSiteEntry;
import io.github.deplague.jmcmcp.domain.model.LeakingClassEntry;
import io.github.deplague.jmcmcp.domain.model.MemoryLeaksResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.Comparator;
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
 * Domain service for memory leak analysis from old object samples.
 */
public class MemoryLeaksService {

    public MemoryLeaksResult analyze(IItemCollection events, int topN) {
        IItemCollection samples = events.apply(ItemFilters.type("jdk.OldObjectSample"));
        long count = JfrItemUtils.count(samples);

        if (count == 0) {
            return new MemoryLeaksResult(false, 0, List.of(), List.of());
        }

        Map<String, LeakStats> classStats = new HashMap<>();
        Map<String, LeakStats> siteStats = new HashMap<>();

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "allocationSize");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            IMemberAccessor<Object, IItem> objAccessor = JfrItemUtils.getAccessor(iterable.getType(), "object");

            for (IItem item : iterable) {
                String className = "Unknown";
                if (classAccessor != null) {
                    Object clazz = classAccessor.getMember(item);
                    if (clazz != null) {
                        className = clazz.toString();
                    }
                } else if (objAccessor != null) {
                    Object obj = objAccessor.getMember(item);
                    if (obj != null) {
                        className = obj.toString();
                    }
                }

                String stackTrace = "Unknown";
                if (stackAccessor != null) {
                    Object st = stackAccessor.getMember(item);
                    if (st != null) {
                        stackTrace = JfrItemUtils.formatStackTrace(st, 5);
                    }
                }

                long size = 0;
                if (sizeAccessor != null) {
                    IQuantity q = sizeAccessor.getMember(item);
                    if (q != null) {
                        size = q.longValue();
                    }
                }

                LeakStats cs = classStats.computeIfAbsent(className, k -> new LeakStats());
                cs.count++;
                cs.totalSize += size;

                String siteKey = className + " allocated at:\n" + stackTrace;
                LeakStats ss = siteStats.computeIfAbsent(siteKey, k -> new LeakStats());
                ss.count++;
                ss.totalSize += size;
            }
        }

        List<LeakingClassEntry> leakingClasses = classStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
                .limit(topN)
                .map(e -> new LeakingClassEntry(e.getKey(), e.getValue().count))
                .toList();

        List<LeakSiteEntry> leakSites = siteStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
                .limit(topN)
                .map(e -> new LeakSiteEntry(e.getKey(), e.getValue().count))
                .toList();

        return new MemoryLeaksResult(true, count, leakingClasses, leakSites);
    }

    private static class LeakStats {
        long count;
        long totalSize;
    }
}
