package io.github.deplague.jmcmcp.infrastructure.mcp;

import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Output formatting and truncation utility for MCP tools.
 * Prevents overwhelming LLM context windows by enforcing configurable character limits.
 */
public final class ToolOutput {

    public static final int DEFAULT_MAX_CHARS = 40_000;
    private static final int ESTIMATED_NOTICE_LENGTH = 200;
    private static final int FENCE_RESERVE = 6;
    /** Below this the notice, fence and hint overhead cannot be honoured, so smaller budgets are raised to it. */
    public static final int MIN_MAX_CHARS = 1_000;

    private ToolOutput() {}

    /**
     * Resolves maximum allowed output characters from MicroProfile configuration,
     * falling back to {@link #DEFAULT_MAX_CHARS} if unconfigured or inaccessible.
     */
    public static int configuredMaxChars() {
        try {
            return ConfigProvider.getConfig()
                    .getOptionalValue("jmc.mcp.max-output-chars", Integer.class)
                    .map(v -> Math.max(MIN_MAX_CHARS, v))
                    .orElse(DEFAULT_MAX_CHARS);
        } catch (Throwable t) {
            return DEFAULT_MAX_CHARS;
        }
    }

    /**
     * Limit markdown output using the configured threshold (default 40,000 characters).
     */
    public static String limit(String text) {
        return limit(text, configuredMaxChars());
    }

    /**
     * Limit markdown output to specified character threshold.
     * Ensures total length does not exceed {@code maxChars}, closes any unclosed
     * markdown code fences, and preserves trailing {@code <agent_hint>} directives.
     */
    public static String limit(String text, int requestedMaxChars) {
        int maxChars = Math.max(MIN_MAX_CHARS, requestedMaxChars);
        if (text == null || text.length() <= maxChars) {
            return text;
        }

        int hintIndex = text.lastIndexOf("<agent_hint>");
        String hint = null;
        String contentToLimit = text;
        if (hintIndex >= 0 && text.indexOf("</agent_hint>", hintIndex) > hintIndex) {
            String candidate = text.substring(hintIndex).strip();
            // Only preserve the hint if it leaves room for real content; otherwise it is truncated with the rest.
            if (candidate.length() + ESTIMATED_NOTICE_LENGTH + FENCE_RESERVE + 2 <= maxChars / 2) {
                hint = candidate;
                contentToLimit = text.substring(0, hintIndex);
            }
        }

        int hintOverhead = (hint != null ? hint.length() + 2 : 0);
        int totalOverhead = ESTIMATED_NOTICE_LENGTH + hintOverhead + FENCE_RESERVE;
        int effectiveMax = maxChars - totalOverhead;

        int cutPoint = contentToLimit.lastIndexOf('\n', effectiveMax);
        if (cutPoint <= effectiveMax / 2) {
            cutPoint = effectiveMax;
        }

        String body = contentToLimit.substring(0, cutPoint);

        // Close open code fences if truncated inside a code block
        int fenceCount = countOccurrences(body, "```");
        if (fenceCount % 2 != 0) {
            body = body + "\n```";
        }

        String notice = "\n\n> [!NOTE]\n> *Output truncated at " + cutPoint + " characters ("
                + text.length() + " total). Narrow your query using `top_n`, `start_time`/`end_time`, or `package_prefix`.*";

        if (hint != null) {
            return body + notice + "\n\n" + hint;
        }
        return body + notice;
    }

    private static int countOccurrences(String text, String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }
}
