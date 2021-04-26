package cn.pioneeruniverse.dev.dto;

import java.io.Serializable;
import java.util.List;

public class TestDefectInfoDTO implements Serializable {

    private static final long serialVersionUID = -882774576983544024L;

    /**
     * 系统id
     **/
    private List<Long> systemId;   //系统id

    /**
     * 缺陷来源
     **/
    private List<Long> defectSource;  //缺陷来源

    /**
     * 紧急程度
     **/
    private List<Long> emergencyLevel;  //紧急程度

    /**
     * 开始时间
     **/
    private String startTime;   //开始时间

    /**
     * 结束时间
     **/
    private String endTime;     //结束时间

    public TestDefectInfoDTO() {
    }

    public TestDefectInfoDTO(List<Long> systemId, List<Long> defectSource, List<Long> emergencyLevel, String startTime, String endTime) {
        this.systemId = systemId;
        this.defectSource = defectSource;
        this.emergencyLevel = emergencyLevel;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public List<Long> getSystemId() {
        return systemId;
    }

    public void setSystemId(List<Long> systemId) {
        this.systemId = systemId;
    }

    public List<Long> getDefectSource() {
        return defectSource;
    }

    public void setDefectSource(List<Long> defectSource) {
        this.defectSource = defectSource;
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

    public List<Long> getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(List<Long> emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    @Override
    public String toString() {
        return "DefectInfoDTO{" +
                "systemId=" + systemId +
                ", defectSource=" + defectSource +
                ", emergencyLevel=" + emergencyLevel +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
