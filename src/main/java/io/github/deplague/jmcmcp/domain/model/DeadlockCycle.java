package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * A detected deadlock cycle.
 */
public record DeadlockCycle(List<String> threads, List<String> monitors) {
}
