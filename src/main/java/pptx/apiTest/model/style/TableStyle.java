package pptx.apiTest.model.style;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TableStyle {
    //数据偶数行填充色
    private Color erenColor = new Color(255,246,234);
    //奇数行填充色
    private Color oddColor = new Color(123,11,34);
    //表头填充色
    private Color headerColor = new Color(11,22,33,44);
    //表头文字颜色
    private Color headFontColor = new Color(22,33,44,55);
    //数据文字颜色
    private Color dataFontColor = new Color(11,22,33,44);
    //表头行高
    private int headerRowHight = 40;
    //表头文字大小
    private double headerFontSize = 14;
    //数据行高
    private int dataRowHight = 25;
    //数据字体大小
    private double dataFontSize = 12;
    //列宽集合，可为空，从第一列按顺序设置，如果读取不到就不进行设置
    private List<Integer> columnWidthList = new ArrayList<>();

    public Color getErenColor() {
        return erenColor;
    }

    public void setErenColor(Color erenColor) {
        this.erenColor = erenColor;
    }

    public Color getOddColor() {
        return oddColor;
    }

    public void setOddColor(Color oddColor) {
        this.oddColor = oddColor;
    }

    public Color getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(Color headerColor) {
        this.headerColor = headerColor;
    }

    public Color getHeadFontColor() {
        return headFontColor;
    }

    public void setHeadFontColor(Color headFontColor) {
        this.headFontColor = headFontColor;
    }

    public Color getDataFontColor() {
        return dataFontColor;
    }

    public void setDataFontColor(Color dataFontColor) {
        this.dataFontColor = dataFontColor;
    }

    public int getHeaderRowHight() {
        return headerRowHight;
    }

    public void setHeaderRowHight(int headerRowHight) {
        this.headerRowHight = headerRowHight;
    }

    public double getHeaderFontSize() {
        return headerFontSize;
    }

    public void setHeaderFontSize(double headerFontSize) {
        this.headerFontSize = headerFontSize;
    }

    public int getDataRowHight() {
        return dataRowHight;
    }

    public void setDataRowHight(int dataRowHight) {
        this.dataRowHight = dataRowHight;
    }

    public double getDataFontSize() {
        return dataFontSize;
    }

    public void setDataFontSize(double dataFontSize) {
        this.dataFontSize = dataFontSize;
    }

    public List<Integer> getColumnWidthList() {
        return columnWidthList;
    }

    public void setColumnWidthList(List<Integer> columnWidthList) {
        this.columnWidthList = columnWidthList;
    }
}
