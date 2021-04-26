package cn.pioneeruniverse.project.entity;

/**
 *@author
 *@Description 项目计划审批人表类，
 *项目计划变更的审批人包括一级审批，二级审批。具体对应的审批人信息存于该bean
 *@Date 2020/8/7
 *@return
 **/
public class TblProjectPlanApproveUser {

    private	Long id;
    private	Long projectPlanApproveRequestId; //项目变更申请ID
    private	Long userId; //审批人ID
    private	int approveLevel; //审批层次（1级审批，2级审批）
    private	int	approveStatus; //'审批状态（1=待审批；2=已审批）'
    private String approveSuggest; //审批意见
    private	int	approveOnOff;// '审批开关（1=允许；2=不允许）',

    private	Long projectId;//项目ID
    private int operate; //审批人操作，对应页面的按钮1：【同意并提交下一级审批人】，2：【同意并办结】，3：【驳回】

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectPlanApproveRequestId() {
        return projectPlanApproveRequestId;
    }
    public void setProjectPlanApproveRequestId(Long projectPlanApproveRequestId) {
        this.projectPlanApproveRequestId = projectPlanApproveRequestId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getApproveLevel() {
        return approveLevel;
    }
    public void setApproveLevel(int approveLevel) {
        this.approveLevel = approveLevel;
    }

    public int getApproveStatus() {
        return approveStatus;
    }
    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveSuggest() {
        return approveSuggest;
    }
    public void setApproveSuggest(String approveSuggest) {
        this.approveSuggest = approveSuggest;
    }

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public int getOperate() {
        return operate;
    }
    public void setOperate(int operate) {
        this.operate = operate;
    }

    public int getApproveOnOff() {
        return approveOnOff;
    }
    public void setApproveOnOff(int approveOnOff) {
        this.approveOnOff = approveOnOff;
    }
}
