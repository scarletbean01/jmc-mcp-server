package io.github.deplague.jmcmcp.domain.model;

/**
 * JVM thread metrics snapshot.
 */
public record JvmThreadInfo(
        int threadCount,
        int peakThreadCount,
        int daemonThreadCount
) {
}
