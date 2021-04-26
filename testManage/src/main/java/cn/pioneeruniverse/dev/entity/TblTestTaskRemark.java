package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_test_task_remark")
public class TblTestTaskRemark extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private Long testTaskId;
	private String testTaskRemark;

	/**
	 * 用户id
	 **/
	private Long userId;

	/**
	 * 用户姓名
	 **/
	private String userName;

	/**
	 * 用户账号
	 **/
	private String userAccount;
	public Long getTestTaskId() {
		return testTaskId;
	}
	public void setTestTaskId(Long testTaskId) {
		this.testTaskId = testTaskId;
	}
	public String getTestTaskRemark() {
		return testTaskRemark;
	}
	public void setTestTaskRemark(String testTaskRemark) {
		this.testTaskRemark = testTaskRemark;
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
