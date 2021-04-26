package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblDevTaskRemark
 * @Description:开发任务
 * @author author
 * @date 2020年8月16日
 *
 */

@TableName("tbl_dev_task_remark")
public class TblDevTaskRemark extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private Long devTaskId;//开发工作任务ID
	private String devTaskRemark; //处理备注
	private Long userId;//处理人
	private String userName;//处理人姓名
	private String userAccount;//处理人账号
	
	public Long getDevTaskId() {
		return devTaskId;
	}
	public void setDevTaskId(Long devTaskId) {
		this.devTaskId = devTaskId;
	}
	
	public String getDevTaskRemark() {
		return devTaskRemark;
	}
	public void setDevTaskRemark(String devTaskRemark) {
		this.devTaskRemark = devTaskRemark;
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
