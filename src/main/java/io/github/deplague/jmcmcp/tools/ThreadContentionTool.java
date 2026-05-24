package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP tool for analyzing thread contention (monitor enter/wait).
 */
public final class ThreadContentionTool {

    private static final String NAME = "thread_contention";

    private final JfrAnalysisService service;

    public ThreadContentionTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze thread contention in a JFR recording. " +
                                "Identifies top monitor lock contentions and wait locations.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top contention sites to return (default 10)", 10)
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

        Map<ContentionKey, Long> durationMap = new HashMap<>();

        processContention(events, "jdk.JavaMonitorEnter", durationMap);
        processContention(events, "jdk.JavaMonitorWait", durationMap);

        if (durationMap.isEmpty()) {
            return "# Thread Contention Analysis\n\nNo monitor contention or wait events found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Contention Analysis\n\n");
        sb.append("| Total Duration | Monitor Class | Contention Site (top 5 frames) |\n");
        sb.append("|----------------|---------------|--------------------------------|\n");

        durationMap.entrySet().stream()
                .sorted(Map.Entry.<ContentionKey, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(entry -> {
                    sb.append("| ").append(JfrAnalysisService.display(org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND.quantity(entry.getValue()))).append(" | ");
                    sb.append("`").append(entry.getKey().monitorClass).append("` | ");
                    sb.append("`").append(entry.getKey().stackTrace.replace("\n", "`<br>`")).append("` |\n");
                });

        return sb.toString();
    }

    private void processContention(IItemCollection events, String typeId, Map<ContentionKey, Long> map) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> monitorAccessor = JfrItemUtils.getAccessor(iterable.getType(), "monitorClass");
            IMemberAccessor<IQuantity, IItem> durationAccessor = JfrItemUtils.getAccessor(iterable.getType(), JfrAttributes.DURATION.getIdentifier());
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (monitorAccessor != null && durationAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    Object monitorObj = monitorAccessor.getMember(item);
                    IQuantity durationQ = durationAccessor.getMember(item);
                    Object stackObj = stackAccessor.getMember(item);

                    if (monitorObj != null && durationQ != null && stackObj != null) {
                        String monitorClass = monitorObj.toString();
                        String trace = JfrItemUtils.formatStackTrace(stackObj, 5);
                        ContentionKey key = new ContentionKey(monitorClass, trace);
                        map.merge(key, durationQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.NANOSECOND), Long::sum);
                    }
                }
            }
        }
    }

    private record ContentionKey(String monitorClass, String stackTrace) {
    }
}
