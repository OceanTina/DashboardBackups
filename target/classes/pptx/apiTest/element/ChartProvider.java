package pptx.apiTest.element;


import pptx.apiTest.exception.PPTxException;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.charts.ChartData;
import org.apache.poi.xslf.usermodel.XSLFChart;
import org.apache.poi.xslf.usermodel.XSLFSlide;


import java.util.ArrayList;
import java.util.List;

public class ChartProvider {
    /**
     * 701根据slide返回所有XSLFChart的List集合
     */
    public static List<XSLFChart> getChartList(XSLFSlide slide)
    {
        List<XSLFChart> chartList = new ArrayList<XSLFChart>();
        for(POIXMLDocumentPart part:slide.getRelations())
        {
            if(part instanceof XSLFChart)
            {
                XSLFChart chart = (XSLFChart)part;
            }
        }
        return chartList;
    }

    /**
     * 702根据slide和索引返回一个XSLFChart对象
     */
    public static XSLFChart getChart(XSLFSlide slide,int index)throws PPTxException
    {
        XSLFChart chart;
        List<XSLFChart> chartList = getChartList(slide);
        try {
            chart = chartList.get(index);
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            throw new PPTxException("Can not find slide of" + index);
        }
        return chart;
    }

    /**
     * 703更新chart的数据，实质是更新chart包含的Excel
     */
    public static boolean updateData(XSLFChart chart,ChartData data)throws PPTxException
    {
        //先更新chart.xml文件
        updateChartXML(chart, data);
        //更新excel文件
        updateChartXLSX(chart,data);
        return true;
    }

    /**
     * 704更新chart关联的xml文件
     */
    private static void updateChartXML(XSLFChart chart, ChartData data)
    {
        //updateSerList(chart, data);
    }

    /**
     * 705更新chart关联的Excel文件内容
     */
    private static void updateChartXLSX(XSLFChart chart,ChartData data)throws PPTxException
    {
        POIXMLDocumentPart xlsPart = XLSXProvider.getXLSXPart(chart);
        //ExcelHandler.updateXLSXData(xlsPart,data);
    }
}
