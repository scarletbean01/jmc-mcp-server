package io.github.deplague.jmcmcp.infrastructure.api;

import io.github.deplague.jmcmcp.infrastructure.api.model.ApiResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.Map;

/**
 * REST resource for server health and JVM metrics.
 */
@Path("/api/v1/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @RunOnVirtualThread
    @GET
    public Response health() {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

        var heap = memory.getHeapMemoryUsage();
        var nonHeap = memory.getNonHeapMemoryUsage();

        Map<String, Object> health = Map.of(
                "status", "UP",
                "uptimeSeconds", runtime.getUptime() / 1000,
                "memory", Map.of(
                        "heapUsed", heap.getUsed(),
                        "heapMax", heap.getMax(),
                        "heapCommitted", heap.getCommitted(),
                        "nonHeapUsed", nonHeap.getUsed()
                ),
                "jvm", Map.of(
                        "name", System.getProperty("java.vm.name"),
                        "version", System.getProperty("java.version"),
                        "availableProcessors", Runtime.getRuntime().availableProcessors()
                )
        );

        return Response.ok(ApiResponse.ok(health)).build();
    }
}
