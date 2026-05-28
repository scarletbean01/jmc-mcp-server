package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.RecordingComparisonAggType;
import io.github.deplague.jmcmcp.domain.model.RecordingComparisonDelta;
import io.github.deplague.jmcmcp.domain.model.RecordingComparisonMetric;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RunnableFuture;
import java.util.stream.Collectors;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.IUnit;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.IResult;
import org.openjdk.jmc.flightrecorder.rules.RuleRegistry;
import org.openjdk.jmc.flightrecorder.rules.Severity;

/**
 * Pure domain service for comprehensive A/B comparison of two JFR recordings.
 * Contains no MCP-specific or framework logic.
 */
public final class CompareRecordingsService {

    private static final int MAX_RULES_CACHE_ENTRIES = 20;
    private static final Map<String, Map<String, Severity>> RULES_CACHE =
        Collections.synchronizedMap(
            new LinkedHashMap<>(MAX_RULES_CACHE_ENTRIES, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(
                    Map.Entry<String, Map<String, Severity>> eldest
                ) {
                    return size() > MAX_RULES_CACHE_ENTRIES;
                }
            }
        );

    private record AnalysisContext(
        IItemCollection events,
        double durationSec,
        String path
    ) {}

    private static class BatchAgg {
        double sum;
        long count;
        IQuantity max;
        List<IQuantity> values = new ArrayList<>();
        IUnit unit;
    }

    private static final List<RecordingComparisonMetric> METRICS = List.of(
        // CPU
        new RecordingComparisonMetric(
            "CPU",
            "Avg Machine Total",
            "jdk.CPULoad",
            "machineTotal",
            RecordingComparisonAggType.AVG,
            false
        ),
        new RecordingComparisonMetric(
            "CPU",
            "Avg JVM User",
            "jdk.CPULoad",
            "jvmUser",
            RecordingComparisonAggType.AVG,
            false
        ),
        new RecordingComparisonMetric(
            "CPU",
            "Avg JVM System",
            "jdk.CPULoad",
            "jvmSystem",
            RecordingComparisonAggType.AVG,
            false
        ),
        // GC
        new RecordingComparisonMetric(
            "Garbage Collection",
            "Total GC Pause Time",
            "jdk.GCPhasePause",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "Garbage Collection",
            "Max GC Pause Time",
            "jdk.GCPhasePause",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.MAX,
            false
        ),
        new RecordingComparisonMetric(
            "Garbage Collection",
            "P95 GC Pause Time",
            "jdk.GCPhasePause",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.P95,
            false
        ),
        new RecordingComparisonMetric(
            "Garbage Collection",
            "P99 GC Pause Time",
            "jdk.GCPhasePause",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.P99,
            false
        ),
        new RecordingComparisonMetric(
            "Garbage Collection",
            "Young GC Count",
            "jdk.YoungGarbageCollection",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        new RecordingComparisonMetric(
            "Garbage Collection",
            "Old GC Count",
            "jdk.OldGarbageCollection",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        new RecordingComparisonMetric(
            "Garbage Collection",
            "Avg Heap Used",
            "jdk.GCHeapSummary",
            "heapUsed",
            RecordingComparisonAggType.AVG,
            false
        ),
        // Memory
        new RecordingComparisonMetric(
            "Memory",
            "Total TLAB Alloc",
            "jdk.ObjectAllocationInNewTLAB",
            "tlabSize",
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "Memory",
            "Total Non-TLAB Alloc",
            "jdk.ObjectAllocationOutsideTLAB",
            "allocationSize",
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "Memory",
            "Avg Metaspace Used",
            "jdk.MetaspaceSummary",
            "metaspace.used",
            RecordingComparisonAggType.AVG,
            false
        ),
        new RecordingComparisonMetric(
            "Memory",
            "Avg Metaspace Committed",
            "jdk.MetaspaceSummary",
            "metaspace.committed",
            RecordingComparisonAggType.AVG,
            false
        ),
        new RecordingComparisonMetric(
            "Memory",
            "Max Code Cache Entries",
            "jdk.CodeCacheStatistics",
            "entries",
            RecordingComparisonAggType.MAX,
            false
        ),
        // Contention
        new RecordingComparisonMetric(
            "Contention",
            "Total Monitor Enter Duration",
            "jdk.JavaMonitorEnter",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "Contention",
            "P95 Monitor Enter Duration",
            "jdk.JavaMonitorEnter",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.P95,
            false
        ),
        new RecordingComparisonMetric(
            "Contention",
            "Total Monitor Wait Duration",
            "jdk.JavaMonitorWait",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "Contention",
            "P95 Monitor Wait Duration",
            "jdk.JavaMonitorWait",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.P95,
            false
        ),
        // I/O & Networking
        new RecordingComparisonMetric(
            "I/O",
            "Total File Read",
            "jdk.FileRead",
            "bytesRead",
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "I/O",
            "Total File Written",
            "jdk.FileWrite",
            "bytesWritten",
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "I/O",
            "Socket Read Bytes",
            "jdk.SocketRead",
            "bytesRead",
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "I/O",
            "Socket Write Bytes",
            "jdk.SocketWrite",
            "bytesWritten",
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "I/O",
            "Socket Connect Count",
            "jdk.SocketConnect",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        new RecordingComparisonMetric(
            "I/O",
            "Avg Socket Connect Duration",
            "jdk.SocketConnect",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.AVG,
            false
        ),
        // Runtime
        new RecordingComparisonMetric(
            "Runtime",
            "Safepoint Duration",
            "jdk.SafepointStateSynchronization",
            JfrAttributes.DURATION.getIdentifier(),
            RecordingComparisonAggType.SUM,
            true
        ),
        new RecordingComparisonMetric(
            "Runtime",
            "Peak Thread Count",
            "jdk.JavaThreadStatistics",
            "peakCount",
            RecordingComparisonAggType.MAX,
            false
        ),
        new RecordingComparisonMetric(
            "Runtime",
            "Class Load Count",
            "jdk.ClassLoad",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        new RecordingComparisonMetric(
            "Runtime",
            "Class Unload Count",
            "jdk.ClassUnload",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        // JVM Internals
        new RecordingComparisonMetric(
            "JVM Internals",
            "Exception Count",
            "jdk.JavaExceptionThrow",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        new RecordingComparisonMetric(
            "JVM Internals",
            "JIT Compilations",
            "jdk.Compilation",
            null,
            RecordingComparisonAggType.COUNT,
            true
        ),
        new RecordingComparisonMetric(
            "JVM Internals",
            "Deoptimizations",
            "jdk.Deoptimization",
            null,
            RecordingComparisonAggType.COUNT,
            true
        )
    );

    public String analyze(
        IItemCollection baselineEvents,
        double baselineDurationSec,
        String baselinePath,
        IItemCollection targetEvents,
        double targetDurationSec,
        String targetPath
    ) {
        AnalysisContext bCtx = new AnalysisContext(
            baselineEvents,
            baselineDurationSec,
            baselinePath
        );
        AnalysisContext tCtx = new AnalysisContext(
            targetEvents,
            targetDurationSec,
            targetPath
        );

        CompletableFuture<Map<String, List<String>>> metricsTask =
            CompletableFuture.supplyAsync(() -> calculateMetrics(bCtx, tCtx));
        CompletableFuture<Map<String, Severity>> rulesBaselineTask =
            CompletableFuture.supplyAsync(() -> evaluateRules(bCtx));
        CompletableFuture<Map<String, Severity>> rulesTargetTask =
            CompletableFuture.supplyAsync(() -> evaluateRules(tCtx));

        CompletableFuture<List<RecordingComparisonDelta>> cpuDeltasTask =
            CompletableFuture.supplyAsync(() ->
                calculateEventDeltas(
                    bCtx,
                    tCtx,
                    new String[] { "jdk.ExecutionSample" },
                    "stackTrace",
                    null
                )
            );

        CompletableFuture<List<RecordingComparisonDelta>> allocDeltasTask =
            CompletableFuture.supplyAsync(() ->
                calculateEventDeltas(
                    bCtx,
                    tCtx,
                    new String[] {
                        "jdk.ObjectAllocationInNewTLAB",
                        "jdk.ObjectAllocationOutsideTLAB",
                    },
                    "objectClass",
                    "allocationSize"
                )
            );

        CompletableFuture<List<RecordingComparisonDelta>> contentionDeltasTask =
            CompletableFuture.supplyAsync(() ->
                calculateEventDeltas(
                    bCtx,
                    tCtx,
                    new String[] { "jdk.JavaMonitorEnter", "jdk.ThreadPark" },
                    "stackTrace",
                    JfrAttributes.DURATION.getIdentifier()
                )
            );

        CompletableFuture<List<RecordingComparisonDelta>> exceptionDeltasTask =
            CompletableFuture.supplyAsync(() ->
                calculateEventDeltas(
                    bCtx,
                    tCtx,
                    new String[] {
                        "jdk.JavaExceptionThrow",
                        "jdk.JavaErrorThrow",
                    },
                    "thrownClass",
                    null
                )
            );

        try {
            CompletableFuture.allOf(
                metricsTask,
                rulesBaselineTask,
                rulesTargetTask,
                cpuDeltasTask,
                allocDeltasTask,
                contentionDeltasTask,
                exceptionDeltasTask
            ).join();
        } catch (CompletionException e) {
            throw new RuntimeException(
                "Parallel analysis failed: " + e.getCause().getMessage(),
                e.getCause()
            );
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Comprehensive JFR Recording Comparison\n\n");
        sb.append("**Baseline:** `")
            .append(baselinePath)
            .append("` (")
            .append(String.format("%.1fs", bCtx.durationSec))
            .append(")\n");
        sb.append("**Target:** `")
            .append(targetPath)
            .append("` (")
            .append(String.format("%.1fs", tCtx.durationSec))
            .append(")\n\n");

        if (
            Math.abs(bCtx.durationSec - tCtx.durationSec) /
                Math.max(bCtx.durationSec, tCtx.durationSec) >
            0.5
        ) {
            sb.append(
                "> \u26a0\ufe0f **Warning:** Recording durations differ significantly ("
            )
                .append(
                    String.format(
                        "%.1fs vs %.1fs",
                        bCtx.durationSec,
                        tCtx.durationSec
                    )
                )
                .append(
                    "). Normalized per-second comparisons may be skewed by startup or shutdown behavior.\n\n"
                );
        }

        sb.append("## Summary of Major Findings\n\n");
        appendSummary(
            sb,
            metricsTask.join(),
            rulesBaselineTask.join(),
            rulesTargetTask.join()
        );

        sb.append("\n## Detailed Metrics Comparison\n\n");
        sb.append(
            "> **Note:** Metrics marked with `*` (like SUM or COUNT) are normalized per-second before comparing.\n\n"
        );

        for (var entry : metricsTask.join().entrySet()) {
            sb.append("### ").append(entry.getKey()).append("\n");
            sb.append("| Metric | Baseline | Target | Delta | Stat |\n");
            sb.append("|--------|----------|--------|-------|------|\n");
            for (String row : entry.getValue()) {
                sb.append(row).append("\n");
            }
            sb.append("\n");
        }

        appendRulesDiff(sb, rulesBaselineTask.join(), rulesTargetTask.join());
        appendDeltaSection(
            sb,
            "CPU Hotspot Deltas",
            cpuDeltasTask.join(),
            "Samples/sec"
        );
        appendDeltaSection(
            sb,
            "Allocation Deltas",
            allocDeltasTask.join(),
            "Bytes/sec"
        );
        appendDeltaSection(
            sb,
            "Lock Contention Deltas",
            contentionDeltasTask.join(),
            "Nanos/sec"
        );
        appendDeltaSection(
            sb,
            "Exception & Error Deltas",
            exceptionDeltasTask.join(),
            "Throws/sec"
        );

        sb.append(
            "\n<agent_hint>Significant changes detected between recordings. Use `correlate` for deeper analysis of the target recording or `diff_stack_traces` for method-level comparison.</agent_hint>\n"
        );

        return sb.toString();
    }

    private Map<String, List<String>> calculateMetrics(
        AnalysisContext bCtx,
        AnalysisContext tCtx
    ) {
        // Group metrics by (eventId, attrId) to compute all aggregations in a single pass
        Map<String, List<RecordingComparisonMetric>> groups = new LinkedHashMap<>();
        for (RecordingComparisonMetric def : METRICS) {
            String key =
                def.eventId() +
                "|" +
                (def.attrId() != null ? def.attrId() : "__count__");
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(def);
        }

        Map<String, List<String>> categories = new LinkedHashMap<>();
        for (List<RecordingComparisonMetric> group : groups.values()) {
            IItemCollection bEvents = bCtx.events.apply(
                ItemFilters.type(group.getFirst().eventId())
            );
            IItemCollection tEvents = tCtx.events.apply(
                ItemFilters.type(group.getFirst().eventId())
            );

            Map<RecordingComparisonMetric, IQuantity> bVals = computeMetricsBatch(
                bEvents,
                group
            );
            Map<RecordingComparisonMetric, IQuantity> tVals = computeMetricsBatch(
                tEvents,
                group
            );

            for (RecordingComparisonMetric def : group) {
                IQuantity bVal = bVals.get(def);
                IQuantity tVal = tVals.get(def);

                String deltaStr = "N/A";
                String indicator = "\u2139\ufe0f";

                if (bVal != null && tVal != null) {
                    double bv = bVal.doubleValue();
                    double tv = tVal.doubleValue();
                    if (def.normalize()) {
                        bv /= bCtx.durationSec;
                        tv /= tCtx.durationSec;
                    }
                    if (bv != 0) {
                        double diff = ((tv - bv) / bv) * 100.0;
                        deltaStr = String.format("%.2f%%", diff);
                        if (diff > 0) deltaStr = "+" + deltaStr;
                        indicator = getIndicator(diff);
                    } else if (tv != 0) {
                        deltaStr = "+100% (was 0)";
                        indicator = "\ud83d\udd34";
                    } else {
                        deltaStr = "0.00%";
                    }
                }

                String label = def.label() + (def.normalize() ? " *" : "");
                String row = String.format(
                    "| %s | %s | %s | %s | %s |",
                    label,
                    display(bVal),
                    display(tVal),
                    deltaStr,
                    indicator
                );
                categories
                    .computeIfAbsent(def.category(), k -> new ArrayList<>())
                    .add(row);
            }
        }
        return categories;
    }

    private static IQuantity toIQuantity(Object value) {
        if (value instanceof IQuantity q) return q;
        if (value instanceof Number n) return UnitLookup.NUMBER_UNITY.quantity(
            n.doubleValue()
        );
        return null;
    }

    private static IQuantity percentileSorted(
        List<IQuantity> sorted,
        double p
    ) {
        if (sorted == null || sorted.isEmpty()) return null;
        int index = (int) Math.max(
            0,
            Math.ceil((p / 100.0) * sorted.size()) - 1
        );
        return sorted.get(index);
    }

    private Map<RecordingComparisonMetric, IQuantity> computeMetricsBatch(
        IItemCollection events,
        List<RecordingComparisonMetric> defs
    ) {
        String attrId = defs.getFirst().attrId();

        // COUNT-only metrics with no attribute
        if (attrId == null) {
            long totalCount = JfrItemUtils.count(events);
            Map<RecordingComparisonMetric, IQuantity> result = new HashMap<>();
            for (RecordingComparisonMetric def : defs) {
                result.put(def, UnitLookup.NUMBER_UNITY.quantity(totalCount));
            }
            return result;
        }

        boolean needPercentile = defs
            .stream()
            .anyMatch(d -> d.type() == RecordingComparisonAggType.P95 || d.type() == RecordingComparisonAggType.P99);
        BatchAgg agg = new BatchAgg();

        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> accessor = JfrItemUtils.getAccessor(
                iterable.getType(),
                attrId
            );
            if (accessor == null) continue;
            for (IItem item : iterable) {
                Object raw = accessor.getMember(item);
                if (raw == null) continue;
                IQuantity q = toIQuantity(raw);
                if (q == null) continue;
                agg.sum += q.doubleValue();
                agg.count++;
                if (agg.max == null || q.compareTo(agg.max) > 0) agg.max = q;
                if (needPercentile) agg.values.add(q);
                if (agg.unit == null) agg.unit = q.getUnit();
            }
        }

        List<IQuantity> sorted = null;
        if (needPercentile && !agg.values.isEmpty()) {
            sorted = new ArrayList<>(agg.values);
            Collections.sort(sorted);
        }

        Map<RecordingComparisonMetric, IQuantity> result = new HashMap<>();
        for (RecordingComparisonMetric def : defs) {
            result.put(
                def,
                switch (def.type()) {
                    case SUM -> agg.unit != null
                        ? agg.unit.quantity(agg.sum)
                        : null;
                    case AVG -> (agg.count == 0 || agg.unit == null)
                        ? null
                        : agg.unit.quantity(agg.sum / agg.count);
                    case MAX -> agg.max;
                    case P95 -> percentileSorted(sorted, 95.0);
                    case P99 -> percentileSorted(sorted, 99.0);
                    case COUNT -> UnitLookup.NUMBER_UNITY.quantity(agg.count);
                }
            );
        }
        return result;
    }

    private void appendSummary(
        StringBuilder sb,
        Map<String, List<String>> metrics,
        Map<String, Severity> bRules,
        Map<String, Severity> tRules
    ) {
        List<String> majorPoints = new ArrayList<>();

        for (String rule : tRules.keySet()) {
            Severity bSev = bRules.getOrDefault(rule, Severity.OK);
            Severity tSev = tRules.get(rule);
            if (tSev.compareTo(bSev) > 0 && tSev.compareTo(Severity.INFO) > 0) {
                majorPoints.add(
                    "\ud83d\udd34 **New Performance Issue:** Rule `" +
                        rule +
                        "` increased from " +
                        bSev.getLocalizedName() +
                        " to " +
                        tSev.getLocalizedName()
                );
            }
        }

        for (List<String> rows : metrics.values()) {
            for (String row : rows) {
                if (row.contains("\ud83d\udd34")) {
                    String[] parts = row.split("\\|");
                    majorPoints.add(
                        "\ud83d\udd34 **Regression:** " +
                            parts[1].trim() +
                            " increased by " +
                            parts[4].trim()
                    );
                }
            }
        }

        if (majorPoints.isEmpty()) {
            sb.append(
                "No major regressions detected. The system performance appears stable compared to the baseline.\n"
            );
        } else {
            majorPoints
                .stream()
                .limit(5)
                .forEach(p -> sb.append("- ").append(p).append("\n"));
        }
    }

    private String getIndicator(double delta) {
        if (delta > 10.0) return "\ud83d\udd34"; // Significant regression
        if (delta < -10.0) return "\ud83d\ude80"; // Significant improvement
        return "\u2139\ufe0f"; // Minor change
    }

    private Map<String, Severity> evaluateRules(AnalysisContext ctx) {
        return RULES_CACHE.computeIfAbsent(ctx.path, p -> {
            return RuleRegistry.getRules()
                .parallelStream()
                .map(rule -> {
                    try {
                        RunnableFuture<IResult> future = rule.createEvaluation(
                            ctx.events,
                            null,
                            null
                        );
                        future.run();
                        IResult r = future.get();
                        return Map.entry(rule.getName(), r.getSeverity());
                    } catch (Exception e) {
                        return Map.entry(rule.getName(), Severity.OK);
                    }
                })
                .collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
        });
    }

    private void appendRulesDiff(
        StringBuilder sb,
        Map<String, Severity> baselineRules,
        Map<String, Severity> targetRules
    ) {
        sb.append("## JMC Rules Comparison\n\n");
        List<String> regressions = new ArrayList<>();
        List<String> improvements = new ArrayList<>();

        Set<String> allRules = new java.util.TreeSet<>(baselineRules.keySet());
        allRules.addAll(targetRules.keySet());

        for (String ruleName : allRules) {
            Severity bSev = baselineRules.getOrDefault(ruleName, Severity.OK);
            Severity tSev = targetRules.getOrDefault(ruleName, Severity.OK);

            if (tSev.compareTo(bSev) > 0) {
                regressions.add(
                    String.format(
                        "| %s | %s | %s |",
                        ruleName,
                        bSev.getLocalizedName(),
                        tSev.getLocalizedName()
                    )
                );
            } else if (tSev.compareTo(bSev) < 0) {
                improvements.add(
                    String.format(
                        "| %s | %s | %s |",
                        ruleName,
                        bSev.getLocalizedName(),
                        tSev.getLocalizedName()
                    )
                );
            }
        }

        if (regressions.isEmpty() && improvements.isEmpty()) {
            sb.append("No changes in automated rule severities.\n\n");
            return;
        }

        if (!regressions.isEmpty()) {
            sb.append("### \ud83d\udd34 Rule Regressions\n\n");
            sb.append("| Rule | Baseline Severity | Target Severity |\n");
            sb.append("|------|-------------------|-----------------|\n");
            regressions.forEach(r -> sb.append(r).append("\n"));
            sb.append("\n");
        }

        if (!improvements.isEmpty()) {
            sb.append("### \ud83d\ude80 Rule Improvements\n\n");
            sb.append("| Rule | Baseline Severity | Target Severity |\n");
            sb.append("|------|-------------------|-----------------|\n");
            improvements.forEach(r -> sb.append(r).append("\n"));
            sb.append("\n");
        }
    }

    private List<RecordingComparisonDelta> calculateEventDeltas(
        AnalysisContext baseline,
        AnalysisContext target,
        String[] typeIds,
        String keyAttr,
        String valueAttr
    ) {
        Map<String, Double> bRates = calculateRates(
            baseline,
            typeIds,
            keyAttr,
            valueAttr
        );
        Map<String, Double> tRates = calculateRates(
            target,
            typeIds,
            keyAttr,
            valueAttr
        );

        List<RecordingComparisonDelta> deltas = new ArrayList<>();
        Set<String> allKeys = new HashSet<>(bRates.keySet());
        allKeys.addAll(tRates.keySet());

        for (String key : allKeys) {
            double bRate = bRates.getOrDefault(key, 0.0);
            double tRate = tRates.getOrDefault(key, 0.0);
            if (bRate == 0 && tRate == 0) continue;
            deltas.add(new RecordingComparisonDelta(key, bRate, tRate, tRate - bRate));
        }

        deltas.sort(Comparator.comparingDouble(RecordingComparisonDelta::delta).reversed());
        return deltas;
    }

    private Map<String, Double> calculateRates(
        AnalysisContext ctx,
        String[] typeIds,
        String keyAttr,
        String valueAttr
    ) {
        Map<String, Double> rates = new HashMap<>();
        Map<IMCStackTrace, String> stackTraceCache = new IdentityHashMap<>();
        for (String typeId : typeIds) {
            IItemCollection events = ctx.events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : events) {
                IMemberAccessor<Object, IItem> keyAccessor = keyAttr.equals(
                    "stackTrace"
                )
                    ? JfrItemUtils.getAccessor(iterable.getType(), "stackTrace")
                    : JfrItemUtils.getAccessor(iterable.getType(), keyAttr);
                IMemberAccessor<Object, IItem> valAccessor =
                    valueAttr != null
                        ? JfrItemUtils.getAccessor(
                              iterable.getType(),
                              valueAttr
                          )
                        : null;

                // Specialized logic for Allocation rates to handle TLAB vs Non-TLAB
                if (typeId.contains("ObjectAllocation")) {
                    valAccessor = JfrItemUtils.getAccessor(
                        iterable.getType(),
                        typeId.contains("Outside")
                            ? "allocationSize"
                            : "tlabSize"
                    );
                }

                if (keyAccessor != null) {
                    for (IItem item : iterable) {
                        Object keyObj = keyAccessor.getMember(item);
                        if (keyObj != null) {
                            String key;
                            if (
                                keyAttr.equals("stackTrace") &&
                                keyObj instanceof IMCStackTrace stackTrace
                            ) {
                                key = stackTraceCache.get(stackTrace);
                                if (key == null) {
                                    key = JfrItemUtils.formatStackTrace(
                                        stackTrace,
                                        5
                                    );
                                    stackTraceCache.put(stackTrace, key);
                                }
                            } else {
                                key = keyObj.toString();
                            }
                            double val = 1.0;
                            if (valAccessor != null) {
                                Object raw = valAccessor.getMember(item);
                                val = raw instanceof IQuantity q
                                    ? q.doubleValue()
                                    : JfrItemUtils.toDouble(raw);
                                if (Double.isNaN(val)) val = 0;
                                // Handle Nanos
                                if (
                                    raw instanceof IQuantity q &&
                                    q.getUnit().getIdentifier().contains("ns")
                                ) {
                                    val = q.clampedLongValueIn(
                                        UnitLookup.NANOSECOND
                                    );
                                }
                            }
                            rates.merge(
                                key,
                                val / ctx.durationSec,
                                Double::sum
                            );
                        }
                    }
                }
            }
        }
        return rates;
    }

    private void appendDeltaSection(
        StringBuilder sb,
        String title,
        List<RecordingComparisonDelta> deltas,
        String unit
    ) {
        sb.append("## ").append(title).append("\n\n");

        List<RecordingComparisonDelta> regressions = deltas
            .stream()
            .filter(d -> d.delta() > 0)
            .limit(5)
            .toList();
        List<RecordingComparisonDelta> improvements = deltas
            .stream()
            .filter(d -> d.delta() < 0)
            .sorted(Comparator.comparingDouble(RecordingComparisonDelta::delta))
            .limit(3)
            .toList();

        if (regressions.isEmpty() && improvements.isEmpty()) {
            sb.append("No significant changes found.\n\n");
            return;
        }

        sb.append("| Change | Baseline | Target | Source |\n");
        sb.append("|--------|----------|--------|--------|\n");

        for (RecordingComparisonDelta d : regressions) {
            sb.append(
                String.format(
                    "| \ud83d\udd34 %s | %s | %s | `%s` |\n",
                    formatValue(d.delta(), unit),
                    formatValue(d.baselineRate(), unit),
                    formatValue(d.targetRate(), unit),
                    d.key().replace("\n", "`<br>`")
                )
            );
        }
        for (RecordingComparisonDelta d : improvements) {
            sb.append(
                String.format(
                    "| \ud83d\ude80 %s | %s | %s | `%s` |\n",
                    formatValue(d.delta(), unit),
                    formatValue(d.baselineRate(), unit),
                    formatValue(d.targetRate(), unit),
                    d.key().replace("\n", "`<br>`")
                )
            );
        }
        sb.append("\n");
    }

    private String formatValue(double val, String unit) {
        String absVal = String.format("%.2f", Math.abs(val));
        String prefix = val > 0 ? "+" : (val < 0 ? "-" : "");

        if (unit.equals("Bytes/sec")) {
            return prefix + formatBytes((long) Math.abs(val)) + "/s";
        } else if (unit.equals("Nanos/sec")) {
            return (
                prefix +
                formatDuration((long) (Math.abs(val) / 1_000_000)) +
                "/s"
            );
        }
        return prefix + absVal + " " + unit;
    }

    private static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(IDisplayable.AUTO);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        return (millis / 1000.0) + "s";
    }
}
