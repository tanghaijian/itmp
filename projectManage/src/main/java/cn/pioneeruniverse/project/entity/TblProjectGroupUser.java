package cn.pioneeruniverse.project.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblProjectGroupUser
* @Description: 项目小组人员
* @author author
* @date 2020年8月7日 下午4:14:09
*
 */
@TableName("tbl_project_group_user")
public class TblProjectGroupUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 项目小组ID
	 **/
	private Long projectGroupId;//项目小组ID

	/**
	 * 人员ID
	 **/
	private Long userId; //人员ID

	/**
	 * 项目岗位
	 **/
	private Integer userPost; //项目岗位

	/**
	 * 预计开始日期
	 **/
	private Date planStartDate; //预计开始日期

	/**
	 * 预计结束日期
	 **/
	private Date planEndDate;//预计结束日期

	/**
	 * 分配工时
	 **/
	private Double allocateWorkload; //分配工时

	/**
	 * 同小组所有用户
	 **/
	@TableField(exist = false)
	private List<Long> userIds; //同小组所有用户

	/**
	 * 是否全部展示
	 **/
	@TableField(exist = false)
	private Integer isAll; //是否全部展示

	/**
	 * 项目ID
	 **/
	@TableField(exist = false)
	private Long projectId;//项目ID


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

	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public Integer getIsAll() {
		return isAll;
	}
	public void setIsAll(Integer isAll) {
		this.isAll = isAll;
	}

	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "TblProjectGroupUser{" +
				"projectGroupId=" + projectGroupId +
				", userId=" + userId +
				", userPost=" + userPost +
				", planStartDate=" + planStartDate +
				", planEndDate=" + planEndDate +
				", allocateWorkload=" + allocateWorkload +
				", userIds=" + userIds +
				", isAll=" + isAll +
				", projectId=" + projectId +
				'}';
	}
}
