package io.github.deplague.jmcmcp.domain.exception;

/**
 * Thrown when JFR analysis fails due to malformed data or unexpected errors.
 */
public class AnalysisFailedException extends JmcMcpDomainException {

    public AnalysisFailedException(String message) {
        super(message);
    }

    public AnalysisFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
