package cn.pioneeruniverse.project.feignFallback;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.project.feignInterface.ProjectToSystemInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProjectToSystemFallback implements FallbackFactory<ProjectToSystemInterface> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectToSystemFallback.class);

    @Override
    public ProjectToSystemInterface create(Throwable throwable) {
        return new ProjectToSystemInterface() {


            @Override
            public Map<String, String> insertMessage(String message) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> findFieldByTableName(String tableName) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Map<String, String> sendMessage(String messageJson) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<Map<String, Object>> getUserInfoByUserIds(List<Long> userIds) {
                logger.error(throwable.getMessage(), throwable);
                return null;
            }


        };
    }
}
