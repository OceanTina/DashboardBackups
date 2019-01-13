package restfu;

public class RoaExceptionInfo {
    public static final String ROA_EXCEPTION = "ROA_EXFRAME_EXCEPTION";
    private String exceptionId;
    private String exceptionType;
    private String descArgs[];
    private String reasonArgs[];
    private String detailArgs[];
    private String adviceArgs[];


    public RoaExceptionInfo() {
    }


    public static String getRoaException() {
        return ROA_EXCEPTION;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String[] getDescArgs() {
        return descArgs;
    }

    public void setDescArgs(String[] descArgs) {
        this.descArgs = descArgs;
    }

    public String[] getReasonArgs() {
        return reasonArgs;
    }

    public void setReasonArgs(String[] reasonArgs) {
        this.reasonArgs = reasonArgs;
    }

    public String[] getDetailArgs() {
        return detailArgs;
    }

    public void setDetailArgs(String[] detailArgs) {
        this.detailArgs = detailArgs;
    }

    public String[] getAdviceArgs() {
        return adviceArgs;
    }

    public void setAdviceArgs(String[] adviceArgs) {
        this.adviceArgs = adviceArgs;
    }
}
