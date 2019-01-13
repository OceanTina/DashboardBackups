package source;

public interface IMapperProxy {
    <T> T getMapper(Class<T> var1, String var2);

    <T> T getMapperBatch(Class<T> var1, String var2);

    void setDataSource(String var1);

    <T> T getMapper(Class<T> var1);

    <T> T getMapperBatch(Class<T> var1);
}
