package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.GcConfiguration;
import io.github.deplague.jmcmcp.domain.model.GcCycleEntry;
import io.github.deplague.jmcmcp.domain.model.GcCauseEntry;
import io.github.deplague.jmcmcp.domain.model.GcDetailResult;
import io.github.deplague.jmcmcp.domain.model.GcPhaseEntry;
import io.github.deplague.jmcmcp.domain.model.GenerationalSummary;
import io.github.deplague.jmcmcp.domain.model.HeapTrendSummary;
import io.github.deplague.jmcmcp.domain.model.ReferenceStatEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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
 * Pure domain service for detailed GC analysis.
 */
public final class GcDetailService {

    public GcDetailResult analyze(IItemCollection events, String detailLevel) {
        boolean showAll = "all".equals(detailLevel);

        GcConfiguration config = null;
        GenerationalSummary genSummary = null;
        List<ReferenceStatEntry> refStats = List.of();
        Double refOverhead = null;
        List<GcCauseEntry> causeDist = List.of();
        List<GcPhaseEntry> phases = List.of();
        HeapTrendSummary heapTrend = null;
        List<GcCycleEntry> cycles = List.of();

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
        IItemCollection config = events.apply(ItemFilters.type("jdk.GCConfiguration"));
        IItemCollection heapConfig = events.apply(ItemFilters.type("jdk.GCHeapConfiguration"));
        IItemCollection survivorConfig = events.apply(ItemFilters.type("jdk.GCSurvivorConfiguration"));

        Optional<IItem> configItem = firstItem(config);
        Optional<IItem> heapItem = firstItem(heapConfig);
        Optional<IItem> survivorItem = firstItem(survivorConfig);

        return new GcConfiguration(
                configItem.flatMap(i -> JfrItemUtils.getMember(i, "youngCollector")).map(Object::toString).orElse("N/A"),
                configItem.flatMap(i -> JfrItemUtils.getMember(i, "oldCollector")).map(Object::toString).orElse("N/A"),
                configItem.flatMap(i -> JfrItemUtils.getMember(i, "parallelGCThreads")).map(Object::toString).orElse("N/A"),
                configItem.flatMap(i -> JfrItemUtils.getMember(i, "concurrentGCThreads")).map(Object::toString).orElse("N/A"),
                heapItem.flatMap(i -> displayOpt(JfrItemUtils.getQuantity(i, "minSize"))).orElse("N/A"),
                heapItem.flatMap(i -> displayOpt(JfrItemUtils.getQuantity(i, "maxSize"))).orElse("N/A"),
                heapItem.flatMap(i -> displayOpt(JfrItemUtils.getQuantity(i, "initialSize"))).orElse("N/A"),
                survivorItem.flatMap(i -> JfrItemUtils.getMember(i, "maxTenuringThreshold")).map(Object::toString).orElse("N/A")
        );
    }

    private GenerationalSummary extractGenerationalSummary(IItemCollection events) {
        IItemCollection young = events.apply(ItemFilters.type("jdk.YoungGarbageCollection"));
        IItemCollection old = events.apply(ItemFilters.type("jdk.OldGarbageCollection"));

        long youngCount = JfrItemUtils.count(young);
        IQuantity youngTotal = JfrItemUtils.sumQuantity(young, JfrAttributes.DURATION.getIdentifier());
        IQuantity youngAvg = JfrItemUtils.avgQuantity(young, JfrAttributes.DURATION.getIdentifier());

        long oldCount = JfrItemUtils.count(old);
        IQuantity oldTotal = JfrItemUtils.sumQuantity(old, JfrAttributes.DURATION.getIdentifier());
        IQuantity oldAvg = JfrItemUtils.avgQuantity(old, JfrAttributes.DURATION.getIdentifier());

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
        IItemCollection refStats = events.apply(ItemFilters.type("jdk.GCReferenceStatistics"));
        IItemCollection refPhases = events.apply(ItemFilters.type("jdk.GCPhasePause"));

        Map<String, Long> refCounts = new HashMap<>();
        for (IItemIterable iterable : refStats) {
            IMemberAccessor<String, IItem> typeAcc = JfrItemUtils.getAccessor(iterable.getType(), "type");
            IMemberAccessor<Object, IItem> countAcc = JfrItemUtils.getAccessor(iterable.getType(), "count");
            if (typeAcc != null && countAcc != null) {
                for (IItem item : iterable) {
                    String type = typeAcc.getMember(item);
                    Object c = countAcc.getMember(item);
                    if (type != null && c != null) {
                        refCounts.merge(type, JfrItemUtils.toLong(c), Long::sum);
                    }
                }
            }
        }

        Map<String, IQuantity> phaseTimes = new HashMap<>();
        for (IItemIterable iterable : refPhases) {
            IMemberAccessor<String, IItem> nameAcc = JfrItemUtils.getAccessor(iterable.getType(), "name");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrAttributes.DURATION.getAccessor(iterable.getType());
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
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
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
        IItemCollection allGcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        IQuantity totalGcPause = JfrItemUtils.sumQuantity(allGcPauses, JfrAttributes.DURATION.getIdentifier());

        Map<String, IQuantity> refPhaseTimes = new HashMap<>();
        for (IItemIterable iterable : allGcPauses) {
            IMemberAccessor<String, IItem> nameAcc = JfrItemUtils.getAccessor(iterable.getType(), "name");
            IMemberAccessor<IQuantity, IItem> durationAcc = JfrAttributes.DURATION.getAccessor(iterable.getType());
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
        IItemCollection gcs = events.apply(ItemFilters.type("jdk.GarbageCollection"));
        for (IItemIterable iterable : gcs) {
            IMemberAccessor<String, IItem> causeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "cause");
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
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(e -> new GcCauseEntry(e.getKey(), e.getValue(), null, null))
                .toList();
    }

    private List<GcPhaseEntry> extractPhaseBreakdown(IItemCollection events) {
        IItemCollection phases = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        if (!phases.hasItems()) {
            return List.of();
        }

        Map<String, List<IQuantity>> phaseDurations = new HashMap<>();
        for (IItemIterable iterable : phases) {
            IMemberAccessor<String, IItem> nameAccessor = JfrItemUtils.getAccessor(iterable.getType(), "name");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(
                    iterable.getType(),
                    JfrAttributes.DURATION.getIdentifier()
            );
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
                sumNs += q.doubleValueIn(UnitLookup.NANOSECOND);
            }
            phaseSums.put(entry.getKey(), UnitLookup.NANOSECOND.quantity(sumNs));
        }

        return phaseDurations.entrySet().stream()
                .sorted((a, b) -> phaseSums.get(b.getKey()).compareTo(phaseSums.get(a.getKey())))
                .map(entry -> {
                    List<IQuantity> durations = entry.getValue();
                    Collections.sort(durations);
                    double sumNs = 0;
                    for (IQuantity q : durations) {
                        sumNs += q.doubleValueIn(UnitLookup.NANOSECOND);
                    }
                    IQuantity avg = UnitLookup.NANOSECOND.quantity(sumNs / durations.size());
                    IQuantity p95 = durations.get((int) Math.max(0, Math.ceil(0.95 * durations.size()) - 1));
                    IQuantity p99 = durations.get((int) Math.max(0, Math.ceil(0.99 * durations.size()) - 1));
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
        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        if (!heapSummary.hasItems()) {
            return null;
        }
        return new HeapTrendSummary(
                display(JfrItemUtils.minQuantity(heapSummary, "heapUsed")),
                display(JfrItemUtils.maxQuantity(heapSummary, "heapUsed")),
                display(JfrItemUtils.avgQuantity(heapSummary, "heapUsed")),
                display(JfrItemUtils.percentileQuantity(heapSummary, "heapUsed", 95))
        );
    }

    private List<GcCycleEntry> extractGcCycles(IItemCollection events) {
        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        if (!heapSummary.hasItems()) {
            return List.of();
        }

        Map<Long, Map<String, IQuantity>> cycleMap = new TreeMap<>();
        for (IItemIterable iterable : heapSummary) {
            IMemberAccessor<Object, IItem> gcIdAccessor = JfrItemUtils.getAccessor(iterable.getType(), "gcId");
            IMemberAccessor<IQuantity, IItem> usedAccessor = JfrItemUtils.getAccessor(iterable.getType(), "heapUsed");
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "heapSize");
            IMemberAccessor<String, IItem> whenAccessor = JfrItemUtils.getAccessor(iterable.getType(), "when");

            if (gcIdAccessor != null) {
                for (IItem item : iterable) {
                    Object gcIdObj = gcIdAccessor.getMember(item);
                    if (gcIdObj != null) {
                        long gcId = JfrItemUtils.toLong(gcIdObj);
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
            return Optional.empty();
        }
        return Optional.of(collection.iterator().next().iterator().next());
    }

    private static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(IDisplayable.AUTO);
    }

    private static Optional<String> displayOpt(Optional<IQuantity> quantity) {
        return quantity.map(q -> q.displayUsing(IDisplayable.AUTO));
    }
}
