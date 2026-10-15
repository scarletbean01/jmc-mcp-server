package io.github.deplague.jmcmcp.infrastructure.heapdump;

import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeapDumpCacheTest {

    private HeapDumpCache cache;

    @BeforeEach
    void setUp() {
        RecordingAccessController accessController = new RecordingAccessController();
        cache = new HeapDumpCache(30, accessController);
    }

    @Test
    void rejectsPathOutsideAllowlist() {
        assertThatThrownBy(() -> cache.loadSnapshot("/etc/shadow.hprof"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access denied");
    }

    @Test
    void rejectsDisallowedExtensions() {
        assertThatThrownBy(() -> cache.loadSnapshot("test.txt"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("File extension not allowed");
    }

    @Test
    void rejectsTraversalPaths() {
        assertThatThrownBy(() -> cache.loadSnapshot("../../etc/shadow.hprof"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Path traversal detected");
    }
}
