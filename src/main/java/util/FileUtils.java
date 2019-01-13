package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FileUtils
{
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static final Properties loadProperties(String filePath)
    {
        if (!isFileExists(filePath))
        {
            return null;
        }
        Properties prop = new Properties();

        FileInputStream in = null;
        try
        {
            in = new FileInputStream(new File(filePath));
            prop.load(in);
        }
        catch (IOException ex)
        {
            logger.warn("loadProperties fail, filePath=" + filePath, ex);
        }
        finally
        {
            closeQuietly(in);
        }
        return prop;
    }

    public static String getPath(File file)
    {
        String path = null;
        try
        {
            path = file.getCanonicalPath();
        }
        catch (IOException e)
        {
            logger.error("file.getCanonicalPath() IOException:", e);
        }
        return path;
    }
    public static void scanFile(String fileDir, List<File> filelist)
    {
        scanFile(fileDir, filelist, null);
    }

    public static void scanFile(String fileDir, List<File> filelist, final String filter)
    {
        File dir = new File(fileDir);
        if (!dir.exists() || filelist == null)
        {
            return;
        }

        File[] files = dir.listFiles();

        if (files == null)
        {
            return;
        }
        for (File file : files)
        {
            if (file.isDirectory() && !file.isHidden() && file.canRead())
            {
                scanFile(file.getAbsolutePath(), filelist, filter);
            }
            else
            {
                if (!file.isHidden() && file.canRead() && (filter == null || file.getName().endsWith(filter)))
                {
                    filelist.add(file);
                }
            }
        }
    }
    private static void deleteFolder(File dir)
    {
        File fileList[] = dir.listFiles();
        if (fileList == null)
        {
            return;
        }
        int listlen = fileList.length;
        for (int i = 0; i < listlen; i++)
        {
            if (fileList[i].isDirectory())
            {
                deleteFolder(fileList[i]);
            }
            else
            {
                fileList[i].delete();
            }
        }
        dir.delete();
    }
    public static void deleteFolder(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            if (file.isDirectory())
            {
                deleteFolder(file);
            }
            else
            {
                throw new RuntimeException((new StringBuilder("Invalid folder path: ")).append(path).toString());
            }
        }
    }
    public static boolean removeFile(String fileName)
    {
        if (fileName == null || fileName.trim().isEmpty())
        {
            return true;
        }

        File fp = new File(fileName);
        if (fp.exists() && fp.isFile() && fp.canWrite())
        {
            return fp.delete();
        }
        else
        {
            return false;
        }
    }

    public static void clearDir(String path)
    {
        File fp = new File(path);

        if (fp.exists())
        {
            if (fp.isDirectory())
            {
                File[] fps = fp.listFiles();
                if (fps != null)
                {
                    for (int i = 0; i < fps.length; i++)
                    {
                        if (fps[i].exists())
                        {
                            if (fps[i].isDirectory())
                            {
                                clearDir(fps[i].getPath());
                            }
                            if (fps[i].canWrite())
                            {
                                fps[i].delete();
                            }
                        }
                    }
                }

                // 子目录删除后删除自身
                fp.delete();
            }
            else
            {
                fp.delete();
            }
        }
    }
    public static boolean copyFile(String srcPath, String destPath)
    {
        try
        {
            //dest路径安全性检查
            File srcFile = new File(SafeResourceUtil.safeFilePath(srcPath));
            File destFile = new File(SafeResourceUtil.safeFilePath(destPath));
            org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
        }
        catch (IOException e)
        {
            logger.error("some exception when write file");
            return false;
        }
        return true;
    }
    public static boolean copyFile(InputStream srcStream, String destPath)
    {
        FileOutputStream outStream = null;
        try
        {
            outStream = new FileOutputStream(SafeResourceUtil.safeFilePath(destPath));
            return copyFile(srcStream, outStream);
        }
        catch (FileNotFoundException e)
        {
            logger.error("some exception when write file");
            return false;
        }
        catch (IOException e)
        {
            logger.error("destPath is not safe");
            return false;
        }
    }
    public static boolean copyFile(InputStream srcStream, FileOutputStream destStream)
    {
        ReadableByteChannel rChannel = null;
        FileChannel outChannel = null;
        boolean result = true;
        try
        {
            rChannel = Channels.newChannel(srcStream);
            outChannel = destStream.getChannel();
            outChannel.transferFrom(rChannel, 0, Integer.MAX_VALUE);
        }
        catch (IOException e)
        {
            logger.error("some exception when write file", e);
            result = false;
        }
        finally
        {
            closeQuietly(srcStream);
            closeQuietly(destStream);
            closeQuietly(rChannel);
            closeQuietly(outChannel);
        }

        return result;
    }

    public static void closeQuietly(Closeable closeable)
    {
        try
        {
            if (closeable != null)
            {
                closeable.close();
            }
        }
        catch (IOException e)
        {
            logger.error("some exception when close file", e);
        }
    }
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return true;
        }

        return false;
    }

    public static boolean unZip(String srcFile, String destDir, boolean deleteFile)
    {
        if (null == destDir || destDir.trim().isEmpty())
        {
            destDir = srcFile.substring(0, srcFile.lastIndexOf("."));
        }

        InputStream is = null;
        BufferedOutputStream bos = null;
        ZipFile zipFile = null;
        try
        {
            //对文件路径做校验
            srcFile = SafeResourceUtil.safeFilePath(srcFile);
            destDir = SafeResourceUtil.safeFilePath(destDir);

            File file = new File(srcFile);
            if (!file.exists())
            {
                logger.error("the zip file is not exist");
                return false;
            }
            zipFile = new ZipFile(file, Charset.forName("GBK"));
            Enumeration e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry zipEntry = null;
                try {
                    zipEntry = (ZipEntry) e.nextElement();
                } catch (Exception ex) {
                    logger.error("unzip nextElement Exception", ex);
                    continue;
                }

                if (zipEntry == null) {
                    continue;
                }

                String name = zipEntry.getName();

                // 若是文件夹，则在目标路径中新建该文件夹
                if (zipEntry.isDirectory()) {
                    name = name.substring(0, name.length() - 1);
                    File newDir = new File(destDir + File.separator + name);
                    //新文件夹不存在时则创建
                    if (!newDir.exists()) {
                        boolean flag = newDir.mkdirs();
                        if (!flag) {
                            logger.error("create new dir return false!");
                        }
                    }
                } else {
                    File newFile = new File(destDir + File.separator + name);
                    //先判断其父级目录是否存在，不存在则创建父级目录
                    File parentFile = newFile.getParentFile();
                    if (null != parentFile && !parentFile.exists()) {
                        boolean flag = parentFile.mkdirs();
                        if (!flag) {
                            logger.error("mk parent directory return false!");
                        }
                    }
                    //文件不存在时创建文件
                    if (!newFile.exists()) {
                        boolean flag = newFile.createNewFile();
                        if (!flag) {
                            logger.error("create new file return false!");
                        }
                    }
                    is = zipFile.getInputStream(zipEntry);
                    bos = new BufferedOutputStream(SafeResourceUtil.getSafeLinuxOStream(newFile, true));
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = is.read(b, 0, 1024)) != -1) {
                        bos.write(b, 0, length);
                    }

                    closeQuietly(is);
                    closeQuietly(bos);
                }
            }

            if(deleteFile)
            {
                boolean flag = file.delete();
                if(!flag)
                {
                    logger.error("");
                }
            }
            return true;
            }
            catch (IOException e)
            {
                return false;
            }
            finally
            {
            closeQuietly(is);
            closeQuietly(bos);
            closeQuietly(zipFile);
            }

        }

    public static float format(long size, String mode)
    {
        DecimalFormat df = new DecimalFormat("###.####");
        float f;

        switch (mode)
        {
            case "MB":
            {
                f = (float)size / (float) (1024 * 1024);
                return Float.valueOf(df.format(new Float(f).floatValue()));
            }
            case "GB":
            {
                f = (float)size / (float) (1024 * 1024);
                return Float.valueOf(df.format(new Float(f).floatValue()));
            }
            case "KB":
                default:
                {
                    f = (float)size / (float)1024;
                    return Float.valueOf(df.format(new Float(f).floatValue()));
                }
        }
    }
}


