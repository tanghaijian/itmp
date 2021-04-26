package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblReportMonthlySummary extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5180463691634946612L;

	private String yearMonth; //时间

    private Integer planWindowsNumber; //计划内版本次数

    private Integer tempWindowsNumber; //临时版本次数

    private Integer tempAddTaskNumber; //临时增加任务数

    private Integer tempDelTaskNumber; //临时删除任务数

    private Integer totalTaskNumber; //测试任务总数

    private Integer requirementNumber; //业务需求数

    private Integer defectNumber; //缺陷数

    private Double changePercent; //变更率

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
    
    

}