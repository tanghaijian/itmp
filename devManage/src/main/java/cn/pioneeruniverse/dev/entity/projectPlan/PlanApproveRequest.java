package cn.pioneeruniverse.dev.entity.projectPlan;

import java.util.Date;

public class PlanApproveRequest {

    private	Long id;

    /**
     * 项目ID
     **/
    private	Long projectId; //项目ID

    /**
     * 申请人ID
     **/
    private	Long requestUserId; //申请人ID

    /**
     * 申请理由
     **/
    private	String requestReason; //申请理由

    /**
     * 审批状态
     **/
    private	int	approveStatus; //审批状态

    /**
     * 申请人姓名
     **/
    private	String	userName; //申请人姓名

    /**
     * 项目名
     **/
    private	String	projectName;//项目名

    /**
     * 提交日期
     **/
    private Date commitDate;//提交日期

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

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getCommitDate() {
        return commitDate;
    }
    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }
}
