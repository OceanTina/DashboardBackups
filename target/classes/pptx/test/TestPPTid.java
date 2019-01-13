package test;

import pptx.apiTest.element.FileProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.contstant.PPTxContstant;
import org.apache.poi.xslf.usermodel.*;

import java.util.List;

public class TestPPTid {
    public static void main(String[] args) throws Exception
    {
        String model = PPTxContstant.PPT_TEMPLATE;
        XMLSlideShow slideShow = FileProvider.openPPTxFile(model);

        try {
            List<XSLFSlide> slideList = slideShow.getSlides();
            XSLFSlide slide = slideList.get(0);

            System.out.println(slide.getSlideNumber());

            int i = 0;
            for(XSLFSlide xslfShapes : slideList)
            {
                System.out.println("当前第" + xslfShapes.getSlideNumber() + "页");
                for(XSLFShape shape : xslfShapes)
                {
                    System.out.println("-----------[" + ++i + "]-------------");
                    System.out.println("shapeClass:" + shape.getClass());
                    System.out.println("shapeName:" + shape.getShapeName());
                    System.out.println("shapeId:" + shape.getShapeId());
                    if(shape instanceof XSLFTextBox)
                    {
                        String a = ((XSLFTextBox)shape).getText();
                        System.out.println("TEXT:\t" + a);
                    }

                    if(shape instanceof XSLFGroupShape)
                    {
                        List<XSLFShape> list = ((XSLFGroupShape)shape).getShapes();
                        int o = 0;
                        for(XSLFShape innerShape : list)
                        {
                            System.out.println(++o + "\t");
                            if(innerShape instanceof XSLFTextBox)
                            {
                                System.out.println(((XSLFTextBox)innerShape).getText());
                            }
                            else
                            {
                                System.out.println("");
                            }
                        }
                    }

                    if(shape instanceof XSLFPictureShape)
                    {
                        System.out.println(((XSLFPictureShape)shape).getPictureData());
                    }
                }
            }
            FileProvider.closePPTx(slideShow);
        } catch (PPTxException e) {
            e.printStackTrace();
        }
    }
}
