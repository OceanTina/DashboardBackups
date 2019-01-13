package pptx.apiTest.element;

import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.model.style.BasicShapeStyle;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShapeProvider {




    //List<XSLFTable> shapeListByType = ShapeProvider.getShapeListByType(slide, XSLFTable.class);
    public static <T extends XSLFShape> List<T> getShapeListByType(XSLFSlide slide, Class<T> type)
    {
        return Optional.ofNullable(slide.getShapes()).orElseGet(Collections::emptyList)
                .stream().filter(xslfShape -> type == xslfShape.getClass()).map(xslfShape -> (T)xslfShape)
                .collect(Collectors.toList());
    }
    /**
     * 401获取形状
     */
    public static XSLFShape getShape(XSLFSlide slide, int shapeId)throws PPTxException
    {
        List<XSLFShape> shapes = slide.getShapes();
        for(XSLFShape shape:shapes)
        {
            if(shape instanceof XSLFAutoShape && shape.getShapeId() == shapeId)
            {
                return shape;
            }
        }
        return null;
    }
    /**
     * 402
     */
    public static boolean deleteShape(XSLFSlide slide, int shapeId)throws PPTxException
    {
        try {
            XSLFShape shape = getShape(slide, shapeId);
            System.out.println(shape.getShapeName() + "delete success!");
            slide.removeShape(shape);
        } catch (Exception e) {
            System.out.println(e);
            return true;
        }
        return false;
    }
    /**
     * 403添加多边图形
     */
    public static boolean addAutoShape(XSLFSlide slide, BasicShapeStyle basicShapeStyle)throws PPTxException
    {
        try {
            XSLFAutoShape autoShape = slide.createAutoShape();
            autoShape.setShapeType(basicShapeStyle.getShapeType());
            autoShape.setAnchor(basicShapeStyle.getRectangle2D());
            autoShape.setLineColor(basicShapeStyle.getLineColor());
            autoShape.setFillColor(basicShapeStyle.getFillColor());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * 连接符图形
     */
    public static boolean addConnectorShape(XSLFSlide slide,BasicShapeStyle basicShapeStyle)throws PPTxException
    {
        try {
            XSLFConnectorShape connectorShape = slide.createConnector();
            connectorShape.setShapeType(basicShapeStyle.getShapeType());
            connectorShape.setAnchor(basicShapeStyle.getRectangle2D());
            connectorShape.setLineColor(basicShapeStyle.getLineColor());
            connectorShape.setFillColor(basicShapeStyle.getFillColor());
            connectorShape.setLineHeadDecoration(basicShapeStyle.getLineTailDecoration());
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}
