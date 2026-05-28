package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JfrEventStatsResult;
import io.github.deplague.jmcmcp.domain.service.JfrEventStatsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates JFR event statistics analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class JfrEventStatsApplicationService {

    private final JfrProvider jfrProvider;
    private final JfrEventStatsService jfrEventStatsService;

    public JfrEventStatsResult analyze(String filePath, String eventType, String startTime, String endTime) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return jfrEventStatsService.analyze(events, eventType);
    }
}
