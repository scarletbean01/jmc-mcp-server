package io.github.deplague.jmcmcp.infrastructure.jfr;

import org.openjdk.jmc.common.item.IAccessorKey;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.IType;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for JFR item accessors.
 * Caches accessors per type to avoid repeated attribute scans.
 */
public final class JfrAccessorRepository {

    @SuppressWarnings("rawtypes")
    private static final Map<IType<?>, Map<String, IMemberAccessor>> ACCESSOR_CACHE = Collections.synchronizedMap(new WeakHashMap<>());

    private JfrAccessorRepository() {
        // utility class
    }

    /**
     * Get an accessor for a specific attribute identifier on a given type.
     */
    @SuppressWarnings("unchecked")
    public static <T> IMemberAccessor<T, IItem> getAccessor(IType<?> type, String identifier) {
        Map<String, IMemberAccessor> typeCache = ACCESSOR_CACHE.computeIfAbsent(
                type,
                t -> new ConcurrentHashMap<>()
        );
        return (IMemberAccessor<T, IItem>) typeCache.computeIfAbsent(
                identifier,
                id -> {
                    for (Map.Entry<IAccessorKey<?>, ? extends org.openjdk.jmc.common.IDescribable> entry : type.getAccessorKeys().entrySet()) {
                        if (id.equals(entry.getKey().getIdentifier())) {
                            return type.getAccessor((IAccessorKey<Object>) entry.getKey());
                        }
                    }
                    return null;
                }
        );
    }

    /**
     * Get a member value from an item by attribute identifier string.
     */
    public static <T> Optional<T> getMember(IItem item, String identifier) {
        IMemberAccessor<T, IItem> accessor = getAccessor(item.getType(), identifier);
        return accessor != null
                ? Optional.ofNullable(accessor.getMember(item))
                : Optional.empty();
    }

    /**
     * Get an {@link org.openjdk.jmc.common.unit.IQuantity} member from an item.
     */
    public static <T> Optional<T> getQuantity(IItem item, String identifier) {
        return getMember(item, identifier);
    }
}
