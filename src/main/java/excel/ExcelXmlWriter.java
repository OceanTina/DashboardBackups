package excel;

import jdk.internal.org.xml.sax.SAXException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class ExcelXmlWriter
{
    private static final Logger log = LoggerFactory.getLogger(ExcelXmlWriter.class);
    private static void copyStream(InputStream in, OutputStream out) throws IOException
    {
        byte[] chunk = new byte[1024];
        int count;
        while ((count = in.read(chunk)) >= 0)
        {
            out.write(chunk, 0, count);
        }
    }
    public File addExcelXmlNodes(String excelPath, Map<Integer, Map<String, String>> linkMap,
                                 Map<Integer, Collection<String>> mergeCellMap) throws IOException, OpenXML4JException,
            TransformerConfigurationException, ParserConfigurationException, SAXException

    {
        if (linkMap == null)
        {
            linkMap = Collections.emptyMap();
        }
        if (mergeCellMap == null)
        {
            mergeCellMap = Collections.emptyMap();
        }
        //都为空，则不做任何操作。
        if (linkMap.isEmpty() && mergeCellMap.isEmpty())
        {
            return null;
        }
        long startTime = System.currentTimeMillis();
        // 处理各sheet页的超链接
        List<SheetEntry> sheetEntryList = new ArrayList<SheetEntry>();
        try
        {
            // sheet页取待处理数据的合集
            Set<Integer> linkSheetIdSet = linkMap.keySet();
            Set<Integer> mergeCellSheetIdSet = mergeCellMap.keySet();
            Set<Integer> sheetIdSet = new HashSet<Integer>();
            sheetIdSet.addAll(linkSheetIdSet);
            sheetIdSet.addAll(mergeCellSheetIdSet);

            for (Integer sheetId : sheetIdSet)
            {
                Map<String, String> sheetLinkMap = linkMap.get(sheetId);
                Collection<String> sheetMergeCellMap = mergeCellMap.get(sheetId);
                SheetEntry sheetEntry = addSheetXmlNodes(sheetId, excelPath, sheetLinkMap, sheetMergeCellMap);
                sheetEntryList.add(sheetEntry);
            }

            // 拷贝到Excel压缩文件中
            File excelFile = new File(excelPath);
            File tmpExcelFile = File.createTempFile("tmpExcel", ".xlsm");

            replaceSheetXml(excelFile, sheetEntryList, tmpExcelFile);

            long endTime = System.currentTimeMillis();
            log.info("add hyperlink cost time:" + (endTime - startTime) + "ms.");

            return tmpExcelFile;
        }
        finally
        {
            // 删除sheet页xml临时文件
            deleteTmpSheetXml(sheetEntryList);
        }

    }

    private void deleteTmpSheetXml(List<SheetEntry> sheetEntryList)
    {
        for (SheetEntry sheet : sheetEntryList)
        {
            File tmpFile = sheet.tmpFile;
            if (tmpFile == null || !tmpFile.exists())
            {
                continue;
            }
            tmpFile.delete();
        }
    }

    private SheetEntry addSheetXmlNodes(int sheetId, String excelPath, Map<String, String> linkMap,
                                        Collection<String> mergeCellSet) throws TransformerConfigurationException,
            ParserConfigurationException, SAXException, IOException, OpenXML4JException
    {

        String rSheetId = "rId" + sheetId;

        // 修改后的sheet页文件
        File tmpSheetFile = File.createTempFile("tmpSheet", ".xml");

        // 获取excel中的sheet页输入源
        OPCPackage excelPkg = OPCPackage.open(excelPath);
        InputStream sheetInputSream = getSheetInputStream(excelPkg, rSheetId);

        try
        {
            // 进行修改，增加超链接
            modify(sheetInputSream, tmpSheetFile.getAbsolutePath(), linkMap, mergeCellSet);
        }
        finally
        {
            if (sheetInputSream != null)
            {
                try
                {
                    sheetInputSream.close();
                }
                catch (IOException e)
                {
                    log.error("", e);
                }

            }

            if (excelPkg != null)
            {
                try
                {
                    excelPkg.close();
                }
                catch (IOException e)
                {
                    log.error("", e);
                }

            }
        }

        String entry = "xl/worksheets/sheet" + sheetId + ".xml";

        SheetEntry sheetEntry = new SheetEntry(tmpSheetFile, entry);

        return sheetEntry;
    }
    private void modify(InputStream sheetIn, String outputFile, Map<String, String> linkMap,
                        Collection<String> mergeCellSet) throws ParserConfigurationException,
            SAXException, TransformerConfigurationException, IOException
    {
        SAXParserFactory spf = SaxParserFactories.newSecurityInstance();
        SAXParser saxParser = spf.newSAXParser();

        XMLReader xmlReader = saxParser.getXMLReader();

        WriteHandler wHandler = new WriteHandler(outputFile);
        wHandler.start();

        // 转换成需要处理的超链接，合并单元格类
        List<IXmlNodeHandler> nodeHandlerList = new ArrayList<IXmlNodeHandler>();
        HyperLinkHandler linkHandler = new HyperLinkHandler(linkMap);
        nodeHandlerList.add(linkHandler);

        MergeCellHandler mregeCellHandler = new MergeCellHandler(mergeCellSet);
        nodeHandlerList.add(mregeCellHandler);

        ExcelContentHandler rwHandler = new ExcelContentHandler(wHandler.getHandler(), nodeHandlerList);
        xmlReader.setContentHandler(rwHandler);

        try
        {
            xmlReader.parse(new InputSource(sheetIn));
        }
        finally
        {
            wHandler.end();
        }

    }
    private InputStream getSheetInputStream(OPCPackage excelPkg, String rSheetId) throws IOException, OpenXML4JException
    {

        XSSFReader r = new XSSFReader(excelPkg);
        InputStream sheetIn = r.getSheet(rSheetId);
        return sheetIn;
    }
    private void replaceSheetXml(File oldExcelfile, List<SheetEntry> sheetEntryList,
                                 File tmpExcelFile) throws IOException
    {

        InputStream is = null;
        try (OutputStream excelOut = SafeResourceUtil.getSafeLinuxOStream(tmpExcelFile, true);
             ZipFile zip = new ZipFile(oldExcelfile);
             ZipOutputStream zos = new ZipOutputStream(excelOut)
