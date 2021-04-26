package cn.pioneeruniverse.dev.vo;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 	时间追踪VO表
 * */
public class TestRequirementFeatureTimeTraceVO extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 开发任务id
	 * */
	private Long requirementFeatureId;
	/**
	 * 开发任务创建时间 
	 * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureCreateTime;
    
    /**
     * 	首次创建工作任务时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date devTaskCreateTime;
    
    /**
     *	 首次提交代码时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date codeFirstCommitTime;
    
    /**
     *	 状态变为实施完成时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureDevCompleteTime;
    
    /**
     * 	开发任务首次测试环境部署时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureFirstTestDeployTime;
    
    /**
     *	 开发任务对应的测试任务首次变成实施中时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureTestingTime;
    
    /**
     *	 开发任务对应的测试任务最后变成实施完成时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureTestCompleteTime;
    
    /**
     *	 最后一次提交上线部署时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureLastProdTime;
    
    /**
     * 	上线部署完成时间
     * */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date requirementFeatureProdCompleteTime;


    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }
    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public Date getRequirementFeatureCreateTime() {
        return requirementFeatureCreateTime;
    }
    public void setRequirementFeatureCreateTime(Date requirementFeatureCreateTime) {
        this.requirementFeatureCreateTime = requirementFeatureCreateTime;
    }

    public Date getDevTaskCreateTime() {
        return devTaskCreateTime;
    }
    public void setDevTaskCreateTime(Date devTaskCreateTime) {
        this.devTaskCreateTime = devTaskCreateTime;
    }

    public Date getCodeFirstCommitTime() {
        return codeFirstCommitTime;
    }
    public void setCodeFirstCommitTime(Date codeFirstCommitTime) {
        this.codeFirstCommitTime = codeFirstCommitTime;
    }

    public Date getRequirementFeatureDevCompleteTime() {
        return requirementFeatureDevCompleteTime;
    }
    public void setRequirementFeatureDevCompleteTime(Date requirementFeatureDevCompleteTime) {
        this.requirementFeatureDevCompleteTime = requirementFeatureDevCompleteTime;
    }

    public Date getRequirementFeatureFirstTestDeployTime() {
        return requirementFeatureFirstTestDeployTime;
    }
    public void setRequirementFeatureFirstTestDeployTime(Date requirementFeatureFirstTestDeployTime) {
        this.requirementFeatureFirstTestDeployTime = requirementFeatureFirstTestDeployTime;
    }

    public Date getRequirementFeatureTestingTime() {
        return requirementFeatureTestingTime;
    }
    public void setRequirementFeatureTestingTime(Date requirementFeatureTestingTime) {
        this.requirementFeatureTestingTime = requirementFeatureTestingTime;
    }

    public Date getRequirementFeatureTestCompleteTime() {
        return requirementFeatureTestCompleteTime;
    }
    public void setRequirementFeatureTestCompleteTime(Date requirementFeatureTestCompleteTime) {
        this.requirementFeatureTestCompleteTime = requirementFeatureTestCompleteTime;
    }

    public Date getRequirementFeatureLastProdTime() {
        return requirementFeatureLastProdTime;
    }
    public void setRequirementFeatureLastProdTime(Date requirementFeatureLastProdTime) {
        this.requirementFeatureLastProdTime = requirementFeatureLastProdTime;
    }

    public Date getRequirementFeatureProdCompleteTime() {
        return requirementFeatureProdCompleteTime;
    }
    public void setRequirementFeatureProdCompleteTime(Date requirementFeatureProdCompleteTime) {
        this.requirementFeatureProdCompleteTime = requirementFeatureProdCompleteTime;
    }

    @Override
    public String toString() {
        return "RequirementFeatureTimeTraceVO{" +
                "requirementFeatureId=" + requirementFeatureId +
                ", requirementFeatureCreateTime=" + requirementFeatureCreateTime +
                ", devTaskCreateTime=" + devTaskCreateTime +
                ", codeFirstCommitTime=" + codeFirstCommitTime +
                ", requirementFeatureDevCompleteTime=" + requirementFeatureDevCompleteTime +
                ", requirementFeatureFirstTestDeployTime=" + requirementFeatureFirstTestDeployTime +
                ", requirementFeatureTestingTime=" + requirementFeatureTestingTime +
                ", requirementFeatureTestCompleteTime=" + requirementFeatureTestCompleteTime +
                ", requirementFeatureLastProdTime=" + requirementFeatureLastProdTime +
                ", requirementFeatureProdCompleteTime=" + requirementFeatureProdCompleteTime +
                '}';
    }
}