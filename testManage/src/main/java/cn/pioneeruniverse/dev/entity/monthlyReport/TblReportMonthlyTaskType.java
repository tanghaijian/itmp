package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.amazonaws.partitions.PartitionRegionImpl;

/**
 * 任务类型表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlyTaskType extends BaseEntity {

    private String yearMonth; //时间

    private Integer systemType;//0,所有项目、1:敏捷、2:运维、3:项目

    private Integer type;//(1:业务需求、2:缺陷修复)

    private Integer taskNumber;//任务类型数

    private Double percentage;//占比 i/(1+2)

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(Integer taskNumber) {
        this.taskNumber = taskNumber;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
