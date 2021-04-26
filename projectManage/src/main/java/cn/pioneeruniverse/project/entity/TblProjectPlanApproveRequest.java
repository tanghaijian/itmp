package cn.pioneeruniverse.project.entity;

import java.util.Date;

/**
 *@author
 *@Description 项目计划审批申请表类 项目计划变更时需要审批，由变更人提出申请，每一次变更对应一次申请
 *@Date 2020/8/7
 *@return
 **/
public class TblProjectPlanApproveRequest {

    private	Long id;
    private	Long projectId; //项目ID
    private	Long requestUserId;//申请人
    private	String requestReason;//变更原因
    private	int	approveStatus;//审批状态（1=审批中；2=已审批,3=驳回）


    private	String	userName;//申请人姓名

    private Date commitDate;//提交申请日期

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getRequestUserId() {
        return requestUserId;
    }
    public void setRequestUserId(Long requestUserId) {
        this.requestUserId = requestUserId;
    }

    public String getRequestReason() {
        return requestReason;
    }
    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public int getApproveStatus() {
        return approveStatus;
    }
    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCommitDate() {
        return commitDate;
    }
    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }
}
