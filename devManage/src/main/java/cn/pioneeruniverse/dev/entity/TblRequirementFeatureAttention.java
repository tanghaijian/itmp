package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblRequirementFeatureAttention
* @Description: 开发任务关注
* @author author
* @date 2020年8月8日 上午10:08:55
*
 */
@TableName("tbl_requirement_feature_attention")
public class TblRequirementFeatureAttention extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long requirementFeatureId;//开发任务ID

	private Long userId; //关注用户

	public Long getRequirementFeatureId() {
		return requirementFeatureId;
	}

	public void setRequirementFeatureId(Long requirementFeatureId) {
		this.requirementFeatureId = requirementFeatureId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}