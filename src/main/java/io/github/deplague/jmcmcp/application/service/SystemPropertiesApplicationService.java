package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.SystemPropertiesResult;
import io.github.deplague.jmcmcp.domain.service.SystemPropertiesService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Application service that orchestrates JFR loading and system properties extraction.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class SystemPropertiesApplicationService {

    private final JfrProvider jfrProvider;
    private final SystemPropertiesService systemPropertiesService;

    public SystemPropertiesResult analyze(String filePath, String filter) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(filePath);
        return systemPropertiesService.analyze(events, filter);
    }
}
