package util;

import sun.security.util.ResourcesMgr;

import java.text.MessageFormat;

public class MessageUtil {

    public static String getMessage(String key)
    {
        return getMessage(key, null, "zh_CN");
    }

    public static boolean isHaveString(String key)
    {
        return ResourceMgr.isHaveString(key, "zh_CN");
    }

    public static String getMessage(String key, String zh_CN)
    {
        return getMessage(key, null, zh_CN);
    }

    public static String getMessage(String key, Object[] args)
    {
        return getMessage(key, args, "zh_CN");
    }

    public static String getMessage(String key, Object[] args, String zh_CN)
    {
        String msg = ResourceMgr.findString(key, zh_CN);
        if((msg != null) && (args != null))
        {
            msg = MessageFormat.format(msg, args);
        }
        return msg;
    }
}
