package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.domain.model.HotMethodEntry;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.github.deplague.jmcmcp.domain.service.HotMethodsService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import java.io.IOException;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * Legacy compatibility wrapper for macro tools that still rely on the
 * old {@code analyze()} API. This class is NOT registered as an MCP tool.
 * The canonical MCP adapter is {@link io.github.deplague.jmcmcp.adapters.mcp.HotMethodsTool}.
 *
 * <p>Will be removed in Phase 2 when all macro tools are refactored.</p>
 */
public final class HotMethodsTool {

    private final JfrAnalysisService service;

    public HotMethodsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public String analyze(
            String filePath,
            String startTimeStr,
            String endTimeStr,
            String threadName,
            String packagePrefix,
            int topN
    ) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(
                allEvents,
                startTimeStr,
                endTimeStr
        );

        HotMethodsService hotMethodsService = new HotMethodsService();
        HotMethodsResult result = hotMethodsService.analyze(
                events, threadName, packagePrefix, topN
        );

        if (!result.hasResults()) {
            return "# Hot Methods\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Hot Methods & Call Paths\n\n");
        sb.append("| Samples | Stack Trace (top 5 frames) |\n");
        sb.append("|---------|----------------------------|\n");

        for (HotMethodEntry entry : result.entries()) {
            sb.append("| ").append(entry.sampleCount()).append(" | ");
            sb.append("`")
                    .append(entry.stackTrace().replace("\n", "`<br>`"))
                    .append("` |\n");
        }

        String topMethod = result.topMethod() != null
                ? result.topMethod()
                : "unknown";
        sb.append("\n<agent_hint>Top hot method is `")
                .append(topMethod)
                .append(
                        "`. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>\n"
                );

        return sb.toString();
    }
}
