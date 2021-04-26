package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * @Author:
 * @Description: 开发任务表实体表
 * @Date: Created in 15:06 2020/08/25
 * @Modified By:
 */
public class TblRequirementFeature extends BaseEntity {

	private static final long serialVersionUID = 1L;

    private String featureName; //任务名称
    private String featureOverview; //任务描述
    private Long projectId;  //项目ID
    private Long systemId;  //系统ID
    private Long requirementId;  //需求ID
    private Long commissioningWindowId;   //投产窗口ID

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName == null ? null : featureName.trim();
    }

    public String getFeatureOverview() {
        return featureOverview;
    }

    public void setFeatureOverview(String featureOverview) {
        this.featureOverview = featureOverview == null ? null : featureOverview.trim();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }	

}