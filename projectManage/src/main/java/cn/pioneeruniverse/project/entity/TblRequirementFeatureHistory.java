package cn.pioneeruniverse.project.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblRequirementFeatureHistory
* @Description:开发任务历史bean
* @author author
* @date 2020年8月31日 上午11:10:28
*
 */
@TableName("tbl_requirement_feature_history")
public class TblRequirementFeatureHistory {
    private Long id;

    private Long requirementFeatureId;//开发任务ID

    private Long assetSystemTreeId;//模块，对应的系统资产树的ID

    private Double estimateRemainWorkload;//预估剩余工作量

    private Long commissioningWindowId; //投产窗口ID

    private Long sprintId; //关联的冲刺ID

    private Long projectPlanId; //项目计划ID

    private String requirementFeatureStatus;//开发任务状态

    private Integer status;//有效性状态

    private Date createDate; //创建日期

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public Long getAssetSystemTreeId() {
        return assetSystemTreeId;
    }

    public void setAssetSystemTreeId(Long assetSystemTreeId) {
        this.assetSystemTreeId = assetSystemTreeId;
    }

    public Double getEstimateRemainWorkload() {
        return estimateRemainWorkload;
    }

    public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
        this.estimateRemainWorkload = estimateRemainWorkload;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Long getProjectPlanId() {
        return projectPlanId;
    }

    public void setProjectPlanId(Long projectPlanId) {
        this.projectPlanId = projectPlanId;
    }

    public String getRequirementFeatureStatus() {
        return requirementFeatureStatus;
    }

    public void setRequirementFeatureStatus(String requirementFeatureStatus) {
        this.requirementFeatureStatus = requirementFeatureStatus == null ? null : requirementFeatureStatus.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}