package cn.pioneeruniverse.project.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.entity.TblProgramInfo;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.service.newproject.NewProjectService;

/**
 * Description: 新建类项目controller
 * Author:
 * Date: 2020/08/13 下午 1:41
 */
@RestController
@RequestMapping("newProject")
public class NewProjectController extends BaseController {

	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private NewProjectService newProjectService;
	
	/**
	 * 新建项目列表查询
	 * @param projectInfo 项目类对象
	 * @param request
	 * @return java.util.Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="getAllNewProject",method=RequestMethod.POST)
	public Map<String,Object> getAllNewProject(TblProjectInfo projectInfo, Integer page,Integer rows, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			Long uid  = CommonUtil.getCurrentUserId(request);
			LinkedHashMap codeMap = (LinkedHashMap) redisUtils.get(CommonUtil.getToken(request));
			List<String> roleCodes = (List<String>) codeMap.get("roles");
			List<TblProjectInfo> list = newProjectService.getAllNewProject(projectInfo, uid, roleCodes, page, rows);
			List<TblProjectInfo> list2 = newProjectService.getAllNewProject(projectInfo, uid, roleCodes, 1, Integer.MAX_VALUE);
			double total = Math.ceil(list2.size()*1.0/rows);
			//查询的总条目数
			map.put("records", list2.size());    
			//当前页
			map.put("page", page);	
			//总页数
			map.put("total", total); 
			//每页数据
			map.put("data", list);				
		} catch (Exception e) {
			return super.handleException(e, "新建项目列表查询失败！");
		}
		return map;
	}
	
	
	/**
	 * 详情/数据回显
	 * @param id 项目id
	 * @return java.util.Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="getNewProjectById",method=RequestMethod.POST)
	public Map<String,Object> getNewProjectById(Long id){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProjectInfo projectInfo = newProjectService.getNewProjectById(id);
			//项目信息
			map.put("data", projectInfo);
		} catch (Exception e) {
			return super.handleException(e, "获取项目详情失败！");
		}
		return map;
	}
	
	/**
	 * 新增新建项目
	 * @param tblProjectInfo 项目类对象
	 * @param request
	 * @return java.util.Map<String,Object> key:status  value :1正常返回，2异常返回
	 */
	@RequestMapping(value="insertNewProject",method=RequestMethod.POST)
	public Map<String,Object> insertNewProject(@RequestBody String tblProjectInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProjectInfo projectInfo = JSONObject.parseObject(tblProjectInfo, TblProjectInfo.class);
			Long projectId = newProjectService.insertNewProject(projectInfo, request);
			//新建类
			Integer projectType = 2;
			newProjectService.updateSystemDirectory(projectId, request, projectType);
		} catch (Exception e) {
			return super.handleException(e, "新增新建项目失败！");
		}
		return map;
	}
	
	/**
	 * 新增或编辑新建项目时根据开发系统更新系统目录
	 * @param projectId 项目id
	 * @param projectType 项目类型
	 * @return java.util.Map<String,Object>key:status  value :1正常返回，2异常返回
	 */
	@RequestMapping(value="updateSystemDirectory",method=RequestMethod.POST)
	public Map<String,Object> updateSystemDirectory(Long projectId, HttpServletRequest request, Integer projectType) {
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			newProjectService.updateSystemDirectory(projectId, request, projectType);
		} catch (Exception e) {
			return super.handleException(e, "更新目录失败！");
		}
		return map;
	}


	/**
	 * 新增项目群
	 * @param tblProgramInfo 项目类对象
	 * @param request
	 * @return java.util.Map<String,Object> key:status  value :1正常返回，2异常返回
	 */
	@RequestMapping(value="insertProgram",method=RequestMethod.POST)
	public Map<String,Object> insertProgram(@RequestBody String tblProgramInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProgramInfo programInfo = JSONObject.parseObject(tblProgramInfo, TblProgramInfo.class);
			newProjectService.insertProgram(programInfo, request);
		} catch (Exception e) {
			return super.handleException(e, "新增项目群失败！");
		}
		return map;
	}

	
	/**
	 * 新建项目群关联新建项目弹窗
	 * @param projectSearchInfo 项目类对象
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return java.util.Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="getNewProjectByPage",method=RequestMethod.POST)
	public Map<String,Object> getNewProjectByPage(TblProjectInfo projectSearchInfo,Integer pageNumber,Integer pageSize){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblProjectInfo> list = newProjectService.getNewProjectByPage(projectSearchInfo,  pageNumber, pageSize);
			List<TblProjectInfo> list2 = newProjectService.getNewProjectByPage(projectSearchInfo, 1, Integer.MAX_VALUE);
			//总数
			map.put("total", list2.size());
			//数据
			map.put("rows", list);
		} catch (Exception e) {
			return super.handleException(e, "获取新建项目失败！");
		}
		return map;
	}
	
	
	/**
	 * 编辑新建项目
	 * @param tblProjectInfo 项目类对象
	 * @param request
	 * @return java.util.Map<String,Object> key:status  value :1正常返回，2异常返回
	 */
	@RequestMapping(value="updateNewProject",method=RequestMethod.POST)
	public Map<String,Object> updateNewProject(@RequestBody String tblProjectInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProjectInfo projectInfo = JSONObject.parseObject(tblProjectInfo, TblProjectInfo.class);
			newProjectService.updateNewProject(projectInfo, request);
			Integer projectType = 2;
			updateSystemDirectory(projectInfo.getId(), request, projectType);
		} catch (Exception e) {
			return super.handleException(e, "编辑新建项目失败！");
		}
		return map;
	}
	
	
}
