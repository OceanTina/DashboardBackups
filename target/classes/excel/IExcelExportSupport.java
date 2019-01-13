package excel;

public interface IExcelExportSupport {
    IDataProvider getDataProvider(String dataProviderBean);

    IReportDataProvider getReportDataProvider(String dataProviderBean);

    ISheetExtension getSheetExtension(String sheetExtentionBean);

    ExcelMeta getExcelMeta();

    SheetMeta getSheetMeta(String sheetName);


}
