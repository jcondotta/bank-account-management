package com.jcondotta.cache;

import java.util.function.Supplier;

public interface CacheStorePutIfAbsent<K, V> {

    void putIfAbsent(K cacheKey, V cacheValue);
    void putIfAbsent(K cacheKey, Supplier<V> valueSupplier);
}
