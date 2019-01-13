package excel;

import util.CollectionUtil;

import java.util.List;

public abstract class AbstractDataPageProvider implements IDataProvider
{
    private int pageSize, pageNo;

    private Object exInfo;

    private volatile boolean hasNextPage = true;

    public void reset(int iPageSize, Object objExInfo)
    {
        this.pageSize = iPageSize;
        this.exInfo = objExInfo;

        this.pageNo = 1;
        this.hasNextPage = true;
    }

    @Override
    public void getData(String conditionJson, PageConfig page)
    {
        // ... do nothing.
    }

    public <T> List<T> nextPage()
    {
        List<T> result = getPageData(pageNo++, pageSize, exInfo);
        hasNextPage = !CollectionUtil.isEmpty(result);
        return result;
    }
    public boolean hasNextPage()
    {
        return hasNextPage;
    }
    protected abstract <T> List<T> getPageData(int pageNo, int pageSize, Object exInfo);
}
