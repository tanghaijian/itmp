package cn.pioneeruniverse.dev.velocity.tag;

import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.feignInterface.DevManageWebToDevManageInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:34 2019/5/27
 * @Modified By:
 */
public class VelocityCodeBase {

    private static DevManageWebToDevManageInterface devManageWebToDevManageInterface = SpringContextHolder.getBean(DevManageWebToDevManageInterface.class);

    public List<Map<String, Object>> getMyProjectSystems(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return devManageWebToDevManageInterface.getMyProjectSystems(token);
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getSystemScmSubmitConfigs(Long systemId, Integer scmType) {
        if (systemId != null && scmType != null) {
            return devManageWebToDevManageInterface.getSystemScmSubmitConfigs(systemId, scmType);
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getSystemScmSubmitRegexConfigs(Long systemId) {
        if (systemId != null) {
            return devManageWebToDevManageInterface.getSystemScmSubmitRegexConfigs(systemId);
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getSystemGitLabProjects(Long systemId) {
        if (systemId != null) {
            return devManageWebToDevManageInterface.getSystemGitLabProjects(systemId);
        } else {
            return null;
        }
    }

}
