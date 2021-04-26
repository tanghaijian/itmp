package cn.pioneeruniverse.project.dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * 里程碑DTO
 */
public class ProjectPlanDTO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
    private Long projectId; //项目id
    private String planName;    //项目名称
    private Date planStartDate; //项目计划开始时间
    private Date planEndDate; //项目计划结束时间
    private Integer currentProgress;  //当前进度

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Integer getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ProjectPlanDTO{" +
                "projectId=" + projectId +
                ", planName='" + planName + '\'' +
                ", planStartDate=" + planStartDate +
                ", planEndDate=" + planEndDate +
                ", currentProgress=" + currentProgress +
                '}';
    }
}
