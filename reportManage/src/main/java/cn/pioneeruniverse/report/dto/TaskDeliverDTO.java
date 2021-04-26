package cn.pioneeruniverse.report.dto;

import java.io.Serializable;
import java.util.List;

public class TaskDeliverDTO implements Serializable {
    private static final long serialVersionUID = -882774576983544024L;

    /**
     * 系统id
     **/
    private List<Long> systemId;   //系统id

    /**
     * 任务来源
     **/
    private List<Long> requirementFeatureSource;  //任务来源

    /**
     * 交付状态
     **/
    private List<Long> deliveryStatus;   //交付状态

    /**
     * 开始时间
     **/
    private String startTime;   //开始时间

    /**
     * 结束时间
     **/
    private String endTime;     //结束时间

    public TaskDeliverDTO() {
    }

    public List<Long> getSystemId() {
        return systemId;
    }

    public void setSystemId(List<Long> systemId) {
        this.systemId = systemId;
    }

    public List<Long> getRequirementFeatureSource() {
        return requirementFeatureSource;
    }

    public void setRequirementFeatureSource(List<Long> requirementFeatureSource) {
        this.requirementFeatureSource = requirementFeatureSource;
    }

    public List<Long> getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(List<Long> deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "TaskDeliverDTO{" +
                "systemId=" + systemId +
                ", requirementFeatureSource=" + requirementFeatureSource +
                ", deliveryStatus=" + deliveryStatus +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
