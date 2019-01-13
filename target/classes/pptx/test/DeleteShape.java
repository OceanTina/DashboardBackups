package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.element.ShapeProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.contstant.PPTxContstant;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.util.List;

public class DeleteShape {
    public static void main(String[] args) {
        try {
            //测试获取形状
            XMLSlideShow pptx1 = FileProvider.openPPTxFile(PPTxContstant.PPT_OUTPUT);
            List<XSLFSlide> slides = pptx1.getSlides();
            ShapeProvider.deleteShape(slides.get(1), 11);

            //保存PPTx文件
            FileProvider.savePPTxFile(pptx1, PPTxContstant.PPT_OUTPUT);
            //关闭PPTx文件
            FileProvider.closePPTx(pptx1);

        } catch (PPTxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
