package cn.pioneeruniverse.project.entity;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblProjectChangeInfo
 * @Description 项目变更类
 * @author author
 * @date 2020年8月27日
 *
 */
@TableName("tbl_project_change_info")
public class TblProjectChangeInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Long projectId;    //项目表主键
	
	private Long programId;    //项目群主键
	
	private Long responsibleUserId;    //责任方
	
	private String changeItem;    //变更事项
	
	private Integer changeStatus;    //状态(数据字典)
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	private Date submitDate;    //变更提出日期
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	private Date approveDate;    //变更批准日期
	
	private String analyseSummary;    //分析汇总
	
	private String resultExplain;    //执行结果说明
	
	private String remark;    //备注
	
	 @Transient
	 private String responsibleUser;    //责任人
	 
	 @Transient
	 private String changeStatusName;  //状态
	
	@Transient
	private Long number;    //序号
	
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

	public String getChangeItem() {
		return changeItem;
	}

	public void setChangeItem(String changeItem) {
		this.changeItem = changeItem;
	}

	public Integer getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(Integer changeStatus) {
		this.changeStatus = changeStatus;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public String getAnalyseSummary() {
		return analyseSummary;
	}

	public void setAnalyseSummary(String analyseSummary) {
		this.analyseSummary = analyseSummary;
	}

	public String getResultExplain() {
		return resultExplain;
	}

	public void setResultExplain(String resultExplain) {
		this.resultExplain = resultExplain;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(String responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getChangeStatusName() {
		return changeStatusName;
	}

	public void setChangeStatusName(String changeStatusName) {
		this.changeStatusName = changeStatusName;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

}
