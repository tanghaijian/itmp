package cn.pioneeruniverse.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblMessageQueueHistory
 * @Description
 * @author author
 * @date 2020年8月27日
 *
 */
@TableName("tbl_message_queue_history")
public class TblMessageQueueHistory extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	//消息标题
	private String messageTitle;
	//消息内容
	private String messageContent;
	//消息接收者
	private String messageReceiver;

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle == null ? null : messageTitle.trim();
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent == null ? null : messageContent.trim();
	}

	public String getMessageReceiver() {
		return messageReceiver;
	}

	public void setMessageReceiver(String messageReceiver) {
		this.messageReceiver = messageReceiver == null ? null : messageReceiver.trim();
	}

}