package cn.pioneeruniverse.dev.vo;

import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblTestWorkVo
 * @Description:
 * @author author
 * @date 2020年8月19日
 *
 */
public class TblTestWorkVo extends BaseEntity {
	private String testTaskName;

	private String testTaskCode;// 任务编号

	private String testTaskStatus;

	private String testStage;// 测试阶段

	private String systemName;// 系统co

	private String requirementCode;// 关联需求

	private String featureCode;// 关联测试任务

	private String userName;// 测试人员

	private String windowName;// 投产窗口

	private String taskAssignUserName;// 任务分配

	@Transient
	private String sidx; // 排序字段

	@Transient
	private String sord; // 排序顺序
	
	private String caseNum; // 用例数
	
	private String defectNum;//缺陷数
	
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

	public String getTestTaskStatus() {
		return testTaskStatus;
	}

	public void setTestTaskStatus(String testTaskStatus) {
		this.testTaskStatus = testTaskStatus;
	}

	public String getTestStage() {
		return testStage;
	}

	public void setTestStage(String testStage) {
		this.testStage = testStage;
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

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getTaskAssignUserName() {
		return taskAssignUserName;
	}

	public void setTaskAssignUserName(String taskAssignUserName) {
		this.taskAssignUserName = taskAssignUserName;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public String getDefectNum() {
		return defectNum;
	}

	public void setDefectNum(String defectNum) {
		this.defectNum = defectNum;
	}

}
