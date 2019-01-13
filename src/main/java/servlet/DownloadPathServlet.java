package servlet;

public class DownloadPathServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.setHeader("content-type", "text/html;charset=UTF-8");

            // 文件绝对路径(带文件名)
            String fPath = request.getParameter("filePath");
            //其他参数
            String params = request.getParameter("params");
            Map<String, String> queryParams = (StringUtil.isNullOrEmpty(params)) ?
                    (new HashMap<String, String>()) :
                    (JsonUtil.fromJson(params, new TypeReference<Map<String, String>>()
                    {
                    }));

            //判断文件是否存在
            File downloadFile = org.apache.commons.io.FileUtils.getFile(fPath);

            if (downloadFile.exists())
            {
                //更名
                if (queryParams.containsKey("name") && !StringUtil.isNullOrEmpty(queryParams.get("name")))
                {
                    StringBuffer s = new StringBuffer(queryParams.get("name").trim());
                    File temp = new File(downloadFile.getParent(),
                            s.append(downloadFile.getName().substring(downloadFile.getName().lastIndexOf("."))).toString());
                    boolean isSuccessd = downloadFile.renameTo(temp);
                    if (isSuccessd)
                    {
                        downloadFile = new File(temp.getCanonicalPath());
                    }
                }
            }
            else
            {
                //扫描
                File[] files = new File(downloadFile.getParent()).listFiles();
                String paramFileName = queryParams.get("name");
                if (null != files && files.length > 0 && null != paramFileName && !paramFileName.isEmpty())
                {
                    downloadFile = files[0];
                    StringBuffer s = new StringBuffer(paramFileName.trim());

                    String fileName = downloadFile.getName();
                    fileName = fileName.substring(fileName.lastIndexOf(CommonConst.CONNECTLINE) + 1, fileName.length());
                    File temp =
                            new File(downloadFile.getParent(), s.append(fileName.substring(fileName.lastIndexOf("."))).toString());

                    boolean isSuccessd = downloadFile.renameTo(temp);
                    if (isSuccessd)
                    {
                        downloadFile = new File(temp.getCanonicalPath());
                    }
                }
                else
                {
                    LOGGER.error("File is not found! ");
                    return;
                }
            }

            // 写文件
            FileInputStream fileInputStream = null;
            try
            {
                Long length = downloadFile.length();
                response.setContentLength(length.intValue());
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + new String(downloadFile.getName().getBytes("GB2312"), "ISO8859_1"));
                response.addHeader("Content-Length", "" + length);
                // 以流的形式下载文件
                response.setContentType("application/octet-stream");
                if(!isInSecureDir(downloadFile) || !Files.isRegularFile(downloadFile.toPath()))
                {
                    throw new IOException("File not in secure directory or Not a regular file!");
                }
                fileInputStream = new FileInputStream(downloadFile);
                getServletOutputStream(response, fileInputStream);

                LOGGER.error("Download file successfully !");
            }
            catch (IOException e)
            {
                LOGGER.error("Download file failed !");
            }
            finally
            {
                if (fileInputStream != null)
                {
                    try
                    {
                        fileInputStream.close();
                    }
                    catch (IOException e2)
                    {
                        LOGGER.error("");
                    }
                }
            }
        }
        catch (Throwable e)
        {
            LOGGER.error("exception:", e);
        }
    }

    private void getServletOutputStream(HttpServletResponse response, FileInputStream fileInputStream)
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


}
