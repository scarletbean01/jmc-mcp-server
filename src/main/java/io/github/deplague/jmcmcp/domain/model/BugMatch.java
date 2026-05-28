package io.github.deplague.jmcmcp.domain.model;

/**
 * A matched JDK bug.
 */
public record BugMatch(BugSignature bug, String matchedText, boolean versionAffected) {
}
