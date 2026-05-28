package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.TimeSeriesResult;
import io.github.deplague.jmcmcp.domain.service.TimeSeriesService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Application service that orchestrates JFR loading and time-series analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class TimeSeriesApplicationService {

    private final JfrProvider jfrProvider;
    private final TimeSeriesService timeSeriesService;

    public TimeSeriesResult analyze(
            String filePath,
            String startTime,
            String endTime,
            String bucketSize,
            String metric) throws IOException {

        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(
                allEvents, startTime, endTime
        );
        return timeSeriesService.analyze(events, bucketSize, metric);
    }
}
