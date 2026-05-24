package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * MCP tool for listing JVM system properties captured in a JFR recording.
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
                        .description("List JVM system properties and environment variables captured in a JFR recording. " +
                                "Helps understand the environment and configuration of the JVM at the time of recording.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "filter", SchemaUtil.stringProp("Optional glob-like filter for property keys (e.g., 'java.*')")
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

        // System Properties
        var props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        if (props.hasItems()) {
            sb.append("# System Properties\n\n");
            Map<String, String> sortedProps = new TreeMap<>();
            for (var iterable : props) {
                for (var item : iterable) {
                    String key = JfrItemUtils.getMember(item, "key");
                    String value = JfrItemUtils.getMember(item, "value");
                    if (key != null && (filter == null || key.contains(filter.replace("*", "")))) {
                        sortedProps.put(key, value);
                    }
                }
            }
            if (sortedProps.isEmpty()) {
                sb.append("No properties matching filter '").append(filter).append("'.\n");
            } else {
                sb.append("| Key | Value |\n");
                sb.append("|-----|-------|\n");
                sortedProps.forEach((k, v) -> sb.append(String.format("| `%s` | `%s` |%n", k, v)));
            }
            sb.append("\n");
        }

        // Environment Variables
        var envVars = events.apply(ItemFilters.type("jdk.InitialEnvironmentVariable"));
        if (envVars.hasItems()) {
            sb.append("# Environment Variables\n\n");
            Map<String, String> sortedEnv = new TreeMap<>();
            for (var iterable : envVars) {
                for (var item : iterable) {
                    String key = JfrItemUtils.getMember(item, "key");
                    String value = JfrItemUtils.getMember(item, "value");
                    if (key != null && (filter == null || key.contains(filter.replace("*", "")))) {
                        sortedEnv.put(key, value);
                    }
                }
            }
            if (sortedEnv.isEmpty()) {
                sb.append("No environment variables matching filter '").append(filter).append("'.\n");
            } else {
                sb.append("| Variable | Value |\n");
                sb.append("|----------|-------|\n");
                sortedEnv.forEach((k, v) -> sb.append(String.format("| `%s` | `%s` |%n", k, v)));
            }
            sb.append("\n");
        }

        if (!props.hasItems() && !envVars.hasItems()) {
            sb.append("No system properties or environment variables found in this recording.\n");
        }

        return sb.toString();
    }


}
