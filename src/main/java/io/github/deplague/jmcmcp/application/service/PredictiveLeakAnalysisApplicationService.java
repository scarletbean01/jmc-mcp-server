package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.PredictiveLeakResult;
import io.github.deplague.jmcmcp.domain.service.PredictiveLeakAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates predictive leak analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class PredictiveLeakAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final PredictiveLeakAnalysisService predictiveLeakAnalysisService;

    public PredictiveLeakResult analyze(String filePath, String startTime, String endTime, double rSquaredThreshold) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return predictiveLeakAnalysisService.analyze(events, rSquaredThreshold);
    }
}
