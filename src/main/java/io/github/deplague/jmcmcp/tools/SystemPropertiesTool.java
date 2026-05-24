package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MCP tool for extracting system properties from a JFR recording.
 */
public final class SystemPropertiesTool {

    private static final String NAME = "system_properties";

    private final JfrAnalysisService service;

    public SystemPropertiesTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("List the JVM and OS system properties captured in a JFR recording.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "filter", SchemaUtil.stringProp("Optional filter for property names (e.g., 'java.vm')")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String filter = SchemaUtil.getStringOrDefault(request.arguments(), "filter", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, filter);
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

    private String analyze(String filePath, String filter) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# System Properties\n\n");

        IItemCollection props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        if (props.hasItems()) {
            sb.append("| Property | Value |\n");
            sb.append("|----------|-------|\n");

            List<IItem> sorted = new ArrayList<>();
            props.forEach(iterable -> iterable.forEach(sorted::add));
            sorted.stream()
                    .filter(item -> {
                        if (filter == null) return true;
                        String key = String.valueOf(JfrItemUtils.getMember(item, "key").orElse(null));
                        return key.contains(filter);
                    })
                    .sorted((a, b) -> {
                        String ka = String.valueOf(JfrItemUtils.getMember(a, "key").orElse(null));
                        String kb = String.valueOf(JfrItemUtils.getMember(b, "key").orElse(null));
                        return ka.compareTo(kb);
                    })
                    .forEach(item -> {
                        Object key = JfrItemUtils.getMember(item, "key").orElse(null);
                        Object val = JfrItemUtils.getMember(item, "value").orElse(null);
                        sb.append(String.format("| %s | %s |%n", key, val));
                    });
        } else {
            sb.append("No system properties found in the recording.\n");
        }

        return sb.toString();
    }
}
