package excel.excelReport;

import contstant.CommonConst;
import enumeration.EnumPublishMode;

import excel.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import util.CollectionUtil;
import util.MessageUtil;
import util.StringUtil;

import java.util.*;
import java.util.concurrent.CountDownLatch;


public class ExcelWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);

    private static final int DEFAULT_BATCH_SIZE = 3000;
    private POISupport poiSupport = null;
    private String excelId = null;
    private String outputDir = null;
    private String conditionJson = null;
    private String sheets = null;
    private String taskId = null;
    private IDataProvider reportDataProvider = null;

    public ExcelWriter(String excelId)
    {
        this.excelId = excelId;
        this.outputDir = CommonConst.Excel_ROOT;
    }

    public ExcelWriter(String excelId, String outputDir) {
        this.excelId = excelId;
        this.outputDir = outputDir;
    }

    public ExcelWriter(String excelId, String outputDir, String sheets) {
        this.excelId = excelId;
        this.outputDir = outputDir;
        this.sheets = sheets;
    }

    public void setConditionJson(String conditionJson) {
        this.conditionJson = conditionJson;
    }

    private String getFileOutputDir(String parentDir, String fileName, String extension)
    {
        return ReportUtil.buildExcelFilePath(parentDir, fileName, extension);
    }

    public String write()throws OldExcelFormatException
    {
        LOGGER.error("Excel export begin");

        ExcelMeta excelMeta = getExclSupport(excelId).getExcelMeta();
        List<SheetMeta> sheetList = excelMeta.getSheetList();
        if((sheetList == null) || (sheetList.isEmpty()))
        {
            LOGGER.error("sheetList no data");
        }

        String fileName = excelMeta.getFileName();
        String extention = excelMeta.getExtention();

        Map<String, String> paramMap = JsonUtil.fromJson(conditionJson, new TypeReference<Map<String, String>>()
        {});

        String projectId = paramMap.get("projectId");
        String tempOutputDir = outputDir;
        String publishMode = AppDefineInfoUtil.getInstance().getAttributeByKey("publishMode");
        //判断部署模式
        if(EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
        {
            tempOutputDir = TempFileUtil.createTempPath();
        }

        String output = getFileOutputDir(tempOutputDir, fileName, extention);
        String templeteFile = excelMeta.getTempleteFile();
        poiSupport = new POISupport(output);
        List<SheetMeta> sheetLeftList = new ArrayList<>();

        //多线程写Excel, 一个线程对应一张sheet

        try {
            filterExcelSheet(sheetList, sheetLeftList, removeSheetList);
            if(!sheetLeftList.isEmpty())
            {
                taskId = TaskProgressDispather.getTaskId();
                CountDownLatch countDownLatch = new CountDownLatch(sheetLeftList.size());
                if(excelMeta.getType() == 0)
                {
                    poiSupport.createWorkbook();
                    for(SheetMeta sheetMeta : sheetLeftList)
                    {
                        Sheet sheet = poiSupport.createSheet(sheetMeta.getDisplayName());
                        //创建sheet页
                        ThreadPool.getInstance().execute(
                                new SheetWreteThread(countDownLatch, sheet, sheetMeta, false, sheetLeftList.size());
                        )
                    }
                }
                else
                {
                    poiSupport.createWorkbook(templeteFile);
                    for(SheetMeta sheetMeta : sheetLeftList)
                    {
                        Sheet sheet = poiSupport.getSXSheetByName(sheetMeta.getDisplayName());
                        //从模板中获取sheet页
                        ThreadPool.getInstance().execute(
                                new SheetWriteThread(countDownLatch, sheet, sheetMeta, true, sheetLeftList.size()));
                    }
                }

                //主线程等待所有子线程结束
                countDownLatch.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(excelMeta.getType() != 0)
        {
            //删除不显示的sheet，仅针对模板
            for(String remSheetName : removeSheetList)
            {
                poiSupport.removeSheet(remSheetName);
            }
        }

        if(reportDataProvider != null)
        {
            reportDataProvider.removeReportCacheData(projectId);
            reportDataProvider = null;
        }

        //生产报表
        poiSupport.write();

        //判断部署模式
        if(EnumPublishMode.DISTRIBUTED.getValue().equals(publishMode))
        {
            /**
             * 1.output xls 压缩至某个临时目录
             * 2.压缩包上传，返回fid
             * 3.output清除压缩包
             * 4.output = fid
             */

            //临时目录
            String tmpPath = FileUtil.getFormatDirPath(PlatformEnvUtil.getAppTmpPath(), UUID.randomUUID().toString().replace("-", ""));
            //ZIP压缩包名
            SimpleDataFormat timeFormat = new SimpleDataFormat("yyyyMMddHHmmss");
            String zipFileName = "report" + "-" + timeFormat.format(new Date()) + "-" +
                    projectId + "-" + fileName;
            zipFileName = FileMgrUtil.checkAndReNameFile(zipFileName);
            List<File> fileLst = new ArrayList<>();
            fileLst.add(new File(output));

            //ZIP压缩包地址
            String soucreFile = FileUtils.compressZipFile(zipFileName, fileLst, tmpPath);
            String targeDir = FileMgrUtil.generateProjectTargetDierctory(Long.valueOf(projectId));
            //压缩包上传，返回fid
            String result = FileMgrUtil.uploadFile(soucreFile, targeDir);
            //清除压缩包
            FileUtils.removeFile(soucreFile);
            Map<String, Object> jsonMap = JsonUtil.fromJson(result, new TypeReference<Map<String, Object>>()
            {});
            //成功
            if(EnumResponseResult.SUCCESS.getValue().equals(jsonMap.get("result")))
            {
                output = (String) jsonMap.get("fileId");
            }
            else
            {
                output = "";
            }
        }

        return output + CommonConst.AT_SEPARATOR_STR + extention;

    }

    private void filterExcelSheet(List<SheetMeta> sheetList, List<SheetMeta> sheetLeftList, List<String> removeSheetList)
    {
        for(SheetMeta meta : sheetList)
        {
            boolean toRemove = true;
            if(!StringUtils.isEmpty(sheets))
            {
                String coverSheetName = MessageUtil.getMessage("com.report.cover");
                if(meta.getDisplayName().equals(coverSheetName) || sheets.contains(meta.getDisplayName()))
                {
                    toRemove = false;
                }
            }
            else
            {
                toRemove = false;
            }
            if(toRemove)
            {
                removeSheetList.add(meta.getDisplayName());
            }
            else
            {
                sheetLeftList.add(meta);
            }
        }
    }


    public IExcelExportSupport getExclSupport(String strExcelId)
    {
        return (IExcelExportSupport)SpringContext.getBean(ExcelExportConstants.EXCEL_EXPORT_SUPPORT_BEAN_PREFIX + strExcelId);
    }

    class SheetWriteThread extends QThread
    {
        //线程计数
        private CountDownLatch countDownLatch;
        private String[] dataType;
        private String[] fieldNames;
        private String[] titles;
        private int batchSize = DEFAULT_BATCH_SIZE;
        private SheetMeta sheetMeta;
        private IDataProvider dataProvider;
        private ISheetExtension sheetExtension;
        private Sheet sheet;
        private boolean isFromTemplete;
        private int total;
        private PageConfig pageConfig = new PageConfig();
        private Locale locale;

        //合并单元格临时记录
        private Map<Integer, MergeColumnMeta> tempMerge = new HashedMap();

        public SheetWriteThread(CountDownLatch countDownLatch, Sheet sheet, SheetMeta sheetMeta, boolean isFromTemplete, int total)
        {
            this.sheetMeta = sheetMeta;
            this.countDownLatch = countDownLatch;
            this.sheet = sheet;
            this.isFromTemplete = isFromTemplete;
            this.locale = ThreadLocaleUtil.getLocale();
            this.total = total;
            buildTemp();
        }

        //构建合并单元格临时记录
        private void buildTemp()
        {
            List<ColunMeta> columnLst = sheetMeta.getColumnList();
            int index = 0;
            for(ColumnMeta column : columnLst)
            {
                if(column.isMerge() && (!this.tempMerge.containsKey(index)))
                {
                    this.tempMerge.put(index, new MergeColumnMeta(index));
                }
                index ++;
            }
        }

        @Override
        public void doAction()
        {
            ThreadLocaleUtil.setLocale(this.locale);
            TaskProgressDispather.setTaskId(taskId);

            try {
                writeSheetDataToExcel();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //线程执行结束，线程数减一
                int step;
                if(total > 9)
                {
                    step = 90 / total;
                }
                else
                {
                    step = (9/total) * 10;
                }
                int progress = (int)countDownLatch.getCount() * step;
                if(progress > 90)
                {
                    progress = 90;
                }
                TaskProgressDispather.setProgress(100 - progress, "Report" + sheetMeta.getName() + "is being completed!");
                countDownLatch.countDown();
                pageConfig.reset();
            }
        }

        @Override
        public String getTreadName() {
            return "Excel-Report";
        }


        private void writeSheetDataToExcel()
        {
            if(sheetMeta == null)
            {
                LOGGER.error("sheetMeta no data");
            }

            //数据分批数不能大于POI中内容最大行数
            //_rowAccessWindowSize,代码中DEFAULT_BATCH_SIZE默认与_rowAccessWindowSize一致均为3000

            int temp = this.sheetMeta.getBatchSize();
            batchSize = ((temp <= 0) || temp > DEFAULT_BATCH_SIZE) ? DEFAULT_BATCH_SIZE : temp;
            //设置初始分页
            this.pageConfig.setPageSize(batchSize);
            //设置sheet页名
            this.pageConfig.setSheetName(sheetMeta.getName());


            IExcelExportSupport exportSupport = getExclSupport(excelId);
            beanConvert(sheetMeta, exportSupport);

            //sheet从0计数
            int rowIndex = sheetMeta.getBefinRowIndex() - 1;
            if(!isFromTemplete)
            {
                //创建表头
                poiSupport.writeTitleData(sheet, titles, rowIndex - 1, columnIndex);
            }
            //写数据
            if(!ReportUtil.isNullOrEmpty(sheetMeta.getDataProvider()))
            {
                dataProvider = exportSupport.getDataProvider(sheetMeta.getDataProvider());
                reportDataProvider = dataProvider;
            }
            //获取页签扩展对象
            if(!ReportUtil.isNullOrEmpty(sheetMeta.getSheetExtensionBean()))
            {
                this.sheetExtension = exportSupport.getSheetExtension(this.sheetMeta.getSheetExtensionBean());
            }
            if(dataProvider == null)
            {
                //当前dataProvider为null且sheetExtension为null时
                if(dataProvider == null)
                {
                    //当dataProvider为null且sheetExtension为null时
                    if(this.sheetExtension == null)
                    {
                        return;
                    }
                    //当dataProvider为null且sheetExtension不为null时
                    //采用第二种方式，页签自定义重绘
                    this.sheetExtension.render(sheet, conditionJson, rowIndex, poiSupport);
                    return;
                }
                if(dataProvider instanceof AbstractDataPageProvider)
                {
                    //使用分页获取数据，分页写入实现
                    ((AbstractDataPageProvider)dataProvider).reset(batchSize, conditionJson);

                    writeCellDataByList(rowIndex, columnIndex, (AbstractDataPageProvider)dataProvider);
                    return;
                }
                //仍旧全量获取数据，分页写入
                writeCellDataByList(rowIndex, columnIndex);
            }
        }

        private void writeCellDataByList(int rowIndex, int columnIndex)
        {
            int startIndex = rowIndex;
            //全量获取数据，分页写入磁盘
            dataProvider.getData(conditionJson, pageConfig);
            if(CollectionUtil.isEmpty(pageConfig.getData()))
            {
                LOGGER.error("there is no data");
                return;
            }
            for (int i = 1; i <= pageConfig.getTotalPages(); i++)
            {
                pageConfig.setCurPage(i);
                List<?> batchData = pageConfig.page();
                rowIndex = writeCellData(rowIndex, columnIndex, batchData);
            }
            //合并单元格
            mergeCellRegins();
        }

        private int writeCellData(int rowIndex, int columnIndex, List<?> batchData)
        {
            int startIndex = rowIndex;
            //位置偏移
            int colOffset = 0;
            if(CollectionUtil.isEmpty(batchData))
            {
                return rowIndex;
            }
            Iterator<?> localIterator = batchData.iterator();
            while (localIterator.hasNext())
            {
                //每行行数据
                List<Object> dataList = VOConverUtil.reflect(localIterator.next(), fieldNames);
                if(CollectionUtil.isEmpty(dataList))
                {
                    continue;
                }
                Iterator<Object> dataListIterator = dataList.iterator();
                colOffset = 0;
                int currentColIndex = columnIndex;
                while (dataListIterator.hasNext())
                {
                    Object value = dataListIterator.next();
                    poiSupport.writeContentData(sheet, rowIndex, currentColIndex, value, dataType[colOffset]);
                    MergeColumnMeta mergeColumnMeta = tempMerge.get(colOffset);
                    if(value != null && mergeColumnMeta != null)
                    {
                        mergeColumnMeta.tryMergeRowData(rowIndex, String.valueOf(value));
                    }
                    //下一列
                    colOffset++;
                    currentColIndex++;
                }
                //下一行
                rowIndex++;
            }
            if(sheetExtension != null)
            {
                if(this.sheetExtension instanceof ISheetExtensionWithSheetMeta)
                {
                    ((ISheetExtensionWithSheetMeta)this.sheetExtension).render(sheet, sheetMeta, pageConfig.page(), startIndex, rowIndex);
                }
                else
                {
                    this.sheetExtension.render(sheet, pageConfig.page(), startIndex, rowIndex);
                }
            }

            //将缓存刷到磁盘
            poiSupport.flushRows(sheet);
            return rowIndex;
        }

        //按照指定合并规则进行单元格合并
        private void mergeRegion(final List<CellMergeRegion> mergeRegionList)
        {
            if(mergeRegionList == null || mergeRegionList.isEmpty())
            {
                return;
            }

            for(CellMergeRegion item : mergeRegionList)
            {
                poiSupport.addMergedRegion(sheet, item, getStartRow(), item.getEndRow(), item.getStartCol(), item.getEndCol());
            }
        }

        //单列内合并单元格
        private void mergeRegion()
        {
            //合并规则关联，其他列的合并不由数据值决定，而由先关列决定
            List<ColumnMeta> columnLst = sheetMeta.getColumnList();
            for(int columnIdx = 0; columnIdx < columnLst.size(); columnIdx++)
            {
                int assMerge = getAssociationMerge(columnLst, columnIdx);
                MergeColumnMeta associateMergeMeta = tempMerge.get(assMerge);
                if(associateMergeMeta == null)
                {
                    LOGGER.error("");
                    continue;
                }
                int currentColIndex = columnIdx + sheetMeta.getBeginColumnIndex() - 1;
                for (Iterator<MergeColumnMeta.MergeColumnRowMeta> colRowMetaItr = associateMergeMeta.mergeRowIndexItr(true);
                        colRowMetaItr.hasNext();)
                {
                    MergeColumnMeta.MergeColumnRowMeta columnRowMeta = colRowMetaItr.next();
                    poiSupport.addMergedRegion(sheet, columnRowMeta.getMinRowIndex(), columnRowMeta.getMaxRowIndex(), currentColIndex, currentColIndex);
                }
            }
        }

        private int getAssociationMerge(List<ColumnMeta> columnList, int index)
        {
            int associationColumn = index;
            //对出现循环依赖的情况进行检测
            Set<Integer> associationSet = new HashSet<>(columnList.size());
            do
            {
                if(associationColumn < 0 || associationColumn >= columnList.size())
                {
                    break;
                }
                int temp = columnList.get(associationColumn).getAssociationMerge();
                if(temp == -1 || temp == associationColumn)
                {
                    break;
                }
                if(associationSet.contains(temp))
                {
                    LOGGER.error("");
                    break;
                }
                associationSet.add(temp);
                associationColumn = temp;
            }
            while (true);
            return associationColumn;
        }

        private void beanConvert(SheetMeta sheetMetaData, IExcelExportSupport exportSupport)
        {
            dataType = new String[sheetMetaData.getColumnList().size()];
            fieldNames = new String[sheetMetaData.getColumnList().size()];
            titles = new String[sheetMetaData.getColumnList().size()];
            String voBeanName = sheetMetaData.getVoBeanName();
            Class<?> voClass = null;
            if(!"".equals(voBeanName))
            {

                try {
                    ClassLoader classLoader = this.getClass().getClassLoader();
                    if(classLoader == null)
                    {
                        LOGGER.error("");
                    }

                    voClass classLoader.loadClas(voBeanName);
                    //解决planner找不到voBeanName对应class的问题
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int i = 0;
            for(ColumnMeta columnMeta : sheetMetaData.getColumnList())
            {
                fieldNames[i] = columnMeta.getFieldName();

                if(ReportUtil.isNullOrEmpty(columnMeta.getType()))
                {
                    dataType[i] = getFieldType4Class(voClass.columnMeta.getFieIdName());
                }
                else
                {
                    dataType[i] = columnMeta.getType();
                }
                titles[i] = columnMeta.getDisplayName();
                i++;
            }
        }

        private String getFieldType4Class(Class<?> voClass, String fieIdName)
        {
            if(ReportUtil.isNullOrEmpty(fieIdName))
            {
                LOGGER.error("");
            }
            return wrapperType2String(getFileClass(voClass, fieIdName));
        }

        private Class<?> getFileClass(Class<?> voClass, String fieIdName)
        {
            Class<?> whileClass = voClass;

            try {
                if(fieIdName.indexOf(".") != -1)
                {
                    String tempFied = fieIdName.substring(0, fieIdName.indexOf("."));
                    fieIdName = fieIdName.substring(fieIdName.indexOf(".") + 1, fieIdName.length());
                    whileClass = getFileClass(whileClass.getDeclaredField(tempFied).getType(), fieIdName);
                }
                return whileClass.getDeclaredField(fieIdName).getType();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }

        private String wrapperType2String(Class<?> primitiveType)
        {
            if(primitiveType.toString().lastIndexOf(".") != -1)
            {
                return primitiveType.toString().substring(primitiveType.toString().lastIndexOf(".") +1);
            }
            return primitiveType.toString();
        }
    }
}

