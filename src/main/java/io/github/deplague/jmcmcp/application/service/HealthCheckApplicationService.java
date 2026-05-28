package io.github.deplague.jmcmcp.application.service;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.domain.model.AsyncJobQueueInfo;
import io.github.deplague.jmcmcp.domain.model.HealthCheckReport;
import io.github.deplague.jmcmcp.domain.model.JvmMemoryInfo;
import io.github.deplague.jmcmcp.domain.model.JvmThreadInfo;
import io.github.deplague.jmcmcp.domain.model.RecordingCacheInfo;
import io.github.deplague.jmcmcp.domain.service.HealthCheckService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrRecordingCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

/**
 * Application service that orchestrates server health check data gathering.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class HealthCheckApplicationService {

    private final JfrRecordingCache recordingCache;
    private final AsyncJobService asyncJobService;
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

        AsyncJobQueueInfo asyncJobQueueInfo = new AsyncJobQueueInfo(
                asyncJobService.activeJobs(),
                asyncJobService.pendingJobs(),
                asyncJobService.completedJobs(),
                asyncJobService.failedJobs(),
                asyncJobService.totalJobs(),
                asyncJobService.recommendedPollSeconds()
        );

        return healthCheckService.buildReport(
                jvmMemory,
                jvmThreads,
                recordingCacheInfo,
                asyncJobQueueInfo,
                uptimeMs,
                startedAt,
                System.getProperty("java.vm.name"),
                System.getProperty("java.version")
        );
    }
}
