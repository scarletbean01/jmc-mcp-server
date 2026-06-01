package io.github.deplague.jmcmcp.domain.model;

/**
 * A single class loading event.
 */
public record ClassLoadEntry(String className, String duration, String loader) {
}
