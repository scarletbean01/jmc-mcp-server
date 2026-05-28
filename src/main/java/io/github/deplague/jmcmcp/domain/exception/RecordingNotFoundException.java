package io.github.deplague.jmcmcp.domain.exception;

/**
 * Thrown when a JFR recording cannot be found or accessed.
 */
public class RecordingNotFoundException extends JmcMcpDomainException {

    public RecordingNotFoundException(String message) {
        super(message);
    }

    public RecordingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
