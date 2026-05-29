package io.github.deplague.jmcmcp.domain.service;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Pure domain service for formatting a high-CPU diagnostic macro report.
 */
@ApplicationScoped
public final class HighCpuDiagnosticService {

    public String formatReport(String healthResult, String threadResult, String hotMethodsResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("# High CPU Diagnostic Report\n\n");
        sb.append("> **Macro Execution:** This report aggregates `system_health`, `thread_cpu`, and `hot_methods` into a single view.\n\n");

        sb.append("---\n");
        sb.append("## Step 1: System Health Context\n\n");
        sb.append(stripTitle(healthResult, "# System Health Analysis\n\n"));

        sb.append("\n---\n");
        sb.append("## Step 2: Top CPU Consuming Threads\n\n");
        sb.append(stripTitle(threadResult, "# Thread CPU Analysis\n\n"));

        sb.append("\n---\n");
        sb.append("## Step 3: Global Hot Methods\n\n");
        sb.append(stripTitle(hotMethodsResult, "# Hot Methods & Call Paths\n\n"));

        sb.append("\n<agent_hint>Review the overarching CPU utilization in Step 1. Then match the top threads in Step 2 with the hot methods in Step 3. If standard CPU looks normal but the application is slow, consider investigating lock contention or I/O limits.</agent_hint>\n");

        return sb.toString();
    }

    private String stripTitle(String markdown, String title) {
        if (markdown == null) {
            return "";
        }
        return markdown.replace(title, "");
    }
}
