package com.jcondotta.cache;

import java.util.Optional;

public interface ReadCacheStore<K, V> {

    Optional<V> getIfPresent(K cacheKey);

    default V get(K cacheKey) {
        return getIfPresent(cacheKey).orElse(null);
    }
}