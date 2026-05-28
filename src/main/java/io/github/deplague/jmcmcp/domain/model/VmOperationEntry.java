package io.github.deplague.jmcmcp.domain.model;

/**
 * A single VM operation event.
 */
public record VmOperationEntry(String operation, String duration, String caller) {
}
