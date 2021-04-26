package cn.pioneeruniverse.dev.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.bean.PropertyInfo;


public class DevDetailVo  extends BaseVo{
	
	 private String testTaskName;
	    
	 private String testTaskCode;
	 //描述
	 private String testTaskOverview;
	 //任务状态
	 private Integer devTaskStatus;
	 private String testDevTaskStatus;
	 private Long devuserID;
	 //开发人员名字
	 private String devuserName;
	 //创建姓名
	 private String createName;
	 //实际开始日期
	 private Date actualStartDate;
	 //实际结束日期
	 private Date actualEndDate;
	 //实际工作量（人天）
	 private double actualWorkload;
	 //计划开始日期
	 private Date planStartDate;
	 //计划结束日期
	 private Date planEndDate;
	 //预估工作量（人天）
	 private double planWorkload;
	 //关联任务名称
	 private String featureName;
	//关联任务编号
	 private String featureCode;
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
	 //需求来源
	 private String requirementSource;
	 //需求类型
	 private String requirementType;
	//需求描述
	 private String requirementOverview;
	 //优先级
	 private String requirementPriority;
	 //需求计划
	 private String requirementPanl;
	 //需求状态
	 private String requirementStatus;
	 //需求名称
	 private String requirementName;
	//需求编号
	 private String requirementCode;
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
	 private String testStage;//测试阶段
//	 private Long cases;//工作任务下所有测试集关联的案例  设计案例数
//	 private Long caseExecutes;//执行案例数
	 private Integer designCaseNumber;
	 private Integer executeCaseNumber;
	 private List<String> executeUser;//案例执行人
	 private String requirementFeatureSource;//任务类型
	 private Integer requirementChangeNumber;//需求变更次数
	 private String importantRequirementType;//重点需求类型
	 private Date pptDeployTime;//发布版测时间
	 private Date submitTestTime;//提交测试时间
	 private Long defectNum;//缺陷数
	 private String requirementFeatureStatusName;//关联测试任务状态名称
	 private String taskAssignUserName;//任务分配人员
	 private Long featureId;//关联任务id
	 private Integer environmentType;//环境类型
	private int projectType;
	 
	 
	public Integer getEnvironmentType() {
		return environmentType;
	}
	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}
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

	public String getTestTaskName() {
		return testTaskName;
	}
	public void setTestTaskName(String testTaskName) {
		this.testTaskName = testTaskName;
	}
	public String getTestTaskCode() {
		return testTaskCode;
	}
	public void setTestTaskCode(String testTaskCode) {
		this.testTaskCode = testTaskCode;
	}
	
	public String getTestTaskOverview() {
		return testTaskOverview;
	}
	public void setTestTaskOverview(String testTaskOverview) {
		this.testTaskOverview = testTaskOverview;
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
	public String getTestStage() {
		return testStage;
	}
	public void setTestStage(String testStage) {
		this.testStage = testStage;
	}
	public String getTestDevTaskStatus() {
		return testDevTaskStatus;
	}
	public void setTestDevTaskStatus(String testDevTaskStatus) {
		this.testDevTaskStatus = testDevTaskStatus;
	}
	public String getRequirementOverview() {
		return requirementOverview;
	}
	public void setRequirementOverview(String requirementOverview) {
		this.requirementOverview = requirementOverview;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
//	public Long getCases() {
//		return cases;
//	}
//	public void setCases(Long cases) {
//		this.cases = cases;
//	}
//	public Long getCaseExecutes() {
//		return caseExecutes;
//	}
//	public void setCaseExecutes(Long caseExecutes) {
//		this.caseExecutes = caseExecutes;
//	}
	public List<String> getExecuteUser() {
		return executeUser;
	}
	public void setExecuteUser(List<String> executeUser) {
		this.executeUser = executeUser;
	}
	public String getRequirementFeatureSource() {
		return requirementFeatureSource;
	}
	public void setRequirementFeatureSource(String requirementFeatureSource) {
		this.requirementFeatureSource = requirementFeatureSource;
	}
	public Integer getRequirementChangeNumber() {
		return requirementChangeNumber;
	}
	public void setRequirementChangeNumber(Integer requirementChangeNumber) {
		this.requirementChangeNumber = requirementChangeNumber;
	}
	public String getImportantRequirementType() {
		return importantRequirementType;
	}
	public void setImportantRequirementType(String importantRequirementType) {
		this.importantRequirementType = importantRequirementType;
	}
	public Date getPptDeployTime() {
		return pptDeployTime;
	}
	public void setPptDeployTime(Date pptDeployTime) {
		this.pptDeployTime = pptDeployTime;
	}
	public Date getSubmitTestTime() {
		return submitTestTime;
	}
	public void setSubmitTestTime(Date submitTestTime) {
		this.submitTestTime = submitTestTime;
	}
	public Integer getDesignCaseNumber() {
		return designCaseNumber;
	}
	public void setDesignCaseNumber(Integer designCaseNumber) {
		this.designCaseNumber = designCaseNumber;
	}
	public Integer getExecuteCaseNumber() {
		return executeCaseNumber;
	}
	public void setExecuteCaseNumber(Integer executeCaseNumber) {
		this.executeCaseNumber = executeCaseNumber;
	}
	public Long getDefectNum() {
		return defectNum;
	}
	public void setDefectNum(Long defectNum) {
		this.defectNum = defectNum;
	}
	public String getRequirementFeatureStatusName() {
		return requirementFeatureStatusName;
	}
	public void setRequirementFeatureStatusName(String requirementFeatureStatusName) {
		this.requirementFeatureStatusName = requirementFeatureStatusName;
	}
	public String getTaskAssignUserName() {
		return taskAssignUserName;
	}
	public void setTaskAssignUserName(String taskAssignUserName) {
		this.taskAssignUserName = taskAssignUserName;
	}

	public String getRequirementCode() {
		return requirementCode;
	}

	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}

	public int getProjectType() {
		return projectType;
	}
	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
}
