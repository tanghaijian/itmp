package cn.pioneeruniverse.system.feignFallback.dic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.system.feignInterface.dic.DataDicInterface;
import feign.hystrix.FallbackFactory;

/**
 *
 * @ClassName:DataDicFallback
 * @Description 数据字典接口降级除了类
 * @author author
 * @date 2020年8月24日
 *
 */
@Component
public class DataDicFallback implements FallbackFactory<DataDicInterface> {

    private final static Logger logger = LoggerFactory.getLogger(DataDicFallback.class);

    @Override
    public DataDicInterface create(Throwable cause) {
        return new DataDicInterface() {

            @Override
            public List<TblDataDicDTO> getDataDicByTermCode(String termCode) {
                return null;
            }

        };

    }

    @Deprecated
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
