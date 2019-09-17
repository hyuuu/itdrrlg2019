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
    // 初始化缓存项
    private static final int INITIALCAPACITY = Const.TOKEN_INIT_SIZE;
    // 最大缓存项
    private static final int MAXIMUMSIZE = Const.TOKEN_MAX_SIZE;
    // 定时回收
    private static final int EXPIREAFTERACCESS = Const.TOKEN_TIMEOUT;

    //LRU算法（超过最大值使用此算法）
    private static LoadingCache<String, Object> localCache = CacheBuilder.newBuilder()
            .initialCapacity(INITIALCAPACITY)//初始化缓存项为1000
            .maximumSize(MAXIMUMSIZE)//设置缓存项最大值不超过10000
            .expireAfterAccess(EXPIREAFTERACCESS, TimeUnit.MINUTES)//定时回收
            .build(new CacheLoader<String, Object>() {
                //当缓存中没有对应的值的时候执行load方法
                @Override
                public Object load(String s) throws Exception {
                    return "null";
                }
            });

    public static void set(String key, Object obj) {
        localCache.put(key, obj);
    }

    public static Object get(String key) {
        try {
            Object obj = localCache.get(key);
            if (obj == null || obj.equals("null")) {
                return null;
            }
            return obj;
        } catch (ExecutionException e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * 清缓存
     * @param key
     */
    public static void clearCache(String key){
        localCache.invalidate(key);
    }
}
