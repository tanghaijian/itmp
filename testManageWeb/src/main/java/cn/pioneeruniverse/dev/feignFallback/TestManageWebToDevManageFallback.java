package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.dev.feignInterface.TestManageWebToDevManageInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:53 2019/9/24
 * @Modified By:
 */
@Component
public class TestManageWebToDevManageFallback implements FallbackFactory<TestManageWebToDevManageInterface> {

    private static final Logger logger = LoggerFactory.getLogger(TestManageWebToDevManageFallback.class);

    @Override
    public TestManageWebToDevManageInterface create(Throwable throwable) {
        return new TestManageWebToDevManageInterface() {

            @Override
            public List<Map<String, Object>> getSystemVersionsBySystemId(Long systemId) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

        };
    }
}
