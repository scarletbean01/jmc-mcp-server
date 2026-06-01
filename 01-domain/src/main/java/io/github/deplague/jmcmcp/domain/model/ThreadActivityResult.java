package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of thread activity analysis.
 */
public record ThreadActivityResult(
        Optional<ThreadStats> threadStats,
        ThreadLifecycle threadLifecycle,
        List<SleepHotspot> sleepHotspots
) {
}
