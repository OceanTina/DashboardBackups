package servlet;

public class DownloadScriptServlet extends HttpServlet
{
    private static final long serialVersionUID = 6458511380674724008L;
    private static final Logger logger = LoggerFactory.getLogger(DownloadScriptServlet.class);
    public static final String DBAPP_VAR_DIR = PlatformEnvUtil.getAppVarPath();

    private String enc = "UTF-8";

    @Override
    public void init(ServletConfig config) throws ServletException
    {}

    @Override
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String msgStr = "";
            response.setCharacterEncoding(enc);
            response.setContentType("text/html");
            response.setHeader("content-type", "text/html;charset=UTF-8");
            // 项目ID
            String taskId = request.getParameter(ACTOPSAppROA.TASK_ID);
            if (StringUtil.isNullOrEmpty(taskId))
            {
                logger.error("invalid taskId.");
                return;
            }
            // 下载文件路径
            String filePath = request.getParameter(ACTOPSAppROA.FILE_PATH);

            String publishMode = AppDefineInfoUtil.getInstance().getAttributeByKey("publishMode");
            logger.error("publishMode ");
            //判断是否是部署模式
            if (EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
            {
                String downloadResult = FileMgrUtil.downloadFile(filePath, DBAPP_VAR_DIR);
                if (null == downloadResult || downloadResult.isEmpty())
                {
                    logger.error("download from fileMgr failed with empty result");
                    return;
                }
                Map<String, String> resultMap =
                        JsonUtil.fromJson(downloadResult, new TypeReference<Map<String, String>>()
                        {
                        });
                if (Integer.valueOf(resultMap.get("result")) == 0)
                {
                    filePath = resultMap.get("filePath");
                }
            }

            if (StringUtil.isNullOrEmpty(filePath))
            {
                logger.error("invalid filePath.");
                return;
            }
            // 文件名称
            String fileName = request.getParameter(ACTOPSAppROA.FILE_NAME);
            if (StringUtil.isNullOrEmpty(filePath))
            {
                logger.error("invalid fileName.");
                return;
            }

            ExcelInfo excelInfo = new ExcelInfo();
            //        File downloadFile = new File(filePath);
            File downloadFile = org.apache.commons.io.FileUtils.getFile(filePath);
            if (downloadFile == null)
            {
                logger.error("invalid filePath.");
                return;
            }

            if (!downloadFile.exists())
            {
                logger.error("file is not found!");
                msgStr = MessageUtil.getMessage(ACTOPSErrorCode.SCRIPT_DOWNLOAD_NOT_FOUND);
                PrintWriter out = response.getWriter();
                outMsg(out, msgStr);
                request.setAttribute(ACTOPSErrorCode.SCRIPT_DOWNLOAD_NOT_FOUND, ACTOPSErrorCode.DOWNLOAD_FAILED);
                return;
            }

            // 写文件
            FileInputStream fileInputStream = null;
            try
            {
                response.setContentType("text/plain");
                Long length = downloadFile.length();
                response.setContentLength(length.intValue());
                fileName = URLEncoder.encode(fileName, enc);
                response.addHeader("Content-Disposition", HttpSafeHeader.toSafeValue("attachment; filename=" + fileName));
                fileInputStream = new FileInputStream(downloadFile);
                getServletOutputStream(response, fileInputStream);

                logger.error("Download file successfully");
            }
            catch (IOException e)
            {
                logger.error(ACTOPSErrorCode.SCRIPT_DOWNLOAD_WRITE_ERROR);
                msgStr = MessageUtil.getMessage(ACTOPSErrorCode.SCRIPT_DOWNLOAD_WRITE_ERROR,
                        new String[] {excelInfo.getFileName()});
                PrintWriter out = response.getWriter();
                outMsg(out, msgStr);
                request.setAttribute(ACTOPSErrorCode.SCRIPT_DOWNLOAD_WRITE_ERROR, "Download file write error!");
            }
            finally
            {
                if (fileInputStream != null)
                {
                    try
                    {
                        fileInputStream.close();
                        //判断是否是部署模式
                        if (EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
                        {
                            FileUtils.removeFile(filePath);
                        }
                    }
                    catch (Exception e2)
                    {
                        logger.error(ACTOPSErrorCode.SCRIPT_DOWNLOAD_WRITE_ERROR);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            logger.error("exception:", e);
        }
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
            logger.error("Get Output Stream Error!");
        }
        finally
        {
            if(servletOutputStream != null)
            {
                FileUtils.closeQuietly(servletOutputStream);
            }
        }
    }

    private void outMsg(PrintWriter out, String message) {
        out.write(getResponseResult(message));
    }

    public String replaceBlank(final String value)
    {
        return value.replaceAll("\t|\r|\n", "");
    }

    private static String getResponseResult(String param)
    {
        return "{\"resi;t\"" + param + "\"}";
    }
}