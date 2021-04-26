package cn.pioneeruniverse.project.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * 项目计划（燃尽图）
 */
public class ProjectPlanNewDTO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;
    private String planNewStartDate; //计划开始日期
    private String planNewEndDate; //计划开始日期
    private Date createDate; //创建日期
    private Double estimateWorkload;   //工时
    private Double estimateRemainWorkload;  //实际工作量
    private String Date;    //日期
    private Double countWorkload;   //工时
    private Integer weed;   //冲刺天数
    private Long projectId; //项目id
    private Long sprintId;  //冲刺id
    private List<Long> sprintListId;
    public String getPlanNewStartDate() {
        return planNewStartDate;
    }

    public void setPlanNewStartDate(String planNewStartDate) {
        this.planNewStartDate = planNewStartDate;
    }

    public String getPlanNewEndDate() {
        return planNewEndDate;
    }

    public void setPlanNewEndDate(String planNewEndDate) {
        this.planNewEndDate = planNewEndDate;
    }

    public Double getEstimateWorkload() {
        return estimateWorkload;
    }

    public void setEstimateWorkload(Double estimateWorkload) {
        this.estimateWorkload = estimateWorkload;
    }

    public Double getEstimateRemainWorkload() {
        return estimateRemainWorkload;
    }

    public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
        this.estimateRemainWorkload = estimateRemainWorkload;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Double getCountWorkload() {
        return countWorkload;
    }

    public void setCountWorkload(Double countWorkload) {
        this.countWorkload = countWorkload;
    }

    public Integer getWeed() {
        return weed;
    }

    public void setWeed(Integer weed) {
        this.weed = weed;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public List<Long> getSprintListId() {
        return sprintListId;
    }

    public void setSprintListId(List<Long> sprintListId) {
        this.sprintListId = sprintListId;
    }

    @Override
    public String toString() {
        return "ProjectPlanNewDTO{" +
                "planNewStartDate='" + planNewStartDate + '\'' +
                ", planNewEndDate='" + planNewEndDate + '\'' +
                ", createDate=" + createDate +
                ", estimateWorkload=" + estimateWorkload +
                ", estimateRemainWorkload=" + estimateRemainWorkload +
                ", Date='" + Date + '\'' +
                ", countWorkload=" + countWorkload +
                ", weed=" + weed +
                ", projectId=" + projectId +
                ", sprintId=" + sprintId +
                ", sprintListId=" + sprintListId +
                '}';
    }
}
