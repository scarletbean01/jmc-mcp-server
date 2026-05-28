package io.github.deplague.jmcmcp.application;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import io.github.deplague.jmcmcp.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * CDI producer methods for domain and infrastructure objects.
 * Keeps the domain layer free of framework annotations while making
 * instances available for injection.
 */
@ApplicationScoped
public class DomainConfig {

    @Produces
    @ApplicationScoped
    public HotMethodsService hotMethodsService() {
        return new HotMethodsService();
    }

    @Produces
    @ApplicationScoped
    public JfrRecordingCache jfrRecordingCache() {
        return new JfrRecordingCache();
    }

    @Produces
    @ApplicationScoped
    public RecordingAccessController recordingAccessController() {
        return new RecordingAccessController();
    }

    @Produces
    @ApplicationScoped
    public CallTreeCache callTreeCache() {
        return new CallTreeCache();
    }

    @Produces
    @ApplicationScoped
    public AsyncJobService asyncJobService() {
        return new AsyncJobService();
    }
}
