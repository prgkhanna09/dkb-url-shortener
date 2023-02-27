package com.dkb.urlshortener.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
@EnableCaching
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class RedisConfiguration {

    @Value("\${redis.host}")
    lateinit var host: String
    @Value("\${redis.port}")
    lateinit var port: Integer

    @Bean
    @Profile("!test") // we don't need profiling for mvp as we use secrets to load configs
    // unless major config changes are needed
    fun jedisConnectionFactory(): JedisConnectionFactory? {
        return JedisConnectionFactory(RedisStandaloneConfiguration(host, port.toInt()))
    }

    @Bean
    @Profile("!test")
    fun redisTemplate(): RedisTemplate<String, Any>? {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(jedisConnectionFactory()!!)
        return template
    }
}
