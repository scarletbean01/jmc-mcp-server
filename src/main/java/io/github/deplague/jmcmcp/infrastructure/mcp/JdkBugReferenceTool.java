package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.JdkBugReferenceApplicationService;
import io.github.deplague.jmcmcp.domain.model.JdkBugReferenceResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JDK bug cross-reference analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class JdkBugReferenceTool {

    private final JdkBugReferenceApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Cross-reference JFR events against known JDK bug signatures. Matches CompilerFailure, InternalError, and other events to known OpenJDK bugs and provides version-specific workarounds.")
    public ToolResponse jdkBugReference(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime
    ) {
        try {
            JdkBugReferenceResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(JdkBugReferenceResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JDK Bug Cross-Reference\n\n");

        result.jvmVersion().ifPresent(v -> sb.append("**JVM Version:** ").append(v).append("\n\n"));

        if (!result.hasMatches()) {
            sb.append("## ✅ No Known JDK Bug Matches\n\n");
            sb.append("No events in this recording match known JDK bug signatures.\n\n");
            sb.append("Analyzed:\n");
            sb.append("- Compiler failures: ").append(result.compilerFailureCount()).append("\n");
            sb.append("- Java errors: ").append(result.errorCount()).append("\n");
            sb.append("- Biased lock revocations: ").append(result.biasedLockRevocationCount()).append("\n");
            return sb.toString();
        }

        sb.append("## ⚠️ ").append(result.matches().size())
                .append(" Potential JDK Bug Match").append(result.matches().size() > 1 ? "es" : "").append("\n\n");

        for (var match : result.matches()) {
            var bug = match.bug();
            sb.append("### ").append(bug.id()).append("\n\n");
            sb.append("| Field | Value |\n");
            sb.append("|-------|-------|\n");
            sb.append("| Severity | ").append(bug.severity()).append(" |\n");
            sb.append("| Category | ").append(bug.category()).append(" |\n");
            sb.append("| Matched Pattern | `").append(match.matchedText()).append("` |\n");
            sb.append("| Affected Versions | ").append(String.join(", ", bug.affectedVersions())).append(" |\n");
            if (bug.fixedIn() != null) {
                sb.append("| Fixed In | ").append(bug.fixedIn()).append(" |\n");
            }
            if (match.versionAffected()) {
                sb.append("| **Your Version** | ⚠️ **AFFECTED** |\n");
            } else if (result.jvmVersion().isPresent()) {
                sb.append("| **Your Version** | Likely not affected |\n");
            }
            if (bug.workaround() != null) {
                sb.append("| Workaround | `").append(bug.workaround()).append("` |\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
