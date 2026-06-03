package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.netbeans.lib.profiler.heap.Heap;
import org.openjdk.jmc.common.item.IItemCollection;

import java.util.*;

@ApplicationScoped
public final class HeapDumpCrossAnalysisService {

    private static final long HIGH_SEVERITY_RETAINED_THRESHOLD = 1_048_576L; // 1 MB
    private static final long MEDIUM_SEVERITY_RETAINED_THRESHOLD = 524_288L; // 500 KB
    private static final long HIGH_SEVERITY_SAMPLE_THRESHOLD = 10L;
    private static final long MEDIUM_SEVERITY_SAMPLE_THRESHOLD = 5L;

    private final MemoryLeaksService memoryLeaksService;
    private final HeapDumpClassHistogramService classHistogramService;

    public HeapDumpCrossAnalysisService(MemoryLeaksService memoryLeaksService,
                                        HeapDumpClassHistogramService classHistogramService) {
        this.memoryLeaksService = memoryLeaksService;
        this.classHistogramService = classHistogramService;
    }

    public CrossAnalysisResult analyze(IItemCollection jfrEvents, Heap heap, int topN) {
        MemoryLeaksResult leaks = memoryLeaksService.analyze(jfrEvents, Integer.MAX_VALUE);
        HeapDumpClassHistogramResult histogram = classHistogramService.analyze(heap, Integer.MAX_VALUE);

        if (!leaks.hasData() && (!histogram.hasData() || histogram.entries().isEmpty())) {
            return new CrossAnalysisResult(false, List.of(),
                    new CrossAnalysisSummary(0, 0, 0, 0),
                    "No JFR OldObjectSample data and no heap dump histogram data available.");
        }

        Map<String, HeapDumpClassHistogramEntry> heapByClass = new HashMap<>();
        for (HeapDumpClassHistogramEntry entry : histogram.entries()) {
            heapByClass.put(entry.className(), entry);
        }

        Map<String, Long> sampleCountByClass = new HashMap<>();
        Map<String, List<String>> sitesByClass = new HashMap<>();
        for (LeakingClassEntry leak : leaks.leakingClasses()) {
            sampleCountByClass.put(leak.className(), leak.sampleCount());
        }
        for (LeakSiteEntry site : leaks.leakSites()) {
            String siteKey = site.siteKey();
            String className = extractClassNameFromSite(siteKey);
            sitesByClass.computeIfAbsent(className, k -> new ArrayList<>()).add(siteKey);
        }

        Set<String> allClasses = new HashSet<>();
        allClasses.addAll(sampleCountByClass.keySet());
        allClasses.addAll(heapByClass.keySet());

        List<UnifiedClassEntry> unified = new ArrayList<>();
        int high = 0, medium = 0, low = 0;

        for (String className : allClasses) {
            long sampleCount = sampleCountByClass.getOrDefault(className, 0L);
            HeapDumpClassHistogramEntry heapEntry = heapByClass.get(className);
            long instanceCount = heapEntry != null ? heapEntry.instanceCount() : 0L;
            long retainedSize = heapEntry != null ? heapEntry.totalRetainedSize() : 0L;
            List<String> sites = sitesByClass.getOrDefault(className, List.of());

            String severity = computeSeverity(sampleCount, retainedSize);
            switch (severity) {
                case "HIGH" -> high++;
                case "MEDIUM" -> medium++;
                case "LOW" -> low++;
            }

            unified.add(new UnifiedClassEntry(className, sampleCount, sites, instanceCount, retainedSize, severity));
        }

        unified.sort(Comparator.<UnifiedClassEntry>comparingLong(e -> {
                    return switch (e.severity()) {
                        case "HIGH" -> 3;
                        case "MEDIUM" -> 2;
                        case "LOW" -> 1;
                        default -> 0;
                    };
                }).reversed()
                .thenComparing(Comparator.comparingLong(UnifiedClassEntry::heapRetainedSize).reversed()));

        List<UnifiedClassEntry> limited = unified.size() > topN ? unified.subList(0, topN) : unified;
        CrossAnalysisSummary summary = new CrossAnalysisSummary(high, medium, low, unified.size());
        String recommendation = buildRecommendation(high, medium, low, leaks.hasData(), !histogram.entries().isEmpty());

        return new CrossAnalysisResult(true, limited, summary, recommendation);
    }

    private static String extractClassNameFromSite(String siteKey) {
        int idx = siteKey.indexOf(" allocated at:");
        return idx > 0 ? siteKey.substring(0, idx) : siteKey;
    }

    private static String computeSeverity(long sampleCount, long retainedSize) {
        if (sampleCount >= HIGH_SEVERITY_SAMPLE_THRESHOLD && retainedSize >= HIGH_SEVERITY_RETAINED_THRESHOLD) {
            return "HIGH";
        }
        if (sampleCount >= MEDIUM_SEVERITY_SAMPLE_THRESHOLD || retainedSize >= MEDIUM_SEVERITY_RETAINED_THRESHOLD) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private static String buildRecommendation(int high, int medium, int low,
                                               boolean hasJfrData, boolean hasHeapData) {
        if (!hasJfrData && !hasHeapData) {
            return "No data available from either JFR or heap dump.";
        }
        if (!hasJfrData) {
            return "JFR recording lacks OldObjectSample events. Enable object sampling in JFR settings for leak correlation.";
        }
        if (!hasHeapData) {
            return "Heap dump histogram is empty. The heap dump may be corrupt or from a very young JVM.";
        }
        if (high > 0) {
            return String.format(
                    "Critical memory pressure detected: %d high-severity classes combine high JFR sample counts with large retained heap sizes. " +
                    "Investigate these classes first — they are likely leak candidates. Consider reviewing allocation sites and lifecycle management.",
                    high);
        }
        if (medium > 0) {
            return String.format(
                    "%d medium-severity classes show moderate leakage signals. Review allocation patterns and object retention graphs for these classes.",
                    medium);
        }
        if (low > 0) {
            return "Only low-severity correlations found. Leakage may be diffuse or the sampling interval may be too coarse.";
        }
        return "No significant correlations between JFR samples and heap retention.";
    }
}
