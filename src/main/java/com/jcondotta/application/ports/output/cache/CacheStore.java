package com.jcondotta.application.ports.output.cache;

import java.util.Objects;

public interface CacheStore<V> extends ReadCacheStore<String, V>, WriteCacheStore<String, V> {

    default void validateCacheKey(String cacheKey) {
        Objects.requireNonNull(cacheKey, CacheErrorMessages.KEY_MUST_NOT_BE_NULL);
    }

    default void validateCacheValue(V cacheValue) {
        Objects.requireNonNull(cacheValue, CacheErrorMessages.VALUE_MUST_NOT_BE_NULL);
    }
}