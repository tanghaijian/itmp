package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblQuestionLog
* @Description: 记录问题操作的日志，比如新增问题、修改问题等
* @author author
* @date 2020年8月31日 上午11:06:17
*
 */
@TableName("tbl_question_log")
public class TblQuestionLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 问题ID
	 **/
	private Long questionId; //问题ID

	/**
	 * 日志类型：新增问题、修改问题等字符串
	 **/
	private String logType; //日志类型：新增问题、修改问题等字符串

	/**
	 * 日志详情
	 **/
	private String logDetail;//日志详情

	/**
	 * 记录人
	 **/
	private Long userId;//记录人

	/**
	 * 记录人姓名
	 **/
	private String userName;//记录人姓名

	/**
	 * 记录人账号
	 **/
	private String userAccount;//记录人账号

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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
