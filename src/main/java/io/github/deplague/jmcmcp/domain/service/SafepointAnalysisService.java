package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.SafepointAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.SafepointCauseEntry;
import io.github.deplague.jmcmcp.domain.model.TopSafepointEntry;
import io.github.deplague.jmcmcp.domain.model.TtspSummary;
import io.github.deplague.jmcmcp.domain.model.VmOperationSummary;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Domain service for safepoint and STW pause analysis.
 */
public class SafepointAnalysisService {

    public SafepointAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection safepoints = events.apply(ItemFilters.type("jdk.SafepointBegin"));

        if (!safepoints.hasItems()) {
            return new SafepointAnalysisResult(
                    false, 0, 0, 0, 0, 0,
                    List.of(), List.of(), null, null
            );
        }

        long count = JfrItemUtils.count(safepoints);
        long totalNanos = sumNanos(safepoints, JfrAttributes.DURATION.getIdentifier());
        long avgNanos = avgNanos(safepoints, JfrAttributes.DURATION.getIdentifier());
        long maxNanos = maxNanos(safepoints, JfrAttributes.DURATION.getIdentifier());
        long p95Nanos = percentileNanos(safepoints, JfrAttributes.DURATION.getIdentifier(), 95);

        // Cause distribution
        Map<String, CauseStats> causeMap = new HashMap<>();
        for (IItemIterable iterable : safepoints) {
            IMemberAccessor<String, IItem> opAccessor = JfrItemUtils.getAccessor(iterable.getType(), "operation");
            if (opAccessor == null) {
                opAccessor = JfrItemUtils.getAccessor(iterable.getType(), "name");
            }
            IMemberAccessor<IQuantity, IItem> durationAccessor =
                    JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());

            if (opAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String op = opAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (op != null && duration != null) {
                        CauseStats stats = causeMap.computeIfAbsent(op, k -> new CauseStats());
                        stats.count++;
                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        stats.totalNanos += nanos;
                        if (nanos > stats.maxNanos) {
                            stats.maxNanos = nanos;
                        }
                    }
                }
            }
        }

        List<SafepointCauseEntry> causeDistribution = causeMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .map(e -> {
                    CauseStats s = e.getValue();
                    return new SafepointCauseEntry(
                            e.getKey(),
                            s.count,
                            s.totalNanos,
                            s.totalNanos / s.count,
                            s.maxNanos
                    );
                })
                .toList();

        // Top-N longest safepoints
        List<IItem> sortedSafepoints = new ArrayList<>();
        for (IItemIterable iterable : safepoints) {
            for (IItem item : iterable) {
                sortedSafepoints.add(item);
            }
        }

        List<TopSafepointEntry> topSafepoints = sortedSafepoints.stream()
                .sorted((a, b) -> {
                    IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    if (da == null) return (db == null) ? 0 : 1;
                    if (db == null) return -1;
                    return db.compareTo(da);
                })
                .limit(topN)
                .map(item -> {
                    IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    Object op = JfrItemUtils.getMember(item, "operation")
                            .orElse(JfrItemUtils.getMember(item, "name").orElse("Unknown"));
                    IQuantity start = JfrItemUtils.getQuantity(item, JfrAttributes.START_TIME.getIdentifier()).orElse(null);
                    return new TopSafepointEntry(
                            duration != null ? duration.clampedLongValueIn(UnitLookup.NANOSECOND) : 0,
                            op.toString(),
                            start != null ? start.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : ""
                    );
                })
                .toList();

        // VM Operations
        IItemCollection vmOps = events.apply(ItemFilters.type("jdk.ExecuteVMOperation"));
        VmOperationSummary vmSummary = null;
        if (vmOps.hasItems()) {
            long vmoCount = JfrItemUtils.count(vmOps);
            long vmoAvg = avgNanos(vmOps, JfrAttributes.DURATION.getIdentifier());
            long vmoMax = maxNanos(vmOps, JfrAttributes.DURATION.getIdentifier());
            vmSummary = new VmOperationSummary(vmoCount, vmoAvg, vmoMax);
        }

        // TTSP
        IItemCollection ttsp = events.apply(ItemFilters.type("jdk.SafepointStateSynchronization"));
        TtspSummary ttspSummary = null;
        if (ttsp.hasItems()) {
            long avgTtsp = avgNanos(ttsp, JfrAttributes.DURATION.getIdentifier());
            long maxTtsp = maxNanos(ttsp, JfrAttributes.DURATION.getIdentifier());
            long p95Ttsp = percentileNanos(ttsp, JfrAttributes.DURATION.getIdentifier(), 95);
            ttspSummary = new TtspSummary(avgTtsp, maxTtsp, p95Ttsp);
        }

        return new SafepointAnalysisResult(
                true,
                count,
                totalNanos,
                avgNanos,
                maxNanos,
                p95Nanos,
                causeDistribution,
                topSafepoints,
                vmSummary,
                ttspSummary
        );
    }

    private long sumNanos(IItemCollection events, String attr) {
        IQuantity q = JfrItemUtils.sumQuantity(events, attr);
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private long avgNanos(IItemCollection events, String attr) {
        IQuantity q = JfrItemUtils.avgQuantity(events, attr);
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private long maxNanos(IItemCollection events, String attr) {
        IQuantity q = JfrItemUtils.maxQuantity(events, attr);
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private long percentileNanos(IItemCollection events, String attr, int percentile) {
        IQuantity q = JfrItemUtils.percentileQuantity(events, attr, percentile);
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private static class CauseStats {
        long count;
        long totalNanos;
        long maxNanos;
    }
}
