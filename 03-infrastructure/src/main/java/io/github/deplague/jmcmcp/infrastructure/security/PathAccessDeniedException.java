package io.github.deplague.jmcmcp.infrastructure.security;

/**
 * Thrown when a file path falls outside the configured allowlist
 * ({@code JMC_MCP_ALLOWED_PATHS}). The message is deliberately generic so it can be
 * surfaced to MCP clients without leaking server-side paths.
 */
public class PathAccessDeniedException extends SecurityException {

    public static final String MESSAGE = "Access denied: path not allowed under JMC_MCP_ALLOWED_PATHS";

    public PathAccessDeniedException() {
        super(MESSAGE);
    }
}
