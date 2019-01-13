package source;

public class AbstractDao<T> {
    private IMapperProxy mapperProxy;

    public AbstractDao() {
    }

    public IMapperProxy getMapperProxy() {
        return mapperProxy;
    }

    public void setMapperProxy(IMapperProxy mapperProxy) {
        this.mapperProxy = mapperProxy;
    }

    protected T getMapper(Class<T> type)
    {
        return this.mapperProxy.getMapper(type);
    }

    protected T getMapperBatch(Class<T> type)
    {
        return this.mapperProxy.getMapperBatch(type);
    }

    protected T getMapper(Class<T> mapperInf, String dataSourceName)
    {
        return this.mapperProxy.getMapper(mapperInf, dataSourceName);
    }

    protected T getMapperBatch(Class<T> mapperInf, String dataSourceName)
    {
        return this.mapperProxy.getMapperBatch(mapperInf, dataSourceName);
    }

    public void setDataSource(String dataSourceName)
    {
        this.mapperProxy.setDataSource(dataSourceName);
    }
}
