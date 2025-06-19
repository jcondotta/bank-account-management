package com.jcondotta.cache;

import java.util.Objects;

public interface CacheStore<K, V> extends ReadCacheStore<K, V>, WriteCacheStore<K, V> {

    default void validateCacheKey(K cacheKey) {
        Objects.requireNonNull(cacheKey, CacheErrorMessages.KEY_MUST_NOT_BE_NULL);
    }

    default void validateCacheValue(V cacheValue) {
        Objects.requireNonNull(cacheValue, CacheErrorMessages.VALUE_MUST_NOT_BE_NULL);
    }
}