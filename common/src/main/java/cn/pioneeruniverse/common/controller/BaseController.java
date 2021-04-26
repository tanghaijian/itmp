package cn.pioneeruniverse.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pioneeruniverse.common.constants.Constants;

import java.util.HashMap;
import java.util.Map;

/**
* @author author
* @Description 公共controller
* @Date 2020/9/3
* @return 
**/
public class BaseController {
    public final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
    * @author author
    * @Description 返回错误信息 和 异常信息
    * @Date 2020/9/3
    * @param e
    * @param message
    * @return java.util.Map<java.lang.String,java.lang.Object>
    *     map.put("status", Constants.ITMP_RETURN_FAILURE); 失败状态
    *     map.put("errorMessage", message); 错误信息
    *     map.put("e",e.getMessage()); 异常信息
    **/
    public Map<String, Object> handleException(Exception e, String message) {
        e.printStackTrace();
        logger.error(message + ":" + e.getMessage(), e);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.ITMP_RETURN_FAILURE);
        map.put("errorMessage", message);
        map.put("e",e.getMessage());
        return map;
    }
}
