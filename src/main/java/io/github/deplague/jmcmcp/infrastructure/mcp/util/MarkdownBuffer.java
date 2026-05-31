package io.github.deplague.jmcmcp.infrastructure.mcp.util;

/**
 * ThreadLocal-pooled StringBuilder for MCP tool markdown formatting.
 * Eliminates per-request StringBuilder allocation for hot formatting paths.
 *
 * <p>Usage:
 * <pre>{@code
 *   MarkdownBuffer buf = MarkdownBuffer.get();
 *   buf.append("# Title\n");
 *   String result = buf.toStringAndReset();
 * }</pre>
 */
public final class MarkdownBuffer {

    private static final int DEFAULT_CAPACITY = 4096;
    private static final int MAX_CAPACITY = 65536;

    private static final ThreadLocal<MarkdownBuffer> POOL = ThreadLocal.withInitial(
            () -> new MarkdownBuffer(DEFAULT_CAPACITY)
    );

    private final StringBuilder sb;

    private MarkdownBuffer(int capacity) {
        this.sb = new StringBuilder(capacity);
    }

    /**
     * Obtain a pooled MarkdownBuffer for the current thread.
     */
    public static MarkdownBuffer get() {
        return POOL.get();
    }

    /**
     * Append a string to the buffer.
     */
    public MarkdownBuffer append(String s) {
        sb.append(s);
        return this;
    }

    /**
     * Append a char sequence to the buffer.
     */
    public MarkdownBuffer append(CharSequence csq) {
        sb.append(csq);
        return this;
    }

    /**
     * Append a formatted string (delegates to String.format).
     */
    public MarkdownBuffer appendf(String format, Object... args) {
        sb.append(String.format(format, args));
        return this;
    }

    /**
     * Append a char to the buffer.
     */
    public MarkdownBuffer append(char c) {
        sb.append(c);
        return this;
    }

    /**
     * Append a long to the buffer.
     */
    public MarkdownBuffer append(long l) {
        sb.append(l);
        return this;
    }

    /**
     * Append a double to the buffer.
     */
    public MarkdownBuffer append(double d) {
        sb.append(d);
        return this;
    }

    /**
     * Append an int to the buffer.
     */
    public MarkdownBuffer append(int i) {
        sb.append(i);
        return this;
    }

    /**
     * Append a boolean to the buffer.
     */
    public MarkdownBuffer append(boolean b) {
        sb.append(b);
        return this;
    }

    /**
     * Append an object (uses String.valueOf) to the buffer.
     */
    public MarkdownBuffer append(Object obj) {
        sb.append(obj);
        return this;
    }

    /**
     * Append a newline.
     */
    public MarkdownBuffer nl() {
        sb.append('\n');
        return this;
    }

    /**
     * Append a string and then a newline.
     */
    public MarkdownBuffer line(String s) {
        sb.append(s).append('\n');
        return this;
    }

    /**
     * Return the current content and reset the buffer for reuse.
     * If the buffer has grown beyond MAX_CAPACITY, trims it back to DEFAULT_CAPACITY.
     */
    public String toStringAndReset() {
        String result = sb.toString();
        int cap = sb.capacity();
        sb.setLength(0);
        if (cap > MAX_CAPACITY) {
            sb.trimToSize();
            sb.ensureCapacity(DEFAULT_CAPACITY);
        }
        return result;
    }

    /**
     * Return the current content without resetting.
     */
    @Override
    public String toString() {
        return sb.toString();
    }

    /**
     * Current length of the buffer content.
     */
    public int length() {
        return sb.length();
    }

    /**
     * Reset the buffer (clear without returning content).
     */
    public void reset() {
        sb.setLength(0);
    }
}
