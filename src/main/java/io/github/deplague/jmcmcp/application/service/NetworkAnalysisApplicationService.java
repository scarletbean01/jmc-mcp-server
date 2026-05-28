package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.NetworkAnalysisResult;
import io.github.deplague.jmcmcp.domain.service.NetworkAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates network analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class NetworkAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final NetworkAnalysisService networkAnalysisService;

    public NetworkAnalysisResult analyze(String filePath, String startTime, String endTime, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return networkAnalysisService.analyze(events, topN);
    }
}
