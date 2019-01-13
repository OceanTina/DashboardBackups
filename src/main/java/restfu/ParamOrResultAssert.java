package restfu;

import util.JsonUtil;

public final class ParamOrResultAssert {

    public static void assertResponse(IRestResponse response) throws Exception {
        //if (response.getStatus() != HttpStatus2.OK_200)
        if (response.getStatus() != 200) {
            RoaExceptionInfo roaExInfo = null;
            try {
                roaExInfo = (RoaExceptionInfo) JsonUtil.fromJson(response.getResponseContent(), RoaExceptionInfo.class);
            } catch (Exception e) {
                throw new Exception("JsonParseException! " + response.getStatus()
                        + response.getResponseContent());
            }

            throw new Exception("");
        }
    }
}
