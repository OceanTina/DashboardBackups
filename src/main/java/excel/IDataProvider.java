package excel;

public interface IDataProvider {
    void getData(String conditionJson, PageConfig page);
    void removeReportCacheData(String projectId);
}
