package cn.pioneeruniverse.dev.vo;

import java.util.List;

/**
*@author fanwentao
*@Description 缺陷引入vo类
*@Date 2020/8/11
*@return
**/
public class DefectInputInfoVo {
	private String defectCodeSql;
	
	private String emergencyLevelSql;
	
	private String defectSummarySql;
	
	private String requirementCodeSql;
	
	private String defectStatusSql;
	
	private String defectSourceSql;
	
	private String severityLevel;
	
	private String defectTypeSql;
	
	private String assignUserNameSql;
	
	private String testUserNameSql;
	
	private String submitUserNameSql;
	
	private String developUserNameSql;
	
	private List<Long> testTaskIdList;
	
	private List<Long> requirementFeatureIdList;
	
	private List<Long> systemIdList;
	
	private List<Long> windowIdList;
	
	private String repairRoundSql;
	
	private String sidx;
	
	private String sord;
	
	private Long uid;

	public String getDefectCodeSql() {
		return defectCodeSql;
	}

	public void setDefectCodeSql(String defectCodeSql) {
		this.defectCodeSql = defectCodeSql;
	}

	public String getEmergencyLevelSql() {
		return emergencyLevelSql;
	}

	public void setEmergencyLevelSql(String emergencyLevelSql) {
		this.emergencyLevelSql = emergencyLevelSql;
	}

	public String getDefectSummarySql() {
		return defectSummarySql;
	}

	public void setDefectSummarySql(String defectSummarySql) {
		this.defectSummarySql = defectSummarySql;
	}

	public String getDefectStatusSql() {
		return defectStatusSql;
	}

	public void setDefectStatusSql(String defectStatusSql) {
		this.defectStatusSql = defectStatusSql;
	}

	public List<Long> getTestTaskIdList() {
		return testTaskIdList;
	}

	public void setTestTaskIdList(List<Long> testTaskIdList) {
		this.testTaskIdList = testTaskIdList;
	}

	public List<Long> getRequirementFeatureIdList() {
		return requirementFeatureIdList;
	}

	public void setRequirementFeatureIdList(List<Long> requirementFeatureIdList) {
		this.requirementFeatureIdList = requirementFeatureIdList;
	}

	public List<Long> getSystemIdList() {
		return systemIdList;
	}

	public void setSystemIdList(List<Long> systemIdList) {
		this.systemIdList = systemIdList;
	}

	public List<Long> getWindowIdList() {
		return windowIdList;
	}

	public void setWindowIdList(List<Long> windowIdList) {
		this.windowIdList = windowIdList;
	}

	public String getRepairRoundSql() {
		return repairRoundSql;
	}

	public void setRepairRoundSql(String repairRoundSql) {
		this.repairRoundSql = repairRoundSql;
	}

	public String getDefectSourceSql() {
		return defectSourceSql;
	}

	public void setDefectSourceSql(String defectSourceSql) {
		this.defectSourceSql = defectSourceSql;
	}

	public String getSeverityLevel() {
		return severityLevel;
	}

	public void setSeverityLevel(String severityLevel) {
		this.severityLevel = severityLevel;
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

	public String getRequirementCodeSql() {
		return requirementCodeSql;
	}

	public void setRequirementCodeSql(String requirementCodeSql) {
		this.requirementCodeSql = requirementCodeSql;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getDefectTypeSql() {
		return defectTypeSql;
	}

	public void setDefectTypeSql(String defectTypeSql) {
		this.defectTypeSql = defectTypeSql;
	}

	public String getAssignUserNameSql() {
		return assignUserNameSql;
	}

	public void setAssignUserNameSql(String assignUserNameSql) {
		this.assignUserNameSql = assignUserNameSql;
	}

	public String getTestUserNameSql() {
		return testUserNameSql;
	}

	public void setTestUserNameSql(String testUserNameSql) {
		this.testUserNameSql = testUserNameSql;
	}

	public String getSubmitUserNameSql() {
		return submitUserNameSql;
	}

	public void setSubmitUserNameSql(String submitUserNameSql) {
		this.submitUserNameSql = submitUserNameSql;
	}

	public String getDevelopUserNameSql() {
		return developUserNameSql;
	}

	public void setDevelopUserNameSql(String developUserNameSql) {
		this.developUserNameSql = developUserNameSql;
	}

	
}
