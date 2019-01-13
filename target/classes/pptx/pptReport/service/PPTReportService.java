package pptx.pptReport.service;

import contstant.CommonConst;
import contstant.Commons;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import pptx.pptReport.business.inf.EvolReportBusiness;
import roa.EvolROA;
import source.PUT;
import source.Path;
import source.Task;

import javax.xml.ws.spi.http.HttpContext;
import java.util.HashMap;
import java.util.Map;

public class PPTReportService {

    @Autowired
    private EvolReportBusiness evolReportBusiness;

    @PUT
    @Path(EvolROA.URL_EVOL_TASK_PPT)
    @Task(taskType = Commons.TASK_PPT_100G, group = TaskConstants.TASK_GROUP_REPORT)
    public String do100GReportTask(HttpContext context, @QueryParam("project-id") final String projectId)throws ServiceException {
        String reportTaskId = TaskHttpUtil.getTaskId(context.getHttpServletRequest());
        if (StringUtil.isNullOrEmpty(projectId) || StringUtil.isNullOrEmpty(reportTaskId)) {
            LOGGER.error("taskId {} export ppt failed since reportTaskId is {}", new Object[]{projectId, reportTaskId});
            throw new ServiceException(EvolErrorCode.ERROR_INVALID_SOLUTIONID, HttpStatus2.BAD_REQUEST_400);
        }
        String params = "";
        try {
            params = StringUtil.toString(context.getHttpServletRequest().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (StringUtil.isNullOrEmpty(params)) {
            LOGGER.error("taskId {} export ppt failed since param invalid: {}-{}",
                    new Object[]{projectId, reportTaskId, params});
            TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_5, CommonConst.REPORT_BAD_PARAM, true);
            return "";
        }

        Map<String, String> conditionMap = new HashMap<>();
        try {
            conditionMap = JosonUtil.fromJson(params, new TypeReference<Map<String, String>>() {
            });
        } catch (IllegalArgumentException e) {
            LOGGER.error("taskId" + projectId + "export ppt failed since param transform exception:", e);
            TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_5, CommonConst.REPORT_BAD_PARAM,
                    true);
            return "";
        }
        String xuserName = "";
        if(conditionMap.containsKey("userName"))
        {
            xuserName = conditionMap.get("userName");
        }
        String filePath = evolReportBusiness.exportPPTReport(projectId, reportTaskId, conditionMap, xuserName);
        TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_99, CommonConst.REPORT_FINISHED, false);
        return filePath;
    }
}
