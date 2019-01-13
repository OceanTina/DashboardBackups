package servlet;

public class DownloadGisExcelServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(DownloadGisExcelServlet.class);

    private static final String REPORT_DOWNLOAD_WRITE_ERROR = "com.netstar.report.download.message.writeerror";

    @Override
    public void init(ServletConfig config) throws ServletException
    {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //下载
        try
        {
            String filePath = downloadExcel(request, response);
            //删除文件夹
            deleteDir(filePath);
        }
        catch (Throwable e)
        {
            logger.error("exception:", e);
        }

    }

    //excel下载
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public String downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setHeader("content-type", "text/html;charset=UTF-8");

        // excel文件路径
        String filePath = request.getParameter("filePath");

        File downloadFile = org.apache.commons.io.FileUtils.getFile(filePath);

        boolean isFileExist = downloadFile.exists();
        // 写文件
        LoggerUtil.getLogger().error("a:" + isFileExist);
        FileInputStream fileInputStream = null;
        try
        {
            Long length = downloadFile.length();
            response.setContentLength(length.intValue());
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(downloadFile.getName().getBytes("GB2312"), "ISO8859_1"));
            response.addHeader("Content-Length", "" + length);
            // 以流的形式下载文件。
            response.setContentType("application/octet-stream");
            if(!isInSecureDir(downloadFile) || !Files.isRegularFile(downloadFile.toPath()))
            {
                throw new IOException("File not in secure directory or Not a regular file!");
            }
            fileInputStream = new FileInputStream(downloadFile);
            getServletOutputStream(response, fileInputStream);
            logger.error("Download file successfully");
        }
        catch (IOException e)
        {
            logger.error(REPORT_DOWNLOAD_WRITE_ERROR, e);
            String msgStr = MessageUtil.getMessage(REPORT_DOWNLOAD_WRITE_ERROR, new String[] {downloadFile.getName()});
            PrintWriter out = response.getWriter();
            outMsg(out, msgStr);
            request.setAttribute(REPORT_DOWNLOAD_WRITE_ERROR, "Download file write error!");
        }
        catch (Throwable e)
        {
            logger.error("exception:", e);
        }

        finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();

                }
                catch (Exception e2)
                {
                    logger.error(REPORT_DOWNLOAD_WRITE_ERROR);
                }
            }
        }
        return filePath;
    }

    //excel文件删除
    public void deleteDir(String filePath)
    {
        filePath = Normalizer.normalize(filePath, Normalizer.Form.NFKC);
        String dirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));

        if (null != dirPath)
        {
            File fileDir = new File(dirPath);
            if (fileDir.exists())
            {
                //删除上传的临时文件
                FileUtils.clearDir(dirPath);
            }
        }

    }

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
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
            logger.error("Get Servlet Output Stream Error!");
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

    public String replaceBlank(final String value)
    {
        return value.replaceAll("\t|\r|\n", "");
    }

}