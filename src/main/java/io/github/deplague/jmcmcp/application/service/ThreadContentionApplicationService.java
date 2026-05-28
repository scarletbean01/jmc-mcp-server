package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ThreadContentionResult;
import io.github.deplague.jmcmcp.domain.service.ThreadContentionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates thread contention analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ThreadContentionApplicationService {

    private final JfrProvider jfrProvider;
    private final ThreadContentionService threadContentionService;

    public ThreadContentionResult analyze(String filePath, String startTime, String endTime, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return threadContentionService.analyze(events, topN);
    }
}
