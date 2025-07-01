package com.jcondotta.infrastructure.adapters.cache;

import com.jcondotta.application.ports.output.cache.CacheStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RedisCacheStore<V> implements CacheStore<V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheStore.class);

    private final RedisTemplate<String, V> redisTemplate;
    private final long ttlSeconds;
    private final Class<V> type;

    public RedisCacheStore(RedisTemplate<String, V> redisTemplate, long ttlSeconds, Class<V> type) {
        this.redisTemplate = redisTemplate;
        this.ttlSeconds = ttlSeconds;
        this.type = type;
    }

    @Override
    public void put(String cacheKey, V cacheValue) {
        validateCacheKey(cacheKey);
        validateCacheValue(cacheValue);

        LOGGER.debug("Adding cache entry: key='{}', value='{}'", cacheKey, cacheValue);

        redisTemplate
            .opsForValue()
            .set(cacheKey, cacheValue, Duration.ofSeconds(ttlSeconds));

        LOGGER.info("Cache put: key='{}' successfully stored.", cacheKey);
    }

    @Override
    public Optional<V> getIfPresent(String cacheKey) {
        validateCacheKey(cacheKey);

        V value = redisTemplate.opsForValue().get(cacheKey);
        if (value != null) {
            LOGGER.debug("Cache hit: key='{}'", cacheKey);
            if (type.isInstance(value)) {
                return Optional.of(type.cast(value));
            }
            else {
                LOGGER.warn("Cache key='{}' has unexpected type: {}", cacheKey, value.getClass().getName());
                return Optional.empty();
            }
        }

        LOGGER.info("Cache miss: key='{}'", cacheKey);
        return Optional.empty();
    }

    @Override
    public Optional<V> getOrFetch(String cacheKey, Supplier<Optional<V>> supplier) {
        validateCacheKey(cacheKey);
        Objects.requireNonNull(supplier, "cache.supplier.notNull");

        return getIfPresent(cacheKey)
            .or(() -> {
                Optional<V> valueFetched = supplier.get();
                valueFetched.ifPresent(value -> put(cacheKey, value));
                return valueFetched;
            });
    }

    @Override
    public void evict(String cacheKey) {
        validateCacheKey(cacheKey);

        LOGGER.info("Evicting cache entry with key: '{}'", cacheKey);
        redisTemplate.delete(cacheKey);

        LOGGER.debug("Cache evict: key='{}' removed.", cacheKey);
    }

    @Override
    public void evictByPrefixKey(String keyPrefix) {
        Objects.requireNonNull(keyPrefix, "cache.keyPrefix.notNull");
        LOGGER.info("Evicting Redis keys with prefix: '{}'", keyPrefix);

        try {
            redisTemplate.execute((RedisConnection connection) -> {
                List<byte[]> keysToDelete = new ArrayList<>();
                // Use try-with-resources to ensure the cursor is closed automatically
                try (Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().match(keyPrefix + "*").count(1000).build())) {

                    // Collect all matching keys into a list
                    cursor.forEachRemaining(keysToDelete::add);
                }

                // If any keys were found, delete them in a single batch operation
                if (!keysToDelete.isEmpty()) {
                    connection.del(keysToDelete.toArray(new byte[0][]));
                    LOGGER.debug("Evicted {} Redis keys with prefix '{}'", keysToDelete.size(), keyPrefix);
                } else {
                    LOGGER.debug("No Redis keys found with prefix '{}' to evict.", keyPrefix);
                }
                return null; // Required return value for the execute method
            });
        } catch (Exception e) {
            // Log any exception that occurs during the Redis operation
            LOGGER.error("Failed to evict keys with prefix '{}'", keyPrefix, e);
        }
    }

    @Override
    public void putIfAbsent(String cacheKey, V cacheValue) {
        validateCacheKey(cacheKey);
        validateCacheValue(cacheValue);

        LOGGER.debug("Attempting to set cache entry for key='{}' if absent.", cacheKey);

        Boolean wasEntrySet = redisTemplate
            .opsForValue()
            .setIfAbsent(cacheKey, cacheValue, ttlSeconds, TimeUnit.SECONDS);

        // Log based on the actual outcome
        if (Boolean.TRUE.equals(wasEntrySet)) {
            LOGGER.info("Cache entry set for key='{}' as it was absent.", cacheKey);
        } else {
            LOGGER.info("Cache entry for key='{}' already existed; value was not updated.", cacheKey);
        }
    }
}
