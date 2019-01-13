package redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CacheUtil implements ICacheUtil
{
    private static final Lock LOCK = new ReentrantLock();
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtil.class);
    private volatile JedisPool pool = null;
    private String cacheName;

    public CacheUtil(String cacheName)
    {
        this.cacheName = cacheName;
    }

    @Override
    public JedisPool getPool()
    {
        if (this.pool == null)
        {
            LOCK.lock();
            try
            {
                if (this.pool == null)
                {
                    JedisPoolConfig config = JedisUtil.getPoolConfig();

                    this.pool = JedisUtil.getJedisPool(config, cacheName);
                }
            }
            catch (IllegalArgumentException ex)
            {
                LOGGER.error("Create redis pool failed!", ex);
            }
            finally
            {
                LOCK.unlock();
            }
        }
        return this.pool;
    }

    @Override
    public int getDbSize()
    {
        // 不再提供查询大小接口
        return 0;
    }

    @Override
    public void clearPool()
    {
        this.pool.destroy();
        this.pool = null;
    }
}
