package cn.pioneeruniverse.dev.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *@author liushan
 *@Description 缺陷vo类
 *@Date 2020/8/7
 *@return
 **/
public class DefectInfoVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long testTaskId;// 测试任务表（工作任务）表主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long testSetCaseExecuteId;

    private String caseNumber;

    private String defectCode;// 缺陷编号

    private String defectSummary;// 缺陷摘要

    private Integer defectType;// 缺陷类型

    private Integer defectStatus;// 缺陷状态

    private Integer repairRound;// 修复轮次
    
    private String repairRoundStr;

    private Integer defectSource;// 缺陷来源
    
    private Integer severityLevel;// 严重级别

    private Integer emergencyLevel;// 紧急程度
    
    private Integer rejectReason;// 驳回理由

    private Integer solveStatus;// 解决情况状态

    private String defectOverview;// 缺陷描述

    @JsonSerialize(using = ToStringSerializer.class)
    private Long submitUserId;// 提交人（用户表主键）

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",locale="zh",timezone="GMT+8")
    private Date submitDate;// 提交日期

    @JsonSerialize(using = ToStringSerializer.class)
    private Long assignUserId;// 指派处理人（用户表主键）

    private String requirementCode; // 需求编号

    @JsonSerialize(using = ToStringSerializer.class)
    private Long commissioningWindowId; // 投产窗口

    @JsonSerialize(using = ToStringSerializer.class)
    private Long devTaskId; // 工作任务名称

    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementFeatureId; // 开发任务名称

    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementId; // 需求id

    private String statusName;// 状态名称

    private String requirementName;// 需求名称 

    private String systemName;// 系统名称

    private String windowName;// 投产窗口

    private String submitUserName;// 提交人名称
    
    private String testTaskName; //工作任务名称

    private String featureCode;// 测试任务编号
    
    private Long testStage;// 测试阶段（数据字典，1:系测，2:版测）

    private String testCaseName;// 案例名称

    private String assignUserName;//转派人名称

    private String featureName;// 需求特性表名称

    private String devTaskName;// 开发工作任务名称

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy年MM月dd日",locale="zh",timezone="GMT+8")
    private Date actualStartDate;// 实际开始时间

    private String defectStatusList;
    private Integer[] defectStatusForStr;

    private String submitUserIdStr;
    private Long[] submitUserIdList;

    private String systemIdStr;
    private Long[] systemIdList;

    private String requirementCodeStr;
    private String[] requirementCodeList;

    private String commissioningWindowIdStr;
    private Long[] commissioningWindowIdList;

    private String defectSourceStr;//缺陷来源
    private String severityLevelStr;//缺陷等级
    private String emergencyLevelStr;//紧急程度
    private String defectTypeStr;// 缺陷类型
    
    private String[] assignUserIds; //主修复人
    
    private String testUserName; //测试人
    
    private String[] testUserIds;
    
    
    private String testTaskCode;//工作任务编号
    
    private String developUserName;//开发人员
    
    private String[] developUserIds;
    
	private String sidx; //排序字段
	
	private String sord; //排序顺序

    private Long uid;  //用户id

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

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

    public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

    public String getDevTaskName() {
        return devTaskName;
    }

    public void setDevTaskName(String devTaskName) {
        this.devTaskName = devTaskName;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Long getDevTaskId() {
        return devTaskId;
    }

    public void setDevTaskId(Long devTaskId) {
        this.devTaskId = devTaskId;
    }

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public String getDefectStatusList() {
        return defectStatusList;
    }

    public void setDefectStatusList(String defectStatusList) {
        this.defectStatusList = defectStatusList;
        if(defectStatusList != null  && !defectStatusList.equals("")){
            String[] str = defectStatusList.split(",");
            this.defectStatusForStr = (Integer[]) ConvertUtils.convert(str,Integer.class);
        }
    }

    public Integer[] getDefectStatusForStr() {
        return defectStatusForStr;
    }

    public void setDefectStatusForStr(Integer[] defectStatusForStr) {
        this.defectStatusForStr = defectStatusForStr;
    }

    public String getSubmitUserIdStr() {
        return submitUserIdStr;
    }

    public void setSubmitUserIdStr(String submitUserIdStr) {
        this.submitUserIdStr = submitUserIdStr;
        if(submitUserIdStr != null && !submitUserIdStr.equals("")){
            String[] str = submitUserIdStr.split(",");
            this.submitUserIdList = (Long[]) ConvertUtils.convert(str,Long.class);
        }
    }

    public Long[] getSubmitUserIdList() {
        return submitUserIdList;
    }

    public void setSubmitUserIdList(Long[] submitUserIdList) {
        this.submitUserIdList = submitUserIdList;
    }

    public String getSystemIdStr() {
        return systemIdStr;
    }

    public void setSystemIdStr(String systemIdStr) {
        this.systemIdStr = systemIdStr;
        if(systemIdStr != null && !systemIdStr.equals("")){
            String[] str = systemIdStr.split(",");
            this.systemIdList = (Long[]) ConvertUtils.convert(str,Long.class);
        }
    }

    public Long[] getSystemIdList() {
        return systemIdList;
    }

    public void setSystemIdList(Long[] systemIdList) {
        this.systemIdList = systemIdList;
    }

    public String getRequirementCodeStr() {
        return requirementCodeStr;
    }

    public void setRequirementCodeStr(String requirementCodeStr) {
        this.requirementCodeStr = requirementCodeStr;
        if(requirementCodeStr != null && !requirementCodeStr.equals("")){
            this.requirementCodeList = requirementCodeStr.split(",");
        }
    }

    public String[] getRequirementCodeList() {
        return requirementCodeList;
    }

    public void setRequirementCodeList(String[] requirementCodeList) {
        this.requirementCodeList = requirementCodeList;
    }

    public String getCommissioningWindowIdStr() {
        return commissioningWindowIdStr;
    }

    public void setCommissioningWindowIdStr(String commissioningWindowIdStr) {
        this.commissioningWindowIdStr = commissioningWindowIdStr;
        if(commissioningWindowIdStr != null && !commissioningWindowIdStr.equals("")){
            String[] str = commissioningWindowIdStr.split(",");
            this.commissioningWindowIdList = (Long[]) ConvertUtils.convert(str,Long.class);
        }
    }

    public Long[] getCommissioningWindowIdList() {
        return commissioningWindowIdList;
    }

    public void setCommissioningWindowIdList(Long[] commissioningWindowIdList) {
        this.commissioningWindowIdList = commissioningWindowIdList;
    }

	public String[] getAssignUserIds() {
		return assignUserIds;
	}

	public void setAssignUserIds(String[] assignUserIds) {
		this.assignUserIds = assignUserIds;
	}

	public String getTestUserName() {
		return testUserName;
	}

	public void setTestUserName(String testUserName) {
		this.testUserName = testUserName;
	}

	public String[] getTestUserIds() {
		return testUserIds;
	}

	public void setTestUserIds(String[] testUserIds) {
		this.testUserIds = testUserIds;
	}

	public String getTestTaskCode() {
		return testTaskCode;
	}

	public void setTestTaskCode(String testTaskCode) {
		this.testTaskCode = testTaskCode;
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

    public String getEmergencyLevelStr() {
        return emergencyLevelStr;
    }

    public void setEmergencyLevelStr(String emergencyLevelStr) {
        this.emergencyLevelStr = emergencyLevelStr;
    }

    public String getDefectTypeStr() {
        return defectTypeStr;
    }

    public void setDefectTypeStr(String defectTypeStr) {
        this.defectTypeStr = defectTypeStr;
    }

	public String getDevelopUserName() {
		return developUserName;
	}

	public void setDevelopUserName(String developUserName) {
		this.developUserName = developUserName;
	}

	public String[] getDevelopUserIds() {
		return developUserIds;
	}

	public void setDevelopUserIds(String[] developUserIds) {
		this.developUserIds = developUserIds;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getRepairRoundStr() {
		return repairRoundStr;
	}

	public void setRepairRoundStr(String repairRoundStr) {
		this.repairRoundStr = repairRoundStr;
	}

	
	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getTestTaskName() {
		return testTaskName;
	}

	public void setTestTaskName(String testTaskName) {
		this.testTaskName = testTaskName;
	}
	
	
}