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
 * MCP tool for native memory tracking and library analysis.
 */
public final class NativeMemoryTool {

    private static final String NAME = "native_memory";
    private final JfrAnalysisService service;

    public NativeMemoryTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Provide a memory footprint overview including native libraries and direct buffer limits.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }
                        String result = analyze(filePath, startTimeStr, endTimeStr);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# Native Memory Analysis\n\n");

        IItemCollection nativeLibs = events.apply(ItemFilters.type("jdk.NativeLibrary"));
        long libCount = JfrItemUtils.count(nativeLibs);
        
        sb.append("## Memory Configuration\n");
        sb.append("- **Loaded Native Libraries:** ").append(libCount).append("\n");

        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IQuantity maxHeap = JfrItemUtils.maxQuantity(heapSummary, "heapSize");
        if (maxHeap != null) {
            sb.append("- **Max Heap Size Observed:** ").append(JfrAnalysisService.display(maxHeap)).append("\n");
        }
        sb.append("\n");

        IItemCollection props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        Map<String, String> memProps = new HashMap<>();
        for (IItemIterable iterable : props) {
            IMemberAccessor<String, IItem> keyAccessor = JfrItemUtils.getAccessor(iterable.getType(), "key");
            IMemberAccessor<String, IItem> valueAccessor = JfrItemUtils.getAccessor(iterable.getType(), "value");
            if (keyAccessor != null && valueAccessor != null) {
                for (IItem item : iterable) {
                    String key = keyAccessor.getMember(item);
                    if (key != null && (key.toLowerCase().contains("memory") || key.toLowerCase().contains("buffer") || key.toLowerCase().contains("alloc"))) {
                        memProps.put(key, valueAccessor.getMember(item));
                    }
                }
            }
        }
        
        if (!memProps.isEmpty()) {
            sb.append("## Memory-Related System Properties\n");
            sb.append("| Key | Value |\n|---|---|\n");
            memProps.forEach((k, v) -> sb.append("| `").append(k).append("` | `").append(v).append("` |\n"));
            sb.append("\n");
        }

        if (libCount > 0) {
            sb.append("## Loaded Native Libraries (Top 50)\n");
            sb.append("| Library Name | Base Path |\n|---|---|\n");
            int count = 0;
            for (IItemIterable iterable : nativeLibs) {
                IMemberAccessor<String, IItem> nameAccessor = JfrItemUtils.getAccessor(iterable.getType(), "name");
                if (nameAccessor != null) {
                    for (IItem item : iterable) {
                        if (count++ >= 50) break;
                        String name = nameAccessor.getMember(item);
                        String path = JfrItemUtils.getMember(item, "topLevelPath").map(Object::toString).orElse("N/A");
                        sb.append("| `").append(name).append("` | `").append(path).append("` |\n");
                    }
                }
                if (count >= 50) break;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
