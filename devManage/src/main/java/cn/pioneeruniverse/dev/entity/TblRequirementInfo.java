package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


@TableName("tblRequirementInfo")
public class TblRequirementInfo extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*基本信息*/
    private String requirementName;    //需求名称
    private String requirementCode;    //需求编码
    private String requirementStatus;    //需求状态(数据字典)
    private String requirementSource;    //需求来源(数据字典)
    private String requirementType;        //需求类型(数据字典)
    @TableField(exist = false)
    private List<String> requirementTypeList;
    private String requirementPlan;        //需求计划(数据字典)
    private String requirementPriority;    //需求优先级(数据字典)
    private Date expectOnlineDate;    //期望上线日期
    private Date planOnlineDate;    //计划上线日期
    private Date actualOnlineDate;    //实际上线日期
    private Long applyUserId;    //提出人(人员表主键)
    private Long applyDeptId;    //归属部门(部门表主键)
    private Long developmentDeptId;    //开发部门(部门表主键)
    private Date openDate;    //开启日期
    private String requirementOverview;    //需求描述

    /*重点需求相关*/
    private String importantRequirementStatus;        //是否重点需求(1=是，2=否)
    private String importantRequirementType;        //重点需求类型
    private String importantRequirementDelayStatus;    //重点需求是否延误(1=是，2=否)
    private String importantRequirementOnlineQuarter;    //重点需求上线季度
    private String importantRequirementDelayReason;    //重点需求延误原因

    /*成本与收益*/
    private Long directIncome;    //直接收益
    private Long forwardIncome;    //远期收益
    private Long recessiveIncome;    //隐性收益
    private Long directCostReduction;    //直接成本节约
    private Long forwardCostReduction;    //远期成本节约
    private String anticipatedIncome;    //预期收益
    private String estimateCost;    //预估成本

    /*其他信息*/
    private String hangupStatus;    //需求是否挂起(1=是；2=否)
    private Date hangupDate;    //需求挂起日期
    private Long changeCount;    //变更次数
    private Long developmentManageUserId;    //开发管理人员(用户表主键)
    private Long requirementManageUserId;    //需求管理人员(用户表主键)
    private Long requirementAcceptanceUserId;    //需求验收人员(用户表主键)
    private String requirementProperty;    //需求性质
    private String requirementClassify;    //需求分类
    private String requirementSubdivision;    //细分类型
    private Date planIntegrationTestDate;    //计划联调日期
    private Date actualIntegrationTestDate;    //实际联调日期
    private String acceptanceDescription;    //验收描述
    private String acceptanceTimeliness;    //验收时效
    private String dataMigrationStatus;    //是否数据迁移(状态 1=是；2=否)
    private double workload;    //工作量

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public List<String> getRequirementTypeList() {
        return requirementTypeList;
    }

    public void setRequirementTypeList(List<String> requirementTypeList) {
        this.requirementTypeList = requirementTypeList;
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public String getRequirementStatus() {
        return requirementStatus;
    }

    public void setRequirementStatus(String requirementStatus) {
        this.requirementStatus = requirementStatus;
    }

    public String getRequirementSource() {
        return requirementSource;
    }

    public void setRequirementSource(String requirementSource) {
        this.requirementSource = requirementSource;
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

    public String getRequirementPlan() {
        return requirementPlan;
    }

    public void setRequirementPlan(String requirementPlan) {
        this.requirementPlan = requirementPlan;
    }

    public String getRequirementPriority() {
        return requirementPriority;
    }

    public void setRequirementPriority(String requirementPriority) {
        this.requirementPriority = requirementPriority;
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

    public Long getDevelopmentDeptId() {
        return developmentDeptId;
    }

    public void setDevelopmentDeptId(Long developmentDeptId) {
        this.developmentDeptId = developmentDeptId;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getRequirementOverview() {
        return requirementOverview;
    }

    public void setRequirementOverview(String requirementOverview) {
        this.requirementOverview = requirementOverview;
    }

    public String getImportantRequirementStatus() {
        return importantRequirementStatus;
    }

    public void setImportantRequirementStatus(String importantRequirementStatus) {
        this.importantRequirementStatus = importantRequirementStatus;
    }

    public String getImportantRequirementType() {
        return importantRequirementType;
    }

    public void setImportantRequirementType(String importantRequirementType) {
        this.importantRequirementType = importantRequirementType;
    }

    public String getImportantRequirementDelayStatus() {
        return importantRequirementDelayStatus;
    }

    public void setImportantRequirementDelayStatus(String importantRequirementDelayStatus) {
        this.importantRequirementDelayStatus = importantRequirementDelayStatus;
    }

    public String getImportantRequirementOnlineQuarter() {
        return importantRequirementOnlineQuarter;
    }

    public void setImportantRequirementOnlineQuarter(String importantRequirementOnlineQuarter) {
        this.importantRequirementOnlineQuarter = importantRequirementOnlineQuarter;
    }

    public String getImportantRequirementDelayReason() {
        return importantRequirementDelayReason;
    }

    public void setImportantRequirementDelayReason(String importantRequirementDelayReason) {
        this.importantRequirementDelayReason = importantRequirementDelayReason;
    }

    public Long getDirectIncome() {
        return directIncome;
    }

    public void setDirectIncome(Long directIncome) {
        this.directIncome = directIncome;
    }

    public Long getForwardIncome() {
        return forwardIncome;
    }

    public void setForwardIncome(Long forwardIncome) {
        this.forwardIncome = forwardIncome;
    }

    public Long getRecessiveIncome() {
        return recessiveIncome;
    }

    public void setRecessiveIncome(Long recessiveIncome) {
        this.recessiveIncome = recessiveIncome;
    }

    public Long getDirectCostReduction() {
        return directCostReduction;
    }

    public void setDirectCostReduction(Long directCostReduction) {
        this.directCostReduction = directCostReduction;
    }

    public Long getForwardCostReduction() {
        return forwardCostReduction;
    }

    public void setForwardCostReduction(Long forwardCostReduction) {
        this.forwardCostReduction = forwardCostReduction;
    }

    public String getAnticipatedIncome() {
        return anticipatedIncome;
    }

    public void setAnticipatedIncome(String anticipatedIncome) {
        this.anticipatedIncome = anticipatedIncome;
    }

    public String getEstimateCost() {
        return estimateCost;
    }

    public void setEstimateCost(String estimateCost) {
        this.estimateCost = estimateCost;
    }

    public String getHangupStatus() {
        return hangupStatus;
    }

    public void setHangupStatus(String hangupStatus) {
        this.hangupStatus = hangupStatus;
    }

    public Date getHangupDate() {
        return hangupDate;
    }

    public void setHangupDate(Date hangupDate) {
        this.hangupDate = hangupDate;
    }

    public Long getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Long changeCount) {
        this.changeCount = changeCount;
    }

    public Long getDevelopmentManageUserId() {
        return developmentManageUserId;
    }

    public void setDevelopmentManageUserId(Long developmentManageUserId) {
        this.developmentManageUserId = developmentManageUserId;
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

    public String getRequirementProperty() {
        return requirementProperty;
    }

    public void setRequirementProperty(String requirementProperty) {
        this.requirementProperty = requirementProperty;
    }

    public String getRequirementClassify() {
        return requirementClassify;
    }

    public void setRequirementClassify(String requirementClassify) {
        this.requirementClassify = requirementClassify;
    }

    public String getRequirementSubdivision() {
        return requirementSubdivision;
    }

    public void setRequirementSubdivision(String requirementSubdivision) {
        this.requirementSubdivision = requirementSubdivision;
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

    public String getAcceptanceDescription() {
        return acceptanceDescription;
    }

    public void setAcceptanceDescription(String acceptanceDescription) {
        this.acceptanceDescription = acceptanceDescription;
    }

    public String getAcceptanceTimeliness() {
        return acceptanceTimeliness;
    }

    public void setAcceptanceTimeliness(String acceptanceTimeliness) {
        this.acceptanceTimeliness = acceptanceTimeliness;
    }

    public String getDataMigrationStatus() {
        return dataMigrationStatus;
    }

    public void setDataMigrationStatus(String dataMigrationStatus) {
        this.dataMigrationStatus = dataMigrationStatus;
    }

    public double getWorkload() {
        return workload;
    }

    public void setWorkload(double workload) {
        this.workload = workload;
    }
}