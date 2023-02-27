package com.dkb.urlshortener.configuration

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
@EnableCaching
class RedisConfiguration {

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory? {
        return JedisConnectionFactory(RedisStandaloneConfiguration("localhost", 6379))
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any>? {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(jedisConnectionFactory()!!)
        return template
    }
}
