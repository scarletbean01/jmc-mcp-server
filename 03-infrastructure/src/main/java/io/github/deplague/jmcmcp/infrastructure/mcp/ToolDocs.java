package io.github.deplague.jmcmcp.infrastructure.mcp;

/**
 * Standardized documentation strings for MCP tool parameters.
 * Eliminates inconsistent phrasing and copy-paste errors across tool definitions.
 */
public final class ToolDocs {

    private ToolDocs() {}

    public static final String JFR_FILE_PATH = "Absolute or relative path to the .jfr recording file";
    public static final String HEAP_DUMP_PATH = "Absolute or relative path to the .hprof heap dump file";

    public static final String START_TIME = "Optional start time in ISO-8601 format or epoch milliseconds/seconds (e.g., '2023-01-01T12:00:00Z')";
    public static final String END_TIME = "Optional end time in ISO-8601 format or epoch milliseconds/seconds (e.g., '2023-01-01T12:05:00Z')";

    public static final String PACKAGE_PREFIX = "Optional package prefix to filter stack traces (e.g., 'com.mycompany')";

    public static final String TOP_N_10 = "Number of top results to return (default 10)";
    public static final String TOP_N_20 = "Number of top results to return (default 20)";
    public static final String TOP_N_50 = "Number of top results to return (default 50)";
}
