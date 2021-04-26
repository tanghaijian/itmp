package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.dto.SystemTreeVo;
import cn.pioneeruniverse.dev.feignInterface.DevManageToProjectManageInterface;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;


@Component
public class DevManageToProjectManageFallback extends BaseController implements FallbackFactory<DevManageToProjectManageInterface> {

    /**
    * @author author
    * @Description 统一降级处理
    * @Date 2020/9/7
    * @param cause
    * @return cn.pioneeruniverse.dev.feignInterface.DevManageToProjectManageInterface
    **/
    @Override
    public DevManageToProjectManageInterface create(Throwable cause) {
        return new DevManageToProjectManageInterface() {

            @Override
            public Map<String, Object> insertFirstSystem(SystemTreeVo systemTreeVo, Long currentUserId) {
                return handleFeignError(cause, currentUserId);
            }

            @Override
            public Map<String, Object> getProjectGroupByProjectGroupId(Long projectGroupId) {
                logger.error(cause.getMessage(), cause.getCause());
                return null;
            }

            @Override
            public Map<String, Object> getAssetSystemTreeById(Long id) {
                logger.error(cause.getMessage(), cause.getCause());
                return null;
            }
        };
    }

    private Map<String, Object> handleFeignError(Throwable cause, Object objectJson) {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = "开发接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message + ":" + exception);
        logger.error(message + ":" + objectJson.toString());
        map.put("errorMessage", message);
        return map;
    }
}
