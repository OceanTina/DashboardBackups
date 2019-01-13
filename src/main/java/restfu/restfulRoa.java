package restfu;

import roa.EvolROA;
import util.JsonUtil;

import java.text.MessageFormat;

public class restfulRoa {

    public static UnbThreshold getUnbThreshold (String caseId, String userId,String taskId) throws ServiceException
    {
        IRestParametes restParametes = new RestParametes();
        restParametes.put(EvolROA.TASK_ID,taskId);
        restParametes.put(PARAM_CASEID,caseId);
        restParametes.putHttpContextHeader(HttpContextUtil.USERID_HEADER, userId);
        String url = MessageFormat.format(URL_GET_WAVELENGTH_THRESHOLD, "");
        IRestResponse response = RestfulProxy.get(url, restParametes);
        ParamOrResultAssert.assertResponse(response);
        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<UnbThreshold>()
        {
        });
    }

}
