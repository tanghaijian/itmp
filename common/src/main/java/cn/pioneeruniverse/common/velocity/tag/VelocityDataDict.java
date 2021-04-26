package cn.pioneeruniverse.common.velocity.tag;

import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:31 2018/12/10
 * @Modified By:
 */
public class VelocityDataDict {

    private static RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

    public String getDictValueName(String termCode, String valueCode, String defaultValueName) {
        if (StringUtils.isNotBlank(termCode) && StringUtils.isNotBlank(valueCode)) {
            Map<String, String> dictMap = JsonUtil.fromJson((String) redisUtils.get(termCode), Map.class);
            return StringUtils.isEmpty(dictMap.get(valueCode)) ? defaultValueName : dictMap.get(valueCode);
        }
        return defaultValueName;
    }

    public String getDictValueNames(String termCode, String defaultValueName, String... valueCodes) {
        List<String> valueNameList = Lists.newArrayList();
        for (String valueCode : valueCodes) {
            valueNameList.add(getDictValueName(termCode, valueCode, defaultValueName));
        }
        return StringUtils.join(valueNameList, ",");
    }


    public String getDictValueCode(String termCode, String valueName, String defaultValueCode) {
        if (StringUtils.isNotBlank(termCode) && StringUtils.isNotBlank(valueName)) {
            Map<String, String> dictMap = JsonUtil.fromJson((String) redisUtils.get(termCode), Map.class);
            for (String key : dictMap.keySet()) {
                if (StringUtils.equals(dictMap.get(key), valueName)) {
                    return key;
                }
            }
        }
        return defaultValueCode;
    }


    public Map<String, String> getDictMap(String termCode) {
        return JsonUtil.fromJson((String) redisUtils.get(termCode), Map.class);
    }

}
