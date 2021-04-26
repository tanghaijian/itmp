package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.dev.feignInterface.DevManageWebToSystemInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: devManagemWeb模块请求system模块微服务接口熔断处理
 * @Date: Created in 11:11 2018/12/18
 * @Modified By:
 */
@Component
public class DevManageWebToSystemFallback implements FallbackFactory<DevManageWebToSystemInterface> {
    private final static Logger logger = LoggerFactory.getLogger(DevManageWebToSystemFallback.class);

    @Override
    public DevManageWebToSystemInterface create(Throwable throwable) {
        return new DevManageWebToSystemInterface() {
            @Override
            public List<TblDeptInfoDTO> getAllDeptInfo() {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public List<TblCompanyInfoDTO> getAllCompanyInfo() {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getMenuByCode(String menuButtonCode) {
                logger.error(throwable.getMessage(), throwable.getCause());
                return null;
            }
        };
    }
}
