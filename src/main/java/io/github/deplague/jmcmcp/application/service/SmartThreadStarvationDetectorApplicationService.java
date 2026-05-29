package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ThreadStarvationResult;
import io.github.deplague.jmcmcp.domain.service.SmartThreadStarvationDetectorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates smart thread starvation detection use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class SmartThreadStarvationDetectorApplicationService {

    private final JfrProvider jfrProvider;
    private final SmartThreadStarvationDetectorService detectorService;

    public ThreadStarvationResult analyze(String filePath, String startTime, String endTime,
                                          int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return detectorService.analyze(events, topN);
    }
}
