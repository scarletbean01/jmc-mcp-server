package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of object statistics analysis.
 */
public record ObjectStatisticsResult(
        List<ObjectStatEntry> entries,
        boolean hasData
) {
}
