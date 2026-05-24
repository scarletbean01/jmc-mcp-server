package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP tool for per-class allocation histogram.
 */
public final class ClassHistogramTool {

    private static final String NAME = "class_histogram";
    private final JfrAnalysisService service;

    public ClassHistogramTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide a class instance allocation histogram and top allocating classes.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top classes (default 20)", 20)
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
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                }).build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        Map<String, ClassAllocStats> stats = new HashMap<>();
        processAllocations(events, "jdk.ObjectAllocationInNewTLAB", stats);
        processAllocations(events, "jdk.ObjectAllocationOutsideTLAB", stats);

        if (stats.isEmpty()) {
            return "# Class Allocation Histogram\n\nNo allocation events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Class Allocation Histogram\n\n");
        sb.append("| Class | Count | Total Bytes | Avg Size |\n");
        sb.append("|-------|-------|-------------|----------|\n");

        stats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().totalBytes, a.getValue().totalBytes))
                .limit(topN)
                .forEach(e -> {
                    ClassAllocStats s = e.getValue();
                    long avg = s.count > 0 ? s.totalBytes / s.count : 0;
                    sb.append("| `").append(e.getKey()).append("` | ").append(s.count).append(" | ")
                      .append(SchemaUtil.formatBytes(s.totalBytes)).append(" | ")
                      .append(SchemaUtil.formatBytes(avg)).append(" |\n");
                });

        return sb.toString();
    }

    private void processAllocations(IItemCollection events, String typeId, Map<String, ClassAllocStats> stats) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "objectClass");
            IMemberAccessor<IQuantity, IItem> allocAccessor = JfrItemUtils.getAccessor(iterable.getType(), "allocationSize");
            if (classAccessor != null && allocAccessor != null) {
                for (IItem item : iterable) {
                    Object clazzObj = classAccessor.getMember(item);
                    IQuantity sizeQ = allocAccessor.getMember(item);
                    if (clazzObj != null && sizeQ != null) {
                        String className = clazzObj.toString();
                        ClassAllocStats s = stats.computeIfAbsent(className, k -> new ClassAllocStats());
                        s.count++;
                        s.totalBytes += sizeQ.longValue();
                    }
                }
            }
        }
    }
    
    private static class ClassAllocStats {
        long count;
        long totalBytes;
    }
}
