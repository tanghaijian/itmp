package cn.pioneeruniverse.dev.feignInterface;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 16:35 2019/3/18
 * @Modified By:
 */

import cn.pioneeruniverse.dev.feignFallback.DevManageWebToDevManageFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "devManage", fallbackFactory = DevManageWebToDevManageFallback.class)
public interface DevManageWebToDevManageInterface {

    @RequestMapping(value = "codeReview/getReviewFilesByDevTaskScmId", method = RequestMethod.POST)
    List<Map<String, Object>> getReviewFilesByDevTaskScmId(@RequestParam("devTaskScmId") Long devTaskScmId);

    @RequestMapping(value = "codeReview/getReviewGitFilesByDevTaskScmId", method = RequestMethod.POST)
    List<Map<String, Object>> getReviewGitFilesByDevTaskScmId(@RequestParam("devTaskScmId") Long devTaskScmId);

    @RequestMapping(value = "codeReview/getGitFileInfo", method = RequestMethod.POST)
    Map<String, Object> getGitFileInfo(@RequestParam("toolId") Long toolId, @RequestParam("projectId") Long projectId, @RequestParam("branchName") String branchName);

    @RequestMapping(value = "codeReview/getFileCommentsCountByFileId", method = RequestMethod.POST)
    Map<String, Object> getFileCommentsCountByFileId(@RequestParam("devTaskScmFileId") Long devTaskScmFileId,@RequestParam("scmFileType") Integer scmFileType);

    @RequestMapping(value = "codeReview/getDevTaskDetailByDevTaskId", method = RequestMethod.POST)
    Map<String, Object> getDevTaskDetailByDevTaskId(@RequestParam("devTaskId") Long devTaskId);

    @RequestMapping(value = "version/getMyProjectSystems", method = RequestMethod.POST)
    List<Map<String, Object>> getMyProjectSystems(@RequestParam("token") String token);

    @RequestMapping(value = "version/getSystemScmSubmitConfigs", method = RequestMethod.POST)
    List<Map<String, Object>> getSystemScmSubmitConfigs(@RequestParam("systemId") Long systemId, @RequestParam("scmType") Integer scmType);

    @RequestMapping(value = "version/getSystemScmSubmitRegexConfigs",method = RequestMethod.POST)
    List<Map<String,Object>> getSystemScmSubmitRegexConfigs (@RequestParam("systemId") Long systemId);

    @RequestMapping(value = "version/getSystemGitLabProjects", method = RequestMethod.POST)
    List<Map<String, Object>> getSystemGitLabProjects(@RequestParam("systemId") Long systemId);

}
