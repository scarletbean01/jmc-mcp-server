package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of the smart lock resolver analysis.
 */
public record SmartLockResolverResult(
        boolean hasMonitorEnters,
        int totalDistinctPatterns,
        List<LockHolderIssue> topIssues
) {
}
