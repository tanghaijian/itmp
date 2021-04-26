package cn.pioneeruniverse.project.vo;

import java.util.Date;

public class SynRequirementFeature{

	 /**
	  * 流水号
	  **/
	private Long tbltaskId;	//流水号

	/**
	 * 关联需求ID
	 **/
	private Long reqId;	//关联需求ID

	/**
	 * 关联系统ID
	 **/
	private Long applicationId;	//关联系统ID

	/**
	 * 关联系统Code
	 **/
	private String applicationCode;	//关联系统Code

	/**
	 * 任务名称
	 **/
	private String taskName;	//任务名称

	/**
	 * 任务详细描述
	 **/
	private String taskDescription;	//任务详细描述

	/**
	 * 任务种类(未使用)
	 **/
	private String taskType;	//任务种类(未使用)

	/**
	 * 任务管理岗，即开发管理岗或测试管理岗员工号
	 **/
	private String taskMnger;	//任务管理岗，即开发管理岗或测试管理岗员工号

	/**
	 * 任务执行人员工号
	 **/
	private String taskExer;	//任务执行人员工号

	/**
	 * 任务状态("01：待实施 02：实施中 03：实施完成 00：取消")
	 **/
	private String taskStatus;	//任务状态("01：待实施 02：实施中 03：实施完成 00：取消")

	/**
	 * "是否临时任务 (00：否 01：是)
	 **/
	private String isTasktemp;	//"是否临时任务 (00：否 01：是)

	/**
	 * 任务所属处室
	 **/
	private String taskSection;		//任务所属处室

	/**
	 * 任务处理意见
	 **/
	private String taskResult;		//任务处理意见

	/**
	 * 任务工作量
	 **/
	private Double taskWorkload;		//任务工作量

	/**
	 * 预计开始时间
	 **/
	private Date taskPrestartdate;	//预计开始时间

	/**
	 * 预计结束时间
	 **/
	private Date taskPreenddate;	//预计结束时间

	/**
	 * 实际开始时间
	 **/
	private Date taskStartdate;		//实际开始时间

	/**
	 * 实际结束时间
	 **/
	private Date taskEnddate;	//实际结束时间

	/**
	 * 创建人
	 **/
	private String optCode;		//创建人

	/**
	 * 创建时间
	 **/
	private Date createDate;	//创建时间

	/**
	 * 修改时间
	 **/
	private Date updateDate;	//修改时间

	/**
	 * 任务所属部门
	 **/
	private String taskDepartment;	//任务所属部门

	/**
	 * 预估工作量
	 **/
	private Double taskWorkReckon;	//预估工作量

	
	public Long getTbltaskId() {
		return tbltaskId;
	}
	public void setTbltaskId(Long tbltaskId) {
		this.tbltaskId = tbltaskId;
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskMnger() {
		return taskMnger;
	}
	public void setTaskMnger(String taskMnger) {
		this.taskMnger = taskMnger;
	}
	public String getTaskExer() {
		return taskExer;
	}
	public void setTaskExer(String taskExer) {
		this.taskExer = taskExer;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getIsTasktemp() {
		return isTasktemp;
	}
	public void setIsTasktemp(String isTasktemp) {
		this.isTasktemp = isTasktemp;
	}
	public String getTaskSection() {
		return taskSection;
	}
	public void setTaskSection(String taskSection) {
		this.taskSection = taskSection;
	}
	public String getTaskResult() {
		return taskResult;
	}
	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}
	public Double getTaskWorkload() {
		return taskWorkload;
	}
	public void setTaskWorkload(Double taskWorkload) {
		this.taskWorkload = taskWorkload;
	}
	public Date getTaskPrestartdate() {
		return taskPrestartdate;
	}
	public void setTaskPrestartdate(Date taskPrestartdate) {
		this.taskPrestartdate = taskPrestartdate;
	}
	public Date getTaskPreenddate() {
		return taskPreenddate;
	}
	public void setTaskPreenddate(Date taskPreenddate) {
		this.taskPreenddate = taskPreenddate;
	}
	public Date getTaskStartdate() {
		return taskStartdate;
	}
	public void setTaskStartdate(Date taskStartdate) {
		this.taskStartdate = taskStartdate;
	}
	public Date getTaskEnddate() {
		return taskEnddate;
	}
	public void setTaskEnddate(Date taskEnddate) {
		this.taskEnddate = taskEnddate;
	}
	public String getOptCode() {
		return optCode;
	}
	public void setOptCode(String optCode) {
		this.optCode = optCode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getTaskDepartment() {
		return taskDepartment;
	}
	public void setTaskDepartment(String taskDepartment) {
		this.taskDepartment = taskDepartment;
	}
	public Double getTaskWorkReckon() {
		return taskWorkReckon;
	}
	public void setTaskWorkReckon(Double taskWorkReckon) {
		this.taskWorkReckon = taskWorkReckon;
	}
}