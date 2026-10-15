package io.github.deplague.jmcmcp.infrastructure.jfr;

/**
 * Heuristic thresholds shared by every adapter that interprets numeric time arguments, so
 * MCP-side validation and the JFR provider can never disagree about what is "absolute".
 */
public final class EpochThresholds {

    /** 2000-01-01T00:00:00Z in epoch-seconds. Smaller numerics are relative offsets, not instants. */
    public static final long MIN_VALID_EPOCH_SECONDS = 946_684_800L;

    /** Numerics at or above this are epoch-milliseconds rather than epoch-seconds. */
    public static final long EPOCH_MS_THRESHOLD = MIN_VALID_EPOCH_SECONDS * 1_000L;

    private EpochThresholds() {}
}
