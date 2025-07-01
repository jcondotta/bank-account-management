package com.jcondotta.application.ports.output.cache;

public interface WriteCacheStore<K, V>
    extends CacheStorePut<K, V>,
    CacheStorePutIfAbsent<K, V>,
    CacheStoreEvict<K> {
}