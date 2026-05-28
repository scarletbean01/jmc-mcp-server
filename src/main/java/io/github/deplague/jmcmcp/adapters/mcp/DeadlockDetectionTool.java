package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.DeadlockDetectionApplicationService;
import io.github.deplague.jmcmcp.domain.model.DeadlockCycle;
import io.github.deplague.jmcmcp.domain.model.DeadlockDetectionResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import java.util.List;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for deadlock detection.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class DeadlockDetectionTool implements McpTool {

    private static final String NAME = "deadlock_detection";

    private final DeadlockDetectionApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Detect thread deadlocks by analyzing monitor ownership and wait-for relationships. "
                                                + "Parses thread dump events to build a wait-for graph and detects cycles. "
                                                + "Outputs Mermaid diagrams for visual deadlock rendering."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp()
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "start_time",
                                null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "end_time",
                                null
                        );

                        DeadlockDetectionResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr
                        );
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

    private String formatMarkdown(DeadlockDetectionResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Deadlock Detection\n\n");

        if (!result.hasData()) {
            if (!result.hasThreadDumps()) {
                return """
                        # Deadlock Detection

                        No thread dump or monitor events found. \
                        Enable Thread Dump events or monitor events in your JFR recording configuration.""";
            }
            sb.append("No parseable thread dump events found.");
            return sb.toString();
        }

        if (!result.hasThreadDumps() && (result.monitorEnterCount() > 0 || result.monitorWaitCount() > 0)) {
            sb.append("## ⚠️ Limited Analysis Mode\n\n");
            sb.append("No `jdk.ThreadDump` events available. Deadlock detection requires thread dumps to reconstruct ");
            sb.append("monitor ownership graphs. Showing monitor contention summary instead.\n\n");
            sb.append("- Monitor enter events: ").append(result.monitorEnterCount()).append("\n");
            sb.append("- Monitor wait events: ").append(result.monitorWaitCount()).append("\n\n");
            sb.append("**Recommendation:** Enable Thread Dump events in your JFR configuration ");
            sb.append("(`-XX:StartFlightRecording:settings=profile`) for full deadlock detection.\n");
            return sb.toString();
        }

        if (!result.hasDeadlocks()) {
            sb.append("## Verdict: ✅ NO DEADLOCKS DETECTED\n\n");
            sb.append("Analyzed ").append(result.threadsAnalyzed()).append(" threads from the latest thread dump.\n");
            sb.append("No cycles found in the monitor wait-for graph.\n\n");
            appendMonitorSummary(sb, result);
        } else {
            sb.append("## Verdict: ⛔ ").append(result.deadlocks().size())
                    .append(" DEADLOCK").append(result.deadlocks().size() > 1 ? "S" : "").append(" DETECTED\n\n");
            for (int i = 0; i < result.deadlocks().size(); i++) {
                appendDeadlockCycle(sb, result.deadlocks().get(i), i + 1);
            }
            appendRecommendations(sb, result.deadlocks());
        }

        return sb.toString();
    }

    private void appendMonitorSummary(StringBuilder sb, DeadlockDetectionResult result) {
        sb.append("## Monitor Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append(String.format("| Threads Analyzed | %d |%n", result.threadsAnalyzed()));
        sb.append(String.format("| Threads Holding Locks | %d |%n", result.threadsWithLocks()));
        sb.append(String.format("| Threads Waiting for Locks | %d |%n", result.threadsWaiting()));
        sb.append(String.format("| Total Monitors | %d |%n", result.totalMonitors()));
    }

    private void appendDeadlockCycle(StringBuilder sb, DeadlockCycle cycle, int index) {
        sb.append("### Deadlock Cycle ").append(index).append("\n\n");
        sb.append("```mermaid\ngraph TD\n");
        for (int i = 0; i < cycle.threads().size(); i++) {
            String thread = cycle.threads().get(i);
            String monitor = cycle.monitors().get(i);
            String nextThread = cycle.threads().get((i + 1) % cycle.threads().size());
            sb.append("  ").append(safeMermaid(thread)).append("[\"").append(escapeMermaid(thread)).append("\"]")
                    .append(" -->|waiting for| ").append(safeMermaid(monitor))
                    .append("[\"Monitor ").append(escapeMermaid(monitor)).append("\"]\n");
            sb.append("  ").append(safeMermaid(monitor)).append(" -->|held by| ")
                    .append(safeMermaid(nextThread)).append("\n");
        }
        sb.append("```\n\n");

        sb.append("**Threads involved:** ").append(String.join(", ", cycle.threads())).append("\n\n");

        sb.append("| Thread | Waits For | Held By |\n");
        sb.append("|--------|-----------|--------|\n");
        for (int i = 0; i < cycle.threads().size(); i++) {
            String thread = cycle.threads().get(i);
            String monitor = cycle.monitors().get(i);
            String nextThread = cycle.threads().get((i + 1) % cycle.threads().size());
            sb.append(String.format("| %s | Monitor %s | %s |%n", thread, monitor, nextThread));
        }
        sb.append("\n");
    }

    private void appendRecommendations(StringBuilder sb, List<DeadlockCycle> deadlocks) {
        sb.append("## Recommendations\n\n");
        sb.append("To resolve deadlocks:\n\n");
        sb.append("1. **Lock ordering:** Always acquire locks in a consistent global order\n");
        sb.append("2. **Use `tryLock()` with timeouts:** `java.util.concurrent.locks.Lock.tryLock(timeout, unit)` prevents indefinite blocking\n");
        sb.append("3. **Reduce lock scope:** Minimize the time holding locks by moving non-critical work outside synchronized blocks\n");
        sb.append("4. **Lock ordering by identity:** Use `System.identityHashCode()` to determine lock acquisition order\n\n");
        if (deadlocks.size() > 1) {
            sb.append("⚠️ Multiple deadlocks detected. Investigate each cycle independently.\n");
        }
    }

    private String safeMermaid(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_");
    }

    private String escapeMermaid(String s) {
        return s.replace("\"", "'");
    }
}
