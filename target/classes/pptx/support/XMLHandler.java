package support;

import org.apache.poi.xslf.usermodel.XSLFChart;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;


public class XMLHandler {
    public static Element getChartTypeNode(XSLFChart chart)
    {
        Element chartNode = (Element)chart.getCTChart().getDomNode();
        Element plotArea = getElementByTag(chartNode, "c:plotArea");
        return (Element)plotArea.getChildNodes().item(1);
    }

    public static Element getElementByTag(Element node, String tag)
    {
        return (Element)node.getElementsByTagName(tag).item(0);
    }

    public static void setElementValue(Element node, String val)
    {
        node.getFirstChild().setNodeValue(val);
    }

    public static void clearNodeList(NodeList nodeList)
    {
        while (nodeList.getLength() > 0)
        {
            nodeList.item(0).getParentNode().removeChild(nodeList.item(0));
        }
    }

    public static void setPtTagValue(Element pt, String val)
    {
        setElementValue((Element)pt.getFirstChild(), val);
    }

    private static void addPtTag(Node parent, Element pt, List<String> list, int n)
    {
        for(int i = 0; i < n; i++)
        {
            Element ptNew = (Element)pt.cloneNode(true);
            ptNew.setAttribute("idx", "" + i);
            if(list != null)
            {
                setPtTagValue(ptNew, list.get(i));
                parent.appendChild(ptNew);
            }
        }
    }

    public static void replacePtTag(NodeList nodeList, Element pt, List<String> list, int n)
    {
        Node parent = nodeList.item(0).getParentNode();
        clearNodeList(nodeList);
        addPtTag(parent, pt, list, n);
    }

    public static Element getSerTagTemplate(XSLFChart chart)
    {
        Element chartType = getChartTypeNode(chart);
        NodeList serList = chartType.getElementsByTagName("c:ser");
        Element serTemplate = (Element)serList.item(0).cloneNode(true);
        //删除原有ser标签
        clearNodeList(serList);
        return serTemplate;
    }

    public static NodeList updateCatAndVal(Element ser, List<String> list)
    {
        int num = list.size();
        Element cat = getElementByTag(ser, "c:cat");
        Element cfNode = getElementByTag(cat, "c:f");
        setElementValue(cfNode, "Sheet0!$A$2:$A$" + (num + 1));

        Element strRef = getElementByTag(cat, "c:strRef");
        Element ptCount = getElementByTag(strRef, "c:ptCount");
        ptCount.setAttribute("val","" + num);

        //取到pt的模板
        Element strCache = getElementByTag(strRef, "c:strCache");
        NodeList ptList = strCache.getElementsByTagName("c:pt");
        Element ptTemplate = (Element)ptList.item(0).cloneNode(true);
        replacePtTag(ptList, ptTemplate, list, num);
        //更新val标签
        Element val = getElementByTag(ser, "c:val");
        Element numRef = getElementByTag(val, "c:numRef");
        Element numCache = getElementByTag(numRef, "c:numCache");
        Element valPtCount = getElementByTag(numCache, "c:ptCount");
        valPtCount.setAttribute("val", "" + num);
        NodeList valPtList = numCache.getElementsByTagName("c:pt");
        replacePtTag(valPtList, ptTemplate, null, num);
        return valPtList;
    }
}
