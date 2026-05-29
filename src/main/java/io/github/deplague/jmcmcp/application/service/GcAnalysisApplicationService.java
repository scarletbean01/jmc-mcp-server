package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.GcAnalysisResult;
import io.github.deplague.jmcmcp.domain.service.GcAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates GC analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class GcAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final GcAnalysisService gcAnalysisService;

    public GcAnalysisResult analyze(String filePath, String startTime, String endTime,
                                    String detailLevel) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return gcAnalysisService.analyze(events, detailLevel);
    }
}
