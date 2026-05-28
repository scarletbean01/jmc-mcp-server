package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.GcCauseEntry;
import io.github.deplague.jmcmcp.domain.model.GcRecommendationsResult;
import io.github.deplague.jmcmcp.domain.model.HeapUtilization;
import io.github.deplague.jmcmcp.domain.model.MetaspaceUtilization;
import io.github.deplague.jmcmcp.domain.model.PauseDistribution;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Pure domain service that analyzes GC patterns and generates tuning recommendations.
 */
public final class GcRecommendationsService {

    public GcRecommendationsResult analyze(IItemCollection events) {
        IItemCollection pauseEvents = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        IItemCollection youngGC = events.apply(ItemFilters.type("jdk.YoungGarbageCollection"));
        IItemCollection oldGC = events.apply(ItemFilters.type("jdk.OldGarbageCollection"));
        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IItemCollection metaspaceSummary = events.apply(ItemFilters.type("jdk.MetaspaceSummary"));
        IItemCollection gcConfig = events.apply(ItemFilters.type("jdk.GCConfiguration"));

        String gcAlgorithm = extractGcAlgorithm(gcConfig);

        List<String> warnings = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();

        long pauseCount = JfrItemUtils.count(pauseEvents);
        PauseDistribution pauseDistribution = null;
        if (pauseCount > 0) {
            IQuantity p50 = JfrItemUtils.percentileQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier(), 50);
            IQuantity p95 = JfrItemUtils.percentileQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier(), 95);
            IQuantity p99 = JfrItemUtils.percentileQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier(), 99);
            IQuantity maxPause = JfrItemUtils.maxQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier());
            IQuantity avgPause = JfrItemUtils.avgQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier());

            pauseDistribution = new PauseDistribution(
                    pauseCount,
                    display(avgPause),
                    display(p50),
                    display(p95),
                    display(p99),
                    display(maxPause)
            );

            if (p95 != null) {
                double p95Ms = p95.clampedLongValueIn(UnitLookup.MILLISECOND);
                if (p95Ms > 500) {
                    warnings.add("P95 GC pause exceeds 500ms — this will noticeably impact application latency");
                    recommendations.add("Consider increasing heap size (`-Xmx`) or switching to a low-pause collector (ZGC, Shenandoah)");
                    if (gcAlgorithm != null && gcAlgorithm.contains("G1")) {
                        recommendations.add("For G1GC: try `-XX:MaxGCPauseMillis=200` and `-XX:G1HeapRegionSize=32m`");
                    }
                } else if (p95Ms > 200) {
                    warnings.add("P95 GC pause exceeds 200ms — may impact latency-sensitive applications");
                    if (gcAlgorithm != null && gcAlgorithm.contains("G1")) {
                        recommendations.add("For G1GC: try `-XX:MaxGCPauseMillis=100` and `-XX:G1MixedGCCountTarget=8`");
                    }
                }
            }
        }

        Map<String, Long> youngCausesMap = extractGcCauses(youngGC);
        Map<String, Long> oldCausesMap = extractGcCauses(oldGC);

        List<GcCauseEntry> youngCauses = toCauseEntries(youngCausesMap);
        List<GcCauseEntry> oldCauses = toCauseEntries(oldCausesMap);

        Double fullGcRatio = null;
        long fullGcCount = oldCausesMap.values().stream().mapToLong(Long::longValue).sum();
        long youngGcCount = youngCausesMap.values().stream().mapToLong(Long::longValue).sum();
        if (fullGcCount > 0 && youngGcCount > 0) {
            fullGcRatio = (double) fullGcCount / youngGcCount;
            if (fullGcRatio > 0.1) {
                warnings.add(String.format("Full GC ratio is %.1f%% (1 Full GC per %.0f Young GCs) — excessive full GCs",
                        fullGcRatio * 100, 1.0 / fullGcRatio));
                recommendations.add("Consider increasing young generation size to reduce promotion failures");
                recommendations.add("Check for memory leaks with `predictive_leak_analysis` tool");
            }
        }

        Long explicitGcCount = null;
        if (oldCausesMap.containsKey("System.gc()") || oldCausesMap.containsKey("Explicit GC")) {
            explicitGcCount = oldCausesMap.getOrDefault("System.gc()", 0L) + oldCausesMap.getOrDefault("Explicit GC", 0L);
            warnings.add(String.format("Detected %d explicit System.gc() calls causing Full GCs", explicitGcCount));
            recommendations.add("Add `-XX:+DisableExplicitGC` to prevent code from triggering Full GCs");
        }

        HeapUtilization heapUtilization = null;
        if (heapSummary.hasItems()) {
            IQuantity minHeapUsed = JfrItemUtils.minQuantity(heapSummary, "heapUsed");
            IQuantity maxHeapUsed = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
            IQuantity avgHeapUsed = JfrItemUtils.avgQuantity(heapSummary, "heapUsed");

            if (maxHeapUsed != null && minHeapUsed != null) {
                double maxMB = maxHeapUsed.longValue() / (1024.0 * 1024.0);
                double minMB = minHeapUsed.longValue() / (1024.0 * 1024.0);
                double heapAmplitude = maxMB > 0 ? ((maxMB - minMB) / maxMB) * 100 : 0;

                heapUtilization = new HeapUtilization(
                        display(minHeapUsed),
                        display(avgHeapUsed),
                        display(maxHeapUsed),
                        heapAmplitude
                );

                if (heapAmplitude > 80) {
                    warnings.add(String.format("Heap amplitude is %.0f%% — heap is nearly emptying and refilling, indicating inefficient memory usage", heapAmplitude));
                    recommendations.add("Consider reducing heap size — the JVM may be over-provisioned");
                }
            }
        }

        MetaspaceUtilization metaspaceUtilization = null;
        if (metaspaceSummary.hasItems()) {
            IQuantity maxMetaUsed = JfrItemUtils.maxQuantity(metaspaceSummary, "metaspace.used");
            IQuantity maxMetaCommitted = JfrItemUtils.maxQuantity(metaspaceSummary, "metaspace.committed");

            if (maxMetaUsed != null && maxMetaCommitted != null) {
                double metaUsedMB = maxMetaUsed.longValue() / (1024.0 * 1024.0);
                double metaCommittedMB = maxMetaCommitted.longValue() / (1024.0 * 1024.0);
                double metaUtilization = metaCommittedMB > 0 ? (metaUsedMB / metaCommittedMB) * 100 : 0;

                metaspaceUtilization = new MetaspaceUtilization(metaUsedMB, metaCommittedMB, metaUtilization);

                if (metaUtilization > 90) {
                    warnings.add(String.format("Metaspace utilization is %.0f%% (%.1f MB used / %.1f MB committed) — risk of Metaspace OOM",
                            metaUtilization, metaUsedMB, metaCommittedMB));
                    recommendations.add("Increase metaspace with `-XX:MaxMetaspaceSize=512m` or investigate class loader leaks");
                }
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("GC behavior appears healthy. No specific tuning recommendations.");
        }

        return new GcRecommendationsResult(
                gcAlgorithm,
                pauseDistribution,
                youngCauses,
                oldCauses,
                fullGcRatio,
                explicitGcCount,
                heapUtilization,
                metaspaceUtilization,
                warnings,
                recommendations
        );
    }

    private String extractGcAlgorithm(IItemCollection gcConfig) {
        for (IItemIterable iterable : gcConfig) {
            IMemberAccessor<Object, IItem> nameAccessor = JfrItemUtils.getAccessor(iterable.getType(), "name");
            if (nameAccessor != null) {
                for (IItem item : iterable) {
                    Object name = nameAccessor.getMember(item);
                    if (name != null) return name.toString();
                }
            }
        }
        return null;
    }

    private Map<String, Long> extractGcCauses(IItemCollection gcEvents) {
        Map<String, Long> causes = new HashMap<>();
        for (IItemIterable iterable : gcEvents) {
            IMemberAccessor<Object, IItem> causeAccessor = JfrItemUtils.getAccessor(iterable.getType(), "cause");
            if (causeAccessor != null) {
                for (IItem item : iterable) {
                    Object cause = causeAccessor.getMember(item);
                    String causeStr = cause != null ? cause.toString() : "unknown";
                    causes.merge(causeStr, 1L, Long::sum);
                }
            } else {
                causes.merge("(cause not available)", JfrItemUtils.count(gcEvents), Long::sum);
                break;
            }
        }
        return causes;
    }

    private List<GcCauseEntry> toCauseEntries(Map<String, Long> causes) {
        return causes.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> new GcCauseEntry(e.getKey(), e.getValue(), null, null))
                .toList();
    }

    private static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(IDisplayable.AUTO);
    }
}
