package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of JDK bug cross-reference analysis.
 */
public record JdkBugReferenceResult(
        Optional<String> jvmVersion,
        List<BugMatch> matches,
        long compilerFailureCount,
        long errorCount,
        long biasedLockRevocationCount,
        boolean hasData
) {

    public boolean hasMatches() {
        return matches != null && !matches.isEmpty();
    }
}
