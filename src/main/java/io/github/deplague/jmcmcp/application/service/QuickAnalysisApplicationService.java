package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ErrorAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.GcAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.HeapTrendsResult;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.github.deplague.jmcmcp.domain.model.IoHotspotsResult;
import io.github.deplague.jmcmcp.domain.model.LockAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.SystemHealthResult;
import io.github.deplague.jmcmcp.domain.model.ThreadContentionResult;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.github.deplague.jmcmcp.domain.service.QuickAnalysisService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates the quick analysis use case.
 * Loads the recording, delegates pure analysis to the domain service,
 * calls sub-tool application services for their markdown, and combines
 * everything into a single markdown string.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class QuickAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final QuickAnalysisService quickAnalysisService;
    private final SystemHealthApplicationService systemHealthAppService;
    private final ThreadCpuApplicationService threadCpuAppService;
    private final HotMethodsApplicationService hotMethodsAppService;
    private final GcAnalysisApplicationService gcAnalysisAppService;
    private final HeapTrendsApplicationService heapTrendsAppService;
    private final ThreadContentionApplicationService threadContentionAppService;
    private final IoHotspotsApplicationService ioHotspotsAppService;
    private final LockAnalysisApplicationService lockAnalysisAppService;
    private final ErrorAnalysisApplicationService errorAnalysisAppService;

    public String analyze(String filePath, String startTimeStr, String endTimeStr,
                          String focus) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(
                allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# Quick Analysis Dashboard\n\n");

        QuickAnalysisService.QuickAnalysisResult result = quickAnalysisService.analyze(
                events, focus);

        sb.append("## Recording Overview\n\n");
        sb.append(result.recordingOverviewMarkdown());

        sb.append("\n---\n\n## System Health\n\n");
        try {
            SystemHealthResult healthResult = systemHealthAppService.analyze(
                    filePath, startTimeStr, endTimeStr);
            sb.append(formatSystemHealth(healthResult));
        } catch (Exception e) {
            sb.append("Failed to gather system health: ")
                    .append(e.getMessage())
                    .append("\n");
        }

        sb.append("\n---\n\n## Severity-Classified Findings\n\n");
        sb.append(result.findingsMarkdown());

        sb.append("---\n\n");
        switch (result.effectiveFocus()) {
            case "cpu" -> appendCpuFocus(sb, filePath, startTimeStr, endTimeStr);
            case "memory" -> appendMemoryFocus(sb, filePath, startTimeStr, endTimeStr);
            case "latency" -> appendLatencyFocus(sb, filePath, startTimeStr, endTimeStr);
            case "locks" -> appendLocksFocus(sb, filePath, startTimeStr, endTimeStr);
            default -> appendAutoFocus(sb, filePath, startTimeStr, endTimeStr);
        }

        sb.append("\n---\n\n## Recommended Next Steps\n\n");
        sb.append(result.recommendationsMarkdown());

        return sb.toString();
    }

    private void appendCpuFocus(
            StringBuilder sb,
            String filePath,
            String startTimeStr,
            String endTimeStr
    ) {
        sb.append("## CPU Analysis (Focus)\n\n");
        try {
            ThreadCpuResult result = threadCpuAppService.analyze(
                    filePath, startTimeStr, endTimeStr, null, 10);
            sb.append(formatThreadCpu(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top Hot Methods\n\n");
        try {
            HotMethodsResult result = hotMethodsAppService.analyze(
                    filePath, startTimeStr, endTimeStr, null, null, 10);
            sb.append(formatHotMethods(result));
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
        try {
            GcAnalysisResult result = gcAnalysisAppService.analyze(
                    filePath, startTimeStr, endTimeStr, "all");
            sb.append(formatGcAnalysis(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Heap Trends\n\n");
        try {
            HeapTrendsResult result = heapTrendsAppService.analyze(
                    filePath, startTimeStr, endTimeStr, "1m");
            sb.append(formatHeapTrends(result));
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
        try {
            ThreadContentionResult result = threadContentionAppService.analyze(
                    filePath, startTimeStr, endTimeStr, 10);
            sb.append(formatThreadContention(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## I/O Hotspots (Focus)\n\n");
        try {
            IoHotspotsResult result = ioHotspotsAppService.analyze(
                    filePath, startTimeStr, endTimeStr, "all", 10);
            sb.append(formatIoHotspots(result));
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
        try {
            ThreadContentionResult result = threadContentionAppService.analyze(
                    filePath, startTimeStr, endTimeStr, 10);
            sb.append(formatThreadContention(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Lock Analysis (Focus)\n\n");
        try {
            LockAnalysisResult result = lockAnalysisAppService.analyze(
                    filePath, startTimeStr, endTimeStr, 10);
            sb.append(formatLockAnalysis(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    private void appendAutoFocus(
            StringBuilder sb,
            String filePath,
            String startTimeStr,
            String endTimeStr
    ) {
        sb.append("## Top Hot Methods\n\n");
        try {
            HotMethodsResult result = hotMethodsAppService.analyze(
                    filePath, startTimeStr, endTimeStr, null, null, 5);
            sb.append(formatHotMethods(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top Lock Contentions\n\n");
        try {
            ThreadContentionResult result = threadContentionAppService.analyze(
                    filePath, startTimeStr, endTimeStr, 5);
            sb.append(formatThreadContention(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top I/O Hotspots\n\n");
        try {
            IoHotspotsResult result = ioHotspotsAppService.analyze(
                    filePath, startTimeStr, endTimeStr, "all", 5);
            sb.append(formatIoHotspots(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## GC Summary\n\n");
        try {
            GcAnalysisResult result = gcAnalysisAppService.analyze(
                    filePath, startTimeStr, endTimeStr, "all");
            sb.append(formatGcAnalysis(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n\n## Top Errors\n\n");
        try {
            ErrorAnalysisResult result = errorAnalysisAppService.analyze(
                    filePath, startTimeStr, endTimeStr, 5);
            sb.append(formatErrorAnalysis(result));
        } catch (Exception e) {
            sb.append("Failed: ").append(e.getMessage()).append("\n");
        }
    }

    // ------------------------------------------------------------------
    // Sub-tool formatters (simplified, header-stripped for embedding)
    // ------------------------------------------------------------------

    private String formatSystemHealth(SystemHealthResult result) {
        StringBuilder sb = new StringBuilder();
        result.cpuLoad().ifPresent(cpu -> {
            sb.append("## CPU Load\n");
            if (cpu.avgMachineTotal() != null) {
                sb.append(String.format("- **Avg Machine Total:** %s%n", cpu.avgMachineTotal()));
            }
            if (cpu.maxMachineTotal() != null) {
                sb.append(String.format("- **Max Machine Total:** %s%n", cpu.maxMachineTotal()));
            }
            if (cpu.avgJvmUser() != null) {
                sb.append(String.format("- **Avg JVM User:** %s%n", cpu.avgJvmUser()));
            }
            if (cpu.avgJvmSystem() != null) {
                sb.append(String.format("- **Avg JVM System:** %s%n", cpu.avgJvmSystem()));
            }
            sb.append("\n");
        });
        result.physicalMemory().ifPresent(mem -> {
            sb.append("## Physical Memory\n");
            sb.append(String.format("- **Total Physical Memory:** %s%n", mem.totalSize()));
            sb.append(String.format("- **Min Used:** %s%n", mem.minUsed()));
            sb.append(String.format("- **Max Used:** %s%n", mem.maxUsed()));
            sb.append(String.format("- **Avg Used:** %s%n", mem.avgUsed()));
            sb.append("\n");
        });
        result.cpuInfo().ifPresent(info -> {
            sb.append("## CPU Information\n");
            if (info.cpu() != null) {
                sb.append(String.format("- **CPU:** %s%n", info.cpu()));
            }
            if (info.cores() > 0) {
                sb.append(String.format("- **Cores:** %d%n", info.cores()));
            }
            if (info.sockets() > 0) {
                sb.append(String.format("- **Sockets:** %d%n", info.sockets()));
            }
            sb.append("\n");
        });
        result.containerConfig().ifPresent(config -> {
            sb.append("## Container Configuration\n");
            sb.append("- **CPU Shares:** ").append(config.cpuShares()).append("\n");
            sb.append("- **CPU Period:** ").append(config.cpuPeriod()).append("\n");
            sb.append("- **CPU Quota:** ").append(config.cpuQuota()).append("\n");
            sb.append("- **Memory Limit:** ").append(config.memoryLimit()).append("\n");
            sb.append("- **Swap Limit:** ").append(config.swapLimit()).append("\n");
            sb.append("\n");
        });
        if (!result.hasData()) {
            sb.append("No system health events found in this recording.\n");
        }
        return sb.toString();
    }

    private String formatThreadCpu(ThreadCpuResult result) {
        if (result.totalSamples() == 0) {
            return "No execution samples found in the recording.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Total samples: ").append(result.totalSamples()).append("\n\n");
        sb.append("## Per-Thread CPU Summary\n\n");
        sb.append("| Thread Name | Samples | CPU % | Primary States |\n");
        sb.append("|-------------|---------|-------|----------------|\n");
        for (var thread : result.threads()) {
            String states = thread.stateCounts().entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(2)
                    .map(e -> String.format("%s (%d)", e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format("| %s | %d | %.2f%% | %s |%n",
                    thread.threadName(), thread.samples(), thread.cpuPercent(), states));
        }
        sb.append("\n## Top Methods per Thread\n\n");
        for (var thread : result.threads()) {
            sb.append("### ").append(thread.threadName()).append("\n\n");
            sb.append("| Samples | Method (Top Frame) |\n");
            sb.append("|---------|--------------------|\n");
            for (var method : thread.topMethods()) {
                sb.append(String.format("| %d | `%s` |%n",
                        method.samples(), method.method()));
            }
            sb.append("\n");
        }
        sb.append("## Thread State Distribution\n\n");
        sb.append("| State | Samples | Percentage |\n");
        sb.append("|-------|---------|------------|\n");
        for (var state : result.stateDistribution()) {
            sb.append(String.format("| %s | %d | %.2f%% |%n",
                    state.state(), state.samples(), state.percent()));
        }
        return sb.toString();
    }

    private String formatHotMethods(HotMethodsResult result) {
        if (!result.hasResults()) {
            return "No execution samples found in the recording.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("| Samples | Stack Trace (top 5 frames) |\n");
        sb.append("|---------|----------------------------|\n");
        for (var entry : result.entries()) {
            sb.append("| ").append(entry.sampleCount()).append(" | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`"))
                    .append("` |\n");
        }
        String topMethod = result.topMethod() != null ? result.topMethod() : "unknown";
        sb.append("\n<agent_hint>Top hot method is `")
                .append(topMethod)
                .append("`. Consider `thread_cpu` to see which threads consume the most CPU, "
                        + "`stack_trace_search` with `class_pattern` to find all events involving this class, "
                        + "or `correlate` to see if this method is associated with lock contention or I/O."
                        + "</agent_hint>\n");
        return sb.toString();
    }

    private String formatGcAnalysis(GcAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        result.pauseTimes().ifPresent(pt -> {
            sb.append("## Pause Times\n");
            sb.append(String.format("- **Average Pause:** %s%n", pt.avgPause()));
            sb.append(String.format("- **Maximum Pause:** %s%n", pt.maxPause()));
            sb.append(String.format("- **Total Pause Time:** %s%n", pt.totalPause()));
            sb.append("\n");
        });
        result.frequencies().ifPresent(freq -> {
            sb.append("## GC Frequencies\n");
            if (freq.youngGCs() > 0) {
                sb.append(String.format("- **Young GCs:** %d%n", freq.youngGCs()));
            }
            if (freq.oldGCs() > 0) {
                sb.append(String.format("- **Old GCs:** %d%n", freq.oldGCs()));
            }
            sb.append("\n");
        });
        result.heapSummary().ifPresent(heap -> {
            sb.append("## Heap Summary\n");
            sb.append(String.format("- **Max Heap Used:** %s%n", heap.maxHeapUsed()));
            sb.append(String.format("- **Min Heap Used:** %s%n", heap.minHeapUsed()));
            sb.append(String.format("- **Avg Heap Used:** %s%n", heap.avgHeapUsed()));
            sb.append("\n");
        });
        if (!result.hasData()) {
            sb.append("No garbage collection events found in this recording range.\n");
        } else {
            sb.append("\n<agent_hint>GC analysis complete. Consider `gc_detail` for per-phase pause "
                    + "breakdowns, `gc_recommendations` for JVM tuning advice, or `heap_trends` "
                    + "for memory growth patterns.</agent_hint>\n");
        }
        return sb.toString();
    }

    private String formatHeapTrends(HeapTrendsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Heap Usage Trend\n\n");
        sb.append("| Time | Min | Avg | Max |\n");
        sb.append("|------|-----|-----|-----|\n");
        for (var b : result.heapBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s |%n",
                    SchemaUtil.formatTime(b.bucketStartMs()),
                    b.minBytes() != null ? SchemaUtil.formatBytes(b.minBytes()) : "N/A",
                    b.avgBytes() != null ? SchemaUtil.formatBytes(b.avgBytes()) : "N/A",
                    b.maxBytes() != null ? SchemaUtil.formatBytes(b.maxBytes()) : "N/A"));
        }
        sb.append("\n");

        sb.append("## Metaspace Usage Trend\n\n");
        sb.append("| Time | Used Min | Used Avg | Used Max | Committed Min | Committed Avg | Committed Max |\n");
        sb.append("|------|----------|----------|----------|---------------|---------------|---------------|\n");
        for (var b : result.metaspaceBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s | %s | %s | %s |%n",
                    SchemaUtil.formatTime(b.bucketStartMs()),
                    b.minUsedBytes() != null ? SchemaUtil.formatBytes(b.minUsedBytes()) : "N/A",
                    b.avgUsedBytes() != null ? SchemaUtil.formatBytes(b.avgUsedBytes()) : "N/A",
                    b.maxUsedBytes() != null ? SchemaUtil.formatBytes(b.maxUsedBytes()) : "N/A",
                    b.minCommittedBytes() != null ? SchemaUtil.formatBytes(b.minCommittedBytes()) : "N/A",
                    b.avgCommittedBytes() != null ? SchemaUtil.formatBytes(b.avgCommittedBytes()) : "N/A",
                    b.maxCommittedBytes() != null ? SchemaUtil.formatBytes(b.maxCommittedBytes()) : "N/A"));
        }
        sb.append("\n");

        sb.append("## Thread Count Trend\n\n");
        sb.append("| Time | Min | Avg | Max |\n");
        sb.append("|------|-----|-----|-----|\n");
        for (var b : result.threadBuckets()) {
            sb.append(String.format("| %s | %s | %s | %s |%n",
                    SchemaUtil.formatTime(b.bucketStartMs()),
                    b.minCount() != null ? b.minCount() : "N/A",
                    b.avgCount() != null ? b.avgCount() : "N/A",
                    b.maxCount() != null ? b.maxCount() : "N/A"));
        }
        sb.append("\n");
        return sb.toString();
    }

    private String formatThreadContention(ThreadContentionResult result) {
        if (!result.hasData()) {
            return "No monitor contention or wait events found.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("| Total Duration | Monitor Class | Contention Site (top 5 frames) |\n");
        sb.append("|----------------|---------------|--------------------------------|\n");
        for (var entry : result.topContentions()) {
            sb.append("| ").append(entry.totalDuration()).append(" | ");
            sb.append("`").append(entry.monitorClass()).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`"))
                    .append("` |\n");
        }
        sb.append("\n<agent_hint>Lock `").append(result.topLock()).append("` has ")
                .append(result.topDuration())
                .append(" total contention. Consider `correlate` to see if I/O is performed under this lock, "
                        + "or `request_waterfall` with the contending thread name to trace the full request path."
                        + "</agent_hint>\n");
        return sb.toString();
    }

    private String formatIoHotspots(IoHotspotsResult result) {
        StringBuilder sb = new StringBuilder();
        if (!result.fileEndpoints().isEmpty()) {
            sb.append("## File I/O Hotspots\n\n");
            sb.append("| Duration (Max) | Count | Bytes | Target | Call Site (top 5 frames) |\n");
            sb.append("|----------------|-------|-------|--------|--------------------------|\n");
            for (var entry : result.fileEndpoints()) {
                sb.append("| ").append(entry.maxDuration()).append(" | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.totalBytes()).append(" | ");
                sb.append("`").append(entry.target()).append("` | ");
                sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`"))
                        .append("` |\n");
            }
            sb.append("\n");
        } else if (result.hasFileData()) {
            sb.append("## File I/O Hotspots\n\nNo file I/O events found.\n\n");
        }

        if (!result.socketEndpoints().isEmpty()) {
            sb.append("## Socket I/O Hotspots\n\n");
            sb.append("| Duration (Max) | Count | Bytes | Target | Call Site (top 5 frames) |\n");
            sb.append("|----------------|-------|-------|--------|--------------------------|\n");
            for (var entry : result.socketEndpoints()) {
                sb.append("| ").append(entry.maxDuration()).append(" | ");
                sb.append(entry.count()).append(" | ");
                sb.append(entry.totalBytes()).append(" | ");
                sb.append("`").append(entry.target()).append("` | ");
                sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`"))
                        .append("` |\n");
            }
            sb.append("\n");
        } else if (result.hasSocketData()) {
            sb.append("## Socket I/O Hotspots\n\nNo socket I/O events found.\n\n");
        }

        sb.append("## I/O Latency Percentiles\n\n");
        sb.append("| Operation | P50 | P95 | P99 | Max |\n");
        sb.append("|-----------|-----|-----|-----|-----|\n");
        for (var p : result.percentiles()) {
            sb.append("| ").append(p.operation()).append(" | ");
            sb.append(p.p50()).append(" | ");
            sb.append(p.p95()).append(" | ");
            sb.append(p.p99()).append(" | ");
            sb.append(p.max()).append(" |\n");
        }
        sb.append("\n");
        sb.append("<agent_hint>Top I/O hotspot identified. Consider `correlate` to see which hot methods "
                + "and locks are associated with this endpoint, or `network_analysis` for connection-level details."
                + "</agent_hint>\n");
        return sb.toString();
    }

    private String formatLockAnalysis(LockAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        result.threadParkSummary().ifPresent(park -> {
            sb.append("## Thread Park Summary (LockSupport.park)\n");
            sb.append("- **Total Park Events:** ").append(park.count()).append("\n");
            sb.append("- **Avg Park Duration:** ").append(park.avgDuration()).append("\n");
            sb.append("- **Max Park Duration:** ").append(park.maxDuration()).append("\n\n");
            if (!park.topSites().isEmpty()) {
                sb.append("### Top Park Sites\n");
                sb.append("| Stack Trace | Count | Avg Duration | Max Duration |\n");
                sb.append("|---|---|---|---|\n");
                for (var site : park.topSites()) {
                    sb.append(String.format("| `%s` | %d | %s | %s |%n",
                            site.stackTrace().replace("\n", "`<br>`"),
                            site.count(), site.avgDuration(), site.maxDuration()));
                }
                sb.append("\n");
            }
        });
        if (result.threadParkSummary().isEmpty()) {
            sb.append("No Thread Park events found.\n\n");
        }

        result.biasedLockSummary().ifPresent(biased -> {
            sb.append("## Biased Lock Revocations\n");
            sb.append("- **Single Revocations:** ").append(biased.singleRevocations()).append("\n");
            sb.append("- **Class/Bulk Revocations:** ").append(biased.classRevocations()).append("\n");
            sb.append("- **Self Revocations:** ").append(biased.selfRevocations()).append("\n\n");
            if (!biased.topClasses().isEmpty()) {
                sb.append("### Revoked Lock Classes\n");
                sb.append("| Lock Class | Revocation Count |\n");
                sb.append("|---|---|\n");
                for (var entry : biased.topClasses()) {
                    sb.append(String.format("| `%s` | %d |%n",
                            entry.lockClass(), entry.count()));
                }
                sb.append("\n");
            }
        });
        if (result.biasedLockSummary().isEmpty()) {
            sb.append("No Biased Lock Revocation events found.\n");
        }

        sb.append("\n<agent_hint>Lock contention detected. Consider `correlate` to see if I/O is performed under "
                + "contended locks (a critical anti-pattern), or `deadlock_detection` to check for deadlock cycles."
                + "</agent_hint>\n");
        return sb.toString();
    }

    private String formatErrorAnalysis(ErrorAnalysisResult result) {
        if (!result.hasData()) {
            return "No Java error throw events found.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("## Error Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append("| Total Errors | ").append(result.totalErrors()).append(" |\n");
        sb.append("| Total Exceptions | ").append(result.totalExceptions()).append(" |\n");
        if (result.totalExceptions() > 0) {
            sb.append(String.format("| Error-to-Exception Ratio | %.2f%% |%n",
                    (result.totalErrors() * 100.0) / result.totalExceptions()));
        }
        sb.append("\n");
        sb.append("## Top Errors by Class\n\n");
        sb.append("| Count | Error Class | Message | Throw Site (top 5 frames) | Severity |\n");
        sb.append("|------|-------------|---------|---------------------------|----------|\n");
        for (var entry : result.topErrors()) {
            sb.append("| ").append(entry.count()).append(" | ");
            sb.append("`").append(entry.className()).append("` | ");
            sb.append("`").append(entry.message().replace("\n", " ")).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` | ");
            sb.append(entry.severity()).append(" |\n");
        }
        return sb.toString();
    }
}
