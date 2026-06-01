package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Application service that orchestrates JFR loading and hot methods analysis.
 * Acts as a boundary between adapters and the domain layer.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class HotMethodsApplicationService {
    private final JfrProvider jfrProvider;
    private final HotMethodsService hotMethodsService;


    /**
     * Analyze hot methods for the given recording and filters.
     */
    public HotMethodsResult analyze(
            String filePath,
            String startTime,
            String endTime,
            String threadName,
            String packagePrefix,
            int topN) throws IOException {

        IItemCollection events = jfrProvider.loadRecording(filePath);
        IItemCollection filtered = jfrProvider.filterByTimeRange(
                events, startTime, endTime);

        return hotMethodsService.analyze(filtered, threadName, packagePrefix, topN);
    }
}
