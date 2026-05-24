package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MCP tool for analyzing class loading events and statistics.
 */
public final class ClassLoadingTool {

    private static final String NAME = "class_loading";

    private final JfrAnalysisService service;

    public ClassLoadingTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze class loading events and statistics in a JFR recording. " +
                                "Identifies longest-loading classes and metaspace pressure.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top longest loading classes to return (default 10)", 10)
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

        StringBuilder sb = new StringBuilder();
        sb.append("# Class Loading Analysis\n\n");

        // 1. Longest Class Loads
        IItemCollection classLoadEvents = events.apply(ItemFilters.type("jdk.ClassLoad"));
        if (classLoadEvents.hasItems()) {
            sb.append("## Longest Class Loads\n");
            sb.append("| Class | Duration | Initiating Loader |\n");
            sb.append("|-------|----------|-------------------|\n");

            List<IItem> sortedLoads = new ArrayList<>();
            for (IItemIterable iterable : classLoadEvents) {
                for (IItem item : iterable) {
                    sortedLoads.add(item);
                }
            }

            sortedLoads.stream()
                    .sorted((a, b) -> {
                        IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .forEach(item -> {
                        Object loadedClass = JfrItemUtils.getMember(item, "loadedClass").orElse(null);
                        IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        Object loader = JfrItemUtils.getMember(item, "initiatingClassLoader").orElse(null);
                        sb.append(String.format("| `%s` | %s | %s |%n", loadedClass, JfrAnalysisService.display(duration), loader));
                    });
            sb.append("\n");
        }

        // 2. Class Loading Statistics
        IItemCollection statsEvents = events.apply(ItemFilters.type("jdk.ClassLoadingStatistics"));
        if (statsEvents.hasItems()) {
            sb.append("## Class Loading Statistics\n");
            IQuantity maxLoaded = JfrItemUtils.maxQuantity(statsEvents, "loadedClassCount");
            IQuantity maxUnloaded = JfrItemUtils.maxQuantity(statsEvents, "unloadedClassCount");

            if (maxLoaded != null)
                sb.append(String.format("- **Max Loaded Class Count:** %s%n", JfrAnalysisService.display(maxLoaded)));
            if (maxUnloaded != null)
                sb.append(String.format("- **Max Unloaded Class Count:** %s%n", JfrAnalysisService.display(maxUnloaded)));
            sb.append("\n");
        }

        if (!classLoadEvents.hasItems() && !statsEvents.hasItems()) {
            sb.append("No class loading events found in the recording.\n");
        }

        return sb.toString();
    }
}
