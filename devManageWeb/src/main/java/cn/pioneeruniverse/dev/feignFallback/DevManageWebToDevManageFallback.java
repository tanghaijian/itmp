package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.dev.feignInterface.DevManageWebToDevManageInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: devManagemWeb模块请求devManage模块微服务接口熔断处理
 * @Date: Created in 16:36 2019/3/18
 * @Modified By:
 */
@Component
public class DevManageWebToDevManageFallback implements FallbackFactory<DevManageWebToDevManageInterface> {

    private final static Logger logger = LoggerFactory.getLogger(DevManageWebToDevManageFallback.class);

    @Override
    public DevManageWebToDevManageInterface create(Throwable throwable) {
        return new DevManageWebToDevManageInterface() {

            @Override
            public List<Map<String, Object>> getReviewFilesByDevTaskScmId(Long devTaskScmId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Map<String, Object>> getReviewGitFilesByDevTaskScmId(Long devTaskScmId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getGitFileInfo(Long toolId, Long projectId, String branchName) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getFileCommentsCountByFileId(Long devTaskScmFileId, Integer scmFileType) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getDevTaskDetailByDevTaskId(Long devTaskScmId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Map<String, Object>> getMyProjectSystems(String token) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Map<String, Object>> getSystemScmSubmitConfigs(Long systemId, Integer scmType) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Map<String, Object>> getSystemScmSubmitRegexConfigs(Long systemId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<Map<String, Object>> getSystemGitLabProjects(Long systemId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }
        };
    }
}
