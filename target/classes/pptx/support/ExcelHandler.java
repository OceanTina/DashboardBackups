package support;

import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.model.data.ChartData;
import pptx.apiTest.model.data.TableData;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelHandler {
    /**
     * 获取sheet的对应单元格
     */
    public static XSSFCell getCell(XSSFSheet sheet,int row,int col)
    {
        XSSFRow sheetRow = sheet.getRow(row);
        if(sheetRow == null)
        {
            sheetRow = sheet.createRow(row);
        }
        XSSFCell cell = sheetRow.getCell(col);
        if(cell == null)
        {
            cell = sheetRow.createCell(col);
        }
        return cell;
    }

    public static void updateXLSXData(POIXMLDocumentPart xlsPart, ChartData data)throws PPTxException
    {
        XSSFWorkbook wb = null;

        try {
            wb = new XSSFWorkbook();
            XSSFSheet mysheet = wb.createSheet();
            //在XLSX中加入Categories
            List<String> categories = data.getCategories();
            for(int i = 0;i<categories.size();i++)
            {
                //categories对应第一列
                XSSFCell cell = getCell(mysheet,i + 1,0);
                cell.setCellValue(categories.get(i));
            }
            //在XLSX中增加一列serial
            Map<String,List<String>> serials = data.getSerials();
            int index = 1;
            for(Map.Entry<String,List<String>> entry:serials.entrySet())
            {
                index++;
                XSSFCell headCell = getCell(mysheet,0,index);
                headCell.setCellValue(entry.getKey());
                for(int i = 0;i<entry.getValue().size();i++)
                {
                    XSSFCell cell = getCell(mysheet,i+1,index);
                    cell.setCellValue(Double.valueOf(entry.getValue().get(i)));
                }
            }
            wb.write(xlsPart.getPackagePart().getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new PPTxException("update excel in chart faild");
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateTableData(POIXMLDocumentPart xlsPart,TableData data)throws PPTxException
    {
        XSSFWorkbook wb = null;

        try {
            wb = new XSSFWorkbook(xlsPart.getPackagePart().getInputStream());
            XSSFSheet mysheet = wb.getSheetAt(0);
            //写文件头
            XSSFRow headRow = mysheet.getRow(1);
            for(int i = 0;i<data.getRows().size();i++)
            {
                headRow.getCell(i).setCellValue(data.getHeaders().get(i));
            }
            //表格体
            for(int i=0;i<data.getRows().size();i++)
            {
                List<String> row = data.getRows().get(i);
                XSSFRow contentRow = mysheet.getRow(i+1);
                for(int j=0;j<row.size();j++)
                {
                    contentRow.getCell(j).setCellValue(row.get(j));
                }
            }

            wb.write(xlsPart.getPackagePart().getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new PPTxException("update XLSXData faild");
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
