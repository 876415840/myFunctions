package com.alan.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer

import java.lang.reflect.Method

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午2:56
 * @Modified By:
 */
@Configuration
@EnableCaching
class RedisConfig : CachingConfigurerSupport() {

    @Bean
    override fun keyGenerator(): KeyGenerator {
        return KeyGenerator { target, method, params ->
            val sb = StringBuilder()
            sb.append(target.javaClass.name)
            sb.append(method.name)
            for (obj in params) {
                sb.append(obj.toString())
            }
            sb.toString()
        }
    }

    @Bean
    fun cacheManager(redisTemplate: RedisTemplate<*, *>): CacheManager {
        val rcm = RedisCacheManager(redisTemplate)
        //设置缓存过期时间
        rcm.setDefaultExpiration(60)//秒
        return rcm
    }

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = StringRedisTemplate(factory)
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        val om = ObjectMapper()
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
        jackson2JsonRedisSerializer.setObjectMapper(om)
        template.valueSerializer = jackson2JsonRedisSerializer
        template.afterPropertiesSet()
        return template
    }
}
