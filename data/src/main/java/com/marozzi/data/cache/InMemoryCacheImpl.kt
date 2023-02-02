package com.marozzi.data.cache

import com.marozzi.domain.cache.InMemoryCache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource


/**
 * Implementation of [InMemoryCache]
 */
@OptIn(ExperimentalTime::class)
class InMemoryCacheImpl<Key, Value> : InMemoryCache<Key, Value> {

    private val cacheEntries: MutableMap<Key, CacheEntry<Key, Value>> = mutableMapOf()

    /** Internal mutex to lock modifications and reading on this object */
    private val mutex: Mutex = Mutex()

    override suspend fun get(key: Key): Value? {
        mutex.withLock {
            return cacheEntries[key]?.let {
                if (it.isExpired) {
                    cacheEntries.remove(key)
                    null
                } else {
                    it.value.get() as Value
                }
            }
        }
    }

    override suspend fun getOrElse(key: Key, defaultValue: (key: Key) -> Value): Value {
        mutex.withLock {
            return cacheEntries[key]?.let {
                if (it.isExpired) {
                    cacheEntries.remove(key)
                    defaultValue(key)
                } else {
                    it.value.get() as Value
                }
            } ?: kotlin.run {
                defaultValue(key)
            }
        }
    }

    override suspend fun insertOrUpdate(key: Key, value: Value) =
        insertOrUpdate(key, value, Duration.INFINITE)

    override suspend fun insertOrUpdate(key: Key, value: Value, duration: Duration) {
        mutex.withLock {
            val nowTimeMark = TimeSource.Monotonic.markNow()
            val newEntry = CacheEntry<Key, Value>(
                key = key,
                value = AtomicReference(value),
                insertTime = nowTimeMark,
                expireTime = duration,
            )
            cacheEntries[key] = newEntry
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            cacheEntries.clear()
        }
    }

    /**
     * A cache entry holds the [key] and [value] pair,
     * along with the metadata needed to perform cache expiration.
     */
    internal data class CacheEntry<Key, Value>(
        val key: Key,
        val value: AtomicReference<Value>,
        val insertTime: TimeMark,
        val expireTime: Duration,
    ) {
        val isExpired = (expireTime != Duration.INFINITE || expireTime != Duration.ZERO)
                && insertTime.plus(expireTime).hasPassedNow()
    }
}

