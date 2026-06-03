package io.github.deplague.jmcmcp.domain.exception;

public class HeapDumpNotFoundException extends RuntimeException {
    public HeapDumpNotFoundException(String message) {
        super(message);
    }
}
