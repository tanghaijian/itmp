package cn.pioneeruniverse.dev.vo;

public class WorkVo extends BaseVo{

	private String devTaskCode;
	//任务名称
	private String devTaskName;
	//任务状态
	private String devTaskStatus;
	//涉及系统
	private String systemName;
	//关联需求
	private String requirementCode;
	//关联任务
	private String featureCode;
	//开发人员
	private String userName;
	public String getDevTaskCode() {
		return devTaskCode;
	}
	public void setDevTaskCode(String devTaskCode) {
		this.devTaskCode = devTaskCode;
	}
	public String getDevTaskName() {
		return devTaskName;
	}
	public void setDevTaskName(String devTaskName) {
		this.devTaskName = devTaskName;
	}
	public String getDevTaskStatus() {
		return devTaskStatus;
	}
	public void setDevTaskStatus(String devTaskStatus) {
		this.devTaskStatus = devTaskStatus;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getRequirementCode() {
		return requirementCode;
	}
	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
