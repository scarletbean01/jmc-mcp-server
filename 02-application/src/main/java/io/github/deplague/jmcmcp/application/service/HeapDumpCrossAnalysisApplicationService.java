package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.HeapDumpProvider;
import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.CrossAnalysisResult;
import io.github.deplague.jmcmcp.domain.service.HeapDumpCrossAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.netbeans.lib.profiler.heap.Heap;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class HeapDumpCrossAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final HeapDumpProvider heapDumpProvider;
    private final HeapDumpCrossAnalysisService crossAnalysisService;

    public CrossAnalysisResult analyze(String recordingPath, String heapDumpPath,
                                        String startTime, String endTime, int topN) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(recordingPath);
        if (startTime != null || endTime != null) {
            events = jfrProvider.filterByTimeRange(events, startTime, endTime);
        }
        Heap heap = heapDumpProvider.loadSnapshot(heapDumpPath);
        return crossAnalysisService.analyze(events, heap, topN);
    }
}
