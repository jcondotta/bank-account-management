package com.jcondotta.infrastructure.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcondotta.application.ports.output.cache.CacheStore;
import com.jcondotta.domain.accountholder.model.AccountHolder;
import com.jcondotta.domain.bankaccount.model.BankAccount;
import com.jcondotta.infrastructure.adapters.cache.RedisCacheStore;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Bean
    public CacheStore<BankAccount> redisCacheStore(RedisTemplate<String, BankAccount> redisTemplate) {
        return new RedisCacheStore<>(redisTemplate, 2000000, BankAccount.class);
    }

    @Bean
    public RedisTemplate<String, BankAccount> bankAccountRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, BankAccount> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serializer específico para BankAccount
        Jackson2JsonRedisSerializer<BankAccount> serializer = new Jackson2JsonRedisSerializer<>(BankAccount.class);
        serializer.setObjectMapper(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheStore<AccountHolder> redisAccountHolderCacheStore(RedisTemplate<String, AccountHolder> redisTemplate) {
        return new RedisCacheStore<>(redisTemplate, 2000000, AccountHolder.class);
    }

    @Bean
    public RedisTemplate<String, AccountHolder> accountHolderRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, AccountHolder> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serializer específico para BankAccount
        Jackson2JsonRedisSerializer<AccountHolder> serializer = new Jackson2JsonRedisSerializer<>(AccountHolder.class);
        serializer.setObjectMapper(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        var redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        var jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        // Configure Jackson2JsonRedisSerializer for values
        Jackson2JsonRedisSerializer<Object> serializer =
            new Jackson2JsonRedisSerializer<>(Object.class);

        // Configure the ObjectMapper as needed without activating default typing
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        // Do not activate default typing to avoid extra type information

        serializer.setObjectMapper(objectMapper);

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            // Set TTL for cached values
            .entryTtl(Duration.ofMinutes(10))
            // Use the Jackson serializer for cache values
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(cacheConfiguration)
            .transactionAware()
            .build();
    }

//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration(){
//        RedisCacheConfiguration.defaultCacheConfig()
//            .entryTtl(Duration.ofMinutes(10))
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//            .disableCachingNullValues();
//    }
}