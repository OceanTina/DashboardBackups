package pptx.pptReport.business.inf;

import java.util.Map;

public interface EvolPPTReportBusiness {

    public String exportPPTReport(String projectId, String reportTaskId, Map<String, String> paramMap, String xuserName);
}
