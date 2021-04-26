package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 系统分类数据表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlySystemTypeData extends BaseEntity {

    private String yearMonth; //时间

    private Integer systemType;//1:敏捷、2:运维、3:项目

    private Integer totalTaskNumber; //测试任务总数


    private Integer designCaseNumber;//设计用例数

    private Integer defectNumber; //缺陷数

    private Integer repairedDefectNumber; //修复缺陷数
    private Integer unrepairedDefectNumber; //遗留缺陷数


    private Double defectPercent; //缺陷率


    private Integer systemNum;//系统个数

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public Integer getTotalTaskNumber() {
        return totalTaskNumber;
    }

    public void setTotalTaskNumber(Integer totalTaskNumber) {
        this.totalTaskNumber = totalTaskNumber;
    }

    public Integer getDesignCaseNumber() {
        return designCaseNumber;
    }

    public void setDesignCaseNumber(Integer designCaseNumber) {
        this.designCaseNumber = designCaseNumber;
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

    public Double getDefectPercent() {
        return defectPercent;
    }

    public void setDefectPercent(Double defectPercent) {
        this.defectPercent = defectPercent;
    }

    public Integer getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(Integer systemNum) {
        this.systemNum = systemNum;
    }
}
