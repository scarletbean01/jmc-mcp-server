# Module: 01-domain (jmc-mcp-domain)

This module contains the core domain logic, analysis heuristics, and performance models. It is the innermost layer of the hexagonal architecture.

## Sub-packages
- **`model`**: Pure Java Records representing analysis results (e.g., `AllocationFlameResult`, `GcAnalysisResult`).
- **`service`**: Stateless services implementing JFR analysis heuristics and business logic.
- **`exception`**: Domain-specific exception hierarchy (e.g., `JmcMcpDomainException`, `RecordingNotFoundException`).

## Key Classes
- **`*Service`**: Dozens of specialized analysis services (e.g., `ThreadCpuService`, `MemoryLeaksService`, `JdbcNPlusOneAnalyzerService`).
- **`*Result`**: Data transfer objects (Java Records) that encapsulate analysis findings.

## Responsibilities
- **Analysis Heuristics:** Implementing the logic to detect performance patterns (e.g., N+1 queries, thread starvation, memory leaks).
- **Domain Modeling:** Defining the structure of JFR analysis results.
- **Unit Conversion:** Handling JFR quantities and time-series data.

## Patterns Used
- **Stateless Services:** All domain services are thread-safe and stateless.
- **Rich Data Models:** Using Java Records for immutable, structured analysis output.
- **Custom Exceptions:** Using a typed exception hierarchy for granular error reporting.

## Guidelines for Agents
- **Framework-Free:** This package MUST NOT depend on frameworks like Quarkus or Micrometer.
- **JMC Dependency:** Direct dependency on JMC Core (`org.openjdk.jmc.common`, `org.openjdk.jmc.flightrecorder`) is **explicitly permitted** and required for performance.
- **Logic Only:** No formatting (Markdown/JSON) or protocol-specific code belongs here.
- **Performance:** When implementing new heuristics, be mindful of JFR event volume. Use efficient iterators and avoid large intermediate collections.
- **Purity:** Keep domain models as simple Java Records.
