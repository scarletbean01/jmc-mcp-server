package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.SystemPropertiesApplicationService;
import io.github.deplague.jmcmcp.domain.model.SystemPropertiesResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for extracting system properties from a JFR recording.
 */
@Slf4j
@HandleToolError
@ApplicationScoped
public final class SystemPropertiesTool {

    private final SystemPropertiesApplicationService applicationService;

    @Inject
    public SystemPropertiesTool(SystemPropertiesApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(description = "List the JVM and OS system properties captured in a JFR recording.")
    public ToolResponse systemProperties(
            @ToolArg(name = "jfr_file_path", description = "Path to JFR file", required = true) String jfrFilePath,
            @ToolArg(name = "filter", description = "Optional filter for property names (e.g., 'java.vm')", required = false) String filter) {
        try {
            SystemPropertiesResult result = applicationService.analyze(jfrFilePath, filter);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(SystemPropertiesResult result) {
        if (!result.hasProperties()) {
            return "# System Properties\n\nNo system properties found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# System Properties\n\n");
        sb.append("| Property | Value |\n");
        sb.append("|----------|-------|\n");

        for (var entry : result.entries()) {
            sb.append(String.format("| %s | %s |%n", entry.key(), entry.value()));
        }

        return sb.toString();
    }
}
