package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.List;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for quick analysis.
 * Computes shared metrics, classifies severity, detects dominant bottleneck,
 * and generates recommendations without any framework dependencies.
 */
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
        IQuantity avgCpu = JfrItemUtils.avgQuantity(
                events.apply(ItemFilters.type("jdk.CPULoad")), "machineTotal");
        IItemCollection gcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        long gcPauseCount = JfrItemUtils.count(gcPauses);
        IQuantity p99GcPause = JfrItemUtils.percentileQuantity(
                gcPauses, JfrAttributes.DURATION.getIdentifier(), 99);
        long exceptionCount = JfrItemUtils.count(
                events.apply(ItemFilters.type("jdk.JavaExceptionThrow")));
        long errorCount = JfrItemUtils.count(
                events.apply(ItemFilters.type("jdk.JavaErrorThrow")));
        IQuantity totalLockDuration = JfrItemUtils.sumQuantity(
                events.apply(ItemFilters.type("jdk.JavaMonitorEnter")),
                JfrAttributes.DURATION.getIdentifier());
        IQuantity avgSocketRead = JfrItemUtils.avgQuantity(
                events.apply(ItemFilters.type("jdk.SocketRead")),
                JfrAttributes.DURATION.getIdentifier());
        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IQuantity maxHeapUsed = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
        IQuantity maxHeapSize = JfrItemUtils.maxQuantity(heapSummary, "heapSize");
        long jitCount = JfrItemUtils.count(
                events.apply(ItemFilters.type("jdk.Compilation")));
        long totalEvents = JfrItemUtils.count(events);
        return new SharedMetrics(
                totalEvents, avgCpu, gcPauseCount, p99GcPause,
                exceptionCount, errorCount, totalLockDuration,
                avgSocketRead, maxHeapUsed, maxHeapSize, jitCount);
    }

    private List<Finding> classifySeverity(IItemCollection events, SharedMetrics metrics) {
        List<Finding> findings = new ArrayList<>();

        if (metrics.avgCpu != null) {
            double cpuPct = metrics.avgCpu.doubleValue() * 100;
            if (cpuPct > 90) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                String.format("CPU utilization %.1f%% (machine total)", cpuPct)));
            } else if (cpuPct > 75) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                String.format("CPU utilization %.1f%%", cpuPct)));
            } else if (cpuPct > 60) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                String.format("CPU utilization %.1f%%", cpuPct)));
            }
        }

        if (metrics.totalLockDuration != null) {
            double lockSec = metrics.totalLockDuration.doubleValueIn(UnitLookup.SECOND);
            if (lockSec > 30) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                String.format("Lock contention total %.1fs across all monitors", lockSec)));
            } else if (lockSec > 10) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                String.format("Lock contention total %.1fs", lockSec)));
            } else if (lockSec > 5) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                String.format("Lock contention total %.1fs", lockSec)));
            }
        }

        if (metrics.p99GcPause != null) {
            double p99Ms = metrics.p99GcPause.doubleValueIn(UnitLookup.MILLISECOND);
            if (p99Ms > 500) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                String.format("GC P99 pause %.1fms", p99Ms)));
            } else if (p99Ms > 200) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                String.format("GC P99 pause %.1fms (%d pauses)", p99Ms, metrics.gcPauseCount)));
            } else if (p99Ms > 100) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                String.format("GC P99 pause %.1fms", p99Ms)));
            }
        }

        if (metrics.avgSocketRead != null) {
            double avgMs = metrics.avgSocketRead.doubleValueIn(UnitLookup.MILLISECOND);
            if (avgMs > 500) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                String.format("Socket read latency %.1fms avg", avgMs)));
            } else if (avgMs > 100) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                String.format("Socket read latency %.1fms avg", avgMs)));
            } else if (avgMs > 50) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                String.format("Socket read latency %.1fms avg", avgMs)));
            }
        }

        if (metrics.maxHeapUsed != null && metrics.maxHeapSize != null
                && metrics.maxHeapSize.doubleValue() > 0) {
            double heapPct = (metrics.maxHeapUsed.doubleValue() / metrics.maxHeapSize.doubleValue()) * 100;
            if (heapPct > 90) {
                findings.add(
                        new Finding("CRITICAL", "🔴",
                                String.format("Heap usage %.1f%% of max", heapPct)));
            } else if (heapPct > 75) {
                findings.add(
                        new Finding("HIGH", "🟠",
                                String.format("Heap usage %.1f%% of max", heapPct)));
            } else if (heapPct > 60) {
                findings.add(
                        new Finding("MEDIUM", "🟡",
                                String.format("Heap usage %.1f%% of max", heapPct)));
            }
        }

        if (metrics.errorCount > 0) {
            findings.add(
                    new Finding("HIGH", "🟠",
                            String.format("%d Java errors thrown", metrics.errorCount)));
        }

        if (metrics.jitCount > 500) {
            findings.add(
                    new Finding("MEDIUM", "🟡",
                            String.format("JIT compilation storm (%d compilations)", metrics.jitCount)));
        }

        return findings;
    }

    private String detectDominantBottleneck(SharedMetrics metrics) {
        double cpuScore = metrics.avgCpu != null ? metrics.avgCpu.doubleValue() : 0;
        double lockScore = metrics.totalLockDuration != null
                ? metrics.totalLockDuration.doubleValueIn(UnitLookup.SECOND) : 0;
        double gcScore = metrics.p99GcPause != null
                ? metrics.p99GcPause.doubleValueIn(UnitLookup.MILLISECOND) : 0;
        double ioScore = metrics.avgSocketRead != null
                ? metrics.avgSocketRead.doubleValueIn(UnitLookup.MILLISECOND) : 0;

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
            sb.append(String.format("- **Avg CPU Load:** %.1f%%\n", metrics.avgCpu.doubleValue() * 100));
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
