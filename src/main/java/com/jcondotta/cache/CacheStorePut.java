package com.jcondotta.cache;

public interface CacheStorePut<K, V> {

    void put(K cacheKey, V cacheValue);
}
