package restfu;

import java.util.Map;

public interface IRestResponse
{
    int getStatus();

    void setStatus(int status);

    Map<String, String> getRespHeaderMap();

    void setRespHeaderMap(Map<String, String> header);

    int getRespHeaderInt(String key);

    long getRespHeaderLong(String key);

    String getRespHeaderStr(String key);

    String getResponseContent();

    String getResponseJson();

    void setResponseJson(String responseString);
}
