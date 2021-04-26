package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 *@author
 *@Description 项目计划审批日志表类
 *@Date 2020/8/7
 *@return
 **/
public class TblProjectPlanApproveRequestLog extends BaseEntity {
    private	Long projectPlanApproveRequestId;	//	项目计划审批申请表主键
    private	String logType;	    //	日志类型
    private	String logDetail;	//	日志明细
    private	Long userId;	    //	用户表主键
    private	String userName;	//	用户姓名
    private	String userAccount;	//	用户登录账号

    public Long getProjectPlanApproveRequestId() {
        return projectPlanApproveRequestId;
    }
    public void setProjectPlanApproveRequestId(Long projectPlanApproveRequestId) {
        this.projectPlanApproveRequestId = projectPlanApproveRequestId;
    }

    public String getLogType() {
        return logType;
    }
    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogDetail() {
        return logDetail;
    }
    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}
