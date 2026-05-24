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
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

import java.io.IOException;
import java.util.*;

/**
 * MCP tool for exception and error analysis.
 */
public final class ExceptionAnalysisTool {

    private static final String NAME = "exception_analysis";

    private final JfrAnalysisService service;

    public ExceptionAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze exception and error throw events in a JFR recording. " +
                                "Reports the most frequently thrown exception types and stack traces.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "top_n", SchemaUtil.intProp("Number of top exception/error types to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        int topN = getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, topN);
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

    private String analyze(String filePath, int topN) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# Exception / Error Analysis\n\n");

        // Java Exception Throw
        var exceptions = events.apply(ItemFilters.type("jdk.JavaExceptionThrow"));
        if (exceptions.hasItems()) {
            sb.append("## Exceptions Thrown\n");
            IQuantity count = exceptions.getAggregate(Aggregators.count());
            sb.append(String.format("- **Total Exception Events:** %s%n%n", JfrAnalysisService.display(count)));

            Map<String, Integer> typeCounts = new HashMap<>();
            for (var itemIterable : exceptions) {
                IMemberAccessor<Object, IItem> thrownClassAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "thrownClass");
                if (thrownClassAccessor != null) {
                    for (IItem item : itemIterable) {
                        Object thrownClass = thrownClassAccessor.getMember(item);
                        if (thrownClass != null) {
                            String type = thrownClass.toString();
                            typeCounts.merge(type, 1, Integer::sum);
                        }
                    }
                }
            }

            if (!typeCounts.isEmpty()) {
                sb.append("### Top Exception Types\n");
                sb.append("| Exception Type | Count |\n");
                sb.append("|----------------|-------|\n");
                typeCounts.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %d |%n", e.getKey(), e.getValue())));
                sb.append("\n");
            }
        }

        // Java Error Throw
        var errors = events.apply(ItemFilters.type("jdk.JavaErrorThrow"));
        if (errors.hasItems()) {
            sb.append("## Errors Thrown\n");
            IQuantity count = errors.getAggregate(Aggregators.count());
            sb.append(String.format("- **Total Error Events:** %s%n%n", JfrAnalysisService.display(count)));

            Map<String, Integer> typeCounts = new HashMap<>();
            for (var itemIterable : errors) {
                IMemberAccessor<Object, IItem> thrownClassAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "thrownClass");
                if (thrownClassAccessor != null) {
                    for (IItem item : itemIterable) {
                        Object thrownClass = thrownClassAccessor.getMember(item);
                        if (thrownClass != null) {
                            String type = thrownClass.toString();
                            typeCounts.merge(type, 1, Integer::sum);
                        }
                    }
                }
            }

            if (!typeCounts.isEmpty()) {
                sb.append("### Top Error Types\n");
                sb.append("| Error Type | Count |\n");
                sb.append("|------------|-------|\n");
                typeCounts.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(e -> sb.append(String.format("| `%s` | %d |%n", e.getKey(), e.getValue())));
                sb.append("\n");
            }
        }

        if (!exceptions.hasItems() && !errors.hasItems()) {
            sb.append("No exception or error events found in this recording.\n");
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

    private static int getIntOrDefault(Map<String, Object> args, String key, int defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.intValue();
        }
        if (val instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
