package io.github.deplague.jmcmcp.domain.model;

/**
 * Thread statistics from jdk.JavaThreadStatistics events.
 */
public record ThreadStats(
        String peakCount,
        String activeMin,
        String activeAvg,
        String activeMax,
        String daemonCount,
        String accumulatedCount
) {
}
