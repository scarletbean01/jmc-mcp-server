package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CodeCacheResult;
import io.github.deplague.jmcmcp.domain.model.CodeCacheSegment;
import io.github.deplague.jmcmcp.domain.model.CompilerStats;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.*;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND;

/**
 * Pure domain service for analyzing code cache and JIT statistics.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class CodeCacheService {
    private final JfrAccessorRepository jfrAccessorRepository;
    private final JfrQuantityAggregator jfrQuantityAggregator;

    public CodeCacheResult analyze(IItemCollection events) {
        IItemCollection codeCacheEvents = events.apply(type("jdk.CodeCacheStatistics"));
        IItemCollection compilerEvents = events.apply(type("jdk.CompilerStatistics"));

        List<CodeCacheSegment> segments = new ArrayList<>();
        if (codeCacheEvents.hasItems()) {
            Map<String, IItem> latestSamples = new HashMap<>();
            for (var iterable : codeCacheEvents) {
                IType<?> type1 = iterable.getType();
                var segmentAcc = jfrAccessorRepository.getAccessor(type1, "codeBlobType");
                if (segmentAcc == null) {
                    IType<?> type = iterable.getType();
                    segmentAcc = jfrAccessorRepository.getAccessor(type, "segment");
                }
                for (IItem item : iterable) {
                    Object segmentObj = segmentAcc != null ? segmentAcc.getMember(item) : "Default";
                    String segment = segmentObj != null ? segmentObj.toString() : "Default";
                    latestSamples.put(segment, item);
                }
            }

            for (Map.Entry<String, IItem> entry : latestSamples.entrySet()) {
                IItem item = entry.getValue();
                long entries = jfrAccessorRepository.<IQuantity>getQuantity(item, "entryCount").map(IQuantity::longValue).orElse(0L);
                long methods = jfrAccessorRepository.<IQuantity>getQuantity(item, "methodCount").map(IQuantity::longValue).orElse(0L);
                long reserved = jfrAccessorRepository.<IQuantity>getQuantity(item, "reservedCapacity").map(IQuantity::longValue).orElse(0L);
                long unallocated = jfrAccessorRepository.<IQuantity>getQuantity(item, "unallocatedCapacity").map(IQuantity::longValue).orElse(0L);

                if (reserved == 0) {
                    long start = jfrAccessorRepository.<IQuantity>getQuantity(item, "startAddress").map(IQuantity::longValue).orElse(0L);
                    long end = jfrAccessorRepository.<IQuantity>getQuantity(item, "endAddress").map(IQuantity::longValue).orElse(0L);
                    reserved = end - start;
                }

                double util = reserved > 0 ? (1.0 - (double) unallocated / reserved) * 100.0 : 0.0;
                segments.add(new CodeCacheSegment(
                        entry.getKey(),
                        entries,
                        methods,
                        formatBytes(reserved),
                        formatBytes(unallocated),
                        util
                ));
            }
        }

        CompilerStats compilerStats = new CompilerStats(empty(), empty(), empty(), empty());
        if (compilerEvents.hasItems()) {
            IQuantity totalCount = jfrQuantityAggregator.maxQuantity(compilerEvents, "compileCount");
            IQuantity peakTime = jfrQuantityAggregator.maxQuantity(compilerEvents, "peakTimeSpent");
            IQuantity totalTime = jfrQuantityAggregator.maxQuantity(compilerEvents, "totalTimeSpent");

            Optional<Double> avgTime = empty();
            if (totalTime != null && totalCount != null && totalCount.longValue() > 0) {
                double avgMs = totalTime.doubleValueIn(MILLISECOND) / totalCount.longValue();
                avgTime = of(avgMs);
            }

            compilerStats = new CompilerStats(
                    totalCount != null ? of(totalCount.displayUsing(AUTO)) : empty(),
                    peakTime != null ? of(peakTime.displayUsing(AUTO)) : empty(),
                    totalTime != null ? of(totalTime.displayUsing(AUTO)) : empty(),
                    avgTime
            );
        }

        return new CodeCacheResult(segments, compilerStats, codeCacheEvents.hasItems(), compilerEvents.hasItems());
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
}
