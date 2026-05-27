package io.github.deplague.jmcmcp.async;

/**
 * Callback interface for long-running analysis tools to report incremental progress.
 * The async job framework captures these updates and surfaces them via
 * {@code get_job_status} so clients can observe progress instead of blind polling.
 */
@FunctionalInterface
public interface ProgressReporter {

    /**
     * Report progress for the current analysis phase.
     *
     * @param percent  0–100 overall completion estimate
     * @param message  human-readable description of current step (e.g. "Parsing GC events…")
     */
    void report(int percent, String message);

    /**
     * No-op reporter for synchronous paths where progress is not observed.
     */
    static ProgressReporter noop() {
        return (p, m) -> {};
    }
}
