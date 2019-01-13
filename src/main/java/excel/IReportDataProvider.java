package excel;

import java.util.List;

public interface IReportDataProvider
{
    int getReportDataCount(String conditionJson) throws Exception;

    List getReportPaginationData(String conditionJson, int pageSize, int pageNo) throws Exception;

    List getReportPaginationData(String conditionJson, int pageSize, int pageNo, int bookSize, int bookNo,
                                 List<CellMergeRegion> mergeRegionList) throws Exception;

}
