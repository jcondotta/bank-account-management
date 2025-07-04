package com.jcondotta.application.ports.output.cache;

import java.util.Optional;
import java.util.function.Supplier;

public interface ReadCacheStore<K, V> {

    Optional<V> getIfPresent(K cacheKey);
    Optional<V> getOrFetch(K cacheKey, Supplier<Optional<V>> supplier);

    default V get(K cacheKey) {
        return getIfPresent(cacheKey).orElse(null);
    }
}