package cn.pioneeruniverse.dev.feignInterface;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.dev.feignFallback.DevManageWebToSystemFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: devManagemWeb模块请求system模块微服务接口
 * @Date: Created in 11:06 2018/12/18
 * @Modified By:
 */
@FeignClient(value = "system", fallbackFactory = DevManageWebToSystemFallback.class)
public interface DevManageWebToSystemInterface {

    /**
     * @param
     * @return java.util.List<cn.pioneeruniverse.common.dto.TblDeptInfoDTO>
     * @Description 获取全部部门
     * @MethodName getDeptList
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/18 11:17
     */
    @RequestMapping(value = "dept/getAllDeptInfo", method = RequestMethod.POST)
    List<TblDeptInfoDTO> getAllDeptInfo();

    @RequestMapping(value = "company/getAllCompanyInfo", method = RequestMethod.POST)
    List<TblCompanyInfoDTO> getAllCompanyInfo();
    
    @RequestMapping(value = "menu/getMenuByCode", method = RequestMethod.POST)
    Map<String, Object> getMenuByCode(@RequestParam("menuButtonCode") String menuButtonCode);


}
