package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.element.ShapeProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.model.style.BasicShapeStyle;
import pptx.contstant.PPTxContstant;
import org.apache.poi.sl.usermodel.LineDecoration;
import org.apache.poi.sl.usermodel.ShapeType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.awt.geom.Rectangle2D;

public class BasicChart {
    public static void main(String[] args) {
        try {
            XMLSlideShow pptx = FileProvider.openPPTxFile(PPTxContstant.PPT_TEMPLATE);
            XSLFSlide slide = FileProvider.createSlide(pptx, "BasicShapeTest");
            //List<XSLFSlide> slides = pptx.getSlides();
            //椭圆
            Rectangle2D ellipse = new Rectangle2D.Double(100, 100, 100, 100);
            BasicShapeStyle ellipseShape = new BasicShapeStyle(ShapeType.ELLIPSE, ellipse);
            ShapeProvider.addAutoShape(slide, ellipseShape);
            //等腰三角形
            Rectangle2D triangle = new Rectangle2D.Double(200, 100, 100, 100);
            BasicShapeStyle triangleShape = new BasicShapeStyle(ShapeType.TRIANGLE, triangle);
            ShapeProvider.addAutoShape(slide, triangleShape);
            //矩形
            Rectangle2D rect = new Rectangle2D.Double(200, 100, 100, 100);
            BasicShapeStyle rectShape = new BasicShapeStyle(ShapeType.RECT, rect);
            ShapeProvider.addAutoShape(slide, rectShape);
            //菱形
            Rectangle2D diamond = new Rectangle2D.Double(300, 300, 100, 100);
            BasicShapeStyle diamondShape = new BasicShapeStyle(ShapeType.DIAMOND, diamond);
            ShapeProvider.addAutoShape(slide, diamondShape);
            //八边形
            Rectangle2D octagon = new Rectangle2D.Double(400, 300, 100, 100);
            BasicShapeStyle octagonShape = new BasicShapeStyle(ShapeType.OCTAGON, octagon);
            ShapeProvider.addAutoShape(slide, octagonShape);
            //直线
            Rectangle2D line = new Rectangle2D.Double(400, 100, 100, 100);
            BasicShapeStyle lineShape = new BasicShapeStyle(ShapeType.LINE, line,
                    LineDecoration.DecorationShape.NONE, LineDecoration.DecorationShape.NONE);
            ShapeProvider.addAutoShape(slide, lineShape);
            //箭头直线
            Rectangle2D arrowLine = new Rectangle2D.Double(500, 100, 100, 100);
            BasicShapeStyle arrowLineShape = new BasicShapeStyle(ShapeType.LINE, arrowLine,
                    LineDecoration.DecorationShape.NONE, LineDecoration.DecorationShape.ARROW);
            ShapeProvider.addAutoShape(slide, arrowLineShape);
            //两头圆形直线
            Rectangle2D roundCapLine = new Rectangle2D.Double(600, 100, 100, 100);
            BasicShapeStyle roundCapLineShape = new BasicShapeStyle(ShapeType.LINE, roundCapLine,
                    LineDecoration.DecorationShape.OVAL, LineDecoration.DecorationShape.OVAL);
            ShapeProvider.addAutoShape(slide, roundCapLineShape);
            //圆加箭头直线
            Rectangle2D roundCapArrowLine = new Rectangle2D.Double(600, 100, 100, 100);
            BasicShapeStyle roundCapArrowLineShape = new BasicShapeStyle(ShapeType.LINE, roundCapArrowLine,
                    LineDecoration.DecorationShape.OVAL, LineDecoration.DecorationShape.ARROW);
            ShapeProvider.addAutoShape(slide, roundCapArrowLineShape);


            //保存PPTx文件
            FileProvider.savePPTxFile(pptx, PPTxContstant.PPT_OUTPUT);
            //关闭PPTx对象
            FileProvider.closePPTx(pptx);

        } catch (PPTxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
