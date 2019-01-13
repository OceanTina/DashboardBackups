package pptx.apiTest.element;

import pptx.apiTest.exception.PPTxException;

import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageProvider {
    /**
     * 301添加Image到slide
     */
    public static void addImage(XMLSlideShow pptx, XSLFSlide slide, String imagePath, PictureType pictureType, int x, int y,
                                int width, int height)throws PPTxException
    {
//        try {
//            File img = new File(imagePath);
//            XSLFPictureData pictureData = pptx.addPicture(img,pictureType);
//            XSLFPictureShape shape = slide.createPicture(pictureData);
//            shape.setAnchor(new Rectangle(x,y,width,height));
//        }
//        catch (IOException e)
//        {
//            throw new PPTxException("Image not found");
//        }
    }

    /**
     * 302删除图片
     */
    public static void deleteImage(XMLSlideShow pptx, XSLFSlide slide,int shapeId)throws PPTxException
    {
        //获取textbox更改内容
        List<XSLFShape> shapeList = slide.getShapes();
        for(XSLFShape shape:shapeList)
        {
            if(shape instanceof XSLFPictureShape && shape.getShapeId() == shapeId)
            {
                XSLFPictureData pictureData = ((XSLFPictureShape)shape).getPictureData();
            }
        }
    }
}
