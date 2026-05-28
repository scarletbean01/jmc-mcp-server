package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ThreadDumpResult;
import io.github.deplague.jmcmcp.domain.service.ThreadDumpService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates thread dump extraction use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ThreadDumpApplicationService {

    private final JfrProvider jfrProvider;
    private final ThreadDumpService threadDumpService;

    public ThreadDumpResult analyze(String filePath, int maxDumps) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(filePath);
        return threadDumpService.analyze(events, maxDumps);
    }
}
