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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MCP tool for JVM runtime flags and ergonomics analysis.
 */
public final class JvmFlagsTool {

    private static final String NAME = "jvm_flags";

    private final JfrAnalysisService service;

    public JvmFlagsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze JVM runtime flags and ergonomics (e.g., UseG1GC, CompileThreshold).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "filter", SchemaUtil.stringProp("Substring filter for flag names (e.g., 'GC', 'Compile', 'Heap')")
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
        IItemCollection allEvents = service.loadRecording(filePath);

        List<JvmFlag> flags = new ArrayList<>();
        collectFlags(allEvents, "jdk.BooleanFlag", "boolean", flags);
        collectFlags(allEvents, "jdk.IntFlag", "int", flags);
        collectFlags(allEvents, "jdk.UintFlag", "uint", flags);
        collectFlags(allEvents, "jdk.DoubleFlag", "double", flags);

        if (flags.isEmpty()) {
            return "# JVM Flags Analysis\n\nNo JVM flag events found in the recording.";
        }

        // Sort alphabetically
        flags.sort(Comparator.comparing(f -> f.name));

        StringBuilder sb = new StringBuilder();
        sb.append("# JVM Flags Analysis\n\n");

        if (filter != null && !filter.isBlank()) {
            sb.append("## Filtered Flags (`").append(filter).append("`)\n\n");
            String lowerFilter = filter.toLowerCase();
            List<JvmFlag> filtered = flags.stream().filter(f -> f.name.toLowerCase().contains(lowerFilter)).toList();
            if (filtered.isEmpty()) {
                sb.append("No flags matched the filter.\n");
            } else {
                appendFlagsTable(sb, filtered);
            }
        } else {
            // Unfiltered: show grouped sections
            List<JvmFlag> gcFlags = flags.stream().filter(f -> f.name.contains("GC") || f.name.contains("Heap")).toList();
            List<JvmFlag> compFlags = flags.stream().filter(f -> f.name.contains("Compile") || f.name.contains("Tiered")).toList();
            List<JvmFlag> memFlags = flags.stream().filter(f -> f.name.contains("Memory") || f.name.contains("Heap") || f.name.contains("Metaspace") || f.name.contains("Compressed")).toList();

            sb.append("## GC Configuration\n\n");
            appendFlagsTable(sb, gcFlags);

            sb.append("## Compiler Flags\n\n");
            appendFlagsTable(sb, compFlags);

            sb.append("## Memory Flags\n\n");
            appendFlagsTable(sb, memFlags);

            sb.append("## All Flags\n\n");
            appendFlagsTable(sb, flags);
        }

        return sb.toString();
    }

    private void collectFlags(IItemCollection events, String typeId, String typeName, List<JvmFlag> flags) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            var nameAcc = JfrItemUtils.getAccessor(iterable.getType(), "name");
            var valueAcc = JfrItemUtils.getAccessor(iterable.getType(), "value");

            if (nameAcc != null && valueAcc != null) {
                for (IItem item : iterable) {
                    Object name = nameAcc.getMember(item);
                    Object value = valueAcc.getMember(item);
                    if (name != null && value != null) {
                        flags.add(new JvmFlag(name.toString(), value.toString(), typeName));
                    }
                }
            }
        }
    }

    private void appendFlagsTable(StringBuilder sb, List<JvmFlag> flags) {
        if (flags.isEmpty()) {
            sb.append("No flags found for this category.\n\n");
            return;
        }
        sb.append("| Flag Name | Value | Type |\n");
        sb.append("|-----------|-------|------|\n");
        for (JvmFlag f : flags) {
            sb.append("| `").append(f.name).append("` | ").append(f.value).append(" | ").append(f.type).append(" |\n");
        }
        sb.append("\n");
    }

    private record JvmFlag(String name, String value, String type) {}
}
