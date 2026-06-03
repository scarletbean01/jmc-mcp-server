package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record ReferenceLink(
        String referenceType,
        String fieldName,
        String className,
        long objectId,
        long shallowSize,
        long retainedSize,
        List<ReferenceLink> children
) {}
