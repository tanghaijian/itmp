package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.PinYinUtil;
import cn.pioneeruniverse.common.velocity.tag.VelocityDataDict;
import cn.pioneeruniverse.project.entity.*;
import cn.pioneeruniverse.project.service.userpostpower.UserPostPowerService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName: UserPostPowerController
 * @Description: (运维项目岗位权限分配)
 * @author author
 * @date Jul 28, 2020 11:33:07 AM
 *
 */
@RestController
@RequestMapping("userPostPower")
public class UserPostPowerController extends BaseController {

	@Autowired
	private UserPostPowerService userPostPowerService;

	private static Logger log = LoggerFactory.getLogger(UserPostPowerController.class);

	/**
	 *@Description 获取项目所属岗位
	 *@Date 2020/7/21
	 *@Param [projectId] 项目id
	 *@return java.util.Map<java.lang.String,java.lang.Object> key:data value:List<TblRoleInfo>角色信息
	 **/
	@RequestMapping(value = "getUserPostByProject",method= RequestMethod.POST)
	public Map<String,Object> getUserPostByProject(Long projectId,HttpServletRequest request){
		VelocityDataDict dict= new VelocityDataDict();
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String,String> userPostMap = dict.getDictMap("TBL_PROJECT_GROUP_USER_USER_POST");
			for(Map.Entry<String, String> entry : userPostMap.entrySet()){
				//查询所属项目是否拥有岗位
				List<TblRoleInfo> roleInfoList =
						userPostPowerService.getUserPostRole(projectId,Integer.valueOf(entry.getKey()));
				//如果没有自动创建
				if(roleInfoList==null||roleInfoList.size()<=0){
					TblRoleInfo roleInfo = new TblRoleInfo();
					roleInfo.setProjectId(projectId);
					roleInfo.setUserPost(Integer.valueOf(entry.getKey()));
					roleInfo.setRoleName(entry.getValue());
					String empNo = projectId.toString()+PinYinUtil.getPingYin(entry.getValue()).toUpperCase(Locale.ENGLISH);
					roleInfo.setRoleCode(empNo);
					CommonUtil.setBaseValue(roleInfo,request);
					userPostPowerService.insertUserPostRole(roleInfo);
				}
			}
			List<TblRoleInfo> roleInfoList1 = userPostPowerService.getUserPostRole(projectId,null);
			result.put("data", JSONObject.toJSONString(roleInfoList1));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("mes:" + e.getMessage(), e);
		}
		return result;
	}

	@RequestMapping(value = "setId",method = RequestMethod.POST)
	public void setId(Long id){
		userPostPowerService.setRoleId(id);
	}

	/**
	 *@Description 获取岗位对应的菜单权限
	 *@Date 2020/7/21
	 *@Param [id] 岗位id
	 *@return java.util.Map<java.lang.String,java.lang.Object> Key：result value:List<TblRoleInfo>角色列表信息
	 **/
	@RequestMapping(value = "getRoleMenu",method = RequestMethod.POST)
	public Map<String,Object> getRoleMenu1(Long id){
		Map<String,Object> result = new HashMap<>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			List<TblRoleInfo> list = userPostPowerService.getRoleMenu(id);
			result.put("result",list);
		}catch(Exception e){
			return this.handleException(e, "获取菜单按钮失败");
		}

		return result;
	}

	/**
	 *@Description 获取没有关联项目的角色信息
	 *@Date 2020/7/21
	 *@return java.util.Map<java.lang.String,java.lang.Object>Key：result value:List<TblRoleInfo>角色列表信息
	 **/
	@RequestMapping(value = "getNoProjectRole",method = RequestMethod.POST)
	public Map<String,Object> getNoProjectRole(){
		Map<String,Object> result = new HashMap<>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			List<TblRoleInfo> list = userPostPowerService.getNoProjectRole();
			result.put("result",list);
		}catch(Exception e){
			return this.handleException(e, "获取菜单按钮失败");
		}

		return result;
	}
}
