package redis;

public interface ICacheUtil {
    JedisPool getPool();

    int getDbSize();

    void clearPool();
}
