package io.github.deplague.jmcmcp.domain.model;

/**
 * CPU and GC correlation metrics extracted from a JFR recording.
 */
public record CpuGcMetrics(Double avgCpuLoad, Double maxCpuLoad, long gcPauseCount, String totalGcPauseTime) {
}
