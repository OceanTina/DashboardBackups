package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.element.TextBoxProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.model.style.TextStyle;
import pptx.contstant.PPTxContstant;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.util.List;
import java.awt.geom.Rectangle2D;

public class AddTextToPPT {
    public static void main(String[] args) {
        try {
            XMLSlideShow pptx = FileProvider.openPPTxFile(PPTxContstant.PPT_BLANK);
            XSLFSlide slide = FileProvider.createSlide(pptx, "TextBoxTest");
            XSLFTextBox xslfTextBox = slide.createTextBox();
            XSLFTextParagraph paragraph = xslfTextBox.addNewTextParagraph();
            XSLFTextRun xslfTextRun = paragraph.addNewTextRun();
            xslfTextRun.setText("标题一");
            xslfTextRun.setBold(true);
            xslfTextRun.setFontColor(new Color(32, 33, 101));
            xslfTextRun.setFontSize((double)24);
            xslfTextRun.setFontFamily("仿宋_GB2312");
            xslfTextRun.setUnderlined(true);
            xslfTextBox.setAnchor(new Rectangle(200, 300, 100, 100));
            List<XSLFShape> shapeList = slide.getShapes();
            XSLFAutoShape textbox = null;
            for(XSLFShape shape : shapeList)
            {
                if (shape instanceof XSLFAutoShape && shape.getShapeId() == 3)
                {
                    textbox = (XSLFAutoShape) shape;
                    textbox.setText("替换之后的标题");
                    break;
                }
            }
            //addTextBox
            TextStyle style = new TextStyle();
            style.setFontColor(new Color(255, 0, 255));
            style.setFontSize(10d);
            TextBoxProvider.addTextBox(slide, "addTextBox", style, new Rectangle2D.Double(100,
                    100, 800, 100));
            TextStyle styleTwo = new TextStyle();
            styleTwo.setFontColor(new Color(0, 0, 255));
            TextBoxProvider.addTextBox(slide, "addTextTwo", styleTwo, new Rectangle2D.Double(100,
                    200, 800, 100));
            List<XSLFShape> shapeList2 = slide.getShapes();
            for(XSLFShape shape : shapeList2)
            {
                System.out.println("getClass" + shape.getClass() + shape.getShapeId() + shape.getShapeName());
            }
            TextBoxProvider.updateTextBox(slide, 10, "updateTextBoxFirstText");
            FileProvider.savePPTxFile(pptx, PPTxContstant.PPT_OUTPUT);
        } catch (PPTxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
