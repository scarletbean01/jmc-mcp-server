# Enterprise Profiling Evolution: Pillars 2 & 3

This document outlines the strategic implementation plans for elevating `jmc-mcp` into a cloud-native, expert-level enterprise diagnostic tool.

---

## Pillar 2: Cloud-Native & Enterprise I/O

Enterprise JFR files rarely live on local developer laptops; they reside in cloud storage or are generated dynamically inside Kubernetes clusters.

### Feature 1: Cloud Storage Streaming (S3 / HTTP)
**Goal:** Allow the MCP server to directly analyze remote JFR files without requiring the user to download them manually.

*   **Status:** Planned.
*   **Implementation Strategy:**
    *   Update `JfrProvider` infrastructure to detect URI schemes (e.g., `s3://`, `https://`).
    *   If a remote URI is detected, stream the file to a secure, temporary local cache.
    *   Update tool schemas to reflect that `jfr_file_path` accepts URLs.

### Feature 2: Data Masking / PII Sanitization (CRITICAL)
**Goal:** Prevent sensitive data (PII, credentials, SQL queries) captured in JFR events from being returned to the LLM agent.

*   **Status:** In Progress (Design Phase).
*   **Implementation Strategy:**
    *   Introduce a global configuration (e.g., `JMC_MCP_DATA_MASKING=true`).
    *   Create a `SanitizationUtil` in the `domain` layer that applies regex-based masking to high-risk JFR fields.
    *   **High-Risk Targets:**
        *   `jdk.JavaExceptionThrow` (`message` field): Redact SQL parameters, emails, tokens.
        *   `jdk.SystemProcess` (`commandLine` field): Redact arguments that look like passwords.
        *   `jdk.InitialEnvironmentVariable` / `jdk.InitialSystemProperty`: Mask values for keys containing "SECRET", "PASSWORD", "KEY", "TOKEN".

### Feature 3: Kubernetes (K8s) Live Attachment
**Goal:** Enable the agent to dynamically trigger a JFR dump from a live pod.

*   **Status:** Planned.
*   **Implementation Strategy:**
    *   Create a new macro tool: `k8s_dump_jfr`.
    *   Execution: Use `kubectl exec` and `kubectl cp` under the hood to manage recordings.

---

## Pillar 3: "Expert-in-a-Box" Heuristics

Move the tooling from simply *reporting metrics* to generating *definitive mathematical conclusions*.

### Feature 1: Predictive Memory Leak Analysis
**Goal:** Mathematically prove a memory leak using linear regression on post-GC heap usage.

*   **Status: COMPLETED.**
*   **Implementation:** See `smart_predictive_leak_analysis`. It applies linear regression over post-GC heap summary events to calculate growth rates and project OOM time.

### Feature 2: Automated Deadlock Detection
**Goal:** Automatically detect deadlocks and present them clearly.

*   **Status: COMPLETED.**
*   **Implementation:** See `deadlock_detection`. It analyzes monitor ownership and wait-for relationships to detect cycles.

### Feature 3: JDK Bug Cross-Referencing
**Goal:** Automatically map internal JVM failures to known OpenJDK issues.

*   **Status: COMPLETED.**
*   **Implementation:** See `jdk_bug_reference`. It cross-references events against a built-in database of known JDK bug signatures.

### Feature 4: Smart Bottleneck Identification
**Goal:** One-click macro dashboard with severity classification and auto-detected dominant bottleneck.

*   **Status: COMPLETED.**
*   **Implementation:** See `smart_quick_analysis` and `smart_correlate`. These tools orchestrate multiple sensors to find the "smoking gun" in a recording.

---

## Current Tooling Maturity

As of May 2026, the server provides **69 specialized tools** covering the full spectrum of JVM diagnostics, including advanced interactive call trees and cross-recording diffs. The focus is now shifting toward **Pillar 2** (Cloud/K8s) and the final **Phase 3** of the hexagonal refactoring.