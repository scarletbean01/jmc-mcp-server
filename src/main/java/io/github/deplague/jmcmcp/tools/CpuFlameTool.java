package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP tool for CPU flame graph data and execution sample profiling.
 */
public final class CpuFlameTool {

    private static final String NAME = "cpu_flame";
    private final JfrAnalysisService service;

    public CpuFlameTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide CPU flame graph data including thread states and hottest call paths.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top call paths and methods (default 20)", 20),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);
                    return analyze(filePath, startTimeStr, endTimeStr, topN);
                })).build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection samples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        long totalSamples = JfrItemUtils.count(samples);
        if (totalSamples == 0) {
            return "# CPU Flame Data\n\nNo execution samples found.";
        }

        Map<String, Long> stateDist = new HashMap<>();
        Map<String, Long> pathDist = new HashMap<>();
        Map<String, Long> hottestMethods = new HashMap<>();

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stateAccessor = JfrItemUtils.getAccessor(iterable.getType(), "state");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            for (IItem item : iterable) {
                if (stateAccessor != null) {
                    Object stateObj = stateAccessor.getMember(item);
                    if (stateObj != null) {
                        stateDist.merge(stateObj.toString(), 1L, Long::sum);
                    }
                }

                if (stackAccessor != null) {
                    Object stackObj = stackAccessor.getMember(item);
                    if (stackObj instanceof IMCStackTrace trace) {
                        String path = JfrItemUtils.formatStackTrace(stackObj, 10);
                        pathDist.merge(path, 1L, Long::sum);

                        if (trace.getFrames() != null && !trace.getFrames().isEmpty()) {
                            IMCMethod method = trace.getFrames().get(0).getMethod();
                            if (method != null) {
                                String methodName = method.getType().getFullName() + "." + method.getMethodName();
                                hottestMethods.merge(methodName, 1L, Long::sum);
                            }
                        }
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# CPU Flame Graph Data\n\n");
        sb.append("- **Total Samples:** ").append(totalSamples).append("\n\n");

        sb.append("## CPU State Distribution\n");
        sb.append("| State | Samples | Percentage |\n|---|---|---|\n");
        stateDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> sb.append(String.format("| `%s` | %d | %.1f%% |\n", e.getKey(), e.getValue(), (e.getValue() * 100.0) / totalSamples)));
        sb.append("\n");

        sb.append("## Top CPU Call Paths (Max 10 frames)\n");
        sb.append("| Samples | Percentage | Call Path |\n|---|---|---|\n");
        pathDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(e -> sb.append(String.format("| %d | %.1f%% | `%s` |\n", e.getValue(), (e.getValue() * 100.0) / totalSamples, e.getKey().replace("\n", "`<br>`"))));
        sb.append("\n");

        sb.append("## Hottest Methods (Self Time)\n");
        sb.append("| Method | Samples | Percentage |\n|---|---|---|\n");
        hottestMethods.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(e -> sb.append(String.format("| `%s` | %d | %.1f%% |\n", e.getKey(), e.getValue(), (e.getValue() * 100.0) / totalSamples)));

        return sb.toString();
    }
}
