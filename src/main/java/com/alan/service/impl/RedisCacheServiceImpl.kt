package com.alan.service.impl

import com.alan.service.CacheService
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * 缓存操作-实现类
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午10:06
 * @Modified By:
 */
@Service
class RedisCacheServiceImpl : CacheService {

    @Resource
    private val cacheManager: CacheManager? = null

    override fun <V> cacheResult(key: String, cacheName: String): V {
        val valueWrapper = cacheManager!!.getCache(cacheName).get(key)
        return valueWrapper?.get() as V
    }

    override fun cacheRemove(key: String, cacheName: String) {
        cacheManager!!.getCache(cacheName).evict(key)
    }

    override fun <V> cachePut(key: String, value: V, cacheName: String) {
        cacheManager!!.getCache(cacheName).put(key, value)
    }
}
