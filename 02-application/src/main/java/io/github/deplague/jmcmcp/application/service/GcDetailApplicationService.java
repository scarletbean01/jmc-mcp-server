package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.GcDetailResult;
import io.github.deplague.jmcmcp.domain.service.GcDetailService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates detailed GC analysis.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class GcDetailApplicationService {

    private final JfrProvider jfrProvider;
    private final GcDetailService gcDetailService;

    public GcDetailResult analyze(String filePath, String startTime, String endTime, String detailLevel) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return gcDetailService.analyze(events, detailLevel);
    }
}
