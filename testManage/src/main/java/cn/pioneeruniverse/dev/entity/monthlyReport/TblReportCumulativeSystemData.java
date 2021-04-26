package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 系统累计数据统计表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportCumulativeSystemData extends BaseEntity{

    private String yearMonth; //时间

    private Long systemId;

    private String systemName; //系统名称
	private Integer systemType;//1:敏捷、2:运维、3:项目

    private Integer taskNumber; //测试任务数

    private Integer defectNumber; //缺陷数

    private Integer repairedDefectNumber; //修复缺陷数

    private Integer unrepairedDefectNumber; //遗留缺陷数

    private Integer designCaseNumber; //设计用例数

    private Double defectPercent; //缺陷率

	private Integer undefectedNumber; //漏检缺陷数

	private Double defectedRate; //检出率

    private Integer totalRepairRound; //累计修复轮次

    private Double avgRepairRound; //平均累计修复轮次

	private Integer startMonth; //开始统计月份

	private Integer endMonth; //最后统计月份

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
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

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}

	public Integer getDefectNumber() {
		return defectNumber;
	}

	public void setDefectNumber(Integer defectNumber) {
		this.defectNumber = defectNumber;
	}

	public Integer getRepairedDefectNumber() {
		return repairedDefectNumber;
	}

	public void setRepairedDefectNumber(Integer repairedDefectNumber) {
		this.repairedDefectNumber = repairedDefectNumber;
	}

	public Integer getUnrepairedDefectNumber() {
		return unrepairedDefectNumber;
	}

	public void setUnrepairedDefectNumber(Integer unrepairedDefectNumber) {
		this.unrepairedDefectNumber = unrepairedDefectNumber;
	}

	public Integer getDesignCaseNumber() {
		return designCaseNumber;
	}

	public void setDesignCaseNumber(Integer designCaseNumber) {
		this.designCaseNumber = designCaseNumber;
	}

	public Double getDefectPercent() {
		return defectPercent;
	}

	public void setDefectPercent(Double defectPercent) {
		this.defectPercent = defectPercent;
	}

	public Integer getUndefectedNumber() {
		return undefectedNumber;
	}

	public void setUndefectedNumber(Integer undefectedNumber) {
		this.undefectedNumber = undefectedNumber;
	}

	public Double getDefectedRate() {
		return defectedRate;
	}

	public void setDefectedRate(Double defectedRate) {
		this.defectedRate = defectedRate;
	}

	public Integer getTotalRepairRound() {
		return totalRepairRound;
	}

	public void setTotalRepairRound(Integer totalRepairRound) {
		this.totalRepairRound = totalRepairRound;
	}

	public Double getAvgRepairRound() {
		return avgRepairRound;
	}

	public void setAvgRepairRound(Double avgRepairRound) {
		this.avgRepairRound = avgRepairRound;
	}

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}
}