package com.itdr.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基于Guava Cache缓存类
 */
public class TokenCache {

    //LRU算法（超过最大值使用此算法）
    private static LoadingCache<String, Object> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)//初始化缓存项为1000
            .maximumSize(10000)//设置缓存项最大值不超过10000
            .expireAfterAccess(30, TimeUnit.MINUTES)//定时回收
            .build(new CacheLoader<String, Object>() {
                //当缓存中没有对应的值的时候执行load方法
                @Override
                public Object load(String s) throws Exception {
                    return null;
                }
            });

    public static void set(String key, Object obj) {
        localCache.put(key, obj);
    }

    public static Object get(String key) {
        try {
            Object obj = localCache.get(key);
            if (obj == null) {
                return null;
            }
            return obj;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //清缓存
    public static void clearCache(String key){
        localCache.invalidate(key);
    }
}
