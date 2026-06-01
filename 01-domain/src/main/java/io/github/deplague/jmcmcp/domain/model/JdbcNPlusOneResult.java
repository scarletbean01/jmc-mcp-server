package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of JDBC N+1 query pattern analysis.
 */
public record JdbcNPlusOneResult(
        List<JdbcNPlusOnePattern> patterns,
        int totalSocketEvents,
        boolean hasPatterns
) {
}
