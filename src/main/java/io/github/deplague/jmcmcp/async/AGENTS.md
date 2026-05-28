# Package: io.github.deplague.jmcmcp.async

This package manages asynchronous job execution for heavyweight analysis tasks.

## Responsibilities
- **Lifecycle Management:** Transitioning jobs through PENDING -> RUNNING -> COMPLETED/FAILED.
- **Polling Support:** Used by `get_job_status` and `get_job_result` tools.
- **Resource Cleanup:** Automatic removal of completed jobs after 1 hour.

## Tools Supporting Async
The following tools can be executed asynchronously by passing `async: true`:
- `smart_stack_trace_search`, `smart_quick_analysis`, `smart_correlate`
- `smart_request_waterfall`, `smart_diff_stack_traces`, `smart_compare_recordings`
- `cpu_flame`, `allocation_flame`, `lock_flame`
- `memory_leaks`, `smart_predictive_leak_analysis`, `high_cpu_diagnostic`

## Guidelines for Agents
- **Hexagonal Integration:** In the refactored pattern, the Driving Adapter (MCP Tool) decides whether to invoke the service synchronously or via `AsyncJobService`.
- **Lambda Analysis:** Jobs are submitted as `Callable<String>` (for legacy tools) or `Supplier<DomainRecord>` (for new tools).
- **Concurrency:** Ensure that async tasks do not hold onto large JFR items indefinitely; rely on the `JfrRecordingCache` to manage memory pressure.
