package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.SystemHealthResult;
import io.github.deplague.jmcmcp.domain.service.SystemHealthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates system health analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class SystemHealthApplicationService {

    private final JfrProvider jfrProvider;
    private final SystemHealthService systemHealthService;

    public SystemHealthResult analyze(String filePath, String startTime, String endTime) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return systemHealthService.analyze(events);
    }
}
