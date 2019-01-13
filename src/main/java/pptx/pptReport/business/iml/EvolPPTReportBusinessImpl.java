package pptx.pptReport.business.iml;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import contstant.CommonConst;
import contstant.Commons;
import exception.ServiceExcept;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import pptx.apiTest.element.FileProvider;
import pptx.apiTest.exception.PPTxException;
import pptx.pptReport.PPTxSlideWriter;
import pptx.pptReport.PPTxSlideWriterOrganizer;
import pptx.pptReport.business.inf.EvolPPTReportBusiness;
import pptx.pptReport.business.inf.EvolReportBusiness;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EvolPPTReportBusinessImpl implements EvolPPTReportBusiness {

    @Override
    @Task(taskType = PolicyUtils.Commons.Task_PPT_100G, group = TaskConstants.TASK_GROUP_REPORT)
    public String exportPPTReport(String projectId, String reportTaskId, Map<String, String> paramMap, String xuserName)throws ServiceExcept
    {
        TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_5, CommonConst.REPORT_START, false);
        String now2String = DateUtil.date2String("yyyyMMddHHmmss");
        String localName = ThreadLocaleUtil.getLocaleName();
        String publishMode = AppDefineInfoUtil.getInstance().getAttributeByKey("publishMode");
        String templatePath = DefaultEnvUtil.getAppRoot() + File.separator + Commons.TEMPLATE;
        paramMap.put("userId", xuserName);
        String pptRealPath = templatePath + File.separator + NAME_EVOLNC_PPT + "_" + localName + Commons.PPTX_EXTENSION;

        try {
            File file = new File(pptRealPath);
            if(file.exists())
            {
                LOGGER.error("catch PPT Model file:" + pptRealPath);
            }
            else
            {
                throw new FileNotFoundException("PPT Model file not found!");
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException");
        }
        String topoDir = Commons.ROOT_TEMP_DIR + File.separator + Commons.DIR_TOPO_IMGS + now2String;
        if(EnumPublishMode.DISTRIBUTED.getVale().equals(publishMode))
        {
            String imgPath = DistributedModeUtil.downloadFile(paramMap.get(Commons.PARAM_TOPOIMG_PATH), topoDir);
            paramMap.put(Commons.PARAM_TOPOIMG_PATH, imgPath);
        }

        try {
            String outputPath = Commons.ROOT_TEMP_DIR + File.separator + Commons.DIR_TEMP_PPT + now2String;
            String pptName = MessageUtil.getMessage("com.netstar.evolution.report.pptx.name") + "_" + now2String +
                    Commons.PPTX_EXTENSION;
            String outputPPTxPath = outputPath + File.separator + pptName;

            TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_10, CommonConst.REPORT_WRITE_START, false);

            generatePPTx(projectId, pptRealPath, outputPPTxPath, paramMap);
            TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_80, CommonConst.REPORT_WRITE_FINISHED, false);
        } catch (PPTxException e) {
            LOGGER.error("generate EVOLNC PPTx error!" e);
            TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_80, CommonConst.REPORT_WRITE_EXCEPTION, true);
            TempFileUtils.clearTempFile(topoDir);
            TempFileUtils.clearTempFile(outputPath);
            return "";
        }

        if(EnumPublishMode.DISTRIBUTED.getVale().equals(publishMode))
        {
            outputPPTxPath = DistributedModeUtil.uploadFile(String.valueOf(projectId), outputPPTxPath);
        }
        TempFileUtils.clearTempFile(topoDir);
        TempFileUtils.clearTempFile(outputPath);
        TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_90, CommonConst.REPORT_FINISHED, false);
        return outputPPTxPath;
    }



    private void generatePPTx(String taskId, String pptModelPath, String pptOutputPath, Map<String, String> paramMap)throws PPTxException
    {
        XMLSlideShow slideShow = new XMLSlideShow();
        List<PPTxSlideWriter> ppTxSlideWriterList = new ArrayList<>();


        try {
            slideShow = FileProvider.copyPPTxFile(pptModelPath, pptOutputPath, null);
            PPTxSlideWriterOrganizer.begin(taskId, slideShow, paramMap);
            FileProvider.savePPTxFile(slideShow, pptModelPath);
            LOGGER.error("success:generate EVOLNC PPT");
        } catch (PPTxException e) {
            LOGGER.error("error! PPTxException:", e);
        }
    }
}
