package io.github.deplague.jmcmcp.domain.model;

/**
 * A virtual thread pinning site.
 */
public record VirtualThreadPinningSite(String stackTrace, long count, double percentage) {
}
