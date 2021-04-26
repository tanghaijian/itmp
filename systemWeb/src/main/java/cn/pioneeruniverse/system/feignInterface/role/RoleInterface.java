package cn.pioneeruniverse.system.feignInterface.role;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.system.feignFallback.role.RoleFallback;
import cn.pioneeruniverse.system.vo.role.TblRoleInfo;

/**
 * @deprecated
* @ClassName: RoleInterface
* @Description: 角色相关逻辑处理移至system模块下
* @author author
* @date 2020年9月4日 上午10:31:13
*
 */
@FeignClient(value="system",fallbackFactory=RoleFallback.class)
public interface RoleInterface {
	
	@RequestMapping(value="role/getAllRole",method=RequestMethod.POST)
    Map<String,Object> getAllRole(@RequestParam("roleJson") String roleJson, @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize);
	
	
	@RequestMapping(value="role/insertRole",method=RequestMethod.POST)
    Map<String,Object> insertRole(@RequestBody TblRoleInfo role);
	
	
	@RequestMapping(value="role/findRoleById",method=RequestMethod.POST)
    Map<String,Object> findRoleById(@RequestParam("roleId") Long roleId);
	
	
	@RequestMapping(value="role/updateRole",method=RequestMethod.POST)
    Map<String,Object> updateRole(@RequestBody TblRoleInfo role);
	
	
	
	@RequestMapping(value="role/delRole",method=RequestMethod.POST)
    Map<String,Object> delRole(@RequestParam("roleIds") String roleIds, @RequestParam("lastUpdateBy") Long lastUpdateBy);

	/*@RequestMapping(value="role/getRoleMenu",method=RequestMethod.POST)
    Map<String,Object> getRoleMenu(@RequestParam("id") Long id);*/
}
