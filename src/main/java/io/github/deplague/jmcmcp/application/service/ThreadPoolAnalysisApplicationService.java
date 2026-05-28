package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ThreadPoolAnalysisResult;
import io.github.deplague.jmcmcp.domain.service.ThreadPoolAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates thread pool analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ThreadPoolAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final ThreadPoolAnalysisService threadPoolAnalysisService;

    public ThreadPoolAnalysisResult analyze(String filePath, String startTime, String endTime,
                                            int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return threadPoolAnalysisService.analyze(events, topN);
    }
}
