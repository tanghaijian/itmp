package cn.pioneeruniverse.project.dto;

import java.io.Serializable;

/**
 * 任务实施
 */
public class TaskForceDTO implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
    private Integer requirementFeatureStatus;   //任务状态
    private Integer count;  //数量
    private Integer taskCount;  //总任务数
    private Integer toImplement;    //待实施
    private Integer inImplementation;   //实施中
    private Integer offTheStocks;   //已完成
    private Integer postponed;  //已延期
    private Integer canceled;   //已取消

    public Integer getRequirementFeatureStatus() {
        return requirementFeatureStatus;
    }

    public void setRequirementFeatureStatus(Integer requirementFeatureStatus) {
        this.requirementFeatureStatus = requirementFeatureStatus;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getToImplement() {
        return toImplement;
    }

    public void setToImplement(Integer toImplement) {
        this.toImplement = toImplement;
    }

    public Integer getInImplementation() {
        return inImplementation;
    }

    public void setInImplementation(Integer inImplementation) {
        this.inImplementation = inImplementation;
    }

    public Integer getOffTheStocks() {
        return offTheStocks;
    }

    public void setOffTheStocks(Integer offTheStocks) {
        this.offTheStocks = offTheStocks;
    }

    public Integer getPostponed() {
        return postponed;
    }

    public void setPostponed(Integer postponed) {
        this.postponed = postponed;
    }

    public Integer getCanceled() {
        return canceled;
    }

    public void setCanceled(Integer canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        return "TaskForceDTO{" +
                "requirementFeatureStatus=" + requirementFeatureStatus +
                ", count=" + count +
                ", taskCount=" + taskCount +
                ", toImplement=" + toImplement +
                ", inImplementation=" + inImplementation +
                ", offTheStocks=" + offTheStocks +
                ", postponed=" + postponed +
                ", canceled=" + canceled +
                '}';
    }
}
