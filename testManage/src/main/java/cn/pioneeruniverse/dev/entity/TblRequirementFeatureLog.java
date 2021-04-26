package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;
import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_requirement_feature_log")
public class TblRequirementFeatureLog extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long requirementFeatureId;

    private String logType;

    private String logDetail;

    private Long userId;

    private String userName;

    private String userAccount;

    @Transient
    private List<TblRequirementFeatureLogAttachement> logAttachements;


    public List<TblRequirementFeatureLogAttachement> getLogAttachements() {
        return logAttachements;
    }

    public void setLogAttachements(List<TblRequirementFeatureLogAttachement> logAttachements) {
        this.logAttachements = logAttachements;
    }

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