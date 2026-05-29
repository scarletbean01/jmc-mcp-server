package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.domain.model.*;
import io.github.deplague.jmcmcp.domain.service.HealthCheckService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.lang.management.*;
import java.time.Instant;

/**
 * Application service that orchestrates server health check data gathering.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class HealthCheckApplicationService {

    private final JfrRecordingCache recordingCache;
    private final HealthCheckService healthCheckService;
    private final Instant startedAt = Instant.now();

    public HealthCheckReport check() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        Runtime runtime = Runtime.getRuntime();

        long uptimeMs = runtimeMXBean.getUptime();

        JvmMemoryInfo jvmMemory = new JvmMemoryInfo(
                heapUsage.getUsed(),
                heapUsage.getCommitted(),
                heapUsage.getMax(),
                nonHeapUsage.getUsed(),
                nonHeapUsage.getCommitted(),
                nonHeapUsage.getMax(),
                runtime.maxMemory(),
                runtime.freeMemory()
        );

        JvmThreadInfo jvmThreads = new JvmThreadInfo(
                threadMXBean.getThreadCount(),
                threadMXBean.getPeakThreadCount(),
                threadMXBean.getDaemonThreadCount()
        );

        RecordingCacheInfo recordingCacheInfo = new RecordingCacheInfo(
                recordingCache.size(),
                recordingCache.getHitCount(),
                recordingCache.getMissCount(),
                recordingCache.getEvictionCount(),
                recordingCache.getTotalCachedBytes()
        );

        return healthCheckService.buildReport(
                jvmMemory,
                jvmThreads,
                recordingCacheInfo,
                uptimeMs,
                startedAt,
                System.getProperty("java.vm.name"),
                System.getProperty("java.version")
        );
    }
}
