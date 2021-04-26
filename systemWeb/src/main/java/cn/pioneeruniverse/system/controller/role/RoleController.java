package cn.pioneeruniverse.system.controller.role;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.SpellUtil;
import cn.pioneeruniverse.system.feignInterface.role.RoleInterface;
import cn.pioneeruniverse.system.vo.role.TblRoleInfo;

/**
 * 
* @ClassName: RoleController
* @Description: 角色/权限管理controller
* @author author
* @date 2020年9月3日 下午2:58:55
*
 */
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

	
	@Autowired
	private RoleInterface roleInterface;
	
	
	/**
	 * 
	* @Title: toRoleManage
	* @Description: 系统信息管理-角色管理首页
	* @author author
	* @param request
	* @return ModelAndView
	 */
	@RequestMapping("toRoleManage")
	public ModelAndView toRoleManage(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.setViewName("systemManagement/roleManagement");
		return view;
	}

	/**
	 * @deprecated
	* @Title: toRoleAdd
	* @Description: 废弃
	* @author author
	* @return ModelAndView
	 */
	@RequestMapping("toRoleAdd")
	public ModelAndView toRoleAdd(){
		ModelAndView view = new ModelAndView();
		view.setViewName("role/roleAdd");
		return view;
	}
	
	/**
	 * @deprecated
	* @Title: toRoleUpdate
	* @Description: 废弃
	* @author author
	* @param roleId
	* @return ModelAndView
	 */
	@RequestMapping("toRoleUpdate")
	public ModelAndView toRoleUpdate(Long roleId){
		ModelAndView view = new ModelAndView();
		view.setViewName("role/roleUpdate");
		view.addObject("roleId", roleId);
		return view;
	}
	
	/**
	 * @deprecated
	* @Title: getAllRole
	* @Description: 移至system/role/getAllRole
	* @author author
	* @param roleJson
	* @param pageIndex
	* @param pageSize
	* @return
	* @return Map<String,Object>
	 */
	@RequestMapping(value="getAllRole",method=RequestMethod.POST)
	public Map<String,Object> getAllRole(String roleJson, Integer pageIndex,Integer pageSize){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result = roleInterface.getAllRole(roleJson, pageIndex,pageSize);
		}catch(Exception e){
			return this.handleException(e, "获取权限集失败");
		}
		
		return result;
	}
	
	
	/**
	 * @deprecated
	* @Title: insertRole
	* @Description: 移至system/role/insertRole
	* @author author
	* @param role
	* @return
	* @return Map<String,Object>
	 */
	@RequestMapping(value="insertRole",method=RequestMethod.POST)
	public Map<String,Object> insertRole(TblRoleInfo role){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			Long userId = 1L;//SecurityUserHolder.getCurrentUser().getId()
			role.setCreateBy(userId);
			role.setLastUpdateBy(userId);
			String roleName = role.getRoleName();
			String roleCode =  SpellUtil.getUpEname(roleName);
			role.setRoleCode(roleCode.toUpperCase());
			result = roleInterface.insertRole(role);
		}catch(Exception e){
			return this.handleException(e, "添加权限失败");
		}
		
		return result;
	}
	
	/**
	 * @deprecated
	* @Title: findRoleById
	* @Description: 移至system/role/findRoleById
	* @author author
	* @param roleId
	* @return
	* @return Map<String,Object>
	 */
	@RequestMapping(value="findRoleById",method=RequestMethod.POST)
	public Map<String,Object> findRoleById(Long roleId){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result = roleInterface.findRoleById(roleId);
		}catch(Exception e){
			return this.handleException(e, "获取权限详情失败");
		}
		
		return result;
	}
	
	
	
	/**
	 * @deprecated
	* @Title: updateRole
	* @Description: 移至system/role/updateRole
	* @author author
	* @param role
	* @return
	* @return Map<String,Object>
	 */
	@RequestMapping(value="updateRole",method=RequestMethod.POST)
	public Map<String,Object> updateRole(TblRoleInfo role){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			Long userId = 1L;//SecurityUserHolder.getCurrentUser().getId()
			role.setLastUpdateBy(userId);
			result = roleInterface.updateRole(role);
		}catch(Exception e){
			return this.handleException(e, "更新权限失败");
		}
		
		return result;
	}
	
	/**
	 * @deprecated
	* @Title: delRole
	* @Description: 移至system/role/delRole
	* @author author
	* @param roleIds
	* @return
	* @return Map<String,Object>
	 */
	@RequestMapping(value="delRole",method=RequestMethod.POST)
	public Map<String,Object> delRole(String roleIds){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			Long lastUpdateBy = 1L;//SecurityUserHolder.getCurrentUser().getId()
			roleInterface.delRole(roleIds,lastUpdateBy);
		}catch(Exception e){
			return this.handleException(e, "删除权限失败");
		}
		return result;
	}

	/*@RequestMapping(value = "getRoleMenu",method = RequestMethod.POST)
	public Map<String,Object> getRoleMenu(Long id){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			result = roleInterface.getRoleMenu(id);
		}catch(Exception e){
			return this.handleException(e, "获取菜单按钮权限失败");
		}
		return result;
	}*/

}

