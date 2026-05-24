package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.util.HashMap;
import java.util.Map;

public final class LockFlameTool {

    private static final String NAME = "lock_flame";
    private final JfrAnalysisService service;

    public LockFlameTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide lock contention flame graph data by aggregating monitor enter/wait by full stack trace.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top call paths (default 20)", 20)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }
                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                }).build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        Map<String, Long> pathDist = new HashMap<>();
        long totalNanos = 0;

        for (String typeId : new String[]{"jdk.JavaMonitorEnter", "jdk.JavaMonitorWait", "jdk.ThreadPark"}) {
            IItemCollection locks = events.apply(ItemFilters.type(typeId));
            for (IItemIterable iterable : locks) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                IMemberAccessor<IQuantity, IItem> durAccessor = JfrAttributes.DURATION.getAccessor(iterable.getType());
                
                if (stackAccessor != null && durAccessor != null) {
                    for (IItem item : iterable) {
                        Object stackObj = stackAccessor.getMember(item);
                        IQuantity dur = durAccessor.getMember(item);
                        if (stackObj != null && dur != null) {
                            long nanos = dur.clampedLongValueIn(UnitLookup.NANOSECOND);
                            totalNanos += nanos;
                            String path = JfrItemUtils.formatStackTrace(stackObj, 10);
                            pathDist.merge(path, nanos, Long::sum);
                        }
                    }
                }
            }
        }

        if (totalNanos == 0) {
            return "# Lock Flame Data\n\nNo lock/park events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Lock Flame Graph Data\n\n");
        sb.append("- **Total Lock Duration:** ").append(SchemaUtil.formatDuration(totalNanos / 1_000_000L)).append("\n\n");

        sb.append("## Top Lock Call Paths (Max 10 frames)\n");
        sb.append("| Lock Duration | Percentage | Call Path |\n|---|---|---|\n");
        long finalTotal = totalNanos;
        pathDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(e -> sb.append(String.format("| %s | %.1f%% | `%s` |\n", SchemaUtil.formatDuration(e.getValue() / 1_000_000L), (e.getValue() * 100.0) / finalTotal, e.getKey().replace("\n", "`<br>`"))));

        return sb.toString();
    }
}