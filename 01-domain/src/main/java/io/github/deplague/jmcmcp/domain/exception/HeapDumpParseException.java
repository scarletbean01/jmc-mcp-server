package io.github.deplague.jmcmcp.domain.exception;

public class HeapDumpParseException extends RuntimeException {
    public HeapDumpParseException(String message) {
        super(message);
    }

    public HeapDumpParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
