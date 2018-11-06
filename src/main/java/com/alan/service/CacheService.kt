package com.alan.service

/**
 * 缓存操作
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午10:03
 * @Modified By:
 */
interface CacheService {

    fun <V> cacheResult(key: String, cacheName: String): V

    fun cacheRemove(key: String, cacheName: String)

    fun <V> cachePut(key: String, value: V, cacheName: String)
}
