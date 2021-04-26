package cn.pioneeruniverse.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.List;
/**
 *
 * @ClassName:TblMessageInfo
 * @Description:消息类
 * @author author
 * @date 2020年8月16日
 *
 */
@TableName("tbl_message_info")
public class TblMessageInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	//系统列表
	private List<Object> systemIdList; 
	//消息类型,
	private Integer messageType;
	//消息来源1新建开发任务、2新建工作任务、3新建缺陷、4关注开发任务
	private Integer messageSource;
	//消息主题
	private String messageTitle;
	//消息内容
	private String messageContent;
	//url
	private String messageUrl;
    //菜单ID
	private Long menuButtonId;
    //消息接收人范围（1:所有人，2:指定人）
	private Integer messageReceiverScope;
	//消息接受者
	private String messageReceiver;
	//系统id
	private Long systemId;  
	//项目id
	private Long projectId; 	
	//当前登录用户
	@TableField(exist = false)
	private Long currentUserId;

	public Integer getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(Integer messageSource) {
		this.messageSource = messageSource;
	}

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

	public String getMessageUrl() {
		return messageUrl;
	}

	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl == null ? null : messageUrl.trim();
	}

	public Long getMenuButtonId() {
		return menuButtonId;
	}

	public void setMenuButtonId(Long menuButtonId) {
		this.menuButtonId = menuButtonId;
	}

	public Integer getMessageReceiverScope() {
		return messageReceiverScope;
	}

	public void setMessageReceiverScope(Integer messageReceiverScope) {
		this.messageReceiverScope = messageReceiverScope;
	}

	public String getMessageReceiver() {
		return messageReceiver;
	}

	public void setMessageReceiver(String messageReceiver) {
		this.messageReceiver = messageReceiver == null ? null : messageReceiver.trim();
	}

	public Long getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Long currentUserId) {
		this.currentUserId = currentUserId;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public List<Object> getSystemIdList() {
		return systemIdList;
	}

	public void setSystemIdList(List<Object> systemIdList) {
		this.systemIdList = systemIdList;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "TblMessageInfo{" +
				"systemIdList=" + systemIdList +
				", messageType=" + messageType +
				", messageSource=" + messageSource +
				", messageTitle='" + messageTitle + '\'' +
				", messageContent='" + messageContent + '\'' +
				", messageUrl='" + messageUrl + '\'' +
				", menuButtonId=" + menuButtonId +
				", messageReceiverScope=" + messageReceiverScope +
				", messageReceiver='" + messageReceiver + '\'' +
				", systemId=" + systemId +
				", projectId=" + projectId +
				", currentUserId=" + currentUserId +
				'}';
	}
}