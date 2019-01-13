package method;

import jdk.internal.org.objectweb.asm.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.util.StringUtil;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTypeMethod {
    //101对于double型和0进行的判断
    double weight;
    //if(Float.compare(weight, 0) == 0)

    //commons-collections-3.2.2.jar
    List<Map<String, Object>> lineRateMapLst = null;
    /*
    for(Map<String, Object> lineRate : lineRateMapLst)
    {
        String omsUuid = MapUtils.getString(lineRate, "uuid", "");
        int capacity = MapUtils.getInteger(lineRate, "capacity", 0);
    }
    */

    //JSON格式转换
//    String filterParams = null;
//    Map<String, String> filterParamMap = (null == filterParams) ? (new HashMap<>()) : (JsonUtil.fromJson(filterParams,
//            new TypeReference<Map<String, String>>(){}));
//    List<String> capacityList = (StringUtil.isNullOrEmpty(capacitys)) ? (new ArrayList<>()) : (JsonUtil.fromJson(capacitys,
//            new TypeReference<List<String>>(){}));

    //102codex
//    float relatedWeight = relatedWeightMap.get(scoreResult.getScoreID());//改成
//    Float weightFloat = relatedWeightMap.get(scoreResult.getScoreID());
//    float relatedWeight = (null == weightFloat ? 0.0f : weightFloat.floatValue());

//    if(CollectionUtils.isEmpty(kpiLst))
//        if(CollectionUtil.isEmpty(kpiLst))
//            if(null == kpiLst || kpiLst.isEmpty())

//    BeianVO beianVO = null;//改为
//    BeianVo beianVo;

}
