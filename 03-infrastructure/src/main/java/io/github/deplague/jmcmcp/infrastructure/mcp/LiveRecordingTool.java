package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.LiveRecordingApplicationService;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingDumpResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingInfo;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingListResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStartResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStopResult;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.management.remote.JMXServiceURL;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MCP tool adapter for managing live JFR recordings on a running JVM via JMX.
 * Delegates to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class LiveRecordingTool {

    public enum Action {
        list, start, stop, dump
    }

    private static final long MAX_DURATION_SECONDS = 86_400;

    private static final Set<String> DEFAULT_ALLOWED_HOSTS = Set.of("localhost", "127.0.0.1", "[::1]", "::1");

    private final LiveRecordingApplicationService appService;
    private final RecordingAccessController accessController;
    private final String allowedJmxHostsConfig;

    @Inject
    public LiveRecordingTool(
            LiveRecordingApplicationService appService,
            RecordingAccessController accessController,
            @ConfigProperty(name = "jmc.live.allowed-jmx-hosts", defaultValue = "localhost,127.0.0.1,[::1],::1") String allowedJmxHostsConfig) {
        this.appService = appService;
        this.accessController = accessController;
        this.allowedJmxHostsConfig = allowedJmxHostsConfig;
    }

    public LiveRecordingTool(LiveRecordingApplicationService appService, RecordingAccessController accessController) {
        this(appService, accessController, "localhost,127.0.0.1,[::1],::1");
    }

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.LIVE_RECORDING,
            title = "Live JFR Recording Manager",
            description = "Connect to a running JVM via JMX and manage JFR recordings. Supports listing existing recordings, starting a new recording, stopping a recording, and dumping a recording to a file. Requires the target JVM to have JFR enabled and JMX accessible.",
            annotations = @Tool.Annotations(destructiveHint = true, openWorldHint = true)
    )
    public ToolResponse liveRecording(
            @ToolArg(name = "jmx_url", description = "JMX service URL, e.g. service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi") String jmxUrl,
            @ToolArg(name = "action", description = "Action to perform: list, start, stop, dump") Action action,
            @ToolArg(name = "recording_name", required = false, description = "Name for the recording (start action only)") String recordingName,
            @ToolArg(name = "settings", required = false, description = "Recording settings profile: default or profile (start action only)") String settings,
            @ToolArg(name = "duration_seconds", required = false, defaultValue = "60", description = "Recording duration in seconds, 1-86400 (start action only, default 60)") Integer durationSeconds,
            @ToolArg(name = "recording_id", required = false, description = "Recording ID (stop and dump actions)") Long recordingId,
            @ToolArg(name = "output_path", required = false, description = "Output file path for dump action (must be .jfr)") String outputPath
    ) throws Exception {
        if (action == null) {
            throw new IllegalArgumentException("Action must be specified: list, start, stop, or dump.");
        }

        validateJmxUrl(jmxUrl);

        if (settings != null && !settings.isBlank()) {
            String s = settings.trim().toLowerCase();
            if (!s.equals("default") && !s.equals("profile")) {
                throw new IllegalArgumentException("Invalid settings profile: '" + settings + "'. Supported: default, profile.");
            }
        }

        String result = switch (action) {
            case list -> list(jmxUrl);
            case start -> start(jmxUrl, recordingName, durationSeconds, settings);
            case stop -> stop(jmxUrl, recordingId);
            case dump -> dump(jmxUrl, recordingId, outputPath);
        };
        return ToolResponse.success(ToolOutput.limit(result));
    }

    private Set<String> getAllowedHosts() {
        if (allowedJmxHostsConfig == null || allowedJmxHostsConfig.isBlank()) {
            return DEFAULT_ALLOWED_HOSTS;
        }
        return Arrays.stream(allowedJmxHostsConfig.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private void validateJmxUrl(String jmxUrl) {
        if (jmxUrl == null || jmxUrl.isBlank()) {
            throw new IllegalArgumentException("jmx_url cannot be null or blank");
        }
        try {
            JMXServiceURL url = new JMXServiceURL(jmxUrl);
            if (!"rmi".equalsIgnoreCase(url.getProtocol())) {
                throw new SecurityException("JMX protocol '" + url.getProtocol() + "' is not permitted. Only 'rmi' is allowed.");
            }
            Set<String> allowedHosts = getAllowedHosts();
            String host = url.getHost();
            if (host != null && !host.isBlank()) {
                String cleanHost = host.toLowerCase().replaceAll("[\\[\\]]", "");
                boolean match = allowedHosts.stream().anyMatch(h -> h.replaceAll("[\\[\\]]", "").equalsIgnoreCase(cleanHost));
                if (!match) {
                    throw new SecurityException("JMX host '" + host + "' is not permitted");
                }
            }

            String urlPath = url.getURLPath();
            if (urlPath != null && urlPath.startsWith("/jndi/")) {
                String jndiUrl = urlPath.substring("/jndi/".length());
                if (!jndiUrl.toLowerCase().startsWith("rmi://")) {
                    throw new SecurityException("JNDI URL scheme must be 'rmi://': " + jndiUrl);
                }
                URI jndiUri = URI.create(jndiUrl);
                String jndiHost = jndiUri.getHost();
                if (jndiHost == null) {
                    throw new SecurityException("JNDI URL does not specify a host: " + jndiUrl);
                }
                String cleanJndiHost = jndiHost.toLowerCase().replaceAll("[\\[\\]]", "");
                boolean match = allowedHosts.stream().anyMatch(h -> h.replaceAll("[\\[\\]]", "").equalsIgnoreCase(cleanJndiHost));
                if (!match) {
                    throw new SecurityException("JMX host '" + jndiHost + "' is not permitted");
                }
            } else {
                // Any other path (notably "/stub/<base64>") carries a serialized RMI stub whose real
                // endpoint is opaque to this check and is deserialized by the JMX client.
                throw new SecurityException(
                        "JMX URL path must be of the form /jndi/rmi://<host>:<port>/<name>; other forms such as /stub/ are not permitted.");
            }
        } catch (IllegalArgumentException | SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed JMX service URL: " + jmxUrl, e);
        }
    }

    private String list(String jmxUrl) throws Exception {
        LiveRecordingListResult result = appService.list(jmxUrl);

        if (result.isEmpty()) {
            return "No active JFR recordings found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Active JFR Recordings\n\n");
        sb.append("| ID | Name | State | Duration |\n");
        sb.append("|----|------|-------|----------|\n");

        for (LiveRecordingInfo info : result.recordings()) {
            sb.append(String.format(
                    "| %d | %s | %s | %s |%n",
                    info.id(), info.name(), info.state(), info.duration()
            ));
        }

        return sb.toString();
    }

    private String start(String jmxUrl, String recordingName, Integer durationSeconds, String settings) throws Exception {
        String name = recordingName != null ? recordingName : "mcp-recording-" + System.currentTimeMillis();
        long duration = durationSeconds != null ? durationSeconds : 60;
        if (duration < 1 || duration > MAX_DURATION_SECONDS) {
            throw new IllegalArgumentException("duration_seconds must be between 1 and " + MAX_DURATION_SECONDS + ".");
        }

        LiveRecordingStartResult result = appService.start(jmxUrl, name, duration, settings);
        return String.format(
                "Started JFR recording **%s** (ID: %d) for %d seconds.",
                result.name(),
                result.recordingId(),
                result.durationSeconds()
        );
    }

    private String stop(String jmxUrl, Long recordingId) throws Exception {
        if (recordingId == null || recordingId < 0) {
            throw new IllegalArgumentException("recording_id is required for stop action.");
        }

        LiveRecordingStopResult result = appService.stop(jmxUrl, recordingId);
        return String.format("Stopped JFR recording ID %d.", result.recordingId());
    }

    private String dump(String jmxUrl, Long recordingId, String outputPath) throws Exception {
        if (recordingId == null || recordingId < 0) {
            throw new IllegalArgumentException("recording_id is required for dump action.");
        }
        String path;
        if (outputPath != null && !outputPath.isBlank()) {
            Path p = Paths.get(outputPath);
            path = p.isAbsolute() ? p.normalize().toString()
                    : accessController.getAllowedBasePaths().getFirst().resolve(p).normalize().toString();
        } else {
            path = accessController.getAllowedBasePaths().getFirst()
                    .resolve("recording-" + recordingId + "-" + System.currentTimeMillis() + ".jfr")
                    .normalize().toString();
        }
        Path checked = accessController.validate(path, Set.of(".jfr"));

        LiveRecordingDumpResult result = appService.dump(jmxUrl, recordingId, checked != null ? checked.toString() : path);
        return String.format(
                "Dumped JFR recording ID %d to **%s**.",
                result.recordingId(),
                result.path()
        );
    }
}
