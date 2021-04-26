package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.Date;

public class DevDetailVo  extends BaseEntity {
	
	 private String devTaskName;//开发工作任务名称
	    
	 private String devTaskCode;//开发工作任务编码
	 //描述
	 private String devTaskOverview; 
	 //任务状态
	 private Integer devTaskStatus;
	 private String DevStatus;
	 private Long devuserID;
	 //开发人员名字
	 private String devuserName;
	 //创建姓名
	 private String createName;
	 //计划开始日期
	 private Date planStartDate;
	 //计划结束日期
	 private Date planEndDate;
	 //预估工作量（人天）
	 private double planWorkload;
	 //预估剩余工作量（人天）
	 private double estimateRemainWorkload;
	 //实际开始日期
	 private Date actualStartDate;
	 //实际结束日期
	 private Date actualEndDate;
	 //实际工作量（人天）
	 private double actualWorkload;
	 //关联任务名称
	 private String featureName;
	 //关联任务描述
	 private String featureOverview;
	 //关联任务状态
	 private String requirementFeatureStatus;
	 //管理岗位
	 private Long manageUserId;
	 //管理名称
	 private String manageUserName;
	 //执行人
	 private Long executeUserId;
	 private String executeUserName;
	 //系统名称
	 private Long systemId;
	 private String systemName;
	 
	 private String windowName;//投产窗口名称
	 
	 private String developmentMode;//开发模式（1:敏态，2:稳态）
	 //冲刺名称
	 private String sprintName;
	 //开发任务Code
	 private String featureCode;
	 //需求来源
	 private String requirementSource;
	 //需求类型
	 private String requirementType;
	 
	 private String requirementOverview;//需求描述
	 //优先级
	 private String requirementPriority;
	 //需求计划
	 private String requirementPanl;
	 //需求状态
	 private String requirementStatus;
	//需求名称
	 private String requirementName;
	 //提出人
	 private Long applyUserId;
	 //提出人名称
	 private String applyUserName;
	 //提出人部门
	 private Long applyDeptId;
	 private String applyDeptName;
	 //期望上线时间
	 private Date expectOnlineDate;
	 //计划上线
	 private Date planOnlineDate;	
	 //开启日期
	 private Date openDate;
	 private Date createDate3;
	 private Date lastUpdateDate3;
	 //优先级
	 private Integer devTaskPriority;
	 // 任务类型
	 private Integer devTaskType;


	 //任务you
	public Date getCreateDate3() {
		return createDate3;
	}
	public void setCreateDate3(Date createDate3) {
		this.createDate3 = createDate3;
	}
	public Date getLastUpdateDate3() {
		return lastUpdateDate3;
	}
	public void setLastUpdateDate3(Date lastUpdateDate3) {
		this.lastUpdateDate3 = lastUpdateDate3;
	}
	public String getDevTaskName() {
		return devTaskName;
	}
	public void setDevTaskName(String devTaskName) {
		this.devTaskName = devTaskName;
	}
	public String getDevTaskCode() {
		return devTaskCode;
	}
	public void setDevTaskCode(String devTaskCode) {
		this.devTaskCode = devTaskCode;
	}
	public String getDevTaskOverview() {
		return devTaskOverview;
	}
	public void setDevTaskOverview(String devTaskOverview) {
		this.devTaskOverview = devTaskOverview;
	}
	public Integer getDevTaskStatus() {
		return devTaskStatus;
	}
	public void setDevTaskStatus(Integer devTaskStatus) {
		this.devTaskStatus = devTaskStatus;
	}
	public Long getDevuserID() {
		return devuserID;
	}
	public void setDevuserID(Long devuserID) {
		this.devuserID = devuserID;
	}
	public String getDevuserName() {
		return devuserName;
	}
	public void setDevuserName(String devuserName) {
		this.devuserName = devuserName;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Date getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}
	public Date getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	public double getPlanWorkload() {
		return planWorkload;
	}
	public void setPlanWorkload(double planWorkload) {
		this.planWorkload = planWorkload;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getFeatureOverview() {
		return featureOverview;
	}
	public void setFeatureOverview(String featureOverview) {
		this.featureOverview = featureOverview;
	}
	public String getRequirementFeatureStatus() {
		return requirementFeatureStatus;
	}
	public void setRequirementFeatureStatus(String requirementFeatureStatus) {
		this.requirementFeatureStatus = requirementFeatureStatus;
	}
	public Long getManageUserId() {
		return manageUserId;
	}
	public void setManageUserId(Long manageUserId) {
		this.manageUserId = manageUserId;
	}
	public String getManageUserName() {
		return manageUserName;
	}
	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}
	public Long getExecuteUserId() {
		return executeUserId;
	}
	public void setExecuteUserId(Long executeUserId) {
		this.executeUserId = executeUserId;
	}
	public String getExecuteUserName() {
		return executeUserName;
	}
	public void setExecuteUserName(String executeUserName) {
		this.executeUserName = executeUserName;
	}
	public Long getSystemId() {
		return systemId;
	}
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getRequirementSource() {
		return requirementSource;
	}
	public void setRequirementSource(String requirementSource) {
		this.requirementSource = requirementSource;
	}
	public String getRequirementType() {
		return requirementType;
	}
	public void setRequirementType(String requirementType) {
		this.requirementType = requirementType;
	}
	public String getRequirementPriority() {
		return requirementPriority;
	}
	public void setRequirementPriority(String requirementPriority) {
		this.requirementPriority = requirementPriority;
	}
	public String getRequirementPanl() {
		return requirementPanl;
	}
	public void setRequirementPanl(String requirementPanl) {
		this.requirementPanl = requirementPanl;
	}
	public String getRequirementStatus() {
		return requirementStatus;
	}
	public void setRequirementStatus(String requirementStatus) {
		this.requirementStatus = requirementStatus;
	}
	public Long getApplyUserId() {
		return applyUserId;
	}
	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}
	public String getApplyUserName() {
		return applyUserName;
	}
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	public Long getApplyDeptId() {
		return applyDeptId;
	}
	public void setApplyDeptId(Long applyDeptId) {
		this.applyDeptId = applyDeptId;
	}
	public String getApplyDeptName() {
		return applyDeptName;
	}
	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	public Date getExpectOnlineDate() {
		return expectOnlineDate;
	}
	public void setExpectOnlineDate(Date expectOnlineDate) {
		this.expectOnlineDate = expectOnlineDate;
	}
	public Date getPlanOnlineDate() {
		return planOnlineDate;
	}
	public void setPlanOnlineDate(Date planOnlineDate) {
		this.planOnlineDate = planOnlineDate;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public String getDevStatus() {
		return DevStatus;
	}
	public void setDevStatus(String devStatus) {
		DevStatus = devStatus;
	}
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public double getActualWorkload() {
		return actualWorkload;
	}
	public void setActualWorkload(double actualWorkload) {
		this.actualWorkload = actualWorkload;
	}
	public String getRequirementName() {
		return requirementName;
	}
	public void setRequirementName(String requirementName) {
		this.requirementName = requirementName;
	}
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public String getRequirementOverview() {
		return requirementOverview;
	}
	public void setRequirementOverview(String requirementOverview) {
		this.requirementOverview = requirementOverview;
	}
	public String getDevelopmentMode() {
		return developmentMode;
	}
	public void setDevelopmentMode(String developmentMode) {
		this.developmentMode = developmentMode;
	}
	public String getWindowName() {
		return windowName;
	}
	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
	public Integer getDevTaskPriority() {
		return devTaskPriority;
	}
	public void setDevTaskPriority(Integer devTaskPriority) {
		this.devTaskPriority = devTaskPriority;
	}

	public Integer getDevTaskType() {
		return devTaskType;
	}

	public void setDevTaskType(Integer devTaskType) {
		this.devTaskType = devTaskType;
	}

	public double getEstimateRemainWorkload() {
		return estimateRemainWorkload;
	}

	public void setEstimateRemainWorkload(double estimateRemainWorkload) {
		this.estimateRemainWorkload = estimateRemainWorkload;
	}
}
