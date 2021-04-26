package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.constants.DicConstants;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *@author liushan
 *@Description  风险管理
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_risk_info")
public class TblRiskInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long projectId;   // 项目表主键

	@JsonSerialize(using = ToStringSerializer.class)
	private Long programId;   // 项目群主键

	@JsonSerialize(using = ToStringSerializer.class)
	private Long responsibleUserId;    //责任方

	@PropertyInfo(name = "风险描述")
	private String riskDescription;//风险描述
	
	private Integer riskType;    //风险分类(数据字典)
	
	private Integer riskFactor;    //危险度(数据字典)
	
	private Integer riskProbability;    //发生概率(数据字典)
	
	private Integer riskPriority;    //风险优先级(数据字典)
	
	private String riskInfluence;    //风险影响
	
	private String riskTriggers;    //触发条件
	
	private Integer riskStatus;    //风险状态(数据字典)

	@PropertyInfo(name = "备注")
	private String remark;    //备注
	
	private String copingStrategy;    //应对措施
	
	private String copingStrategyRecord;    //应对措施执行记录

	@JsonSerialize(using = ToStringSerializer.class)
	private Long requirementFeatureId; // 测试任务

	@JsonSerialize(using = ToStringSerializer.class)
	private Long commissioningWindowId;// 投产窗口

	@JsonSerialize(using = ToStringSerializer.class)
	private Long requirementId;// 需求

	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemId;// 系统

	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;// 部门

	@PropertyInfo(name = "环境",dicTermCode = DicConstants.RISK_ENVIRONMENT_TYPE)
	private Integer environmentType;

	@Transient
	@PropertyInfo(name = "测试任务名称")
	private String requirementFeatureName;

	@PropertyInfo(name = "投产窗口")
	private String commissioningWindowName;

	@Transient
	@PropertyInfo(name = "需求编号")
	private String requirementCode;

	@Transient
	@PropertyInfo(name = "系统名称")
	private String systemName;

	@Transient
	@PropertyInfo(name = "所属处室")
	private String deptName;

	@Transient
	private String responsibleUser;    // 责任人
	
	@Transient
	private String riskPriorityName;    //优先级
	
	@Transient
	private String statusName;    //风险状态
	
	@Transient
	private String riskTypeName;    //风险分类名称
	
	@Transient
	private String riskFactorName;   //危险度名称
	
	@Transient
	private String riskProbabilityName;  //发生概率名称

	@Transient
	private String projectName;  //项目名称

	@Transient
	private Long number;  //编号(排序号)

	@Transient
	private String groupUser;  // 项目的人员

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getResponsibleUserId() {
		return responsibleUserId;
	}

	public void setResponsibleUserId(Long responsibleUserId) {
		this.responsibleUserId = responsibleUserId;
	}

	public String getRiskDescription() {
		return riskDescription;
	}

	public void setRiskDescription(String riskDescription) {
		this.riskDescription = riskDescription;
	}

	public Integer getRiskType() {
		return riskType;
	}

	public void setRiskType(Integer riskType) {
		this.riskType = riskType;
	}

	public Integer getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(Integer riskFactor) {
		this.riskFactor = riskFactor;
	}

	public Integer getRiskProbability() {
		return riskProbability;
	}

	public void setRiskProbability(Integer riskProbability) {
		this.riskProbability = riskProbability;
	}

	public Integer getRiskPriority() {
		return riskPriority;
	}

	public void setRiskPriority(Integer riskPriority) {
		this.riskPriority = riskPriority;
	}

	public String getRiskInfluence() {
		return riskInfluence;
	}

	public void setRiskInfluence(String riskInfluence) {
		this.riskInfluence = riskInfluence;
	}

	public String getRiskTriggers() {
		return riskTriggers;
	}

	public void setRiskTriggers(String riskTriggers) {
		this.riskTriggers = riskTriggers;
	}

	public Integer getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(Integer riskStatus) {
		this.riskStatus = riskStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCopingStrategy() {
		return copingStrategy;
	}

	public void setCopingStrategy(String copingStrategy) {
		this.copingStrategy = copingStrategy;
	}

	public String getCopingStrategyRecord() {
		return copingStrategyRecord;
	}

	public void setCopingStrategyRecord(String copingStrategyRecord) {
		this.copingStrategyRecord = copingStrategyRecord;
	}

	public Long getRequirementFeatureId() {
		return requirementFeatureId;
	}

	public void setRequirementFeatureId(Long requirementFeatureId) {
		this.requirementFeatureId = requirementFeatureId;
	}

	public Long getCommissioningWindowId() {
		return commissioningWindowId;
	}

	public void setCommissioningWindowId(Long commissioningWindowId) {
		this.commissioningWindowId = commissioningWindowId;
	}

	public Long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Integer getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}

	public String getRequirementFeatureName() {
		return requirementFeatureName;
	}

	public void setRequirementFeatureName(String requirementFeatureName) {
		this.requirementFeatureName = requirementFeatureName;
	}

	public String getCommissioningWindowName() {
		return commissioningWindowName;
	}

	public void setCommissioningWindowName(String commissioningWindowName) {
		this.commissioningWindowName = commissioningWindowName;
	}

	public String getRequirementCode() {
		return requirementCode;
	}

	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(String responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getRiskPriorityName() {
		return riskPriorityName;
	}

	public void setRiskPriorityName(String riskPriorityName) {
		this.riskPriorityName = riskPriorityName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getRiskTypeName() {
		return riskTypeName;
	}

	public void setRiskTypeName(String riskTypeName) {
		this.riskTypeName = riskTypeName;
	}

	public String getRiskFactorName() {
		return riskFactorName;
	}

	public void setRiskFactorName(String riskFactorName) {
		this.riskFactorName = riskFactorName;
	}

	public String getRiskProbabilityName() {
		return riskProbabilityName;
	}

	public void setRiskProbabilityName(String riskProbabilityName) {
		this.riskProbabilityName = riskProbabilityName;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getGroupUser() {
		return groupUser;
	}

	public void setGroupUser(String groupUser) {
		this.groupUser = groupUser;
	}
}
