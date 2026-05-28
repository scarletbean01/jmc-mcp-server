package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CodeCacheResult;
import io.github.deplague.jmcmcp.domain.model.CodeCacheSegment;
import io.github.deplague.jmcmcp.domain.model.CompilerStats;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for analyzing code cache and JIT statistics.
 */
@Slf4j
public final class CodeCacheService {

    public CodeCacheResult analyze(IItemCollection events) {
        IItemCollection codeCacheEvents = events.apply(ItemFilters.type("jdk.CodeCacheStatistics"));
        IItemCollection compilerEvents = events.apply(ItemFilters.type("jdk.CompilerStatistics"));

        List<CodeCacheSegment> segments = new ArrayList<>();
        if (codeCacheEvents.hasItems()) {
            Map<String, IItem> latestSamples = new HashMap<>();
            for (var iterable : codeCacheEvents) {
                var segmentAcc = JfrItemUtils.getAccessor(iterable.getType(), "codeBlobType");
                if (segmentAcc == null) {
                    segmentAcc = JfrItemUtils.getAccessor(iterable.getType(), "segment");
                }
                for (IItem item : iterable) {
                    Object segmentObj = segmentAcc != null ? segmentAcc.getMember(item) : "Default";
                    String segment = segmentObj != null ? segmentObj.toString() : "Default";
                    latestSamples.put(segment, item);
                }
            }

            for (Map.Entry<String, IItem> entry : latestSamples.entrySet()) {
                IItem item = entry.getValue();
                long entries = JfrItemUtils.getQuantity(item, "entryCount").map(IQuantity::longValue).orElse(0L);
                long methods = JfrItemUtils.getQuantity(item, "methodCount").map(IQuantity::longValue).orElse(0L);
                long reserved = JfrItemUtils.getQuantity(item, "reservedCapacity").map(IQuantity::longValue).orElse(0L);
                long unallocated = JfrItemUtils.getQuantity(item, "unallocatedCapacity").map(IQuantity::longValue).orElse(0L);

                if (reserved == 0) {
                    long start = JfrItemUtils.getQuantity(item, "startAddress").map(IQuantity::longValue).orElse(0L);
                    long end = JfrItemUtils.getQuantity(item, "endAddress").map(IQuantity::longValue).orElse(0L);
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

        CompilerStats compilerStats = new CompilerStats(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        if (compilerEvents.hasItems()) {
            IQuantity totalCount = JfrItemUtils.maxQuantity(compilerEvents, "compileCount");
            IQuantity peakTime = JfrItemUtils.maxQuantity(compilerEvents, "peakTimeSpent");
            IQuantity totalTime = JfrItemUtils.maxQuantity(compilerEvents, "totalTimeSpent");

            Optional<Double> avgTime = Optional.empty();
            if (totalTime != null && totalCount != null && totalCount.longValue() > 0) {
                double avgMs = totalTime.doubleValueIn(org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND) / totalCount.longValue();
                avgTime = Optional.of(avgMs);
            }

            compilerStats = new CompilerStats(
                    totalCount != null ? Optional.of(totalCount.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty(),
                    peakTime != null ? Optional.of(peakTime.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty(),
                    totalTime != null ? Optional.of(totalTime.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty(),
                    avgTime
            );
        }

        return new CodeCacheResult(segments, compilerStats, codeCacheEvents.hasItems(), compilerEvents.hasItems());
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
}
