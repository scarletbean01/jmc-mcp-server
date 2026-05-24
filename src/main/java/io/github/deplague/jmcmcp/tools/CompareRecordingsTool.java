package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for comprehensive comparison of two JFR recordings (A/B testing).
 */
public final class CompareRecordingsTool {

    private static final String NAME = "compare_recordings";

    private final JfrAnalysisService service;

    public CompareRecordingsTool(JfrAnalysisService service) {
        this.service = service;
    }

    private record MetricDef(String category, String label, String eventId, String attrId, AggType type, boolean normalize) {
    }

    private enum AggType {SUM, AVG, MAX, COUNT, P95, P99}

    private static final List<MetricDef> METRICS = List.of(
            // CPU
            new MetricDef("CPU", "Avg Machine Total", "jdk.CPULoad", "machineTotal", AggType.AVG, false),
            new MetricDef("CPU", "Avg JVM User", "jdk.CPULoad", "jvmUser", AggType.AVG, false),
            new MetricDef("CPU", "Avg JVM System", "jdk.CPULoad", "jvmSystem", AggType.AVG, false),
            // GC
            new MetricDef("Garbage Collection", "Total GC Pause Time", "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), AggType.SUM, true),
            new MetricDef("Garbage Collection", "Max GC Pause Time", "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), AggType.MAX, false),
            new MetricDef("Garbage Collection", "P95 GC Pause Time", "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), AggType.P95, false),
            new MetricDef("Garbage Collection", "P99 GC Pause Time", "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), AggType.P99, false),
            new MetricDef("Garbage Collection", "Young GC Count", "jdk.YoungGarbageCollection", null, AggType.COUNT, true),
            new MetricDef("Garbage Collection", "Old GC Count", "jdk.OldGarbageCollection", null, AggType.COUNT, true),
            new MetricDef("Garbage Collection", "Avg Heap Used", "jdk.GCHeapSummary", "heapUsed", AggType.AVG, false),
            // Memory
            new MetricDef("Memory", "Total TLAB Alloc", "jdk.ObjectAllocationInNewTLAB", "tlabSize", AggType.SUM, true),
            new MetricDef("Memory", "Total Non-TLAB Alloc", "jdk.ObjectAllocationOutsideTLAB", "allocationSize", AggType.SUM, true),
            new MetricDef("Memory", "Avg Metaspace Used", "jdk.MetaspaceSummary", "metaspace.used", AggType.AVG, false),
            new MetricDef("Memory", "Max Code Cache Entries", "jdk.CodeCacheStatistics", "entries", AggType.MAX, false),
            // Contention
            new MetricDef("Contention", "Total Monitor Enter Duration", "jdk.JavaMonitorEnter", JfrAttributes.DURATION.getIdentifier(), AggType.SUM, true),
            new MetricDef("Contention", "P95 Monitor Enter Duration", "jdk.JavaMonitorEnter", JfrAttributes.DURATION.getIdentifier(), AggType.P95, false),
            new MetricDef("Contention", "Total Monitor Wait Duration", "jdk.JavaMonitorWait", JfrAttributes.DURATION.getIdentifier(), AggType.SUM, true),
            new MetricDef("Contention", "P95 Monitor Wait Duration", "jdk.JavaMonitorWait", JfrAttributes.DURATION.getIdentifier(), AggType.P95, false),
            // I/O & Networking
            new MetricDef("I/O", "Total File Read", "jdk.FileRead", "bytesRead", AggType.SUM, true),
            new MetricDef("I/O", "Total File Written", "jdk.FileWrite", "bytesWritten", AggType.SUM, true),
            new MetricDef("I/O", "Socket Connect Count", "jdk.SocketConnect", null, AggType.COUNT, true),
            new MetricDef("I/O", "Avg Socket Connect Duration", "jdk.SocketConnect", JfrAttributes.DURATION.getIdentifier(), AggType.AVG, false),
            new MetricDef("I/O", "Max Socket Connect Duration", "jdk.SocketConnect", JfrAttributes.DURATION.getIdentifier(), AggType.MAX, false),
            // Runtime
            new MetricDef("Runtime", "Safepoint Duration", "jdk.SafepointStateSynchronization", JfrAttributes.DURATION.getIdentifier(), AggType.SUM, true),
            new MetricDef("Runtime", "Peak Thread Count", "jdk.JavaThreadStatistics", "peakCount", AggType.MAX, false),
            // JVM Internals
            new MetricDef("JVM Internals", "Exception Count", "jdk.JavaExceptionThrow", null, AggType.COUNT, true),
            new MetricDef("JVM Internals", "JIT Compilations", "jdk.Compilation", null, AggType.COUNT, true),
            new MetricDef("JVM Internals", "Deoptimizations", "jdk.Deoptimization", null, AggType.COUNT, true)
    );

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Perform a comprehensive expert-level A/B comparison of two JFR recordings. " +
                                "Compares CPU, GC (including P95/P99), Memory, I/O, Safepoints, and JVM internals.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "baseline_jfr_path", SchemaUtil.jfrFileProp(),
                                        "target_jfr_path", SchemaUtil.jfrFileProp()
                                ),
                                SchemaUtil.required("baseline_jfr_path", "target_jfr_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String baselinePath = SchemaUtil.getString(request.arguments(), "baseline_jfr_path");
                        String targetPath = SchemaUtil.getString(request.arguments(), "target_jfr_path");

                        String cacheKey = baselinePath + "::" + targetPath;
                        String cached = service.getCachedResult(cacheKey, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(baselinePath, targetPath);
                        service.cacheResult(cacheKey, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String baselinePath, String targetPath) throws IOException {
        IItemCollection baseline = service.loadRecording(baselinePath);
        IItemCollection target = service.loadRecording(targetPath);

        IQuantity bStart = RulesToolkit.getEarliestStartTime(baseline);
        IQuantity bEnd = RulesToolkit.getLatestEndTime(baseline);
        double bDurationSec = (bStart != null && bEnd != null) ? bEnd.subtract(bStart).doubleValueIn(UnitLookup.SECOND) : 1.0;
        if (bDurationSec <= 0) bDurationSec = 1.0;

        IQuantity tStart = RulesToolkit.getEarliestStartTime(target);
        IQuantity tEnd = RulesToolkit.getLatestEndTime(target);
        double tDurationSec = (tStart != null && tEnd != null) ? tEnd.subtract(tStart).doubleValueIn(UnitLookup.SECOND) : 1.0;
        if (tDurationSec <= 0) tDurationSec = 1.0;

        StringBuilder sb = new StringBuilder();
        sb.append("# Comprehensive JFR Recording Comparison\n\n");
        sb.append("**Baseline:** `").append(baselinePath).append("` (").append(String.format("%.1fs", bDurationSec)).append(")\n");
        sb.append("**Target:** `").append(targetPath).append("` (").append(String.format("%.1fs", tDurationSec)).append(")\n\n");
        sb.append("> **Note:** Metrics marked with `*` (like SUM or COUNT) are normalized per-second before comparing to account for differing recording durations.\n\n");

        Map<String, List<String>> categories = new LinkedHashMap<>();

        for (MetricDef def : METRICS) {
            IItemCollection bEvents = baseline.apply(ItemFilters.type(def.eventId));
            IItemCollection tEvents = target.apply(ItemFilters.type(def.eventId));

            IQuantity bVal = getMetricValue(bEvents, def);
            IQuantity tVal = getMetricValue(tEvents, def);

            String deltaStr = "N/A";
            String label = def.label + (def.normalize ? " *" : "");
            
            if (bVal != null && tVal != null) {
                double bv = bVal.doubleValue();
                double tv = tVal.doubleValue();
                
                if (def.normalize) {
                    bv = bv / bDurationSec;
                    tv = tv / tDurationSec;
                }
                
                if (bv != 0) {
                    double diff = (tv - bv) / bv * 100.0;
                    deltaStr = String.format("%.2f%%", diff);
                    if (diff > 0) deltaStr = "+" + deltaStr;
                } else if (tv != 0) {
                    deltaStr = "+100% (was 0)";
                } else {
                    deltaStr = "0.00%";
                }
            }

            String row = String.format("| %s | %s | %s | %s |",
                    label,
                    JfrAnalysisService.display(bVal),
                    JfrAnalysisService.display(tVal),
                    deltaStr);

            categories.computeIfAbsent(def.category, k -> new ArrayList<>()).add(row);
        }

        for (var entry : categories.entrySet()) {
            sb.append("## ").append(entry.getKey()).append("\n");
            sb.append("| Metric | Baseline | Target | Delta (normalized) |\n");
            sb.append("|--------|----------|--------|-------|\n");
            for (String row : entry.getValue()) {
                sb.append(row).append("\n");
            }
            sb.append("\n");
        }

        appendHotMethodDiff(sb, baseline, target, bDurationSec, tDurationSec);
        appendAllocationDiff(sb, baseline, target, bDurationSec, tDurationSec);
        appendContentionDiff(sb, baseline, target, bDurationSec, tDurationSec);
        appendExceptionDiff(sb, baseline, target, bDurationSec, tDurationSec);

        return sb.toString();
    }

    private void appendContentionDiff(StringBuilder sb, IItemCollection baseline, IItemCollection target, double bDur, double tDur) {
        sb.append("## Lock Contention Regressions\n\n");
        Map<String, Double> bRates = calculateContentionRates(baseline, bDur);
        Map<String, Double> tRates = calculateContentionRates(target, tDur);
        appendRegressionTable(sb, bRates, tRates, "Nanos/sec");
    }

    private Map<String, Double> calculateContentionRates(IItemCollection allEvents, double durationSec) {
        Map<String, Double> rates = new HashMap<>();
        for (String typeId : new String[]{"jdk.JavaMonitorEnter", "jdk.ThreadPark"}) {
            IItemCollection events = allEvents.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : events) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<IQuantity, IItem> durationAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                if (stackAccessor != null && durationAccessor != null) {
                    for (IItem item : iterable) {
                        Object st = stackAccessor.getMember(item);
                        IQuantity dur = durationAccessor.getMember(item);
                        if (st != null && dur != null) {
                            String trace = JfrItemUtils.formatStackTrace(st, 5);
                            rates.merge(trace, dur.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND) / durationSec, Double::sum);
                        }
                    }
                }
            }
        }
        return rates;
    }

    private void appendExceptionDiff(StringBuilder sb, IItemCollection baseline, IItemCollection target, double bDur, double tDur) {
        sb.append("## Exception & Error Regressions\n\n");
        Map<String, Double> bRates = calculateExceptionRates(baseline, bDur);
        Map<String, Double> tRates = calculateExceptionRates(target, tDur);
        appendRegressionTable(sb, bRates, tRates, "Throws/sec");
    }

    private Map<String, Double> calculateExceptionRates(IItemCollection allEvents, double durationSec) {
        Map<String, Double> rates = new HashMap<>();
        for (String typeId : new String[]{"jdk.JavaExceptionThrow", "jdk.JavaErrorThrow"}) {
            IItemCollection events = allEvents.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : events) {
                IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "thrownClass");
                if (classAccessor != null) {
                    for (IItem item : iterable) {
                        Object cls = classAccessor.getMember(item);
                        if (cls != null) {
                            rates.merge(cls.toString(), 1.0 / durationSec, Double::sum);
                        }
                    }
                }
            }
        }
        return rates;
    }

    private void appendHotMethodDiff(StringBuilder sb, IItemCollection baseline, IItemCollection target, double bDur, double tDur) {
        sb.append("## CPU Hotspot Regressions\n\n");
        Map<String, Double> bRates = calculateTraceRates(baseline.apply(ItemFilters.type("jdk.ExecutionSample")), bDur);
        Map<String, Double> tRates = calculateTraceRates(target.apply(ItemFilters.type("jdk.ExecutionSample")), tDur);

        appendRegressionTable(sb, bRates, tRates, "Samples/sec");
    }

    private void appendAllocationDiff(StringBuilder sb, IItemCollection baseline, IItemCollection target, double bDur, double tDur) {
        sb.append("## Allocation Regressions\n\n");
        Map<String, Double> bRates = calculateAllocRates(baseline, bDur);
        Map<String, Double> tRates = calculateAllocRates(target, tDur);

        appendRegressionTable(sb, bRates, tRates, "Bytes/sec");
    }

    private void appendRegressionTable(StringBuilder sb, Map<String, Double> bRates, Map<String, Double> tRates, String unit) {
        Map<String, Double> deltas = new HashMap<>();
        for (var entry : tRates.entrySet()) {
            String key = entry.getKey();
            double tRate = entry.getValue();
            double bRate = bRates.getOrDefault(key, 0.0);
            if (tRate > bRate) {
                deltas.put(key, tRate - bRate);
            }
        }

        if (deltas.isEmpty()) {
            sb.append("No significant regressions found.\n\n");
            return;
        }

        sb.append("| Delta (").append(unit).append(") | Baseline Rate | Target Rate | Source (top 5 frames or class) |\n");
        sb.append("|--------|---------------|-------------|--------------------------------|\n");

        deltas.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> {
                    String key = e.getKey();
                    double delta = e.getValue();
                    double bRate = bRates.getOrDefault(key, 0.0);
                    double tRate = tRates.get(key);
                    
                    String dStr = String.format("+%.2f", delta);
                    String bStr = String.format("%.2f", bRate);
                    String tStr = String.format("%.2f", tRate);
                    
                    if (unit.equals("Bytes/sec")) {
                        dStr = "+" + SchemaUtil.formatBytes((long) delta) + "/s";
                        bStr = SchemaUtil.formatBytes((long) bRate) + "/s";
                        tStr = SchemaUtil.formatBytes((long) tRate) + "/s";
                    } else if (unit.equals("Nanos/sec")) {
                        dStr = "+" + SchemaUtil.formatDuration((long) (delta / 1_000_000)) + "/s";
                        bStr = SchemaUtil.formatDuration((long) (bRate / 1_000_000)) + "/s";
                        tStr = SchemaUtil.formatDuration((long) (tRate / 1_000_000)) + "/s";
                    }

                    sb.append(String.format("| %s | %s | %s | `%s` |\n",
                            dStr, bStr, tStr, key.replace("\n", "`<br>`")));
                });
        sb.append("\n");
    }

    private Map<String, Double> calculateTraceRates(IItemCollection events, double durationSec) {
        Map<String, Double> rates = new HashMap<>();
        for (IItemIterable iterable : events) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (stackAccessor != null) {
                for (IItem item : iterable) {
                    Object st = stackAccessor.getMember(item);
                    if (st != null) {
                        String trace = JfrItemUtils.formatStackTrace(st, 5);
                        rates.merge(trace, 1.0 / durationSec, Double::sum);
                    }
                }
            }
        }
        return rates;
    }

    private Map<String, Double> calculateAllocRates(IItemCollection allEvents, double durationSec) {
        Map<String, Double> rates = new HashMap<>();
        for (String typeId : new String[]{"jdk.ObjectAllocationInNewTLAB", "jdk.ObjectAllocationOutsideTLAB"}) {
            IItemCollection events = allEvents.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : events) {
                IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
                IMemberAccessor<IQuantity, IItem> allocAccessor = JfrItemUtils.getAccessor(iterable.getType(), typeId.contains("Outside") ? "allocationSize" : "tlabSize");
                
                if (classAccessor != null && allocAccessor != null) {
                    for (IItem item : iterable) {
                        Object cls = classAccessor.getMember(item);
                        IQuantity alloc = allocAccessor.getMember(item);
                        if (cls != null && alloc != null) {
                            double bytes = alloc.doubleValue();
                            rates.merge(cls.toString(), bytes / durationSec, Double::sum);
                        }
                    }
                }
            }
        }
        return rates;
    }

    private IQuantity getMetricValue(IItemCollection events, MetricDef def) {
        if (!events.hasItems() && def.type != AggType.COUNT) return null;

        return switch (def.type) {
            case SUM -> JfrItemUtils.sumQuantity(events, def.attrId);
            case AVG -> JfrItemUtils.avgQuantity(events, def.attrId);
            case MAX -> JfrItemUtils.maxQuantity(events, def.attrId);
            case P95 -> JfrItemUtils.percentileQuantity(events, def.attrId, 95.0);
            case P99 -> JfrItemUtils.percentileQuantity(events, def.attrId, 99.0);
            case COUNT -> org.openjdk.jmc.common.unit.UnitLookup.NUMBER_UNITY.quantity(JfrItemUtils.count(events));
        };
    }
}
