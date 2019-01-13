package pptx.apiTest.model.data;

import pptx.apiTest.model.style.ThresholdShapeStyle;

public class ThresholdData {
    private int lowPercentage;
    private int highPercentage;
    private ThresholdShapeStyle style = new ThresholdShapeStyle();

    public ThresholdData(int lowPercentage, int highPercentage, ThresholdShapeStyle style) {
        this.lowPercentage = lowPercentage;
        this.highPercentage = highPercentage;
        this.style = style;
    }

    public int getLowPercentage() {
        return lowPercentage;
    }

    public void setLowPercentage(int lowPercentage) {
        this.lowPercentage = lowPercentage;
    }

    public int getHighPercentage() {
        return highPercentage;
    }

    public void setHighPercentage(int highPercentage) {
        this.highPercentage = highPercentage;
    }

    public ThresholdShapeStyle getStyle() {
        return style;
    }

    public void setStyle(ThresholdShapeStyle style) {
        this.style = style;
    }
}
