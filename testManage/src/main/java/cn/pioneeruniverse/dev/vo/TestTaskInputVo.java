package cn.pioneeruniverse.dev.vo;

import java.util.List;

public class TestTaskInputVo {

	/**
	 * 任务编号
	 **/
	private String featureCodeSql; //任务编号

	/**
	 * 任务名称
	 **/
	private String featureNameSql; //任务名称

	/**
	 * 需求变更次数
	 **/
	private String changeNumberSql; //需求变更次数

	/**
	 * 系统id
	 **/
	private List<Long> systemIdList; //系统id


	/**
	 * 需求id
	 **/
	private List<Long> reqIdList; //需求id

	/**
	 * 投产窗口id
	 **/
	private List<Long> windowIdList; //投产窗口id

	/**
	 * 测试任务状态
	 **/
	private String reqFeatureStatusStr; //测试任务状态

	/**
	 * 测试管理岗sql
	 **/
	private String manageUserSql; //测试管理岗sql

	/**
	 * 执行人sql
	 **/
	private String executeUserSql; //执行人sql

	/**
	 * 部署状态
	 **/
	private String deployStatusStr; //部署状态

	/**
	 * 任务来源
	 **/
	private String requirementFeatureSourceStr; //任务来源

	/**
	 * 创建方式
	 **/
	private String createTypeStr; //创建方式
	
	private List<Long> idList;
	
	private Long uid;

	/**
	 * 排序字段
	 **/
	private String sidx; //排序字段

	/**
	 * 排序顺序
	 **/
	private String sord; //排序顺序

	public String getFeatureCodeSql() {
		return featureCodeSql;
	}

	public void setFeatureCodeSql(String featureCodeSql) {
		this.featureCodeSql = featureCodeSql;
	}

	public String getFeatureNameSql() {
		return featureNameSql;
	}

	public void setFeatureNameSql(String featureNameSql) {
		this.featureNameSql = featureNameSql;
	}

	public List<Long> getSystemIdList() {
		return systemIdList;
	}

	public void setSystemIdList(List<Long> systemIdList) {
		this.systemIdList = systemIdList;
	}

	public List<Long> getReqIdList() {
		return reqIdList;
	}

	public void setReqIdList(List<Long> reqIdList) {
		this.reqIdList = reqIdList;
	}

	public List<Long> getWindowIdList() {
		return windowIdList;
	}

	public void setWindowIdList(List<Long> windowIdList) {
		this.windowIdList = windowIdList;
	}

	public String getReqFeatureStatusStr() {
		return reqFeatureStatusStr;
	}

	public void setReqFeatureStatusStr(String reqFeatureStatusStr) {
		this.reqFeatureStatusStr = reqFeatureStatusStr;
	}

	public String getDeployStatusStr() {
		return deployStatusStr;
	}

	public void setDeployStatusStr(String deployStatusStr) {
		this.deployStatusStr = deployStatusStr;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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

	public String getRequirementFeatureSourceStr() {
		return requirementFeatureSourceStr;
	}

	public void setRequirementFeatureSourceStr(String requirementFeatureSourceStr) {
		this.requirementFeatureSourceStr = requirementFeatureSourceStr;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	public String getChangeNumberSql() {
		return changeNumberSql;
	}

	public void setChangeNumberSql(String changeNumberSql) {
		this.changeNumberSql = changeNumberSql;
	}

	public String getCreateTypeStr() {
		return createTypeStr;
	}

	public void setCreateTypeStr(String createTypeStr) {
		this.createTypeStr = createTypeStr;
	}

	public String getManageUserSql() {
		return manageUserSql;
	}

	public void setManageUserSql(String manageUserSql) {
		this.manageUserSql = manageUserSql;
	}

	public String getExecuteUserSql() {
		return executeUserSql;
	}

	public void setExecuteUserSql(String executeUserSql) {
		this.executeUserSql = executeUserSql;
	}

	
}
