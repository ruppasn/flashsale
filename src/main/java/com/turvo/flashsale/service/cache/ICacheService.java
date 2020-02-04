package com.turvo.flashsale.service.cache;

/**
 * Internal Caching Service
 * In-memory methods are to cache locally
 * Other methods are part of external caching service. i.e. Redis
 */
public interface ICacheService<K, V> {

    V get(final K key, final String prefix);

    void set(final K key, final String prefix, final V value);

    void set(final K key, final String prefix, final V value, final Integer timeout);

    void delete(final K key, final String prefix);

}
