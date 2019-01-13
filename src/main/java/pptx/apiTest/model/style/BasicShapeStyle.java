package pptx.apiTest.model.style;


import org.apache.poi.sl.usermodel.LineDecoration;
import org.apache.poi.sl.usermodel.ShapeType;
import org.apache.poi.sl.usermodel.TextShape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BasicShapeStyle {
    //形状中线条的颜色
    private Color lineColor = Color.black;
    //形状中填充的颜色
    private Color fillColor = Color.white;
    //形状的位置
    private Rectangle2D rectangle2D = new Rectangle2D.Double(0,0,0,0);
    //形状的类型
    private ShapeType shapeType = ShapeType.RECT;
    //直线的尾部形状
    private LineDecoration.DecorationShape lineTailDecoration = LineDecoration.DecorationShape.NONE;
    //直线的前端形状
    private LineDecoration.DecorationShape lineHeadDecoration = LineDecoration.DecorationShape.NONE;
    //文字
    private String text = "";
    //文字方向
    private TextShape.TextDirection textDirection = TextShape.TextDirection.HORIZONTAL;
    //旋转角度
    private double theta = 0.0;

    public Color getLineColor() {
        return lineColor;
    }

    public BasicShapeStyle(ShapeType shapeType, Rectangle2D rectangle2D) {
        this.rectangle2D = rectangle2D;
        this.shapeType = shapeType;
    }

    public BasicShapeStyle(ShapeType shapeType, Rectangle2D rectangle2D, LineDecoration.DecorationShape lineTailDecoration, LineDecoration.DecorationShape lineHeadDecoration) {
        this.rectangle2D = rectangle2D;
        this.shapeType = shapeType;
        this.lineTailDecoration = lineTailDecoration;
        this.lineHeadDecoration = lineHeadDecoration;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Rectangle2D getRectangle2D() {
        return rectangle2D;
    }

    public void setRectangle2D(Rectangle2D rectangle2D) {
        this.rectangle2D = rectangle2D;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public LineDecoration.DecorationShape getLineTailDecoration() {
        return lineTailDecoration;
    }

    public void setLineTailDecoration(LineDecoration.DecorationShape lineTailDecoration) {
        this.lineTailDecoration = lineTailDecoration;
    }

    public LineDecoration.DecorationShape getLineHeadDecoration() {
        return lineHeadDecoration;
    }

    public void setLineHeadDecoration(LineDecoration.DecorationShape lineHeadDecoration) {
        this.lineHeadDecoration = lineHeadDecoration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextShape.TextDirection getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(TextShape.TextDirection textDirection) {
        this.textDirection = textDirection;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
