package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.dev.feignInterface.TestManageToProjectManageInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:37 2019/9/26
 * @Modified By:
 */
@Component
public class TestManageToProjectManageFallback implements FallbackFactory<TestManageToProjectManageInterface> {

    private static final Logger logger = LoggerFactory.getLogger(TestManageToProjectManageFallback.class);

    @Override
    public TestManageToProjectManageInterface create(Throwable throwable) {
        return new TestManageToProjectManageInterface() {

            @Override
            public Map<String, Object> getProjectGroupByProjectGroupId(Long projectGroupId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getAssetSystemTreeById(Long id) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getProjectPlanById(Long id) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }
        };
    }
}
