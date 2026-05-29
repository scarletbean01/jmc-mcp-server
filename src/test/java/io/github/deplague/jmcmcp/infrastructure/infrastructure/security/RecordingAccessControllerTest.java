package io.github.deplague.jmcmcp.infrastructure.infrastructure.security;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecordingAccessControllerTest {

    @Test
    void allowsPathInCurrentWorkingDirectory() {
        RecordingAccessController ctrl = new RecordingAccessController();
        // Should not throw for a relative path in the current working directory
        ctrl.validate("before.jfr");
        ctrl.validate("after.jfr");
    }

    @Test
    void allowsAbsolutePathInCurrentWorkingDirectory() {
        RecordingAccessController ctrl = new RecordingAccessController();
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        ctrl.validate(cwd.resolve("before.jfr").toString());
    }

    @Test
    void rejectsPathTraversal() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("../../../etc/passwd"))
                .isInstanceOf(AnalysisFailedException.class)
                .hasMessageContaining("Path traversal");
    }

    @Test
    void rejectsPathTraversalWithUrlEncoding() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("..%2f..%2fetc/passwd"))
                .isInstanceOf(AnalysisFailedException.class)
                .hasMessageContaining("Path traversal");
    }

    @Test
    void rejectsPathTraversalDoubleSlash() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("foo//../etc/passwd"))
                .isInstanceOf(AnalysisFailedException.class)
                .hasMessageContaining("Path traversal");
    }

    @Test
    void rejectsPathOutsideAllowedBase() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("/etc/passwd"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access denied");
    }

    @Test
    void rejectsNullBytes() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("file\0.txt"))
                .isInstanceOf(AnalysisFailedException.class)
                .hasMessageContaining("invalid characters");
    }

    @Test
    void rejectsBlankPath() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("  "))
                .isInstanceOf(AnalysisFailedException.class)
                .hasMessageContaining("blank");
    }

    @Test
    void rejectsNullPath() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate(null))
                .isInstanceOf(AnalysisFailedException.class)
                .hasMessageContaining("null");
    }

    @Test
    void defaultSchemesIncludeFile() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThat(ctrl.getAllowedSchemes()).contains("file");
    }

    @Test
    void allowsFileUriScheme() {
        RecordingAccessController ctrl = new RecordingAccessController();
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        ctrl.validate("file://" + cwd.resolve("before.jfr").toString());
    }

    @Test
    void exposesAllowedBasePaths() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThat(ctrl.getAllowedBasePaths()).isNotEmpty();
    }
}
