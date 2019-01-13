package restfu;

public interface IRestParametes
{
    String get(String key);

    String put(String key, String value);

    String putHttpContextHeader(String key, String value);

    String putHttpContextHeader(String key, int value);

    String getHttpContextHeader(String key);
}

