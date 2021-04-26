package cn.pioneeruniverse.dev.feignInterface;


import cn.pioneeruniverse.dev.feignFallback.TestManageToProjectManageFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 10:35 2019/9/26
 * @Modified By:
 */
@FeignClient(value = "projectManage", fallbackFactory = TestManageToProjectManageFallback.class)
public interface TestManageToProjectManageInterface {

    @RequestMapping(value = "/project/getProjectGroupByProjectGroupId", method = RequestMethod.POST)
    Map<String, Object> getProjectGroupByProjectGroupId(@RequestParam("projectGroupId") Long projectGroupId);

    @RequestMapping(value = "/systemTree/getAssetSystemTreeById", method = RequestMethod.POST)
    Map<String, Object> getAssetSystemTreeById(@RequestParam("id") Long id);

    @RequestMapping(value = "/plan/getProjectPlanById",method= RequestMethod.POST)
    Map<String,Object> getProjectPlanById(@RequestParam("id")Long id);
}
