package pptx.apiTest.element;

import pptx.apiTest.model.style.TextStyle;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextBoxProvider {

    public static void replaceText(XSLFTextShape textShape, String matcher, Object... str)
    {
        int length = str.length;
        int index = 0;
        for(XSLFTextParagraph xslfTextParagraph : textShape)
        {
            for(XSLFTextRun xslfTextRun : xslfTextParagraph)
            {
                String text = xslfTextRun.getRawText();
                Pattern p = Pattern.compile(matcher);
                StringBuffer sb = new StringBuffer();
                Matcher m = p.matcher(text);
                while (index < length && m.find())
                {
                    m.appendReplacement(sb, str[index++].toString());
                }
                m.appendTail(sb);
                xslfTextRun.setText(sb.toString());
                if(index >= length)
                {
                    return;
                }
            }
        }
    }
    /**
     * 601添加文本
     */
    public static void addTextBox(XSLFSlide slide, String text, TextStyle textStyle, Rectangle2D rectangle2D)
    {
        //创建一个textbox,里面会默认一个XSLFTextParagraph
        XSLFTextBox xslfTextBox = slide.createTextBox();
        xslfTextBox.setAnchor(rectangle2D);
        XSLFTextParagraph paragraph = xslfTextBox.getTextParagraphs().get(0);
        XSLFTextRun xslfTextRun = paragraph.getTextRuns().get(0);
        xslfTextBox.setText(text);
        decorateTextStyle(xslfTextRun,textStyle);
    }
    /**
     * 根据shapeId修改文本
     */
    public static void updateTextBox(XSLFSlide slide,int shapeId,String text)
    {
        //获取textbox更改内容
        List<XSLFShape> shapeList = slide.getShapes();
        for(XSLFShape shape:shapeList)
        {
            if(shape instanceof XSLFAutoShape && shape.getShapeId() == shapeId)
            {
                ((XSLFAutoShape)shape).getTextParagraphs().get(0).getTextRuns().get(0).setText(text);
                break;
            }
        }
    }
    /**
     * 根据shapeId修改文本
     */
    public static void updateTextBox(XSLFSlide slide,int shapeId,String text,TextStyle textStyle)
    {
        //获取textbox更改内容
        List<XSLFShape> shapeList = slide.getShapes();
        for(XSLFShape shape:shapeList)
        {
            if(shape instanceof XSLFAutoShape && shape.getShapeId() == shapeId)
            {
                XSLFTextRun xslfTextRun = ((XSLFAutoShape)shape).setText(text);
                decorateTextStyle(xslfTextRun,textStyle);
                break;
            }
        }
    }
    /**
     * 内部方法
     */
    private static void decorateTextStyle(XSLFTextRun xslfTextRun,TextStyle textStyle)
    {
        if(textStyle == null)
        {
            return;
        }
        xslfTextRun.setBold(textStyle.isBold());
        xslfTextRun.setItalic(textStyle.isItalic());
        if(textStyle.getFontSize() != null)
        {
            xslfTextRun.setFontSize(textStyle.getFontSize());
        }
        if(textStyle.getFontColor() != null)
        {
            xslfTextRun.setFontColor(textStyle.getFontColor());
        }
        if(textStyle.getFontFamily() != null)
        {
            xslfTextRun.setFontFamily(textStyle.getFontFamily());
        }
        xslfTextRun.setUnderlined(textStyle.isUnderlined());
    }
}
