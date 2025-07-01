package com.jcondotta.application.ports.output.cache;

/**
 * Interface for a cache store that allows putting values into the cache.
 *
 * @param <K> the type of keys used in the cache
 * @param <V> the type of values stored in the cache
 */
public interface CacheStorePut<K, V> {

    /**
     * Puts a value into the cache with the specified key.
     *
     * @param cacheKey   the key under which the value is stored
     * @param cacheValue the value to be stored in the cache
     */
    void put(K cacheKey, V cacheValue);
}
