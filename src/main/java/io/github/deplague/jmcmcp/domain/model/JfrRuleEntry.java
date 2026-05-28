package io.github.deplague.jmcmcp.domain.model;

/**
 * A single JFR rule result entry.
 */
public record JfrRuleEntry(String name, String severity, String summary, String explanation) {
}
