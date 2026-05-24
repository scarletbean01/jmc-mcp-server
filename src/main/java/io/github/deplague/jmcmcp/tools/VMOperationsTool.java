package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MCP tool for analyzing VM operations and Safepoints.
 */
public final class VMOperationsTool {

    private static final String NAME = "vm_operations";

    private final JfrAnalysisService service;

    public VMOperationsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze VM operations and Safepoints in a JFR recording. " +
                                "Helps identify 'stop-the-world' pauses caused by something other than GC.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath);
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

    private String analyze(String filePath) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# VM Operations & Safepoints\n\n");

        // Safepoints
        var safepoints = events.apply(ItemFilters.type("jdk.SafepointBegin"));
        if (safepoints.hasItems()) {
            sb.append("## Safepoints\n");
            IQuantity count = safepoints.getAggregate(Aggregators.count());
            // Duration is usually in jdk.SafepointEnd or as a duration on jdk.ExecuteVMOperation
            sb.append(String.format("- **Total Safepoints:** %s%n", JfrAnalysisService.display(count)));
            sb.append("\n");
        }

        // VM Operations
        var vmOps = events.apply(ItemFilters.type("jdk.ExecuteVMOperation"));
        if (vmOps.hasItems()) {
            sb.append("## VM Operations\n");
            IQuantity count = vmOps.getAggregate(Aggregators.count());
            IQuantity avgDuration = vmOps.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity maxDuration = vmOps.getAggregate(Aggregators.max(JfrAttributes.DURATION));

            sb.append(String.format("- **Total Operations:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Avg Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append("\n");

            sb.append("### Top VM Operations by Duration\n");
            sb.append("| Operation | Caller | Duration |\n");
            sb.append("|-----------|--------|----------|\n");

            // Sort and list some top operations
            List<IItem> sortedOps = new ArrayList<>();
            vmOps.forEach(iterable -> iterable.forEach(sortedOps::add));

            sortedOps.stream()
                    .sorted((a, b) -> {
                        IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier());
                        IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier());
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(10)
                    .forEach(item -> {
                        Object operation = JfrItemUtils.getMember(item, "operation");
                        Object caller = JfrItemUtils.getMember(item, "caller");
                        IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier());
                        sb.append(String.format("| %s | %s | %s |%n", operation, caller, JfrAnalysisService.display(duration)));
                    });
            sb.append("\n");
        }

        if (!safepoints.hasItems() && !vmOps.hasItems()) {
            sb.append("No VM operation or safepoint events found in this recording.\n");
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }
}
