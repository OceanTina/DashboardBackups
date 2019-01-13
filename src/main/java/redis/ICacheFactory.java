package redis;

public interface ICacheFactory {
    ICache createCache(String paramString);
}
