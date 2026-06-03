package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.HeapDumpProvider;
import io.github.deplague.jmcmcp.domain.model.DominatorTreeResult;
import io.github.deplague.jmcmcp.domain.model.HeapDumpClassHistogramResult;
import io.github.deplague.jmcmcp.domain.model.HeapObjectEntry;
import io.github.deplague.jmcmcp.domain.model.ReferencePathResult;
import io.github.deplague.jmcmcp.domain.service.HeapDumpClassHistogramService;
import io.github.deplague.jmcmcp.domain.service.HeapDumpDominatorTreeService;
import io.github.deplague.jmcmcp.domain.service.HeapDumpReferenceGraphService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.netbeans.lib.profiler.heap.Heap;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class HeapDumpAnalysisApplicationService {

    private final HeapDumpProvider heapDumpProvider;
    private final HeapDumpClassHistogramService classHistogramService;
    private final HeapDumpDominatorTreeService dominatorTreeService;
    private final HeapDumpReferenceGraphService referenceGraphService;

    public HeapDumpClassHistogramResult classHistogram(String filePath, int topN) throws IOException {
        Heap heap = heapDumpProvider.loadSnapshot(filePath);
        return classHistogramService.analyze(heap, topN);
    }

    public DominatorTreeResult dominatorTree(String filePath, int topN) throws IOException {
        Heap heap = heapDumpProvider.loadSnapshot(filePath);
        return dominatorTreeService.buildTopDominators(heap, topN);
    }

    public List<HeapObjectEntry> expandDominatorNode(String filePath, long objectId, int topN) throws IOException {
        Heap heap = heapDumpProvider.loadSnapshot(filePath);
        return dominatorTreeService.expandNode(heap, objectId, topN);
    }

    public ReferencePathResult referenceGraph(String filePath, long objectId, int maxPaths) throws IOException {
        Heap heap = heapDumpProvider.loadSnapshot(filePath);
        return referenceGraphService.findPathsToGcRoots(heap, objectId, maxPaths);
    }
}
