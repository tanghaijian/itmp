package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 * 
* @ClassName: TblRequirementFeature
* @Description: 开发任务bean
* @author author
* @date 2020年8月8日 上午10:07:10
*
 */
@TableName("tbl_requirement_feature")
public class TblRequirementFeature extends BaseEntity{

	private static final long serialVersionUID = 1L;

	//@NotBlank(message = "名称不能为空")
	@PropertyInfo(name="任务名称")
    private String featureName;
    
	@PropertyInfo(name="任务编号")
    private String featureCode;
    
   
   // @Length(min = 5, max = 180, message = "描述长度5-180")
    @PropertyInfo(name="任务描述")
    private String featureOverview;

    @PropertyInfo(name="涉及系统")
    private Long systemId;

    private Long projectId;	//关联项目id

    private Integer createType;//创建方式（1=自建，2=同步）
    
    @PropertyInfo(name="任务来源")
    private Integer  requirementFeatureSource;//任务来源（数据字典：1=业务需求，2=生产问题）
    
    @PropertyInfo(name="任务流水号")
    private Integer taskId;//IT全流程开发任务流水号
    
    @PropertyInfo(name="问题单号")
    private String  questionNumber;//生产问题单编号

    @PropertyInfo(name="任务状态")
    private String requirementFeatureStatus;//01待实施 02实施中03实施完成 00取消
    
    @PropertyInfo(name="优先级")
    private Integer requirementFeaturePriority;//任务优先级
    
    private String deployStatus;//部署状态
    
    @PropertyInfo(name="管理岗")
    private Integer manageUserId;
    
    @PropertyInfo(name="执行人")
    private Integer executeUserId;
    
    //@Range(min=1,max=2,message = "临时任务类型错误")
    private Integer temporaryStatus;//临时任务 （1=是  2=否）
    
    @PropertyInfo(name="所属处室")
    private Integer deptId;
    
    @PropertyInfo(name="处理意见")
    private String handleSuggestion;
    
    @PropertyInfo(name="预计开始时间")
    private Date planStartDate;
    
    @PropertyInfo(name="预计结束时间")
    private Date planEndDate;
    
    @PropertyInfo(name="预计工作量")
    private Double estimateWorkload;

	@PropertyInfo(name="预估剩余工作量")
	private Double estimateRemainWorkload;

    @PropertyInfo(name="实际开始时间")
    private Date actualStartDate;
    
    @PropertyInfo(name="实际结束时间")
    private Date actualEndDate;
    
    @PropertyInfo(name="实际工作量")
    private Double actualWorkload;

    @PropertyInfo(name = "系统版本")
    private Long systemVersionId;//系统版本
    
    private String  systemScmBranch;//源码管理分支

    @PropertyInfo(name = "投产窗口")
    private Long commissioningWindowId;//投产窗口（计划版本）

    @PropertyInfo(name = "关联需求")
    private Long requirementId;
    
    @PropertyInfo(name = "冲刺")
    private Long sprintId;//冲刺id
    
    @PropertyInfo(name = "故事点")
    private Double storyPoint;//故事点



	@PropertyInfo(name = "项目小组表")
	private Long  executeProjectGroupId;//系统版本



	@PropertyInfo(name="模块（资产系统树表主键）")
	private Long assetSystemTreeId;

	@PropertyInfo(name="修复版本")
	private Long repairSystemVersionId;


	@PropertyInfo(name="扩展字段")
	private String fieldTemplate;

	@PropertyInfo(name="项目计划")
	private Long projectPlanId;

	@PropertyInfo(name="验收标记")
	private Integer checkStatus;



    @TableField(exist = false)
    private Long countStatus;	//数量

	@TableField(exist = false)
	private String systemVersionName;//系统版本号名称

	@TableField(exist = false)
	private String commissioningWindowName;//投产窗口名称
	
	@TableField(exist = false)
	private String executeUserIds;//执行人ids
    
    
/*=======================================================================*/

	public Double getEstimateRemainWorkload() {
		return estimateRemainWorkload;
	}

	public void setEstimateRemainWorkload(Double estimateRemainWorkload) {
		this.estimateRemainWorkload = estimateRemainWorkload;
	}

	public String getDeployStatus() {
		return deployStatus;
	}

	public Double getStoryPoint() {
		return storyPoint;
	}

	public void setStoryPoint(Double storyPoint) {
		this.storyPoint = storyPoint;
	}

	public Integer getRequirementFeaturePriority() {
		return requirementFeaturePriority;
	}

	public void setRequirementFeaturePriority(Integer requirementFeaturePriority) {
		this.requirementFeaturePriority = requirementFeaturePriority;
	}

	public Long getSprintId() {
		return sprintId;
	}

	public void setSprintId(Long sprintId) {
		this.sprintId = sprintId;
	}

	public void setDeployStatus(String deployStatus) {
		this.deployStatus = deployStatus;
	}
   
    public String getSystemScmBranch() {
		return systemScmBranch;
	}

	public void setSystemScmBranch(String systemScmBranch) {
		this.systemScmBranch = systemScmBranch;
	}

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

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
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

	public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
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

	public Long getCommissioningWindowId() {
        return commissioningWindowId;
    }

    public void setCommissioningWindowId(Long commissioningWindowId) {
        this.commissioningWindowId = commissioningWindowId;
    }

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public Integer getManageUserId() {
		return manageUserId;
	}

	public void setManageUserId(Integer manageUserId) {
		this.manageUserId = manageUserId;
	}

	public Integer getExecuteUserId() {
		return executeUserId;
	}

	public void setExecuteUserId(Integer executeUserId) {
		this.executeUserId = executeUserId;
	}

	public Integer getTemporaryStatus() {
		return temporaryStatus;
	}

	public void setTemporaryStatus(Integer temporaryStatus) {
		this.temporaryStatus = temporaryStatus;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getHandleSuggestion() {
		return handleSuggestion;
	}

	public void setHandleSuggestion(String handleSuggestion) {
		this.handleSuggestion = handleSuggestion;
	}

	public String getRequirementFeatureStatus() {
		return requirementFeatureStatus;
	}

	public void setRequirementFeatureStatus(String requirementFeatureStatus) {
		this.requirementFeatureStatus = requirementFeatureStatus;
	}

	public String getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Long getCountStatus() {
		return countStatus;
	}
	public void setCountStatus(Long countStatus) {
		this.countStatus = countStatus;
	}

    public Long getSystemVersionId() {
        return systemVersionId;
    }

    public void setSystemVersionId(Long systemVersionId) {
        this.systemVersionId = systemVersionId;
    }

	public String getSystemVersionName() {
		return systemVersionName;
	}

	public void setSystemVersionName(String systemVersionName) {
		this.systemVersionName = systemVersionName;
	}

	public String getCommissioningWindowName() {
		return commissioningWindowName;
	}

	public void setCommissioningWindowName(String commissioningWindowName) {
		this.commissioningWindowName = commissioningWindowName;
	}
	public Long getExecuteProjectGroupId() {
		return executeProjectGroupId;
	}

	public void setExecuteProjectGroupId(Long executeProjectGroupId) {
		this.executeProjectGroupId = executeProjectGroupId;
	}

	public Long getRepairSystemVersionId() {
		return repairSystemVersionId;
	}

	public void setRepairSystemVersionId(Long repairSystemVersionId) {
		this.repairSystemVersionId = repairSystemVersionId;
	}
	public Long getAssetSystemTreeId() {
		return assetSystemTreeId;
	}

	public void setAssetSystemTreeId(Long assetSystemTreeId) {
		this.assetSystemTreeId = assetSystemTreeId;
	}

	public String getFieldTemplate() {
		return fieldTemplate;
	}

	public void setFieldTemplate(String fieldTemplate) {
		this.fieldTemplate = fieldTemplate;
	}

	public Long getProjectPlanId() {
		return projectPlanId;
	}
	public void setProjectPlanId(Long projectPlanId) {
		this.projectPlanId = projectPlanId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	public String getExecuteUserIds() {
		return executeUserIds;
	}

	public void setExecuteUserIds(String executeUserIds) {
		this.executeUserIds = executeUserIds;
	}

	@Override
	public String toString() {
		return "TblRequirementFeature{" +
				"featureName='" + featureName + '\'' +
				", featureCode='" + featureCode + '\'' +
				", featureOverview='" + featureOverview + '\'' +
				", systemId=" + systemId +
				", projectId=" + projectId +
				", createType=" + createType +
				", requirementFeatureSource=" + requirementFeatureSource +
				", taskId=" + taskId +
				", questionNumber='" + questionNumber + '\'' +
				", requirementFeatureStatus='" + requirementFeatureStatus + '\'' +
				", requirementFeaturePriority=" + requirementFeaturePriority +
				", deployStatus='" + deployStatus + '\'' +
				", manageUserId=" + manageUserId +
				", executeUserId=" + executeUserId +
				", temporaryStatus=" + temporaryStatus +
				", deptId=" + deptId +
				", handleSuggestion='" + handleSuggestion + '\'' +
				", planStartDate=" + planStartDate +
				", planEndDate=" + planEndDate +
				", estimateWorkload=" + estimateWorkload +
				", estimateRemainWorkload=" + estimateRemainWorkload +
				", actualStartDate=" + actualStartDate +
				", actualEndDate=" + actualEndDate +
				", actualWorkload=" + actualWorkload +
				", systemVersionId=" + systemVersionId +
				", systemScmBranch='" + systemScmBranch + '\'' +
				", commissioningWindowId=" + commissioningWindowId +
				", requirementId=" + requirementId +
				", sprintId=" + sprintId +
				", storyPoint=" + storyPoint +
				", executeProjectGroupId=" + executeProjectGroupId +
				", assetSystemTreeId=" + assetSystemTreeId +
				", repairSystemVersionId=" + repairSystemVersionId +
				", fieldTemplate='" + fieldTemplate + '\'' +
				", projectPlanId=" + projectPlanId +
				", countStatus=" + countStatus +
				", systemVersionName='" + systemVersionName + '\'' +
				", commissioningWindowName='" + commissioningWindowName + '\'' +
				'}';
	}
}