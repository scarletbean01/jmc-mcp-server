package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.*;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.*;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.*;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrValueConverter.toLong;
import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.util.Collections.sort;
import static java.util.List.of;
import static java.util.Map.Entry;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for detailed GC analysis.
 */
@ApplicationScoped
public final class GcDetailService {

    public GcDetailResult analyze(IItemCollection events, String detailLevel) {
        boolean showAll = "all".equals(detailLevel);

        GcConfiguration config = null;
        GenerationalSummary genSummary = null;
        List<ReferenceStatEntry> refStats = of();
        Double refOverhead = null;
        List<GcCauseEntry> causeDist = of();
        List<GcPhaseEntry> phases = of();
        HeapTrendSummary heapTrend = null;
        List<GcCycleEntry> cycles = of();

        if (showAll || "summary".equals(detailLevel)) {
            config = extractConfiguration(events);
            genSummary = extractGenerationalSummary(events);
            refStats = extractReferenceStatistics(events);
            refOverhead = computeReferenceOverhead(events);
            causeDist = extractCauseDistribution(events);
        }

        if (showAll || "phases".equals(detailLevel)) {
            phases = extractPhaseBreakdown(events);
        }

        if (showAll || "heap_trends".equals(detailLevel)) {
            heapTrend = extractHeapTrendSummary(events);
            cycles = extractGcCycles(events);
        }

        return new GcDetailResult(
                config,
                genSummary,
                refStats,
                refOverhead,
                causeDist,
                phases,
                heapTrend,
                cycles
        );
    }

    private GcConfiguration extractConfiguration(IItemCollection events) {
        IItemCollection config = events.apply(type("jdk.GCConfiguration"));
        IItemCollection heapConfig = events.apply(type("jdk.GCHeapConfiguration"));
        IItemCollection survivorConfig = events.apply(type("jdk.GCSurvivorConfiguration"));

        Optional<IItem> configItem = firstItem(config);
        Optional<IItem> heapItem = firstItem(heapConfig);
        Optional<IItem> survivorItem = firstItem(survivorConfig);

        return new GcConfiguration(
                configItem.flatMap(i -> getMember(i, "youngCollector")).map(Object::toString).orElse("N/A"),
                configItem.flatMap(i -> getMember(i, "oldCollector")).map(Object::toString).orElse("N/A"),
                configItem.flatMap(i -> getMember(i, "parallelGCThreads")).map(Object::toString).orElse("N/A"),
                configItem.flatMap(i -> getMember(i, "concurrentGCThreads")).map(Object::toString).orElse("N/A"),
                heapItem.flatMap(i -> displayOpt(getQuantity(i, "minSize"))).orElse("N/A"),
                heapItem.flatMap(i -> displayOpt(getQuantity(i, "maxSize"))).orElse("N/A"),
                heapItem.flatMap(i -> displayOpt(getQuantity(i, "initialSize"))).orElse("N/A"),
                survivorItem.flatMap(i -> getMember(i, "maxTenuringThreshold")).map(Object::toString).orElse("N/A")
        );
    }

    private GenerationalSummary extractGenerationalSummary(IItemCollection events) {
        IItemCollection young = events.apply(type("jdk.YoungGarbageCollection"));
        IItemCollection old = events.apply(type("jdk.OldGarbageCollection"));

        long youngCount = count(young);
        IQuantity youngTotal = sumQuantity(young, DURATION.getIdentifier());
        IQuantity youngAvg = avgQuantity(young, DURATION.getIdentifier());

        long oldCount = count(old);
        IQuantity oldTotal = sumQuantity(old, DURATION.getIdentifier());
        IQuantity oldAvg = avgQuantity(old, DURATION.getIdentifier());

        return new GenerationalSummary(
                youngCount,
                display(youngTotal),
                display(youngAvg),
                oldCount,
                display(oldTotal),
                display(oldAvg)
        );
    }

    private List<ReferenceStatEntry> extractReferenceStatistics(IItemCollection events) {
        IItemCollection refStats = events.apply(type("jdk.GCReferenceStatistics"));
        IItemCollection refPhases = events.apply(type("jdk.GCPhasePause"));

        Map<String, Long> refCounts = new HashMap<>();
        for (IItemIterable iterable : refStats) {
            IType<?> type2 = iterable.getType();
            IMemberAccessor<String, IItem> typeAcc = getAccessor(type2, "type");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> countAcc = getAccessor(type1, "count");
            if (typeAcc != null && countAcc != null) {
                for (IItem item : iterable) {
                    String type = typeAcc.getMember(item);
                    Object c = countAcc.getMember(item);
                    if (type != null && c != null) {
                        refCounts.merge(type, toLong(c), Long::sum);
                    }
                }
            }
        }

        Map<String, IQuantity> phaseTimes = new HashMap<>();
        for (IItemIterable iterable : refPhases) {
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> nameAcc = getAccessor(type, "name");
            IMemberAccessor<IQuantity, IItem> durationAcc = DURATION.getAccessor(iterable.getType());
            if (nameAcc != null && durationAcc != null) {
                for (IItem item : iterable) {
                    String name = nameAcc.getMember(item);
                    if (name != null && (name.contains("Reference") || name.contains("Ref "))) {
                        IQuantity d = durationAcc.getMember(item);
                        if (d != null) {
                            phaseTimes.merge(name, d, IQuantity::add);
                        }
                    }
                }
            }
        }

        List<ReferenceStatEntry> entries = new ArrayList<>();
        refCounts.entrySet().stream()
                .sorted(Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> {
                    String type = e.getKey();
                    String match = phaseTimes.keySet().stream()
                            .filter(k -> k.toLowerCase().contains(type.toLowerCase().replace("reference", "")))
                            .findFirst()
                            .orElse(null);
                    String timeStr = match != null ? display(phaseTimes.get(match)) : "N/A";
                    if (match != null) {
                        phaseTimes.remove(match);
                    }
                    entries.add(new ReferenceStatEntry(type, e.getValue(), timeStr));
                });

        phaseTimes.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(e -> entries.add(new ReferenceStatEntry(e.getKey(), 0L, display(e.getValue()))));

        return entries;
    }

    private Double computeReferenceOverhead(IItemCollection events) {
        IItemCollection allGcPauses = events.apply(type("jdk.GCPhasePause"));
        IQuantity totalGcPause = sumQuantity(allGcPauses, DURATION.getIdentifier());

        Map<String, IQuantity> refPhaseTimes = new HashMap<>();
        for (IItemIterable iterable : allGcPauses) {
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> nameAcc = getAccessor(type, "name");
            IMemberAccessor<IQuantity, IItem> durationAcc = DURATION.getAccessor(iterable.getType());
            if (nameAcc != null && durationAcc != null) {
                for (IItem item : iterable) {
                    String name = nameAcc.getMember(item);
                    if (name != null && (name.contains("Reference") || name.contains("Ref "))) {
                        IQuantity d = durationAcc.getMember(item);
                        if (d != null) {
                            refPhaseTimes.merge(name, d, IQuantity::add);
                        }
                    }
                }
            }
        }

        IQuantity totalRefPauseTime = null;
        for (IQuantity qty : refPhaseTimes.values()) {
            if (totalRefPauseTime == null) {
                totalRefPauseTime = qty;
            } else {
                totalRefPauseTime = totalRefPauseTime.add(qty);
            }
        }

        if (totalGcPause != null && totalRefPauseTime != null && totalGcPause.doubleValue() > 0) {
            return (totalRefPauseTime.doubleValue() / totalGcPause.doubleValue()) * 100.0;
        }
        return null;
    }

    private List<GcCauseEntry> extractCauseDistribution(IItemCollection events) {
        Map<String, Integer> causeCounts = new HashMap<>();
        IItemCollection gcs = events.apply(type("jdk.GarbageCollection"));
        for (IItemIterable iterable : gcs) {
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> causeAccessor = getAccessor(type, "cause");
            if (causeAccessor != null) {
                for (IItem item : iterable) {
                    String cause = causeAccessor.getMember(item);
                    if (cause != null) {
                        causeCounts.merge(cause, 1, Integer::sum);
                    }
                }
            }
        }

        return causeCounts.entrySet().stream()
                .sorted(Entry.<String, Integer>comparingByValue().reversed())
                .map(e -> new GcCauseEntry(e.getKey(), e.getValue(), null, null))
                .toList();
    }

    private List<GcPhaseEntry> extractPhaseBreakdown(IItemCollection events) {
        IItemCollection phases = events.apply(type("jdk.GCPhasePause"));
        if (!phases.hasItems()) {
            return of();
        }

        Map<String, List<IQuantity>> phaseDurations = new HashMap<>();
        for (IItemIterable iterable : phases) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<String, IItem> nameAccessor = getAccessor(type1, "name");
            IType<?> type = iterable.getType();
            IMemberAccessor<IQuantity, IItem> durationAccessor = getAccessor(type, DURATION.getIdentifier());
            if (nameAccessor != null && durationAccessor != null) {
                for (IItem item : iterable) {
                    String name = nameAccessor.getMember(item);
                    IQuantity duration = durationAccessor.getMember(item);
                    if (name != null && duration != null) {
                        phaseDurations.computeIfAbsent(name, k -> new ArrayList<>()).add(duration);
                    }
                }
            }
        }

        Map<String, IQuantity> phaseSums = new HashMap<>();
        for (var entry : phaseDurations.entrySet()) {
            double sumNs = 0;
            for (IQuantity q : entry.getValue()) {
                sumNs += q.doubleValueIn(NANOSECOND);
            }
            phaseSums.put(entry.getKey(), NANOSECOND.quantity(sumNs));
        }

        return phaseDurations.entrySet().stream()
                .sorted((a, b) -> phaseSums.get(b.getKey()).compareTo(phaseSums.get(a.getKey())))
                .map(entry -> {
                    List<IQuantity> durations = entry.getValue();
                    sort(durations);
                    double sumNs = 0;
                    for (IQuantity q : durations) {
                        sumNs += q.doubleValueIn(NANOSECOND);
                    }
                    IQuantity avg = NANOSECOND.quantity(sumNs / durations.size());
                    IQuantity p95 = durations.get((int) max(0, ceil(0.95 * durations.size()) - 1));
                    IQuantity p99 = durations.get((int) max(0, ceil(0.99 * durations.size()) - 1));
                    IQuantity max = durations.getLast();
                    return new GcPhaseEntry(
                            entry.getKey(),
                            durations.size(),
                            display(avg),
                            display(p95),
                            display(p99),
                            display(max)
                    );
                })
                .toList();
    }

    private HeapTrendSummary extractHeapTrendSummary(IItemCollection events) {
        IItemCollection heapSummary = events.apply(type("jdk.GCHeapSummary"));
        if (!heapSummary.hasItems()) {
            return null;
        }
        return new HeapTrendSummary(
                display(minQuantity(heapSummary, "heapUsed")),
                display(maxQuantity(heapSummary, "heapUsed")),
                display(avgQuantity(heapSummary, "heapUsed")),
                display(percentileQuantity(heapSummary, "heapUsed", 95))
        );
    }

    private List<GcCycleEntry> extractGcCycles(IItemCollection events) {
        IItemCollection heapSummary = events.apply(type("jdk.GCHeapSummary"));
        if (!heapSummary.hasItems()) {
            return of();
        }

        Map<Long, Map<String, IQuantity>> cycleMap = new TreeMap<>();
        for (IItemIterable iterable : heapSummary) {
            IType<?> type3 = iterable.getType();
            IMemberAccessor<Object, IItem> gcIdAccessor = getAccessor(type3, "gcId");
            IType<?> type2 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> usedAccessor = getAccessor(type2, "heapUsed");
            IType<?> type1 = iterable.getType();
            IMemberAccessor<IQuantity, IItem> sizeAccessor = getAccessor(type1, "heapSize");
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> whenAccessor = getAccessor(type, "when");

            if (gcIdAccessor != null) {
                for (IItem item : iterable) {
                    Object gcIdObj = gcIdAccessor.getMember(item);
                    if (gcIdObj != null) {
                        long gcId = toLong(gcIdObj);
                        String when = whenAccessor != null ? whenAccessor.getMember(item) : "After GC";
                        if ("After GC".equals(when) || !cycleMap.containsKey(gcId)) {
                            Map<String, IQuantity> data = new HashMap<>();
                            if (usedAccessor != null) data.put("used", usedAccessor.getMember(item));
                            if (sizeAccessor != null) data.put("size", sizeAccessor.getMember(item));
                            cycleMap.put(gcId, data);
                        }
                    }
                }
            }
        }

        return cycleMap.entrySet().stream()
                .limit(20)
                .map(entry -> new GcCycleEntry(
                        entry.getKey(),
                        display(entry.getValue().get("used")),
                        display(entry.getValue().get("size"))
                ))
                .toList();
    }

    private Optional<IItem> firstItem(IItemCollection collection) {
        if (!collection.hasItems()) {
            return empty();
        }
        return Optional.of(collection.iterator().next().iterator().next());
    }

    private static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(AUTO);
    }

    private static Optional<String> displayOpt(Optional<IQuantity> quantity) {
        return quantity.map(q -> q.displayUsing(AUTO));
    }
}
