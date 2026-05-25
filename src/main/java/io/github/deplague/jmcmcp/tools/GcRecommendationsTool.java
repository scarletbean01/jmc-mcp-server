package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GcRecommendationsTool {

    private static final String NAME = "gc_recommendations";

    private final JfrAnalysisService service;

    public GcRecommendationsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze GC patterns and provide tuning recommendations. " +
                                "Evaluates pause distribution, GC cause patterns, heap utilization, and metaspace pressure " +
                                "to generate actionable JVM tuning advice.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection pauseEvents = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        IItemCollection youngGC = events.apply(ItemFilters.type("jdk.YoungGarbageCollection"));
        IItemCollection oldGC = events.apply(ItemFilters.type("jdk.OldGarbageCollection"));
        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IItemCollection metaspaceSummary = events.apply(ItemFilters.type("jdk.MetaspaceSummary"));
        IItemCollection gcConfig = events.apply(ItemFilters.type("jdk.GCConfiguration"));

        List<String> recommendations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("# GC Tuning Recommendations\n\n");

        String gcAlgorithm = extractGcAlgorithm(gcConfig);
        if (gcAlgorithm != null) {
            sb.append("**GC Algorithm:** ").append(gcAlgorithm).append("\n\n");
        }

        long pauseCount = JfrItemUtils.count(pauseEvents);
        if (pauseCount == 0) {
            sb.append("No GC pause events found. Cannot provide recommendations.\n");
            return sb.toString();
        }

        IQuantity p50Pause = JfrItemUtils.percentileQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier(), 50);
        IQuantity p95Pause = JfrItemUtils.percentileQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier(), 95);
        IQuantity p99Pause = JfrItemUtils.percentileQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier(), 99);
        IQuantity maxPause = JfrItemUtils.maxQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier());
        IQuantity avgPause = JfrItemUtils.avgQuantity(pauseEvents, JfrAttributes.DURATION.getIdentifier());

        sb.append("## Pause Distribution\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Total Pauses | %d |\n", pauseCount));
        sb.append(String.format("| Avg Pause | %s |\n", JfrAnalysisService.display(avgPause)));
        sb.append(String.format("| P50 Pause | %s |\n", JfrAnalysisService.display(p50Pause)));
        sb.append(String.format("| P95 Pause | %s |\n", JfrAnalysisService.display(p95Pause)));
        sb.append(String.format("| P99 Pause | %s |\n", JfrAnalysisService.display(p99Pause)));
        sb.append(String.format("| Max Pause | %s |\n", JfrAnalysisService.display(maxPause)));
        sb.append("\n");

        if (p95Pause != null) {
            double p95Ms = p95Pause.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND);
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

        Map<String, Long> youngCauses = extractGcCauses(youngGC);
        Map<String, Long> oldCauses = extractGcCauses(oldGC);

        sb.append("## GC Cause Analysis\n\n");
        if (!youngCauses.isEmpty()) {
            sb.append("### Young GC Causes\n\n");
            sb.append("| Cause | Count |\n|------|-------|\n");
            youngCauses.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(e -> sb.append(String.format("| %s | %d |\n", e.getKey(), e.getValue())));
            sb.append("\n");
        }
        if (!oldCauses.isEmpty()) {
            sb.append("### Old GC Causes\n\n");
            sb.append("| Cause | Count |\n|------|-------|\n");
            oldCauses.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(e -> sb.append(String.format("| %s | %d |\n", e.getKey(), e.getValue())));
            sb.append("\n");
        }

        long fullGcCount = oldCauses.values().stream().mapToLong(Long::longValue).sum();
        long youngGcCount = youngCauses.values().stream().mapToLong(Long::longValue).sum();
        if (fullGcCount > 0 && youngGcCount > 0) {
            double fullGcRatio = (double) fullGcCount / youngGcCount;
            if (fullGcRatio > 0.1) {
                warnings.add(String.format("Full GC ratio is %.1f%% (1 Full GC per %.0f Young GCs) — excessive full GCs", fullGcRatio * 100, 1.0 / fullGcRatio));
                recommendations.add("Consider increasing young generation size to reduce promotion failures");
                recommendations.add("Check for memory leaks with `predictive_leak_analysis` tool");
            }
        }

        if (oldCauses.containsKey("System.gc()") || oldCauses.containsKey("Explicit GC")) {
            long explicitCount = oldCauses.getOrDefault("System.gc()", 0L) + oldCauses.getOrDefault("Explicit GC", 0L);
            warnings.add(String.format("Detected %d explicit System.gc() calls causing Full GCs", explicitCount));
            recommendations.add("Add `-XX:+DisableExplicitGC` to prevent code from triggering Full GCs");
        }

        if (heapSummary.hasItems()) {
            IQuantity minHeapUsed = JfrItemUtils.minQuantity(heapSummary, "heapUsed");
            IQuantity maxHeapUsed = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
            IQuantity avgHeapUsed = JfrItemUtils.avgQuantity(heapSummary, "heapUsed");

            if (maxHeapUsed != null && minHeapUsed != null) {
                double maxMB = maxHeapUsed.longValue() / (1024.0 * 1024.0);
                double minMB = minHeapUsed.longValue() / (1024.0 * 1024.0);
                double heapAmplitude = maxMB > 0 ? ((maxMB - minMB) / maxMB) * 100 : 0;

                sb.append("## Heap Utilization\n\n");
                sb.append("| Metric | Value |\n");
                sb.append("|--------|-------|\n");
                sb.append(String.format("| Min Heap Used | %s |\n", JfrAnalysisService.display(minHeapUsed)));
                sb.append(String.format("| Avg Heap Used | %s |\n", JfrAnalysisService.display(avgHeapUsed)));
                sb.append(String.format("| Max Heap Used | %s |\n", JfrAnalysisService.display(maxHeapUsed)));
                sb.append(String.format("| Heap Amplitude | %.1f%% |\n", heapAmplitude));
                sb.append("\n");

                if (heapAmplitude > 80) {
                    warnings.add(String.format("Heap amplitude is %.0f%% — heap is nearly emptying and refilling, indicating inefficient memory usage", heapAmplitude));
                    recommendations.add("Consider reducing heap size — the JVM may be over-provisioned");
                }
            }
        }

        if (metaspaceSummary.hasItems()) {
            IQuantity maxMetaUsed = JfrItemUtils.maxQuantity(metaspaceSummary, "metaspace.used");
            IQuantity maxMetaCommitted = JfrItemUtils.maxQuantity(metaspaceSummary, "metaspace.committed");

            if (maxMetaUsed != null && maxMetaCommitted != null) {
                double metaUsedMB = maxMetaUsed.longValue() / (1024.0 * 1024.0);
                double metaCommittedMB = maxMetaCommitted.longValue() / (1024.0 * 1024.0);
                double metaUtilization = metaCommittedMB > 0 ? (metaUsedMB / metaCommittedMB) * 100 : 0;

                if (metaUtilization > 90) {
                    warnings.add(String.format("Metaspace utilization is %.0f%% (%.1f MB used / %.1f MB committed) — risk of Metaspace OOM", metaUtilization, metaUsedMB, metaCommittedMB));
                    recommendations.add("Increase metaspace with `-XX:MaxMetaspaceSize=512m` or investigate class loader leaks");
                }
            }
        }

        if (!warnings.isEmpty()) {
            sb.append("## ⚠️ Warnings\n\n");
            for (String warning : warnings) {
                sb.append("- ").append(warning).append("\n");
            }
            sb.append("\n");
        }

        sb.append("## Recommendations\n\n");
        if (recommendations.isEmpty()) {
            sb.append("GC behavior appears healthy. No specific tuning recommendations.\n");
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, recommendations.get(i)));
            }
        }

        return sb.toString();
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
}