package cn.pioneeruniverse.project.vo;

public class SynRequirementSystem{

	/**
	 * id
	 **/
	private Long reqsystemId;		//id

	/**
	 * 需求id
	 **/
	private Long reqId; 			//需求id

	/**
	 * 系统id
	 **/
	private Long applicationId; 	//系统id

	/**
	 * 系统编号
	 **/
	private String applicationCode; //系统编号

	/**
	 * 功能点计数
	 **/
	private String functionCount; 	//功能点计数

	/**
	 * 主系统标识
	 **/
	private String isMainsystem; 	//主系统标识

	/**
	 * 挂起标识
	 **/
	private String appHangup; 		//挂起标识

	/**
	 * 挂起人
	 **/
	private String hangupEmp; 		//挂起人

	/**
	 * 挂起时间
	 **/
	private String hangupTime; 		//挂起时间

	/**
	 * 开发管理岗
	 **/
	private String appDevmngcode;   //开发管理岗

	/**
	 * 测试管理岗
	 **/
	private String appTestmngcode;	//测试管理岗

	public Long getReqsystemId() {
		return reqsystemId;
	}
	public void setReqsystemId(Long reqsystemId) {
		this.reqsystemId = reqsystemId;
	}
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}
	public String getFunctionCount() {
		return functionCount;
	}
	public void setFunctionCount(String functionCount) {
		this.functionCount = functionCount;
	}
	public String getIsMainsystem() {
		return isMainsystem;
	}
	public void setIsMainsystem(String isMainsystem) {
		this.isMainsystem = isMainsystem;
	}
	public String getAppHangup() {
		return appHangup;
	}
	public void setAppHangup(String appHangup) {
		this.appHangup = appHangup;
	}
	public String getHangupEmp() {
		return hangupEmp;
	}
	public void setHangupEmp(String hangupEmp) {
		this.hangupEmp = hangupEmp;
	}
	public String getHangupTime() {
		return hangupTime;
	}
	public void setHangupTime(String hangupTime) {
		this.hangupTime = hangupTime;
	}

	public String getAppDevmngcode() {
		return appDevmngcode;
	}
	public void setAppDevmngcode(String appDevmngcode) {
		this.appDevmngcode = appDevmngcode;
	}

	public String getAppTestmngcode() {
		return appTestmngcode;
	}
	public void setAppTestmngcode(String appTestmngcode) {
		this.appTestmngcode = appTestmngcode;
	}
}