package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.StackTraceSearchResult;
import io.github.deplague.jmcmcp.domain.service.StackTraceSearchService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates stack-trace search use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class StackTraceSearchApplicationService {

    private final JfrProvider jfrProvider;
    private final StackTraceSearchService stackTraceSearchService;

    public StackTraceSearchResult analyze(
            String filePath,
            String startTime,
            String endTime,
            String classPattern,
            String eventType,
            int limit
    ) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return stackTraceSearchService.analyze(events, classPattern, eventType, limit);
    }
}
