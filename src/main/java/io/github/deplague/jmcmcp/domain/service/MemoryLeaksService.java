package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.LeakSiteEntry;
import io.github.deplague.jmcmcp.domain.model.LeakingClassEntry;
import io.github.deplague.jmcmcp.domain.model.MemoryLeaksResult;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Domain service for memory leak analysis from old object samples.
 */
@ApplicationScoped
public final class MemoryLeaksService {

    public MemoryLeaksResult analyze(IItemCollection events, int topN) {
        IItemCollection samples = events.apply(ItemFilters.type("jdk.OldObjectSample"));
        long count = JfrQuantityAggregator.count(samples);

        if (count == 0) {
            return new MemoryLeaksResult(false, 0, List.of(), List.of());
        }

        Map<String, LeakStats> classStats = new HashMap<>();
        Map<String, LeakStats> siteStats = new HashMap<>();

        for (IItemIterable iterable : samples) {
            IType<?> type3 = iterable.getType();
            IMemberAccessor<Object, IItem> classAccessor = JfrAccessorRepository.getAccessor(type3, "objectClass");
            IType<?> type2 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrAccessorRepository.getAccessor(type2, "allocationSize");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = JfrAccessorRepository.getAccessor(type1, "stackTrace");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> objAccessor = JfrAccessorRepository.getAccessor(type, "object");

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
                        stackTrace = JfrStackTraceService.formatStackTrace(st, 5);
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
