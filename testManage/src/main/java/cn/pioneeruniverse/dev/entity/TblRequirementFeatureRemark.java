package cn.pioneeruniverse.dev.entity;

import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;
import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_requirement_feature_remark")
public class TblRequirementFeatureRemark extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long requirementFeatureId;

    private String requirementFeatureRemark;

    private Long userId;

    private String userName;

    private String userAccount;
    @Transient
    private List<TblRequirementFeatureRemarkAttachement> remarkAttachements;


    public List<TblRequirementFeatureRemarkAttachement> getRemarkAttachements() {
        return remarkAttachements;
    }

    public void setRemarkAttachements(List<TblRequirementFeatureRemarkAttachement> remarkAttachements) {
        this.remarkAttachements = remarkAttachements;
    }

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

   
    public String getRequirementFeatureRemark() {
		return requirementFeatureRemark;
	}

	public void setRequirementFeatureRemark(String requirementFeatureRemark) {
		this.requirementFeatureRemark = requirementFeatureRemark;
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