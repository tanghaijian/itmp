package cn.pioneeruniverse.project.feignInterface;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.project.entity.TblDeptInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.feignFallback.ProjectManageWebToSystemFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: projectManagemWeb模块请求system模块微服务接口
 * @Date: Created in 15:06 2020/08/24
 * @Modified By:
 */
@FeignClient(value = "system", fallbackFactory = ProjectManageWebToSystemFallback.class)
public interface ProjectManageWebToSystemInterface {

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

    /**
     * @Title: getAllCompanyInfo
     * @Description: 获取所有公司
     * @author author
     * @return List<TblCompanyInfoDTO>
     * @throws
     */
    @RequestMapping(value = "company/getAllCompanyInfo", method = RequestMethod.POST)
    List<TblCompanyInfoDTO> getAllCompanyInfo();
    /**
     * @Title: getDept
     * @Description: 获取所有部门
     * @author author
     * @return List<TblDeptInfo>
     * @throws
     */
    @RequestMapping(value="user/getDept",method=RequestMethod.POST)
    List<TblDeptInfo> getDept();

    /**
     * @Title: getUser
     * @Description: 获取所有用户
     * @author author
     * @return List<TblUserInfo>
     * @throws
     */
    @RequestMapping(value="user/getUserNoCon",method=RequestMethod.POST)
    List<TblUserInfo> getUser();

    /**
     * @Title: findUserById
     * @Description: 根据ID获取用户信息
     * @author author
     * @param userId 用户ID
     * @return map
     * @throws
     */
    @RequestMapping(value = "user/findUserById", method = RequestMethod.POST)
    Map<String, Object> findUserById(@RequestParam("userId") Long userId);

    /**
     * @Title: findUserById
     * @Description: 根据ID获取部门信息
     * @author author
     * @param id 部门ID
     * @return map
     * @throws
     */
    @RequestMapping(value="dept/selectDeptById",method=RequestMethod.POST)
    Map<String,Object> selectDeptById(@RequestParam("id") Long id);
}
