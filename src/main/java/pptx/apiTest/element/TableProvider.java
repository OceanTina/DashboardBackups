package pptx.apiTest.element;

import pptx.apiTest.model.data.TableData;
import pptx.apiTest.model.style.TableStyle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class TableProvider {
    /**
     * 501修改表格数据
     */
    public static void updateTable(XSLFSlide slide, int shapeId, TableData tableData, TableStyle tableStyle)
    {
        List<String> headers = tableData.getHeaders();
        List<List<String>> rows = tableData.getRows();
        int tableRowNum = rows.size() + 1;
        //获取textbox 更改内容
        List<XSLFShape> shapeList = slide.getShapes();
        for(XSLFShape shape:shapeList)
        {
            if(shape instanceof XSLFTable && shape.getShapeId() == shapeId)
            {
                for(int i=0;i<tableRowNum;i++)
                {
                    List<String> rowData = i ==0 ? headers:rows.get(i-1);
                    updateRowData((XSLFTable)shape,i,rowData,tableStyle);
                }
                break;
            }
        }
    }
    /**
     * 502修改每一行数据
     */
    private static void updateRowData(XSLFTable shape,int row,List<String> rowData,TableStyle tableStyle)
    {
        if(CollectionUtils.isEmpty(rowData))
        {
            return;
        }
        double fontSize = row == 0 ? tableStyle.getHeaderFontSize():tableStyle.getDataFontSize();
        Color fontColor = row == 0 ? tableStyle.getHeadFontColor():tableStyle.getDataFontColor();
        for(int j=0;j<rowData.size();j++)
        {
            XSLFTextRun textRun = ((XSLFTable)shape).getCell(row,j).setText(rowData.get(j));
            textRun.setFontSize(fontSize);
            textRun.setFontColor(fontColor);
        }
    }
    /**
     * 503添加带表头的table
     */
    public static void addTableWithHeader(XSLFSlide slide, TableStyle style, TableData data,int x,int y,int width,int height)
    {
        List<String> headers = data.getHeaders();
        List<List<String>> rows = data.getRows();
        XSLFTable table = slide.createTable();
        table.setAnchor(new Rectangle2D.Double(x,y,width,height));
        int tableRowNum = rows.size() + 1;
        int tableColNum = headers.size();
        for(int i = 0;i<tableRowNum;i++)
        {
            if(i == 0)
            {
                addTableTitle(table,headers,style);
            }
            else
            {
                List<String> rowData = rows.get(i-1);
                Color fillColor = null;
                if(i%2 == 0)
                {
                    fillColor = style.getErenColor();
                }
                else
                {
                    fillColor = style.getOddColor();
                }
                addTableData(table,rowData,tableColNum,fillColor,style);
            }
        }
    }
    /**
     * 内部方法
     */
    private static void addTableTitle(XSLFTable table,List<String> headers,TableStyle style)
    {
        List<Integer> columnWidthList = style.getColumnWidthList();
        XSLFTableRow row = table.addRow();
        row.setHeight(style.getHeaderRowHight());
        int tableColNum = headers.size();
        for(int i=0;i<tableColNum;i++)
        {
            XSLFTableCell tableCell =  addCell(row, style.getHeaderColor());
            XSLFTextParagraph graph = tableCell.addNewTextParagraph();
            graph.setTextAlign(TextParagraph.TextAlign.CENTER);
            XSLFTextRun run = graph.addNewTextRun();
            run.setFontSize(style.getHeaderFontSize());
            run.setFontColor(style.getHeadFontColor());
            run.setText(headers.get(i));
            if(columnWidthList != null && columnWidthList.size()>i)
            {
                table.setColumnWidth(i,columnWidthList.get(i));
            }
        }
    }
    /**
     * 内部方法
     */
    private static void addTableData(XSLFTable table,List<String> rowData,int tableColNum,Color fillColor,TableStyle style)
    {
        XSLFTableRow row =table.addRow();
        row.setHeight(style.getDataRowHight());
        for(int i=0;i<tableColNum;i++)
        {
            XSLFTableCell tableCell = addCell(row,fillColor);
            XSLFTextParagraph graph = tableCell.addNewTextParagraph();
            graph.setTextAlign(TextParagraph.TextAlign.LEFT);
            XSLFTextRun run = graph.addNewTextRun();
            run.setFontSize(style.getDataFontSize());
            run.setText(rowData.get(i));
        }
    }
    /**
     * 内部方法
     */
    private static XSLFTableCell addCell(XSLFTableRow row,Color fillColor)
    {
        XSLFTableCell tableCell = row.addCell();
        tableCell.setLeftInset(8);
        tableCell.setRightInset(1);
        tableCell.setBottomInset(1);
        tableCell.setTopInset(1);
        tableCell.setBorderColor(TableCell.BorderEdge.bottom,Color.WHITE);
        tableCell.setBorderColor(TableCell.BorderEdge.left,Color.WHITE);
        tableCell.setBorderColor(TableCell.BorderEdge.right,Color.white);
        tableCell.setBorderColor(TableCell.BorderEdge.top,Color.WHITE);
        tableCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        tableCell.setFillColor(fillColor);
        return tableCell;
    }
}
