package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP tool for analyzing virtual thread pinning and failures.
 */
public final class VirtualThreadsTool {

    private static final String NAME = "virtual_threads";

    private final JfrAnalysisService service;

    public VirtualThreadsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze virtual thread pinning sites and execution failures (Java 21+).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top pinning sites to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection pinned = events.apply(ItemFilters.type("jdk.VirtualThreadPinned"));
        IItemCollection submitFailed = events.apply(ItemFilters.type("jdk.VirtualThreadSubmitFailed"));
        IItemCollection sleepFailed = events.apply(ItemFilters.type("jdk.VirtualThreadSleepFailed"));

        long pinnedCount = JfrItemUtils.count(pinned);
        long submitFailedCount = JfrItemUtils.count(submitFailed);
        long sleepFailedCount = JfrItemUtils.count(sleepFailed);

        if (pinnedCount == 0 && submitFailedCount == 0 && sleepFailedCount == 0) {
            return "# Virtual Thread Analysis\n\nNo virtual thread pinning or failure events found in the recording. Virtual threads may not be in use, or JFR events are not enabled.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Virtual Thread Analysis\n\n");

        if (pinnedCount > 0) {
            sb.append("## Pinning Summary\n\n");
            sb.append("- **Total Pinned Events:** ").append(pinnedCount).append("\n\n");

            Map<String, Long> pinningSites = new HashMap<>();
            for (IItemIterable iterable : pinned) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (stackAccessor != null) {
                    for (IItem item : iterable) {
                        Object stack = stackAccessor.getMember(item);
                        if (stack != null) {
                            String trace = JfrItemUtils.formatStackTrace(stack, 5);
                            pinningSites.merge(trace, 1L, Long::sum);
                        }
                    }
                }
            }

            sb.append("### Top Pinning Sites\n\n");
            sb.append("| Stack Trace (top 5 frames) | Count | Percentage |\n");
            sb.append("|----------------------------|-------|------------|\n");
            pinningSites.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(topN)
                    .forEach(e -> {
                        double pct = (e.getValue() * 100.0) / pinnedCount;
                        sb.append("| `").append(e.getKey().replace("\n", "`<br>`")).append("` | ")
                                .append(e.getValue()).append(" | ")
                                .append(String.format("%.2f%%", pct)).append(" |\n");
                    });
            sb.append("\n");
        }

        if (submitFailedCount > 0) {
            sb.append("## Submission Failures (Carrier Pool Exhaustion)\n\n");
            sb.append("| Exception | Count |\n");
            sb.append("|-----------|-------|\n");
            Map<String, Long> exceptions = new HashMap<>();
            for (IItemIterable iterable : submitFailed) {
                IMemberAccessor<String, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "exception");
                if (msgAccessor != null) {
                    for (IItem item : iterable) {
                        String msg = msgAccessor.getMember(item);
                        exceptions.merge(msg != null ? msg : "Unknown", 1L, Long::sum);
                    }
                }
            }
            exceptions.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(e -> sb.append("| ").append(e.getKey()).append(" | ").append(e.getValue()).append(" |\n"));
            sb.append("\n");
        }

        if (sleepFailedCount > 0) {
            sb.append("## Sleep Failures\n\n");
            sb.append("| Exception | Count |\n");
            sb.append("|-----------|-------|\n");
            Map<String, Long> exceptions = new HashMap<>();
            for (IItemIterable iterable : sleepFailed) {
                IMemberAccessor<String, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "exception");
                if (msgAccessor != null) {
                    for (IItem item : iterable) {
                        String msg = msgAccessor.getMember(item);
                        exceptions.merge(msg != null ? msg : "Unknown", 1L, Long::sum);
                    }
                }
            }
            exceptions.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(e -> sb.append("| ").append(e.getKey()).append(" | ").append(e.getValue()).append(" |\n"));
            sb.append("\n");
        }

        return sb.toString();
    }
}
