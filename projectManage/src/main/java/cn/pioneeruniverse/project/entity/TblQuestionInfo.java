package cn.pioneeruniverse.project.entity;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 问题
 */

@TableName("tbl_question_info")
public class TblQuestionInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Long projectId;    //项目表主键
	
	private Long programId;    //项目群主键
	
	private Long responsibleUserId;    //责任方
	
	private String questionDescription;    //描述
	
	private Integer questionReasonType;    //原因分类(数据字典)
	
	private Integer questionImportance;    //重要性(数据字典)
	
	private Integer questionEmergencyLevel;    //紧急程度(数据字典)
	
	private Integer questionPriority;    //优先级(数据字典)
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	private Date happenDate;    //发生日期
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	private Date endDate;    //结束日期
	
	private String copingStrategyRecord;    //解决措施执行记录
	
	private Integer happenStage;    //发生阶段(数据字典)
	
	private String reasonAnalysis;    //问题原因分析
	
	private String remark;    //备注
	
	@Transient
	private String responsibleUser;    //责任人
	
	@Transient
	private Long number;    //序号
	
	@Transient
	private String questionReasonTypeName;    //原因分类
	
	@Transient
	private String questionImportanceName;    //重要性
	
	@Transient
	private String questionEmergencyLevelName;    //紧急程度
	
	@Transient
	private String questionPriorityName;    //优先级
	
	@Transient
	private String happenStageName;    //发生阶段

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getResponsibleUserId() {
		return responsibleUserId;
	}

	public void setResponsibleUserId(Long responsibleUserId) {
		this.responsibleUserId = responsibleUserId;
	}

	public String getQuestionDescription() {
		return questionDescription;
	}

	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}

	public Integer getQuestionReasonType() {
		return questionReasonType;
	}

	public void setQuestionReasonType(Integer questionReasonType) {
		this.questionReasonType = questionReasonType;
	}

	public Integer getQuestionImportance() {
		return questionImportance;
	}

	public void setQuestionImportance(Integer questionImportance) {
		this.questionImportance = questionImportance;
	}

	public Integer getQuestionEmergencyLevel() {
		return questionEmergencyLevel;
	}

	public void setQuestionEmergencyLevel(Integer questionEmergencyLevel) {
		this.questionEmergencyLevel = questionEmergencyLevel;
	}

	public Integer getQuestionPriority() {
		return questionPriority;
	}

	public void setQuestionPriority(Integer questionPriority) {
		this.questionPriority = questionPriority;
	}

	public Date getHappenDate() {
		return happenDate;
	}

	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCopingStrategyRecord() {
		return copingStrategyRecord;
	}

	public void setCopingStrategyRecord(String copingStrategyRecord) {
		this.copingStrategyRecord = copingStrategyRecord;
	}

	public Integer getHappenStage() {
		return happenStage;
	}

	public void setHappenStage(Integer happenStage) {
		this.happenStage = happenStage;
	}

	public String getReasonAnalysis() {
		return reasonAnalysis;
	}

	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(String responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getQuestionReasonTypeName() {
		return questionReasonTypeName;
	}

	public void setQuestionReasonTypeName(String questionReasonTypeName) {
		this.questionReasonTypeName = questionReasonTypeName;
	}

	public String getQuestionImportanceName() {
		return questionImportanceName;
	}

	public void setQuestionImportanceName(String questionImportanceName) {
		this.questionImportanceName = questionImportanceName;
	}

	public String getQuestionEmergencyLevelName() {
		return questionEmergencyLevelName;
	}

	public void setQuestionEmergencyLevelName(String questionEmergencyLevelName) {
		this.questionEmergencyLevelName = questionEmergencyLevelName;
	}

	public String getQuestionPriorityName() {
		return questionPriorityName;
	}

	public void setQuestionPriorityName(String questionPriorityName) {
		this.questionPriorityName = questionPriorityName;
	}

	public String getHappenStageName() {
		return happenStageName;
	}

	public void setHappenStageName(String happenStageName) {
		this.happenStageName = happenStageName;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	
	
}
