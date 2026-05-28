package io.github.deplague.jmcmcp.domain.model;

/**
 * Blocking statistics for a thread pool or thread prefix.
 */
public record BlockedPoolEntry(String poolName, long blockCount, long totalBlockedNanos) {
}
