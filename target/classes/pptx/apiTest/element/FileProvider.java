package pptx.apiTest.element;

import pptx.apiTest.exception.PPTxException;
import pptx.apiTest.testppt.FileUtils;

import org.apache.poi.xslf.usermodel.*;


import java.io.*;
import java.util.List;

public class FileProvider {
    /**
     * 801打开PPTX文件
     */
    public static XMLSlideShow openPPTxFile(String file)throws PPTxException
    {
        FileInputStream is = null;
        XMLSlideShow pptx = null;
        try {
            is = new FileInputStream(file);
            pptx = new XMLSlideShow(is);
        }
        catch (IOException e)
        {
            throw new PPTxException("open PPTx file fail");
        }
        finally {
            if(is != null)
            {
                try {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return pptx;
    }

    /**
     * 802保存PPTx
     */
    public static void savePPTxFile(XMLSlideShow pptx, String filePath)throws PPTxException
    {
        OutputStream out = null;
        try
        {
            //保存文件
            out = new FileOutputStream(filePath);
            pptx.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new PPTxException("save PPTx file faild");
        }
        finally {
           if(out == null)
           {
               try {
                   out.close();
               }
               catch (IOException e)
               {
                   e.printStackTrace();
               }
           }
        }
    }

    /**
     * 803关闭PPTx对象
     */
    public static void closePPTx(XMLSlideShow pptx)throws PPTxException
    {
        try {
            if(pptx != null)
            {
                pptx.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new PPTxException("close PPTx file fail");
        }
    }


    /**
     * 804从pptx中赋值执行序列的slide到另一个文件中
     */
    public synchronized static XMLSlideShow copyPPTxFile(String srcPath,String outputPath,List<Integer> indexList)throws PPTxException
    {
        File srcFile = new File(srcPath);
        File outputFile = new File(outputPath);
        try {
            FileUtils.copyFile(srcFile,outputFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new PPTxException("copy file faild");
        }
        XMLSlideShow outputPPTx = openPPTxFile(outputPath);
        List<XSLFSlide> slides = outputPPTx.getSlides();
        //如果indexList为空，则认为copy全部
        if(null == indexList || 0 >= indexList.size())
        {
            return outputPPTx;
        }
        //否则默认copy indexList里面的slide
        for(int i = slides.size() - 1;i>=0;i--)
        {
            XSLFSlide slide = slides.get(i);
            if(!indexList.contains(slide.getSlideNumber() - 1))
            {
                outputPPTx.removeSlide(slide.getSlideNumber()-1);
            }
        }
        return outputPPTx;
    }

    /**
     * 创建slide
     */
    public static XSLFSlide createSlide(XMLSlideShow pptx, String title)throws PPTxException
    {
        XSLFSlideMaster defaultMaster = pptx.getSlideMasters().get(0);
        XSLFSlideLayout layout = defaultMaster.getLayout("TITLE_AND_CONTENT");
        if(null == layout)
        {
            throw new PPTxException("NO TITLE_AND_CONTENT layout found in ppt template file");
        }
        XSLFSlide slide = pptx.createSlide(layout);
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setText(title);
        return slide;
    }

    /**
     * 805创建slide
     */
    public static XSLFSlide createSlideUseDefaultLayout(XMLSlideShow pptx, String title)throws PPTxException
    {
        XSLFSlideMaster defaultMaster = pptx.getSlideMasters().get(0);
        XSLFSlideLayout layout = defaultMaster.getLayout("TITLE_AND_CONTENT");

        if(layout == null)
        {
            throw new PPTxException("Layout is not found in ppt template file");
        }
        XSLFSlide slide = pptx.createSlide(layout);
        if(title != null)
        {
            XSLFTextShape title1 = slide.getPlaceholder(0);
            title1.setText(title);
        }
        return slide;
    }


    /**
     * 806根据index索引获取slide对象
     */
    public static XSLFSlide getSlide(XMLSlideShow pptx, int index)throws PPTxException
    {
        List<XSLFSlide> slides = pptx.getSlides();
        if (index >= slides.size())
        {
            throw new PPTxException("index is out of Boundary");
        }
        return slides.get(index);
    }

    /**
     * 807合并两个PPTx对象
     */
    public static void mergePPTxFile(XMLSlideShow srcPPTx, XMLSlideShow subPPTx)throws PPTxException
    {
        for(XSLFSlide srcSlide:subPPTx.getSlides())
        {
            createSlide(srcPPTx,"merge").importContent(srcSlide);
        }
    }

}
