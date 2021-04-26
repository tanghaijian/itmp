package cn.pioneeruniverse.dev.entity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_project_group_user")
public class TblProjectGroupUser extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private Long projectGroupId;
	
	private Long userId;
	
	private Integer userPost;
	
	private Date planStartDate;
	
	private Date planEndDate;
	
	private Double allocateWorkload;

	public Long getProjectGroupId() {
		return projectGroupId;
	}

	public void setProjectGroupId(Long projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUserPost() {
		return userPost;
	}

	public void setUserPost(Integer userPost) {
		this.userPost = userPost;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public Double getAllocateWorkload() {
		return allocateWorkload;
	}

	public void setAllocateWorkload(Double allocateWorkload) {
		this.allocateWorkload = allocateWorkload;
	}
	
	
}
