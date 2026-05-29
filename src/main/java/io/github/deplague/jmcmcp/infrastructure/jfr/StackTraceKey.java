package io.github.deplague.jmcmcp.infrastructure.jfr;

import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.IMCFrame;

import java.util.List;
import java.util.Objects;

/**
 * Zero-allocation wrapper for stack traces to be used as Map keys.
 * Compares stack traces based on their frames rather than formatted strings,
 * drastically reducing short-lived garbage generation during aggregation.
 */
public final class StackTraceKey {

    private final Object stackTraceObj;
    private final int maxFrames;
    private final String packagePrefix;
    private int cachedHash;

    public StackTraceKey(Object stackTraceObj, int maxFrames) {
        this(stackTraceObj, maxFrames, null);
    }

    public StackTraceKey(Object stackTraceObj, int maxFrames, String packagePrefix) {
        this.stackTraceObj = stackTraceObj;
        this.maxFrames = maxFrames;
        this.packagePrefix = packagePrefix;
    }

    public Object getStackTraceObj() {
        return stackTraceObj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackTraceKey that = (StackTraceKey) o;
        
        // Fast path for identity
        if (this.stackTraceObj == that.stackTraceObj && 
            this.maxFrames == that.maxFrames && 
            Objects.equals(this.packagePrefix, that.packagePrefix)) {
            return true;
        }

        // If not identical, we must compare the frames
        if (!(this.stackTraceObj instanceof IMCStackTrace st1) || !(that.stackTraceObj instanceof IMCStackTrace st2)) {
            return Objects.equals(this.stackTraceObj, that.stackTraceObj);
        }

        List<? extends IMCFrame> f1 = st1.getFrames();
        List<? extends IMCFrame> f2 = st2.getFrames();
        
        if (f1 == null || f2 == null) return f1 == f2;

        int i1 = 0, i2 = 0;
        int count1 = 0, count2 = 0;
        
        // Skip frames until prefix matches
        if (packagePrefix != null && !packagePrefix.isBlank()) {
            i1 = skipUntilPrefix(f1, packagePrefix);
            i2 = skipUntilPrefix(f2, packagePrefix);
        }

        while (i1 < f1.size() && i2 < f2.size() && count1 < maxFrames && count2 < maxFrames) {
            IMCFrame frame1 = f1.get(i1);
            IMCFrame frame2 = f2.get(i2);
            
            if (!Objects.equals(frame1.getMethod(), frame2.getMethod()) || 
                !Objects.equals(frame1.getFrameLineNumber(), frame2.getFrameLineNumber())) {
                return false;
            }
            i1++;
            i2++;
            count1++;
            count2++;
        }
        
        return count1 == count2;
    }

    @Override
    public int hashCode() {
        if (cachedHash != 0) return cachedHash;
        
        if (!(stackTraceObj instanceof IMCStackTrace st)) {
            return stackTraceObj != null ? stackTraceObj.hashCode() : 0;
        }

        List<? extends IMCFrame> frames = st.getFrames();
        if (frames == null || frames.isEmpty()) return 0;

        int hash = 1;
        int count = 0;
        int i = 0;

        if (packagePrefix != null && !packagePrefix.isBlank()) {
            i = skipUntilPrefix(frames, packagePrefix);
        }

        while (i < frames.size() && count < maxFrames) {
            IMCFrame frame = frames.get(i);
            hash = 31 * hash + (frame.getMethod() != null ? frame.getMethod().hashCode() : 0);
            hash = 31 * hash + (frame.getFrameLineNumber() != null ? frame.getFrameLineNumber().hashCode() : 0);
            i++;
            count++;
        }
        
        cachedHash = hash;
        return hash;
    }

    private int skipUntilPrefix(List<? extends IMCFrame> frames, String prefix) {
        for (int i = 0; i < frames.size(); i++) {
            var method = frames.get(i).getMethod();
            if (method != null && method.getType() != null) {
                if (method.getType().getFullName().startsWith(prefix)) {
                    return i;
                }
            }
        }
        return frames.size(); // Not found, skip all
    }
}
