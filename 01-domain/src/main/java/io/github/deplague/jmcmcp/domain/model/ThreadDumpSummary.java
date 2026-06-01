package io.github.deplague.jmcmcp.domain.model;

/**
 * Thread state summary from JFR ThreadDump events.
 */
public record ThreadDumpSummary(int totalThreads, int blockedCount, int waitingCount) {
}
