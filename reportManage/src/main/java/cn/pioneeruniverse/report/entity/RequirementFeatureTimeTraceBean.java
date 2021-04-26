package cn.pioneeruniverse.report.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RequirementFeatureTimeTraceBean extends BaseEntity{

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
    private Date requirementFeatureCreateTime;	
    
    /**
     * 	首次创建工作任务时间
     * */
    private Date devTaskCreateTime;
    
    /**
     *	 首次提交代码时间
     * */
    private Date codeFirstCommitTime;
    
    /**
     *	 状态变为实施完成时间
     * */
    private Date requirementFeatureDevCompleteTime;
    
    /**
     * 	开发任务首次测试环境部署时间
     * */
    private Date requirementFeatureFirstTestDeployTime;
    
    /**
     *	 开发任务对应的测试任务首次变成实施中时间
     * */
    private Date requirementFeatureTestingTime;
    
    /**
     *	 开发任务对应的测试任务最后变成实施完成时间
     * */
    private Date requirementFeatureTestCompleteTime;
    
    /**
     *	 最后一次提交上线部署时间
     * */
    private Date requirementFeatureLastProdTime;
    
    /**
     * 	上线部署完成时间
     * */
    private Date requirementFeatureProdCompleteTime;
    
    @TableField(exist = false)
    private Long systemId;
    
    @TableField(exist = false)
    private Long requirementId;
    
    @TableField(exist = false)
    private String reqFeatureIds;

    @TableField(exist = false)
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date cliskDate;
    @TableField(exist = false)
    private int ready;		//就绪
    @TableField(exist = false) 
    private int process;		//实现中
    @TableField(exist = false)
    private int achieve;		//实现完成
    @TableField(exist = false)
    private int waitValid;	//待验证
    @TableField(exist = false)
    private int inVerification;	//验证中
    @TableField(exist = false)
    private int stayOnline;	//待上线
    @TableField(exist = false)
    private int onLine;		//已上线
    
    @TableField(exist = false)
    private double taskDays;		//任务总天数
    
    public Date getCliskDate() {
		return cliskDate;
	}
	public void setCliskDate(Date cliskDate) {
		this.cliskDate = cliskDate;
	}
	public int getReady() {
		return ready;
	}
	public void setReady(int ready) {
		this.ready = ready;
	}
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public int getAchieve() {
		return achieve;
	}
	public void setAchieve(int achieve) {
		this.achieve = achieve;
	}
	public int getWaitValid() {
		return waitValid;
	}
	public void setWaitValid(int waitValid) {
		this.waitValid = waitValid;
	}
	public int getInVerification() {
		return inVerification;
	}
	public void setInVerification(int inVerification) {
		this.inVerification = inVerification;
	}
	public int getStayOnline() {
		return stayOnline;
	}
	public void setStayOnline(int stayOnline) {
		this.stayOnline = stayOnline;
	}
	public int getOnLine() {
		return onLine;
	}
	public void setOnLine(int onLine) {
		this.onLine = onLine;
	}

	public String getReqFeatureIds() {
		return reqFeatureIds;
	}

	public void setReqFeatureIds(String reqFeatureIds) {
		this.reqFeatureIds = reqFeatureIds;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

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
	public double getTaskDays() {
		return taskDays;
	}
	public void setTaskDays(double taskDays) {
		this.taskDays = taskDays;
	}

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date prodCompleteTime;

    public Date getProdCompleteTime() {
        return prodCompleteTime;
    }
    public void setProdCompleteTime(Date prodCompleteTime) {
        this.prodCompleteTime = prodCompleteTime;
    }

    @Override
    public String toString() {
        return "RequirementFeatureTimeTraceBean{" +
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
                ", systemId=" + systemId +
                ", requirementId=" + requirementId +
                ", reqFeatureIds='" + reqFeatureIds + '\'' +
                ", cliskDate=" + cliskDate +
                ", ready=" + ready +
                ", process=" + process +
                ", achieve=" + achieve +
                ", waitValid=" + waitValid +
                ", inVerification=" + inVerification +
                ", stayOnline=" + stayOnline +
                ", onLine=" + onLine +
                ", taskDays=" + taskDays +
                ", prodCompleteTime=" + prodCompleteTime +
                '}';
    }
}