package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 缺陷等级表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlyDefectLevel extends BaseEntity {

    private String yearMonth; //时间

    private Integer systemType;//0,所有项目、1:敏捷、2:运维、3:项目


    private Integer level;//(1:建议性缺陷、2:文字错误、3:轻微缺陷、4:一般性确信啊、5:严重缺陷)

    private Integer defectNumber;//缺陷数

    private Double percentage;//占比 i/(1+2+3+4+5)

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDefectNumber() {
        return defectNumber;
    }

    public void setDefectNumber(Integer defectNumber) {
        this.defectNumber = defectNumber;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
