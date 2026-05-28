package io.github.deplague.jmcmcp.domain.model;

/**
 * Connection pool exhaustion detection summary.
 */
public record ConnectionPoolSummary(boolean poolDetected, String poolName,
                                    int threadsWaiting, int blockEvents, double confidence) {
}
