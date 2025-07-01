package com.jcondotta.application.ports.output.cache;

public interface CacheStorePutIfAbsent<K, V> {
    void putIfAbsent(K cacheKey, V cacheValue);
}
