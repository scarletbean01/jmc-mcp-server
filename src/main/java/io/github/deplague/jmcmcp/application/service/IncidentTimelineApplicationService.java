package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.IncidentTimelineResult;
import io.github.deplague.jmcmcp.domain.service.IncidentTimelineService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates incident timeline analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class IncidentTimelineApplicationService {

    private final JfrProvider jfrProvider;
    private final IncidentTimelineService incidentTimelineService;

    public IncidentTimelineResult analyze(
            String filePath,
            String anchorEvent,
            String anchorTimeStr,
            int windowMs
    ) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(filePath);
        return incidentTimelineService.analyze(events, anchorEvent, anchorTimeStr, windowMs);
    }
}
