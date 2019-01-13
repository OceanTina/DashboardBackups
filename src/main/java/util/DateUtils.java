package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String date2String(String formater, Date aDate)
    {
        if(null == formater || "".equals(formater))
        {
            return null;
        }
        if(null == aDate)
        {
            return null;
        }
        return (new SimpleDateFormat((formater)).format(aDate));
    }

    public static String date2String(String formater)
    {
        return date2String(formater, new Date());
    }
}
