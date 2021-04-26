package cn.pioneeruniverse.project.entity;

/**
 *@author
 *@Description 项目计划审批人配置类，项目默认审批人
 *@Date 2020/8/7
 *@return
 **/
public class TblProjectPlanApproveUserConfig {

    private	Long id;
    private	Long projectId;//项目ID
    private	Long userId; //审批人
    private	int approveLevel; //审批层级

    private	String userName;

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

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
