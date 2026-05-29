# Package: io.github.deplague.jmcmcp.infrastructure.security

This package provides technical guards and access control logic for the JMC MCP Server.

## Responsibilities
- **Path Validation:** Ensure JFR recording paths are valid and safe to read.
- **Access Control:** `RecordingAccessController` manages permissions for reading local files and connecting to remote JMX endpoints.

## Guidelines for Agents
- **Security First:** Always consult the `RecordingAccessController` before performing any I/O or network operation.
- **No Domain Leakage:** Security logic here should focus on technical safety (filesystem traversal, credential management) rather than business rules.
