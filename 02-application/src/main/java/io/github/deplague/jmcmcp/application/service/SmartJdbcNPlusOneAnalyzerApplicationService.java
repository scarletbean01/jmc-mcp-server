package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JdbcNPlusOneResult;
import io.github.deplague.jmcmcp.domain.service.SmartJdbcNPlusOneAnalyzerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates JDBC N+1 query pattern analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class SmartJdbcNPlusOneAnalyzerApplicationService {

    private final JfrProvider jfrProvider;
    private final SmartJdbcNPlusOneAnalyzerService analyzerService;

    public JdbcNPlusOneResult analyze(String filePath, String startTime, String endTime, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return analyzerService.analyze(events, topN);
    }
}
