package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record ReferencePathResult(
        String targetClassName,
        long targetObjectId,
        long targetRetainedSize,
        List<ReferenceLink> pathsToGcRoots
) {}
