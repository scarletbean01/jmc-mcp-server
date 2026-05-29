package io.github.deplague.jmcmcp.domain.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.*;
import static java.lang.String.format;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND;
import static org.openjdk.jmc.common.unit.UnitLookup.SECOND;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for quick analysis.
 * Computes shared metrics, classifies severity, detects dominant bottleneck,
 * and generates recommendations without any framework dependencies.
 */
@ApplicationScoped
public final class QuickAnalysisService {

    public record SharedMetrics(
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
            long jitCount) {
    }

    public record Finding(String severity, String severityIcon, String message) {
    }

    public record QuickAnalysisResult(
            SharedMetrics metrics,
            List<Finding> findings,
            String effectiveFocus,
            String recordingOverviewMarkdown,
            String findingsMarkdown,
            String recommendationsMarkdown) {
    }

    public QuickAnalysisResult analyze(IItemCollection events, String focus) {
        SharedMetrics metrics = computeSharedMetrics(events);
        List<Finding> findings = classifySeverity(events, metrics);
        String effectiveFocus = "auto".equals(focus) ? detectDominantBottleneck(metrics) : focus;
        String recordingOverview = buildRecordingOverview(metrics);
        String findingsMarkdown = buildFindingsMarkdown(findings);
        String recommendations = generateRecommendations(findings, effectiveFocus);
        return new QuickAnalysisResult(
                metrics, findings, effectiveFocus, recordingOverview, findingsMarkdown, recommendations);
    }

    private SharedMetrics computeSharedMetrics(IItemCollection events) {
        IItemCollection items2 = events.apply(type("jdk.CPULoad"));
        var cpuStats = batchStats(items2, "machineTotal");
        IItemCollection gcPauses = events.apply(type("jdk.GCPhasePause"));
        long gcPauseCount = count(gcPauses);
        String identifier = DURATION.getIdentifier();
        var gcPauseStats = batchStats(gcPauses, identifier, 99);
        long exceptionCount = count(events.apply(type("jdk.JavaExceptionThrow")));
        long errorCount = count(events.apply(type("jdk.JavaErrorThrow")));
        IItemCollection items = events.apply(type("jdk.JavaMonitorEnter"));
        IQuantity totalLockDuration = sumQuantity(items, DURATION.getIdentifier());
        IItemCollection items1 = events.apply(type("jdk.SocketRead"));
        var socketReadStats = batchStats(items1, DURATION.getIdentifier());
        IItemCollection heapSummary = events.apply(type("jdk.GCHeapSummary"));
        var heapStats = batchStats(heapSummary, "heapUsed");
        IQuantity maxHeapSize = maxQuantity(heapSummary, "heapSize");
        long jitCount = count(events.apply(type("jdk.Compilation")));
        long totalEvents = count(events);
        return new SharedMetrics(
                totalEvents, cpuStats.get("avg"), gcPauseCount, gcPauseStats.get("p99"),
                exceptionCount, errorCount, totalLockDuration,
                socketReadStats.get("avg"), heapStats.get("max"), maxHeapSize, jitCount);
    }

    private List<Finding> classifySeverity(IItemCollection events, SharedMetrics metrics) {
        List<Finding> findings = new ArrayList<>();

        if (metrics.avgCpu != null) {
            double cpuPct = metrics.avgCpu.doubleValue() * 100;
            if (cpuPct > 90) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                format("CPU utilization %.1f%% (machine total)", cpuPct)));
            } else if (cpuPct > 75) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                format("CPU utilization %.1f%%", cpuPct)));
            } else if (cpuPct > 60) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                format("CPU utilization %.1f%%", cpuPct)));
            }
        }

        if (metrics.totalLockDuration != null) {
            double lockSec = metrics.totalLockDuration.doubleValueIn(SECOND);
            if (lockSec > 30) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                format("Lock contention total %.1fs across all monitors", lockSec)));
            } else if (lockSec > 10) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                format("Lock contention total %.1fs", lockSec)));
            } else if (lockSec > 5) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                format("Lock contention total %.1fs", lockSec)));
            }
        }

        if (metrics.p99GcPause != null) {
            double p99Ms = metrics.p99GcPause.doubleValueIn(MILLISECOND);
            if (p99Ms > 500) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                format("GC P99 pause %.1fms", p99Ms)));
            } else if (p99Ms > 200) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                format("GC P99 pause %.1fms (%d pauses)", p99Ms, metrics.gcPauseCount)));
            } else if (p99Ms > 100) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                format("GC P99 pause %.1fms", p99Ms)));
            }
        }

        if (metrics.avgSocketRead != null) {
            double avgMs = metrics.avgSocketRead.doubleValueIn(MILLISECOND);
            if (avgMs > 500) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                format("Socket read latency %.1fms avg", avgMs)));
            } else if (avgMs > 100) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                format("Socket read latency %.1fms avg", avgMs)));
            } else if (avgMs > 50) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                format("Socket read latency %.1fms avg", avgMs)));
            }
        }

        if (metrics.maxHeapUsed != null && metrics.maxHeapSize != null
                && metrics.maxHeapSize.doubleValue() > 0) {
            double heapPct = (metrics.maxHeapUsed.doubleValue() / metrics.maxHeapSize.doubleValue()) * 100;
            if (heapPct > 90) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                format("Heap usage %.1f%% of max", heapPct)));
            } else if (heapPct > 75) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                format("Heap usage %.1f%% of max", heapPct)));
            } else if (heapPct > 60) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                format("Heap usage %.1f%% of max", heapPct)));
            }
        }

        if (metrics.errorCount > 0) {
            findings.add(
                    new Finding("HIGH", "🟠",
                            format("%d Java errors thrown", metrics.errorCount)));
        }

        if (metrics.jitCount > 500) {
            findings.add(
                    new Finding("MEDIUM", "🟡",
                            format("JIT compilation storm (%d compilations)", metrics.jitCount)));
        }

        return findings;
    }

    private String detectDominantBottleneck(SharedMetrics metrics) {
        double cpuScore = metrics.avgCpu != null ? metrics.avgCpu.doubleValue() : 0;
        double lockScore = metrics.totalLockDuration != null
                ? metrics.totalLockDuration.doubleValueIn(SECOND) : 0;
        double gcScore = metrics.p99GcPause != null
                ? metrics.p99GcPause.doubleValueIn(MILLISECOND) : 0;
        double ioScore = metrics.avgSocketRead != null
                ? metrics.avgSocketRead.doubleValueIn(MILLISECOND) : 0;

        if (cpuScore > 0.75) {
            return "cpu";
        }
        if (lockScore > 10) {
            return "locks";
        }
        if (gcScore > 200) {
            return "memory";
        }
        if (ioScore > 100) {
            return "latency";
        }
        return "cpu";
    }

    private String buildRecordingOverview(SharedMetrics metrics) {
        StringBuilder sb = new StringBuilder();
        sb.append("- **Total Events:** ").append(metrics.totalEvents).append("\n");
        if (metrics.avgCpu != null) {
            sb.append(format("- **Avg CPU Load:** %.1f%%\n", metrics.avgCpu.doubleValue() * 100));
        }
        sb.append("- **GC Pauses:** ").append(metrics.gcPauseCount).append("\n");
        sb.append("- **Exceptions:** ").append(metrics.exceptionCount).append("\n");
        sb.append("- **Errors:** ").append(metrics.errorCount).append("\n");
        return sb.toString();
    }

    private String buildFindingsMarkdown(List<Finding> findings) {
        StringBuilder sb = new StringBuilder();
        for (Finding f : findings) {
            sb.append(f.severityIcon()).append(" **")
                    .append(f.severity()).append(":** ")
                    .append(f.message()).append("\n");
        }
        if (findings.isEmpty()) {
            sb.append("🟢 **LOW:** No significant issues detected.\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String generateRecommendations(List<Finding> findings, String focus) {
        StringBuilder sb = new StringBuilder();
        boolean hasCritical = findings.stream().anyMatch(f -> "CRITICAL".equals(f.severity()));
        boolean hasHigh = findings.stream().anyMatch(f -> "HIGH".equals(f.severity()));

        if (hasCritical) {
            sb.append("⚠️ **Critical issues detected.** Prioritize: ");
            findings.stream()
                    .filter(f -> "CRITICAL".equals(f.severity()))
                    .forEach(f -> sb.append(f.message()).append("; "));
            sb.append("\n\n");
        }

        sb.append("Based on focus `").append(focus).append("`:\n");
        switch (focus) {
            case "cpu" -> sb.append(
                    "- Use `hot_methods` with `package_prefix` for application-level hot spots\n"
                            + "- Use `thread_cpu` to identify which threads consume the most CPU\n"
                            + "- Use `jit_compilation` to check for compilation storms\n");
            case "memory" -> sb.append(
                    "- Use `gc_detail` for per-phase pause breakdowns\n"
                            + "- Use `gc_recommendations` for JVM tuning advice\n"
                            + "- Use `heap_trends` for memory growth patterns\n"
                            + "- Use `predictive_leak_analysis` for mathematical leak confirmation\n");
            case "latency" -> sb.append(
                    "- Use `request_waterfall` with a specific thread name to trace the full request path\n"
                            + "- Use `io_hotspots` for endpoint-level I/O analysis\n"
                            + "- Use `thread_contention` for lock details\n");
            case "locks" -> sb.append(
                    "- Use `lock_analysis` for ThreadPark and Biased Lock Revocation details\n"
                            + "- Use `deadlock_detection` to check for deadlock cycles\n"
                            + "- Use `correlate` to see if I/O is performed under contended locks\n");
            default -> sb.append(
                    "- Use `correlate` for cross-dimensional analysis\n"
                            + "- Use `request_waterfall` for specific thread tracing\n"
                            + "- Use `stack_trace_search` to find specific methods across all event types\n");
        }
        return sb.toString();
    }
}
