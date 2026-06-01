package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.IoAnalysisResult;
import io.github.deplague.jmcmcp.domain.service.IoAnalysisService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates I/O analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class IoAnalysisApplicationService {

    private final JfrProvider jfrProvider;
    private final IoAnalysisService ioAnalysisService;

    public IoAnalysisResult analyze(String filePath, String startTime, String endTime, String ioType) throws IOException {
        IItemCollection allEvents = jfrProvider.loadRecording(filePath);
        IItemCollection events = jfrProvider.filterByTimeRange(allEvents, startTime, endTime);
        return ioAnalysisService.analyze(events, ioType);
    }
}
