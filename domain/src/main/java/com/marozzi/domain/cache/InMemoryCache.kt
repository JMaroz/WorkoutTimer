package com.marozzi.domain.cache

import kotlin.time.Duration

/**
 * Defines an in-memory cache as a key value pairs store.
 */
interface InMemoryCache<Key, Value> {

    /**
     * Returns the value associated with [key] in this cache, or null if there is no
     * cached value for [key].
     */
    suspend fun get(key: Key): Value?

    /**
     * Returns the value associated with [key] in this cache, or defaultValue if there is no
     * cached value for [key].
     */
    suspend fun getOrElse(key: Key, defaultValue: (key: Key) -> Value): Value

    /**
     * Associates [value] with [key] in this cache. If the cache previously contained a
     * value associated with [key], the old value is replaced by [value].
     */
    suspend fun insertOrUpdate(key: Key, value: Value)

    /**
     * Associates [value] with [key] in this cache. If the cache previously contained a
     * value associated with [key], the old value is replaced by [value].
     */
    suspend fun insertOrUpdate(key: Key, value: Value, duration: Duration)

    /**
     * Remove all entries in the cache.
     */
    suspend fun clear()

}