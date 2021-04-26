package cn.pioneeruniverse.dev.feignInterface;

import cn.pioneeruniverse.dev.entity.TblDeptInfo;
import cn.pioneeruniverse.dev.feignFallback.TestManageWebToSystemFallBack;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: testManagemWeb模块请求system模块微服务接口
 * @Date: Created in 15:06 2020/08/27
 * @Modified By:
 */
@FeignClient(value = "system", fallbackFactory = TestManageWebToSystemFallBack.class)
public interface TestManageWebToSystemInterface {

    /**
     * @param
     * @return java.util.Map<String, Object>
     * @Description 获取菜单按钮
     * @MethodName getMenuByCode
     * @author
     * @param menuButtonCode 按钮编号
     * @Date 2020/08/27 15:17
     */
    @RequestMapping(value = "menu/getMenuByCode", method = RequestMethod.POST)
    Map<String, Object> getMenuByCode(@RequestParam("menuButtonCode") String menuButtonCode);

    /**
     * @param
     * @return java.util.List<TblDeptInfo>
     * @Description 获取所有部门
     * @MethodName getDept
     * @author
     * @Date 2020/08/27 15:17
     */
    @RequestMapping(value="user/getDept",method=RequestMethod.POST)
    List<TblDeptInfo> getDept();
}
