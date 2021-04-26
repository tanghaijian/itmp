package cn.pioneeruniverse.dev.feignFallback;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TestManageToSystemFallback extends BaseController implements FallbackFactory<TestManageToSystemInterface> {

    /**
    * @author author
    * @Description 统一降级处理
    * @Date 2020/9/7
    * @param cause
    * @return cn.pioneeruniverse.dev.feignInterface.TestManageToSystemInterface
    **/
    @Override
    public TestManageToSystemInterface create(Throwable cause) {
        return new TestManageToSystemInterface() {
            @Override
            public Map<String, Object> findFieldByTableName(String objectJson) {
                return handleFeignError(cause, objectJson);
            }

            @Override
            public Map<String, Object> insertMessage(String messageJson) {
                return handleFeignError(cause, messageJson);
            }

			@Override
			public Map<String, Object> sendMessage(String messageJson) {
				return handleFeignError(cause, messageJson);
			}

            @Override
            public List<TblDataDic> getDataDicByTermCode(String termCode) {
                return null;
            }


        };
    }

    private Map<String, Object> handleFeignError(Throwable cause, String value) {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = "测试接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message + ":" + exception);
        logger.error(message + ":" + value);
        map.put("errorMessage", message);
        return map;
    }


}
