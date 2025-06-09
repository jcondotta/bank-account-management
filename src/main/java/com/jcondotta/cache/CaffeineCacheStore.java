package com.jcondotta.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class CaffeineCacheStore<K, V> implements CacheStore<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheStore.class);

    private final Cache cache;

    public CaffeineCacheStore(Cache cache) {
        this.cache = Objects.requireNonNull(cache, "Cache must not be null");
    }

    @Override
    public void put(K cacheKey, V cacheValue) {
        validateCacheKey(cacheKey);
        validateCacheValue(cacheValue);

        LOGGER.debug("Adding cache entry: key='{}', value='{}'", cacheKey, cacheValue);

        cache.put(cacheKey, cacheValue);
        logCachePutSuccess(CacheAction.PUT, cacheKey);
    }

    @Override
    public void putIfAbsent(K cacheKey, V cacheValue) {
        validateCacheValue(cacheValue);
        putIfAbsent(cacheKey, () -> cacheValue);
    }

    @Override
    public void putIfAbsent(K cacheKey, Supplier<V> valueSupplier) {
        validateCacheKey(cacheKey);
        validateValueSupplier(valueSupplier);

        var wrapper = cache.get(cacheKey);

        if (wrapper == null || wrapper.get() == null) {
            V value = valueSupplier.get();
            validateCacheValue(value);

            LOGGER.debug("{}: key='{}' not present. Storing value from supplier.", CacheAction.PUT_IF_ABSENT.getDisplay(), cacheKey);

            cache.put(cacheKey, value);
            logCachePutSuccess(CacheAction.PUT_IF_ABSENT, cacheKey);
        }
        else {
            LOGGER.debug("{}: key='{}' already present. Skipping put.", CacheAction.PUT_IF_ABSENT.getDisplay(), cacheKey);
        }
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

    public Optional<V> getOrFetch(K cacheKey, Supplier<Optional<V>> cacheValueLoader) {
        return getOrFetch(cacheKey, ignored -> cacheValueLoader.get());
    }

    @Override
    public Optional<V> getOrFetch(K cacheKey, Function<K, Optional<V>> cacheValueLoader) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        Objects.requireNonNull(cacheValueLoader, "cache.valueLoader.function.notNull");

        try {
            V value = cache.get(cacheKey, () -> fetchAndCacheValue(cacheKey, cacheValueLoader));
            if (value == null) {
                return Optional.empty();
            } else {
                LOGGER.debug("Cache hit (getOrFetch): key='{}'", cacheKey);
                return Optional.of(value);
            }
        }
        catch (Cache.ValueRetrievalException e) {
            LOGGER.error("Error retrieving cache value for key '{}'", cacheKey, e);
            throw e;
        }
    }

    private V fetchAndCacheValue(K cacheKey, Function<K, Optional<V>> cacheValueLoader) {
        LOGGER.warn("Cache miss: Key='{}' not found, fetching from external source.", cacheKey);

        Optional<V> cacheValueLoaded = cacheValueLoader.apply(cacheKey);
        if (cacheValueLoaded.isEmpty()) {
            LOGGER.warn("valueLoader returned empty for Key='{}'", cacheKey);
        }
        else {
            LOGGER.info("Value loaded and cached: Key='{}'", cacheKey);
        }
        return cacheValueLoaded.orElse(null);
    }

    @Override
    public void evict(K cacheKey) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");

        LOGGER.info("Evicting entry for Key='{}'", cacheKey);
        cache.evict(cacheKey);

        LOGGER.debug("Cache evict: Key='{}' removed from cache.", cacheKey);
    }

    private void logCachePutSuccess(CacheAction action, K key) {
        LOGGER.atInfo()
                .setMessage("Cache {}: key='{}' successfully stored in cache.")
                .addArgument(action)
                .addArgument(key)
                .log();
    }
}