package cn.pioneeruniverse.dev.entity;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblRequirementFeatureLog
* @Description: 开发任务日志
* @author author
* @date 2020年8月8日 上午10:09:33
*
 */
@TableName("tbl_requirement_feature_log")
public class TblRequirementFeatureLog extends BaseEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private Long requirementFeatureId; //开发任务ID

    private String logType; //日志类型，字符串如新增开发任务

    private String logDetail; //日志内容

    private Long userId; //责任人

    private String userName; //责任人姓名

    private String userAccount; //责任人账号

    @Transient
    private List<TblRequirementFeatureLogAttachement> logAttachements; //日志附件

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

    public List<TblRequirementFeatureLogAttachement> getLogAttachements() {
        return logAttachements;
    }

    public void setLogAttachements(List<TblRequirementFeatureLogAttachement> logAttachements) {
        this.logAttachements = logAttachements;
    }


}