package pptx.pptReport;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import pptx.apiTest.element.FileProvider;
import pptx.apiTest.exception.PPTxException;
import util.CollectionUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PPTxSlideWriter {
    protected String taskId;
    protected Set<Integer> slidesindex = new HashSet<Integer>();
    protected Map<Integer, XSLFSlide> slideMap = new HashMap<Integer, XSLFSlide>();
    protected Map<String, String> paramMap = new HashMap<String, String>();

    public void writeSlide() throws PPTxException
    {
        throw new PPTxException("can not call writeSlide() of class PPTxSlideWriteWriter,writer of sub class " +
                "overwrite writeSlide method");
    }

    public void initSlideMap(XMLSlideShow slideShow)throws PPTxException
    {
        if(CollectionUtil.isEmpty(slidesindex))
        {
            throw new PPTxException("slidesindex can not be null or empty");
        }

        for(Integer index: slidesindex)
        {
            slideMap.put(index, FileProvider.getSlide(slideShow, index.intValue()));
        }
    }
}
