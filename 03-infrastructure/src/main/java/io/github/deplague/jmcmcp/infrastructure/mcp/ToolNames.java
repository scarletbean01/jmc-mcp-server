package io.github.deplague.jmcmcp.infrastructure.mcp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Canonical registry of all MCP tool names.
 * Ensures consistent snake_case naming across tool definitions and agent hints.
 */
public final class ToolNames {

    private ToolNames() {}

    public static final String ALLOCATION_FLAME = "allocation_flame";
    public static final String ALLOCATION_HOTSPOTS = "allocation_hotspots";
    public static final String BLOCKING_SUMMARY = "blocking_summary";
    public static final String CALL_TREE = "call_tree";
    public static final String CLASS_HISTOGRAM = "class_histogram";
    public static final String CLASS_LOADING = "class_loading";
    public static final String CODE_CACHE = "code_cache";
    public static final String COMPARE_RECORDINGS = "compare_recordings";
    public static final String CONTAINER_METRICS = "container_metrics";
    public static final String CORRELATE = "correlate";
    public static final String CPU_FLAME = "cpu_flame";
    public static final String DEADLOCK_DETECTION = "deadlock_detection";
    public static final String DIFF_CALL_TREE = "diff_call_tree";
    public static final String DIFF_STACK_TRACES = "diff_stack_traces";
    public static final String DIRECT_BUFFERS = "direct_buffers";
    public static final String ERROR_ANALYSIS = "error_analysis";
    public static final String EVENT_SCHEMA = "event_schema";
    public static final String EXCEPTION_ANALYSIS = "exception_analysis";
    public static final String EXPAND_CALL_TREE = "expand_call_tree";
    public static final String EXPAND_DIFF_CALL_TREE = "expand_diff_call_tree";
    public static final String GC_ANALYSIS = "gc_analysis";
    public static final String GC_CAUSE = "gc_cause";
    public static final String GC_DETAIL = "gc_detail";
    public static final String GC_RECOMMENDATIONS = "gc_recommendations";
    public static final String HEALTH_CHECK = "health_check";
    public static final String HEAP_DUMP_CLASS_HISTOGRAM = "heap_dump_class_histogram";
    public static final String HEAP_DUMP_CROSS_ANALYSIS = "heap_dump_cross_analysis";
    public static final String HEAP_DUMP_DOMINATOR_TREE = "heap_dump_dominator_tree";
    public static final String HEAP_DUMP_REFERENCE_GRAPH = "heap_dump_reference_graph";
    public static final String HEAP_TRENDS = "heap_trends";
    public static final String HIGH_CPU_DIAGNOSTIC = "diagnose_high_cpu";
    public static final String HOT_METHODS = "hot_methods";
    public static final String INCIDENT_TIMELINE = "incident_timeline";
    public static final String IO_ANALYSIS = "io_analysis";
    public static final String IO_HOTSPOTS = "io_hotspots";
    public static final String JDK_BUG_REFERENCE = "jdk_bug_reference";
    public static final String JFR_EVENT_STATS = "jfr_event_stats";
    public static final String JFR_OVERVIEW = "jfr_overview";
    public static final String JFR_RULES = "jfr_rules";
    public static final String JIT_COMPILATION = "jit_compilation";
    public static final String JVM_FLAGS = "jvm_flags";
    public static final String LIVE_RECORDING = "live_recording";
    public static final String LOCK_ANALYSIS = "lock_analysis";
    public static final String LOCK_FLAME = "lock_flame";
    public static final String MEMORY_LEAKS = "memory_leaks";
    public static final String NATIVE_MEMORY = "native_memory";
    public static final String NETWORK_ANALYSIS = "network_analysis";
    public static final String OBJECT_STATISTICS = "object_statistics";
    public static final String PREDICTIVE_LEAK_ANALYSIS = "predictive_leak_analysis";
    public static final String PROCESS_INFO = "process_info";
    public static final String QUICK_ANALYSIS = "quick_analysis";
    public static final String RECORDING_SETTINGS = "recording_settings";
    public static final String REQUEST_WATERFALL = "request_waterfall";
    public static final String SAFEPOINT_ANALYSIS = "safepoint_analysis";
    public static final String SEARCH_EVENTS = "search_events";
    public static final String SMART_JDBC_N_PLUS_ONE_ANALYZER = "smart_jdbc_n_plus_one_analyzer";
    public static final String SMART_LOCK_RESOLVER = "smart_lock_resolver";
    public static final String SMART_THREAD_STARVATION_DETECTOR = "smart_thread_starvation_detector";
    public static final String STACK_TRACE_SEARCH = "stack_trace_search";
    public static final String SYSTEM_HEALTH = "system_health";
    public static final String SYSTEM_PROPERTIES = "system_properties";
    public static final String THREAD_ACTIVITY = "thread_activity";
    public static final String THREAD_ALLOCATION = "thread_allocation";
    public static final String THREAD_CONTENTION = "thread_contention";
    public static final String THREAD_CPU = "thread_cpu";
    public static final String THREAD_DUMP = "thread_dumps";
    public static final String THREAD_POOL_ANALYSIS = "thread_pool_analysis";
    public static final String TIME_SERIES = "time_series";
    public static final String VIRTUAL_THREADS = "virtual_threads";
    public static final String VM_OPERATIONS = "vm_operations";

    public static final Set<String> ALL;

    static {
        Set<String> set = new HashSet<>();
        for (java.lang.reflect.Field field : ToolNames.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isPublic(field.getModifiers())
                    && java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && field.getType() == String.class) {
                try {
                    set.add((String) field.get(null));
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        ALL = Collections.unmodifiableSet(set);
    }
}
