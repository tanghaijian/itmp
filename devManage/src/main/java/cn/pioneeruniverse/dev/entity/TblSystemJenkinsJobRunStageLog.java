package cn.pioneeruniverse.dev.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_system_jenkins_job_run_stage_log")
public class TblSystemJenkinsJobRunStageLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long systemJenkinsJobRunStageId;//TblSystemJenkinsJobRunStage的ID

	private Integer stageId;//TblSystemJenkinsJobRunStage的stageId

	private Integer stageLogId;//jenkins中的STAGE_LOG_ID

	private String stageLogName;//log名

	private Integer stageLogOrder;//Log顺序

	private String stageLogExecNode;//执行节点

	private String stageLogStatus;//执行状态success,failed等

	private Timestamp stageLogStartTime;//开始构建时间

	private Long stageLogDuration;//持续时间

	private Long stageLogPauseDuration;//暂停时间

	private String detailNodeStatus;//nodeStatus执行状态：SUCCESS、FAILED等

	private Long detailLength;//日志长度

	private Integer detailHasmore;//hasMore:1=true;2=false

	private String detailText;//日志详细内容text

	public Long getSystemJenkinsJobRunStageId() {
		return systemJenkinsJobRunStageId;
	}

	public void setSystemJenkinsJobRunStageId(Long systemJenkinsJobRunStageId) {
		this.systemJenkinsJobRunStageId = systemJenkinsJobRunStageId;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public Integer getStageLogId() {
		return stageLogId;
	}

	public void setStageLogId(Integer stageLogId) {
		this.stageLogId = stageLogId;
	}

	public String getStageLogName() {
		return stageLogName;
	}

	public void setStageLogName(String stageLogName) {
		this.stageLogName = stageLogName == null ? null : stageLogName.trim();
	}

	public Integer getStageLogOrder() {
		return stageLogOrder;
	}

	public void setStageLogOrder(Integer stageLogOrder) {
		this.stageLogOrder = stageLogOrder;
	}

	public String getStageLogExecNode() {
		return stageLogExecNode;
	}

	public void setStageLogExecNode(String stageLogExecNode) {
		this.stageLogExecNode = stageLogExecNode == null ? null : stageLogExecNode.trim();
	}

	public String getStageLogStatus() {
		return stageLogStatus;
	}

	public void setStageLogStatus(String stageLogStatus) {
		this.stageLogStatus = stageLogStatus == null ? null : stageLogStatus.trim();
	}

	public Timestamp getStageLogStartTime() {
		return stageLogStartTime;
	}

	public void setStageLogStartTime(Timestamp stageLogStartTime) {
		this.stageLogStartTime = stageLogStartTime;
	}

	public Long getStageLogDuration() {
		return stageLogDuration;
	}

	public void setStageLogDuration(Long stageLogDuration) {
		this.stageLogDuration = stageLogDuration;
	}

	public Long getStageLogPauseDuration() {
		return stageLogPauseDuration;
	}

	public void setStageLogPauseDuration(Long stageLogPauseDuration) {
		this.stageLogPauseDuration = stageLogPauseDuration;
	}

	public String getDetailNodeStatus() {
		return detailNodeStatus;
	}

	public void setDetailNodeStatus(String detailNodeStatus) {
		this.detailNodeStatus = detailNodeStatus == null ? null : detailNodeStatus.trim();
	}

	public Long getDetailLength() {
		return detailLength;
	}

	public void setDetailLength(Long detailLength) {
		this.detailLength = detailLength;
	}

	public Integer getDetailHasmore() {
		return detailHasmore;
	}

	public void setDetailHasmore(Integer detailHasmore) {
		this.detailHasmore = detailHasmore;
	}

	public String getDetailText() {
		return detailText;
	}

	public void setDetailText(String detailText) {
		this.detailText = detailText;
	}
}