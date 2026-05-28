package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JfrRulesResult;
import io.github.deplague.jmcmcp.domain.service.JfrRulesService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates JFR rules engine analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class JfrRulesApplicationService {

    private final JfrProvider jfrProvider;
    private final JfrRulesService jfrRulesService;

    public JfrRulesResult analyze(String filePath, String startTime, String endTime, String minSeverity) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return jfrRulesService.analyze(events, minSeverity);
    }
}
