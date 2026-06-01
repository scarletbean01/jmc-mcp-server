package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of OS and environment context analysis.
 */
public record ProcessInfoResult(
        Optional<String> osName,
        Optional<String> osVersion,
        Optional<String> osArch,
        Optional<String> virtualizationTechnology,
        List<ProcessEntry> processes
) {

    public boolean hasAnyInfo() {
        return osName.isPresent()
                || osVersion.isPresent()
                || osArch.isPresent()
                || virtualizationTechnology.isPresent()
                || !processes.isEmpty();
    }
}
