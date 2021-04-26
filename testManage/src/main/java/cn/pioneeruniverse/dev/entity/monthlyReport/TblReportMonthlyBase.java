package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;


/**
 * 月报基本表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlyBase extends BaseEntity{

    /**
	 * 
	 */
	private String yearMonth; //时间

    private Integer planWindowsNumber; //计划内版本次数

    private Integer tempWindowsNumber; //临时版本次数

    private Integer tempAddTaskNumber; //临时增加任务数

    private Integer tempDelTaskNumber; //临时删除任务数

    private Integer totalTaskNumber; //测试任务总数

    private Integer requirementNumber; //业务需求数



    private Integer defectNumber; //缺陷数

    private Double changePercent; //变更率

	/*
			0：初始（系统自动生成数据报告）、可发起审核
            1：月报保存，基于业务数据生成，可不断刷新业务数据
            2：月报已修改，刷新给出确认框
            */
	private Integer baseStatus;

	private Integer auditStatus;//0:初始，1：发起审核，2：审核完成，3再次发起审核

	private Integer undetectedNumber;//漏检数；

	private Double detectedRate;//检出率:(defectNumber-undetectedNumber)/defectNumber

	private Integer repairRound;//总修复轮次
	private Integer repairedDefectNumber;//修复缺陷数

	private Double avgRepairRound;//平均修复轮次:repairRound/repairedDefectNumber


	private Integer agileSystemNum;//敏捷系统个数

	@TableField(exist = false)
	private String userName; //用户名

	@TableField(exist = false)
	private Integer countWindowsNumber; //月版本总数

	@TableField(exist = false)
	private double versionChangeRate;//版本变更率

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Integer getPlanWindowsNumber() {
		return planWindowsNumber;
	}

	public void setPlanWindowsNumber(Integer planWindowsNumber) {
		this.planWindowsNumber = planWindowsNumber;
	}

	public Integer getTempWindowsNumber() {
		return tempWindowsNumber;
	}

	public void setTempWindowsNumber(Integer tempWindowsNumber) {
		this.tempWindowsNumber = tempWindowsNumber;
	}

	public Integer getTempAddTaskNumber() {
		return tempAddTaskNumber;
	}

	public void setTempAddTaskNumber(Integer tempAddTaskNumber) {
		this.tempAddTaskNumber = tempAddTaskNumber;
	}

	public Integer getTempDelTaskNumber() {
		return tempDelTaskNumber;
	}

	public void setTempDelTaskNumber(Integer tempDelTaskNumber) {
		this.tempDelTaskNumber = tempDelTaskNumber;
	}

	public Integer getTotalTaskNumber() {
		return totalTaskNumber;
	}

	public void setTotalTaskNumber(Integer totalTaskNumber) {
		this.totalTaskNumber = totalTaskNumber;
	}

	public Integer getRequirementNumber() {
		return requirementNumber;
	}

	public void setRequirementNumber(Integer requirementNumber) {
		this.requirementNumber = requirementNumber;
	}

	public Integer getDefectNumber() {
		return defectNumber;
	}

	public void setDefectNumber(Integer defectNumber) {
		this.defectNumber = defectNumber;
	}

	public Double getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Double changePercent) {
		this.changePercent = changePercent;
	}


	public Integer getBaseStatus() {
		return baseStatus;
	}

	public void setBaseStatus(Integer baseStatus) {
		this.baseStatus = baseStatus;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getUndetectedNumber() {
		return undetectedNumber;
	}

	public void setUndetectedNumber(Integer undetectedNumber) {
		this.undetectedNumber = undetectedNumber;
	}

	public Double getDetectedRate() {
		return detectedRate;
	}

	public void setDetectedRate(Double detectedRate) {
		this.detectedRate = detectedRate;
	}

	public Integer getRepairRound() {
		return repairRound;
	}

	public void setRepairRound(Integer repairRound) {
		this.repairRound = repairRound;
	}

	public Integer getRepairedDefectNumber() {
		return repairedDefectNumber;
	}

	public void setRepairedDefectNumber(Integer repairedDefectNumber) {
		this.repairedDefectNumber = repairedDefectNumber;
	}

	public Double getAvgRepairRound() {
		return avgRepairRound;
	}

	public void setAvgRepairRound(Double avgRepairRound) {
		this.avgRepairRound = avgRepairRound;
	}

	public Integer getAgileSystemNum() {
		return agileSystemNum;
	}

	public void setAgileSystemNum(Integer agileSystemNum) {
		this.agileSystemNum = agileSystemNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCountWindowsNumber() {
		return countWindowsNumber;
	}

	public void setCountWindowsNumber(Integer countWindowsNumber) {
		this.countWindowsNumber = countWindowsNumber;
	}

	public double getVersionChangeRate() {
		return versionChangeRate;
	}

	public void setVersionChangeRate(double versionChangeRate) {
		this.versionChangeRate = versionChangeRate;
	}
}