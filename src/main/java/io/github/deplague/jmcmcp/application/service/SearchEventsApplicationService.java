package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.SearchEventsResult;
import io.github.deplague.jmcmcp.domain.service.SearchEventsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates JFR event search use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class SearchEventsApplicationService {

    private final JfrProvider jfrProvider;
    private final SearchEventsService searchEventsService;

    public SearchEventsResult search(String filePath, String startTime, String endTime, String eventType, int limit) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return searchEventsService.search(events, eventType, limit);
    }
}
