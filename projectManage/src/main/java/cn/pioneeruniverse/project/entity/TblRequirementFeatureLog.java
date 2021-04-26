package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblRequirementFeatureLog
* @Description: 开发任务日志bean
* @author author
* @date 2020年8月31日 上午11:12:50
*
 */
@TableName("tbl_requirement_feature_log")
public class TblRequirementFeatureLog extends BaseEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private Long requirementFeatureId;//开发任务ID

    private String logType;//日志类型：如新增开发任务、拆分开发任务等

    private String logDetail;//日志内容详情

    private Long userId;//记录人

    private String userName;//记录人姓名

    private String userAccount;//记录人账号

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail == null ? null : logDetail.trim();
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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

}