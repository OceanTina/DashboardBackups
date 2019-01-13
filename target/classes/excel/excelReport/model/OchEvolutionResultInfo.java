package excel.excelReport.model;

public class OchEvolutionResultInfo {
    String index;
    String ochGroupId;
    String uuid;
    String name;
    String srcSiteName;
    String snkSiteName;
    String errorInfo;
    String errorCode;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getOchGroupId() {
        return ochGroupId;
    }

    public void setOchGroupId(String ochGroupId) {
        this.ochGroupId = ochGroupId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcSiteName() {
        return srcSiteName;
    }

    public void setSrcSiteName(String srcSiteName) {
        this.srcSiteName = srcSiteName;
    }

    public String getSnkSiteName() {
        return snkSiteName;
    }

    public void setSnkSiteName(String snkSiteName) {
        this.snkSiteName = snkSiteName;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
