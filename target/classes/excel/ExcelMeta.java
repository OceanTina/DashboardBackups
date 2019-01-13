package excel;

import util.MessageUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelMeta
{
    private String id;
    private int type = 0;
    private String fileName;
    private List<SheetMeta> sheetList = new ArrayList<SheetMeta>();
    private String templeteFile;
    private String extention;

    public ExcelMeta(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<SheetMeta> getSheetList()
    {
        return sheetList;
    }

    public SheetMeta getSheetMeta(String sheetName)
    {
        Iterator<SheetMeta> iter = this.sheetList.iterator();
        while (iter.hasNext())
        {
            SheetMeta sheet = (SheetMeta)iter.next();
            if (sheet.getName().equals(sheetName))
            {
                return sheet;
            }
        }
        return null;
    }

    public void addSheetMeta(SheetMeta sheetMeta)
    {
        for (SheetMeta sheet : this.sheetList)
        {
            if (sheet.equals(sheetMeta))
            {
                throw new IllegalArgumentException(
                        MessageFormat.format("Template Error.Sheet named {0} repeat.", sheetMeta.getName()));
            }
        }
        this.sheetList.add(sheetMeta);
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getFileName()
    {
        //返回fileName中存储的国际化资源ID对应的信息
        return MessageUtil.getMessage(this.fileName);
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @Override
    public String toString()
    {
        return "ExcelMeta [id=" + id + ", type=" + type + ", fileName=" + fileName + ", sheetList=" + sheetList + "]";
    }
    public String getTempleteFile()
    {
        String localeName = PlatLocaleUtil.locale2LangCountry(ThreadLocaleUtil.getLocale());
        return templeteFile + "_" + localeName + extention;
    }

    public void setTempleteFile(String templeteFile)
    {
        if (StringUtils.isNotBlank(templeteFile))
        {
            this.templeteFile = DefaultEnvUtil.getAppRoot() + File.separator + templeteFile;
            setType(1);
        }
    }

    public String getExtention()
    {
        return extention;
    }

    public void setExtention(String ext)
    {
        if (StringUtils.isNotBlank(ext))
        {
            this.extention = CommonConstant.SEPARATOR_POINT + ext;
        }
        else
        {
            this.extention = EnumFileType.XLSX.getValue();
        }
    }
}