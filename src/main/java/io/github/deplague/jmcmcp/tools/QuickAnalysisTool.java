package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Macro tool for one-click overview dashboard.
 * Orchestrates system health, hot methods, GC analysis, thread contention,
 * I/O hotspots, and error analysis with severity classification.
 */
public final class QuickAnalysisTool {

    private static final String NAME = "smart_quick_analysis";

    private final JfrAnalysisService service;

    public QuickAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
            .tool(
                McpSchema.Tool.builder()
                    .name(NAME)
                    .description(
                        "One-click overview dashboard that runs the most impactful analyses in a single call " +
                            "with severity classification. Auto-detects the dominant bottleneck."
                    )
                    .inputSchema(
                        SchemaUtil.objectSchema(
                            SchemaUtil.props(
                                "jfr_file_path",
                                SchemaUtil.jfrFileProp(),
                                "start_time",
                                SchemaUtil.startTimeProp(),
                                "end_time",
                                SchemaUtil.endTimeProp(),
                                "focus",
                                SchemaUtil.stringProp(
                                    "Focus area: cpu, memory, latency, locks, or auto (default)",
                                    List.of(
                                        "cpu",
                                        "memory",
                                        "latency",
                                        "locks",
                                        "auto"
                                    )
                                ),
                                "async",
                                SchemaUtil.boolProp(
                                    "Run analysis asynchronously and return a job ID",
                                    false
                                )
                            ),
                            SchemaUtil.required("jfr_file_path")
                        )
                    )
                    .build()
            )
            .callHandler((exchange, request) ->
                service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(
                        request.arguments(),
                        "jfr_file_path"
                    );
                    String startTimeStr = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "start_time",
                        null
                    );
                    String endTimeStr = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "end_time",
                        null
                    );
                    String focus = SchemaUtil.getStringOrDefault(
                        request.arguments(),
                        "focus",
                        "auto"
                    );
                    return analyze(filePath, startTimeStr, endTimeStr, focus);
                })
            )
            .build();
    }

    public String analyze(
        String filePath,
        String startTimeStr,
        String endTimeStr,
        String focus
    ) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(
            allEvents,
            startTimeStr,
            endTimeStr
        );

        StringBuilder sb = new StringBuilder();
        sb.append("# Quick Analysis Dashboard\n\n");

        // Pre-compute shared metrics once to avoid redundant passes
        SharedMetrics metrics = computeSharedMetrics(events);

        // Step 1: Recording Overview
        sb.append("## Recording Overview\n\n");
        appendRecordingOverview(sb, metrics);

        // Step 2: System Health
        sb.append("\n---\n\n## System Health\n\n");
        SystemHealthTool healthTool = new SystemHealthTool(service);
        try {
            String healthResult = healthTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr
            );
            sb.append(healthResult.replace("# System Health Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed to gather system health: ")
                .append(e.getMessage())
                .append("\n");
        }

        // Step 3: Severity-Classified Findings
        sb.append("\n---\n\n## Severity-Classified Findings\n\n");
        List<Finding> findings = classifySeverity(events, metrics);
        for (Finding f : findings) {
            sb.append(f.severityIcon)
                .append(" **")
                .append(f.severity)
                .append(":** ")
                .append(f.message)
                .append("\n");
        }
        if (findings.isEmpty()) {
            sb.append("🟢 **LOW:** No significant issues detected.\n");
        }
        sb.append("\n");

        // Step 4-8: Focus-specific deep dives
        String effectiveFocus = focus;
        if ("auto".equals(focus)) {
            effectiveFocus = detectDominantBottleneck(metrics);
        }

        sb.append("---\n\n");
        switch (effectiveFocus) {
            case "cpu" -> appendCpuFocus(
                sb,
                filePath,
                startTimeStr,
                endTimeStr
            );
            case "memory" -> appendMemoryFocus(
                sb,
                filePath,
                startTimeStr,
                endTimeStr
            );
            case "latency" -> appendLatencyFocus(
                sb,
                filePath,
                startTimeStr,
                endTimeStr
            );
            case "locks" -> appendLocksFocus(
                sb,
                filePath,
                startTimeStr,
                endTimeStr
            );
            default -> appendAutoFocus(
                sb,
                filePath,
                startTimeStr,
                endTimeStr,
                events
            );
        }

        // Recommended next steps
        sb.append("\n---\n\n## Recommended Next Steps\n\n");
        sb.append(generateRecommendations(findings, effectiveFocus));

        return sb.toString();
    }

    private record SharedMetrics(
        long totalEvents,
        IQuantity avgCpu,
        long gcPauseCount,
        IQuantity p99GcPause,
        long exceptionCount,
        long errorCount,
        IQuantity totalLockDuration,
        IQuantity avgSocketRead,
        IQuantity maxHeapUsed,
        IQuantity maxHeapSize,
        long jitCount
    ) {}

    private SharedMetrics computeSharedMetrics(IItemCollection events) {
        IQuantity avgCpu = JfrItemUtils.avgQuantity(
            events.apply(ItemFilters.type("jdk.CPULoad")),
            "machineTotal"
        );
        IItemCollection gcPauses = events.apply(
            ItemFilters.type("jdk.GCPhasePause")
        );
        long gcPauseCount = JfrItemUtils.count(gcPauses);
        IQuantity p99GcPause = JfrItemUtils.percentileQuantity(
            gcPauses,
            JfrAttributes.DURATION.getIdentifier(),
            99
        );
        long exceptionCount = JfrItemUtils.count(
            events.apply(ItemFilters.type("jdk.JavaExceptionThrow"))
        );
        long errorCount = JfrItemUtils.count(
            events.apply(ItemFilters.type("jdk.JavaErrorThrow"))
        );
        IQuantity totalLockDuration = JfrItemUtils.sumQuantity(
            events.apply(ItemFilters.type("jdk.JavaMonitorEnter")),
            JfrAttributes.DURATION.getIdentifier()
        );
        IQuantity avgSocketRead = JfrItemUtils.avgQuantity(
            events.apply(ItemFilters.type("jdk.SocketRead")),
            JfrAttributes.DURATION.getIdentifier()
        );
        IItemCollection heapSummary = events.apply(
            ItemFilters.type("jdk.GCHeapSummary")
        );
        IQuantity maxHeapUsed = JfrItemUtils.maxQuantity(
            heapSummary,
            "heapUsed"
        );
        IQuantity maxHeapSize = JfrItemUtils.maxQuantity(
            heapSummary,
            "heapSize"
        );
        long jitCount = JfrItemUtils.count(
            events.apply(ItemFilters.type("jdk.Compilation"))
        );
        long totalEvents = JfrItemUtils.count(events);
        return new SharedMetrics(
            totalEvents,
            avgCpu,
            gcPauseCount,
            p99GcPause,
            exceptionCount,
            errorCount,
            totalLockDuration,
            avgSocketRead,
            maxHeapUsed,
            maxHeapSize,
            jitCount
        );
    }

    private void appendRecordingOverview(
        StringBuilder sb,
        SharedMetrics metrics
    ) {
        sb.append("- **Total Events:** ")
            .append(metrics.totalEvents)
            .append("\n");
        if (metrics.avgCpu != null) sb.append(
            String.format(
                "- **Avg CPU Load:** %.1f%%\n",
                metrics.avgCpu.doubleValue() * 100
            )
        );
        sb.append("- **GC Pauses:** ")
            .append(metrics.gcPauseCount)
            .append("\n");
        sb.append("- **Exceptions:** ")
            .append(metrics.exceptionCount)
            .append("\n");
        sb.append("- **Errors:** ").append(metrics.errorCount).append("\n");
    }

    private List<Finding> classifySeverity(IItemCollection events, SharedMetrics metrics) {
        List<Finding> findings = new ArrayList<>();

        // CPU
        if (metrics.avgCpu != null) {
            double cpuPct = metrics.avgCpu.doubleValue() * 100;
            if (cpuPct > 90) findings.add(
                new Finding("CRITICAL", "🔴", String.format("CPU utilization %.1f%% (machine total)", cpuPct))
            );
            else if (cpuPct > 75) findings.add(
                new Finding("HIGH", "🟠", String.format("CPU utilization %.1f%%", cpuPct))
            );
            else if (cpuPct > 60) findings.add(
                new Finding("MEDIUM", "🟡", String.format("CPU utilization %.1f%%", cpuPct))
            );
        }

        // Lock contention
        if (metrics.totalLockDuration != null) {
            double lockSec = metrics.totalLockDuration.doubleValueIn(
                org.openjdk.jmc.common.unit.UnitLookup.SECOND
            );
            if (lockSec > 30) findings.add(
                new Finding("CRITICAL", "🔴", String.format("Lock contention total %.1fs across all monitors", lockSec))
            );
            else if (lockSec > 10) findings.add(
                new Finding("HIGH", "🟠", String.format("Lock contention total %.1fs", lockSec))
            );
            else if (lockSec > 5) findings.add(
                new Finding("MEDIUM", "🟡", String.format("Lock contention total %.1fs", lockSec))
            );
        }

        // GC pauses
        if (metrics.p99GcPause != null) {
            double p99Ms = metrics.p99GcPause.doubleValueIn(
                org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND
            );
            if (p99Ms > 500) findings.add(
                new Finding("CRITICAL", "🔴", String.format("GC P99 pause %.1fms", p99Ms))
            );
            else if (p99Ms > 200) findings.add(
                new Finding("HIGH", "🟠", String.format("GC P99 pause %.1fms (%d pauses)", p99Ms, metrics.gcPauseCount))
            );
            else if (p99Ms > 100) findings.add(
                new Finding("MEDIUM", "🟡", String.format("GC P99 pause %.1fms", p99Ms))
            );
        }

        // I/O latency
        if (metrics.avgSocketRead != null) {
            double avgMs = metrics.avgSocketRead.doubleValueIn(
                org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND
            );
            if (avgMs > 500) findings.add(
                new Finding("CRITICAL", "🔴", String.format("Socket read latency %.1fms avg", avgMs))
            );
            else if (avgMs > 100) findings.add(
                new Finding("HIGH", "🟠", String.format("Socket read latency %.1fms avg", avgMs))
            );
            else if (avgMs > 50) findings.add(
                new Finding("MEDIUM", "🟡", String.format("Socket read latency %.1fms avg", avgMs))
            );
        }

        // Heap usage
        if (metrics.maxHeapUsed != null && metrics.maxHeapSize != null && metrics.maxHeapSize.doubleValue() > 0) {
            double heapPct = (metrics.maxHeapUsed.doubleValue() / metrics.maxHeapSize.doubleValue()) * 100;
            if (heapPct > 90) findings.add(
                new Finding("CRITICAL", "🔴", String.format("Heap usage %.1f%% of max", heapPct))
            );
            else if (heapPct > 75) findings.add(
                new Finding("HIGH", "🟠", String.format("Heap usage %.1f%% of max", heapPct))
            );
            else if (heapPct > 60) findings.add(
                new Finding("MEDIUM", "🟡", String.format("Heap usage %.1f%% of max", heapPct))
            );
        }

        // Errors
        if (metrics.errorCount > 0) {
            findings.add(
                new Finding("HIGH", "🟠", String.format("%d Java errors thrown", metrics.errorCount))
            );
        }

        // JIT compilation storm
        if (metrics.jitCount > 500) {
            findings.add(
                new Finding("MEDIUM", "🟡", String.format("JIT compilation storm (%d compilations)", metrics.jitCount))
            );
        }

        return findings;
    }

    private String detectDominantBottleneck(SharedMetrics metrics) {
        double cpuScore = metrics.avgCpu != null ? metrics.avgCpu.doubleValue() : 0;
        double lockScore = metrics.totalLockDuration != null
            ? metrics.totalLockDuration.doubleValueIn(org.openjdk.jmc.common.unit.UnitLookup.SECOND)
            : 0;
        double gcScore = metrics.p99GcPause != null
            ? metrics.p99GcPause.doubleValueIn(org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND)
            : 0;
        double ioScore = metrics.avgSocketRead != null
            ? metrics.avgSocketRead.doubleValueIn(org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND)
            : 0;

        if (cpuScore > 0.75) return "cpu";
        if (lockScore > 10) return "locks";
        if (gcScore > 200) return "memory";
        if (ioScore > 100) return "latency";
        return "cpu"; // default
    }

    private void appendCpuFocus(
        StringBuilder sb,
        String filePath,
        String startTimeStr,
        String endTimeStr
    ) {
        sb.append("## CPU Analysis (Focus)\n\n");
        ThreadCpuTool threadCpuTool = new ThreadCpuTool(service);
        try {
            String result = threadCpuTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                null,
                10
            );
            sb.append(result.replace("# Thread CPU Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top Hot Methods\n\n");
        HotMethodsTool hotMethodsTool = new HotMethodsTool(service);
        try {
            String result = hotMethodsTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                null,
                null,
                10
            );
            sb.append(result.replace("# Hot Methods & Call Paths\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    private void appendMemoryFocus(
        StringBuilder sb,
        String filePath,
        String startTimeStr,
        String endTimeStr
    ) {
        sb.append("## GC Analysis (Focus)\n\n");
        GcAnalysisTool gcTool = new GcAnalysisTool(service);
        try {
            String result = gcTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                "all"
            );
            sb.append(result.replace("# GC Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Heap Trends\n\n");
        HeapTrendsTool heapTool = new HeapTrendsTool(service);
        try {
            String result = heapTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                "1m"
            );
            sb.append(
                result
                    .replace("# Heap, Metaspace & Thread Trends\n\n", "")
                    .replace("# Heap Trends\n\n", "")
            );
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    private void appendLatencyFocus(
        StringBuilder sb,
        String filePath,
        String startTimeStr,
        String endTimeStr
    ) {
        sb.append("## Thread Contention (Focus)\n\n");
        ThreadContentionTool contentionTool = new ThreadContentionTool(service);
        try {
            String result = contentionTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                10
            );
            sb.append(result.replace("# Thread Contention Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## I/O Hotspots (Focus)\n\n");
        IoHotspotsTool ioTool = new IoHotspotsTool(service);
        try {
            String result = ioTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                "all",
                10
            );
            sb.append(result.replace("# I/O Hotspots Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    private void appendLocksFocus(
        StringBuilder sb,
        String filePath,
        String startTimeStr,
        String endTimeStr
    ) {
        sb.append("## Thread Contention (Focus)\n\n");
        ThreadContentionTool contentionTool = new ThreadContentionTool(service);
        try {
            String result = contentionTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                10
            );
            sb.append(result.replace("# Thread Contention Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Lock Analysis (Focus)\n\n");
        LockAnalysisTool lockTool = new LockAnalysisTool(service);
        try {
            String result = lockTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                10
            );
            sb.append(result.replace("# Advanced Lock Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    private void appendAutoFocus(
        StringBuilder sb,
        String filePath,
        String startTimeStr,
        String endTimeStr,
        IItemCollection events
    ) {
        sb.append("## Top Hot Methods\n\n");
        HotMethodsTool hotMethodsTool = new HotMethodsTool(service);
        try {
            String result = hotMethodsTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                null,
                null,
                5
            );
            sb.append(result.replace("# Hot Methods & Call Paths\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top Lock Contentions\n\n");
        ThreadContentionTool contentionTool = new ThreadContentionTool(service);
        try {
            String result = contentionTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                5
            );
            sb.append(result.replace("# Thread Contention Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top I/O Hotspots\n\n");
        IoHotspotsTool ioTool = new IoHotspotsTool(service);
        try {
            String result = ioTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                "all",
                5
            );
            sb.append(result.replace("# I/O Hotspots Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## GC Summary\n\n");
        GcAnalysisTool gcTool = new GcAnalysisTool(service);
        try {
            String result = gcTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                "all"
            );
            sb.append(result.replace("# GC Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top Errors\n\n");
        ErrorAnalysisTool errorTool = new ErrorAnalysisTool(service);
        try {
            String result = errorTool.analyze(
                filePath,
                startTimeStr,
                endTimeStr,
                5
            );
            sb.append(
                result
                    .replace("# Error Analysis\n\n", "")
                    .replace("# Java Error Analysis\n\n", "")
            );
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    private String generateRecommendations(
        List<Finding> findings,
        String focus
    ) {
        StringBuilder sb = new StringBuilder();
        boolean hasCritical = findings
            .stream()
            .anyMatch(f -> "CRITICAL".equals(f.severity));
        boolean hasHigh = findings
            .stream()
            .anyMatch(f -> "HIGH".equals(f.severity));

        if (hasCritical) {
            sb.append("⚠️ **Critical issues detected.** Prioritize: ");
            findings
                .stream()
                .filter(f -> "CRITICAL".equals(f.severity))
                .forEach(f -> sb.append(f.message).append("; "));
            sb.append("\n\n");
        }

        sb.append("Based on focus `").append(focus).append("`:\n");
        switch (focus) {
            case "cpu" -> sb.append(
                "- Use `hot_methods` with `package_prefix` for application-level hot spots\n- Use `thread_cpu` to identify which threads consume the most CPU\n- Use `jit_compilation` to check for compilation storms\n"
            );
            case "memory" -> sb.append(
                "- Use `gc_detail` for per-phase pause breakdowns\n- Use `gc_recommendations` for JVM tuning advice\n- Use `heap_trends` for memory growth patterns\n- Use `predictive_leak_analysis` for mathematical leak confirmation\n"
            );
            case "latency" -> sb.append(
                "- Use `request_waterfall` with a specific thread name to trace the full request path\n- Use `io_hotspots` for endpoint-level I/O analysis\n- Use `thread_contention` for lock details\n"
            );
            case "locks" -> sb.append(
                "- Use `lock_analysis` for ThreadPark and Biased Lock Revocation details\n- Use `deadlock_detection` to check for deadlock cycles\n- Use `correlate` to see if I/O is performed under contended locks\n"
            );
            default -> sb.append(
                "- Use `correlate` for cross-dimensional analysis\n- Use `request_waterfall` for specific thread tracing\n- Use `stack_trace_search` to find specific methods across all event types\n"
            );
        }
        return sb.toString();
    }

    private record Finding(
        String severity,
        String severityIcon,
        String message
    ) {}
}
