package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for method-level diff between two JFR recordings.
 * Compares execution samples to show which methods appeared, disappeared,
 * or changed in prominence between baseline and target recordings.
 */
public final class DiffStackTracesTool {

    private static final String NAME = "diff_stack_traces";

    private final JfrAnalysisService service;

    public DiffStackTracesTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Compare hot methods between two JFR recordings at the method level. " +
                                "Shows new methods, disappeared methods, and changed prominence (>20% change).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "baseline_jfr_path", SchemaUtil.stringProp("Path to baseline JFR recording"),
                                        "target_jfr_path", SchemaUtil.stringProp("Path to target JFR recording"),
                                        "package_prefix", SchemaUtil.stringProp("Optional package prefix to filter (e.g., 'com.mycompany')"),
                                        "top_n", SchemaUtil.intProp("Number of top methods per category (default 20)", 20),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("baseline_jfr_path", "target_jfr_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String baselinePath = SchemaUtil.getString(request.arguments(), "baseline_jfr_path");
                    String targetPath = SchemaUtil.getString(request.arguments(), "target_jfr_path");
                    String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);
                    int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);
                    return analyze(baselinePath, targetPath, packagePrefix, topN);
                }))
                .build();
    }

    public String analyze(String baselinePath, String targetPath, String packagePrefix, int topN) throws IOException {
        IItemCollection baseline = service.loadRecording(baselinePath);
        IItemCollection target = service.loadRecording(targetPath);

        double baselineDurationSec = getDurationSeconds(baseline);
        double targetDurationSec = getDurationSeconds(target);
        if (baselineDurationSec <= 0) baselineDurationSec = 1.0;
        if (targetDurationSec <= 0) targetDurationSec = 1.0;

        Map<String, Long> baselineMethods = extractMethodCounts(baseline, packagePrefix);
        Map<String, Long> targetMethods = extractMethodCounts(target, packagePrefix);

        Map<String, Double> baselineRates = normalizeRates(baselineMethods, baselineDurationSec);
        Map<String, Double> targetRates = normalizeRates(targetMethods, targetDurationSec);

        Set<String> allMethods = new LinkedHashSet<>();
        allMethods.addAll(baselineRates.keySet());
        allMethods.addAll(targetRates.keySet());

        List<MethodDiff> newMethods = new ArrayList<>();
        List<MethodDiff> disappearedMethods = new ArrayList<>();
        List<MethodDiff> changedMethods = new ArrayList<>();
        List<MethodDiff> stableMethods = new ArrayList<>();

        for (String method : allMethods) {
            double bRate = baselineRates.getOrDefault(method, 0.0);
            double tRate = targetRates.getOrDefault(method, 0.0);
            double absChange = tRate - bRate;
            double pctChange = bRate > 0 ? ((tRate - bRate) / bRate) * 100.0 : (tRate > 0 ? Double.POSITIVE_INFINITY : 0.0);

            MethodDiff diff = new MethodDiff(method, bRate, tRate, absChange, pctChange);

            if (bRate == 0 && tRate > 0) {
                newMethods.add(diff);
            } else if (tRate == 0 && bRate > 0) {
                disappearedMethods.add(diff);
            } else if (Math.abs(pctChange) > 20.0) {
                changedMethods.add(diff);
            } else {
                stableMethods.add(diff);
            }
        }

        newMethods.sort(Comparator.comparingDouble(MethodDiff::targetRate).reversed());
        disappearedMethods.sort(Comparator.comparingDouble(MethodDiff::baselineRate).reversed());
        changedMethods.sort((a, b) -> Double.compare(Math.abs(b.pctChange), Math.abs(a.pctChange)));

        StringBuilder sb = new StringBuilder();
        sb.append("# Stack Trace Diff\n\n");

        sb.append("## Recording Context\n\n");
        sb.append("| Recording | Duration | Total Samples | Samples/sec |\n");
        sb.append("|-----------|----------|---------------|-------------|\n");
        long baselineTotal = baselineMethods.values().stream().mapToLong(Long::longValue).sum();
        long targetTotal = targetMethods.values().stream().mapToLong(Long::longValue).sum();
        sb.append(String.format("| Baseline | %.1fs | %d | %.1f/s |\n", baselineDurationSec, baselineTotal, baselineTotal / baselineDurationSec));
        sb.append(String.format("| Target | %.1fs | %d | %.1f/s |\n", targetDurationSec, targetTotal, targetTotal / targetDurationSec));
        sb.append("\n");

        sb.append("## New Methods (in target, not in baseline)\n\n");
        if (newMethods.isEmpty()) {
            sb.append("No new methods found.\n\n");
        } else {
            sb.append("| Method | Target Samples/s | Baseline Samples/s | Change |\n");
            sb.append("|--------|-----------------|-------------------|--------|\n");
            newMethods.stream().limit(topN).forEach(d ->
                    sb.append(String.format("| `%s` | %.1f | 0 | NEW |\n", d.methodName, d.targetRate)));
            sb.append("\n");
        }

        sb.append("## Disappeared Methods (in baseline, not in target)\n\n");
        if (disappearedMethods.isEmpty()) {
            sb.append("No disappeared methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | Change |\n");
            sb.append("|--------|-------------------|-----------------|--------|\n");
            disappearedMethods.stream().limit(topN).forEach(d ->
                    sb.append(String.format("| `%s` | %.1f | 0 | REMOVED |\n", d.methodName, d.baselineRate)));
            sb.append("\n");
        }

        sb.append("## Changed Prominence (>20% change, normalized)\n\n");
        if (changedMethods.isEmpty()) {
            sb.append("No significantly changed methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | Change | % Change |\n");
            sb.append("|--------|-------------------|-----------------|--------|----------|\n");
            changedMethods.stream().limit(topN).forEach(d ->
                    sb.append(String.format("| `%s` | %.1f | %.1f | %.1f | %.1f%% |\n",
                            d.methodName, d.baselineRate, d.targetRate, d.absoluteChange, d.pctChange)));
            sb.append("\n");
        }

        sb.append("## Stable Methods (<20% change, top 10)\n\n");
        if (stableMethods.isEmpty()) {
            sb.append("No stable methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | % Change |\n");
            sb.append("|--------|-------------------|-----------------|----------|\n");
            stableMethods.stream().limit(10).forEach(d ->
                    sb.append(String.format("| `%s` | %.1f | %.1f | %.1f%% |\n",
                            d.methodName, d.baselineRate, d.targetRate, d.pctChange)));
            sb.append("\n");
        }

        sb.append("<agent_hint>Significant changes detected between recordings. Consider `compare_recordings` for metric-level comparison or `correlate` for deeper analysis of the target recording.</agent_hint>\n");

        return sb.toString();
    }

    private double getDurationSeconds(IItemCollection events) {
        IQuantity start = RulesToolkit.getEarliestStartTime(events);
        IQuantity end = RulesToolkit.getLatestEndTime(events);
        if (start != null && end != null) {
            return end.subtract(start).doubleValueIn(UnitLookup.SECOND);
        }
        return 1.0;
    }

    private Map<String, Long> extractMethodCounts(IItemCollection events, String packagePrefix) {
        Map<String, Long> methodCounts = new HashMap<>();
        IItemCollection samples = events.apply(ItemFilters.type("jdk.ExecutionSample"));

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
            if (stackAccessor == null) continue;

            for (IItem item : iterable) {
                Object st = stackAccessor.getMember(item);
                if (st == null) continue;

                String trace = JfrItemUtils.formatStackTraceFocusingOn(st, 1, packagePrefix);
                if (trace == null || trace.isEmpty() || trace.startsWith("No frames") || trace.startsWith("Empty")) continue;

                methodCounts.merge(trace, 1L, Long::sum);
            }
        }

        return methodCounts;
    }

    private Map<String, Double> normalizeRates(Map<String, Long> methodCounts, double durationSec) {
        Map<String, Double> rates = new HashMap<>();
        for (var entry : methodCounts.entrySet()) {
            rates.put(entry.getKey(), entry.getValue() / durationSec);
        }
        return rates;
    }

    private record MethodDiff(String methodName, double baselineRate, double targetRate,
                               double absoluteChange, double pctChange) {}
}