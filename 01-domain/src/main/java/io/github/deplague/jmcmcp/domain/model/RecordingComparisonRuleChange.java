package io.github.deplague.jmcmcp.domain.model;

/**
 * A single JMC rule severity change between baseline and target.
 */
public record RecordingComparisonRuleChange(
        String rule,
        String baselineSeverity,
        String targetSeverity
) {
}
