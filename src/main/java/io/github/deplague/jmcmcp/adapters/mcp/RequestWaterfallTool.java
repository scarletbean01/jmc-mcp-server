package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.RequestWaterfallApplicationService;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallEvent;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallResult;
import io.github.deplague.jmcmcp.domain.model.WaterfallPhaseSummary;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;

/**
 * MCP tool adapter for end-to-end request tracing via thread waterfall view.
 * Reconstructs the chronological sequence of events for a specific thread,
 * showing lock acquisitions, I/O operations, CPU samples, and exceptions.
 */
@ApplicationScoped
public final class RequestWaterfallTool implements McpTool {

    private static final String NAME = "smart_request_waterfall";

    private final RequestWaterfallApplicationService applicationService;

    @Inject
    public RequestWaterfallTool(RequestWaterfallApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Trace a single request end-to-end by reconstructing the chronological " +
                                "sequence of events (locks, I/O, CPU, exceptions) for a specific thread.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "thread_name", SchemaUtil.stringProp("Exact thread name or regex pattern to match"),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "max_events", SchemaUtil.intProp("Maximum events in waterfall (default 100)", 100),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path", "thread_name")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String threadName = SchemaUtil.getString(request.arguments(), "thread_name");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int maxEvents = SchemaUtil.getIntOrDefault(request.arguments(), "max_events", 100);
                        SchemaUtil.getBooleanOrDefault(request.arguments(), "async", false);

                        RequestWaterfallResult result = applicationService.analyze(
                                filePath, startTimeStr, endTimeStr, threadName, maxEvents);

                        if (!result.hasResults()) {
                            return CallToolResult.builder()
                                    .addTextContent("# Request Waterfall\n\nNo events found for thread pattern: `" + threadName + "`")
                                    .isError(false)
                                    .build();
                        }

                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String formatMarkdown(RequestWaterfallResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Request Waterfall\n\n");

        sb.append("## Thread Summary\n\n");
        sb.append("- **Matched Thread(s):** ")
                .append(String.join(", ", result.matchedThreads()))
                .append("\n");
        sb.append("- **Total Events:** ")
                .append(result.events().size())
                .append("\n");
        sb.append("- **Time Span:** ")
                .append(SchemaUtil.formatDuration(result.endTimeMs() - result.baseTimeMs()))
                .append("\n\n");

        sb.append("| Event Type | Count |\n|------------|-------|\n");
        result.eventTypeCounts().entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> sb.append("| `")
                        .append(e.getKey())
                        .append("` | ")
                        .append(e.getValue())
                        .append(" |\n"));
        sb.append("\n");

        sb.append("## Waterfall Timeline\n\n");
        sb.append("| Time | Event Type | Phase | Duration | Detail | Top Frame |\n");
        sb.append("|------|------------|-------|----------|--------|-----------|\n");
        for (RequestWaterfallEvent we : result.events()) {
            long offsetMs = we.timeMs() - result.baseTimeMs();
            sb.append(String.format("| +%dms | `%s` | %s | %s | %s | `%s` |\n",
                    offsetMs,
                    we.eventType(),
                    we.phase(),
                    we.durationMs() > 0 ? we.durationMs() + "ms" : "—",
                    we.detail().isEmpty() ? "—" : we.detail(),
                    we.topFrame().isEmpty() ? "—" : we.topFrame()
            ));
        }
        sb.append("\n");

        sb.append("## Phase Breakdown\n\n");
        sb.append("| Phase | Total Time | % of Recorded | Event Count |\n");
        sb.append("|-------|-----------|---------------|-------------|\n");
        long totalRecordedMs = result.endTimeMs() - result.baseTimeMs();
        for (WaterfallPhaseSummary ps : result.phaseSummaries()) {
            double pct = totalRecordedMs > 0
                    ? (ps.totalTimeMs() * 100.0 / totalRecordedMs)
                    : 0;
            sb.append(String.format("| %s | %s | %.1f%% | %d |\n",
                    ps.phaseName(),
                    ps.totalTimeMs() > 0
                            ? SchemaUtil.formatDuration(ps.totalTimeMs())
                            : "—",
                    pct,
                    ps.eventCount()));
        }
        sb.append("\n");

        String dominantPhase = result.phaseSummaries().stream()
                .max(Comparator.comparingLong(WaterfallPhaseSummary::totalTimeMs))
                .map(WaterfallPhaseSummary::phaseName)
                .orElse("");
        sb.append("<agent_hint>");
        if ("BLOCKED".equals(dominantPhase)) {
            sb.append("Thread spends most time blocked. Consider `thread_contention` for lock details or `correlate` to see what I/O happens under locks.");
        } else if ("IO".equals(dominantPhase)) {
            sb.append("Thread spends most time in I/O. Consider `io_hotspots` for endpoint-level analysis.");
        } else if ("CPU".equals(dominantPhase)) {
            sb.append("Thread spends most time on CPU. Consider `hot_methods` with `package_prefix` for application-level hot spots.");
        } else {
            sb.append("Waterfall trace complete. Consider `correlate` for cross-dimensional analysis or `stack_trace_search` to find specific methods.");
        }
        sb.append("</agent_hint>\n");

        return sb.toString();
    }
}
