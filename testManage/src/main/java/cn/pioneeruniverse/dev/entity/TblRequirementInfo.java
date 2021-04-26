package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.dev.vo.RequirementVo;
@TableName("tbl_requirement_info")
public class TblRequirementInfo extends RequirementVo{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String requirementName;

    private String requirementCode;

    private String requirementStatus;

    private String requirementSource;

    private String requirementType;
    @TableField(exist = false)
   	private	List<String> requirementTypeList;
   

	private Long applyUserId;

    private Long applyDeptId;

    private Long developmentManageUserId;

    private Long developmentDeptId;

    private Long requirementManageUserId;

    private Long requirementAcceptanceUserId;

    private String requirementOverview;

    private String requirementPriority;

    private String requirementPlan;

    private Date expectOnlineDate;

    private Date planOnlineDate;

    private Date actualOnlineDate;

    private Date planIntegrationTestDate;

    private Date actualIntegrationTestDate;

    private Date openDate;

    private String importantRequirementStatus;

    private String importantRequirementDelayStatus;

    private String importantRequirementType;

    private String importantRequirementOnlineQuarter;

    private String importantRequirementDelayReason;

    private Byte directIncome;

    private Byte forwardIncome;

    private Byte recessiveIncome;

    private Byte directCostReduction;

    private Byte forwardCostReduction;

    private String anticipatedIncome;

    private String estimateCost;

    private String hangupStatus;

    private Date hangupDate;

    private Byte changeCount;

    private String requirementProperty;

    private String requirementClassify;

    private String requirementSubdivision;

    private String acceptanceDescription;

    private String acceptanceTimeliness;

    private String dataMigrationStatus;

    private Double workload;

    private Long parentId;

    private String parentIds;

 
   

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName == null ? null : requirementName.trim();
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode == null ? null : requirementCode.trim();
    }

    public String getRequirementStatus() {
        return requirementStatus;
    }

    public void setRequirementStatus(String requirementStatus) {
        this.requirementStatus = requirementStatus == null ? null : requirementStatus.trim();
    }

    public String getRequirementSource() {
        return requirementSource;
    }

    public void setRequirementSource(String requirementSource) {
        this.requirementSource = requirementSource == null ? null : requirementSource.trim();
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType == null ? null : requirementType.trim();
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Long getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(Long applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public Long getDevelopmentManageUserId() {
        return developmentManageUserId;
    }

    public void setDevelopmentManageUserId(Long developmentManageUserId) {
        this.developmentManageUserId = developmentManageUserId;
    }

    public Long getDevelopmentDeptId() {
        return developmentDeptId;
    }

    public void setDevelopmentDeptId(Long developmentDeptId) {
        this.developmentDeptId = developmentDeptId;
    }

    public Long getRequirementManageUserId() {
        return requirementManageUserId;
    }

    public void setRequirementManageUserId(Long requirementManageUserId) {
        this.requirementManageUserId = requirementManageUserId;
    }

    public Long getRequirementAcceptanceUserId() {
        return requirementAcceptanceUserId;
    }

    public void setRequirementAcceptanceUserId(Long requirementAcceptanceUserId) {
        this.requirementAcceptanceUserId = requirementAcceptanceUserId;
    }

    public String getRequirementOverview() {
        return requirementOverview;
    }

    public void setRequirementOverview(String requirementOverview) {
        this.requirementOverview = requirementOverview == null ? null : requirementOverview.trim();
    }

    public String getRequirementPriority() {
        return requirementPriority;
    }

    public void setRequirementPriority(String requirementPriority) {
        this.requirementPriority = requirementPriority == null ? null : requirementPriority.trim();
    }

    public String getRequirementPlan() {
        return requirementPlan;
    }

    public void setRequirementPlan(String requirementPlan) {
        this.requirementPlan = requirementPlan == null ? null : requirementPlan.trim();
    }

    public Date getExpectOnlineDate() {
        return expectOnlineDate;
    }

    public void setExpectOnlineDate(Date expectOnlineDate) {
        this.expectOnlineDate = expectOnlineDate;
    }

    public Date getPlanOnlineDate() {
        return planOnlineDate;
    }

    public void setPlanOnlineDate(Date planOnlineDate) {
        this.planOnlineDate = planOnlineDate;
    }

    public Date getActualOnlineDate() {
        return actualOnlineDate;
    }

    public void setActualOnlineDate(Date actualOnlineDate) {
        this.actualOnlineDate = actualOnlineDate;
    }

    public Date getPlanIntegrationTestDate() {
        return planIntegrationTestDate;
    }

    public void setPlanIntegrationTestDate(Date planIntegrationTestDate) {
        this.planIntegrationTestDate = planIntegrationTestDate;
    }

    public Date getActualIntegrationTestDate() {
        return actualIntegrationTestDate;
    }

    public void setActualIntegrationTestDate(Date actualIntegrationTestDate) {
        this.actualIntegrationTestDate = actualIntegrationTestDate;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getImportantRequirementStatus() {
        return importantRequirementStatus;
    }

    public void setImportantRequirementStatus(String importantRequirementStatus) {
        this.importantRequirementStatus = importantRequirementStatus == null ? null : importantRequirementStatus.trim();
    }

    public String getImportantRequirementDelayStatus() {
        return importantRequirementDelayStatus;
    }

    public void setImportantRequirementDelayStatus(String importantRequirementDelayStatus) {
        this.importantRequirementDelayStatus = importantRequirementDelayStatus == null ? null : importantRequirementDelayStatus.trim();
    }

    public String getImportantRequirementType() {
        return importantRequirementType;
    }

    public void setImportantRequirementType(String importantRequirementType) {
        this.importantRequirementType = importantRequirementType == null ? null : importantRequirementType.trim();
    }

    public String getImportantRequirementOnlineQuarter() {
        return importantRequirementOnlineQuarter;
    }

    public void setImportantRequirementOnlineQuarter(String importantRequirementOnlineQuarter) {
        this.importantRequirementOnlineQuarter = importantRequirementOnlineQuarter == null ? null : importantRequirementOnlineQuarter.trim();
    }

    public String getImportantRequirementDelayReason() {
        return importantRequirementDelayReason;
    }

    public void setImportantRequirementDelayReason(String importantRequirementDelayReason) {
        this.importantRequirementDelayReason = importantRequirementDelayReason == null ? null : importantRequirementDelayReason.trim();
    }

    public Byte getDirectIncome() {
        return directIncome;
    }

    public void setDirectIncome(Byte directIncome) {
        this.directIncome = directIncome;
    }

    public Byte getForwardIncome() {
        return forwardIncome;
    }

    public void setForwardIncome(Byte forwardIncome) {
        this.forwardIncome = forwardIncome;
    }

    public Byte getRecessiveIncome() {
        return recessiveIncome;
    }

    public void setRecessiveIncome(Byte recessiveIncome) {
        this.recessiveIncome = recessiveIncome;
    }

    public Byte getDirectCostReduction() {
        return directCostReduction;
    }

    public void setDirectCostReduction(Byte directCostReduction) {
        this.directCostReduction = directCostReduction;
    }

    public Byte getForwardCostReduction() {
        return forwardCostReduction;
    }

    public void setForwardCostReduction(Byte forwardCostReduction) {
        this.forwardCostReduction = forwardCostReduction;
    }

    public String getAnticipatedIncome() {
        return anticipatedIncome;
    }

    public void setAnticipatedIncome(String anticipatedIncome) {
        this.anticipatedIncome = anticipatedIncome == null ? null : anticipatedIncome.trim();
    }

    public String getEstimateCost() {
        return estimateCost;
    }

    public void setEstimateCost(String estimateCost) {
        this.estimateCost = estimateCost == null ? null : estimateCost.trim();
    }

    public String getHangupStatus() {
        return hangupStatus;
    }

    public void setHangupStatus(String hangupStatus) {
        this.hangupStatus = hangupStatus == null ? null : hangupStatus.trim();
    }

    public Date getHangupDate() {
        return hangupDate;
    }

    public void setHangupDate(Date hangupDate) {
        this.hangupDate = hangupDate;
    }

    public Byte getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Byte changeCount) {
        this.changeCount = changeCount;
    }

    public String getRequirementProperty() {
        return requirementProperty;
    }

    public void setRequirementProperty(String requirementProperty) {
        this.requirementProperty = requirementProperty == null ? null : requirementProperty.trim();
    }

    public String getRequirementClassify() {
        return requirementClassify;
    }

    public void setRequirementClassify(String requirementClassify) {
        this.requirementClassify = requirementClassify == null ? null : requirementClassify.trim();
    }

    public String getRequirementSubdivision() {
        return requirementSubdivision;
    }

    public void setRequirementSubdivision(String requirementSubdivision) {
        this.requirementSubdivision = requirementSubdivision == null ? null : requirementSubdivision.trim();
    }

    public String getAcceptanceDescription() {
        return acceptanceDescription;
    }

    public void setAcceptanceDescription(String acceptanceDescription) {
        this.acceptanceDescription = acceptanceDescription == null ? null : acceptanceDescription.trim();
    }

    public String getAcceptanceTimeliness() {
        return acceptanceTimeliness;
    }

    public void setAcceptanceTimeliness(String acceptanceTimeliness) {
        this.acceptanceTimeliness = acceptanceTimeliness == null ? null : acceptanceTimeliness.trim();
    }

    public String getDataMigrationStatus() {
        return dataMigrationStatus;
    }

    public void setDataMigrationStatus(String dataMigrationStatus) {
        this.dataMigrationStatus = dataMigrationStatus == null ? null : dataMigrationStatus.trim();
    }

    public Double getWorkload() {
        return workload;
    }

    public void setWorkload(Double workload) {
        this.workload = workload;
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
    public List<String> getRequirementTypeList() {
		return requirementTypeList;
	}

	public void setRequirementTypeList(List<String> requirementTypeList) {
		this.requirementTypeList = requirementTypeList;
	}
}