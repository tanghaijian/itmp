package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblReportMonthlySystem
 * @Description 统计信息
 * @author author
 * @date 2020年8月24日
 *
 */
public class TblReportMonthlySystem extends BaseEntity{

    private String yearMonth; //时间

    private Long systemId; 
    
    @TableField(exist = false)
    private String systemName; //系统名称

    private Integer taskNumber; //测试任务数

    private Integer defectNumber; //缺陷数

    private Integer repairedDefectNumber; //修复缺陷数

    private Integer unrepairedDefectNumber; //遗留缺陷数

    private Integer designCaseNumber; //设计用例数

    private Double defectPercent; //缺陷率

    private Integer totalRepairRound; //累计修复轮次

    private Double avgRepairRound; //平均修复轮次

    private Integer lastmonthUndefectedNumber; //上月漏检缺陷数

    private String lastmonthUndefectedBelonger; //上月漏检归属

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

    
}