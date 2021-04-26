package cn.pioneeruniverse.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

@TableName("tbl_message_queue")
public class TblMessageQueue extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息标题
	 **/
	private String messageTitle; 

	/**
	 * 消息内容
	 **/
	private String messageContent;

	/**
	 * 消息接收者
	 **/
	private String messageReceiver;

	/**
	 * 发送方式（1:邮件，2:微信）
	 **/
	private Integer sendMethod;

	/**
	 * 发送次数
	 **/
	private Integer sendCount;

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageReceiver() {
		return messageReceiver;
	}

	public void setMessageReceiver(String messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

	public Integer getSendMethod() {
		return sendMethod;
	}

	public void setSendMethod(Integer sendMethod) {
		this.sendMethod = sendMethod;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

}