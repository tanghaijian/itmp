package cn.pioneeruniverse.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.entity.TblProjectGroup;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.entity.User;
import cn.pioneeruniverse.project.service.newproject.NewProjectService;
import cn.pioneeruniverse.project.service.oamproject.OamProjectService;

/**
 * 
* @ClassName: OamProjectInfoController
* @Description: 运维类项目管理controller
* @author author
* @date 2020年8月7日 下午1:23:58
*
 */
@RestController
@RequestMapping("oamproject")
public class OamProjectInfoController extends BaseController{
	
	@Autowired
	private OamProjectService oamProjectService;

	@Autowired
	private RedisUtils redisUtils;


	@Autowired
	private NewProjectService newProjectService;

	/**
	 * 
	* @Title: selectStatusName
	* @Description: 运维项目状态下拉框
	* @author author
	* @param termCode 数据字典编码
	* @return java.util.Map<String, Object> key:data  value：List数据字典valuename组成的列表
	*                                           status  1正常返回，2异常返回
	 */
	@ResponseBody
	@RequestMapping(value="selectStatusName",method=RequestMethod.POST)
	public Map<String, Object> selectStatusName(@RequestParam("termCode")String termCode) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			ArrayList<String> list = new ArrayList<>();
			String redisStr = redisUtils.get(termCode).toString();
			JSONObject jsonObj = JSON.parseObject(redisStr);
//			for (String key : jsonObj.keySet()) {
//				String ValueName = jsonObj.get(key).toString();
//				list.add(ValueName);
//			}
			//valueCode为2的valueName
			String value1 = jsonObj.get("2").toString();
			//valueCode为4的valueName
			String value2 = jsonObj.get("4").toString();
			list.add(value1);
			list.add(value2);
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 
	* @Title: selectOamProject
	* @Description: 查询运维类项目
	* @author author
	* @param projectName 项目名称
	* @param projectStatusName 项目状态
	* @param projectManageName 项目管理岗
	* @param developManageName 开发管理岗
	* @param page 第几页
	* @param rows 每页条数
	* @param request
	* @return java.util.Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="selectOamProject",method=RequestMethod.POST)
	public Map<String, Object> selectOamProject(String projectName,String projectStatusName,
			String projectManageName,String developManageName,
			Integer page,Integer rows,HttpServletRequest request){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long uid  = CommonUtil.getCurrentUserId(request);
			LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) codeMap.get("roles");
			List<TblProjectInfo> list = oamProjectService.selectOamProject(projectName,projectStatusName,projectManageName,developManageName,uid,roleCodes,page,rows);
			List<TblProjectInfo> list2 = oamProjectService.selectOamProject(projectName,projectStatusName,projectManageName,developManageName,uid,roleCodes,1,Integer.MAX_VALUE);
            if (list2 != null && list2.size() > 0) {
               double total = Math.ceil(list2.size() * 1.0 / rows);
				map.put("total", total);//总页数
				map.put("records", list2.size()); //总记录数
			}else{
				map.put("total", 0);
				map.put("records", 0);
			}

			map.put("page", page);					//当前页
			          //总页数
			map.put("data", list);				//每页数据
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		
		return map;
		
	}
	

	/**
	 * 新增项目和编辑项目的关联系统页面的分页显示(查询的是未绑定和未选中的系统)
	 * @param tblSystemInfo 系统类对象
	 * @param pageNumber 第几页
	 * @param pageSize 每页条数
	 * @param systemIds 系统id集合
	 * @return java.util.Map<String, Object> status=1正常，2异常
	 */
	@ResponseBody
	@RequestMapping(value="selectSystemInfo",method=RequestMethod.POST)
	public Map<String, Object> selectSystemInfo(TblSystemInfo tblSystemInfo,
			@RequestParam(value="pageNumber",defaultValue = "1", required = true)Integer pageNumber,                                  
			@RequestParam(value="pageSize",defaultValue = "10",required = true)Integer pageSize,
			@RequestParam(value="systemIds[]") Long[] systemIds
			){
			HashMap<String, Object> map = new HashMap<>();
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblSystemInfo> list = oamProjectService.selectSystemInfo(tblSystemInfo,pageNumber,pageSize,systemIds);
			List<TblSystemInfo> list2 = oamProjectService.selectSystemInfo(tblSystemInfo,1,Integer.MAX_VALUE,systemIds);
			map.put("total", list2.size()); //总数量
			map.put("rows", list);//当前页的数据
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
		}
	
	/**
	 * 新增运维项目
	 * @param tblProjectInfo 项目类对象
	 * @return java.util.Map<String, Object>  status  1正常返回，2异常返回
	 */
	@RequestMapping(value="insertOamProject",method=RequestMethod.POST)
	public Map<String,Object> insertOamProject(String tblProjectInfo,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		try {
			Gson gson = new Gson();
			TblProjectInfo projectInfo = gson.fromJson(tblProjectInfo, TblProjectInfo.class);
			Long projectId = oamProjectService.insertOamProject(projectInfo,request);
			Integer projectType = 1;
			//更新系统目录
			newProjectService.updateSystemDirectory(projectId, request, projectType);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 结束项目
	 * @param id 项目id
	 * @return java.util.Map<String, Object>  status  1正常返回，2异常返回
	 */
	@RequestMapping(value="endProject",method=RequestMethod.POST)
	public Map<String, Object> endProject(Long id, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			oamProjectService.endProject(id,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 根据项目id查询项目详情and项目组成员（项目详情页面和编辑页面的数据回显使用同一个请求）
	 * @param id 项目id
	 * @param homeType 跳转页面方式
	 * @return java.util.Map<String, Object> key:data 项目信息
	 */
	@RequestMapping(value="selectProjectAndUserById",method=RequestMethod.POST)
	public Map<String, Object> selectProjectAndUserById(Long id,Integer homeType){
		HashMap<String,Object> map = new HashMap<>();
		if (homeType == 1){  //项目主页获者项目群主页过来
//			//基本信息
//			ProjectHomeDTO projectHome = projectHomeService.selectSystemInfoByID(homeId);
//			//项目关联系统
//			List<ProjectHomeDTO> interactedSystem = projectHomeService.projectSystemList(homeId);
//			logger.info("interactedSystem:"+interactedSystem);
//			map.put("projectHome",projectHome);
//			map.put("interactedSystem",interactedSystem);
			try {
				//查询项目详情及人员详情
				TblProjectInfo tblProjectInfo = oamProjectService.selectProject(id);
				map.put("data", tblProjectInfo);
				map.put("type", 1);//未用

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("mes:" + e.getMessage(), e);
			}
			logger.info("团队成员:"+map);
			return  map;
		}else{
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
			try {
				//查询项目详情及人员详情
				TblProjectInfo tblProjectInfo = oamProjectService.selectProject(id);
				map.put("data", tblProjectInfo);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("mes:" + e.getMessage(), e);
			}
			return map;
		}
	}
	
	/**
	 * 
	* @Title: selectUserPost
	* @Description: 项目岗位信息，存储于redis中
	* @author author
	* @return Map<String,Object> data项目岗位信息 
	 */
	@RequestMapping(value="selectUserPost",method=RequestMethod.POST)
	public Map<String, Object> selectUserPost(){
		HashMap<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			String redisStr = redisUtils.get("TBL_PROJECT_GROUP_USER_USER_POST").toString();
			JSONObject jsonObject = JSON.parseObject(redisStr);
			map.put("data", jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 编辑页面的关联人员的弹窗
	 * @param userName 用户姓名
	 * @param employeeNumber 用户名
	 * @param deptName 部门名称
	 * @param companyName 公司名称
	 * @param pageNumber 第几页
	 * @param pageSize 每页条数
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="selectUser",method=RequestMethod.POST)
	public Map<String, Object> selectUser(String userName,String employeeNumber,String deptName,String companyName,Integer pageNumber,Integer pageSize){
		HashMap<String, Object> map = new HashMap<>();
		map.put("statsu", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblUserInfo> list = oamProjectService.selectUser(userName,employeeNumber,deptName,companyName,pageNumber,pageSize);
			List<TblUserInfo> list2 = oamProjectService.selectUser(userName,employeeNumber,deptName,companyName,1,Integer.MAX_VALUE);
			map.put("total", list2.size());
			map.put("rows", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 搜索部门名称显示在关联人员的搜索下拉框
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="selectDeptName",method=RequestMethod.POST)
	public Map<String, Object> selectDeptName(){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<String> list = oamProjectService.selectDeptName();
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 查询公司名称显示在关联人员搜索的下拉框
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="selectCompanyName",method=RequestMethod.POST)
	public Map<String, Object> selectCompanyName(){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<String> list = oamProjectService.selectCompanyName();
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 编辑项目
	 * @param tblProjectInfo 项目对象
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="editProject",method=RequestMethod.POST)
	public Map<String, Object> editProject(String tblProjectInfo,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			Gson gson = new Gson();
			TblProjectInfo projectInfo = gson.fromJson(tblProjectInfo, TblProjectInfo.class);
			oamProjectService.editProject(projectInfo,request);
			Integer projectType = 1;
			newProjectService.updateSystemDirectory(projectInfo.getId(), request, projectType);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 维护项目组织的项目小组数据显示(根据项目id查询该项目对应的项目小组《上下级》)
	 * @param id 项目id
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="selectProjectGroup",method=RequestMethod.POST)
	public Map<String, Object> selectProjectGroup(Long id){
		HashMap<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblProjectGroup> list = oamProjectService.selectProjectGroup(id);
			int size = list.size();
			map.put("records", size);
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 根据人员弹窗选中的人员的id，去查询封装显示在编辑页面
	 * @param ids 用户id集合
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="selectUsers",method=RequestMethod.POST)
	public Map<String, Object> selectUsers(Long[] ids){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<User> list = oamProjectService.selectUsers(ids);
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 
	* @Title: deletePeojectGroup
	* @Description: 删除项目小组
	* @author author
	* @param projectGroupId 项目小组id
	* @param request
	* @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="deletePeojectGroup",method=RequestMethod.POST)
	public Map<String, Object> deletePeojectGroup(Long projectGroupId,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			oamProjectService.deletePeojectGroup(projectGroupId,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 维护项目组织的编辑项目组
	 * @param tblProjectGroup 项目小组对象
	 * @param request
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="editProjectGroup",method=RequestMethod.POST)
	public Map<String, Object> editProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			oamProjectService.editProjectGroup(tblProjectGroup,request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	/**
	 * 维护项目组织的新增项目组
	 * @param tblProjectGroup 项目小组对象
	 * @param request
	 * @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="saveProjectGroup",method=RequestMethod.POST)
	public Map<String, Object> saveProjectGroup(TblProjectGroup tblProjectGroup, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		try {
			oamProjectService.saveProjectGroup(tblProjectGroup, request);
			map.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			map = super.handleException(e, e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 
	* @Title: selectOamProject2
	* @Description: 查询运维类项目
	* @author author
	* @param projectName :项目名称
	* @param projectStatusName：项目状态名
	* @param projectManageName：项目管理岗
	* @param developManageName：开发管理岗
	* @param page 第几页
	* @param rows 每页条数
	* @return java.util.Map<String, Object>
	 */
	@RequestMapping(value="selectOamProject2",method=RequestMethod.POST)
	public Map<String, Object> selectOamProject2(String projectName,String projectStatusName,
			String projectManageName,String developManageName,
			Integer page,Integer rows){
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblProjectInfo> list = oamProjectService.selectOamProject(projectName,projectStatusName,projectManageName,developManageName,null,null,page,rows);
			List<TblProjectInfo> list2 = oamProjectService.selectOamProject(projectName,projectStatusName,projectManageName,developManageName,null,null,1,Integer.MAX_VALUE);
			map.put("total", list2.size());    //查询的总条目数
			map.put("rows", list);				//每页数据
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("mes:" + e.getMessage(), e);
		}
		
		return map;
		
	}
	 
    /**
     * 
    * @Title: getProjectUserPost
    * @Description: 根据项目ID和岗位ID获取岗位角色信息
    * @author author
    * @param projectId 项目id
    * @param userPostIds 岗位id
    * @return map key userPosts 返回的岗位角色信息
    *                 status 1正常返回，2异常返回
     */
    @RequestMapping(value = "getProjectUserPost",method = RequestMethod.POST)
    public Map<String, Object> getProjectUserPost(Long  projectId,String userPostIds){
    	Map<String,Object> map = new HashMap<>();
    	try {
    		List<Map<String,Object>> list = oamProjectService.getProjectUserPost(projectId,userPostIds);
    		map.put("userPosts", list);
    		map.put("status",Constants.ITMP_RETURN_SUCCESS);
    	}catch (Exception e) {
           return super.handleException(e, "查询项目岗位失败！");
        }
    	return map;
    }

	
}
