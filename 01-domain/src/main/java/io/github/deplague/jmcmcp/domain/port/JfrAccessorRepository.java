package io.github.deplague.jmcmcp.domain.port;

import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.IType;

import java.util.Optional;

public interface JfrAccessorRepository {
    <T> IMemberAccessor<T, IItem> getAccessor(IType<?> type, String identifier);

    <T> Optional<T> getMember(IItem item, String identifier);

    <T> Optional<T> getQuantity(IItem item, String identifier);
}
