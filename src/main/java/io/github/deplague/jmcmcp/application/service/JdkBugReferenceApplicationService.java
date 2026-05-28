package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JdkBugReferenceResult;
import io.github.deplague.jmcmcp.domain.service.JdkBugReferenceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates JDK bug cross-reference use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class JdkBugReferenceApplicationService {

    private final JfrProvider jfrProvider;
    private final JdkBugReferenceService jdkBugReferenceService;

    public JdkBugReferenceResult analyze(String filePath, String startTime, String endTime) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return jdkBugReferenceService.analyze(events);
    }
}
