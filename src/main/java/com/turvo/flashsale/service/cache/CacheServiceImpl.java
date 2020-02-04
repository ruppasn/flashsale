package com.turvo.flashsale.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.turvo.flashsale.config.Constants.DEFAULT_CACHE_TIMEOUT_IN_SECONDS;

/**
 * Distributed cache methods use Redis
 */
@Service(value = "cacheService")
public class CacheServiceImpl<K, V> implements ICacheService<K, V> {

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public V get(final K key, final String prefix) {
        return redisTemplate.opsForValue().get(prefix + ":" + key.toString());
    }

    @Override
    public void set(final K key, final String prefix, final V value) {
        this.set(key, prefix, value, DEFAULT_CACHE_TIMEOUT_IN_SECONDS);
    }

    @Override
    public void set(final K key, final String prefix, final V value, final Integer timeout) {
        redisTemplate.opsForValue().set(prefix + ":" + key.toString(), value);
        redisTemplate.expire(prefix + ":" + key.toString(), timeout, TimeUnit.SECONDS);
    }

    @Override
    public void delete(final K key, final String prefix) {
        redisTemplate.delete(prefix + ":" + key.toString());
    }

}
