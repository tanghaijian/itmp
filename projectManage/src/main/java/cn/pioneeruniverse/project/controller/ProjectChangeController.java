package cn.pioneeruniverse.project.controller;

import java.util.HashMap;
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
import cn.pioneeruniverse.project.entity.TblProjectChangeInfo;
import cn.pioneeruniverse.project.entity.TblProjectChangeLog;
// import cn.pioneeruniverse.project.entity.TblQuestionLog;
import cn.pioneeruniverse.project.service.projectChange.ProjectChangeService;
/**
 *
 * @ClassName: ProjectChangeController
 * @Description: 项目变更Controller
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
@RestController
@RequestMapping("change")
public class ProjectChangeController extends BaseController {

	@Autowired
	private ProjectChangeService changeService;
	
	/**
	 * 变更列表
	 * @param projectId 项目ID
	 * @param request
	 * @return Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="getChanges",method=RequestMethod.POST)
	public Map<String,Object> getChanges(Long projectId, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblProjectChangeInfo> list = changeService.getChanges(projectId, request);
			map.put("data", list);//项目变更信息列表
		} catch (Exception e) {
			return super.handleException(e, "变更列表查询失败！");
		}
		return map;
	}
	
	
	/**
	 * 
	 * 删除变更
	 * @param id 项目变更ID
	 * @param request
	 * @return Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="deleteProjectChange",method=RequestMethod.POST)
	public Map<String,Object> deleteProjectChange(Long id, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			changeService.deleteProjectChange(id, request);
		} catch (Exception e) {
			return super.handleException(e, "删除变更失败！");
		}
		return map;
	}
	
	
	/**
	 * 新增变更
	 * @param projectChange 项目变更信息
	 * @param request
	 * @return Map<String, Object> key:status=1正常，2异常
	 */
	@RequestMapping(value="insertProjectChange",method=RequestMethod.POST)
	public Map<String,Object> insertProjectChange(@RequestBody String projectChange, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProjectChangeInfo tblProjectChangeInfo = JSONObject.parseObject(projectChange, TblProjectChangeInfo.class);
			changeService.insertProjectChange(tblProjectChangeInfo, request);
		} catch (Exception e) {
			return super.handleException(e, "新增变更失败！");
		}
		return map;
	}
	
	
	/**
	 * 变更详情
	 * @param id 项目变更ID
	 * @return Map<String, Object> key:status=1正常，2异常
	 */
	@RequestMapping(value="getProjectChangeById",method=RequestMethod.POST)
	public Map<String,Object> getProjectChangeById(Long id){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProjectChangeInfo tblProjectChangeInfo = changeService.getProjectChangeById(id);
			//获取变更日志
			List<TblProjectChangeLog> logs = changeService.getChangeLog(id);
			map.put("data", tblProjectChangeInfo);//变更信息
			map.put("logs", logs);//日志
		} catch (Exception e) {
			return super.handleException(e, "获取变更详情失败！");
		}
		return map;
	}
	
	
	/**
	 * 编辑变更记录
	 * @param projectChangeInfo 项目变更信息
	 * @param request
	 * @return Map<String, Object> key:status=1正常，2异常
	 */
	@RequestMapping(value="updateProjectChange",method=RequestMethod.POST)
	public Map<String,Object> updateProjectChange(@RequestBody String projectChangeInfo, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblProjectChangeInfo tblProjectChangeInfo = JSONObject.parseObject(projectChangeInfo, TblProjectChangeInfo.class);
			changeService.updateProjectChange(tblProjectChangeInfo, request);
		} catch (Exception e) {
			return super.handleException(e, "编辑变更记录失败！");
		}
		return map;
	}
	
	
	/**
	 * 项目群管理变更列表
	 * @param programId 项目群ID
	 * @param request
	 * @return Map<String, Object> key:status=1正常，2异常
	 */
	@RequestMapping(value="getChangesByProgram",method=RequestMethod.POST)
	public Map<String,Object> getChangesByProgram(Long programId, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblProjectChangeInfo> list = changeService.getChangesByProgram(programId, request);
			//项目变更列表
			map.put("data", list);
		} catch (Exception e) {
			return super.handleException(e, "获取变更列表失败！");
		}
		return map;
	}
}
