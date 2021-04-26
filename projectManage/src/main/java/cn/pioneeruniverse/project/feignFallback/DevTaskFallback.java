package cn.pioneeruniverse.project.feignFallback;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.feignInterface.DevTaskInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DevTaskFallback extends BaseController implements FallbackFactory<DevTaskInterface> {

    private static final Logger logger = LoggerFactory.getLogger(DevTaskFallback.class);

    /**
    * @author author
    * @Description 统一降级处理
    * @Date 2020/9/9
    * @param cause
    * @return cn.pioneeruniverse.project.feignInterface.DevTaskInterface
    **/
    @Override
    public DevTaskInterface create(Throwable cause) {
        return new DevTaskInterface() {

            @Override
            public Map<String, Object> changeCancelStatus(Long requirementId) {
                return handleFeignError(cause);
            }

            @Override
            public Map<String, Object> cancelStatusReqFeature(Long reqFeatureId) {
                return null;
            }

            @Override
            public Map<String, Object> getCodeFilesByDevTaskId(Long devTaskId) {
                return null;
            }

            @Override
            public String getSystemNameById(Long systemId) {
                logger.error(cause.getMessage(), cause);
                return null;
            }

            @Override
            public List<Map<String, Object>> getMyProjectSystems(String token) {
                logger.error(cause.getMessage(), cause);
                return null;
            }

            @Override
            public String getNewFeatureCode() {
                return null;
            }
        };
    }

    private Map<String, Object> handleFeignError(Throwable cause) {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = "接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message + ":" + exception);
        map.put("errorMessage", message);
        return map;
    }
}
