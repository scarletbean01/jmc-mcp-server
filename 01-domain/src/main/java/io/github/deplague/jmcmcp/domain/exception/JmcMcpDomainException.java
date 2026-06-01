package io.github.deplague.jmcmcp.domain.exception;

/**
 * Base exception for all domain-level errors in JMC-MCP.
 * Adapters are responsible for translating these into protocol-specific responses.
 */
public class JmcMcpDomainException extends RuntimeException {

    public JmcMcpDomainException(String message) {
        super(message);
    }

    public JmcMcpDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
