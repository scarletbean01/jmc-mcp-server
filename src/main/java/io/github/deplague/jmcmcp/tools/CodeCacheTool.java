package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.deplague.jmcmcp.tools.SchemaUtil.formatBytes;

/**
 * MCP tool for Code Cache usage and JIT statistics analysis.
 */
public final class CodeCacheTool {

    private static final String NAME = "code_cache";

    private final JfrAnalysisService service;

    public CodeCacheTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze Code Cache usage and JIT compiler statistics.")
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
                })
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection codeCacheEvents = events.apply(ItemFilters.type("jdk.CodeCacheStatistics"));
        IItemCollection compilerEvents = events.apply(ItemFilters.type("jdk.CompilerStatistics"));

        if (!codeCacheEvents.hasItems() && !compilerEvents.hasItems()) {
            return "# Code Cache & JIT Analysis\n\nNo code cache or compiler statistics found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Code Cache & JIT Analysis\n\n");

        if (codeCacheEvents.hasItems()) {
            sb.append("## Code Cache Segment Usage\n\n");
            sb.append("| Segment | Entry Count | Method Count | Capacity | Unallocated | Utilization |\n");
            sb.append("|---------|-------------|--------------|----------|-------------|-------------|\n");

            // Group by segment and get the latest sample for each
            Map<String, IItem> latestSamples = new HashMap<>();
            for (var iterable : codeCacheEvents) {
                var segmentAcc = JfrItemUtils.getAccessor(iterable.getType(), "codeBlobType");
                if (segmentAcc == null) segmentAcc = JfrItemUtils.getAccessor(iterable.getType(), "segment");
                
                for (IItem item : iterable) {
                    Object segmentObj = segmentAcc != null ? segmentAcc.getMember(item) : "Default";
                    String segment = segmentObj != null ? segmentObj.toString() : "Default";
                    latestSamples.put(segment, item); // Assuming items are ordered or latest overwrites
                }
            }

            for (Map.Entry<String, IItem> entry : latestSamples.entrySet()) {
                IItem item = entry.getValue();
                long entries = JfrItemUtils.getQuantity(item, "entryCount").map(IQuantity::longValue).orElse(0L);
                long methods = JfrItemUtils.getQuantity(item, "methodCount").map(IQuantity::longValue).orElse(0L);
                long reserved = JfrItemUtils.getQuantity(item, "reservedCapacity").map(IQuantity::longValue).orElse(0L);
                long unallocated = JfrItemUtils.getQuantity(item, "unallocatedCapacity").map(IQuantity::longValue).orElse(0L);
                
                if (reserved == 0) {
                   // Fallback to start/end address if reservedCapacity is missing
                   long start = JfrItemUtils.getQuantity(item, "startAddress").map(IQuantity::longValue).orElse(0L);
                   long end = JfrItemUtils.getQuantity(item, "endAddress").map(IQuantity::longValue).orElse(0L);
                   reserved = end - start;
                }

                double util = reserved > 0 ? (1.0 - (double) unallocated / reserved) * 100.0 : 0;
                String utilStr = String.format("%.2f%%", util);
                if (util > 90) utilStr = "**" + utilStr + "** (Warning)";

                sb.append("| ").append(entry.getKey()).append(" | ")
                        .append(entries).append(" | ")
                        .append(methods).append(" | ")
                        .append(formatBytes(reserved)).append(" | ")
                        .append(formatBytes(unallocated)).append(" | ")
                        .append(utilStr).append(" |\n");
            }
            sb.append("\n");
        }

        if (compilerEvents.hasItems()) {
            sb.append("## Compilation Statistics\n\n");
            IQuantity totalCount = JfrItemUtils.maxQuantity(compilerEvents, "compileCount");
            IQuantity peakTime = JfrItemUtils.maxQuantity(compilerEvents, "peakTimeSpent");
            IQuantity totalTime = JfrItemUtils.maxQuantity(compilerEvents, "totalTimeSpent");

            if (totalCount != null) {
                sb.append("- **Total Compilations:** ").append(JfrAnalysisService.display(totalCount)).append("\n");
            }
            if (peakTime != null) {
                sb.append("- **Peak Compilation Time:** ").append(JfrAnalysisService.display(peakTime)).append("\n");
            }
            if (totalTime != null) {
                sb.append("- **Total Compilation Time:** ").append(JfrAnalysisService.display(totalTime)).append("\n");
                if (totalCount != null && totalCount.longValue() > 0) {
                    double avgMs = totalTime.doubleValueIn(org.openjdk.jmc.common.unit.UnitLookup.MILLISECOND) / totalCount.longValue();
                    sb.append("- **Average Compilation Time:** ").append(String.format("%.2f ms", avgMs)).append("\n");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
