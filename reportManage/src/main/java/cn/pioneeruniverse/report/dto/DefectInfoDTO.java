package cn.pioneeruniverse.report.dto;

import java.io.Serializable;
import java.util.List;
/**
 *
 * @ClassName:DefectInfoDTO
 * @Description:缺陷
 * @author author
 * @date 2020年8月16日
 *
 */

public class DefectInfoDTO implements Serializable {

    private static final long serialVersionUID = -882774576983544024L;

    private List<Long> systemId;   //系统id
    private List<Long> defectSource;  //缺陷来源
    private List<Long> emergencyLevel;  //紧急程度
    private String startTime;   //开始时间
    private String endTime;     //结束时间

    public DefectInfoDTO() {
    }

    public DefectInfoDTO(List<Long> systemId, List<Long> defectSource, List<Long> emergencyLevel, String startTime, String endTime) {
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
