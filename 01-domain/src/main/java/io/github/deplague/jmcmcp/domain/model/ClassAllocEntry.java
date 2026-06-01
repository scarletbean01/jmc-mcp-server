package io.github.deplague.jmcmcp.domain.model;

/**
 * A single class allocation entry.
 */
public record ClassAllocEntry(String className, long count, String totalBytes, String avgSize) {
}
