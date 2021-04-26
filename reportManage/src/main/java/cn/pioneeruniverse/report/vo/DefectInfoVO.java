package cn.pioneeruniverse.report.vo;

import java.io.Serializable;
import java.sql.Date;


public class DefectInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 提出日期
     **/
    private Date submitDate;  //

    /**
     * 关闭日期
     **/
    private Date closeTime; //

    /**
     * 缺陷来源
     **/
    private Integer defectSource;   //

    /**
     * 紧急程度
     **/
    private Integer emergencyLevel; //

    /**
     * 缺陷状态
     **/
    private Integer defectStatus;   //

    public DefectInfoVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Integer getDefectSource() {
        return defectSource;
    }

    public void setDefectSource(Integer defectSource) {
        this.defectSource = defectSource;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(Integer emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public Integer getDefectStatus() {
        return defectStatus;
    }

    public void setDefectStatus(Integer defectStatus) {
        this.defectStatus = defectStatus;
    }

    @Override
    public String toString() {
        return "DefectInfoVO{" +
                "id=" + id +
                ", submitDate=" + submitDate +
                ", closeTime=" + closeTime +
                ", defectSource=" + defectSource +
                ", emergencyLevel=" + emergencyLevel +
                ", defectStatus=" + defectStatus +
                '}';
    }
}
