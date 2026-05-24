package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP tool for JIT compilation and Deoptimization analysis.
 */
public final class JitCompilationTool {

    private static final String NAME = "jit_compilation";

    private final JfrAnalysisService service;

    public JitCompilationTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze JIT compilation and deoptimization events in a JFR recording. " +
                                "Identifies frequent deoptimizations, compilation failures, and longest-running compilations.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top methods to return (default 10)", 10)
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
        sb.append("# JIT Compilation & Deoptimization Analysis\n\n");

        // 1. Compilations
        var compilations = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.Compilation"));
        if (compilations.hasItems()) {
            sb.append("## JIT Compilations\n");
            IQuantity totalCount = compilations.getAggregate(Aggregators.count());
            IQuantity avgDuration = compilations.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity maxDuration = JfrItemUtils.maxQuantity(compilations, JfrAttributes.DURATION.getIdentifier());

            sb.append(String.format("- **Total Compilations:** %s%n", JfrAnalysisService.display(totalCount)));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append("\n");

            sb.append("### Longest Compilations\n");
            sb.append("| Method | Duration | Level |\n");
            sb.append("|--------|----------|-------|\n");

            List<IItem> sortedComp = new ArrayList<>();
            compilations.forEach(iterable -> iterable.forEach(sortedComp::add));
            sortedComp.stream()
                    .sorted((a, b) -> {
                        IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .forEach(item -> {
                        Object method = JfrItemUtils.getMember(item, "method").orElse(null);
                        IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        Object level = JfrItemUtils.getMember(item, "compilationId").orElse(null); // JMC often labels level here or separate
                        sb.append(String.format("| `%s` | %s | %s |%n", method, JfrAnalysisService.display(duration), level));
                    });
            sb.append("\n");
        }

        // 2. Deoptimizations
        var deopts = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.Deoptimization"));
        if (deopts.hasItems()) {
            sb.append("## Deoptimizations\n");
            IQuantity totalCount = deopts.getAggregate(Aggregators.count());
            sb.append(String.format("- **Total Deoptimizations:** %s%n", JfrAnalysisService.display(totalCount)));
            sb.append("\n");

            Map<String, Integer> methodDeopts = new HashMap<>();
            for (var itemIterable : deopts) {
                IMemberAccessor<Object, IItem> methodAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "method");
                if (methodAccessor != null) {
                    for (IItem item : itemIterable) {
                        Object method = methodAccessor.getMember(item);
                        if (method != null) {
                            methodDeopts.merge(method.toString(), 1, Integer::sum);
                        }
                    }
                }
            }

            if (!methodDeopts.isEmpty()) {
                sb.append("### Top Deoptimized Methods\n");
                sb.append("| Method | Count |\n");
                sb.append("|--------|-------|\n");
                methodDeopts.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %d |%n", e.getKey(), e.getValue())));
                sb.append("\n");
            }
        }

        // 3. Compiler Failures
        var failures = events.apply(org.openjdk.jmc.common.item.ItemFilters.type("jdk.CompilerFailure"));
        if (failures.hasItems()) {
            sb.append("## Compiler Failures\n");
            sb.append("| Method | Message |\n");
            sb.append("|--------|---------|\n");
            for (var itemIterable : failures) {
                IMemberAccessor<Object, IItem> methodAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "method");
                IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "failureMessage");
                if (methodAccessor != null && msgAccessor != null) {
                    for (IItem item : itemIterable) {
                        sb.append(String.format("| `%s` | %s |%n", methodAccessor.getMember(item), msgAccessor.getMember(item)));
                    }
                }
            }
            sb.append("\n");
        }

        if (!compilations.hasItems() && !deopts.hasItems() && !failures.hasItems()) {
            sb.append("No JIT compilation or deoptimization events found.\n");
        }

        return sb.toString();
    }


}
