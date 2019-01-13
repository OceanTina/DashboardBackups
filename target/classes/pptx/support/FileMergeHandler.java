package support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileMergeHandler {

    public static void main(String[]args)throws IOException
    {
        String sourceUrl = "D:\\test\\source.pptx";
        String taretUrl = "D:\\test\\target.pptx";
        List<Integer> srcPageNumbers = new ArrayList<Integer>();
        srcPageNumbers.add(1);
        FileMergeHandler.mergePPT(sourceUrl,taretUrl);
    }

    /**
     * 合并PPT
     */
    public static void mergePPT(String sourceUrl,String targeUrl)throws IOException
    {
        List<Integer> srcPageNumbers = new ArrayList<Integer>();
        if(!sourceUrl.endsWith(".pptx") || !targeUrl.endsWith(".pptx"))
        {
            //记录日志或者抛出异常
            System.out.println("file is not pptx");
            return;
        }

        //pptx文件可以直接解压
        String sourceUnzipPath = sourceUrl.substring(0,sourceUrl.lastIndexOf(".pptx")) + "Temp";
        String targetUnzipPath = targeUrl.substring(0,targeUrl.lastIndexOf(".pptx")) + "Temp";
        unZip(sourceUrl,sourceUnzipPath,false);
        unZip(targeUrl,targetUnzipPath,false);
        int srcPageNumber = getSrcPageNumber(sourceUnzipPath + File.separator + "ppt" + File.separator + "slides");
        for(int i = 1;i <=srcPageNumber;i++)
        {
            srcPageNumbers.add(i);
        }
        int nextSlideNumber = getTargetNextSlideNumber(targetUnzipPath + File.separator + "ppt" + File.separator + "slides");
        //target 目录中ppt/slides/_rels/目录下面，取其中已存在的一个
        String targetExistMaxSlideOut = "../slideLayouts/slideLayout6.xml";
        for(Integer pageNumber:srcPageNumbers)
        {
            String srcSlidePath = sourceUnzipPath + File.separator + "ppt" + File.separator + "slides" + File.separator
                    + "slide" + pageNumber + ".xml";
            String destSlidePath = targetUnzipPath +File.separator + "ppt" + File.separator + "slides" + File.separator
                    + "slide" + nextSlideNumber + ".xml";
            System.out.println("nextSlideNumber=" + nextSlideNumber);
            copyFile(srcSlidePath,destSlidePath);

            String srcSlideRelsPath = sourceUnzipPath + File.separator + "ppt" + File.separator + "slides" + File.separator
                    + "_rels" + File.separator + "slide" + pageNumber + ".xml.rels";
            String destSlideRelsPath = targetUnzipPath + File.separator + "ppt" + File.separator + "slides" + File.separator
                    + "_rels" + File.separator + "slide" + nextSlideNumber + ".xml.rels";
            copyFile(srcSlideRelsPath,srcSlideRelsPath);

            String partName = "/ppt/slides/" + "slide" + nextSlideNumber + ".xml";
            String contentType = "application/vnd.openxmlformats-officedocument.presentationml.slide+xml";
            String xmlUrl = targetUnzipPath + File.separator + "[Content_Types].xml";
            modifyContentXml(xmlUrl,partName,contentType);

            nextSlideNumber++;
        }

        //重新压缩目标文件为。pptx
        //ZipUtil.zipFile(sourceUnzipPath,sourceUnzipPath + ".pptx");
        zipFile(targetUnzipPath, targetUnzipPath + ".pptx");
    }


    private static int getSrcPageNumber(String slideFolderUrl)
    {
        int pageNumber = 0;
        File file = new File(slideFolderUrl);
        File[] files = file.listFiles();
        for(File subFile:files)
        {
            if(subFile.isDirectory())
            {
                continue;
            }
            if(subFile.getName().indexOf(".xml") != -1)
            {
                pageNumber++;
            }
        }
        return pageNumber;
    }

    private static void modifyContentXml(String xmlUrl,String partName,String contentType)
    {
        File inputXml = new File(xmlUrl);

        try {
            //使用SAXReader 解析XML文档
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputXml);

            List list = document.selectNodes("//Types");
            Iterator iterator = list.iterator();
            while (iterator.hasNext())
            {
                Element ownerElement = (Element) iterator.next();

                Element dateElement = ownerElement.addElement("Override");

                dateElement.addAttribute("PartName",partName);
                dateElement.addAttribute("ContentType",contentType);
            }
            //将修改后的文档流写入新的xml文件中
            XMLWriter output = new XMLWriter(new FileWriter(new File(xmlUrl)));
            output.write(document);
            output.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getTargetNextSlideNumber(String slideFolderUrl)
    {
        int maxNumber = -1;
        File file = new File(slideFolderUrl);
        File[] files = file.listFiles();
        for(File subFile : files)
        {
            if(subFile.isDirectory())
            {
                continue;
            }
            //文件名称为类似：slide4.xml,取数字的最大值
            String fileName = subFile.getName();
            int index = fileName.lastIndexOf(".xml");
            //slide*.xml截取*代表的数字
            int number = Integer.parseInt(fileName.substring(5,index));
            if(number > maxNumber)
            {
                maxNumber = number;
            }
        }
        return maxNumber + 1;
    }

    /**
     * 拷贝文件
     */
    public static boolean copyFile(String srcPath,String destPath)
    {
        FileInputStream inStream = null;
        FileOutputStream outStream = null;

        try {
            inStream = new FileInputStream(srcPath);
            outStream = new FileOutputStream(destPath);
            return copyFile(inStream, outStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拷贝文件
     */
    public static boolean copyFile(InputStream srcStream, FileOutputStream destStream)
    {
        ReadableByteChannel rChannel = null;
        FileChannel outChannel = null;
        boolean result = true;

        try {
            rChannel = Channels.newChannel(srcStream);
            outChannel = destStream.getChannel();
            outChannel.transferFrom(rChannel, 0, Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(srcStream);
            closeQuietly(destStream);
            closeQuietly(rChannel);
            closeQuietly(outChannel);
        }
        return result;
    }

    /**
     * 解压Zip文件
     */
    @SuppressWarnings("rawtypes")
    public static boolean unZip(String srcFile,String destDir, boolean deleteFile)
    {
        if(null == destDir || destDir.trim().isEmpty())
        {
            destDir = srcFile.substring(0, srcFile.lastIndexOf("."));
        }

        InputStream is = null;
        BufferedOutputStream bos = null;

        try {
            File file = new File(srcFile);
            if(!file.exists())
            {
                System.out.println("the zip file is not exit");
                return false;
            }
            ZipFile zipFile = new ZipFile(file, Charset.forName("GBK"));
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements())
            {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                //若是文件夹，则在目录路径中新建
                if(zipEntry.isDirectory())
                {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(destDir + File.separator + name);
                    f.mkdirs();
                }
                else
                {
                    File f = new File(destDir + File.separator + zipEntry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    is = zipFile.getInputStream(zipEntry);
                    bos = new BufferedOutputStream(new FileOutputStream(f));
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = is.read(b, 0, 1024)) != -1)
                    {
                        bos.write(b, 0, length);
                    }
                    closeQuietly(is);
                    closeQuietly(bos);
                }
            }
            zipFile.close();
            if(deleteFile)
            {
                file.delete();
            }
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            closeQuietly(is);
            closeQuietly(bos);
        }
    }

    public static void closeQuietly(Closeable closeable)
    {
        try {
            if(closeable != null)
            {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFile(String folderUrl, String zipFileUrl)throws IOException
    {
        File inFile = new File(folderUrl);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileUrl));
        File[] files = inFile.listFiles();
        for(File file : files)
        {
            zipFile(file, zos, "");
        }
        zos.close();
    }

    private static void zipFile(File inFile, ZipOutputStream zos, String dir)throws IOException
    {
        if(inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            for (File file : files) {
                zipFile(file, zos, dir + inFile.getName() + "\\");
            }
        }
        else
        {
            String entryName = null;
            if(!"".equals(dir))
            {
                entryName = dir + inFile.getName();
            }
            else
            {
                entryName = inFile.getName();
            }
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFile));
            //文件缓冲区
            byte[] datas = new byte[2048];
            int len = 0;
            while ((len = is.read(datas)) != -1)
            {
                zos.write(datas, 0 , len);
                is.close();
            }
        }
    }


}
