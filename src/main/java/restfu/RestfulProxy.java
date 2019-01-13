package restfu;

public class RestfulProxy
{
    public static IRestResponse get(String uri, IRestParametes restParametes) throws ServiceException
    {
        return doAction(RestfulMethod.GET, uri, restParametes, null);
    }
    public static IRestResponse get(String uri, IRestParametes restParametes,
                                    IRestOptions restOptions) throws ServiceException
    {
        return doAction(RestfulMethod.GET, uri, restParametes, restOptions);
    }
    public static IRestResponse put(String uri, IRestParametes restParametes) throws ServiceException
    {
        return doAction(RestfulMethod.PUT, uri, restParametes, null);
    }
    public static IRestResponse put(String uri, IRestParametes restParametes,
                                    IRestOptions restOptions) throws ServiceException
    {
        return doAction(RestfulMethod.PUT, uri, restParametes, restOptions);
    }
    public static void asyncPut(String uri, IRestParametes restParametes,
                                IRestAsyncCallback callback) throws ServiceException
    {
        doAsyncAction(RestfulMethod.PUT, uri, restParametes, null, callback);
    }
    public static IRestResponse post(String uri, IRestParametes restParametes) throws ServiceException
    {
        return doAction(RestfulMethod.POST, uri, restParametes, null);
    }
    public static IRestResponse post(String uri, IRestParametes restParametes,
                                     IRestOptions restOptions) throws ServiceException
    {
        return doAction(RestfulMethod.POST, uri, restParametes, restOptions);
    }
    public static IRestResponse delete(String uri, IRestParametes restParametes) throws ServiceException
    {
        return doAction(RestfulMethod.DELETE, uri, restParametes, null);
    }
    public static IRestResponse delete(String uri, IRestParametes restParametes,
                                       IRestOptions restOptions) throws ServiceException
    {
        return doAction(RestfulMethod.DELETE, uri, restParametes, restOptions);
    }
    private static IRestResponse doAction(RestfulMethod action, String uri, IRestParametes restParametes,
                                          IRestOptions restOptions) throws ServiceException
    {
        // 增加重试
        IRestResponse response = null;
        int tryTimes = 0;
        do
        {
            // 增加线程本地化语言信息到请求的头部
            addThreadLocale2Header(restParametes);

            if (restOptions == null)
            {
                response = action.method(uri, restParametes);
            }
            else
            {
                response = action.method(uri, restParametes, restOptions);
            }

            tryTimes++;

        } while (RetryHandler.getInstance().needRetry(response, tryTimes, uri));

        return response;
    }

    private static void addThreadLocale2Header(IRestParametes restParametes)
    {
        Locale locale = ThreadLocaleUtil.getLocale();

        String header = restParametes.getHttpContextHeader("Cookie");

        // 没有设置，才增加到头部
        if (StringUtil.isNullOrEmpty(header) || header.indexOf("user_locale") < 0)
        {
            String localeName = PlatLocaleUtil.locale2LangCountry(locale);
            restParametes.putHttpContextHeader("Cookie", "user_locale=" + localeName);
        }
    }
    private static void doAsyncAction(RestfulMethod action, String uri, IRestParametes restParametes,
                                      IRestOptions restOptions, IRestAsyncCallback callback)throws serviceException
    {
        //增加线程本地化语言信息到请求的头部
        addThreadLocale2Header(restParametes);

        if(restOptions == null)
        {
            action.asyncMethod(uri, restParametes, callback);
        }
        else
        {
            action.asyncMethod(uri, restParametes, restOptions, callback);
        }
    }
}
