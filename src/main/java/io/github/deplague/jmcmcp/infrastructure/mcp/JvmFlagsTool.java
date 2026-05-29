package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.port.JfrProvider;
import io.github.deplague.jmcmcp.domain.model.JvmFlagEntry;
import io.github.deplague.jmcmcp.domain.model.JvmFlagsResult;
import io.github.deplague.jmcmcp.domain.service.JvmFlagsService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItemCollection;

import java.util.List;

/**
 * MCP tool adapter for analyzing JVM runtime flags and ergonomics.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class JvmFlagsTool {

    private final JfrProvider jfrProvider;
    private final JvmFlagsService jvmFlagsService;

    @RunOnVirtualThread
    @Tool(description = "Analyze JVM runtime flags and ergonomics (e.g., UseG1GC, CompileThreshold).")
    public ToolResponse jvmFlags(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "filter", required = false, description = "Substring filter for flag names (e.g., 'GC', 'Compile', 'Heap')") String filter
    ) {
        try {
            IItemCollection events = jfrProvider.loadRecording(jfrFilePath);
            JvmFlagsResult result = jvmFlagsService.analyze(events, filter);
            String markdown = formatMarkdown(result, filter);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
