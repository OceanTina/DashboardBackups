package redis;

import java.nio.charset.Charset;

public class RedisCache implements ICache
{
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ICacheUtil cacheUtil = null;
    private String id;

    public RedisCache(String id)
    {
        this.id = id;
    }

    public void setCacheName(String cacheName)
    {
        this.cacheUtil = new CacheUtil(cacheName);
    }

    public void putInt(String key, int value, int timeout)
    {
        putString(key, String.valueOf(value), timeout);
    }

    public void putLong(String key, long value, int timeout)
    {
        putString(key, String.valueOf(value), timeout);
    }

    public void putString(String key, String value, int timeout)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, putString() fail.");
            return;
        }
        try (Jedis jedis = pool.getResource())
        {
            jedis.set(key, value);

            setExpire(key, timeout, jedis);
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis putString ", e);
        }
    }

    public void putObject(String key, Object value, int timeout)
    {
        byte[] newValue = SerializeUtil.serialize(value);
        byte[] newKey = key.getBytes();

        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, putObject() fail.");
            return;
        }
        try (Jedis jedis = pool.getResource())
        {
            jedis.set(newKey, newValue);

            setExpire(newKey, timeout, jedis);
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis putObject ", e);
        }
    }

    private void setExpire(String key, int timeout, Jedis jedis)
    {
        if ((timeout > 0) && (jedis != null))
        {
            Optional<Long> expired = Optional.ofNullable(jedis.expire(key, timeout));
            expired.ifPresent(value -> logger.info(String.format("expired %d", value)));
        }
    }

    private void setExpire(byte[] key, int timeout, Jedis jedis)
    {
        if ((timeout > 0) && (jedis != null))
        {
            Optional<Long> expired = Optional.ofNullable(jedis.expire(key, timeout));
            expired.ifPresent(value -> logger.info(String.format("expired %d", value)));
        }
    }

    public int getIntValue(String key)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, getIntValue() fail.");
            return -1;
        }
        try (Jedis jedis = pool.getResource())
        {
            return Integer.parseInt(jedis.get(key));
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis getIntValue ", e);
            return -1;
        }
    }

    public long getLongValue(String key)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, getLongValue() fail.");
            return -1L;
        }
        try (Jedis jedis = pool.getResource()) {
            return Long.parseLong(jedis.get(key));
        }
        catch (JedisConnectionException e)
        {
            logger.error("");
            return -1L;
        }
    }


    public String getStringValue(String key)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, getStringValue() fail.");
            return null;
        }
        try (Jedis jedis = pool.getResource())
        {
            return jedis.get(key);
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis getStringValue ", e);
            return null;
        }
    }

    private void returnJedisResource(Jedis jedis)
    {
        if (jedis != null)
        {
            jedis.close();
        }
    }

    public void clear()
    {
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, clear() fail.");
            return;
        }
        try (Jedis jedis = pool.getResource())
        {
            jedis.flushDB();
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis getObject ", e);
        }
    }

    public String getId()
    {
        return this.id;
    }

    public Object getObject(Object key)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, getObject() fail.");
            return null;
        }
        try (Jedis jedis = pool.getResource())
        {
            byte[] values = jedis.get(convertObjectToString(key).getBytes());
            if (values != null)
            {
                return SerializeUtil.unserialize(values);
            }
            return null;
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis getObject ", e);
            return null;
        }
    }

    public ReadWriteLock getReadWriteLock()
    {
        return this.readWriteLock;
    }

    @Deprecated
    public int getSize()
    {
        return this.cacheUtil.getDbSize();
    }

    public void putObject(Object key, Object value)
    {
        putObject(convertObjectToString(key), value, 0);
    }

    private String convertObjectToString(Object key)
    {
        String sKey;
        if ((key instanceof String))
        {
            sKey = (String)key;
        }
        else
        {
            sKey = String.valueOf(key.hashCode());
        }
        return sKey;
    }

    public Object removeObject(Object key)
    {
        String sKey = convertObjectToString(key);
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, removeObject() fail.");
            return null;
        }
        try (Jedis jedis = pool.getResource())
        {
            Object obj = jedis.get(sKey);
            jedis.del(new String[] {sKey});
            return obj;
        }
        catch (JedisConnectionException e)
        {
            logger.error("jedis removeObject ", e);
            return null;
        }
    }

    public Jedis getJedis()
    {
        Jedis jedis = null;
        JedisPool pool = this.cacheUtil.getPool();
        if (pool == null)
        {
            logger.error("jedis pool is null, getJedis() fail.");
            return null;
        }
        try
        {
            jedis = pool.getResource();
        }
        catch (JedisConnectionException e)
        {
            logger.error("");
        }
        return jedis;
    }


    public void returnJedis(Jedis jedis)
    {
        returnJedisResource(jedis);
    }

    public void put(String key, byte[] value, int timeout)
    {
        byte[] newKey = key.getBytes(Charset.forName("utf-8"));
        put(newKey, value, timeout);
    }

    public void put(byte[] key, byte[] value, int timeout)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if(pool == null)
        {
            return;
        }

        try (Jedis jedis = pool.getResource())
        {
            jedis.set(key, value);
            setExpire(key, timeout, jedis);

        }
        catch (JedisConnectionException e)
        {
            logger.error("");
        }
    }

    public byte[] get(String key)
    {
        byte[] newKey = key.getBytes(Charset.forName("utf-8"));
        return get(newKey);
    }

    public byte[] get(byte[] key)
    {
        JedisPool pool = this.cacheUtil.getPool();
        if(pool == null)
        {
            return new byte[0];
        }

        try (Jedis jedis = pool.getResource())
        {
            return jedis.get(key);

        }
        catch (JedisConnectionException e)
        {
            return new byte[0];
        }
    }
}
