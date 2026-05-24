# Enterprise Profiling Evolution: Pillars 2 & 3

This document outlines the strategic implementation plans for elevating `jmc-mcp` into a cloud-native, expert-level enterprise diagnostic tool.

---

## Pillar 2: Cloud-Native & Enterprise I/O

Enterprise JFR files rarely live on local developer laptops; they reside in cloud storage or are generated dynamically inside Kubernetes clusters. Furthermore, security and compliance are paramount.

### Feature 1: Cloud Storage Streaming (S3 / HTTP)
**Goal:** Allow the MCP server to directly analyze remote JFR files without requiring the user to download them manually.

*   **Implementation Strategy:**
    *   Update `JfrAnalysisService.loadRecording(String filePath)` to detect URI schemes (e.g., `s3://`, `https://`).
    *   If a remote URI is detected, stream the file to a secure, temporary local cache using Java's `HttpClient` or an AWS SDK (if configured).
    *   *Alternative:* Use JMC's chunked parsing capabilities to analyze the stream without fully realizing the multi-gigabyte file on disk (though local temp caching is often more stable for JMC).
    *   Update tool schemas to reflect that `jfr_file_path` accepts URLs.

### Feature 2: Data Masking / PII Sanitization (CRITICAL)
**Goal:** Prevent sensitive data (PII, credentials, SQL queries) captured in JFR events (like Exceptions or environment variables) from being returned to the LLM agent, satisfying enterprise InfoSec requirements.

*   **Implementation Strategy:**
    *   Introduce a global configuration (e.g., via environment variable `JMC_MCP_DATA_MASKING=true` or a dedicated MCP tool to toggle it).
    *   Create a `SanitizationUtil` class that applies regex-based masking to high-risk JFR fields before they are appended to the Markdown output.
    *   **High-Risk Targets:**
        *   `jdk.JavaExceptionThrow` (`message` field): Replace potential SQL parameters, emails, or tokens with `[REDACTED]`.
        *   `jdk.SystemProcess` (`commandLine` field): Redact arguments that look like passwords or API keys.
        *   `jdk.InitialEnvironmentVariable` / `jdk.InitialSystemProperty`: Mask values for keys containing "SECRET", "PASSWORD", "KEY", "TOKEN".
    *   *Result:* The LLM receives structural stack traces and metrics but no sensitive payload data.

### Feature 3: Kubernetes (K8s) Live Attachment
**Goal:** Enable the agent to dynamically trigger a JFR dump from a live pod.

*   **Implementation Strategy:**
    *   Create a new macro tool: `k8s_dump_jfr`.
    *   **Parameters:** `namespace`, `pod_name`, `duration_seconds`.
    *   **Execution:** Under the hood, the server executes `kubectl exec -n <namespace> <pod_name> -- jcmd 1 JFR.start duration=<duration_seconds>s filename=/tmp/dump.jfr`.
    *   Wait for the duration, then execute `kubectl cp` to retrieve the file to the MCP server's workspace, finally returning the local path to the agent for immediate analysis.

---

## Pillar 3: "Expert-in-a-Box" Heuristics

Move the tooling from simply *reporting metrics* to generating *definitive mathematical conclusions*. The LLM should receive diagnoses, not just raw data.

### Feature 1: Predictive Memory Leak Analysis
**Goal:** Mathematically prove a memory leak rather than relying on the LLM to visually interpret a markdown table of heap trends.

*   **Implementation Strategy:**
    *   Enhance `MemoryLeaksTool` or create a new `predictive_leak_analysis` tool.
    *   Extract `jdk.GCHeapSummary` events.
    *   Filter to only use "After GC" (post-collection) heap usage points to eliminate the noise of short-lived allocations.
    *   Apply a **Linear Regression** algorithm over the post-GC usage data points across the timeline.
    *   **Heuristic:** If the slope (growth rate) is strongly positive and the $R^2$ (correlation coefficient) is $> 0.85$, it is a confirmed leak.
    *   **Output:** Calculate the exact time to `OutOfMemoryError` by projecting the line until it intersects with the configured `MaxHeapSize`. Return a definitive statement: *"Memory Leak Detected: Post-GC heap is growing at 50MB/min. OOM projected in 2.5 hours."*

### Feature 2: Automated Lock Cycle (Deadlock) Detection
**Goal:** Automatically detect deadlocks and present them clearly.

*   **Implementation Strategy:**
    *   Enhance `LockAnalysisTool` or `thread_contention`.
    *   Analyze `jdk.JavaMonitorEnter` and `jdk.ThreadDump` events.
    *   Construct a directed graph in memory where nodes are Threads and Monitors, and edges represent "holds" or "waiting for".
    *   Use a cycle-detection algorithm (e.g., Tarjan's or DFS).
    *   **Output:** If a cycle is found, generate the deadlock chain using Mermaid.js syntax:
        ```mermaid
        graph TD
          ThreadA -->|waiting for| LockX
          LockX -->|held by| ThreadB
          ThreadB -->|waiting for| LockY
          LockY -->|held by| ThreadA
        ```
    *   *Result:* The LLM agent automatically renders this diagram in the chat UI, instantly explaining the deadlock visually to the user.

### Feature 3: JDK Bug Cross-Referencing
**Goal:** Automatically map internal JVM failures to known OpenJDK issues.

*   **Implementation Strategy:**
    *   When an event like `jdk.CompilerFailure` or `jdk.JavaErrorThrow` (specifically internal JVM errors) occurs, extract the failure message and the JVM version from `jdk.OSInformation`.
    *   Include a static JSON/CSV mapping file within the MCP server containing common OpenJDK bug signatures (e.g., C2 compiler crashes, G1GC assert failures).
    *   **Output:** Append a high-confidence diagnosis: *"This C2 CompilerFailure matches the signature for JDK-821423. It is a known bug in your runtime (17.0.2) and is resolved by upgrading to 17.0.4 or disabling the C2 optimization via `-XX:CompileCommand=exclude,com/package/Class::method`."*