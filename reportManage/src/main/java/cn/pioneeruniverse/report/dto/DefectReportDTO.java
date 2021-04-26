package cn.pioneeruniverse.report.dto;

import java.io.Serializable;
import java.util.Date;

public class DefectReportDTO implements Serializable {

    private static final long serialVersionUID = -882774576983544024L;

    /**
     * 系统id
     **/
    private String systemId;    //

    /**
     * 缺陷状态
     **/
    private String defectStatus; //

    /**
     * 缺陷来源
     **/
    private String defectSource; //

    /**
     * 缺陷类型
     **/
    private String defectType; //

    /**
     * 缺陷等级
     **/
    private String severityLevel; //

    /**
     * 紧急程度
     **/
    private String emergencyLevel; //

    /**
     * 投产窗口
     **/
    private String commissioningWindowId; //

    /**
     * 缺陷开始时间
     **/
    private Date createDate; //

    /**
     * 缺陷结束时间
     **/
    private Date endDate; //

    /**
     * 投产窗口开始时间
     **/
    private Date windowDate; //

    /**
     * 投产窗口结束时间
     **/
    private Date endWindowDate; //

    public DefectReportDTO() {
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getDefectStatus() {
        return defectStatus;
    }

    public void setDefectStatus(String defectStatus) {
        this.defectStatus = defectStatus;
    }

    public String getDefectSource() {
        return defectSource;
    }

    public void setDefectSource(String defectSource) {
        this.defectSource = defectSource;
    }

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(String commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public Date getWindowDate() {
        return windowDate;
    }

    public void setWindowDate(Date windowDate) {
        this.windowDate = windowDate;
    }

    public Date getEndWindowDate() {
        return endWindowDate;
    }

    public void setEndWindowDate(Date endWindowDate) {
        this.endWindowDate = endWindowDate;
    }
}
