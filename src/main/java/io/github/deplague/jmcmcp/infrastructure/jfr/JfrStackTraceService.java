package io.github.deplague.jmcmcp.infrastructure.jfr;

import org.openjdk.jmc.common.IMCFrame;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for parsing and formatting JFR stack traces.
 */
public final class JfrStackTraceService {

    private JfrStackTraceService() {
        // utility class
    }

    /**
     * Format a stack trace object with all frames (no truncation).
     */
    public static String formatFullStackTrace(Object stackTraceObj) {
        return formatStackTrace(stackTraceObj, Integer.MAX_VALUE);
    }

    /**
     * Check whether any frame in a stack trace matches the given regex pattern.
     */
    public static boolean stackTraceMatches(Object stackTraceObj, Pattern pattern) {
        if (!(stackTraceObj instanceof IMCStackTrace stackTrace)) {
            return false;
        }
        List<? extends IMCFrame> frames = stackTrace.getFrames();
        if (frames == null || frames.isEmpty()) {
            return false;
        }
        
        // Fast paths and zero-allocation checks first
        boolean needsCombined = pattern.pattern().contains(".");
        
        for (IMCFrame frame : frames) {
            IMCMethod method = frame.getMethod();
            if (method == null) continue;
            String typeName = method.getType().getFullName();
            String methodName = method.getMethodName();
            
            if (pattern.matcher(typeName).find() || pattern.matcher(methodName).find()) {
                return true;
            }
            
            // Only pay the concatenation penalty if the regex explicitly might cross the boundary
            if (needsCombined && pattern.matcher(typeName + "." + methodName).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Formats a stack trace object into a truncated string.
     */
    public static String formatStackTrace(Object stackTraceObj, int maxFrames) {
        return formatStackTraceFocusingOn(stackTraceObj, maxFrames, null);
    }

    /**
     * Formats a stack trace object, optionally focusing on a specific package prefix.
     */
    public static String formatStackTraceFocusingOn(Object stackTraceObj, int maxFrames, String packagePrefix) {
        if (!(stackTraceObj instanceof IMCStackTrace stackTrace)) {
            return "No stack trace available";
        }

        List<? extends IMCFrame> frames = stackTrace.getFrames();
        if (frames == null || frames.isEmpty()) {
            return "Empty stack trace";
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        boolean foundPrefix = false;

        for (IMCFrame frame : frames) {
            IMCMethod method = frame.getMethod();
            if (method == null) continue;

            String typeName = method.getType().getFullName();

            if (packagePrefix != null && !packagePrefix.isBlank() && !foundPrefix) {
                if (typeName.startsWith(packagePrefix)) {
                    foundPrefix = true;
                } else {
                    continue; // Skip framework frames until we hit the business logic
                }
            }

            if (count >= maxFrames) {
                sb.append("  ...");
                break;
            }

            if (count > 0) sb.append("\n");
            sb.append("  at ")
                    .append(typeName)
                    .append(".")
                    .append(method.getMethodName())
                    .append("():")
                    .append(frame.getFrameLineNumber());
            count++;
        }

        if (count == 0 && packagePrefix != null && !packagePrefix.isBlank()) {
            return "No frames matched package prefix: " + packagePrefix;
        }

        return sb.toString();
    }

    /**
     * Thread-safe reusable cache for formatted stack traces.
     */
    public static final class StackTraceFormatCache {
        private final IdentityHashMap<Object, String> cache = new IdentityHashMap<>();

        public String format(Object stackTraceObj, int maxFrames) {
            return cache.computeIfAbsent(stackTraceObj, k -> formatStackTrace(k, maxFrames));
        }

        public String formatFocusingOn(Object stackTraceObj, int maxFrames, String packagePrefix) {
            return cache.computeIfAbsent(stackTraceObj, k -> formatStackTraceFocusingOn(k, maxFrames, packagePrefix));
        }

        public int size() {
            return cache.size();
        }
    }
}
