package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ErrorAnalysisResult;
import io.github.deplague.jmcmcp.domain.service.ErrorAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates error analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ErrorAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final ErrorAnalysisService errorAnalysisService;

    public ErrorAnalysisResult analyze(String filePath, String startTime, String endTime, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return errorAnalysisService.analyze(events, topN);
    }
}
