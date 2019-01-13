package excel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ExcelExportConstants
{
    //public static final String EXCEL_ROOT = PlatformEnvUtil.getAppTmpPath() + File.separator + "excel" + File.separator;
    public static final String EXCEL_ROOT = "";
    public static final String EXCEL_EXPORT_SUPPORT_BEAN_PREFIX = "IExcelExportSupport.";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyy-MM-dd HH:mm:ss";
    public static final String EXCEL_EXPORT_INT = "int";
    public static final String EXCEL_EXPORT_DOUBLE = "double";
    public static final String EXCEL_EXPORT_DATE = "date";
    public static final String EXCEL_EXPORT_LONG = "long";
    public static final String EXCEL_EXPORT_TIMESTSMP = "timestamp";
    public static final String EXCEL_EXPORT_STRING = "string";
    public static final String EXCEL_EXPORT_BOOLEAN = "boolean";
    public static final String EXCEL_EXPORT_BYTE = "byte";
    private static Map<String, String> dataTypeDictionary = new HashMap<String, String>();

    static
    {
        dataTypeDictionary.put("int", EXCEL_EXPORT_INT);
        dataTypeDictionary.put("integer", EXCEL_EXPORT_INT);
        dataTypeDictionary.put("short", EXCEL_EXPORT_INT);
        dataTypeDictionary.put("biginteger", EXCEL_EXPORT_INT);

        dataTypeDictionary.put("double", EXCEL_EXPORT_DOUBLE);
        dataTypeDictionary.put("float", EXCEL_EXPORT_DOUBLE);
        dataTypeDictionary.put("bigdecimal", EXCEL_EXPORT_LONG);
        dataTypeDictionary.put("long", EXCEL_EXPORT_LONG);

        dataTypeDictionary.put("string", EXCEL_EXPORT_STRING);
        dataTypeDictionary.put("charset", EXCEL_EXPORT_STRING);
        dataTypeDictionary.put("char", EXCEL_EXPORT_STRING);

        dataTypeDictionary.put("byte", EXCEL_EXPORT_BYTE);
        dataTypeDictionary.put("boolean", EXCEL_EXPORT_BOOLEAN);
        dataTypeDictionary.put("date", EXCEL_EXPORT_DATE);
        dataTypeDictionary.put("timestamp", EXCEL_EXPORT_TIMESTSMP);
    }

    public static Map<String, String> getDataTypeDictionary()
    {
        return dataTypeDictionary;
    }
}
