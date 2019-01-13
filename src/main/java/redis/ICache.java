package redis;

public interface ICache extends Cache {
    void putInt(String var1, int var2, int var3);

    void putLong(String var1, long var2, int var4);

    void putString(String var1, String var2, int var3);

    void putObject(String var1, Object var2, int var3);

    int getIntValue(String var1);

    long getLongValue(String var1);

    String getStringValue(String var1);

    void put(String var1, byte[] var2, int var3);

    void put(byte[] var1, byte[] var2, int var3);

    byte[] get(String var1);

    byte[] get(byte[] var1);

    Jedis getJedis();

    void returnJedis(Jedis var1);

    void returnBrokenJedis(Jedis var1);
}
