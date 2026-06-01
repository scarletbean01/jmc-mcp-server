package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Biased lock revocation summary.
 */
public record BiasedLockSummary(
        long singleRevocations,
        long classRevocations,
        long selfRevocations,
        List<RevokedLockClassEntry> topClasses
) {
}
