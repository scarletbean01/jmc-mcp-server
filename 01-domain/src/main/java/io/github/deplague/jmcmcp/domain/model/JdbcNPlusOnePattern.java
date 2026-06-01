package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single detected JDBC N+1 query pattern.
 */
public record JdbcNPlusOnePattern(
        String threadName,
        String triggeringMethod,
        long totalReads,
        double burstWindowMs,
        double avgDurationMs,
        double confidence,
        boolean hasOrm,
        String sampleTrace
) {
}
