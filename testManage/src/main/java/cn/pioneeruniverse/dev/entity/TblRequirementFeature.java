package cn.pioneeruniverse.dev.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import org.springframework.format.annotation.DateTimeFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.bean.PropertyInfo;

@TableName("tbl_requirement_feature")
public class TblRequirementFeature extends BaseEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @PropertyInfo(name = "任务名称")
    private String featureName;

    private String featureCode;

    @PropertyInfo(name = "任务描述")
    private String featureOverview;

    @PropertyInfo(name = "涉及系统")
    private Long systemId;

    private Integer createType;

    @PropertyInfo(name = "任务类型")
    private Integer requirementFeatureSource;

    @PropertyInfo(name = "任务流水号")
    private Long taskId;

    @PropertyInfo(name = "关联需求")
    private Long requirementId;

    @PropertyInfo(name = "需求编号")
    private String requirementCode;

    @PropertyInfo(name = "需求类型")
    private String requirementType;

    @PropertyInfo(name = "需求状态")
    private String requirementStatus;
    
    @PropertyInfo(name = "需求变更次数")
    private Integer requirementChangeNumber;
    
    @PropertyInfo(name = "重点需求类型")
    private String importantRequirementType;

    @PropertyInfo(name = "问题单号")
    private String questionNumber;
    
    @PropertyInfo(name="任务状态")
    private String requirementFeatureStatus;
    
    private String deployStatus;

    @PropertyInfo(name = "管理岗")
    private Long manageUserId;

    @PropertyInfo(name = "执行人")
    private Long executeUserId;
    
    @PropertyInfo(name = "开发部门")
    private Long developmentDeptId;

    private Integer temporaryStatus;

    @PropertyInfo(name = "所属处室")
    private Long deptId;

    private String handleSuggestion;

    private Date planStartDate;

    private Date planEndDate;

    private Double estimateWorkload;

    private Date actualStartDate;

    private Date actualEndDate;

    private Double actualWorkload;
    
    @TableField(exist = false)
    private String windowName;

    private Long projectId;
    
    @TableField(exist = false)
    private String systemName;

    @PropertyInfo(name = "项目计划表主键")
    private Long projectPlanId;
    
    
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

	public Long getDevRequirementFeatureId() {
        return devRequirementFeatureId;
    }

    public void setDevRequirementFeatureId(Long devRequirementFeatureId) {
        this.devRequirementFeatureId = devRequirementFeatureId;
    }

    @TableField(exist = false)
    private Long  devRequirementFeatureId;

    public Double getEstimateRemainWorkload() {
        return estimateRemainWorkload;
    }

    public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
        this.estimateRemainWorkload = estimateRemainWorkload;
    }

    private Double estimateRemainWorkload;





    @PropertyInfo(name = "预计系测开始时间")
    private Date planSitStartDate;

    @PropertyInfo(name = "预计系测结束时间")
    private Date planSitEndDate;

    @PropertyInfo(name = "预计系测工作量")
    private Double estimateSitWorkload;

    @PropertyInfo(name = "实际系测开始时间")
    private Date actualSitStartDate;

    @PropertyInfo(name = "实际系测结束时间")
    private Date actualSitEndDate;

    @PropertyInfo(name = "实际系测工作量")
    private Double actualSitWorkload;

    @PropertyInfo(name = "系测测试案例数")
    private Integer sitTestCaseAmount;

    @PropertyInfo(name = "系测测试缺陷数")
    private Integer sitDefectAmount;

    @PropertyInfo(name = "预计版测开始时间")
    private Date planPptStartDate;

    @PropertyInfo(name = "预计版测结束时间")
    private Date planPptEndDate;

    @PropertyInfo(name = "预计版测工作量")
    private Double estimatePptWorkload;

    @PropertyInfo(name = "实际版测开始时间")
    private Date actualPptStartDate;

    @PropertyInfo(name = "实际版测结束时间")
    private Date actualPptEndDate;

    @PropertyInfo(name = "实际版测工作量")
    private Double actualPptWorkload;

    @PropertyInfo(name = "版测测试案例数")
    private Integer pptTestCaseAmount;

    @PropertyInfo(name = "版测测试缺陷数")
    private Integer pptDefectAmount;
    
    @JsonFormat(timezone = "GMT+8", locale = "zh",pattern = "yyyy-MM-dd")    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @PropertyInfo(name = "发布版测日期")
    private Date pptDeployTime;
    
    @JsonFormat(timezone = "GMT+8", locale = "zh",pattern = "yyyy-MM-dd")    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @PropertyInfo(name = "提交测试日期")
    private Date submitTestTime;
    
    @PropertyInfo(name = "投产窗口")
    private Long commissioningWindowId;

    @PropertyInfo(name = "扩展字段")
    private String fieldTemplate;

    public String getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}

	public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName == null ? null : featureName.trim();
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode == null ? null : featureCode.trim();
    }

    public String getFeatureOverview() {
        return featureOverview;
    }

    public void setFeatureOverview(String featureOverview) {
        this.featureOverview = featureOverview == null ? null : featureOverview.trim();
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber == null ? null : questionNumber.trim();
    }

    public String getRequirementFeatureStatus() {
        return requirementFeatureStatus;
    }

    public void setRequirementFeatureStatus(String requirementFeatureStatus) {
        this.requirementFeatureStatus = requirementFeatureStatus == null ? null : requirementFeatureStatus.trim();
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


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getHandleSuggestion() {
        return handleSuggestion;
    }

    public void setHandleSuggestion(String handleSuggestion) {
        this.handleSuggestion = handleSuggestion == null ? null : handleSuggestion.trim();
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

    public Double getActualWorkload() {
        return actualWorkload;
    }

    public void setActualWorkload(Double actualWorkload) {
        this.actualWorkload = actualWorkload;
    }

    public Date getPlanSitStartDate() {
        return planSitStartDate;
    }

    public void setPlanSitStartDate(Date planSitStartDate) {
        this.planSitStartDate = planSitStartDate;
    }

    public Date getPlanSitEndDate() {
        return planSitEndDate;
    }

    public void setPlanSitEndDate(Date planSitEndDate) {
        this.planSitEndDate = planSitEndDate;
    }

    public Double getEstimateSitWorkload() {
        return estimateSitWorkload;
    }

    public void setEstimateSitWorkload(Double estimateSitWorkload) {
        this.estimateSitWorkload = estimateSitWorkload;
    }

    public Date getActualSitStartDate() {
        return actualSitStartDate;
    }

    public void setActualSitStartDate(Date actualSitStartDate) {
        this.actualSitStartDate = actualSitStartDate;
    }

    public Date getActualSitEndDate() {
        return actualSitEndDate;
    }

    public void setActualSitEndDate(Date actualSitEndDate) {
        this.actualSitEndDate = actualSitEndDate;
    }

    public Double getActualSitWorkload() {
        return actualSitWorkload;
    }

    public void setActualSitWorkload(Double actualSitWorkload) {
        this.actualSitWorkload = actualSitWorkload;
    }

    public Date getPlanPptStartDate() {
        return planPptStartDate;
    }

    public void setPlanPptStartDate(Date planPptStartDate) {
        this.planPptStartDate = planPptStartDate;
    }

    public Date getPlanPptEndDate() {
        return planPptEndDate;
    }

    public void setPlanPptEndDate(Date planPptEndDate) {
        this.planPptEndDate = planPptEndDate;
    }

    public Double getEstimatePptWorkload() {
        return estimatePptWorkload;
    }

    public void setEstimatePptWorkload(Double estimatePptWorkload) {
        this.estimatePptWorkload = estimatePptWorkload;
    }

    public Date getActualPptStartDate() {
        return actualPptStartDate;
    }

    public void setActualPptStartDate(Date actualPptStartDate) {
        this.actualPptStartDate = actualPptStartDate;
    }

    public Date getActualPptEndDate() {
        return actualPptEndDate;
    }

    public void setActualPptEndDate(Date actualPptEndDate) {
        this.actualPptEndDate = actualPptEndDate;
    }

    public Double getActualPptWorkload() {
        return actualPptWorkload;
    }

    public void setActualPptWorkload(Double actualPptWorkload) {
        this.actualPptWorkload = actualPptWorkload;
    }

    public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public Integer getRequirementFeatureSource() {
        return requirementFeatureSource;
    }

    public void setRequirementFeatureSource(Integer requirementFeatureSource) {
        this.requirementFeatureSource = requirementFeatureSource;
    }

    public Integer getTemporaryStatus() {
        return temporaryStatus;
    }

    public void setTemporaryStatus(Integer temporaryStatus) {
        this.temporaryStatus = temporaryStatus;
    }

    public Integer getSitTestCaseAmount() {
        return sitTestCaseAmount;
    }

    public void setSitTestCaseAmount(Integer sitTestCaseAmount) {
        this.sitTestCaseAmount = sitTestCaseAmount;
    }

    public Integer getSitDefectAmount() {
        return sitDefectAmount;
    }

    public void setSitDefectAmount(Integer sitDefectAmount) {
        this.sitDefectAmount = sitDefectAmount;
    }

    public Integer getPptTestCaseAmount() {
        return pptTestCaseAmount;
    }

    public void setPptTestCaseAmount(Integer pptTestCaseAmount) {
        this.pptTestCaseAmount = pptTestCaseAmount;
    }

    public Integer getPptDefectAmount() {
        return pptDefectAmount;
    }

    public void setPptDefectAmount(Integer pptDefectAmount) {
        this.pptDefectAmount = pptDefectAmount;
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

    public String getRequirementStatus() {
        return requirementStatus;
    }

    public void setRequirementStatus(String requirementStatus) {
        this.requirementStatus = requirementStatus;
    }

    public String getFieldTemplate() {
        return fieldTemplate;
    }

    public void setFieldTemplate(String fieldTemplate) {
        this.fieldTemplate = fieldTemplate;
    }

	public Integer getRequirementChangeNumber() {
		return requirementChangeNumber;
	}

	public void setRequirementChangeNumber(Integer requirementChangeNumber) {
		this.requirementChangeNumber = requirementChangeNumber;
	}

	public String getImportantRequirementType() {
		return importantRequirementType;
	}

	public void setImportantRequirementType(String importantRequirementType) {
		this.importantRequirementType = importantRequirementType;
	}

	public Date getPptDeployTime() {
		return pptDeployTime;
	}

	public void setPptDeployTime(Date pptDeployTime) {
		this.pptDeployTime = pptDeployTime;
	}

	public Date getSubmitTestTime() {
		return submitTestTime;
	}

	public void setSubmitTestTime(Date submitTestTime) {
		this.submitTestTime = submitTestTime;
	}

	public Long getDevelopmentDeptId() {
		return developmentDeptId;
	}

	public void setDevelopmentDeptId(Long developmentDeptId) {
		this.developmentDeptId = developmentDeptId;
	}

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectPlanId() {
        return projectPlanId;
    }
    public void setProjectPlanId(Long projectPlanId) {
        this.projectPlanId = projectPlanId;
    }
}