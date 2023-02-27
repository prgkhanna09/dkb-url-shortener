package com.dkb.urlshortener.cache

import com.dkb.urlshortener.utils.Constants
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

interface CacheManager {
    fun get(key: String): Any?
    fun save(key: String, data: Any)
    fun delete(key: String)
}

@Component
class CacheManagerImpl(
    private val redisTemplate: RedisTemplate<String, Any>
) : CacheManager {

    // No TTL from cache for now, if needed we can do it asynchronously or introduce ttl later on
    override fun get(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }

    override fun save(key: String, data: Any) {
        redisTemplate.opsForValue().set(key, data)
    }

    override fun delete(key: String) {
        redisTemplate.opsForValue().set(key, "", 1, TimeUnit.MILLISECONDS)
    }

    fun getUrlKey(url: String): String {
        return Constants.SHORTEN_URL_CACHE_KEY_PREFIX + url
    }
}
