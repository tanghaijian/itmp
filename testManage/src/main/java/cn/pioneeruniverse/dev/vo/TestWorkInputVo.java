package cn.pioneeruniverse.dev.vo;

import java.util.List;

import org.springframework.data.annotation.Transient;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TestWorkInputVo extends BaseEntity{

	/**
	 * 任务编号
	 **/
	private String testTaskCodeSql; //任务编号

	/**
	 * 任务名称
	 **/
	private String testTaskNameSql; //任务名称

	/**
	 * 状态
	 **/
	private String workTaskStatus;//状态

	/**
	 * 系统id
	 **/
	private List<Long> systemIdList; //系统id

	/**
	 * 需求id
	 **/
	private List<Long> reqIdList; //需求id

	/**
	 * 测试任务编号
	 **/
	private List<Long> featureCodeSql; //测试任务编号

	/**
	 * 测试人id
	 **/
	private String testUserIdName; //测试人id

	/**
	 * 测试阶段
	 **/
	private String testStageName;//测试阶段

	/**
	* 投产窗口id
	**/
	private List<Long> windowIdList; //投产窗口id

	/**
	 * 任务分配人员
	 **/
	private String taskAssignUserId;//任务分配人员

	/**
	 * 排序字段
	 **/
	@Transient
	private String sidx; //排序字段

	/**
	 * 排序顺序
	 **/
	@Transient
	private String sord; //排序顺序

	/**
	 * 当前用户id
	 **/
	private Long currentUserId; //当前用户id

	/**
	 * 缺陷数
	 **/
	private String defectNum;//缺陷数

	/**
	 * 任务id集合
	 **/
	private List<Long> idList;//任务id集合

	public String getTestTaskCodeSql() {
		return testTaskCodeSql;
	}

	public void setTestTaskCodeSql(String testTaskCodeSql) {
		this.testTaskCodeSql = testTaskCodeSql;
	}

	public String getTestTaskNameSql() {
		return testTaskNameSql;
	}

	public void setTestTaskNameSql(String testTaskNameSql) {
		this.testTaskNameSql = testTaskNameSql;
	}

	public String getWorkTaskStatus() {
		return workTaskStatus;
	}

	public void setWorkTaskStatus(String workTaskStatus) {
		this.workTaskStatus = workTaskStatus;
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

	public List<Long>  getFeatureCodeSql() {
		return featureCodeSql;
	}

	public void setFeatureCodeSql(List<Long>  featureCodeSql) {
		this.featureCodeSql = featureCodeSql;
	}

	public String getTestUserIdName() {
		return testUserIdName;
	}

	public void setTestUserIdName(String testUserIdName) {
		this.testUserIdName = testUserIdName;
	}

	public String getTestStageName() {
		return testStageName;
	}

	public void setTestStageName(String testStageName) {
		this.testStageName = testStageName;
	}

	public List<Long> getWindowIdList() {
		return windowIdList;
	}

	public void setWindowIdList(List<Long> windowIdList) {
		this.windowIdList = windowIdList;
	}

	public String getTaskAssignUserId() {
		return taskAssignUserId;
	}

	public void setTaskAssignUserId(String taskAssignUserId) {
		this.taskAssignUserId = taskAssignUserId;
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

	public Long getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Long currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getDefectNum() {
		return defectNum;
	}

	public void setDefectNum(String defectNum) {
		this.defectNum = defectNum;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
	
	
	
}
