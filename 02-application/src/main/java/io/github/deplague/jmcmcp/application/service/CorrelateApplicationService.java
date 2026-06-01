package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.CorrelateResult;
import io.github.deplague.jmcmcp.domain.service.CorrelateService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates JFR loading and correlation analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class CorrelateApplicationService {
    private final JfrProvider jfrProvider;
    private final CorrelateService correlateService;

    /**
     * Analyze cross-dimensional correlations for the given recording and filters.
     */
    public CorrelateResult analyze(
            String filePath,
            String dimension,
            String startTime,
            String endTime,
            int topN) throws IOException {

        IItemCollection events = jfrProvider.loadRecording(filePath);
        IItemCollection filtered = jfrProvider.filterByTimeRange(events, startTime, endTime);

        return correlateService.analyze(filtered, dimension, topN);
    }
}
