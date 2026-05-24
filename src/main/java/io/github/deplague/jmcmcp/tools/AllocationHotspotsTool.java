package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.deplague.jmcmcp.tools.SchemaUtil.formatBytes;

/**
 * MCP tool for memory allocation hotspot analysis.
 */
public final class AllocationHotspotsTool {

    private static final String NAME = "allocation_hotspots";

    private final JfrAnalysisService service;

    public AllocationHotspotsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Find memory allocation hotspots and allocation sites in a JFR recording. " +
                                "Reports top allocating classes and their call paths.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top allocation sites to return (default 10)", 10)
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

        Map<AllocationKey, Long> allocationMap = new HashMap<>();

        processAllocations(events, "jdk.ObjectAllocationInNewTLAB", "tlabSize", allocationMap);
        processAllocations(events, "jdk.ObjectAllocationOutsideTLAB", "allocationSize", allocationMap);

        if (allocationMap.isEmpty()) {
            return "# Allocation Hotspots\n\nNo allocation events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Allocation Hotspots\n\n");
        sb.append("| Total Allocated | Class | Allocation Site (top 5 frames) |\n");
        sb.append("|-----------------|-------|--------------------------------|\n");

        allocationMap.entrySet().stream()
                .sorted(Map.Entry.<AllocationKey, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(entry -> {
                    sb.append("| ").append(formatBytes(entry.getValue())).append(" | ");
                    sb.append("`").append(entry.getKey().className).append("` | ");
                    sb.append("`").append(entry.getKey().stackTrace.replace("\n", "`<br>`")).append("` |\n");
                });

        return sb.toString();
    }

    private void processAllocations(IItemCollection events, String typeId, String sizeAttr, Map<AllocationKey, Long> map) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            IMemberAccessor<IQuantity, IItem> sizeAccessor = JfrItemUtils.getAccessor(iterable.getType(), sizeAttr);
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (classAccessor != null && sizeAccessor != null && stackAccessor != null) {
                for (IItem item : iterable) {
                    Object classObj = classAccessor.getMember(item);
                    IQuantity sizeQ = sizeAccessor.getMember(item);
                    Object stackObj = stackAccessor.getMember(item);

                    if (classObj != null && sizeQ != null && stackObj != null) {
                        String className = classObj.toString();
                        String trace = JfrItemUtils.formatStackTrace(stackObj, 5);
                        AllocationKey key = new AllocationKey(className, trace);
                        map.merge(key, sizeQ.clampedLongValueIn(org.openjdk.jmc.common.unit.UnitLookup.BYTE), Long::sum);
                    }
                }
            }
        }
    }

    private record AllocationKey(String className, String stackTrace) {
    }
}
