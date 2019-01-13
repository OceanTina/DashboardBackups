package servlet;

import org.codehaus.jackson.map.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadExcelServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadExcelServlet.class);
    private static final String REPORT_DOWNLOAD_NOT_FOUND = "com.netstar.report.download.message.notfound";
    private static final String REPORT_DOWNLOAD_WRITE_ERROR = "com.netstar.report.download.message.writeerror";
    private static final String URL_GET_TASK_PATH = "/rest/netstar/pmapp/v1/tasks/{0}/path";
    private static final String DBAPP_VAR_REPORT_DIR = PlatformEnvUtil.getAppVarPath() + File.separator + "report";

    @Override
    public void init(ServletConfig config) throws ServletException
    {}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String msgStr;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.setHeader("content-type", "text/html;charset=UTF-8");

            Cookie[] cookies = request.getCookies();
            //通过Cookie数组获取当前语言环境字符串
            String localName = LocaleUtil.getLocale(cookies);

            // 项目ID
            String taskId = request.getParameter("task-id");
            String jsonStr = request.getParameter("types");
            int[] excelTypes = JsonUtil.fromJson(jsonStr, int[].class);
            if (taskId == null)
            {
                return;
            }

            ExcelInfo excelInfo = new ExcelInfo();

            String publishMode = AppDefineInfoUtil.getInstance().getAttributeByKey("publishMode");

            List<ReportFileInfo> excelInfoList;

            try
            {
                //获取下载的文件
                excelInfoList = getDownloadFile(taskId, localName, excelTypes);
                LOGGER.error("excelInfoList");
            }
            catch (ServiceException e)
            {
                msgStr = MessageUtil.getMessage(REPORT_DOWNLOAD_NOT_FOUND);
                PrintWriter out = response.getWriter();
                outMsg(out, msgStr);
                request.setAttribute(REPORT_DOWNLOAD_NOT_FOUND, "Download failed.");
                return;
            }

            if (CollectionUtil.isNotEmpty(excelInfoList))
            {
                excelInfo.setFilePath(excelInfoList.get(0).getReportPath());
            }
            else
            {
                msgStr = MessageUtil.getMessage(REPORT_DOWNLOAD_NOT_FOUND);
                PrintWriter out = response.getWriter();
                outMsg(out, msgStr);
                request.setAttribute(REPORT_DOWNLOAD_NOT_FOUND, "Download failed.");
                return;
            }

            LOGGER.error("excelInfo ");
            File downloadFile = new File(excelInfo.getFilePath());
            if (!downloadFile.exists())
            {
                msgStr = MessageUtil.getMessage(REPORT_DOWNLOAD_NOT_FOUND);
                PrintWriter out = response.getWriter();
                outMsg(out, msgStr);
                request.setAttribute(REPORT_DOWNLOAD_NOT_FOUND, "Download failed.");
                return;
            }

            // 写文件
            FileInputStream fileInputStream = null;
            try
            {
                Long length = downloadFile.length();
                response.setContentLength(length.intValue());
                response.addHeader("Content-Disposition",
                        "attachment;filename="
                                + new String(downloadFile.getName().getBytes("UTF-8"), "ISO-8859-1"));
                response.addHeader("Content-Length", "" + length);
                // 以流的形式下载文件。
                response.setContentType("application/octet-stream");

                fileInputStream = new FileInputStream(downloadFile);
                getServletOutputStream(response, fileInputStream);
                LOGGER.error("Download file successfully");
            }
            catch (IOException e)
            {
                LOGGER.error(REPORT_DOWNLOAD_WRITE_ERROR);
                msgStr = MessageUtil.getMessage(REPORT_DOWNLOAD_WRITE_ERROR, new String[] {excelInfo.getFileName()});
                PrintWriter out = response.getWriter();
                outMsg(out, msgStr);
                request.setAttribute(REPORT_DOWNLOAD_WRITE_ERROR, "Download file write error!");
            }
            finally
            {
                if (fileInputStream != null)
                {
                    try
                    {
                        fileInputStream.close();

                        //打包下载完成后，删除目录中压缩包
                        for (Integer excelType : excelTypes)
                        {
                            if (excelType == EnumExcelType.COMPRESS_EXPORT.type)
                            {
                                FileUtils.clearDir(excelInfo.getFilePath());
                            }
                        }

                        //判断是否是部署模式
                        if (EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
                        {
                            FileUtils.clearDirFiles(DBAPP_VAR_REPORT_DIR);
                        }
                    }
                    catch (Exception e2)
                    {
                        LOGGER.error(REPORT_DOWNLOAD_WRITE_ERROR);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            LOGGER.error("dashBoard.NetStarDbappGateway.DownloadExcelServlet.exception: ", e);
        }
    }
    private List<ReportFileInfo> getDownloadFile(String taskId, String localName, int[] excelTypes)
            throws ServiceException
    {
        String publishMode = AppDefineInfoUtil.getInstance().getAttributeByKey("publishMode");

        List<ReportFileInfo> excelInfoList = new ArrayList();

        //查询当前taskID下所有报表信息
        List<ReportExportInfo> reportInfos = ReportRoa.getReportInfos(taskId, null);

        Map<Integer, ReportExportInfo> map = (Map<Integer, ReportExportInfo>)BeanUtil.beanLst2Map(reportInfos, "reportType");

        //不需要去压缩的特殊报表（不属于任何一个项目的报表）
        List<Integer> noNeedZipTypes = new ArrayList<>();
        noNeedZipTypes.add(EnumExcelType.KPI_SEARCH.type);
        noNeedZipTypes.add(EnumExcelType.FIBER_DYNAMIC_ANALYSIS.type);
        noNeedZipTypes.add(EnumExcelType.NETWORK_LIST.type);

        String reportPath = "";
        for (Integer excelType : excelTypes)
        {
            ReportFileInfo reportFileInfo = new ReportFileInfo();
            if (!map.keySet().contains(excelType))
            {
                continue;
            }

            if ("zh_CN".equals(localName))
            {
                reportPath = map.get(excelType).getZhReportPath();
            }
            else if ("en_US".equals(localName))
            {
                reportPath = map.get(excelType).getEnReportPath();
            }

            //判断是否是部署模式
            if (EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
            {
                reportPath = DistributedModeUtil.downloadFile(reportPath, DBAPP_VAR_REPORT_DIR);
            }

            //不是压缩包则压缩(已经压缩的报表请在压缩的时候统一命名压缩)
            //命名规则：项目名_任务ID_报表名(_时间戳).zip
            //String zipFileName = projectName + "_" + taskId + "_" + fileName + ".zip";
            if (!StringUtil.isNullOrEmpty(reportPath) && !reportPath.contains(".zip") && !noNeedZipTypes.contains(excelType))
            {

                int beginIndex = reportPath.lastIndexOf(File.separator) + 1;
                int endIndex = reportPath.lastIndexOf(".");
                String fileName = reportPath.substring(beginIndex, endIndex);
                String projectName = ProjectRoa.getProjectNameByTaskId(taskId);
                String zipFileName = projectName + "_" + taskId + "_" + fileName + ".zip";
                zipFileName = FileUtils.checkAndReNameFile(zipFileName);
                LOGGER.error("zipFileName: ", zipFileName);

                List<File> fileLst = new ArrayList<File>();
                fileLst.add(new File(reportPath));
                String newPath=PlatformEnvUtil.getAppVarPath() + File.separator + "temp" + File.separator + UUID.randomUUID().toString();
//                reportPath = FileUtils.compressZipFile(zipFileName, fileLst, dataPath.getTaskPath());
                reportPath = FileUtils.compressZipFile(zipFileName, fileLst, newPath);
            }

            reportFileInfo.setReportPath(reportPath);
            reportFileInfo.setReportType(excelType);
            excelInfoList.add(reportFileInfo);
        }

        return excelInfoList;
    }

    public void getServletOutputStream(HttpServletResponse response, FileInputStream fileInputStream)
    {
        ServletOutputStream servletOutputStream = null;
        try
        {
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, servletOutputStream);
        }
        catch (IOException e)
        {
            LOGGER.error("Get Servlet Output Stream Error!");
        }
        finally
        {
            if (servletOutputStream != null)
            {
                FileUtils.closeQuietly(servletOutputStream);
            }
        }
    }

    private void outMsg(PrintWriter out, String message)
    {
        out.write(getResponseResult(message));
    }

    private static String getResponseResult(String param)
    {
        return "{\"result\":\"" + param + "\"}";
    }

}

