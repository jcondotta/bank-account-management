package com.jcondotta.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jcondotta.service.dto.BankAccountDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheManagerConfig {

    @Value("${caches.bank-accounts-by-id.name}")
    private String bankAccountsCacheName;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(bankAccountsCacheName);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }

    @Bean
    @Qualifier("bankAccountCacheService")
    public CacheStore<String, BankAccountDTO> bankAccountCacheService(CacheManager cacheManager) {
        Cache springCache = cacheManager.getCache(bankAccountsCacheName);
        return new CaffeineCacheStore<>(springCache);
    }
}