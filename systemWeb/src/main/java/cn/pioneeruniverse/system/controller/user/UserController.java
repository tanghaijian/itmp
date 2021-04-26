package cn.pioneeruniverse.system.controller.user;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.ExcelUtil;
import cn.pioneeruniverse.common.utils.SpellUtil;
import cn.pioneeruniverse.system.feignInterface.role.RoleInterface;
import cn.pioneeruniverse.system.feignInterface.user.UserInterface;
import cn.pioneeruniverse.system.vo.role.TblRoleInfo;
import cn.pioneeruniverse.system.vo.user.ExcelUser;
import cn.pioneeruniverse.system.vo.user.TblUserInfo;

/**
 * 
* @ClassName: UserController
* @Description: 用户管理controller
* @author author
* @date 2020年9月3日 下午5:39:17
*
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	

	@Autowired
	private UserInterface userInterface;
	@Autowired
	private RoleInterface roleInterface;
	
	/**
	 * 
	* @Title: toUserManage
	* @Description: 系统信息管理-用户管理
	* @author author
	* @param request
	* @return ModelAndView
	 */
	@RequestMapping(value = "toUserManage")
	public ModelAndView toUserManage(HttpServletRequest request) {
		
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("url",Constants.SYSTEM_MANAGE_UI_URL+request.getRequestURI());
		view.setViewName("systemManagement/userManage");
		return view;
	}

	/**
	 * @deprecated
	 * 测试案例执行用户弹窗
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toUserModal",method=RequestMethod.GET)
	public ModelAndView toUserModal(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("token", CommonUtil.getToken(request));
		view.addObject("userId",CommonUtil.getCurrentUserId(request));
		view.addObject("userUrl",request.getParameter("userUrl"));
		view.addObject("singleValue",request.getParameter("singleValue"));
		view.addObject("paramDatas",request.getParameter("paramDatas"));
		view.setViewName("systemManagement/userModalShow");
		return view;
	}

	//公司部门信息
	/*@RequestMapping(value="getCompany",method=RequestMethod.POST)
	public List<Company> getCompany() {
		List<Company> list=null;
		list=userInterface.getCompany();
		
         return list;
	}*/
	//获取部门信息
	/*@RequestMapping(value="getDept",method=RequestMethod.POST)
	public List<TblDeptInfo> getDept() {
		List<TblDeptInfo> list=null;
		list=userInterface.getDept();
		
         return list;
	}*/
	
	/**
	 * @deprecated
	* @Title: toUserAdd
	* @Description: 废弃
	* @author author
	* @return ModelAndView
	 */
	@RequestMapping(value = "toUserAdd")
	public ModelAndView toUserAdd() {
		ModelAndView view = new ModelAndView();
		Map<String, Object> result = roleInterface.getAllRole(null, null, null);
		if (result != null && result.get("status").equals(Constants.ITMP_RETURN_SUCCESS) && result.get("data") != null) {
			view.addObject("roles",
					JSONObject.parseObject(result.get("data").toString(), new TypeReference<List<TblRoleInfo>>() {
					}));
		}

		view.setViewName("user/userAdd");
		return view;
	}

	/**
	 * @deprecated
	* @Title: toUserUpdate
	* @Description: 废弃
	* @author author
	* @param userId
	* @return ModelAndView
	 */
	@RequestMapping(value = "toUserUpdate")
	public ModelAndView toUserUpdate(Long userId) {
		ModelAndView view = new ModelAndView();
		view.addObject("updateUserId", userId);
		Map<String, Object> result = roleInterface.getAllRole(null, null, null);
		if (result != null && result.get("status").equals(Constants.ITMP_RETURN_SUCCESS) && result.get("data") != null) {
			view.addObject("roles",
					JSONObject.parseObject(result.get("data").toString(), new TypeReference<List<TblRoleInfo>>() {
					}));
		}
		view.setViewName("user/userUpdate");
		return view;
	}
	
	
	/**
	 * @deprecated
	* @Title: getExcelUser
	* @Description: 废弃，移至system/user/getExcelAllUser
	* @author author
	* @param excelData
	* @param request
	* @param response
	* @return void
	 */
	@ResponseBody
	@RequestMapping(value="getExcelAllUser")
	public void getExcelUser(String excelData,HttpServletRequest request,HttpServletResponse response){
		List<ExcelUser> list = new ArrayList<ExcelUser>();  
		try {
	        list = JSONObject.parseArray(excelData, ExcelUser.class);
			//list=userInterface.getExcelAllUser();
			String[] title = {"ID","姓名","用户名","所属处室","在职状态","角色","入职日期"};
	         //excel文件名
			SimpleDateFormat sformat=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
			  long now=System.currentTimeMillis();
			String day=sformat.format(now);
			String fileName = "用户信息表"+day+".xls";
	         //sheet名
			String sheetName = "用户信息表";
			String[][] content = new String[list.size()][];
			SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd");
			if(list.size()>0&&list!=null) {
				for (int i= 0; i < list.size(); i++) {
					content[i] = new String[title.length];
	         //  TblUserInfo  obj = list.get(i);
					content[i][0] = list.get(i).getId().toString();
					content[i][1] = list.get(i).getUserName().toString();
					content[i][2] = list.get(i).getUserAccount().toString();
					content[i][3] = list.get(i).getDeptName().toString();
					content[i][4] = list.get(i).getUserStatus();
					content[i][5] = list.get(i).getRoleName().toString();
					Date date=list.get(i).getEntryDate();
					if(null!=date) {
						content[i][6] = simdate.format(list.get(i).getEntryDate());
					}
					
					
				}
			}
	         //创建HSSFWorkbook 
	         HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
	        this.setResponseHeader(response, fileName);
	       OutputStream os = response.getOutputStream();
	       	wb.write(os);
	       	os.flush();
	       	os.close();
		} catch (Exception e) {
			e.printStackTrace();
		     logger.error("mes:" + e.getMessage(), e);
		}
	}
	 //发送响应流方法

    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }
	
	//获取信息
/*	@RequestMapping(value = "getAllUserByAjax",method = RequestMethod.POST)
	public Map getAllUserByAjax(  String FindUser, Integer rows,Integer page) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = userInterface.getAllUser(FindUser,page,rows);//传输
		} catch (Exception e) {
			e.printStackTrace();
		     logger.error("mes:" + e.getMessage(), e);
		}

		return result;
	}*/
 
	/**
	 * @deprecated
	* @Title: saveUser
	* @Description: 移至system/user/saveUser
	* @author author
	* @param user
	* @param roleIds
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "saveUser", method = RequestMethod.POST)
	public Map<String, Object> saveUser(String user, String roleIds) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblUserInfo newUser = JSONObject.parseObject(user, TblUserInfo.class);
			newUser.setCreateBy(1L);//SecurityUserHolder.getCurrentUser().getId());
			newUser.setLastUpdateBy(1L);//SecurityUserHolder.getCurrentUser().getId());
			result = userInterface.saveUser(JSONObject.toJSONString(newUser), roleIds);
		} catch (Exception e) {
			return handleException(e, "新增用户失败");
		}

		return result;
	}

	/**
	 * @deprecated
	* @Title: delUser
	* @Description: 移至system/user/delUser
	* @author author
	* @param userIds
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "delUser", method = RequestMethod.POST)
	public Map<String, Object> delUser(String userIds) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long lastUpdateBy = 1L;//SecurityUserHolder.getCurrentUser().getId()
			result = userInterface.delUser(userIds, lastUpdateBy);
		} catch (Exception e) {
			return handleException(e, "删除用户失败");
		}

		return result;
	}

	/**
	 * @deprecated
	* @Title: updateUser
	* @Description: 移至system/user/updateUser
	* @author author
	* @param user
	* @param roleIds
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public Map<String, Object> updateUser(String user, String roleIds) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblUserInfo newUser = JSONObject.parseObject(user, TblUserInfo.class);
			newUser.setLastUpdateBy(1L);//SecurityUserHolder.getCurrentUser().getId());
			result = userInterface.updateUser(JSONObject.toJSONString(newUser), roleIds);
		} catch (Exception e) {
			return handleException(e, "更新用户失败");
		}

		return result;
	}

	/**
	 * @deprecated
	* @Title: findUserById
	* @Description: 移至system/user/findUserById
	* @author author
	* @param userId
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "findUserById", method = RequestMethod.POST)
	public Map<String, Object> findUserById(Long userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			result = userInterface.findUserById(userId);
		} catch (Exception e) {
			return handleException(e, "获取用户失败");
		}
		return result;
	}

	// 获取用户中文名转化为英文，如果重复，会加上_以及数字编号
	@RequestMapping(value = "getUserAccount", method = RequestMethod.POST)
	public Map<String, Object> getUserAccount(String realName) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			String userAccount = SpellUtil.getUpEname(realName);
			result = userInterface.getPreUser(userAccount);
			if (result != null && result.get("status").equals(Constants.ITMP_RETURN_SUCCESS)) {
				Long count = Long.valueOf(result.get("data").toString());
				if (count == 0)
					result.put("data", userAccount);
				else
					result.put("data", userAccount + "_" + count);
			}
		} catch (Exception e) {
			return handleException(e, "获取用户失败");
		}
		return result;
	}

	/**
	 * @deprecated
	* @Title: resetPassword
	* @Description: 移至system/user/resetPassword
	* @author author
	* @param ids
	* @return Map<String,Object>
	 */
	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public Map<String, Object> resetPassword(String ids) {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<Long> userIds = new ArrayList<Long>();
			String[] selIds = ids.split(",");
			for (String id : selIds) {
				userIds.add(Long.valueOf(id));
			}

			TblUserInfo user = new TblUserInfo();
			user.setUserIds(userIds);
			user.setLastUpdateBy(1L);//SecurityUserHolder.getCurrentUser().getId());
			result = userInterface.resetPassword(user);
		} catch (Exception e) {
			return handleException(e, "更新用户失败");
		}

		return result;
	}
	/*user.setUserName(userName == ""?null:userName);
	if(StringUtils.isNotEmpty(companyId)){
		user.setCompanyId(Long.parseLong(companyId));
	}
	if(StringUtils.isNotEmpty(deptId)){
		user.setDeptId(Long.parseLong(deptId));
	}
	if(StringUtils.isNotEmpty(userType)) {
		user.setUserType(Integer.parseInt(userType));
	}
	if (StringUtils.isNotEmpty(userStatus)) {
		user.setUserStatus(Integer.parseInt(userStatus));
	}
	//入职时间
	Date entryD=null;
	if(entryDate!= null && entryDate.length()!= 0) {
		entryD=simdate.parse(entryDate);
	}
	user.setEntryDate(entryD);
	//离职时间
	Date leaveD=null;
	if(leaveDate!=null&&leaveDate.length()!= 0){
		leaveD=simdate.parse(leaveDate);
	}
	user.setLeaveDate(leaveD);*/

}
