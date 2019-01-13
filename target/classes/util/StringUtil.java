package util;

public final class StringUtil {
    public static boolean isNullOrEmpty(final String str)
    {
        if(null == str || str.trim().isEmpty())
        {
            return true;
        }
        return false;
    }
}
