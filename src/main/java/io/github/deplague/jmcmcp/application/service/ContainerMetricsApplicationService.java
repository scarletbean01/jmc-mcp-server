package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ContainerMetricsResult;
import io.github.deplague.jmcmcp.domain.service.ContainerMetricsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates container metrics analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ContainerMetricsApplicationService {

    private final JfrProvider jfrProvider;
    private final ContainerMetricsService containerMetricsService;

    public ContainerMetricsResult analyze(String filePath, String startTime, String endTime) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return containerMetricsService.analyze(events);
    }
}
