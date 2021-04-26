package cn.pioneeruniverse.dev.velocity.tag;

import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.feignInterface.DevManageWebToDevManageInterface;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 16:34 2019/3/18
 * @Modified By:
 */
public class VelocityCodeReview {

    private static DevManageWebToDevManageInterface devManageWebToDevManageInterface = SpringContextHolder.getBean(DevManageWebToDevManageInterface.class);

    public List<Map<String, Object>> getReviewFilesByDevTaskScmId(Long devTaskScmId) {
        return devManageWebToDevManageInterface.getReviewFilesByDevTaskScmId(devTaskScmId);
    }

    public Map<String, Object> getDevTaskDetailByDevTaskId(Long devTaskId) {
        return devManageWebToDevManageInterface.getDevTaskDetailByDevTaskId(devTaskId);
    }

    public List<Map<String, Object>> getReviewGitFilesByDevTaskScmId(Long devTaskScmId) {
        return devManageWebToDevManageInterface.getReviewGitFilesByDevTaskScmId(devTaskScmId);
    }

    public Map<String, Object> getGitFileInfo(Long toolId, Long projectId, String branchName) {
        return devManageWebToDevManageInterface.getGitFileInfo(toolId, projectId, branchName);
    }

}
