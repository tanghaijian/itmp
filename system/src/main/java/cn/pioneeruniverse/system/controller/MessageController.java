package cn.pioneeruniverse.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import cn.pioneeruniverse.system.entity.TblMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.system.entity.TblMessageInfo;
import cn.pioneeruniverse.system.service.message.IMessageService;

@RestController
@RequestMapping("message")
public class MessageController extends BaseController {

	@Autowired
	private IMessageService iMessageService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 获取内部消息
	 * @param id 消息ID
	 * @return map Key：status=1 正常返回 ，2异常返回 
    *                 data返回的数据
	 */
	@RequestMapping(value="selectMessageById",method=RequestMethod.POST)
	public Map<String,Object> selectMessageById(Long id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblMessageInfo message = iMessageService.selectMessageById(id);
			result.put("data", message);
		}catch(Exception e){
			return this.handleException(e, "获取消息详情失败");
		}
		return result;
	}

	/**
	 * 获取内部消息列表
	 * @param request
	 * @param messageJson 封装的查询条件
	 * @param rows 每页数
	 * @param page 第几页
	 * @return map Key：status=1 正常返回 ，2异常返回 
    *                   rows    List<TblMessageInfo>消息列表
                        records 总数
                        total   总页数
                        page    第几页
	 */
	@RequestMapping(value="getAllMessage",method=RequestMethod.POST)
	public Map<String,Object> getAllMessage(HttpServletRequest request, String messageJson, Integer rows, Integer page) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			Long userId = CommonUtil.getCurrentUserId(request);
			TblMessageInfo message = new TblMessageInfo();
			if (StringUtil.isNotEmpty(messageJson)) {
				message = JSONObject.parseObject(messageJson, TblMessageInfo.class);
			}
			message.setCurrentUserId(userId);
			message.setStatus(1);
			result = iMessageService.getAllMessage(message, page, rows);
		}catch(Exception e){
			return this.handleException(e, "获取消息详情失败");
		}
		return result;
	}

	/**
	 * 插入内部消息
	 * @param request
	 * @param messageJson TblMessageInfo json化字符串
	 * @return map status 1正常，2异常
	 */
	@RequestMapping(value="insertMessage",method=RequestMethod.POST)
	public Map<String,Object> insertMessage(HttpServletRequest request, String messageJson) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblMessageInfo message = new TblMessageInfo();
			if (StringUtil.isNotEmpty(messageJson)) {
				message = JSONObject.parseObject(messageJson, TblMessageInfo.class);
			}
			logger.info("消息参数:"+message);
			iMessageService.insertMessage(request, message);
		}catch(Exception e){
			return this.handleException(e, "新增消息失败");
		}
		return result;
	}

	/**
	 * 删除内部消息
	 * @param request
	 * @param id 消息ID
	 * @return
	 */
	@RequestMapping(value="deleteMessage",method=RequestMethod.POST)
	public Map<String,Object> deleteMessage(HttpServletRequest request, Long id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblMessageInfo message = new TblMessageInfo();
			message.setId(id);
			iMessageService.deleteMessage(request, message);
		}catch(Exception e){
			return this.handleException(e, "删除消息失败");
		}
		return result;
	}

	/**
	 * 删除内部消息列表
	 * @param request
	 * @param ids 多条消息ID
	 * @return map Key：status=1 正常返回 ，2异常返回 
	 */
	@RequestMapping(value="deleteMessageList",method=RequestMethod.POST)
	public Map<String,Object> deleteMessageList(HttpServletRequest request, String ids) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			iMessageService.deleteMessageList(request, ids);
		}catch(Exception e){
			return this.handleException(e, "删除消息失败");
		}
		return result;
	}


	/**
	 * 发送外部消息：email 微信...
	 * 发送方式SEND_METHOD（1:邮件，2:微信）特别增加参数：3.邮件和微信。
	 * @param request
	 * @param messageJson 需要发送的消息内容
	 * @return
	 */
	@RequestMapping(value="sendMessage",method=RequestMethod.POST)
	public Map<String,Object> sendMessage(HttpServletRequest request, String messageJson) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblMessageQueue queue = new TblMessageQueue();
			if (StringUtil.isNotEmpty(messageJson)) {
				queue = JSONObject.parseObject(messageJson, TblMessageQueue.class);
			}
			if (queue.getSendMethod() == 1) {//发送方式（1:邮件，2:微信）3.邮件和微信。
				stringRedisTemplate.convertAndSend(Constants.SEND_EMAIL_MESSAGE,messageJson);
			} else if (queue.getSendMethod() == 2) {
				stringRedisTemplate.convertAndSend(Constants.SEND_WECHAT_MESSAGE,messageJson);
			} else if (queue.getSendMethod() == 3) {
				stringRedisTemplate.convertAndSend(Constants.SEND_EMAIL_MESSAGE,messageJson);
				stringRedisTemplate.convertAndSend(Constants.SEND_WECHAT_MESSAGE,messageJson);
			}
		}catch(Exception e){
			return this.handleException(e, "发送消息失败");
		}
		return result;
	}

	/**
	 * 根据jobManage定时任务发送消息
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value="sendMessageJob",method=RequestMethod.POST)
	public Map<String,Object> sendMessageJob(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", Constants.ITMP_RETURN_SUCCESS);
		try{
			TblMessageQueue queue = new TblMessageQueue();
			List<TblMessageQueue> queueList = iMessageService.selectMessageQueueList(request, queue);
			for (TblMessageQueue bean : queueList) {
				String messageJson = JSON.toJSONString(bean);
				if (bean.getSendMethod() == 1) {//发送方式（1:邮件，2:微信）3.邮件和微信。
					stringRedisTemplate.convertAndSend(Constants.SEND_EMAIL_MESSAGE, messageJson);
				} else if (bean.getSendMethod() == 2) {
					stringRedisTemplate.convertAndSend(Constants.SEND_WECHAT_MESSAGE, messageJson);
				}
			}
		}catch(Exception e){
			return this.handleException(e, "发送消息失败");
		}
		return result;
	}
}
