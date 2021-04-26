package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.dev.feignInterface.DevManageWebToProjectManageInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:30 2019/7/8
 * @Modified By:
 */
@Component
public class DevManageWebToProjectManageFallback implements FallbackFactory<DevManageWebToProjectManageInterface> {

    private final static Logger logger = LoggerFactory.getLogger(DevManageWebToProjectManageFallback.class);

    @Override
    public DevManageWebToProjectManageInterface create(Throwable throwable) {
        return new DevManageWebToProjectManageInterface() {
            @Override
            public List<Map<String, Object>> getAllProjectInfo() {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }
        };
    }
}
