package cn.pioneeruniverse.dev.feignFallback.tool;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.dev.feignInterface.tool.ToolInterface;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2018/10/30
 * Time: 上午 9:27
 */
@Component
public class ToolFallback implements FallbackFactory<ToolInterface> {

    private final static Logger logger = LoggerFactory.getLogger(ToolFallback.class);


    @Override
    public ToolInterface create(Throwable e) {
        return new ToolInterface(){
           /* @Override
            public Map<String, Object> findList() {
                return handleFeignError(e);
            }

            @Override
            public Map<String, Object> updateTool(TblToolInfo tblToolInfo) {
                return handleFeignError(e);
            }*/
        };
    }

    private Map<String,Object> handleFeignError(Throwable cause){
        Map<String,Object> map = new HashMap<String,Object>();
        String message = "部门接口调用故障";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        logger.error(message+":"+exception);
        map.put("errorMessage", message);
        return map;
    }
}
