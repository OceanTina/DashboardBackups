package excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestExcelReader {

    public static void main(String[] args) {
        String filePath = "F:\\JAVA2018\\excel\\101.xlsx";
        String sheetName = "Sheet1";
        try {
            List<List<String>> map = getListMap(filePath, sheetName);

            for(List<String> rows : map)
            {
                for(String cell : rows)
                {
                    System.out.println(cell);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<List<String>> getListMap(String filePath, String sheetName)throws IOException
    {
        List<List<String>> list = new ArrayList<>();
        Workbook book = getExcelWorkbook(filePath);
        Sheet sheet = book.getSheet(sheetName);
        int lastRowNum = sheet.getLastRowNum();
        String[] fields = null;
        List<String> dataList = null;
        //根据表头的长度来读取内容
        int lastCellNum = sheet.getRow(0).getLastCellNum();

        for(int i = 0; i <= lastRowNum; i++)
        {
            Row row = null;
            row = sheet.getRow(i);
            //用于判断是否整形数据都为空
            boolean emptyFlag = false;
            if(row != null)
            {
                if(i != 0)
                {
                    dataList = new ArrayList<String>();
                }
                if(i == 0)
                {
                    fields = new String[lastCellNum];
                }
                Cell cell = null;
                for(int j = 0; j < lastCellNum; j++)
                {
                    cell = row.getCell(j);
                    if(cell != null)
                    {
                        emptyFlag = true;
                        int type = cell.getCellType();
                        String cellValue;
                        if(type == 0)
                        {
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        }
                        else
                        {
                            cellValue = cell.getStringCellValue();
                        }

                        if(i == 0)
                        {
                            if(null != fields)
                            {
                                //0行为表头
                                fields[j] = cellValue;
                            }
                        }
                        else
                        {
                            if(null != dataList)
                            {
                                dataList.add(cellValue);
                            }
                        }
                    }
                    else
                    {
                        if(dataList != null)
                        {
                            dataList.add("");
                        }
                    }
                }
                if(i != 0 && emptyFlag)
                {
                    list.add(dataList);
                }
            }
        }
        return list;
    }


    public static Workbook getExcelWorkbook(String filePath)throws IOException
    {
        Workbook book = null;
        File file = null;
        FileInputStream fis = null;

        try {
            file = new File(filePath);
            if(!file.exists())
            {
                throw new RuntimeException("文件不存在");
            }
            else
            {
                fis = new FileInputStream(file);

                String fileType = filePath.substring(filePath.lastIndexOf(".") + 1,
                        filePath.length()).toLowerCase();
                //取文件后缀名类型
                if("xls".equals(fileType))
                {
                    //读取2003版本
                    book = new HSSFWorkbook(fis);
                }
                else
                {
                    //读取2007版本
                    book = new XSSFWorkbook(fis);
                }
                //book = WorkbookFactory.create(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return book;
    }
}
