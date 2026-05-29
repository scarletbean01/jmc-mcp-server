package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.NativeMemoryResult;
import io.github.deplague.jmcmcp.domain.service.NativeMemoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates native memory analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class NativeMemoryApplicationService {

    private final JfrProvider jfrProvider;
    private final NativeMemoryService nativeMemoryService;

    public NativeMemoryResult analyze(String filePath, String startTime, String endTime) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return nativeMemoryService.analyze(events);
    }
}
