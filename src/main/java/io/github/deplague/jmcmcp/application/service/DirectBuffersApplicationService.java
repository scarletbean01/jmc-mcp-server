package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.DirectBufferResult;
import io.github.deplague.jmcmcp.domain.service.DirectBuffersService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates direct buffer statistics analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class DirectBuffersApplicationService {

    private final JfrProvider jfrProvider;
    private final DirectBuffersService directBuffersService;

    public DirectBufferResult analyze(String filePath, String startTime, String endTime) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return directBuffersService.analyze(events);
    }
}
