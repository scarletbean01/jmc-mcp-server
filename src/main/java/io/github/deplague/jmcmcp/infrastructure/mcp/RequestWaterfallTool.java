package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.RequestWaterfallApplicationService;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallEvent;
import io.github.deplague.jmcmcp.domain.model.RequestWaterfallResult;
import io.github.deplague.jmcmcp.domain.model.WaterfallPhaseSummary;
import io.github.deplague.jmcmcp.application.service.FormatUtil;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;

/**
 * MCP tool adapter for end-to-end request tracing via thread waterfall view.
 * Reconstructs the chronological sequence of events for a specific thread,
 * showing lock acquisitions, I/O operations, CPU samples, and exceptions.
 */
@HandleToolError
@ApplicationScoped
public final class RequestWaterfallTool {

    private final RequestWaterfallApplicationService applicationService;

    @Inject
    public RequestWaterfallTool(RequestWaterfallApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(description = "Trace a single request end-to-end by reconstructing the chronological sequence of events (locks, I/O, CPU, exceptions) for a specific thread.")
    public ToolResponse smartRequestWaterfall(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "thread_name", description = "Exact thread name or regex pattern to match") String threadName,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "max_events", required = false, description = "Maximum events in waterfall (default 100)") Integer maxEvents
    ) {
        try {
            RequestWaterfallResult result = applicationService.analyze(
                    jfrFilePath, startTime, endTime, threadName, maxEvents != null ? maxEvents : 100);

            if (!result.hasResults()) {
                return ToolResponse.success("# Request Waterfall\n\nNo events found for thread pattern: `" + threadName + "`");
            }

            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
                .append(FormatUtil.formatDuration(result.endTimeMs() - result.baseTimeMs()))
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
                            ? FormatUtil.formatDuration(ps.totalTimeMs())
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
