package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of JFR rules engine analysis.
 */
public record JfrRulesResult(
        List<JfrRuleEntry> rules,
        String minSeverity,
        boolean hasData
) {
}
