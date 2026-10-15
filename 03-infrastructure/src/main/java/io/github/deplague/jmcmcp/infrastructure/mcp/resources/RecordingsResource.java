package io.github.deplague.jmcmcp.infrastructure.mcp.resources;

import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Path;

/**
 * MCP Resource providing discoverable JFR recordings under allowed paths.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public class RecordingsResource {

    private final RecordingAccessController accessController;

    @Resource(uri = "mcp-jmc://recordings", name = "Available JFR Recordings", description = "Lists discoverable JFR recording files under configured allowed paths")
    public String listRecordings() {
        StringBuilder sb = new StringBuilder("# Discoverable Recordings\n\n");
        sb.append("Configured allowed base paths:\n");
        for (Path base : accessController.getAllowedBasePaths()) {
            sb.append("- `").append(base).append("`\n");
            File dir = base.toFile();
            if (dir.exists() && dir.isDirectory()) {
                File[] jfrFiles = dir.listFiles((_, name) -> name.toLowerCase().endsWith(".jfr"));
                if (jfrFiles != null && jfrFiles.length > 0) {
                    for (File f : jfrFiles) {
                        sb.append("  - `").append(f.getName()).append("` (").append(f.length() / 1024).append(" KB)\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}
