package cn.pioneeruniverse.common.entity;

import java.util.Date;
import java.util.Map;

import cn.pioneeruniverse.common.bean.PropertyInfo;

/**
 * jiraVO
 */
public class JiraDevelopmentVO extends BaseEntity {

    @PropertyInfo(name="任务编号")
    private String featureCode;

    @PropertyInfo(name="任务名称")
    private String featureName;

    @PropertyInfo(name="任务描述")
    private String featureOverview;

    @PropertyInfo(name="创建方式")
    private Integer createType; //创建方式（1=自建，2=同步）

    @PropertyInfo(name="任务优先级")
    private Integer requirementFeaturePriority;
    @PropertyInfo(name="任务优先级")
    private String requirementFeatureStatus;

    private String systemReqID;

    @PropertyInfo(name="管理岗")
    private Long manageUserId;
    private String UserCode;
    @PropertyInfo(name="执行人")
    private Long executeUserId;

    @PropertyInfo(name="模块")
    private Long assetSystemTreeId; //模块（资产系统树表主键）
    private String moduleName;
    private String moduleName1;

    @PropertyInfo(name="执行项目小组")
    private Long  executeProjectGroupId; //执行项目小组（项目小组表主键）
    private String projectGroupName;

    @PropertyInfo(name="预计开始时间")
    private Date planStartDate;

    @PropertyInfo(name="预计结束时间")
    private Date planEndDate;

    @PropertyInfo(name="预计工作量")
    private Double estimateWorkload;

    @PropertyInfo(name="预计剩余工作量")
    private Double estimateRemainWorkload;

    @PropertyInfo(name="修复版本")
    private Long repairSystemVersionId;
    private String repairSystemVersioName;

    @PropertyInfo(name="扩展字段")
    private String fieldTemplate;

    @PropertyInfo(name="系统ID")
    private Long systemId;
    private String systemCode;

    private Map[] maps;

    public String getFeatureName() {
        return featureName;
    }
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureCode() {
        return featureCode;
    }
    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getFeatureOverview() {
        return featureOverview;
    }
    public void setFeatureOverview(String featureOverview) {
        this.featureOverview = featureOverview;
    }

    public Integer getCreateType() {
        return createType;
    }
    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public Integer getRequirementFeaturePriority() {
        return requirementFeaturePriority;
    }
    public void setRequirementFeaturePriority(Integer requirementFeaturePriority) {
        this.requirementFeaturePriority = requirementFeaturePriority;
    }

    public Long getManageUserId() {
        return manageUserId;
    }
    public void setManageUserId(Long manageUserId) {
        this.manageUserId = manageUserId;
    }

    public Long getExecuteUserId() {
        return executeUserId;
    }
    public void setExecuteUserId(Long executeUserId) {
        this.executeUserId = executeUserId;
    }

    public Long getExecuteProjectGroupId() {
        return executeProjectGroupId;
    }
    public void setExecuteProjectGroupId(Long executeProjectGroupId) {
        this.executeProjectGroupId = executeProjectGroupId;
    }

    public Long getAssetSystemTreeId() {
        return assetSystemTreeId;
    }
    public void setAssetSystemTreeId(Long assetSystemTreeId) {
        this.assetSystemTreeId = assetSystemTreeId;
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

    public Long getRepairSystemVersionId() {
        return repairSystemVersionId;
    }
    public void setRepairSystemVersionId(Long repairSystemVersionId) {
        this.repairSystemVersionId = repairSystemVersionId;
    }

    public String getFieldTemplate() {
        return fieldTemplate;
    }
    public void setFieldTemplate(String fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public String getUserCode() {
        return UserCode;
    }
    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName1() {
        return moduleName1;
    }
    public void setModuleName1(String moduleName1) {
        this.moduleName1 = moduleName1;
    }

    public String getProjectGroupName() {
        return projectGroupName;
    }
    public void setProjectGroupName(String projectGroupName) {
        this.projectGroupName = projectGroupName;
    }

    public String getRepairSystemVersioName() {
        return repairSystemVersioName;
    }
    public void setRepairSystemVersioName(String repairSystemVersioName) {
        this.repairSystemVersioName = repairSystemVersioName;
    }

    public String getSystemReqID() {
        return systemReqID;
    }
    public void setSystemReqID(String systemReqID) {
        this.systemReqID = systemReqID;
    }

    public Long getSystemId() {
        return systemId;
    }
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Map[] getMaps() {
        return maps;
    }
    public void setMaps(Map[] maps) {
        this.maps = maps;
    }

    public String getRequirementFeatureStatus() {
        return requirementFeatureStatus;
    }
    public void setRequirementFeatureStatus(String requirementFeatureStatus) {
        this.requirementFeatureStatus = requirementFeatureStatus;
    }
}

