package io.github.deplague.jmcmcp.infrastructure.infrastructure.security;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.infrastructure.security.PathAccessDeniedException;
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
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Path traversal");
    }

    @Test
    void rejectsPathTraversalWithUrlEncoding() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("..%2f..%2fetc/passwd"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Path traversal");
    }

    @Test
    void rejectsPathTraversalDoubleSlash() {
        RecordingAccessController ctrl = new RecordingAccessController();
        assertThatThrownBy(() -> ctrl.validate("foo//../etc/passwd"))
                .isInstanceOf(SecurityException.class)
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
                .isInstanceOf(SecurityException.class)
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

    @Test
    void validatesAllowedExtensions() {
        RecordingAccessController ctrl = new RecordingAccessController();
        ctrl.validate("before.jfr", java.util.Set.of(".jfr"));

        assertThatThrownBy(() -> ctrl.validate("before.jfr", java.util.Set.of(".hprof")))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("File extension not allowed");
    }

    @Test
    void rejectsSymlinkPointingOutsideAllowedBase() throws Exception {
        java.nio.file.Path outsideFile = java.nio.file.Files.createTempFile("secret", ".jfr");
        java.nio.file.Path linkInside = java.nio.file.Paths.get("target").resolve("test-symlink-" + System.nanoTime() + ".jfr");
        java.nio.file.Files.deleteIfExists(linkInside);
        try {
            try {
                java.nio.file.Files.createSymbolicLink(linkInside, outsideFile);
            } catch (UnsupportedOperationException | SecurityException e) {
                org.junit.jupiter.api.Assumptions.abort("Filesystem does not support symlinks");
            }
            RecordingAccessController ctrl = new RecordingAccessController();
            assertThatThrownBy(() -> ctrl.validate(linkInside.toString()))
                    .isInstanceOf(SecurityException.class)
                    .hasMessageContaining("Access denied");
        } finally {
            java.nio.file.Files.deleteIfExists(linkInside);
            java.nio.file.Files.deleteIfExists(outsideFile);
        }
    }

    @Test
    void rejectsSymlinkDirForNonExistingFilePointingOutsideAllowedBase() throws Exception {
        java.nio.file.Path outsideDir = java.nio.file.Files.createTempDirectory("secret-dir");
        java.nio.file.Path linkDirInside = java.nio.file.Paths.get("target").resolve("test-symlink-dir-" + System.nanoTime());
        java.nio.file.Files.deleteIfExists(linkDirInside);
        try {
            try {
                java.nio.file.Files.createSymbolicLink(linkDirInside, outsideDir);
            } catch (UnsupportedOperationException | SecurityException e) {
                org.junit.jupiter.api.Assumptions.abort("Filesystem does not support symlinks");
            }
            RecordingAccessController ctrl = new RecordingAccessController();
            java.nio.file.Path nonExistingInsideSymlink = linkDirInside.resolve("new-recording.jfr");
            assertThatThrownBy(() -> ctrl.validate(nonExistingInsideSymlink.toString(), java.util.Set.of(".jfr")))
                    .isInstanceOf(SecurityException.class)
                    .hasMessageContaining("Access denied");
        } finally {
            java.nio.file.Files.deleteIfExists(linkDirInside);
            java.nio.file.Files.deleteIfExists(outsideDir);
        }
    }
    @Test
    void rejectsDanglingSymlinkPointingOutsideAllowedBase() throws Exception {
        java.nio.file.Path link = java.nio.file.Paths.get("target").resolve("test-dangling-" + System.nanoTime() + ".jfr");
        java.nio.file.Path missingOutside = java.nio.file.Paths.get(
                System.getProperty("java.io.tmpdir"), "dangling-target-" + System.nanoTime() + ".jfr");
        java.nio.file.Files.deleteIfExists(link);
        try {
            try {
                java.nio.file.Files.createSymbolicLink(link, missingOutside);
            } catch (UnsupportedOperationException | SecurityException e) {
                org.junit.jupiter.api.Assumptions.abort("Filesystem does not support symlinks");
            }
            RecordingAccessController ctrl = new RecordingAccessController();
            assertThatThrownBy(() -> ctrl.validate(link.toString(), java.util.Set.of(".jfr")))
                    .isInstanceOf(PathAccessDeniedException.class);
        } finally {
            java.nio.file.Files.deleteIfExists(link);
        }
    }

    @Test
    void rejectsSymlinkWhoseTargetHasDisallowedExtension() throws Exception {
        java.nio.file.Path realFile = java.nio.file.Files.createTempFile(java.nio.file.Paths.get("target"), "real", ".txt");
        java.nio.file.Path link = java.nio.file.Paths.get("target").resolve("test-ext-link-" + System.nanoTime() + ".jfr");
        java.nio.file.Files.deleteIfExists(link);
        try {
            try {
                java.nio.file.Files.createSymbolicLink(link, realFile.toAbsolutePath());
            } catch (UnsupportedOperationException | SecurityException e) {
                org.junit.jupiter.api.Assumptions.abort("Filesystem does not support symlinks");
            }
            RecordingAccessController ctrl = new RecordingAccessController();
            assertThatThrownBy(() -> ctrl.validate(link.toString(), java.util.Set.of(".jfr")))
                    .isInstanceOf(SecurityException.class)
                    .hasMessageContaining("File extension not allowed");
        } finally {
            java.nio.file.Files.deleteIfExists(link);
            java.nio.file.Files.deleteIfExists(realFile);
        }
    }

    @Test
    void allowsNewFileInsideAllowedBase() {
        RecordingAccessController ctrl = new RecordingAccessController();
        ctrl.validate("target/brand-new-recording.jfr", java.util.Set.of(".jfr"));
    }

    /** Two-step dangling-symlink escapes that a textual normalize() of the link target would miss. */
    @Test
    void rejectsDanglingSymlinkWithRelativeTargetThroughSymlinkedDirectory() throws Exception {
        java.nio.file.Path outside = java.nio.file.Files.createTempDirectory("escape-out");
        java.nio.file.Path inner = java.nio.file.Files.createDirectories(outside.resolve("a"));
        java.nio.file.Path dirLink = java.nio.file.Paths.get("target").resolve("rl-" + System.nanoTime());
        java.nio.file.Path danglingInOutside = inner.resolve("x.jfr");
        try {
            try {
                java.nio.file.Files.createSymbolicLink(dirLink, inner.toAbsolutePath());
                java.nio.file.Files.createSymbolicLink(danglingInOutside, java.nio.file.Paths.get("../y.jfr"));
            } catch (UnsupportedOperationException | SecurityException e) {
                org.junit.jupiter.api.Assumptions.abort("Filesystem does not support symlinks");
            }
            RecordingAccessController ctrl = new RecordingAccessController();
            assertThatThrownBy(() -> ctrl.validate(dirLink.resolve("x.jfr").toString(), java.util.Set.of(".jfr")))
                    .isInstanceOf(PathAccessDeniedException.class);
        } finally {
            java.nio.file.Files.deleteIfExists(danglingInOutside);
            java.nio.file.Files.deleteIfExists(dirLink);
            java.nio.file.Files.deleteIfExists(inner);
            java.nio.file.Files.deleteIfExists(outside);
        }
    }

    @Test
    void rejectsDanglingSymlinkWhoseDotDotCrossesAnotherSymlink() throws Exception {
        java.nio.file.Path outside = java.nio.file.Files.createTempDirectory("escape-out");
        java.nio.file.Path deep = java.nio.file.Files.createDirectories(outside.resolve("a").resolve("deep"));
        long id = System.nanoTime();
        java.nio.file.Path dirLink = java.nio.file.Paths.get("target").resolve("rs-" + id);
        java.nio.file.Path dangling = java.nio.file.Paths.get("target").resolve("rx-" + id + ".jfr");
        try {
            try {
                java.nio.file.Files.createSymbolicLink(dirLink, deep.toAbsolutePath());
                java.nio.file.Files.createSymbolicLink(dangling, java.nio.file.Paths.get(dirLink.getFileName() + "/../z.jfr"));
            } catch (UnsupportedOperationException | SecurityException e) {
                org.junit.jupiter.api.Assumptions.abort("Filesystem does not support symlinks");
            }
            RecordingAccessController ctrl = new RecordingAccessController();
            assertThatThrownBy(() -> ctrl.validate(dangling.toString(), java.util.Set.of(".jfr")))
                    .isInstanceOf(PathAccessDeniedException.class);
        } finally {
            java.nio.file.Files.deleteIfExists(dangling);
            java.nio.file.Files.deleteIfExists(dirLink);
            java.nio.file.Files.deleteIfExists(deep);
            java.nio.file.Files.deleteIfExists(deep.getParent());
            java.nio.file.Files.deleteIfExists(outside);
        }
    }

    @Test
    void returnsTheCanonicalPathThatWasChecked() {
        RecordingAccessController ctrl = new RecordingAccessController();
        java.nio.file.Path checked = ctrl.validate("target/not-yet-created.jfr", java.util.Set.of(".jfr"));
        assertThat(checked.isAbsolute()).isTrue();
        assertThat(checked.endsWith(java.nio.file.Paths.get("target", "not-yet-created.jfr"))).isTrue();
    }
}
