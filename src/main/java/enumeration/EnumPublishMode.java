package enumeration;

public enum EnumPublishMode {
    SINGLE("0", "单机模式"),
    DISTRIBUTED("1", "分布式模式");

    public final String value;
    public final String desc;

    private EnumPublishMode(String value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public String getValue()
    {
        return value;
    }
}
