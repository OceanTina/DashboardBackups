package excel.excelReport.service;

import contstant.CommonConst;
import contstant.Commons;
import contstant.TaskConstant;
import excel.excelReport.business.inf.EvolExcelReportBusiness;
import exception.ServiceExcept;
import org.springframework.beans.factory.annotation.Autowired;
import roa.EvolROA;
import source.PUT;
import source.Path;
import source.QueryParam;
import source.Task;

import javax.xml.ws.spi.http.HttpContext;

public class excelService {

    @Autowired
    private EvolExcelReportBusiness evolEXCELReportBusiness;
    @PUT
    @Path(EvolROA.URL_EVOL_TASK_EXCEL)
    @Task(taskTpe = Commons.TASK_EXCEL_100G, group = TaskConstant.TASK_GROUP_REPORT)
    public String do100GExcelTask(HttpContext context, @QueryParam("project-id") final String projectId)throws ServiceExcept
    {
        String reportTaskId = TaskHttpUtil.getTaskId(context.getHttpServletRequest());
        String solutionId = "";
        String filePath = "";

        String filePath = evolEXCELReportBusiness.exportExcelReport(projectId, reportTaskId, solutionId);

        TaskProgressStateUtil.updateTaskProgress(reportTaskId, CommonConst.PROGRESS_99, CommonConst.REPORT_FINISHED, false);
        return filePath;
    }
}
