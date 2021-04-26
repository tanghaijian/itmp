package cn.pioneeruniverse.dev.service.testReport;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.vo.TestReportParamVo;

public interface ITestReport {
	
	public Map<String, Object> getChartData(String time,String systemIdStr,HttpServletRequest request);
	
	public void export1(TestReportParamVo testReportParamVo,HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	public Map<String, Object> queryReport(String time);
	
	public Map<String, Object> queryHis(String time);
	
	public Map<String, Object> queryHisByTime(String time);
	
	public void addHis(String allDevVersionTableData,String allSystemTableData,HttpServletRequest request);
	
	public List<TblSystemInfo> getAllSystem();
	/**
	 * 获取系统缺陷数统计
	 * @param time
	 * @param systemIdStr
	 * @param request
	 * @return
	 */
	public Map<String, Object> getDefectProData(String time,String systemIdStr,HttpServletRequest request);
	/**
	 * 软件开发版本情况(运维期)
	 * @param time
	 * @param request
	 * @return
	 */
	public Map<String, Object> getDevVersionReport(String time,HttpServletRequest request);
	/**
	 * 质量较差系统缺陷统计
	 * @param time
	 * @param request
	 * @return
	 */
	public Map<String, Object> getWorseSystemReport(String time,HttpServletRequest request);
	/**
	 * 缺陷率整体趋势统计
	 * @param time
	 * @param request
	 * @return
	 */
	public Map<String, Object> getDefectTotalReport(String time,HttpServletRequest request);
	/**
	 * 缺陷等级分布
	 * @param time
	 * @param request
	 * @return
	 */
	public Map<String, Object> getDefectLevelReport(String time,HttpServletRequest request);
	
	/**
	 * 获取新建类和运维期系统
	 * @return
	 */
	public Map<String, Object> getOamAndNewSystem();
	
	/**
	 * 获取所有系统
	 * @param systemName
	 * @param systemCode
	 * @param type
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	public Map<String, Object> getSystemInfo(String systemName,String systemCode,Integer pageNumber,
			Integer pageSize,HttpServletRequest request);

	/**
	 *获取运维期或者项目期下的非敏捷系统
	 * @param systemName
	 * @param systemCode
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	public Map<String, Object> getSystemInfoByAgile(String systemName,String systemCode,Integer pageNumber,
											 Integer pageSize,HttpServletRequest request);

	/**
	 * 添加系统类型
	 * @param systemIdStr
	 * @param request
	 * @return
	 */
	public void addSystemClass(String systemIdStr,Integer systemClass,HttpServletRequest request);
	
	
}
