package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.EventSchemaResult;
import io.github.deplague.jmcmcp.domain.service.EventSchemaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Orchestrates event schema discovery use case.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class EventSchemaApplicationService {

    private final JfrProvider jfrProvider;
    private final EventSchemaService eventSchemaService;

    public EventSchemaResult analyze(String filePath, String eventType) throws IOException {
        IItemCollection events = jfrProvider.loadRecording(filePath);
        return eventSchemaService.analyze(events, eventType);
    }
}
