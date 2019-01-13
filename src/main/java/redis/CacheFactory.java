package redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheFactory implements ICacheFactory
{
    private static  final Map<String, ICache> CACHE_MAP = new ConcurrentHashMap<String, ICache>();

    public ICache createCache(String cacheName)
    {
        ICache cache = CACHE_MAP.get(cacheName);
        if (cache == null)
        {
            synchronized (CACHE_MAP)
            {
                cache = CACHE_MAP.get(cacheName);
                if (cache == null)
                {
                    RedisCache redisCache = new RedisCache(cacheName);
                    redisCache.setCacheName(cacheName);
                    cache = redisCache;
                    CACHE_MAP.put(cacheName, cache);
                }
            }
        }
        return cache;
    }
}
