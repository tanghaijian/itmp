package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblProjectChangeLog
 * @Description项目变更日志类
 * @author author
 * @date 2020年8月27日
 *
 */
@TableName("tbl_project_change_log")
public class TblProjectChangeLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Long projectChangeId; //项目变更ID
	
	private String logType;//便跟类型，中文字符串表示，比如：新增变更，修改变更
	
	private String logDetail; //变更日志
	
	private Long userId;//责任人ID
	
	private String userName;//责任人姓名
	
	private String userAccount;//责任人账号

	public Long getProjectChangeId() {
		return projectChangeId;
	}

	public void setProjectChangeId(Long projectChangeId) {
		this.projectChangeId = projectChangeId;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
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
