package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPool
{
    private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);
    private static final String FILE_NAME =
            //PlatformEnvUtil.getAppEtcPath() + File.separator + "conf" + File.separator + "threadPool.properties";
            "threadPool.properties";
    private static final String CORE_THREAD_COUNT_NAME = "thread.pool.coreThreadCount";
    private static final String MAX_THREAD_COUNT_NAME = "thread.pool.maxThreadCount";
    private static final String IDLE_ALIVE_TIME_NAME = "thread.pool.idleAliveTime";
    private static final int CORE_THREAD_COUNT_DEFAULT = 8;
    private static final int MAX_THREAD_COUNT_DEFAULT = 128;
    private static final long IDLE_ALIVE_TIME_DEFAULT = 300L;
    private static final ThreadPool INSTANCE = new ThreadPool();
    private int coreThreadCount = CORE_THREAD_COUNT_DEFAULT;
    private int maxThreadCount = MAX_THREAD_COUNT_DEFAULT;
    private long idleAliveTime = IDLE_ALIVE_TIME_DEFAULT;
    private ThreadPoolExecutor executor;

    private ThreadPool()
    {
        Properties prop = FileUtils.loadProperties(FILE_NAME);

        if (prop != null)
        {
            coreThreadCount =
                    Integer.parseInt(prop.getProperty(CORE_THREAD_COUNT_NAME, String.valueOf(CORE_THREAD_COUNT_DEFAULT)));
            maxThreadCount =
                    Integer.parseInt(prop.getProperty(MAX_THREAD_COUNT_NAME, String.valueOf(MAX_THREAD_COUNT_DEFAULT)));
            idleAliveTime =
                    Long.parseLong(prop.getProperty(IDLE_ALIVE_TIME_NAME, String.valueOf(IDLE_ALIVE_TIME_DEFAULT)));
        }

        ThreadPool.logger.error("threadPool[coreThread:" + coreThreadCount + "; maxThread:" + maxThreadCount
                + "; idleTme:" + idleAliveTime + ";]");
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor exe)
            {
                ThreadPool.logger.info(String.format("rejected Thread %s : ", new Object[] {r}));
                try
                {
                    r.run();
                }
                catch (Exception e)
                {
                    ThreadPool.logger.error("Run Runnable exception, Runnable:" + r, e);
                }
            }
        };
        this.executor = new ThreadPoolExecutor(coreThreadCount, maxThreadCount, idleAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), handler);

        Runtime.getRuntime().addShutdownHook(new Thread("Thread pool shut down hook") {
            public void run()
            {
                ThreadPool.this.executor.shutdown();
            }
        });
    }

    public static ThreadPool getInstance()
    {
        return INSTANCE;
    }

    public void execute(Runnable task)
    {
        try
        {
            if (this.executor.isShutdown())
            {
                task.run();
            }
            else
            {
                this.executor.execute(task);
            }
        }
        catch (Exception e)
        {
            logger.error("Run task exception ,task is:" + task, e);
        }
    }
}