package com.jcondotta.application.ports.output.cache;

public interface CacheStoreEvict<K> {

    /**
     * Evicts a single cache entry by key.
     *
     * @param cacheKey the exact key to evict
     */
    void evict(K cacheKey);

    /**
     * Evicts all cache entries that start with the given key prefix.
     *
     * @param cacheKey the prefix of keys to evict
     */
    void evictByPrefixKey(K cacheKey);
}
