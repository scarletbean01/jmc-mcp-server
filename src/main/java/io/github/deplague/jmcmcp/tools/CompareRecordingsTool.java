package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * MCP tool for comprehensive comparison of two JFR recordings (A/B testing).
 */
public final class CompareRecordingsTool {

    private static final String NAME = "compare_recordings";

    private final JfrAnalysisService service;

    public CompareRecordingsTool(JfrAnalysisService service) {
        this.service = service;
    }

    private record MetricDef(String category, String label, String eventId, String attrId, AggType type) {}
    private enum AggType { SUM, AVG, MAX, COUNT }

    private static final List<MetricDef> METRICS = List.of(
        // CPU
        new MetricDef("CPU", "Avg Machine Total", "jdk.CPULoad", "machineTotal", AggType.AVG),
        new MetricDef("CPU", "Avg JVM User", "jdk.CPULoad", "jvmUser", AggType.AVG),
        new MetricDef("CPU", "Avg JVM System", "jdk.CPULoad", "jvmSystem", AggType.AVG),
        // GC
        new MetricDef("Garbage Collection", "Total GC Pause Time", "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), AggType.SUM),
        new MetricDef("Garbage Collection", "Max GC Pause Time", "jdk.GCPhasePause", JfrAttributes.DURATION.getIdentifier(), AggType.MAX),
        new MetricDef("Garbage Collection", "Young GC Count", "jdk.YoungGarbageCollection", null, AggType.COUNT),
        new MetricDef("Garbage Collection", "Old GC Count", "jdk.OldGarbageCollection", null, AggType.COUNT),
        new MetricDef("Garbage Collection", "Avg Heap Used", "jdk.GCHeapSummary", "heapUsed", AggType.AVG),
        // Memory
        new MetricDef("Memory", "Total TLAB Alloc", "jdk.ObjectAllocationInNewTLAB", "tlabSize", AggType.SUM),
        new MetricDef("Memory", "Total Non-TLAB Alloc", "jdk.ObjectAllocationOutsideTLAB", "allocationSize", AggType.SUM),
        // Contention
        new MetricDef("Contention", "Total Monitor Enter Duration", "jdk.JavaMonitorEnter", JfrAttributes.DURATION.getIdentifier(), AggType.SUM),
        new MetricDef("Contention", "Total Monitor Wait Duration", "jdk.JavaMonitorWait", JfrAttributes.DURATION.getIdentifier(), AggType.SUM),
        // I/O
        new MetricDef("I/O", "Total File Read", "jdk.FileRead", "bytesRead", AggType.SUM),
        new MetricDef("I/O", "Total File Written", "jdk.FileWrite", "bytesWritten", AggType.SUM),
        new MetricDef("I/O", "Total Socket Read", "jdk.SocketRead", "bytesRead", AggType.SUM),
        new MetricDef("I/O", "Total Socket Written", "jdk.SocketWrite", "bytesWritten", AggType.SUM),
        // JVM Internals
        new MetricDef("JVM Internals", "Exception Count", "jdk.JavaExceptionThrow", null, AggType.COUNT),
        new MetricDef("JVM Internals", "JIT Compilations", "jdk.Compilation", null, AggType.COUNT),
        new MetricDef("JVM Internals", "Deoptimizations", "jdk.Deoptimization", null, AggType.COUNT)
    );

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Perform a comprehensive A/B comparison of two JFR recordings. " +
                                "Compares CPU, GC, Memory, I/O, and JVM internal metrics with delta percentages.")
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

                        String result = analyze(baselinePath, targetPath);
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

        StringBuilder sb = new StringBuilder();
        sb.append("# Comprehensive JFR Recording Comparison\n\n");
        sb.append("**Baseline:** `").append(baselinePath).append("`\n");
        sb.append("**Target:** `").append(targetPath).append("`\n\n");

        Map<String, List<String>> categories = new LinkedHashMap<>();

        for (MetricDef def : METRICS) {
            IItemCollection bEvents = baseline.apply(ItemFilters.type(def.eventId));
            IItemCollection tEvents = target.apply(ItemFilters.type(def.eventId));

            IQuantity bVal = getMetricValue(bEvents, def);
            IQuantity tVal = getMetricValue(tEvents, def);

            String deltaStr = "N/A";
            if (bVal != null && tVal != null) {
                double bv = bVal.doubleValue();
                double tv = tVal.doubleValue();
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
                def.label, 
                JfrAnalysisService.display(bVal), 
                JfrAnalysisService.display(tVal), 
                deltaStr);
            
            categories.computeIfAbsent(def.category, k -> new ArrayList<>()).add(row);
        }

        for (var entry : categories.entrySet()) {
            sb.append("## ").append(entry.getKey()).append("\n");
            sb.append("| Metric | Baseline | Target | Delta |\n");
            sb.append("|--------|----------|--------|-------|\n");
            for (String row : entry.getValue()) {
                sb.append(row).append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private IQuantity getMetricValue(IItemCollection events, MetricDef def) {
        if (!events.hasItems() && def.type != AggType.COUNT) return null;
        
        return switch (def.type) {
            case SUM -> JfrItemUtils.sumQuantity(events, def.attrId);
            case AVG -> JfrItemUtils.avgQuantity(events, def.attrId);
            case MAX -> JfrItemUtils.maxQuantity(events, def.attrId);
            case COUNT -> org.openjdk.jmc.common.unit.UnitLookup.NUMBER_UNITY.quantity(JfrItemUtils.count(events));
        };
    }
}
