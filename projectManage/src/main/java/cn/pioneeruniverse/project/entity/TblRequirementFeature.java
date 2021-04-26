package cn.pioneeruniverse.project.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.project.vo.RequirementFeatureVo;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @ClassName:TblRequirementFeature
 * @Description:开发任务类
 * @author author
 * @date 2020年8月16日
 *
 */
public class TblRequirementFeature extends RequirementFeatureVo {

    private static final long serialVersionUID = 1L;

    private String featureName; //任务名称
    @PropertyInfo(name="任务编号")
    private String featureCode; //任务编号
    private String featureOverview; //特性描述(开还任务描述)
    private Long systemId;    //系统表主键
    private Long assetSystemTreeId;//模块（资产系统树表主键）
    private Long createType;//创建方式（1=自建，2=同步）
    private Long requirementFeatureSource;//任务来源（数据字典：1=业务需求，2=生产问题）
    @PropertyInfo(name="任务流水号")
    private Long taskId;//流水号
    private Long requirementId;    //需求表主键
    private String questionNumber;  //生产问题单编号
    private String requirementFeatureStatus; //开发任务状态
    private String deployStatus;            //部署状态(数据字典)
    private Long manageUserId;    //管理人员
    private Long executeUserId;    //执行人员
    private Long developmentDeptId; //开发部门
    private Long temporaryStatus;    //临时任务
    private Long deptId;            //所属处室
    private String handleSuggestion;    //处理意见
    private Date planStartDate;    //计划开始日期                                                                
    private Date planEndDate;    //计划结束日期
    private double estimateWorkload;//预估工作量
    private double estimateRemainWorkload;//预估剩余工作量（人天）
    private Date actualStartDate;    //实际开始日期
    private Date actualEndDate;        //实际结束日期
    private double actualWorkload;    //实际工作量
    private Long commissioningWindowId;//投产窗口
    private Long sprintId;//冲刺表主键
    private Long projectPlanId;//项目计划表主键

    private Integer requirementChangeNumber;//需求变更次数

    @Transient
    private String requirementName;//需求名称

    @Transient
    private String taskType;	//任务类型

    @Transient
    private String requirementCode;//需求编码

    @Transient
    private List<Long> requirementIds;

    @Transient
    @JsonFormat(pattern = "yyyyMMdd", timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date windowDate; //窗口投产日期
    @Transient
    private List<TblDevTask> devTaskList;  //开发任务下的工作任务
    @Transient
    private String manageUserName;  //管理人员
    @Transient
    private String executeUserName;  //开发人员
    
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

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
    
    public Long getAssetSystemTreeId() {
		return assetSystemTreeId;
	}

	public void setAssetSystemTreeId(Long assetSystemTreeId) {
		this.assetSystemTreeId = assetSystemTreeId;
	}

	public Long getCreateType() {
        return createType;
    }

    public void setCreateType(Long createType) {
        this.createType = createType;
    }

    public Long getRequirementFeatureSource() {
        return requirementFeatureSource;
    }

    public void setRequirementFeatureSource(Long requirementFeatureSource) {
        this.requirementFeatureSource = requirementFeatureSource;
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
        this.questionNumber = questionNumber;
    }

    public String getRequirementFeatureStatus() {
        return requirementFeatureStatus;
    }

    public void setRequirementFeatureStatus(String requirementFeatureStatus) {
        this.requirementFeatureStatus = requirementFeatureStatus;
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

    public Long getTemporaryStatus() {
        return temporaryStatus;
    }

    public void setTemporaryStatus(Long temporaryStatus) {
        this.temporaryStatus = temporaryStatus;
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
        this.handleSuggestion = handleSuggestion;
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

    public double getEstimateWorkload() {
        return estimateWorkload;
    }

    public void setEstimateWorkload(double estimateWorkload) {
        this.estimateWorkload = estimateWorkload;
    }
    
    public double getEstimateRemainWorkload() {
		return estimateRemainWorkload;
	}

	public void setEstimateRemainWorkload(double estimateRemainWorkload) {
		this.estimateRemainWorkload = estimateRemainWorkload;
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

    public double getActualWorkload() {
        return actualWorkload;
    }

    public void setActualWorkload(double actualWorkload) {
        this.actualWorkload = actualWorkload;
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

	public String getRequirementName() {
		return requirementName;
	}
	public void setRequirementName(String requirementName) {
		this.requirementName = requirementName;
	}

	public List<Long> getRequirementIds() {
		return requirementIds;
	}
	public void setRequirementIds(List<Long> requirementIds) {
		this.requirementIds = requirementIds;
	}

	public String getRequirementCode() {
		return requirementCode;
	}
	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}

	public Date getWindowDate() {
		return windowDate;
	}
	public void setWindowDate(Date windowDate) {
		this.windowDate = windowDate;
	}

	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

    public Long getDevelopmentDeptId() {
        return developmentDeptId;
    }
    public void setDevelopmentDeptId(Long developmentDeptId) {
        this.developmentDeptId = developmentDeptId;
    }

    public String getDeployStatus() {
        return deployStatus;
    }
    public void setDeployStatus(String deployStatus) {
        this.deployStatus = deployStatus;
    }

	public List<TblDevTask> getDevTaskList() {
		return devTaskList;
	}

	public void setDevTaskList(List<TblDevTask> devTaskList) {
		this.devTaskList = devTaskList;
	}

	public String getManageUserName() {
		return manageUserName;
	}

	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}

	public String getExecuteUserName() {
		return executeUserName;
	}

	public void setExecuteUserName(String executeUserName) {
		this.executeUserName = executeUserName;
	}

    public Integer getRequirementChangeNumber() {
        return requirementChangeNumber;
    }

    public void setRequirementChangeNumber(Integer requirementChangeNumber) {
        this.requirementChangeNumber = requirementChangeNumber;
    }
}