package io.github.deplague.jmcmcp.adapters.mcp;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Interface for all MCP tool adapters.
 * Each adapter exposes its {@link SyncToolSpecification} for registration
 * with the MCP server.
 */
public interface McpTool {

    /**
     * @return the MCP tool specification including schema and call handler
     */
    SyncToolSpecification spec();
}
