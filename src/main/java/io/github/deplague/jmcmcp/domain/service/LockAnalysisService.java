package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.BiasedLockSummary;
import io.github.deplague.jmcmcp.domain.model.LockAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.ParkSiteEntry;
import io.github.deplague.jmcmcp.domain.model.RevokedLockClassEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadParkSummary;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for advanced lock analysis.
 */
public final class LockAnalysisService {

    public LockAnalysisResult analyze(IItemCollection events, int topN) {
        Optional<ThreadParkSummary> parkSummary = analyzeThreadPark(events, topN);
        Optional<BiasedLockSummary> biasedLockSummary = analyzeBiasedLocks(events, topN);

        boolean hasData = parkSummary.isPresent() || biasedLockSummary.isPresent();
        return new LockAnalysisResult(parkSummary, biasedLockSummary, hasData);
    }

    private Optional<ThreadParkSummary> analyzeThreadPark(IItemCollection events, int topN) {
        IItemCollection parks = events.apply(ItemFilters.type("jdk.ThreadPark"));
        long parkCount = JfrItemUtils.count(parks);
        if (parkCount == 0) {
            return Optional.empty();
        }

        IQuantity avgPark = JfrItemUtils.avgQuantity(parks, JfrAttributes.DURATION.getIdentifier());
        IQuantity maxPark = JfrItemUtils.maxQuantity(parks, JfrAttributes.DURATION.getIdentifier());

        Map<String, ParkStats> parkSites = new HashMap<>();
        for (IItemIterable iterable : parks) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
            if (stackAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    Object stack = stackAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (stack != null && duration != null) {
                        String trace = JfrItemUtils.formatStackTrace(stack, 5);
                        ParkStats ps = parkSites.computeIfAbsent(trace, k -> new ParkStats());
                        ps.count++;
                        long nanos = duration.clampedLongValueIn(UnitLookup.NANOSECOND);
                        ps.totalNanos += nanos;
                        ps.maxNanos = Math.max(ps.maxNanos, nanos);
                    }
                }
            }
        }

        List<ParkSiteEntry> topSites = parkSites.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .map(e -> {
                    ParkStats s = e.getValue();
                    return new ParkSiteEntry(
                            e.getKey(),
                            s.count,
                            displayNanos(s.totalNanos / s.count),
                            displayNanos(s.maxNanos)
                    );
                })
                .toList();

        return Optional.of(new ThreadParkSummary(
                parkCount,
                display(avgPark),
                display(maxPark),
                topSites
        ));
    }

    private Optional<BiasedLockSummary> analyzeBiasedLocks(IItemCollection events, int topN) {
        IItemCollection revocs = events.apply(ItemFilters.type("jdk.BiasedLockRevocation"));
        IItemCollection classRevocs = events.apply(ItemFilters.type("jdk.BiasedLockClassRevocation"));
        IItemCollection selfRevocs = events.apply(ItemFilters.type("jdk.BiasedLockSelfRevocation"));

        long rCount = JfrItemUtils.count(revocs);
        long crCount = JfrItemUtils.count(classRevocs);
        long srCount = JfrItemUtils.count(selfRevocs);

        if (rCount == 0 && crCount == 0 && srCount == 0) {
            return Optional.empty();
        }

        Map<String, Long> classCounts = new HashMap<>();
        for (IItemCollection c : new IItemCollection[]{revocs, classRevocs, selfRevocs}) {
            for (IItemIterable iterable : c) {
                IMemberAccessor<Object, IItem> lockClassAcc = JfrItemUtils.getAccessor(iterable.getType(), "lockClass");
                if (lockClassAcc == null) {
                    lockClassAcc = JfrItemUtils.getAccessor(iterable.getType(), "revokedClass");
                }
                if (lockClassAcc != null) {
                    for (IItem item : iterable) {
                        Object lockClass = lockClassAcc.getMember(item);
                        if (lockClass != null) {
                            classCounts.merge(lockClass.toString(), 1L, Long::sum);
                        }
                    }
                }
            }
        }

        List<RevokedLockClassEntry> topClasses = classCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new RevokedLockClassEntry(e.getKey(), e.getValue()))
                .toList();

        return Optional.of(new BiasedLockSummary(rCount, crCount, srCount, topClasses));
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(IDisplayable.AUTO);
    }

    private static String displayNanos(long nanos) {
        return UnitLookup.NANOSECOND.quantity(nanos).displayUsing(IDisplayable.AUTO);
    }

    private static class ParkStats {
        long count;
        long totalNanos;
        long maxNanos;
    }
}
