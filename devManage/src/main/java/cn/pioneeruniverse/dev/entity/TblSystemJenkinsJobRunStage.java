package cn.pioneeruniverse.dev.entity;

import java.sql.Timestamp;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_system_jenkins_job_run_stage")
public class TblSystemJenkinsJobRunStage extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long systemJenkinsJobRunId;//系统JENKINS配置表主键

	private Integer stageId;//Jenkins日志中参数id

	private String stageName;//STAGE名称

	private Integer stageOrder;//STAGE顺序

	private String stageExecNode;//执行节点

	private String stageStatus;//执行状态：SUCCESS、FAILED等

	private Timestamp stageStartTime;//开始构建时间=startTimeMillis

	private Long stageDuration;//持续时间=durationMillis

	private Long stagePauseDuration;//暂停时间=pauseDurationMillis
	
	@TableField(exist = false)
	private List<TblSystemJenkinsJobRunStageLog> stageLogList;//具体日志

	public Long getSystemJenkinsJobRunId() {
		return systemJenkinsJobRunId;
	}

	public void setSystemJenkinsJobRunId(Long systemJenkinsJobRunId) {
		this.systemJenkinsJobRunId = systemJenkinsJobRunId;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName == null ? null : stageName.trim();
	}

	public Integer getStageOrder() {
		return stageOrder;
	}

	public void setStageOrder(Integer stageOrder) {
		this.stageOrder = stageOrder;
	}

	public String getStageExecNode() {
		return stageExecNode;
	}

	public void setStageExecNode(String stageExecNode) {
		this.stageExecNode = stageExecNode == null ? null : stageExecNode.trim();
	}

	public String getStageStatus() {
		return stageStatus;
	}

	public void setStageStatus(String stageStatus) {
		this.stageStatus = stageStatus == null ? null : stageStatus.trim();
	}

	public Timestamp getStageStartTime() {
		return stageStartTime;
	}

	public void setStageStartTime(Timestamp stageStartTime) {
		this.stageStartTime = stageStartTime;
	}

	public Long getStageDuration() {
		return stageDuration;
	}

	public void setStageDuration(Long stageDuration) {
		this.stageDuration = stageDuration;
	}

	public Long getStagePauseDuration() {
		return stagePauseDuration;
	}

	public void setStagePauseDuration(Long stagePauseDuration) {
		this.stagePauseDuration = stagePauseDuration;
	}

	public List<TblSystemJenkinsJobRunStageLog> getStageLogList() {
		return stageLogList;
	}

	public void setStageLogList(List<TblSystemJenkinsJobRunStageLog> stageLogList) {
		this.stageLogList = stageLogList;
	}
	
	

}