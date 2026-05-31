package io.github.deplague.jmcmcp.infrastructure.api.health;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * Liveness probe for JMC MCP Server.
 *
 * <p>Returns UP if the JVM is alive and the application is running.
 * Kubernetes uses this to determine if the container should be restarted.
 */
@Slf4j
@Liveness
@ApplicationScoped
public class JmcLivenessCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("jmc-mcp-server-live");
    }
}
