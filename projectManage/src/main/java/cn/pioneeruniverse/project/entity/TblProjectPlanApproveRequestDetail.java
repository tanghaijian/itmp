package cn.pioneeruniverse.project.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblProjectPlanApproveRequestDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private Long projectPlanApproveRequestId;   	//项目计划审批申请表主键
    private Long projectPlanId;   	                //项目计划表主键
    private Long projectId;   			//项目表主键
    private String planCode;    		//计划编号
    private String planName;    		//计划名称
    private Long responsibleUserId;    	//责任方
    private String deliverables;    	//成果物

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planStartDate;    			//计划开始日期
    private Integer planStartMilestone;    	//计划开始日期是否里程碑
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planEndDate;    			//计划结束日期
    private Integer planEndMilestone;    	//计划结束日期是否里程碑

    private double planDuration;    //计划工期（天）
    private double planWorkload;    //计划工作量（人天）
    private	Date actualStartDate;   //实际开始日期
    private	Date actualEndDate;		//实际结束日期
    private Integer planSchedule;   //计划进度（单位%）
    private Long parentId;    		//父ID
    private String parentIds;    	//所有父ID
    private Integer planLevel;   	//计划层级
    private Integer planOrder;   	//计划顺序

    /*非表字段*/
    private String responsibleUser;  //责任人

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPlanCode() {
        return planCode;
    }
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanName() {
        return planName;
    }
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Long getResponsibleUserId() {
        return responsibleUserId;
    }
    public void setResponsibleUserId(Long responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }

    public String getDeliverables() {
        return deliverables;
    }
    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }
    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Integer getPlanStartMilestone() {
        return planStartMilestone;
    }
    public void setPlanStartMilestone(Integer planStartMilestone) {
        this.planStartMilestone = planStartMilestone;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }
    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Integer getPlanEndMilestone() {
        return planEndMilestone;
    }
    public void setPlanEndMilestone(Integer planEndMilestone) {
        this.planEndMilestone = planEndMilestone;
    }

    public double getPlanDuration() {
        return planDuration;
    }
    public void setPlanDuration(double planDuration) {
        this.planDuration = planDuration;
    }

    public double getPlanWorkload() {
        return planWorkload;
    }
    public void setPlanWorkload(double planWorkload) {
        this.planWorkload = planWorkload;
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
        this.parentIds = parentIds;
    }

    public Integer getPlanLevel() {
        return planLevel;
    }
    public void setPlanLevel(Integer planLevel) {
        this.planLevel = planLevel;
    }

    public Integer getPlanOrder() {
        return planOrder;
    }
    public void setPlanOrder(Integer planOrder) {
        this.planOrder = planOrder;
    }

    public Integer getPlanSchedule() {
        return planSchedule;
    }
    public void setPlanSchedule(Integer planSchedule) {
        this.planSchedule = planSchedule;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }
    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    private String planCodeOlder;                        //旧计划编号
    private String planNameOlder;                        //旧计划名称
    private Long responsibleUserIdOlder;                 //旧责任方
    private String deliverablesOlder;                    //旧成果物

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planStartDateOlder;    			        //旧计划开始日期
    private Integer planStartMilestoneOlder;                //旧计划开始日期是否里程碑
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date planEndDateOlder;    		                //旧计划结束日期
    private Integer planEndMilestoneOlder;                  //旧计划结束日期是否里程碑

    private double planDurationOlder;                       //旧计划工期（天）
    private double planWorkloadOlder;                       //旧计划工作量（人天）
    private	Date actualStartDateOlder;                      //旧实际开始日期
    private	Date actualEndDateOlder;		                //旧实际结束日期


    public Long getProjectPlanApproveRequestId() {
        return projectPlanApproveRequestId;
    }
    public void setProjectPlanApproveRequestId(Long projectPlanApproveRequestId) {
        this.projectPlanApproveRequestId = projectPlanApproveRequestId;
    }

    public Long getProjectPlanId() {
        return projectPlanId;
    }
    public void setProjectPlanId(Long projectPlanId) {
        this.projectPlanId = projectPlanId;
    }

    public String getPlanCodeOlder() {
        return planCodeOlder;
    }
    public void setPlanCodeOlder(String planCodeOlder) {
        this.planCodeOlder = planCodeOlder;
    }

    public String getPlanNameOlder() {
        return planNameOlder;
    }
    public void setPlanNameOlder(String planNameOlder) {
        this.planNameOlder = planNameOlder;
    }

    public Long getResponsibleUserIdOlder() {
        return responsibleUserIdOlder;
    }
    public void setResponsibleUserIdOlder(Long responsibleUserIdOlder) {
        this.responsibleUserIdOlder = responsibleUserIdOlder;
    }

    public String getDeliverablesOlder() {
        return deliverablesOlder;
    }
    public void setDeliverablesOlder(String deliverablesOlder) {
        this.deliverablesOlder = deliverablesOlder;
    }

    public Date getPlanStartDateOlder() {
        return planStartDateOlder;
    }
    public void setPlanStartDateOlder(Date planStartDateOlder) {
        this.planStartDateOlder = planStartDateOlder;
    }

    public Integer getPlanStartMilestoneOlder() {
        return planStartMilestoneOlder;
    }
    public void setPlanStartMilestoneOlder(Integer planStartMilestoneOlder) {
        this.planStartMilestoneOlder = planStartMilestoneOlder;
    }

    public Date getPlanEndDateOlder() {
        return planEndDateOlder;
    }
    public void setPlanEndDateOlder(Date planEndDateOlder) {
        this.planEndDateOlder = planEndDateOlder;
    }

    public Integer getPlanEndMilestoneOlder() {
        return planEndMilestoneOlder;
    }
    public void setPlanEndMilestoneOlder(Integer planEndMilestoneOlder) {
        this.planEndMilestoneOlder = planEndMilestoneOlder;
    }

    public double getPlanDurationOlder() {
        return planDurationOlder;
    }
    public void setPlanDurationOlder(double planDurationOlder) {
        this.planDurationOlder = planDurationOlder;
    }

    public double getPlanWorkloadOlder() {
        return planWorkloadOlder;
    }
    public void setPlanWorkloadOlder(double planWorkloadOlder) {
        this.planWorkloadOlder = planWorkloadOlder;
    }

    public Date getActualStartDateOlder() {
        return actualStartDateOlder;
    }
    public void setActualStartDateOlder(Date actualStartDateOlder) {
        this.actualStartDateOlder = actualStartDateOlder;
    }

    public Date getActualEndDateOlder() {
        return actualEndDateOlder;
    }
    public void setActualEndDateOlder(Date actualEndDateOlder) {
        this.actualEndDateOlder = actualEndDateOlder;
    }

}
