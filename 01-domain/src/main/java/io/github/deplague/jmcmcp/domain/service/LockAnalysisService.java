package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import io.github.deplague.jmcmcp.domain.service.StackTraceKey;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.deplague.jmcmcp.domain.service.JfrStackTraceService.formatStackTrace;
import static java.lang.Long.compare;
import static java.lang.Math.max;
import static java.util.Map.Entry;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for advanced lock analysis.
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class LockAnalysisService {
    private final JfrAccessorRepository jfrAccessorRepository;
    private final JfrQuantityAggregator jfrQuantityAggregator;

    public LockAnalysisResult analyze(IItemCollection events, int topN) {
        Optional<ThreadParkSummary> parkSummary = analyzeThreadPark(events, topN);
        Optional<BiasedLockSummary> biasedLockSummary = analyzeBiasedLocks(events, topN);

        boolean hasData = parkSummary.isPresent() || biasedLockSummary.isPresent();
        return new LockAnalysisResult(parkSummary, biasedLockSummary, hasData);
    }

    private Optional<ThreadParkSummary> analyzeThreadPark(IItemCollection events, int topN) {
        IItemCollection parks = events.apply(type("jdk.ThreadPark"));
        long parkCount = jfrQuantityAggregator.count(parks);
        if (parkCount == 0) {
            return empty();
        }

        var stats = jfrQuantityAggregator.batchStats(parks, DURATION.getIdentifier());

        Map<StackTraceKey, ParkStats> parkSites = new HashMap<>();
        for (IItemIterable iterable : parks) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> stackAccessor = jfrAccessorRepository.getAccessor(type, "stackTrace");
            IMemberAccessor<IQuantity, IItem> durationAccessor = DURATION.getAccessor(iterable.getType());
            if (stackAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    Object stack = stackAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (stack != null && duration != null) {
                        StackTraceKey trace = new StackTraceKey(stack, 5);
                        ParkStats ps = parkSites.computeIfAbsent(trace, k -> new ParkStats());
                        ps.count++;
                        long nanos = duration.clampedLongValueIn(NANOSECOND);
                        ps.totalNanos += nanos;
                        ps.maxNanos = max(ps.maxNanos, nanos);
                    }
                }
            }
        }

        List<ParkSiteEntry> topSites = parkSites.entrySet().stream()
                .sorted((a, b) -> compare(b.getValue().totalNanos, a.getValue().totalNanos))
                .limit(topN)
                .map(e -> {
                    ParkStats s = e.getValue();
                    return new ParkSiteEntry(
                            formatStackTrace(e.getKey().getStackTraceObj(), 5),
                            s.count,
                            displayNanos(s.totalNanos / s.count),
                            displayNanos(s.maxNanos)
                    );
                })
                .toList();

        return of(new ThreadParkSummary(
                parkCount,
                display(stats.get("avg")),
                display(stats.get("max")),
                topSites
        ));
    }

    private Optional<BiasedLockSummary> analyzeBiasedLocks(IItemCollection events, int topN) {
        IItemCollection revocs = events.apply(type("jdk.BiasedLockRevocation"));
        IItemCollection classRevocs = events.apply(type("jdk.BiasedLockClassRevocation"));
        IItemCollection selfRevocs = events.apply(type("jdk.BiasedLockSelfRevocation"));

        long rCount = jfrQuantityAggregator.count(revocs);
        long crCount = jfrQuantityAggregator.count(classRevocs);
        long srCount = jfrQuantityAggregator.count(selfRevocs);

        if (rCount == 0 && crCount == 0 && srCount == 0) {
            return empty();
        }

        Map<String, Long> classCounts = new HashMap<>();
        for (IItemCollection c : new IItemCollection[]{revocs, classRevocs, selfRevocs}) {
            for (IItemIterable iterable : c) {
                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> lockClassAcc = jfrAccessorRepository.getAccessor(type1, "lockClass");
                if (lockClassAcc == null) {
                    IType<?> type = iterable.getType();
                    lockClassAcc = jfrAccessorRepository.getAccessor(type, "revokedClass");
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
                .sorted(Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new RevokedLockClassEntry(e.getKey(), e.getValue()))
                .toList();

        return of(new BiasedLockSummary(rCount, crCount, srCount, topClasses));
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(AUTO);
    }

    private static String displayNanos(long nanos) {
        return NANOSECOND.quantity(nanos).displayUsing(AUTO);
    }

    private static class ParkStats {
        long count;
        long totalNanos;
        long maxNanos;
    }
}