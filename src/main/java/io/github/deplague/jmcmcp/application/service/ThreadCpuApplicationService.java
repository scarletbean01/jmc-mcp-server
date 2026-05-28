package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.github.deplague.jmcmcp.domain.service.ThreadCpuService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates thread CPU analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ThreadCpuApplicationService {

    private final JfrProvider jfrProvider;
    private final ThreadCpuService threadCpuService;

    public ThreadCpuResult analyze(String filePath, String startTime, String endTime,
                                   String packagePrefix, int topN) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return threadCpuService.analyze(events, packagePrefix, topN);
    }
}
