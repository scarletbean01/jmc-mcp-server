package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of thread dump analysis.
 */
public record ThreadDumpResult(
        List<ThreadDumpEntry> dumps,
        boolean hasData
) {
}
