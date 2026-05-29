package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.ProcessInfoResult;
import io.github.deplague.jmcmcp.domain.service.ProcessInfoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;

/**
 * Orchestrates OS and environment context analysis use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class ProcessInfoApplicationService {

    private final JfrProvider jfrProvider;
    private final ProcessInfoService processInfoService;

    public ProcessInfoResult analyze(String filePath) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(filePath);
        return processInfoService.analyze(events);
    }
}
