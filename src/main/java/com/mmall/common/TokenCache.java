package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangLi
 * @Date: 2018/9/15 20:42
 * @Package: com.mmall.common
 * @Description:
 */
public class TokenCache {
    public static final String TOKEN_PREFIX = "token_";
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //使用guava做缓存，可以用Redis代替
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                /**
                 * 默认的数据加载方法实现，当调用get方法取值时，如果key没有对应的value，则返回此方法的返回值
                 * @param s
                 * @return
                 * @throws Exception
                 */
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String get(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache Error",e);
        }
        return null;
    }

}
