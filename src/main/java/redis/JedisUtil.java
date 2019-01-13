package redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtils;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.util.Optional;
import java.util.Properties;

public class JedisUtil
{
    private static final String MAX_ACTIVE = "redis.pool.maxActive";
    private static final String DEFAULT_MAX_ACTIVE = String.valueOf(1024);
    private static final String MAX_IDLE = "redis.pool.maxIdle";
    private static final String DEFAULT_MAX_IDEL = String.valueOf(200);
    private static final String MAX_WAIT = "redis.pool.maxWait";
    private static final String DEFAULT_MAX_WAIT = String.valueOf(1000);
    private static final String TEST_ON_BORROW = "redis.pool.testOnBorrow";
    private static final String DEFAULT_TEST_ON_BORROW = String.valueOf(true);
    private static final String TEST_ON_RETURN = "redis.pool.testOnReturn";
    private static final String DEFAULT_TEST_ON_RETURN = String.valueOf(true);
    private static final int DEFAULT_TIME_OUT = 60000;
    private static final long MIN_EVICTABLE_IDLE_TIME_MILLIS = 30000L;
    private static final int NUM_TESTS_PER_EVICTION_RUN = 20000;

    private static Properties redisPropertis;
    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    private static Map<String, DataSourceMeta> jedisInfoMap = null;

    static
    {
        StringBuilder dataSourceCfgDir = new StringBuilder(PlatformEnvUtil.getAppRoot())
                .append(File.separator)
                .append("etc")
                .append(File.separator)
                .append("datasource")
                .append(File.separator)
                .append("datasource.cfg");

        redisPropertis = Optional.ofNullable(
                FileUtils.loadProperties(dataSourceCfgDir.toString())).orElse(new Properties());
        try
        {
            jedisInfoMap = DBInfoReader.readRedisInfo();
        }
        catch (ServiceException e)
        {
            logger.error("read jedis info failed", e);
        }
        logger.info("Read Redis data source file finished, count  = {}", Integer.valueOf(jedisInfoMap.size()));
    }

    public static JedisPoolConfig getPoolConfig()
    {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(Integer.parseInt(redisPropertis.getProperty(MAX_ACTIVE, DEFAULT_MAX_ACTIVE)));
        config.setMaxIdle(Integer.parseInt(redisPropertis.getProperty(MAX_IDLE, DEFAULT_MAX_IDEL)));
        config.setMaxWaitMillis(Integer.parseInt(redisPropertis.getProperty(MAX_WAIT, DEFAULT_MAX_WAIT)));
        config.setTestOnBorrow(
                Boolean.valueOf(redisPropertis.getProperty(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW)).booleanValue());
        config.setTestOnReturn(
                Boolean.valueOf(redisPropertis.getProperty(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN)).booleanValue());

        config.setMinEvictableIdleTimeMillis(MIN_EVICTABLE_IDLE_TIME_MILLIS);
        config.setNumTestsPerEvictionRun(NUM_TESTS_PER_EVICTION_RUN);

        return config;
    }

    public static JedisPool getJedisPool(JedisPoolConfig config, String redisName) throws IllegalArgumentException
    {
        DataSourceMeta dsMeta = jedisInfoMap.get(redisName);
        if (null == dsMeta)
        {
            throw new IllegalArgumentException("Can not get "
                    + " redis config from " + PlatformEnvUtil.getAppConfigFilePath());
        }

        JedisPool jedisPool = new JedisPool(config, dsMeta.getServerName(),
                dsMeta.getPort(), DEFAULT_TIME_OUT, dsMeta.getReidsPasswd());

        try (Jedis jedis = jedisPool.getResource())
        {
            if(!jedis.ping().equals("PONG"))
            {
                logger.error("");
            }

        }
        return jedisPool;
        }
}
