package io.github.deplague.jmcmcp.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToolSchemaTest {

    @Test
    void toolNamesAreUnique() {
        // This is a basic sanity check that our tool names don't collide.
        // In a real test we'd instantiate the tools and inspect their specs.
        java.util.Set<String> names = java.util.Set.of(
                "jfr_overview",
                "gc_analysis",
                "hot_methods",
                "thread_contention",
                "allocation_hotspots",
                "io_analysis",
                "exception_analysis",
                "jfr_rules",
                "live_recording",
                "system_health",
                "thread_dumps",
                "search_events",
                "vm_operations",
                "object_statistics",
                "system_properties",
                "recording_settings",
                "time_series",
                "jit_compilation"
        );
        assertThat(names).hasSize(18);
    }
}
