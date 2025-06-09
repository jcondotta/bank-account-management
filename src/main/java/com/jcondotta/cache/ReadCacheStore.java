package com.jcondotta.cache;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ReadCacheStore<K, V> {

    Optional<V> getIfPresent(K cacheKey);
    Optional<V> getOrFetch(K cacheKey, Function<K, Optional<V>> cacheValueLoader);
    Optional<V> getOrFetch(K cacheKey, Supplier<Optional<V>> cacheValueLoader);

    default V get(K cacheKey) {
        return getIfPresent(cacheKey).orElse(null);
    }
}