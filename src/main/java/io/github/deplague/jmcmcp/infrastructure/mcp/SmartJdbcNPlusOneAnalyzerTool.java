package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.SmartJdbcNPlusOneAnalyzerApplicationService;
import io.github.deplague.jmcmcp.domain.model.JdbcNPlusOnePattern;
import io.github.deplague.jmcmcp.domain.model.JdbcNPlusOneResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for detecting JDBC N+1 query anti-patterns.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class SmartJdbcNPlusOneAnalyzerTool {

    private final SmartJdbcNPlusOneAnalyzerApplicationService applicationService;

    @Inject
    public SmartJdbcNPlusOneAnalyzerTool(SmartJdbcNPlusOneAnalyzerApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(description = "Smart tool that detects JDBC N+1 query anti-patterns by analyzing sequential short-duration socket I/O events correlated with SQL/ORM stack traces.")
    public ToolResponse smartJdbcNPlusOneAnalyzer(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "top_n", required = false, description = "Number of top N+1 patterns to return (default 5)") Integer topN
    ) {
        try {
            JdbcNPlusOneResult result = applicationService.analyze(
                    jfrFilePath, startTime, endTime, topN != null ? topN : 5);

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(JdbcNPlusOneResult result) {
        if (result.totalSocketEvents() == 0) {
            return "# Smart JDBC N+1 Analyzer\n\n"
                    + "No socket I/O events found in the recording.\n\n"
                    + "<agent_hint>No socket I/O events were recorded. Ensure the JFR recording includes "
                    + "`jdk.SocketRead` and `jdk.SocketWrite` events. Consider using `event_schema` to verify "
                    + "available event types, or `io_hotspots` for a broader I/O analysis.</agent_hint>\n";
        }

        if (!result.hasPatterns()) {
            return "# Smart JDBC N+1 Analyzer\n\n"
                    + "No N+1 query patterns detected. No thread showed a sustained burst of short sequential DB socket reads.\n\n"
                    + "<agent_hint>No N+1 patterns detected. If you suspect database latency issues, try `io_hotspots` "
                    + "for general I/O analysis, `stack_trace_search` with `class_pattern` set to your DAO/repository package, "
                    + "or `request_waterfall` to trace individual request paths.</agent_hint>\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Smart JDBC N+1 Analyzer\n\n");
        sb.append("Analyzed ").append(result.totalSocketEvents()).append(" socket I/O events.\n");
        sb.append("Detected ").append(result.patterns().size()).append(" potential N+1 pattern(s).\n\n");

        int count = 0;
        for (JdbcNPlusOnePattern p : result.patterns()) {
            count++;
            sb.append("## Pattern #").append(count).append("\n\n");
            sb.append("- **Triggering Method:** `").append(p.triggeringMethod()).append("`\n");
            sb.append("- **Thread:** `").append(p.threadName()).append("`\n");
            sb.append("- **Sequential Reads:** ").append(p.totalReads()).append("\n");
            sb.append("- **Burst Window:** ").append(String.format("%.2f ms", p.burstWindowMs())).append("\n");
            sb.append("- **Avg Read Duration:** ").append(String.format("%.3f ms", p.avgDurationMs())).append("\n");
            sb.append("- **Confidence:** ").append(String.format("%.0f%%", p.confidence() * 100)).append("\n");
            if (p.hasOrm()) {
                sb.append("- **ORM Framework:** Detected in stack trace\n");
            }
            sb.append("\n**Sample Stack Trace:**\n\n");
            sb.append("```\n").append(p.sampleTrace()).append("\n```\n\n");
        }

        JdbcNPlusOnePattern worst = result.patterns().getFirst();
        StringBuilder hint = new StringBuilder();
        hint.append("Detected an N+1 query pattern in `").append(worst.triggeringMethod()).append("`. ");
        hint.append(worst.totalReads()).append(" sequential DB socket reads occurred in ");
        hint.append(String.format("%.0f ms", worst.burstWindowMs())).append(". ");
        if (worst.hasOrm()) {
            hint.append("An ORM framework is involved. ");
        }
        hint.append("Consider refactoring to use a SQL JOIN, batch fetching (e.g., `@BatchSize` or `JOIN FETCH`), "
                + "or an Entity Graph. Use `smart_stack_trace_search` with `class_pattern` to find all occurrences "
                + "of this method.");

        sb.append("<agent_hint>").append(hint).append("</agent_hint>\n");

        return sb.toString();
    }
}
