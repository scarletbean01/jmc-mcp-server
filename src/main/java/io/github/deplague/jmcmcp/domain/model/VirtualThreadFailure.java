package io.github.deplague.jmcmcp.domain.model;

/**
 * A virtual thread failure entry.
 */
public record VirtualThreadFailure(String exception, long count) {
}
