package cn.pioneeruniverse.project.service.risk;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.project.entity.TblRiskAttachement;
import cn.pioneeruniverse.project.entity.TblRiskInfo;
import cn.pioneeruniverse.project.entity.TblRiskLog;
import org.springframework.web.multipart.MultipartFile;

public interface RiskService {

	/**
	 *@author liushan
	 *@Description
	 *@Date 2020/7/29
	 *@Param [riskInfo 风险实体类接收参数, projectType 项目类型标识, page, pageSzie, request]
	 *@return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	Map<String,Object> getRiskInfo(TblRiskInfo riskInfo, Integer projectType, Integer page, Integer pageSzie, HttpServletRequest request);
	/**
	 *@author liushan
	 *@Description 新增风险
	 *@Date 2020/7/31
	 * @param tblRiskInfo 风险数据
	 * @param files 附件
	 * @param request
	 *@return void
	 **/
	void insertRiskInfo(TblRiskInfo tblRiskInfo, MultipartFile[] files, HttpServletRequest request) throws Exception;
	/**
	 *@author liushan
	 *@Description 删除风险
	 *@Date 2020/7/31
	 * @param id
	 * @param request
	 *@return void
	 **/
	void deleteRiskInfo(Long id, HttpServletRequest request);
	/**
	 *@author liushan
	 *@Description 获取详情
	 *@Date 2020/7/31
	 * @param id
	 *@return cn.pioneeruniverse.project.entity.TblRiskInfo
	 **/
	TblRiskInfo getRiskInfoById(Long id);
	/**
	 *@author liushan
	 *@Description 获取风险数据
	 *@Date 2020/7/31
	 * @param id
	 *@return cn.pioneeruniverse.project.entity.TblRiskInfo
	 **/
	TblRiskInfo getRiskById(Long id);

	/**
	 *@author liushan
	 *@Description 获取风险日志
	 *@Date 2020/7/31
	 * @param id
	 *@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskLog>
	 **/
	List<TblRiskLog> getRiskLog(Long id);
	/**
	 *@author liushan
	 *@Description 获取风险附件
	 *@Date 2020/4/28
	 *@Param [id]
	 *@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskAttachement>
	 **/
	List<TblRiskAttachement> getRiskAttachement(Long id);
	/**
	 *@author liushan
	 *@Description 编辑风险
	 *@Date 2020/7/31
	 * @param tblRiskInfo
	 * @param projectType
	 * @param files
	 * @param removeFileIds
	 * @param request
	 *@return void
	 **/
	void updateRisk(TblRiskInfo tblRiskInfo, Integer projectType, MultipartFile[] files, String removeFileIds, HttpServletRequest request) throws Exception;
	/**
	 *@author liushan
	 *@Description 项目群管理风险列表
	 *@Date 2020/7/31
	 * @param programId 运维类项目
	 * @param request
	 *@return java.util.List<cn.pioneeruniverse.project.entity.TblRiskInfo>
	 **/
	List<TblRiskInfo> getRiskInfoByProgram(Long programId, HttpServletRequest request);
	/**
	 *@author liushan
	 *@Description 获取开发任务名称
	 *@Date 2020/7/31
	 * @param requirementFeatureId
	 *@return java.lang.String
	 **/
	String getRequirementFeatureName(Long requirementFeatureId);
}
