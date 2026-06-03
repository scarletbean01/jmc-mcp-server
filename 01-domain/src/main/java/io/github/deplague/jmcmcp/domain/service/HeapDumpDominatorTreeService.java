package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DominatorTreeResult;
import io.github.deplague.jmcmcp.domain.model.HeapObjectEntry;
import jakarta.enterprise.context.ApplicationScoped;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.HeapSummary;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.ObjectFieldValue;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public final class HeapDumpDominatorTreeService {

    @SuppressWarnings("unchecked")
    public DominatorTreeResult buildTopDominators(Heap heap, int topN) {
        List<Instance> biggest = heap.getBiggestObjectsByRetainedSize(topN);
        if (biggest == null || biggest.isEmpty()) {
            HeapSummary summary = heap.getSummary();
            long totalBytes = summary != null ? summary.getTotalLiveBytes() : 0;
            long totalInstances = summary != null ? summary.getTotalLiveInstances() : 0;
            return new DominatorTreeResult(false, totalInstances, heap.getAllClasses().size(), totalBytes, List.of());
        }

        List<HeapObjectEntry> topDominators = biggest.stream()
                .map(i -> new HeapObjectEntry(
                        i.getInstanceId(),
                        i.getJavaClass().getName(),
                        i.getSize(),
                        i.getRetainedSize(),
                        1,
                        List.of(),
                        hasObjectFieldValues(i)
                ))
                .toList();

        HeapSummary summary = heap.getSummary();
        long totalBytes = summary != null ? summary.getTotalLiveBytes() : 0;
        long totalInstances = summary != null ? summary.getTotalLiveInstances() : 0;

        return new DominatorTreeResult(true, totalInstances, heap.getAllClasses().size(), totalBytes, topDominators);
    }

    @SuppressWarnings("unchecked")
    public List<HeapObjectEntry> expandNode(Heap heap, long objectId, int topN) {
        Instance instance = heap.getInstanceByID(objectId);
        if (instance == null) {
            return List.of();
        }

        List<HeapObjectEntry> children = new java.util.ArrayList<>();
        for (Object fvObj : instance.getFieldValues()) {
            if (fvObj instanceof ObjectFieldValue ofv) {
                Instance i = ofv.getInstance();
                if (i != null) {
                    children.add(new HeapObjectEntry(
                            i.getInstanceId(),
                            i.getJavaClass().getName(),
                            i.getSize(),
                            i.getRetainedSize(),
                            1,
                            List.of(),
                            hasObjectFieldValues(i)
                    ));
                }
            }
        }
        children.sort(Comparator.comparingLong(HeapObjectEntry::retainedSize).reversed());
        return children.size() > topN ? children.subList(0, topN) : children;
    }

    private boolean hasObjectFieldValues(Instance instance) {
        for (Object fvObj : instance.getFieldValues()) {
            if (fvObj instanceof ObjectFieldValue ofv && ofv.getInstance() != null) {
                return true;
            }
        }
        return false;
    }
}
