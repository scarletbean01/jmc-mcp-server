package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramEntry;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import com.google.common.collect.MinMaxPriorityQueue;
import jakarta.enterprise.context.ApplicationScoped;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.JavaClass;

import java.util.ArrayList;
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

        Comparator<HeapDumpClassHistogramEntry> comparator = Comparator
                .comparingLong(HeapDumpClassHistogramEntry::totalRetainedSize)
                .thenComparingLong(HeapDumpClassHistogramEntry::totalShallowSize);

        MinMaxPriorityQueue<HeapDumpClassHistogramEntry> topEntries = MinMaxPriorityQueue
                .orderedBy(comparator.reversed())
                .maximumSize(topN)
                .create();

        for (JavaClass jc : allClasses) {
            long count = jc.getInstancesCount();
            long shallow = jc.getAllInstancesSize();
            
            totalInstances += count;
            totalBytes += shallow;

            if (count > 0) {
                long retained = heap.isRetainedSizeByClassComputed() ? jc.getRetainedSizeByClass() : 0;
                topEntries.add(new HeapDumpClassHistogramEntry(jc.getName(), count, shallow, retained));
            }
        }

        List<HeapDumpClassHistogramEntry> entries = new ArrayList<>(topEntries);
        entries.sort(comparator.reversed());

        return new HeapDumpClassHistogramResult(true, totalInstances, totalBytes, entries);
    }
}
