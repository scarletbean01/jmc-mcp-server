package io.github.deplague.jmcmcp.infrastructure.api.health;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import java.io.File;

/**
 * Readiness probe for JMC MCP Server.
 *
 * <p>Returns UP if the server is ready to accept traffic:
 * <ul>
 *   <li>Storage directory is writable</li>
 *   <li>JFR recording cache is operational</li>
 * </ul>
 * Kubernetes uses this to determine if the pod should receive traffic.
 */
@Slf4j
@Readiness
@ApplicationScoped
public class JmcReadinessCheck implements HealthCheck {

    private final JfrRecordingCache recordingCache;
    private final String storagePath;

    @Inject
    public JmcReadinessCheck(JfrRecordingCache recordingCache) {
        this.recordingCache = recordingCache;
        this.storagePath = System.getProperty("storage.path", "uploads");
    }

    @Override
    public HealthCheckResponse call() {
        boolean storageWritable = checkStorageWritable();
        boolean cacheHealthy = checkCacheHealthy();

        if (storageWritable && cacheHealthy) {
            return HealthCheckResponse.up("jmc-mcp-server-ready");
        }

        return HealthCheckResponse.builder()
                .name("jmc-mcp-server-ready")
                .down()
                .withData("storageWritable", String.valueOf(storageWritable))
                .withData("cacheHealthy", String.valueOf(cacheHealthy))
                .build();
    }

    private boolean checkStorageWritable() {
        try {
            File dir = new File(storagePath).getAbsoluteFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.isDirectory() && dir.canWrite();
        } catch (Exception e) {
            log.warn("Storage directory not writable: {}", storagePath, e);
            return false;
        }
    }

    private boolean checkCacheHealthy() {
        try {
            // Verify cache is accessible by checking its size (doesn't throw)
            recordingCache.size();
            return true;
        } catch (Exception e) {
            log.warn("JFR recording cache is unhealthy", e);
            return false;
        }
    }
}
