package pptx.pptReport;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import pptx.apiTest.exception.PPTxException;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//spring-beans-4.1.9RELEASE.jar
public class PPTxSlideWriterOrganizer implements DestructionAwareBeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PPTxSlideWriterOrganizer.class);

    private static List<PPTxSlideWriter> writerList = new ArrayList<PPTxSlideWriter>();

    public PPTxSlideWriterOrganizer() {
    }


    public static void begin(String taskId, XMLSlideShow slideShow, Map<String, String> paramMap)throws PPTxException
    {
        if(CollectionUtil.isEmpty(writerList))
        {
            LOGGER.error("doAction error:writerList empty!" );
            return;
        }


        try {
            for (PPTxSlideWriter writer: writerList)
            {
                assignParam2Writer(writer, taskId, paramMap);
                assignSlideMap2Writer(writer, slideShow);
            }
        } catch (PPTxException e) {
            e.printStackTrace();
        }

        //全部slide页及参数分配完成后，再执行修改、删除等操作（否则删除会出错）

        try {
            for(PPTxSlideWriter writer: writerList)
            {
                writer.writeSlide();
            }
        } catch (PPTxException e) {
            e.printStackTrace();
        }
    }

    private static void assignParam2Writer(PPTxSlideWriter writer, String taskId, Map<String, String> paramMap)
    {
        writer.taskId = taskId;
        writer.paramMap = paramMap;
    }

    private static void assignSlideMap2Writer(PPTxSlideWriter writer, XMLSlideShow slideShow)throws PPTxException
    {
        if(null == slideShow || CollectionUtils.isEmpty(slideShow.getSlides()))
        {
            throw new PPTxException("null PPTxSlideShow!");
        }

        writer.initSlideMap(slideShow);
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        if(isPPTxWriter(bean))
        {
            LOGGER.error("PPTxSlideWriter = init:" + name);
            PPTxSlideWriter writer = (PPTxSlideWriter)bean;
            writerList.add(writer);
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object o, String s)throws BeansException
    {}

    private boolean isPPTxWriter(Object bean)
    {
        return null != bean && PPTxSlideWriter.class.isAssignableFrom(bean.getClass());
    }




}
