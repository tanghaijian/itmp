package cn.pioneeruniverse.system.service.message;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.system.entity.TblMessageInfo;
import cn.pioneeruniverse.system.entity.TblMessageQueue;

public interface IMessageService {
	/**
	 *
	 * @Title: getAllMessage
	 * @Description: 获取所有消息查询列表
	 * @author author
	 * @param message 封装的查询条件
	 * @param pageIndex 第几页
	 * @param pageSize 每页数量
	 * @return
	 */
	Map<String, Object> getAllMessage(TblMessageInfo message, Integer pageIndex, Integer pageSize);
	/**
	 *
	 * @Title: selectMessageById
	 * @Description: 获取具体的消息
	 * @author author
	 * @param id 消息ID
	 * @return TblMessageInfo 消息信息
	 */
	TblMessageInfo selectMessageById(Long id);
	/**
	 *
	 * @Title: insertMessage
	 * @Description: 插入消息
	 * @author author
	 * @param request
	 * @param message 消息
	 */
	void insertMessage(HttpServletRequest request, TblMessageInfo message);
	/**
	 *
	 * @Title: deleteMessage
	 * @Description: 删除消息（逻辑删除）
	 * @author author
	 * @param request
	 * @param message 需要删除的消息
	 */
	void deleteMessage(HttpServletRequest request, TblMessageInfo message);
	/**
	 *
	 * @Title: deleteMessageList
	 * @Description: 删除多条消息
	 * @author author
	 * @param request
	 * @param ids 需要删除的消息ID
	 */
	void deleteMessageList(HttpServletRequest request, String ids);
	/**
	 *
	 * @Title: selectMessageQueueList
	 * @Description: 获取所有消息
	 * @author author
	 * @param request
	 * @param queue 查询条件
	 * @return
	 */
	List<TblMessageQueue> selectMessageQueueList(HttpServletRequest request, TblMessageQueue queue);
	/**
	 *
	 * @Title: saveMessageData
	 * @Description: 保存消息
	 * @author author
	 * @param success 是否发送成功
	 * @param queue 消息信息
	 */
	void saveMessageData(boolean success, TblMessageQueue queue);


}
