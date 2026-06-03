package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramEntry;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.JavaClass;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public final class HeapDumpClassHistogramService {

    public HeapDumpClassHistogramResult analyze(Heap heap, int topN) {
        List<JavaClass> allClasses = heap.getAllClasses();
        if (allClasses == null || allClasses.isEmpty()) {
            return new HeapDumpClassHistogramResult(false, 0, 0, List.of());
        }

        long totalInstances = 0;
        long totalBytes = 0;

        List<HeapDumpClassHistogramEntry> entries = allClasses.stream()
                .filter(jc -> jc.getInstancesCount() > 0)
                .map(jc -> {
                    long count = jc.getInstancesCount();
                    long shallow = jc.getAllInstancesSize();
                    long retained = heap.isRetainedSizeByClassComputed() ? jc.getRetainedSizeByClass() : 0;
                    return new HeapDumpClassHistogramEntry(jc.getName(), count, shallow, retained);
                })
                .sorted(Comparator.comparingLong(HeapDumpClassHistogramEntry::totalRetainedSize).reversed()
                        .thenComparing(Comparator.comparingLong(HeapDumpClassHistogramEntry::totalShallowSize).reversed()))
                .limit(topN)
                .toList();

        for (JavaClass jc : allClasses) {
            totalInstances += jc.getInstancesCount();
            totalBytes += jc.getAllInstancesSize();
        }

        return new HeapDumpClassHistogramResult(true, totalInstances, totalBytes, entries);
    }
}
