package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @deprecated
* @ClassName: TblProjectSchedulePlanning
* @Description: 项目进度计划（未用）
* @author author
* @date 2020年8月24日 下午3:52:55
*
 */
@TableName("tbl_project_schedule_planning")
public class TblProjectSchedulePlanning extends BaseEntity {

	private static final long serialVersionUID = 1L;

    private Long projectId;

    private Long requirementFeatureId;

    private Date planStartDate;

    private Date planEndDate;

    private Double planDuration;

    private Double planWorkload;

    private Date actualStartDate;

    private Date actualEndDate;

    private Long parentId;

    private String parentIds;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
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

    public Double getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(Double planDuration) {
        this.planDuration = planDuration;
    }

    public Double getPlanWorkload() {
        return planWorkload;
    }

    public void setPlanWorkload(Double planWorkload) {
        this.planWorkload = planWorkload;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds == null ? null : parentIds.trim();
    }

}