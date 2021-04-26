package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 月报系统数据表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlySystemData extends BaseEntity{

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

	private Integer auditStatus; //是否审核(0:未审核;1:已审核)

    private Integer totalRepairRound; //累计修复轮次

    private Double avgRepairRound; //平均修复轮次

    private Integer lastmonthUndefectedNumber; //上月漏检缺陷数

    private String lastmonthUndefectedBelonger; //上月漏检归属

	/*
	0:月报数据
	1:审核时页面数据
	2:审核时统计业务数据
	 */
	private Integer type;

	@TableField(exist = false)
	private String userName;

	@TableField(exist = false)
	private Long userId;

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

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
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

	public Integer getLastmonthUndefectedNumber() {
		return lastmonthUndefectedNumber;
	}

	public void setLastmonthUndefectedNumber(Integer lastmonthUndefectedNumber) {
		this.lastmonthUndefectedNumber = lastmonthUndefectedNumber;
	}

	public String getLastmonthUndefectedBelonger() {
		return lastmonthUndefectedBelonger;
	}

	public void setLastmonthUndefectedBelonger(String lastmonthUndefectedBelonger) {
		this.lastmonthUndefectedBelonger = lastmonthUndefectedBelonger;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}