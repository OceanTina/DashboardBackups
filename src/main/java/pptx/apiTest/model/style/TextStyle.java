package pptx.apiTest.model.style;

import java.awt.*;

public class TextStyle {
    private boolean isBold = false;
    private boolean isItalic = false;
    private Double fontSize = 16.0;
    private Color fontColor = Color.black;
    private String fontFamily;
    private boolean isUnderlined = false;

    public TextStyle() {
    }

    public TextStyle(boolean isBold, boolean isItalic, Double fontSize, Color fontColor, String fontFamily, boolean isUnderlined) {
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.fontFamily = fontFamily;
        this.isUnderlined = isUnderlined;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public Double getFontSize() {
        return fontSize;
    }

    public void setFontSize(Double fontSize) {
        this.fontSize = fontSize;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public boolean isUnderlined() {
        return isUnderlined;
    }

    public void setUnderlined(boolean underlined) {
        isUnderlined = underlined;
    }
}
