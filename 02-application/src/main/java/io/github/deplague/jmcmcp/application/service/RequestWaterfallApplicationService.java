package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallResult;
import io.github.deplague.jmcmcp.domain.service.RequestWaterfallService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates JFR loading and request waterfall analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class RequestWaterfallApplicationService {

    private final JfrProvider jfrProvider;
    private final RequestWaterfallService requestWaterfallService;

    /**
     * Analyze request waterfall for the given recording and filters.
     */
    public RequestWaterfallResult analyze(
            String filePath,
            String startTime,
            String endTime,
            String threadName,
            int maxEvents) throws IOException {

        IItemCollection events = jfrProvider.loadRecording(filePath);
        IItemCollection filtered = jfrProvider.filterByTimeRange(
                events, startTime, endTime);

        return requestWaterfallService.analyze(filtered, threadName, maxEvents);
    }
}
