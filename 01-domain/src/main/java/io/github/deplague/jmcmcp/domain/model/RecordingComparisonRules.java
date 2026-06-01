package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Grouped JMC rule changes: regressions and improvements.
 */
public record RecordingComparisonRules(
        List<RecordingComparisonRuleChange> regressions,
        List<RecordingComparisonRuleChange> improvements
) {
}
