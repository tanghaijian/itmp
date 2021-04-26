package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 *@author liushan
 *@Description 缺陷实体类
 *@Date 2020/8/7
 *@return
 **/
@TableName("tbl_defect_info")
public class TblDefectInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long testTaskId;// 测试任务表（工作任务）表主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId; // 系统id

    @JsonSerialize(using = ToStringSerializer.class)
    private Long testSetCaseExecuteId;// 测试案例执行

    @PropertyInfo(name = "\u6d4b\u8bd5\u6848\u4f8b", length = 15)
    @JsonSerialize(using = ToStringSerializer.class)
    private String testSetCaseExecuteName;// 测试案例执行名称

    @PropertyInfo(name = "\u6848\u4f8b\u7f16\u53f7", length = 15)
    private String caseNumber; // 案例编号

    @PropertyInfo(name = "\u7f3a\u9677\u7f16\u53f7", length = 15)
    private String defectCode;// 缺陷编号

    @PropertyInfo(name = "\u7f3a\u9677\u6458\u8981", length = 15)
    private String defectSummary;// 缺陷摘要

    @PropertyInfo(name = "\u7f3a\u9677\u7c7b\u578b", length = 15)
    private Integer defectType;// 缺陷类型

    @PropertyInfo(name = "\u7f3a\u9677\u72b6\u6001", length = 15)
    private Integer defectStatus;// 缺陷状态

    @PropertyInfo(name = "\u4fee\u590d\u8f6e\u6b21", length = 15)
    private Integer repairRound;// 修复轮次
    
    @TableField(exist = false)
    private String repairRoundStr;//修复轮次

    @PropertyInfo(name = "\u7f3a\u9677\u6765\u6e90", length = 15)
    private Integer defectSource;// 缺陷来源

    @PropertyInfo(name = "\u4e25\u91cd\u7ea7\u522b", length = 15)
    private Integer severityLevel;// 严重级别

    @PropertyInfo(name = "\u7d27\u6025\u7a0b\u5ea6", length = 15)
    private Integer emergencyLevel;// 紧急程度

    @PropertyInfo(name = "\u9a73\u56de\u7406\u7531", length = 15)
    private Integer rejectReason;// 驳回理由

    @PropertyInfo(name = "\u89e3\u51b3\u60c5\u51b5\u72b6\u6001", length = 15)
    private Integer solveStatus;// 解决情况状态

    @PropertyInfo(name = "\u7f3a\u9677\u63cf\u8ff0", length = 15,isDefaultValue = true)
    private String defectOverview;// 缺陷描述

    @PropertyInfo(name = "\u5907\u6ce8", length = 15)
    private String remark; //缺陷备注

    @PropertyInfo(name = "\u63d0\u4ea4\u4eba", length = 15)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long submitUserId;// 提交人（用户表主键）

    @JsonSerialize(using = ToStringSerializer.class)
    private Long testUserId;//测试人员

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date submitDate;// 提交日期

    @JsonSerialize(using = ToStringSerializer.class)
    private Long assignUserId;// 指派处理人（用户表主键）

    @PropertyInfo(name = "\u9700\u6c42\u7f16\u53f7", length = 15)
    private String requirementCode; // 需求编号

    @JsonSerialize(using = ToStringSerializer.class)
    private Long commissioningWindowId; // 投产窗口

    @PropertyInfo(name = "扩展字段")
    private String fieldTemplate;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long developUserId;//开发人员

    private Long projectGroupId; //项目小组

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp closeTime; //关闭时间

    private Long assetSystemTreeId; //模块（资产系统树表主键）

    private Long detectedSystemVersionId; //发现版本号（系统版本表主键）

    private Long repairSystemVersionId; //修复版本号（系统版本表主键）

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date expectRepairDate; //期望修复日期

    private Double estimateWorkload; //估计工作量

    private String rootCauseAnalysis; //根本原因描述

    private Integer createType; //创建方式
    private Timestamp checkTime; //确认时间
    private String problenUrl; //问题链接

    @PropertyInfo(name = "发现环境")
    private Integer discoveryEnvironment;
    /*--------------------非表字段---------------------*/
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementId; // 需求id

    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long oldAssignUserId; // 需求id

    @TableField(exist = false)
    private String requirementName;// 需求名称

    @PropertyInfo(name = "\u7cfb\u7edf\u540d\u79f0", length = 15)
    @TableField(exist = false)
    private String systemName;// 系统名称

    @TableField(exist = false)
    private String systemCode;// 系统编码

    @PropertyInfo(name = "\u6295\u4ea7\u7a97\u53e3", length = 15)
    @TableField(exist = false)
    private String windowName;// 投产窗口

    @TableField(exist = false)
    private String submitUserName;// 提交人名称

    @PropertyInfo(name = "\u5de5\u4f5c\u4efb\u52a1\u6458\u8981", length = 15)
    @TableField(exist = false)
    private String testTaskName;// 工作任务名称

    @TableField(exist = false)
    private Long testStage;// 测试阶段（数据字典，1:系测，2:版测）

    @TableField(exist = false)
    private String testCaseName;// 案例名称

    @PropertyInfo(name = "\u4e3b\u4fee\u590d\u4eba", length = 15)
    @TableField(exist = false)
    private String assignUserName;//转派人名称

    @TableField(exist = false)
    private String featureName;//测试任务名称

    @TableField(exist = false)
    private String testTaskCode;// 工作任务编号
    @Transient
    private Set<Long> userSet; //系统关联的项目的测试管理岗和缺陷关联的测试任务的管理岗的人员集合
    @Transient
    private String flag;

    @PropertyInfo(name = "\u6d4b\u8bd5\u4eba", length = 15)
    @Transient
    private String testUserName;//测试人名称

    @Transient
    private Long featureId; //测试任务id
    @Transient
    private String featureCode; //测试任务编号

    @Transient
    @PropertyInfo(name = "\u5f00\u53d1\u4eba\u5458", length = 15)
    private String developUserName;//开发人名称

    @TableField(exist = false)
    private String projectGroupName; //项目小组名称

    @TableField(exist = false)
    private String assetSystemTreeName; //模块（资产系统数名称）

    @TableField(exist = false)
    private String detectedSystemVersionName; //发现版本号名称

    @TableField(exist = false)
    private String repairSystemVersionName; //修复版本号名称

    private Long projectId;//项目id
    @TableField(exist = false)
    private String projectName;//项目名称
    @TableField(exist = false)
    private int projectType;//项目类型
    
    @TableField(exist = false)
    private String defectSourceStr;//缺陷来源
    @TableField(exist = false)
    private String severityLevelStr;//缺陷等级
    @TableField(exist = false)
    private String defectTypeStr;// 缺陷类型

    public Long getTestTaskId() {
        return testTaskId;
    }

    public void setTestTaskId(Long testTaskId) {
        this.testTaskId = testTaskId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getTestSetCaseExecuteId() {
        return testSetCaseExecuteId;
    }

    public void setTestSetCaseExecuteId(Long testSetCaseExecuteId) {
        this.testSetCaseExecuteId = testSetCaseExecuteId;
    }

    public String getTestSetCaseExecuteName() {
        return testSetCaseExecuteName;
    }

    public void setTestSetCaseExecuteName(String testSetCaseExecuteName) {
        this.testSetCaseExecuteName = testSetCaseExecuteName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getDefectCode() {
        return defectCode;
    }

    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    public Integer getDefectStatus() {
        return defectStatus;
    }

    public void setDefectStatus(Integer defectStatus) {
        this.defectStatus = defectStatus;
    }

    public Integer getRepairRound() {
        return repairRound;
    }

    public void setRepairRound(Integer repairRound) {
        this.repairRound = repairRound;
    }

    public String getDefectSummary() {
        return defectSummary;
    }

    public void setDefectSummary(String defectSummary) {
        this.defectSummary = defectSummary == null ? null : defectSummary.trim();
    }

    public Integer getDefectType() {
        return defectType;
    }

    public void setDefectType(Integer defectType) {
        this.defectType = defectType;
    }

    public Integer getDefectSource() {
        return defectSource;
    }

    public void setDefectSource(Integer defectSource) {
        this.defectSource = defectSource;
    }

    public Integer getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(Integer severityLevel) {
        this.severityLevel = severityLevel;
    }

    public Integer getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(Integer emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public Integer getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(Integer rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Integer getSolveStatus() {
        return solveStatus;
    }

    public void setSolveStatus(Integer solveStatus) {
        this.solveStatus = solveStatus;
    }

    public String getDefectOverview() {
        return defectOverview;
    }

    public void setDefectOverview(String defectOverview) {
        this.defectOverview = defectOverview == null ? null : defectOverview.trim();
    }

    public Long getSubmitUserId() {
        return submitUserId;
    }

    public void setSubmitUserId(Long submitUserId) {
        this.submitUserId = submitUserId;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Long getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Long assignUserId) {
        this.assignUserId = assignUserId;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public String getSubmitUserName() {
        return submitUserName;
    }

    public void setSubmitUserName(String submitUserName) {
        this.submitUserName = submitUserName;
    }

    public String getTestTaskName() {
        return testTaskName;
    }

    public void setTestTaskName(String testTaskName) {
        this.testTaskName = testTaskName;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getAssignUserName() {
        return assignUserName;
    }

    public void setAssignUserName(String assignUserName) {
        this.assignUserName = assignUserName;
    }

    public Long getTestStage() {
        return testStage;
    }

    public void setTestStage(Long testStage) {
        this.testStage = testStage;
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Long getOldAssignUserId() {
        return oldAssignUserId;
    }

    public void setOldAssignUserId(Long oldAssignUserId) {
        this.oldAssignUserId = oldAssignUserId;
    }

    public String getTestTaskCode() {
        return testTaskCode;
    }

    public void setTestTaskCode(String testTaskCode) {
        this.testTaskCode = testTaskCode;
    }

    public String getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(String fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

    public Set<Long> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<Long> userSet) {
        this.userSet = userSet;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getTestUserId() {
        return testUserId;
    }

    public void setTestUserId(Long testUserId) {
        this.testUserId = testUserId;
    }

    public String getTestUserName() {
        return testUserName;
    }

    public void setTestUserName(String testUserName) {
        this.testUserName = testUserName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Long getDevelopUserId() {
        return developUserId;
    }

    public void setDevelopUserId(Long developUserId) {
        this.developUserId = developUserId;
    }

    public String getDevelopUserName() {
        return developUserName;
    }

    public void setDevelopUserName(String developUserName) {
        this.developUserName = developUserName;
    }

    public Long getProjectGroupId() {
        return projectGroupId;
    }

    public void setProjectGroupId(Long projectGroupId) {
        this.projectGroupId = projectGroupId;
    }

    public Timestamp getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    public Long getAssetSystemTreeId() {
        return assetSystemTreeId;
    }

    public void setAssetSystemTreeId(Long assetSystemTreeId) {
        this.assetSystemTreeId = assetSystemTreeId;
    }

    public Long getDetectedSystemVersionId() {
        return detectedSystemVersionId;
    }

    public void setDetectedSystemVersionId(Long detectedSystemVersionId) {
        this.detectedSystemVersionId = detectedSystemVersionId;
    }

    public Long getRepairSystemVersionId() {
        return repairSystemVersionId;
    }

    public void setRepairSystemVersionId(Long repairSystemVersionId) {
        this.repairSystemVersionId = repairSystemVersionId;
    }

    public Date getExpectRepairDate() {
        return expectRepairDate;
    }

    public void setExpectRepairDate(Date expectRepairDate) {
        this.expectRepairDate = expectRepairDate;
    }

    public Double getEstimateWorkload() {
        return estimateWorkload;
    }

    public void setEstimateWorkload(Double estimateWorkload) {
        this.estimateWorkload = estimateWorkload;
    }

    public String getRootCauseAnalysis() {
        return rootCauseAnalysis;
    }

    public void setRootCauseAnalysis(String rootCauseAnalysis) {
        this.rootCauseAnalysis = rootCauseAnalysis;
    }

    public String getProjectGroupName() {
        return projectGroupName;
    }

    public void setProjectGroupName(String projectGroupName) {
        this.projectGroupName = projectGroupName;
    }

    public String getAssetSystemTreeName() {
        return assetSystemTreeName;
    }

    public void setAssetSystemTreeName(String assetSystemTreeName) {
        this.assetSystemTreeName = assetSystemTreeName;
    }

    public String getDetectedSystemVersionName() {
        return detectedSystemVersionName;
    }

    public void setDetectedSystemVersionName(String detectedSystemVersionName) {
        this.detectedSystemVersionName = detectedSystemVersionName;
    }

    public String getRepairSystemVersionName() {
        return repairSystemVersionName;
    }

    public void setRepairSystemVersionName(String repairSystemVersionName) {
        this.repairSystemVersionName = repairSystemVersionName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    public String getProblenUrl() {
        return problenUrl;
    }
    public void setProblenUrl(String problenUrl) {
        this.problenUrl = problenUrl;
    }

    public Integer getCreateType() {
        return createType;
    }
    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectType() {
        return projectType;
    }
    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }
    
    

    public String getRepairRoundStr() {
		return repairRoundStr;
	}

	public void setRepairRoundStr(String repairRoundStr) {
		this.repairRoundStr = repairRoundStr;
	}

	
	
	public String getDefectSourceStr() {
		return defectSourceStr;
	}

	public void setDefectSourceStr(String defectSourceStr) {
		this.defectSourceStr = defectSourceStr;
	}

	public String getSeverityLevelStr() {
		return severityLevelStr;
	}

	public void setSeverityLevelStr(String severityLevelStr) {
		this.severityLevelStr = severityLevelStr;
	}

	public String getDefectTypeStr() {
		return defectTypeStr;
	}

	public void setDefectTypeStr(String defectTypeStr) {
		this.defectTypeStr = defectTypeStr;
	}

    public Integer getDiscoveryEnvironment() {
        return discoveryEnvironment;
    }
    public void setDiscoveryEnvironment(Integer discoveryEnvironment) {
        this.discoveryEnvironment = discoveryEnvironment;
    }

    @Override
    public String toString() {
        return "TblDefectInfo{" +
                "testTaskId=" + testTaskId +
                ", systemId=" + systemId +
                ", testSetCaseExecuteId=" + testSetCaseExecuteId +
                ", testSetCaseExecuteName=" + testSetCaseExecuteName +
                ", caseNumber='" + caseNumber + '\'' +
                ", defectCode='" + defectCode + '\'' +
                ", defectSummary='" + defectSummary + '\'' +
                ", defectType=" + defectType +
                ", defectStatus=" + defectStatus +
                ", repairRound=" + repairRound +
                ", defectSource=" + defectSource +
                ", severityLevel=" + severityLevel +
                ", emergencyLevel=" + emergencyLevel +
                ", rejectReason=" + rejectReason +
                ", solveStatus=" + solveStatus +
                ", defectOverview='" + defectOverview + '\'' +
                ", remark='" + remark + '\'' +
                ", submitUserId=" + submitUserId +
                ", testUserId=" + testUserId +
                ", submitDate=" + submitDate +
                ", assignUserId=" + assignUserId +
                ", requirementCode='" + requirementCode + '\'' +
                ", commissioningWindowId=" + commissioningWindowId +
                ", fieldTemplate='" + fieldTemplate + '\'' +
                ", developUserId=" + developUserId +
                ", projectGroupId=" + projectGroupId +
                ", closeTime=" + closeTime +
                ", assetSystemTreeId=" + assetSystemTreeId +
                ", detectedSystemVersionId=" + detectedSystemVersionId +
                ", repairSystemVersionId=" + repairSystemVersionId +
                ", expectRepairDate=" + expectRepairDate +
                ", estimateWorkload=" + estimateWorkload +
                ", rootCauseAnalysis='" + rootCauseAnalysis + '\'' +
                ", createType=" + createType +
                ", checkTime=" + checkTime +
                ", problenUrl='" + problenUrl + '\'' +
                ", requirementId=" + requirementId +
                ", oldAssignUserId=" + oldAssignUserId +
                ", requirementName='" + requirementName + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", windowName='" + windowName + '\'' +
                ", submitUserName='" + submitUserName + '\'' +
                ", testTaskName='" + testTaskName + '\'' +
                ", testStage=" + testStage +
                ", testCaseName='" + testCaseName + '\'' +
                ", assignUserName='" + assignUserName + '\'' +
                ", featureName='" + featureName + '\'' +
                ", testTaskCode='" + testTaskCode + '\'' +
                ", userSet=" + userSet +
                ", flag='" + flag + '\'' +
                ", testUserName='" + testUserName + '\'' +
                ", featureId=" + featureId +
                ", featureCode='" + featureCode + '\'' +
                ", developUserName='" + developUserName + '\'' +
                ", projectGroupName='" + projectGroupName + '\'' +
                ", assetSystemTreeName='" + assetSystemTreeName + '\'' +
                ", detectedSystemVersionName='" + detectedSystemVersionName + '\'' +
                ", repairSystemVersionName='" + repairSystemVersionName + '\'' +
                '}';
    }
}