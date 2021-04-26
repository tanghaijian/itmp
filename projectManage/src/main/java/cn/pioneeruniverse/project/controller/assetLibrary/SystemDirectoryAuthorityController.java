package cn.pioneeruniverse.project.controller.assetLibrary;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryPostAuthority;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRole;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryRoleRelation;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.service.assetsLibrary.SystemDirectoryAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: SystemDirectoryAuthorityController
* @Description: 系统目录权限Controller，项目管理 -文档-【权限设置】
* @author author
* @date 2020年8月5日 下午3:21:47
*
 */
@RestController
@RequestMapping("/assetLibrary/directoryAuthority")
public class SystemDirectoryAuthorityController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(SystemDirectoryAuthorityController.class);

    @Autowired
    SystemDirectoryAuthorityService systemDirectoryAuthorityService;


    /**
     * 
    * @Title: getUserAuthorities
    * @Description: 获取系统目录角色人员列表(暂未用)
    * @author author
    * @param directoryId 系统目录ID
    * @param request
    * @param response
    * @return
    * @throws
     */
    @RequestMapping(value = "getUserAuthorities", method = RequestMethod.POST)
    public BootStrapTablePage<TblSystemDirectoryUserAuthority> getUserAuthorities(Long directoryId, HttpServletRequest request, HttpServletResponse response) {
        return systemDirectoryAuthorityService.getDirectoryUserAuthority(new BootStrapTablePage<TblSystemDirectoryUserAuthority>(request, response), directoryId);
    }

    /**
     * 
    * @Title: getPostAuthorities
    * @Description: 获取系统目录的岗位权限(暂未用)
    * @author author
    * @param directoryId 系统目录ID
    * @param request
    * @param response
    * @return
    * @throws
     */
    @RequestMapping(value = "getPostAuthorities", method = RequestMethod.POST)
    public BootStrapTablePage<TblSystemDirectoryPostAuthority> getPostAuthorities(Long directoryId, HttpServletRequest request, HttpServletResponse response) {
        return systemDirectoryAuthorityService.getDirectoryPostAuthority(new BootStrapTablePage<TblSystemDirectoryPostAuthority>(request, response), directoryId);
    }


    /**
     * 
    * @Title: addOrUpdateUserAuthority
    * @Description: 新增或修改用户权限(暂未用)
    * @author author
    * @param tblSystemDirectoryUserAuthorities
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "/addOrUpdateUserAuthority", method = RequestMethod.POST)
    public AjaxModel addOrUpdateUserAuthority(@RequestBody List<TblSystemDirectoryUserAuthority> tblSystemDirectoryUserAuthorities, HttpServletRequest request) {
        try {
            systemDirectoryAuthorityService.addOrUpdateDirectoryUserAuthority(tblSystemDirectoryUserAuthorities, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: deleteUserAuthority
    * @Description: 删除用户角色权限(暂未用)
    * @author author
    * @param tblSystemDirectoryUserAuthorities
    * @return
    * @throws
     */
    @RequestMapping(value = "/deleteUserAuthority", method = RequestMethod.POST)
    public AjaxModel deleteUserAuthority(@RequestBody List<TblSystemDirectoryUserAuthority> tblSystemDirectoryUserAuthorities) {
        try {
            systemDirectoryAuthorityService.deleteDirectoryUserAuthority(tblSystemDirectoryUserAuthorities);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: addOrUpdatePostAuthority
    * @Description: 新增或删除岗位权限(暂未用)
    * @author author
    * @param tblSystemDirectoryPostAuthorities
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "/addOrUpdatePostAuthority", method = RequestMethod.POST)
    public AjaxModel addOrUpdatePostAuthority(@RequestBody List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities, HttpServletRequest request) {
        try {
            systemDirectoryAuthorityService.addOrUpdateDirectoryPostAuthority(tblSystemDirectoryPostAuthorities, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: deletePostAuthority
    * @Description: 删除岗位权限（(暂未用)）
    * @author author
    * @param tblSystemDirectoryPostAuthorities
    * @return
    * @throws
     */
    @RequestMapping(value = "/deletePostAuthority", method = RequestMethod.POST)
    public AjaxModel deletePostAuthority(@RequestBody List<TblSystemDirectoryPostAuthority> tblSystemDirectoryPostAuthorities) {
        try {
            systemDirectoryAuthorityService.deleteDirectoryPostAuthority(tblSystemDirectoryPostAuthorities);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * 
    * @Title: getCurrentUserDirectoryAuthority
    * @Description: 获取当前用户的权限(暂未用)
    * @author author
    * @param request
    * @param projectId
    * @param directoryId
    * @return
    * @throws
     */
    @RequestMapping(value = "/getCurrentUserDirectoryAuthority", method = RequestMethod.POST)
    public Map<String, Boolean> getCurrentUserDirectoryAuthority(HttpServletRequest request, Long projectId, Long directoryId) {
       return null;
    }

    //====================以下是最新需求=============================
    /**
     * 
    * @Title: addOrUpdateSystemDirectoryRole
    * @Description: 新增系统目录的角色，左边的加号图标功能
    * @author author
    * @param tblSystemDirectoryRole
    * @param request
    * @return map key status = 1 正常返回 status =其他异常返回
    *                 
    * @throws
     */
    @RequestMapping("addOrUpdateSystemDirectoryRole")
    public Map<String, Object> addOrUpdateSystemDirectoryRole(TblSystemDirectoryRole tblSystemDirectoryRole,HttpServletRequest request){
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		systemDirectoryAuthorityService.addOrUpdateSystemDirectoryRole(tblSystemDirectoryRole,request);
    		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "新增文档角色失败！");
		}
    	return map;
    }
    
    /**
     * 
    * @Title: getAllSystemDirectoryRole
    * @Description: 查询系目录所拥有的所有角色，弹框左边关联角色数据
    * @author author
    * @param projectId
    * @return
    * @throws
     */
    @RequestMapping("getAllSystemDirectoryRole")
    public Map<String, Object> getAllSystemDirectoryRole(Long projectId){
    	Map<String, Object> map = new HashMap<>();
    	try {
			List<TblSystemDirectoryRole> list = systemDirectoryAuthorityService.getAllSystemDirectoryRole(projectId);
			map.put("systemDirectoryRoles", list);
		} catch (Exception e) {
			return super.handleException(e, "查询系统文档角色失败！");
		}
    	return map;
    }
    
   /**
    * 
   * @Title: getSystemDirectoryRoleUserByRoleId
   * @Description: 获取系统目录角色关联的用户，页面右变部分
   * @author author
   * @param roleId
   * @param pageNumber
   * @param pageSize
   * @return
   * @throws
    */
    
    @RequestMapping("getSystemDirectoryRoleUserByRoleId")
    public Map<String, Object> getSystemDirectoryRoleUserByRoleId(Long roleId,Integer pageNumber,Integer pageSize){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		List<TblUserInfo> users = systemDirectoryAuthorityService.getSystemDirectoryRoleUserByRoleId(roleId,pageNumber,pageSize);
    		Long count = systemDirectoryAuthorityService.getRoleUserCountByRoleId(roleId);
    		map.put("rows",users); 
    		map.put("total", count);
    	}catch (Exception e) {
			return super.handleException(e, "查询角色关联的用户失败！");
		}
    	return map;
    }
    
    /**
     * 查询角色关联的文档目录权限 页面中间部分
     * @param projectId 项目id
     * @param roleId 该项目下系统文档角色
     * @author tingting
     * */
    @RequestMapping("getSystemDirectoryAuth")
    public Map<String, Object> getSystemDirectoryAuth(Long projectId,Long roleId){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		//List<TblSystemDirectoryRoleRelation> list = systemDirectoryAuthorityService.getSystemDirectoryRoleRelationByRoleId(roleId);
    		List<TblSystemDirectory> list = systemDirectoryAuthorityService.getSystemDirectoryAuth(projectId,roleId);
    		map.put("auth", list);
    	}catch (Exception e) {
			return super.handleException(e, "查询角色关联的文档目录权限失败！");
		}
    	return map;
    }
    
    /**
     * 关联人员
     * @param roleId 系统文档角色id
     * @param uids 关联的人员id 以逗号分隔的字符串
     * @author tingting
     * */
    @RequestMapping("addSystemDirectoryRoleUser")
    public Map<String, Object> addSystemDirectoryRoleUser(Long roleId,String uids,HttpServletRequest request){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		systemDirectoryAuthorityService.addSystemDirectoryRoleUser(roleId,uids,request);
    		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "关联人员失败！");
		}
    	return map;
    }
    
    /**
     * 取消关联人员
     * @param roleId 系统文档角色id
     * @param uids 取消关联的人员id 以逗号分隔的字符串
     * @author tingting
     * */
    @RequestMapping("cancelSystemDirectoryRoleUser")
    public Map<String, Object> cancelSystemDirectoryRoleUser(Long roleId,String uids,HttpServletRequest request){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		systemDirectoryAuthorityService.cancelSystemDirectoryRoleUser(roleId,uids,request);
    		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			return super.handleException(e, "取消关联人员失败！");
		}
    	return map;
    }
    
    /**
     * 保存系统文档读写权限的修改
     * @param systemDirectoryRoleRelations 修改后读写有权限的数据
     * @param roleId 系统文档角色
     * @author tingting
     * */
    @RequestMapping(value = "saveSystemDirectoryAuth", method = RequestMethod.POST)
    public Map<String, Object> saveSystemDirectoryAuth(@RequestBody List<TblSystemDirectoryRoleRelation> systemDirectoryRoleRelations,
    													/*Long roleId,*/HttpServletRequest request){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		if(!systemDirectoryRoleRelations.isEmpty() && systemDirectoryRoleRelations.size()>0) {
    			Long roleId = systemDirectoryRoleRelations.get(0).getSystemDirectoryRoleId();
        		systemDirectoryAuthorityService.saveSystemDirectoryAuth(systemDirectoryRoleRelations,roleId,request);
        		map.put("status", Constants.ITMP_RETURN_SUCCESS);
    		}
		} catch (Exception e) {
			return super.handleException(e, "保存系统文档权限失败！！");
		}
    	return map;
    }
   
    /**
     * 获取当前登录用户的系统文档权限
     * @param projectId 项目id
     * @param systemDirectoryId 文档目录id
     * @author tingting
     * */
    @RequestMapping("getCurrentUserSystemDirecoryAuth")
    public Map<String, Object> getCurrentUserSystemDirecoryAuth(Long projectId,Long systemDirectoryId,HttpServletRequest request){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		Map<String, Object> authMap = systemDirectoryAuthorityService.getCurrentUserSystemDirecoryAuth(projectId,systemDirectoryId,request);
    		map.put("auth", authMap);
    	} catch (Exception e) {
			return super.handleException(e, "获取当前登录用户的权限失败！");
		}
    	return map;
    }
    
    
    
    
    
    
    
    
    
    
}
