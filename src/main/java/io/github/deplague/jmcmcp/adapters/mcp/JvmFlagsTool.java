package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JvmFlagEntry;
import io.github.deplague.jmcmcp.domain.model.JvmFlagsResult;
import io.github.deplague.jmcmcp.domain.service.JvmFlagsService;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;

/**
 * MCP tool adapter for analyzing JVM runtime flags and ergonomics.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class JvmFlagsTool implements McpTool {

    private static final String NAME = "jvm_flags";

    private final JfrProvider jfrProvider;
    private final JvmFlagsService jvmFlagsService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze JVM runtime flags and ergonomics (e.g., UseG1GC, CompileThreshold)."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "filter",
                                                        SchemaUtil.stringProp(
                                                                "Substring filter for flag names (e.g., 'GC', 'Compile', 'Heap')"
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String filter = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "filter",
                                null
                        );

                        IItemCollection events = jfrProvider.loadRecording(filePath);
                        JvmFlagsResult result = jvmFlagsService.analyze(events, filter);
                        String markdown = formatMarkdown(result, filter);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String formatMarkdown(JvmFlagsResult result, String filter) {
        if (!result.hasFlags()) {
            return "# JVM Flags Analysis\n\nNo JVM flag events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# JVM Flags Analysis\n\n");

        List<JvmFlagEntry> flags = result.flags();

        if (filter != null && !filter.isBlank()) {
            sb.append("## Filtered Flags (`").append(filter).append("`)\n\n");
            if (flags.isEmpty()) {
                sb.append("No flags matched the filter.\n");
            } else {
                appendFlagsTable(sb, flags);
            }
        } else {
            List<JvmFlagEntry> gcFlags = flags.stream()
                    .filter(f -> f.name().contains("GC") || f.name().contains("Heap"))
                    .toList();
            List<JvmFlagEntry> compFlags = flags.stream()
                    .filter(f -> f.name().contains("Compile") || f.name().contains("Tiered"))
                    .toList();
            List<JvmFlagEntry> memFlags = flags.stream()
                    .filter(f -> f.name().contains("Memory") || f.name().contains("Heap")
                            || f.name().contains("Metaspace") || f.name().contains("Compressed"))
                    .toList();

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

    private void appendFlagsTable(StringBuilder sb, List<JvmFlagEntry> flags) {
        if (flags.isEmpty()) {
            sb.append("No flags found for this category.\n\n");
            return;
        }
        sb.append("| Flag Name | Value | Type |\n");
        sb.append("|-----------|-------|------|\n");
        for (JvmFlagEntry f : flags) {
            sb.append("| `").append(f.name()).append("` | ")
                    .append(f.value()).append(" | ")
                    .append(f.type()).append(" |\n");
        }
        sb.append("\n");
    }
}
