package cn.pioneeruniverse.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblRiskAttachement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblRiskInfo;
import cn.pioneeruniverse.project.entity.TblRiskLog;
import cn.pioneeruniverse.project.service.risk.RiskService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("risk")
public class RiskController extends BaseController {
	
	@Autowired
	private RiskService riskService;

	
	/**
	 * 风险列表
	 * @param riskInfo 封装的查询条件
	 * @param request
	 * @param projectType 项目类型标识1运维类，2新建类
	 * @return map  key:data value:List<TblRiskInfo>风险信息列表
	 */
	@RequestMapping(value="getRiskInfo",method=RequestMethod.POST)
	public Map<String, Object> getRiskInfo(TblRiskInfo riskInfo,Integer projectType,
										   @RequestParam(value = "page", required = false)Integer page,
										   @RequestParam(value = "pageSzie", required = false)Integer pageSzie,
										   HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			map = riskService.getRiskInfo(riskInfo,projectType,page,pageSzie,request);

		} catch (Exception e) {
            map =  super.handleException(e, "风险列表查询失败！");
		}
		return map;
	}
	
	
	/**
	 * 
	* @Title: insertRiskInfo
	* @Description: 新增风险信息
	* @author author
	* @param riskInfo 风险信息
	* @param files 附件信息
	* @param request
	* @return Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="insertRiskInfo",method=RequestMethod.POST)
	public Map<String, Object> insertRiskInfo(@RequestParam("riskInfo") String riskInfo,
											  @RequestParam(value = "files",required = false) MultipartFile[] files,
											  HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblRiskInfo tblRiskInfo = JSONObject.parseObject(riskInfo, TblRiskInfo.class);
			riskService.insertRiskInfo(tblRiskInfo, files,request);
		} catch (Exception e) {
			return super.handleException(e, "新增风险失败！");
		}
		return map;
	}

	/**
	 * 
	* @Title: updateRisk
	* @Description: 更新风险信息
	* @author author
	* @param riskInfo 风险信息
	* @param projectType 项目类型1运维，2新建
	* @param files 附件
	* @param removeFileIds 需要移除的附件ID
	* @param request
	* @return Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="updateRisk",method=RequestMethod.POST)
	public Map<String, Object> updateRisk(@RequestParam("riskInfo") String riskInfo,
										  @RequestParam("projectType") Integer projectType,
										  @RequestParam(value = "files",required = false) MultipartFile[] files,
										  @RequestParam(value = "removeFileIds",required = false) String removeFileIds,
										  HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblRiskInfo tblRiskInfo = JSONObject.parseObject(riskInfo, TblRiskInfo.class);
			riskService.updateRisk(tblRiskInfo, projectType,files,removeFileIds,request);
		} catch (Exception e) {
			return super.handleException(e, "风险编辑失败！");
		}
		return map;
	}
	
	
	/**
	 * 删除风险
	 * @param id 风险ID
	 * @param request
	 * @return Map<String, Object> status=1正常，2异常
	 */
	@RequestMapping(value="deleteRiskInfo",method=RequestMethod.POST)
	public Map<String, Object> deleteRiskInfo(Long id, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			riskService.deleteRiskInfo(id, request);
		} catch (Exception e) {
			return super.handleException(e, "删除风险失败！");
		}
		return map;
	}
	
	
	/**
	 * 
	* @Title: getRiskInfoById
	* @Description: 获取风险信息详情
	* @author author
	* @param id 风险ID
	* @param projectType 项目类型1运维，2自建
	* @return map key:data value:TblRiskInfo 风险信息
	*             key:logs value:List<TblRiskLog>风险日志
	 */
	@RequestMapping(value="getRiskInfoById",method=RequestMethod.POST)
	public Map<String, Object> getRiskInfoById(Long id,Integer projectType){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			TblRiskInfo tblRiskInfo = new TblRiskInfo();

			if(projectType.intValue() == 1){
                // 运维类项目进入
				tblRiskInfo = riskService.getRiskById(id);
				// 附件
				List<TblRiskAttachement> attachements = riskService.getRiskAttachement(id);
				map.put("attachements", attachements);
			} else if(projectType.intValue() == 2){
                // 新建类项目进入
				tblRiskInfo = riskService.getRiskInfoById(id);
                //风险日志
                List<TblRiskLog> logs = riskService.getRiskLog(id);
                map.put("logs", logs);
			}
			map.put("data", tblRiskInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return super.handleException(e, "风险详情获取失败！");
		}
		return map;	
	}
	
	
	/**
	 * 项目群管理风险列表
	 * @param programId
	 * @param request
	 * @return map key:data value:List<TblRiskInfo>风险列表
	 */
	@RequestMapping(value="getRiskInfoByProgram",method=RequestMethod.POST)
	public Map<String,Object> getRiskInfoByProgram(Long programId, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", Constants.ITMP_RETURN_SUCCESS);
		try {
			List<TblRiskInfo> list = riskService.getRiskInfoByProgram(programId, request);
			map.put("data", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return super.handleException(e, "获取风险列表失败！");
		}
		return map;
	}
	
	
	
	
}
