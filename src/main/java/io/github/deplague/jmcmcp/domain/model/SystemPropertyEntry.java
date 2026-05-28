package io.github.deplague.jmcmcp.domain.model;

/**
 * A single JVM system property extracted from a JFR recording.
 */
public record SystemPropertyEntry(String key, String value) {
}
