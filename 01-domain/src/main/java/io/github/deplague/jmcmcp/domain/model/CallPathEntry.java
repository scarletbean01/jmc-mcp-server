package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single call path entry with sample count and percentage.
 */
public record CallPathEntry(String callPath, long samples, double percentage) {
}
