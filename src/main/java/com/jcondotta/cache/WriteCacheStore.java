package com.jcondotta.cache;

public interface WriteCacheStore<K, V> extends CacheStorePut<K, V> {
    void evict(K cacheKey);
}