package cn.pioneeruniverse.project.vo;

import java.io.Serializable;

public class ProjectPlanVO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;

    /**
     * 项目计划ID
     **/
    private Long planId;//项目计划ID

    /**
     * 计划名
     **/
    private String PlanName;//计划名

    /**
     * 冲刺ID
     **/
    private Long sprintId;//冲刺ID

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }
}
