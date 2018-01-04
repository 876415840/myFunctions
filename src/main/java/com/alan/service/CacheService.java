package com.alan.service;

/**
 * 缓存操作
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午10:03
 * @Modified By:
 */
public interface CacheService {

    public <V> V cacheResult(String key, String cacheName);

    public void cacheRemove(String key, String cacheName);

    public <V> void cachePut(String key, V value, String cacheName);
}
