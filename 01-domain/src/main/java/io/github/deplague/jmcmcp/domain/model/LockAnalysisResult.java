package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Result of advanced lock analysis.
 */
public record LockAnalysisResult(
        Optional<ThreadParkSummary> threadParkSummary,
        Optional<BiasedLockSummary> biasedLockSummary,
        boolean hasData
) {
}
