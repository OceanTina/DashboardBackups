package servlet;

public class UploadDataInfo
{
    private int projectId;
    private String dataName;
    private float dataSize;
    private int fileType = 0;
    private int dataType = 0;
    private String dataPath;
    private String description;

    public int getProjectId()
    {
        return projectId;
    }
    public int getFileType()
    {
        return fileType;
    }
    public void setFileType(int fileType)
    {
        this.fileType = fileType;
    }
    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }
    public String getDataName()
    {
        return dataName;
    }
    public void setDataName(String dataName)
    {
        this.dataName = dataName;
    }
    public float getDataSize()
    {
        return dataSize;
    }
    public void setDataSize(float dataSize)
    {
        this.dataSize = dataSize;
    }
    public int getDataType()
    {
        return dataType;
    }
    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }
    public String getDataPath()
    {
        return dataPath;
    }
    public void setDataPath(String dataPath)
    {
        this.dataPath = dataPath;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

}
