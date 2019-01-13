package excel;

import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ColumnMeta extends Meta {
    private String type;
    private String fieldName;
    private boolean isMerge;
    private int associationMerge = -1;

    private short fontColor = IndexedColors.BLACK.getIndex();
    private short fillColor = IndexedColors.WHITE.getIndex();
    private int underLineType = FontUnderline.NONE.getValue();
    private boolean isBold = false;
    private boolean isItalic = false;


    public ColumnMeta(String name) {
        super(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isMerge() {
        return isMerge;
    }

    public void setMerge(boolean merge) {
        isMerge = merge;
    }

    public int getAssociationMerge() {
        return associationMerge;
    }

    public void setAssociationMerge(int associationMerge) {
        this.associationMerge = associationMerge;
    }

    public short getFontColor() {
        return fontColor;
    }

    public void setFontColor(short fontColor) {
        this.fontColor = fontColor;
    }

    public short getFillColor() {
        return fillColor;
    }

    public void setFillColor(short fillColor) {
        this.fillColor = fillColor;
    }

    public int getUnderLineType() {
        return underLineType;
    }

    public void setUnderLineType(int underLineType) {
        this.underLineType = underLineType;
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
}
