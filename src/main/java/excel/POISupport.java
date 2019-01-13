package excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.util.IOUtils;

import javax.el.ELClass;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class POISupport {
    public static final short DEFAULT_HEIGHT = (short)256;
    public static final short DEFAULT_COLUMN_SIZE = (short)256 * 20;
    public static final int BUILD_WORKBOOK_SUCCESS = 0;
    public static final int BUILD_WORKBOOK_FAIL = 2;
    public static final int CELL_TYPE_NUMERIC = Cell.CELL_TYPE_NUMERIC;
    public static final int CELL_TYPE_STRING = Cell.CELL_TYPE_STRING;
    public static final int CELL_TYPE_BLANK = Cell.CELL_TYPE_BLANK;
    public static final int CELL_TYPE_BOOLEAN = Cell.CELL_TYPE_BOOLEAN;
    public static final int CELL_TYPE_ERROR = Cell.CELL_TYPE_ERROR;
    protected static final int DEFAULT_SHEET_ZOOM = 120;

    private static final Logger LOGGER = LoggerFactory.getLogger(POISupport.class);

    private XSSFCellStyle defaultCellStyle = null;
    private XSSFCellStyle titleCellStyle = null;
    private XSSFCellStyle dateCellStyle = null;
    private XSSFCellStyle timestampCellStyle = null;
    private XSSFCellStyle doubleCellStyle = null;
    private XSSFCellStyle yellowCellStyle = null;
    private XSSFCellStyle redCellStyle = null;
    private XSSFCellStyle greenCellStyle = null;
    private XSSFCellStyle blueCellStyle = null;

    private DataFormat dataFormat;
    private String outputFile;

    private SXSSFWorkbook sbook;
    private XSSFWorkbook xbook;
    private Workbook rworkbook;

    private List<Sheet> sheetList;
    private int defaultRowAccessWindowSize;

    private Object lock = new Object();

    private CellMergeMgr cellMergeMgr = new CellMergeMgr();

    //101
    public void createWorkbook()
    {
        createWorkbook((InputStream)null, defaultRowAccessWindowSize);
    }

    public void createWorkbook(InputStream input)
    {
        createWorkbook(input, defaultRowAccessWindowSize);
    }

    //301
    public void createWorkbook(String inputFile)
    {
        if(inputFile.endsWith(EnumFileType.XLSM.getValue()))
        {
            createMacroWorkbook(inputFile, defaultRowAccessWindowSize);
        }
        else
        {
            createWorkbook(inputFile, defaultRowAccessWindowSize);
        }
    }

    //302
    private void createMacroWorkbook(String inputFile, int iRowAccessWindowSize)
    {

        LOGGER.error("create xlsm workbook :" + inputFile);

        try {
            sbook = new SXSSFWorkbook(new XSSFWorkbook(OPCPackage.open(inputFile)), iRowAccessWindowSize);
            xbook = sbook.getXSSFWorkbook();

            //初始化
            dataFormat = xbook.createDataFormat();
            setDefaultCellStyle();
            setTitleStyle();
            setDateCellStyle();
            setTimeStampCellStyle();
            setDoubleCellStyle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    //101
    public void createWorkbook(InputStream input, int iRowAccessWindowSize)
    {
        LOGGER.info("initialize the workbook");

        if(sbook == null)
        {
            if(input == null)
            {
                sbook = new SXSSFWorkbook(new XSSFWorkbook(), iRowAccessWindowSize);

                try {
                    File tmplFile = File.createTempFile("poi-sxssf-template", ".xlsx");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    sbook = new SXSSFWorkbook(new XSSFWorkbook(input), iRowAccessWindowSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            xbook = sbook.getXSSFWorkbook();

            //初始化
            dataFormat = xbook.createDataFormat();
            setDefaultCellStyle();
            setTitleStyle();
            setDateCellStyle();
            setTimeStampCellStyle();
            setDoubleCellStyle();
        }
        LOGGER.info("the workbook initialization is complete");
    }

    //
    private void setDoubleCellStyle()
    {
        doubleCellStyle = xbook.createCellStyle();
        doubleCellStyle.cloneStyleFrom(defaultCellStyle);
        doubleCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
    }

    //
    private void setDateCellStyle()
    {
        dateCellStyle = xbook.createCellStyle();
        dateCellStyle.cloneStyleFrom(defaultCellStyle);
        dateCellStyle.setDataFormat(dataFormat.getFormat("YYYY_MM_DD"));
    }

    //
    private void setTimeStampCellStyle()
    {
        timestampCellStyle = xbook.createCellStyle();
        timestampCellStyle.cloneStyleFrom(defaultCellStyle);
        timestampCellStyle.setDataFormat(dataFormat.getFormat("YYYY_MM_DD_HH_MM_SS"));
    }

    //
    private void setDefaultCellStyle()
    {
        if(dateCellStyle == null)
        {
            dateCellStyle = xbook.createCellStyle();
            //设置边框
            defaultCellStyle.setBorderLeft(CellStyle.BORDER_THIN);//细
            defaultCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());//黑
            defaultCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            defaultCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            defaultCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            defaultCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            defaultCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            defaultCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            //设置填充模式，单色填充
            defaultCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            //设置前景色，背景色
            defaultCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            defaultCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
            //设置水平居左，垂直居上
            defaultCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
            defaultCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            XSSFFont localXSSFFont = xbook.createFont();

            localXSSFFont.setItalic(false);
            localXSSFFont.setColor(IndexedColors.BLACK.getIndex());
            localXSSFFont.setFontHeight((short)10);
            localXSSFFont.setFontName("Arial");

            defaultCellStyle.setFont(localXSSFFont);
        }
    }

    private void setTitleStyle()
    {
        titleCellStyle = xbook.createCellStyle();
        titleCellStyle.cloneStyleFrom(defaultCellStyle);
        titleCellStyle.setWrapText(true);

        Font headerFont = xbook.createFont();

        headerFont.setFontName("arial");

        titleCellStyle.setFont(headerFont);
    }

    //201
    public Sheet createSheet(String sheetName)
    {
        Sheet sheet = null;

        synchronized (lock)
        {
            sheet = sbook.createSheet(sheetName);
            //缩放比例
            if(xbook.getSheet(sheetName) != null)
            {
                xbook.getSheet(sheetName).setZoom(DEFAULT_SHEET_ZOOM);
            }
        }
        return sheet;
    }

    //501
    public void removeSheet(String sheetName)
    {
        int idx = xbook.getSheetIndex(sheetName);
        if(idx > 0)
        {
            synchronized (lock)
            {
                xbook.removeSheetAt(idx);
            }
        }
    }

    //401
    public SXSSFSheet getSXSheetByName(String sheetName)
    {
        return (SXSSFSheet)sbook.getSheet(sheetName);
    }

    public SXSSFSheet getSXSheetByIndex(int index)
    {
        return (SXSSFSheet)sbook.getSheetAt(index);
    }

    public Sheet getXSSFSheetByName(String sheetName)
    {
        return xbook.getSheet(sheetName);
    }

    public Sheet getXSSFSheetByIndex(int index)
    {
        return xbook.getSheetAt(index);
    }

    //701
    public void writeTitleData(Sheet sheet, String[] titles, int titleIndex, int columnIndex)
    {
        sheet.createFreezePane(0, titleIndex + 1, 0, titleIndex + 1);
        setTitle(sheet, titles, titleIndex, columnIndex)
    }
    //
    private void setTitle(Sheet sheet, String[] titles, int rowIndex, int columnIndex)
    {
        if(titles == null)
        {
            return;
        }

        for(int i = 0; i< titles.length; i++)
        {
            setStringValue(sheet, rowIndex, columnIndex + 1, titles[i], titleCellStyle);
            //设置行高
            Row row = sheet.getRow(rowIndex);
            row.setHeight(DEFAULT_HEIGHT);

        }
    }
    //
    private int setStringValue(Sheet sheet, int rowIndex, int columnIndex, String value, CellStyle cellStyle)
    {
        Cell cell = createRowColmn(sheet, rowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);

        cell = null;
        return ++rowIndex;
    }
    //
    private int setDateValue(Sheet sheet, int rowIndex, int columnIndex, Date value, CellStyle cellStyle)
    {
        Cell cell = createRowColumn(sheet, rowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);

        cell = null;
        return ++rowIndex;
    }
    //
    private int setLongValue(Sheet sheet, int rowIndex, int columnIndex, long value, CellStyle cellStyle)
    {
        Cell cell = createRowColumn(sheet, rowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);

        cell = null;
        return ++rowIndex;
    }
    //
    private int setIntValue(Sheet sheet, int rowIndex, int columnIndex, int value, CellStyle cellStyle)
    {
        Cell cell = createRowColumn(sheet, rowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);

        cell = null;
        return ++rowIndex;
    }
    //
    private int setDoubleValue(Sheet sheet, int rowIndex, int columnIndex, double value, CellStyle cellStyle)
    {
        Cell cell = createRowColumn(sheet, rowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);

        cell = null;
        return ++rowIndex;
    }
    //
    private int setBooleanValue(Sheet sheet, int rowIndex, int columnIndex, boolean value, CellStyle cellStyle)
    {
        Cell cell = createRowColumn(sheet, rowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);

        cell = null;
        return ++rowIndex;
    }

    //901
    public int writeContentData(Sheet sheet, int rowIndex, int columnIndex, Object value, String valueType)
    {
        String celType = getDataTypeDictionary().get(valueType.toLowerCase());

        if(celType.equals(EXCEL_EXPORT_TIMESTSMP))
        {
            return setDateValue(sheet, rowIndex, columnIndex, (Date)value, timestampCellStyle);
        }
        else if(celType.equals(EXCEL_EXPORT_DATE))
        {
            return setDateValue(sheet, rowIndex, columnIndex, (Date)value, dateCellStyle);
        }
        else if(celType.equals(EXCEL_EXPORT_BOOLEAN))
        {
            return setBooleanValue(sheet, rowIndex, columnIndex, ((Boolean)value).booleanValue(), defaultCellStyle);
        }
        else if(celType.equals(EXCEL_EXPORT_LONG))
        {
            return setLongValue(sheet, rowIndex, columnIndex, ((Long)value).longValue(), defaultCellStyle);
        }
        else if(celType.equals(EXCEL_EXPORT_DOUBLE))
        {
            return setDoubleValue(sheet, rowIndex, columnIndex, ((Double) value).doubleValue(), doubleCellStyle);
        }
        else if(celType.equals(EXCEL_EXPORT_BYTE) || celType.equals(EXCEL_EXPORT_INT))
        {
            return seIntValue(sheet, rowIndex, columnIndex, (Integer.valueOf(String.valueOf(value))).intValue(), defaultCellStyle);
        }
        else
        {
            return setStringValue(sheet, rowIndex, columnIndex, String.valueOf(value), defaultCellStyle);
        }
    }

    public int writeStringContentData(Sheet sheet, int rowIndex, int columnIndex, String value, CellStyle stringCellStyle)
    {
        return setStringValue(sheet, rowIndex, columnIndex, value, stringCellStyle);
    }

    private Cell createRowColumn(Sheet sheet, int rowIndex, int columnIndex)
    {
        Row row = sheet.getRow(rowIndex);
        if(null == row)
        {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(columnIndex);
        if(null == cell)
        {
            cell = row.createCell(columnIndex);
        }

        return cell;
    }

    //601
    public boolean write()
    {

        try (OutputStream outputStream = SafeResourceUtil.getSafeLinuxOStream(outputFile, true))
            {LOGGER.info("");
            if(outputStream != null)
            {
                File f = new File(outputFile);
                boolean flag = f.getParentFile().mkdir();
                if(!flag)
                {
                    LOGGER.error("");
                }
            }
            sbook.write(outputStream);
            //写合并单元格或超链接信息
                if(outputStream != null)
                {
                    addNodesViaXml(outputFile);
                }
                return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            dispose();
            clearObject();
        }
    }

    public void dispose()
    {
        //使用POI自带接口删除临时文件
        sbook.dispose();
    }

    //602
    private void addNodesViaXml(String excelPath)throws IOException
    {
        Map<Integer, Collection<String>> mergeCellMap = this.cellMergeMgr.getXmlMergeCellMap();
        if(mergeCellMap.isEmpty())
        {
            LOGGER.error("");
            return;
        }
        ExcelXmlWriter xmlWriter = new ExcelXmlWriter();
        File tmpExcelFile;

        try {
            tmpExcelFile = xmlWriter.addExcelXmlNodes(excelPath, null, mergeCellMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //替换目的文件
        if(tmpExcelFile != null && tmpExcelFile.exists())
        {
            File oldExcelFile = new File(excelPath);

            if(oldExcelFile.delete())
            {
                FileInputStream in = null;
                OutputStream out = null;

                try {
                    in = new FileInputStream(tmpExcelFile);
                    out = SafeResourceUtil.getSafeLinuxOStream(oldExcelFile, true);
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
                tmpExcelFile.delete();
            }

            else
            {
                LOGGER.error("");
            }

        }
    }


    private void clearObject()
    {
        if(defaultCellStyle != null)
        {
            defaultCellStyle = null;
        }
        if(dataFormat != null)
        {
            dataFormat = null;
        }
        if(sheetList != null)
        {
            sheetList.clear();
            sheetList = null;
        }
        if(xbook != null)
        {
            xbook = null;
        }
        if(sbook != null)
        {
            sbook = null;
        }
    }


    //801
    public void flushRows(Sheet sheet)
    {
        try {
            ((SXSSFSheet)sheet).flushRows();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //1001
    public void addMergedRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol)
    {
        int sheetIndex = sheet.getWorkbook().getSheetIndex(sheet);
        cellMergeMgr.putXmlMergeCell(sheetIndex, firstRow, lastRow, firstCol, lastCol);
    }

    private String getCellStringValue(Cell cell)
    {
        int celltype = cell.getCellType();
        String cellString = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        switch (celltype)
        {
            case CELL_TYPE_STRING:
                cellString = cell.getStringCellValue().trim();
                break;
            case CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(cell))
                {
                    cellString = fmt.format(cell.getDateCellValue());
                }
                else
                {
                    cellString = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case CELL_TYPE_ERROR:
                cellString = String.valueOf(cell.getErrorCellValue());
                break;
            case CELL_TYPE_BOOLEAN:
                cellString = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                cellString = "Unknown Cell Type";
                break;
        }
        return cellString;
    }


    //
    public int getExcelWorkbook(String filepath)throws Exception
    {
        File file = new File(filepath);

        try (FileInputStream fs = new FileInputStream(file))
        {
            if(file.exists())
            {
                rworkbook = WorkbookFactory.create(fs);
            }
            else
            {
                LOGGER.info("");
                return EXCEL_FILE_NOT_EXIST;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(null != rworkbook)
        {
            return BUILD_WORKBOOK_SUCCESS;
        }
        else
        {
            return BUILD_WORKBOOK_FAIL;
        }
    }

    public Sheet getSheetByNum(int sheetIndex)
    {
        if(sheetIndex > 0 && sheetIndex <= rworkbook.getNumberOfSheets())
        {
            return rworkbook.getSheetAt(sheetIndex - 1);
            return null;
        }
    }

    public int getSheetNumByBook()
    {
        return rworkbook.getNumberOfSheets();
    }
}
