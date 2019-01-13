package pptx.apiTest.element;

import pptx.apiTest.exception.PPTxException;
import pptx.contstant.PPTxContstant;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xslf.usermodel.XSLFChart;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.util.List;

public class XLSXProvider {
    /**
     * 701根据名称获取嵌入的Excel文件对象，名称就是1,2,3,4等数字
     */
    public static POIXMLDocumentPart getXLSXPart(XSLFSlide slide,String name)throws PPTxException
    {
        POIXMLDocumentPart xlsPart = null;
        String searchName = PPTxContstant.EMB_PREFFIX + PPTxContstant.EMB_XLSX_PREFFIX + name +
                PPTxContstant.XLSX_SUFFIX;
        for(POIXMLDocumentPart part:slide.getRelations())
        {
            String partName = part.getPackagePart().getPartName().getName();
            if(partName.equals(searchName))
            {
                xlsPart = part;
            }
        }
        return xlsPart;
    }
    /**
     * 查找chart对象相关联的Excel的POI包装对象，根据这个Excel的包装对象可以获取Excel的POI对象和文件流
     */
    public static POIXMLDocumentPart getXLSXPart(XSLFChart chart)
    {
        POIXMLDocumentPart result = null;
        List<POIXMLDocumentPart> parts = chart.getRelations();
        for(POIXMLDocumentPart part:parts)
        {
            if(part.getPackagePart().getPartName().toString().contains("Microsoft_Excel____"))
            {
                result = part;
                break;
            }
        }
        return result;
    }
}
