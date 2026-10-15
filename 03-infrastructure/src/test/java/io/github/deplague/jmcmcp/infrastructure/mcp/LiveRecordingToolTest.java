package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.LiveRecordingApplicationService;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingDumpResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingListResult;
import io.github.deplague.jmcmcp.domain.model.LiveRecordingStartResult;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LiveRecordingToolTest {

    private LiveRecordingApplicationService appService;
    private RecordingAccessController accessController;
    private LiveRecordingTool tool;

    @BeforeEach
    void setUp() {
        appService = mock(LiveRecordingApplicationService.class);
        accessController = new RecordingAccessController();
        tool = new LiveRecordingTool(appService, accessController);
    }

    @Test
    void rejectsUnpermittedJmxHost() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://attacker.com:9091/jmxrmi",
                LiveRecordingTool.Action.list, null, null, null, null, null))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("JMX host 'attacker.com' is not permitted");
    }

    @Test
    void rejectsLdapJndiUrl() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/ldap://attacker.com:1389/x",
                LiveRecordingTool.Action.list, null, null, null, null, null))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("JNDI URL scheme must be 'rmi://'");
    }

    @Test
    void rejectsSerializedStubUrl() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi://localhost:9091/stub/rO0ABXNyAA",
                LiveRecordingTool.Action.list, null, null, null, null, null))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("/stub/");
    }

    @Test
    void rejectsUrlWithoutJndiPath() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi://localhost:9091",
                LiveRecordingTool.Action.list, null, null, null, null, null))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void allowsLocalhostJmxHost() throws Exception {
        when(appService.list("service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi"))
                .thenReturn(new LiveRecordingListResult(List.of()));

        ToolResponse response = tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.list, null, null, null, null, null);

        assertThat(response.isError()).isFalse();
    }

    @Test
    void allowsDirectLocalhostJmxUrl() throws Exception {
        when(appService.list("service:jmx:rmi://localhost:9091/jndi/rmi://localhost:9091/jmxrmi"))
                .thenReturn(new LiveRecordingListResult(List.of()));

        ToolResponse response = tool.liveRecording(
                "service:jmx:rmi://localhost:9091/jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.list, null, null, null, null, null);

        assertThat(response.isError()).isFalse();
    }

    @Test
    void respectsCustomAllowedJmxHosts() throws Exception {
        LiveRecordingTool customTool = new LiveRecordingTool(appService, accessController, "custom-host.local,10.0.0.1");
        when(appService.list("service:jmx:rmi:///jndi/rmi://custom-host.local:9091/jmxrmi"))
                .thenReturn(new LiveRecordingListResult(List.of()));

        ToolResponse response = customTool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://custom-host.local:9091/jmxrmi",
                LiveRecordingTool.Action.list, null, null, null, null, null);

        assertThat(response.isError()).isFalse();

        // But localhost is now rejected since it's not in the custom list
        assertThatThrownBy(() -> customTool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.list, null, null, null, null, null))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("JMX host 'localhost' is not permitted");
    }

    @Test
    void validatesSettingsProfile() throws Exception {
        when(appService.start("service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi", "my-rec", 60L, "profile"))
                .thenReturn(new LiveRecordingStartResult(1L, "my-rec", 60L));

        ToolResponse response = tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.start, "my-rec", "profile", 60, null, null);

        assertThat(response.isError()).isFalse();

        // Invalid settings throws IllegalArgumentException
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.start, "my-rec", "invalid-profile", 60, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid settings profile");
    }

    @Test
    void stopRequiresRecordingId() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.stop, null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("recording_id is required");
    }

    @Test
    void dumpRequiresRecordingId() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.dump, null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("recording_id is required");
    }

    @Test
    void rejectsDisallowedDumpPath() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.dump, null, null, null, 1L, "/etc/malicious.jfr"))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void rejectsNonJfrDumpExtension() {
        assertThatThrownBy(() -> tool.liveRecording(
                "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                LiveRecordingTool.Action.dump, null, null, null, 1L, "dump.txt"))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void rejectsOutOfRangeDuration() {
        for (int bad : new int[]{0, -5, 86_401}) {
            assertThatThrownBy(() -> tool.liveRecording(
                    "service:jmx:rmi:///jndi/rmi://localhost:9091/jmxrmi",
                    LiveRecordingTool.Action.start, "r", null, bad, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("duration_seconds");
        }
    }
}
