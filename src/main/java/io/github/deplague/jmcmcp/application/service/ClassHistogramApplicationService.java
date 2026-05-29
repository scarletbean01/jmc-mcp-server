package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ClassHistogramResult;
import io.github.deplague.jmcmcp.domain.service.ClassHistogramService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates class allocation histogram analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ClassHistogramApplicationService {

    private final JfrProvider jfrProvider;
    private final ClassHistogramService classHistogramService;

    public ClassHistogramResult analyze(String filePath, String startTime, String endTime, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return classHistogramService.analyze(events, topN);
    }
}
