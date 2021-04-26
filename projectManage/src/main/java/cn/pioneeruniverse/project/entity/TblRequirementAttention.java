package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblRequirementAttention
 * @Description:开发任务关注类
 * @author author
 * @date 2020年8月16日
 *
 */
@TableName("tbl_requirement_attention")
public class TblRequirementAttention extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long requirementId;//需求id

	private Long userId;//用户id

	public Long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}