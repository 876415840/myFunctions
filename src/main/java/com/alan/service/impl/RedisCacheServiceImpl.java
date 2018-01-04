package com.alan.service.impl;

import com.alan.service.CacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 缓存操作-实现类
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午10:06
 * @Modified By:
 */
@Service
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private CacheManager cacheManager;

    @Override
    public <V> V cacheResult(String key, String cacheName) {
        Cache.ValueWrapper valueWrapper = cacheManager.getCache(cacheName).get(key);
        return (V)(valueWrapper == null ? null : valueWrapper.get());
    }

    @Override
    public void cacheRemove(String key, String cacheName) {
        cacheManager.getCache(cacheName).evict(key);
    }

    @Override
    public <V> void cachePut(String key, V value, String cacheName) {
        cacheManager.getCache(cacheName).put(key,value);
    }
}
