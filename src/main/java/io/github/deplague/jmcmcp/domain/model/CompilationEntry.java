package io.github.deplague.jmcmcp.domain.model;

/**
 * A JIT compilation entry.
 */
public record CompilationEntry(String method, String duration, String level) {
}
