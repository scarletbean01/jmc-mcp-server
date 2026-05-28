package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.HeapTrendsResult;
import io.github.deplague.jmcmcp.domain.service.HeapTrendsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Application service that orchestrates heap, metaspace and thread trend analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class HeapTrendsApplicationService {

    private final JfrProvider jfrProvider;
    private final HeapTrendsService heapTrendsService;

    public HeapTrendsResult analyze(String filePath, String startTime, String endTime, String bucketSize) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return heapTrendsService.analyze(events, bucketSize);
    }
}
