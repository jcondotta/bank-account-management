package com.jcondotta.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.Objects;
import java.util.Optional;

public class CaffeineCacheStore<K, V> implements CacheStore<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheStore.class);

    private final Cache cache;

    public CaffeineCacheStore(Cache cache) {
        this.cache = Objects.requireNonNull(cache, "cache.notNull");
    }

    @Override
    public void put(K cacheKey, V cacheValue) {
        validateCacheKey(cacheKey);
        validateCacheValue(cacheValue);

        LOGGER.debug("Adding cache entry: key='{}', value='{}'", cacheKey, cacheValue);

        cache.put(cacheKey, cacheValue);

        LOGGER.atInfo()
                .setMessage("Cache put: key='{}' successfully stored in cache.")
                .addArgument(cacheKey)
                .log();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<V> getIfPresent(K cacheKey) {
        validateCacheKey(cacheKey);

        var wrapper = cache.get(cacheKey);
        if (wrapper != null) {
            V value = (V) wrapper.get();
            if (value != null) {
                LOGGER.debug("Cache hit: key='{}'", cacheKey);
                return Optional.of(value);
            }
        }

        LOGGER.debug("Cache miss: key='{}'", cacheKey);
        return Optional.empty();
    }

    @Override
    public void evict(K cacheKey) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");

        LOGGER.info("Evicting cache entry with key: '{}'", cacheKey);
        cache.evict(cacheKey);

        LOGGER.debug("Cache evict: key='{}' removed from cache.", cacheKey);
    }
}