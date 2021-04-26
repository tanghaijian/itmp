package cn.pioneeruniverse.dev.entity;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblRequirementFeatureRemark
* @Description: 开发任务备注
* @author author
* @date 2020年8月8日 上午10:12:57
*
 */
@TableName("tbl_requirement_feature_remark")
public class TblRequirementFeatureRemark extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long requirementFeatureId; //开发任务ID

    private String requirementFeatureRemark; //开发任务备注

    private Long userId; //责任人

    private String userName; //责任人姓名

    private String userAccount; //责任人账号
    
    @Transient
    private List<TblRequirementFeatureRemarkAttachement> remarkAttachements; //备注附件
   
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

	public List<TblRequirementFeatureRemarkAttachement> getRemarkAttachements() {
		return remarkAttachements;
	}

	public void setRemarkAttachements(List<TblRequirementFeatureRemarkAttachement> remarkAttachements) {
		this.remarkAttachements = remarkAttachements;
	}
    
 
}