package cn.pioneeruniverse.dev.feignInterface;

import cn.pioneeruniverse.dev.feignFallback.DevManageWebToProjectManageFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:26 2019/7/8
 * @Modified By:
 */
@FeignClient(value = "projectManage", fallbackFactory = DevManageWebToProjectManageFallback.class)
public interface DevManageWebToProjectManageInterface {

    @RequestMapping(value = "project/getAllProjectInfo", method = RequestMethod.POST)
    List<Map<String, Object>> getAllProjectInfo();

}
