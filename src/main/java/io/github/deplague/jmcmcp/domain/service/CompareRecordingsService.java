package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.model.RecordingComparisonDelta;
import io.github.deplague.jmcmcp.domain.model.RecordingComparisonMetric;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.IUnit;
import org.openjdk.jmc.flightrecorder.rules.IResult;
import org.openjdk.jmc.flightrecorder.rules.Severity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RunnableFuture;

import static io.github.deplague.jmcmcp.domain.model.RecordingComparisonAggType.*;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.count;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrValueConverter.toDouble;
import static java.lang.Double.isNaN;
import static java.lang.Math.*;
import static java.lang.String.format;
import static java.util.Collections.sort;
import static java.util.Collections.synchronizedMap;
import static java.util.Comparator.comparingDouble;
import static java.util.List.of;
import static java.util.Map.Entry;
import static java.util.Map.entry;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toMap;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND;
import static org.openjdk.jmc.common.unit.UnitLookup.NUMBER_UNITY;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;
import static org.openjdk.jmc.flightrecorder.rules.RuleRegistry.getRules;
import static org.openjdk.jmc.flightrecorder.rules.Severity.INFO;
import static org.openjdk.jmc.flightrecorder.rules.Severity.OK;

/**
 * Pure domain service for comprehensive A/B comparison of two JFR recordings.
 * Contains no MCP-specific or framework logic.
 */
@ApplicationScoped
public final class CompareRecordingsService {

    private static final int MAX_RULES_CACHE_ENTRIES = 20;
    private static final Map<String, Map<String, Severity>> RULES_CACHE =
            synchronizedMap(
                    new LinkedHashMap<>(MAX_RULES_CACHE_ENTRIES, 0.75f, true) {
                        @Override
                        protected boolean removeEldestEntry(
                                Entry<String, Map<String, Severity>> eldest
                        ) {
                            return size() > MAX_RULES_CACHE_ENTRIES;
                        }
                    }
            );

    private record AnalysisContext(
            IItemCollection events,
            double durationSec,
            String path
    ) {
    }

    private static class BatchAgg {
        double sum;
        long count;
        IQuantity max;
        List<IQuantity> values = new ArrayList<>();
        IUnit unit;
    }

    private static final List<RecordingComparisonMetric> METRICS = of(
            // CPU
            new RecordingComparisonMetric(
                    "CPU",
                    "Avg Machine Total",
                    "jdk.CPULoad",
                    "machineTotal",
                    AVG,
                    false
            ),
            new RecordingComparisonMetric(
                    "CPU",
                    "Avg JVM User",
                    "jdk.CPULoad",
                    "jvmUser",
                    AVG,
                    false
            ),
            new RecordingComparisonMetric(
                    "CPU",
                    "Avg JVM System",
                    "jdk.CPULoad",
                    "jvmSystem",
                    AVG,
                    false
            ),
            // GC
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "Total GC Pause Time",
                    "jdk.GCPhasePause",
                    DURATION.getIdentifier(),
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "Max GC Pause Time",
                    "jdk.GCPhasePause",
                    DURATION.getIdentifier(),
                    MAX,
                    false
            ),
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "P95 GC Pause Time",
                    "jdk.GCPhasePause",
                    DURATION.getIdentifier(),
                    P95,
                    false
            ),
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "P99 GC Pause Time",
                    "jdk.GCPhasePause",
                    DURATION.getIdentifier(),
                    P99,
                    false
            ),
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "Young GC Count",
                    "jdk.YoungGarbageCollection",
                    null,
                    COUNT,
                    true
            ),
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "Old GC Count",
                    "jdk.OldGarbageCollection",
                    null,
                    COUNT,
                    true
            ),
            new RecordingComparisonMetric(
                    "Garbage Collection",
                    "Avg Heap Used",
                    "jdk.GCHeapSummary",
                    "heapUsed",
                    AVG,
                    false
            ),
            // Memory
            new RecordingComparisonMetric(
                    "Memory",
                    "Total TLAB Alloc",
                    "jdk.ObjectAllocationInNewTLAB",
                    "tlabSize",
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "Memory",
                    "Total Non-TLAB Alloc",
                    "jdk.ObjectAllocationOutsideTLAB",
                    "allocationSize",
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "Memory",
                    "Avg Metaspace Used",
                    "jdk.MetaspaceSummary",
                    "metaspace.used",
                    AVG,
                    false
            ),
            new RecordingComparisonMetric(
                    "Memory",
                    "Avg Metaspace Committed",
                    "jdk.MetaspaceSummary",
                    "metaspace.committed",
                    AVG,
                    false
            ),
            new RecordingComparisonMetric(
                    "Memory",
                    "Max Code Cache Entries",
                    "jdk.CodeCacheStatistics",
                    "entries",
                    MAX,
                    false
            ),
            // Contention
            new RecordingComparisonMetric(
                    "Contention",
                    "Total Monitor Enter Duration",
                    "jdk.JavaMonitorEnter",
                    DURATION.getIdentifier(),
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "Contention",
                    "P95 Monitor Enter Duration",
                    "jdk.JavaMonitorEnter",
                    DURATION.getIdentifier(),
                    P95,
                    false
            ),
            new RecordingComparisonMetric(
                    "Contention",
                    "Total Monitor Wait Duration",
                    "jdk.JavaMonitorWait",
                    DURATION.getIdentifier(),
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "Contention",
                    "P95 Monitor Wait Duration",
                    "jdk.JavaMonitorWait",
                    DURATION.getIdentifier(),
                    P95,
                    false
            ),
            // I/O & Networking
            new RecordingComparisonMetric(
                    "I/O",
                    "Total File Read",
                    "jdk.FileRead",
                    "bytesRead",
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "I/O",
                    "Total File Written",
                    "jdk.FileWrite",
                    "bytesWritten",
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "I/O",
                    "Socket Read Bytes",
                    "jdk.SocketRead",
                    "bytesRead",
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "I/O",
                    "Socket Write Bytes",
                    "jdk.SocketWrite",
                    "bytesWritten",
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "I/O",
                    "Socket Connect Count",
                    "jdk.SocketConnect",
                    null,
                    COUNT,
                    true
            ),
            new RecordingComparisonMetric(
                    "I/O",
                    "Avg Socket Connect Duration",
                    "jdk.SocketConnect",
                    DURATION.getIdentifier(),
                    AVG,
                    false
            ),
            // Runtime
            new RecordingComparisonMetric(
                    "Runtime",
                    "Safepoint Duration",
                    "jdk.SafepointStateSynchronization",
                    DURATION.getIdentifier(),
                    SUM,
                    true
            ),
            new RecordingComparisonMetric(
                    "Runtime",
                    "Peak Thread Count",
                    "jdk.JavaThreadStatistics",
                    "peakCount",
                    MAX,
                    false
            ),
            new RecordingComparisonMetric(
                    "Runtime",
                    "Class Load Count",
                    "jdk.ClassLoad",
                    null,
                    COUNT,
                    true
            ),
            new RecordingComparisonMetric(
                    "Runtime",
                    "Class Unload Count",
                    "jdk.ClassUnload",
                    null,
                    COUNT,
                    true
            ),
            // JVM Internals
            new RecordingComparisonMetric(
                    "JVM Internals",
                    "Exception Count",
                    "jdk.JavaExceptionThrow",
                    null,
                    COUNT,
                    true
            ),
            new RecordingComparisonMetric(
                    "JVM Internals",
                    "JIT Compilations",
                    "jdk.Compilation",
                    null,
                    COUNT,
                    true
            ),
            new RecordingComparisonMetric(
                    "JVM Internals",
                    "Deoptimizations",
                    "jdk.Deoptimization",
                    null,
                    COUNT,
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
                supplyAsync(() -> calculateMetrics(bCtx, tCtx));
        CompletableFuture<Map<String, Severity>> rulesBaselineTask =
                supplyAsync(() -> evaluateRules(bCtx));
        CompletableFuture<Map<String, Severity>> rulesTargetTask =
                supplyAsync(() -> evaluateRules(tCtx));

        CompletableFuture<List<RecordingComparisonDelta>> cpuDeltasTask =
                supplyAsync(() ->
                        calculateEventDeltas(
                                bCtx,
                                tCtx,
                                new String[]{"jdk.ExecutionSample"},
                                "stackTrace",
                                null
                        )
                );

        CompletableFuture<List<RecordingComparisonDelta>> allocDeltasTask =
                supplyAsync(() ->
                        calculateEventDeltas(
                                bCtx,
                                tCtx,
                                new String[]{
                                        "jdk.ObjectAllocationInNewTLAB",
                                        "jdk.ObjectAllocationOutsideTLAB",
                                },
                                "objectClass",
                                "allocationSize"
                        )
                );

        CompletableFuture<List<RecordingComparisonDelta>> contentionDeltasTask =
                supplyAsync(() ->
                        calculateEventDeltas(
                                bCtx,
                                tCtx,
                                new String[]{"jdk.JavaMonitorEnter", "jdk.ThreadPark"},
                                "stackTrace",
                                DURATION.getIdentifier()
                        )
                );

        CompletableFuture<List<RecordingComparisonDelta>> exceptionDeltasTask =
                supplyAsync(() ->
                        calculateEventDeltas(
                                bCtx,
                                tCtx,
                                new String[]{
                                        "jdk.JavaExceptionThrow",
                                        "jdk.JavaErrorThrow",
                                },
                                "thrownClass",
                                null
                        )
                );

        try {
            allOf(
                    metricsTask,
                    rulesBaselineTask,
                    rulesTargetTask,
                    cpuDeltasTask,
                    allocDeltasTask,
                    contentionDeltasTask,
                    exceptionDeltasTask
            ).join();
        } catch (CompletionException e) {
            throw new AnalysisFailedException(
                    "Parallel analysis failed: " + e.getCause().getMessage(),
                    e.getCause()
            );
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Comprehensive JFR Recording Comparison\n\n");
        sb.append("**Baseline:** `")
                .append(baselinePath)
                .append("` (")
                .append(format("%.1fs", bCtx.durationSec))
                .append(")\n");
        sb.append("**Target:** `")
                .append(targetPath)
                .append("` (")
                .append(format("%.1fs", tCtx.durationSec))
                .append(")\n\n");

        if (
                abs(bCtx.durationSec - tCtx.durationSec) /
                        max(bCtx.durationSec, tCtx.durationSec) >
                        0.5
        ) {
            sb.append(
                            "> \u26a0\ufe0f **Warning:** Recording durations differ significantly ("
                    )
                    .append(
                            format(
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
                    type(group.getFirst().eventId())
            );
            IItemCollection tEvents = tCtx.events.apply(
                    type(group.getFirst().eventId())
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
                        deltaStr = format("%.2f%%", diff);
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
                String row = format(
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
        if (value instanceof Number n) return NUMBER_UNITY.quantity(
                n.doubleValue()
        );
        return null;
    }

    private static IQuantity percentileSorted(
            List<IQuantity> sorted,
            double p
    ) {
        if (sorted == null || sorted.isEmpty()) return null;
        int index = (int) max(
                0,
                ceil((p / 100.0) * sorted.size()) - 1
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
            long totalCount = count(events);
            Map<RecordingComparisonMetric, IQuantity> result = new HashMap<>();
            for (RecordingComparisonMetric def : defs) {
                result.put(def, NUMBER_UNITY.quantity(totalCount));
            }
            return result;
        }

        boolean needPercentile = defs
                .stream()
                .anyMatch(d -> d.type() == P95 || d.type() == P99);
        BatchAgg agg = new BatchAgg();

        for (IItemIterable iterable : events) {
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> accessor = getAccessor(type, attrId);
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
            sort(sorted);
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
                        case COUNT -> NUMBER_UNITY.quantity(agg.count);
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
            Severity bSev = bRules.getOrDefault(rule, OK);
            Severity tSev = tRules.get(rule);
            if (tSev.compareTo(bSev) > 0 && tSev.compareTo(INFO) > 0) {
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
            return getRules()
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
                            return entry(rule.getName(), r.getSeverity());
                        } catch (Exception e) {
                            return entry(rule.getName(), OK);
                        }
                    })
                    .collect(
                            toMap(Entry::getKey, Entry::getValue)
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

        Set<String> allRules = new TreeSet<>(baselineRules.keySet());
        allRules.addAll(targetRules.keySet());

        for (String ruleName : allRules) {
            Severity bSev = baselineRules.getOrDefault(ruleName, OK);
            Severity tSev = targetRules.getOrDefault(ruleName, OK);

            if (tSev.compareTo(bSev) > 0) {
                regressions.add(
                        format(
                                "| %s | %s | %s |",
                                ruleName,
                                bSev.getLocalizedName(),
                                tSev.getLocalizedName()
                        )
                );
            } else if (tSev.compareTo(bSev) < 0) {
                improvements.add(
                        format(
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

        deltas.sort(comparingDouble(RecordingComparisonDelta::delta).reversed());
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
            IItemCollection events = ctx.events.apply(type(typeId));
            for (IItemIterable iterable : events) {
                IMemberAccessor<Object, IItem> keyAccessor;
                if (keyAttr.equals(
                        "stackTrace"
                )) {
                    IType<?> type = iterable.getType();
                    keyAccessor = getAccessor(type, "stackTrace");
                } else {
                    IType<?> type = iterable.getType();
                    keyAccessor = getAccessor(type, keyAttr);
                }
                IMemberAccessor<Object, IItem> valAccessor;
                if (valueAttr != null) {
                    IType<?> type = iterable.getType();
                    valAccessor = getAccessor(type, valueAttr);
                } else {
                    valAccessor = null;
                }

                // Specialized logic for Allocation rates to handle TLAB vs Non-TLAB
                if (typeId.contains("ObjectAllocation")) {
                    IType<?> type = iterable.getType();
                    valAccessor = getAccessor(type, typeId.contains("Outside")
                            ? "allocationSize"
                            : "tlabSize");
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
                                    key = formatStackTrace(stackTrace, 5);
                                    stackTraceCache.put(stackTrace, key);
                                }
                            } else {
                                key = keyObj.toString();
                            }
                            double val = 1.0;
                            if (valAccessor != null) {
                                Object raw = valAccessor.getMember(item);
                                val = raw instanceof IQuantity q ? q.doubleValue() : toDouble(raw);
                                if (isNaN(val)) val = 0;
                                // Handle Nanos
                                if (
                                        raw instanceof IQuantity q &&
                                                q.getUnit().getIdentifier().contains("ns")
                                ) {
                                    val = q.clampedLongValueIn(
                                            NANOSECOND
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
                .sorted(comparingDouble(RecordingComparisonDelta::delta))
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
                    format(
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
                    format(
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
        String absVal = format("%.2f", abs(val));
        String prefix = val > 0 ? "+" : (val < 0 ? "-" : "");

        if (unit.equals("Bytes/sec")) {
            return prefix + formatBytes((long) abs(val)) + "/s";
        } else if (unit.equals("Nanos/sec")) {
            return (
                    prefix +
                            formatDuration((long) (abs(val) / 1_000_000)) +
                            "/s"
            );
        }
        return prefix + absVal + " " + unit;
    }

    private static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(AUTO);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (log(bytes) / log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return format("%.2f %sB", bytes / pow(1024, exp), pre);
    }

    private static String formatDuration(long millis) {
        if (millis < 1000) return millis + "ms";
        return (millis / 1000.0) + "s";
    }
}
