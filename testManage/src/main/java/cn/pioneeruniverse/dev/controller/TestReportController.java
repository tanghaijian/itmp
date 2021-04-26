package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.TblTestTask;
import cn.pioneeruniverse.dev.service.testReport.ITestReport;
import cn.pioneeruniverse.dev.vo.TestReportParamVo;

@RestController
@RequestMapping("testReport")
public class TestReportController extends BaseController{

	@Autowired
	private ITestReport testReportService;
	
	/**
	 * 查询报表
	 * @param time
	 * @return
	 */
	@RequestMapping("queryReport")
	public Map<String, Object> queryReport(String time){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.putAll(testReportService.queryReport(time));
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 根据时间查询历史表
	 * @param time
	 * @return
	 */
	@RequestMapping("queryHisByTime")
	public Map<String, Object> queryHisByTime(String time){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.putAll(testReportService.queryHisByTime(time));
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询是否有历史记录
	 * @param time
	 * @return
	 */
	@RequestMapping("queryHis")
	public Map<String, Object> queryHis(String time){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.putAll(testReportService.queryHis(time));
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 插入历史
	 * @param time
	 * @return
	 */
	@RequestMapping("insertHis")
	public Map<String, Object> insertHis(String allDevVersionTableData,String allSystemTableData,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			testReportService.addHis(allDevVersionTableData, allSystemTableData, request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 导出
	 * @param startDate
	 * @param endDate
	 * @param systemIdStr
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("exportReport")
	public Map<String, Object> exportReport(TestReportParamVo testReportParamVo,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			testReportService.export1(testReportParamVo, request, response);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("getChartData")
	public Map<String, Object> getChartData(String time,String systemIdStr,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = testReportService.getChartData(time, systemIdStr,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取所有系统(oam、new)
	 * @return
	 */
	@RequestMapping("getAllSystem")
	public Map<String, Object> getAllSystem(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("systemList", testReportService.getAllSystem());
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	/**
	 * 获取缺陷率统计
	 * @param time
	 * @param systemIdStr
	 * @param request
	 * @return
	 */
	@RequestMapping("getDefectProData")
	public Map<String, Object> getDefectProData(String time,String systemIdStr,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = testReportService.getDefectProData(time, systemIdStr,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	/**
	 * 获取开发版本获取情况
	 * @param time
	 * @param request
	 * @return
	 */
	@RequestMapping("getDevVersionReport")
	public Map<String, Object> getDevVersionReport(String time,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = testReportService.getDevVersionReport(time,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	/**
	 * 最差项目统计
	 * @param time
	 * @param request
	 * @return
	 */
	@RequestMapping("getWorseSystemReport")
	public Map<String, Object> getWorseSystemReport(String time,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = testReportService.getWorseSystemReport(time,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	/**
	 * 获取全年缺陷率统计
	 * @param time
	 * @param request
	 * @return
	 */
	@RequestMapping("getDefectTotalReport")
	public Map<String, Object> getDefectTotalReport(String time,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = testReportService.getDefectTotalReport(time,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取缺陷等级统计
	 * @param time
	 * @param request
	 * @return
	 */
	@RequestMapping("getDefectLevelReport")
	public Map<String, Object> getDefectLevelReport(String time,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = testReportService.getDefectLevelReport(time,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取新建类和运维期系统
	 * @return
	 */
	@RequestMapping("getOamAndNewSystem")
	public Map<String, Object> getOamAndNewSystem(){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.putAll(testReportService.getOamAndNewSystem());
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取所有系统
	 * @param systemName
	 * @param systemCode
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("selectAllSystemInfo")
	public Map<String, Object> selectAllSystemInfo(String systemName,String systemCode,Integer pageNumber,
			Integer pageSize,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.putAll(testReportService.getSystemInfo(systemName,systemCode,pageNumber,pageSize,request));
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}

	/**
	 * 获取运维期或者项目期下的非敏捷系统
	 * @param systemName
	 * @param systemCode
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("selectAllSystemInfoByAgile")
	public Map<String, Object> selectAllSystemInfoByAgile(String systemName,String systemCode,Integer pageNumber,
												   Integer pageSize,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.putAll(testReportService.getSystemInfoByAgile(systemName,systemCode,pageNumber,pageSize,request));
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 添加系统类型
	 * @param systemIdStr
	 * @param systemClass
	 * @param request
	 * @return
	 */
	@RequestMapping("addSystemClass")
	public Map<String, Object> addSystemClass(String systemIdStr,Integer systemClass,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			testReportService.addSystemClass(systemIdStr,systemClass,request);
			result.put("status", Constants.ITMP_RETURN_SUCCESS);
		} catch (Exception e) {
			result = super.handleException(e, e.getMessage());
		}
		return result;
	}
	
}
