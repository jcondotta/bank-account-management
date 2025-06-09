package com.jcondotta.cache;

import java.util.Objects;
import java.util.function.Supplier;

public interface CacheStore<K, V> extends ReadCacheStore<K, V>, WriteCacheStore<K, V> {

    default void validateCacheKey(K cacheKey) {
        Objects.requireNonNull(cacheKey, CacheErrorMessages.KEY_MUST_NOT_BE_NULL);
    }

    default void validateCacheValue(V cacheValue) {
        Objects.requireNonNull(cacheValue, CacheErrorMessages.VALUE_MUST_NOT_BE_NULL);
    }

    default void validateValueSupplier(Supplier<V> valueSupplier) {
        Objects.requireNonNull(valueSupplier, CacheErrorMessages.VALUE_SUPPLIER_MUST_NOT_BE_NULL);
    }
}