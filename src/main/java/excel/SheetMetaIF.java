package excel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SheetMetaIF extends Meta {
    private String voBeanName;
    private String dataProvider;
    private List<ColumnMeta> columnList = new ArrayList<ColumnMeta>();
    private int beginRowIndex = 1;
    private int beginColumnIndex = 1;
    private int batchSize;
    private String sheetExtensionBean;
    private String titleExtensionBean;
    private int bookDataSize = 200000;
    private int pageDataSize = 10000;
    private boolean mergeFromOut = false;

    public SheetMetaIF(String name) {
        super(name);
    }

    public void setExtensionBean(String strVoBeanName, String strDataProvider, String strSheetExtensionBean,
                                 String strTitleExtensionBean) {
        this.voBeanName = strVoBeanName;
        this.dataProvider = strDataProvider;
        this.sheetExtensionBean = strSheetExtensionBean;
        this.titleExtensionBean = strTitleExtensionBean;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public List<ColumnMeta> getColumnList() {
        return columnList;
    }

    public void addColumnMeta(ColumnMeta columnMeta) {
        for (ColumnMeta column : this.columnList) {
            if (column.equals(columnMeta)) {
                throw new IllegalArgumentException(
                        MessageFormat.format("Template Error. columns named {0} repeat.", columnMeta.getName()));
            }
        }
        this.columnList.add(columnMeta);
    }
}
