package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.HealthCheckReport;
import io.github.deplague.jmcmcp.domain.model.JvmMemoryInfo;
import io.github.deplague.jmcmcp.domain.model.JvmThreadInfo;
import io.github.deplague.jmcmcp.domain.model.RecordingCacheInfo;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.Instant;

import static java.lang.String.format;
import static java.time.Duration.ofMillis;

/**
 * Pure domain service for building a server health check report.
 */
@ApplicationScoped
public final class HealthCheckService {

    public HealthCheckReport buildReport(
            JvmMemoryInfo jvmMemory,
            JvmThreadInfo jvmThreads,
            RecordingCacheInfo recordingCache,
            long uptimeMs,
            Instant serverStart,
            String jvmName,
            String javaVersion) {

        double heapUsedPct = jvmMemory.heapMax() > 0
                ? ((jvmMemory.heapUsed() * 100.0) / jvmMemory.heapMax())
                : 0.0;

        String status = determineStatus(heapUsedPct);
        String uptime = formatDuration(ofMillis(uptimeMs));

        return new HealthCheckReport(
                status,
                uptime,
                serverStart,
                jvmName,
                javaVersion,
                heapUsedPct,
                jvmMemory,
                jvmThreads,
                recordingCache
        );
    }

    private String determineStatus(double heapUsedPct) {
        if (heapUsedPct > 95) {
            return "CRITICAL — heap usage > 95%";
        }
        if (heapUsedPct > 85) {
            return "DEGRADED — heap usage > 85%";
        }
        return "HEALTHY";
    }

    private static String formatDuration(Duration d) {
        long days = d.toDays();
        long hours = d.toHoursPart();
        long minutes = d.toMinutesPart();
        long seconds = d.toSecondsPart();
        if (days > 0) {
            return format(
                    "%dd %02dh %02dm %02ds",
                    days,
                    hours,
                    minutes,
                    seconds
            );
        }
        if (hours > 0) {
            return format("%dh %02dm %02ds", hours, minutes, seconds);
        }
        if (minutes > 0) {
            return format("%dm %02ds", minutes, seconds);
        }
        return format("%ds", seconds);
    }
}
