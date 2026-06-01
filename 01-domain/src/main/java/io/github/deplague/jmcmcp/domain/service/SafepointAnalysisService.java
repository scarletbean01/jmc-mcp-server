package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class SafepointAnalysisService {

    private final JfrAccessorRepository jfrAccessorRepository;
    private final JfrQuantityAggregator jfrQuantityAggregator;

    public SafepointAnalysisResult analyze(IItemCollection events, int topN) {
        IItemCollection safepoints = events.apply(ItemFilters.type("jdk.SafepointBegin"));

        if (!safepoints.hasItems()) {
            return new SafepointAnalysisResult(
                    false, 0, 0, 0, 0, 0,
                    List.of(), List.of(), null, null
            );
        }

        long count = jfrQuantityAggregator.count(safepoints);
        long totalNanos = sumNanos(safepoints);
        long avgNanos = avgNanos(safepoints);
        long maxNanos = maxNanos(safepoints);
        long p95Nanos = percentileNanos(safepoints, 95);

        // Cause distribution
        Map<String, CauseStats> causeMap = new HashMap<>();
        for (IItemIterable iterable : safepoints) {
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> opAccessor = jfrAccessorRepository.getAccessor(type, "operation");
            if (opAccessor == null) {
                opAccessor = jfrAccessorRepository.getAccessor(type, "name");
            }
            IMemberAccessor<IQuantity, IItem> durationAccessor =
                    jfrAccessorRepository.getAccessor(type, JfrAttributes.DURATION.getIdentifier());

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
                    IQuantity da = jfrAccessorRepository.<IQuantity>getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    IQuantity db = jfrAccessorRepository.<IQuantity>getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    if (da == null) return (db == null) ? 0 : 1;
                    if (db == null) return -1;
                    return db.compareTo(da);
                })
                .limit(topN)
                .map(item -> {
                    IQuantity duration = jfrAccessorRepository.<IQuantity>getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                    Object op = jfrAccessorRepository.getMember(item, "operation")
                            .orElse(jfrAccessorRepository.getMember(item, "name").orElse("Unknown"));
                    IQuantity start = jfrAccessorRepository.<IQuantity>getQuantity(item, JfrAttributes.START_TIME.getIdentifier()).orElse(null);
                    return new TopSafepointEntry(
                            duration != null ? duration.clampedLongValueIn(UnitLookup.NANOSECOND) : 0,
                            op.toString(),
                            start != null ? start.displayUsing(IDisplayable.AUTO) : ""
                    );
                })
                .toList();

        // VM Operations
        IItemCollection vmOps = events.apply(ItemFilters.type("jdk.ExecuteVMOperation"));
        VmOperationSummary vmSummary = null;
        if (vmOps.hasItems()) {
            long vmoCount = jfrQuantityAggregator.count(vmOps);
            long vmoAvg = avgNanos(vmOps);
            long vmoMax = maxNanos(vmOps);
            vmSummary = new VmOperationSummary(vmoCount, vmoAvg, vmoMax);
        }

        // TTSP
        IItemCollection ttsp = events.apply(ItemFilters.type("jdk.SafepointStateSynchronization"));
        TtspSummary ttspSummary = null;
        if (ttsp.hasItems()) {
            long avgTtsp = avgNanos(ttsp);
            long maxTtsp = maxNanos(ttsp);
            long p95Ttsp = percentileNanos(ttsp, 95);
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

    private long sumNanos(IItemCollection events) {
        IQuantity q = jfrQuantityAggregator.sumQuantity(events, JfrAttributes.DURATION.getIdentifier());
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private long avgNanos(IItemCollection events) {
        IQuantity q = jfrQuantityAggregator.avgQuantity(events, JfrAttributes.DURATION.getIdentifier());
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private long maxNanos(IItemCollection events) {
        IQuantity q = jfrQuantityAggregator.maxQuantity(events, JfrAttributes.DURATION.getIdentifier());
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private long percentileNanos(IItemCollection events, int percentile) {
        IQuantity q = jfrQuantityAggregator.percentileQuantity(events, JfrAttributes.DURATION.getIdentifier(), percentile);
        return q != null ? q.clampedLongValueIn(UnitLookup.NANOSECOND) : 0;
    }

    private static class CauseStats {
        long count;
        long totalNanos;
        long maxNanos;
    }
}
