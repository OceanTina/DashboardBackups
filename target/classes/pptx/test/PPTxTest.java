package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.element.TextBoxProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.model.style.TextStyle;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;


import java.awt.geom.Rectangle2D;

import java.util.List;

public class PPTxTest {
    public static void main(String[]args) throws PPTxException {
        String templatePath = "F:\\JAVA2018\\test01.pptx";
        String text = "Hello PPTx World!";
        System.out.println("this is a test!");

        //打开ppt
        XMLSlideShow slideShow = FileProvider.openPPTxFile(templatePath);
        List<XSLFSlide> slideList = slideShow.getSlides();
        System.out.println(slideList.size());
        XSLFSlide  slide = slideList.get(0);
        XSLFSlideLayout xsfsl = slide.getSlideLayout();
        System.out.println(xsfsl);
        TextStyle textStyle = new TextStyle();

        TextBoxProvider.addTextBox(slide, text, textStyle, new Rectangle2D.Double(5,6,7,8));



    }


}
