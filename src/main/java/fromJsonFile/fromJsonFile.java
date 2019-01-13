package fromJsonFile;

import util.JsonUtil;

public class fromJsonFile {

    String json = JsonUtil.loadJsonFromFile(conf);
    kpiConfList = JsonUtil.fromJson(json, new TypeReference<List<KpiConfVO>>() {});
}
