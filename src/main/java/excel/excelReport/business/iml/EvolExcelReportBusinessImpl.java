package excel.excelReport.business.iml;

import contstant.CommonConst;
import contstant.Commons;
import contstant.TaskConstant;
import enumeration.EnumPublishMode;
import excel.excelReport.business.inf.EvolExcelReportBusiness;
import exception.ServiceExcept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pptx.apiTest.testppt.FileUtils;
import pptx.pptReport.business.iml.EvolReportBusinessImpl;
import roa.EvolROA;
import source.Task;

import java.util.HashMap;
import java.util.Map;

public class EvolExcelReportBusinessImpl implements EvolExcelReportBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvolReportBusinessImpl.class);
    @Override
    @Task(taskTpe = Commons.TASK_EXCEL_100G, group = TaskConstant.TASK_GROUP_REPORT)
    public String exportExcelReport(String projectId, String reportTaskId, String solutionId)throws ServiceExcept
    {
        TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_5, CommonConst.REPORT_START, false);
        EnumSolutionType solutionType = EvolSolutionUtil.getSolutionTypeBySolution(solutionId);
        switch (solutionType)
        {
            case EVOL_100G_EVALUATION:
                return export100GEvolExcelReport(projectId, solutionId, reportTaskId);
            default:
                LOGGER.error("solution type" + solutionType);
                throw new ServiceExcept();
        }
    }

    private String export100GEvolExcelReport(String taskId, String solutionId, String reportTaskId)throws ServiceExcept
    {
        String excelPath = "";
        String publishMode = AppDefineInfoUtil.getInstance().getAttributeByKey("publishMode");
        boolean isPublishMode = false;
        if(EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
        {
            isPublishMode = true;
            excelPath = TempFileUtils.createTempPath();
        }
        else
        {
            DataPath taskDataPath = TaskRoa.getTaskPath(Integer.valueOf(taskId));
            excelPath = taskDataPath.excelPath();
        }

        ExcelWriter writer = new ExcelWriter("evol100GReport", excelPath);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(EvolROA.PROJECT_ID, taskId);
        paramMap.put(EvolROA.SOLUTION_ID, solutionId);
        paramMap.put(EvolROA.SOLUTION_TYPE, String.valueOf(EnumSolutionType.EVOL_100G_EVALUATION.getSolutionType()));

        String condition = JsonUtil.toJson(paramMap);
        writer.setConditionJson(condition);

        String outputFile = "";

        try {
            TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_10, CommonConst.REPORT_WRITE_START, false);
            outputFile = writer.write();
        } catch (Exception e) {
            e.printStackTrace();
        }

        outputFile = FileUtils.getFormatExcelFilePath(outputFile);
        if(isPublishMode)
        {
            TempFileUtils.clearTempFile(excelPath);
        }
        TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_90, CommonConst.REPORT_FINISHED, false);

        return outputFile;
    }
}
