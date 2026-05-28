package io.github.deplague.jmcmcp.domain.model;

/**
 * A single JVM runtime flag.
 */
public record JvmFlagEntry(String name, String value, String type) {
}
