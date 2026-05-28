package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JfrOverviewResult;
import io.github.deplague.jmcmcp.domain.service.JfrOverviewService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates JFR overview analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class JfrOverviewApplicationService {

    private final JfrProvider jfrProvider;
    private final JfrOverviewService jfrOverviewService;

    public JfrOverviewResult analyze(String filePath, String startTime, String endTime) throws IOException {
        IItemCollection fullEvents = jfrProvider.loadRecording(filePath);
        IItemCollection filteredEvents = jfrProvider.filterByTimeRange(fullEvents, startTime, endTime);
        boolean hasTimeFilter = startTime != null || endTime != null;
        return jfrOverviewService.analyze(filePath, fullEvents, filteredEvents, hasTimeFilter);
    }
}
