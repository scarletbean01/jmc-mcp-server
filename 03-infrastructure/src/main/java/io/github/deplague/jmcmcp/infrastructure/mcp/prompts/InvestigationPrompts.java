package io.github.deplague.jmcmcp.infrastructure.mcp.prompts;

import io.quarkiverse.mcp.server.Prompt;
import io.quarkiverse.mcp.server.PromptArg;
import io.quarkiverse.mcp.server.PromptMessage;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * MCP Prompt templates for standard performance diagnostic workflows.
 */
@ApplicationScoped
public class InvestigationPrompts {

    @Prompt(name = "diagnose_high_cpu", title = "Diagnose High CPU", description = "Workflow prompt to guide CPU profiling and hotspot identification")
    public PromptMessage diagnoseHighCpu(
            @PromptArg(name = "jfr_file_path", description = "Path to the JFR recording file") String jfrFilePath
    ) {
        String content = """
            Please analyze the high CPU usage in the JFR recording: `%s`.
            Recommended diagnostic steps:
            1. Call `system_health` to verify system and process CPU load over time.
            2. Call `thread_cpu` to identify which threads are consuming the most CPU cycles.
            3. Call `hot_methods` to locate the top CPU-consuming methods.
            4. If methods appear to be compiler or GC overhead, check `jit_compilation` and `gc_analysis`.
            5. Call `call_tree` or `cpu_flame` for deep visual call hierarchy.
            """.formatted(jfrFilePath);
        return PromptMessage.withUserRole(content);
    }

    @Prompt(name = "diagnose_memory_leak", title = "Diagnose Memory Leak", description = "Workflow prompt to guide memory leak and heap growth investigation")
    public PromptMessage diagnoseMemoryLeak(
            @PromptArg(name = "jfr_file_path", description = "Path to the JFR recording file") String jfrFilePath
    ) {
        String content = """
            Please analyze memory growth and potential leaks in the JFR recording: `%s`.
            Recommended diagnostic steps:
            1. Call `heap_trends` to inspect live heap occupancy after GCs and detect upward slope.
            2. Call `memory_leaks` to identify leak suspects using Old Object Sample heuristics.
            3. Call `allocation_hotspots` to see which call sites allocate the most memory.
            4. Call `predictive_leak_analysis` to forecast Time-To-OOM.
            """.formatted(jfrFilePath);
        return PromptMessage.withUserRole(content);
    }

    @Prompt(name = "diagnose_lock_contention", title = "Diagnose Lock Contention", description = "Workflow prompt to guide lock contention and thread starvation diagnosis")
    public PromptMessage diagnoseLockContention(
            @PromptArg(name = "jfr_file_path", description = "Path to the JFR recording file") String jfrFilePath
    ) {
        String content = """
            Please analyze thread contention and synchronization bottlenecks in: `%s`.
            Recommended diagnostic steps:
            1. Call `thread_contention` to list the most contested locks and total wait time.
            2. Call `deadlock_detection` to ensure no active deadlocks exist.
            3. Call `smart_lock_resolver` to pinpoint contending threads and culprit holding threads.
            4. Call `lock_flame` to inspect contention call trees.
            """.formatted(jfrFilePath);
        return PromptMessage.withUserRole(content);
    }
}
