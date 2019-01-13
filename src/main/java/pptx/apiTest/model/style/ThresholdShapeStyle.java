package pptx.apiTest.model.style;

import org.apache.poi.sl.usermodel.TextShape;

import java.awt.*;

public class ThresholdShapeStyle {
    //长条长度
    private int longStripWidth = 200;
    private int longStripHight = 15;
    //滑块长度
    private int blockWidth = 15;
    private int blockHight = 15;
    //底部开始文字
    private String bottomStartText = "Low";
    private String bottomEndText = "Hight";
    //低百分比阈颜色
    private Color lowColor = Color.green;
    //中百分比阈颜色
    private Color middleColor = Color.orange;
    //高百分比阈颜色
    private Color hightColor = Color.red;
    //中-低之间的滑块颜色
    private Color lowMiddleBlockColor = Color.green;
    //中-高之间的滑块颜色
    private Color hightMiddleBlockColor = Color.RED;
    //字体大小
    private double textSize = 10.0;
    //字体方向
    private TextShape.TextDirection textDirection = TextShape.TextDirection.HORIZONTAL;

    public int getLongStripWidth() {
        return longStripWidth;
    }

    public void setLongStripWidth(int longStripWidth) {
        this.longStripWidth = longStripWidth;
    }

    public int getLongStripHight() {
        return longStripHight;
    }

    public void setLongStripHight(int longStripHight) {
        this.longStripHight = longStripHight;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
    }

    public int getBlockHight() {
        return blockHight;
    }

    public void setBlockHight(int blockHight) {
        this.blockHight = blockHight;
    }

    public String getBottomStartText() {
        return bottomStartText;
    }

    public void setBottomStartText(String bottomStartText) {
        this.bottomStartText = bottomStartText;
    }

    public String getBottomEndText() {
        return bottomEndText;
    }

    public void setBottomEndText(String bottomEndText) {
        this.bottomEndText = bottomEndText;
    }

    public Color getLowColor() {
        return lowColor;
    }

    public void setLowColor(Color lowColor) {
        this.lowColor = lowColor;
    }

    public Color getMiddleColor() {
        return middleColor;
    }

    public void setMiddleColor(Color middleColor) {
        this.middleColor = middleColor;
    }

    public Color getHightColor() {
        return hightColor;
    }

    public void setHightColor(Color hightColor) {
        this.hightColor = hightColor;
    }

    public Color getLowMiddleBlockColor() {
        return lowMiddleBlockColor;
    }

    public void setLowMiddleBlockColor(Color lowMiddleBlockColor) {
        this.lowMiddleBlockColor = lowMiddleBlockColor;
    }

    public Color getHightMiddleBlockColor() {
        return hightMiddleBlockColor;
    }

    public void setHightMiddleBlockColor(Color hightMiddleBlockColor) {
        this.hightMiddleBlockColor = hightMiddleBlockColor;
    }

    public double getTextSize() {
        return textSize;
    }

    public void setTextSize(double textSize) {
        this.textSize = textSize;
    }

    public TextShape.TextDirection getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(TextShape.TextDirection textDirection) {
        this.textDirection = textDirection;
    }
}
