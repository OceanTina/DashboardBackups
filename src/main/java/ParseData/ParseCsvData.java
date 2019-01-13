package ParseData;

import java.io.IOException;

public class ParseCsvData {

    public boolean startParse(String dataPath) {
        //解压到一个临时文件路径下面，并返回临时路径
        String tempPath = ExcelParseUtil.getTempUnzipDir(dataPath);
        List<File> allFileList = ExcelParseUtil.getFileList(tempPath);
        if (allFileList.isEmpty()) {
            LOGGER.error("Can't find any file in the tempPath:{}", tempPath);
            TempFileUtils.clearTempFile(tempPath);
            return false;
        }

        // 当前仅考虑csv格式的脚本文件
        List<File> needFileList = ExcelParseUtil.getCsvList(allFileList);
        for (File currFile : needFileList) {
            parseSingleFile(currFile);
        }
        //结束后删除临时目录
        TempFileUtils.clearTempFile(tempPath);

        // 判断是否有合法脚本文件
        if (!isHaslegalFile) {
            // 一个合法的脚本文件都没有
            LOGGER.error("Can't find any legal alarm data file in the tempPath:{}", tempPath);
            return false;
        }

        return fillAllModelData();
    }


    private void parseSingleFile(File currFile) {
        //获取编码格式
        String charSetStr = getCharSetStr(currFile);
        BufferedReader buffedReader = null;
        InputStreamReader streamReader = null;
        FileInputStream inputStream = null;
        BoundedInputStream boundedInput = null;
        LineIterator lineIte = null;

        String filePath = currFile.getPath();

        try {
            inputStream = new FileInputStream(currFile);
            LOGGER.error("parse file " + currFile + " use charset:" + charSetStr);
            boundedInput = new BoundedInputStream(inputStream, MAX_SIZE);

            if (!"".equals(charSetStr)) {
                streamReader = new InputStreamReader(boundedInput, charSetStr);
            } else {
                streamReader = new InputStreamReader(boundedInput);
            }

            buffedReader = new BufferedReader(streamReader, FILE_SIZE);
            lineIte = new LineIterator(buffedReader);


            //存放表头的长度
            int headColumnCount = 0;
            String rowStr = null;
            String nextRowStr = null;//读下一行数据与上一行进行比较
            String combRowStr = null;//合并两行数据
            int rowNum = 0; //当前的行号，目前仅用来判断此行是否是表头
            int temp = 0;
            Map<String, Integer> eachColumnIndexMap = new HashMap<>();
            //当前脚本文件中英文格式
            boolean isEnglish = false;
            if (lineIte.hasNext()) {
                rowStr = lineIte.nextLine();
            }

            while (!StringUtils.isEmpty(rowStr)) {
                // 脚本文件中，第一行存的是列名，仅用于初始化列与列索引的对应关系
                if (rowNum == ROW_INDEX_FIRST) {
                    if (rowStr != null) {
                        isEnglish = initColNameCollection(rowStr, eachColumnIndexMap);
                        // 通过校验是否所有需要的属性都有，来判断当前脚本文件是否合法
                        if (!isLegalAlarmDataFile(eachColumnIndexMap)) {
                            // 此文件不合法，没必要再解析下去了
                            LOGGER.error("The alarm data file {} is not legal!", filePath);
                            break;
                        } else {
                            // 只要脚本目录下，有一个脚本文件时合法的就算是有合法文件，就要进行后续的存库过程
                            isHaslegalFile = true;
                            headColumnCount = getColumnCountOfRowStr(rowStr);
                        }

                    } else {
                        //第一行(表头)都为空，就没必要再继续解析下去了
                        LOGGER.error("The alarm data file {} has nothing!", filePath);
                        break;
                    }

                    rowNum++;
                    continue;
                }
                temp = getColumnCountOfRowStr(rowStr);
                //如果和表头长度相等，则为有效数据
                if (headColumnCount == temp) {
                    processRowData(rowStr, eachColumnIndexMap, isEnglish);
                } else {
                    if (lineIte.hasNext()) {
                        nextRowStr = lineIte.nextLine();
                    }
                    //如果不相等，再读取下一行数据
                    if (StringUtils.isNotEmpty(nextRowStr)) {
                        if (headColumnCount == getColumnCountOfRowStr(nextRowStr)) {
                            processRowData(rowStr, eachColumnIndexMap, isEnglish);

                            processRowData(nextRowStr, eachColumnIndexMap, isEnglish);
                        } else {
                            processRowData(rowStr, eachColumnIndexMap, isEnglish);
                        }
                    }

                    rowNum++;
                    if (lineIte.hasNext()) {
                        rowStr = lineIte.nextLine();
                    } else {
                        break;
                    }
                }
            }
            catch(IOException ioException)
            {
                LOGGER.error("");
            }

            finally
            {
                if (buffedReader != null) {
                    try {
                        buffedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (streamReader != null) {
                    try {
                        streamReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (boundedInput != null) {
                    try {
                        boundedInput.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (lineIte != null) {
                    lineIte.close();
                }
            }
        }


        private String getCharSetStr (File file)
        {
            boolean isCharSetMatched = false;
            String[] tryReadCharSetArray = new String[]{TXT_CODE_UTF_8, TXT_CODE_GB_2312, TXT_CODE_GBK};
            for (String charSet : tryReadCharSetArray) {
                isCharSetMatched = readFileHeadLine(file, charSet);
                if (isCharSetMatched) {
                    return charSet;
                }
            }
            return "";
        }


        public static List<File> getFileList (String dir){
            ArrayList list = new ArrayList();
            File file = new File(dir);
            if (!file.isDirectory()) {
                list.add(file);
                return list;
            } else {
                if (null != file.listFiles() && file.listFiles().length != 0) {
                    File[] var3 = file.listFiles();
                    int var4 = var3.length;

                    for (int var5 = 0; var5 < var4; ++var5) {
                        File f = var3[var5];
                        String filepath = f.getAbsolutePath();
                        List files = getFileList(filepath);
                        list.addAll(files);
                    }
                }

                return list;
            }
        }
    }

}
