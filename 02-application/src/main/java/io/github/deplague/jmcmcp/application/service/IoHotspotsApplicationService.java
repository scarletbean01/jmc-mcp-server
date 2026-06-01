package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.IoHotspotsResult;
import io.github.deplague.jmcmcp.domain.service.IoHotspotsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates I/O hotspots analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class IoHotspotsApplicationService {

    private final JfrProvider jfrProvider;
    private final IoHotspotsService ioHotspotsService;

    public IoHotspotsResult analyze(String filePath, String startTime, String endTime,
                                    String endpointFilter, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return ioHotspotsService.analyze(events, endpointFilter, topN);
    }
}
