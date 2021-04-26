package cn.pioneeruniverse.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.message.WeChatRequestDTO;
import cn.pioneeruniverse.common.message.WechatUtil;
import cn.pioneeruniverse.common.message.email.utils.SendEmailUtils;
import cn.pioneeruniverse.common.message.email.vo.EmailVO;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.system.entity.TblMessageQueue;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.service.message.IMessageService;
import cn.pioneeruniverse.system.service.user.IUserService;

/**
 * 站外消息队列
 * 
 * @author zhoudu
 *
 */
@Component
public class MessageHandler {
	private final static Logger log = LoggerFactory.getLogger(MessageHandler.class);

	// 消息邮件发送
	/*消息发送基本信息，详细内容参见config配置文件properties*/
	@Value("${message.email.send.enable}")
	private Boolean emailSendEnable;
	@Value("${message.email.address}")
	private String emailAddress;
	@Value("${message.email.port}")
	private String emailPort;
	@Value("${message.email.username}")
	private String emailUserName;
	@Value("${message.email.fromaddress}")
	private String emailFromAddress;

	// 消息微信发送
	/*消息发送基本信息 详细内容参见config配置文件properties*/
	@Value("${message.wechat.send.enable}")
	private Boolean wechatSendEnable;
	@Value("${message.wechat.esb.rest.url}")
	private String wechatESBRestUrl;
	@Value("${message.wechat.timeout}")
	private String wechatTimeout;
	@Value("${message.wechat.consumerID}")
	private String wechatConsumerID;
	@Value("${message.wechat.channel}")
	private String wechatChannel;
	@Value("${message.wechat.subChannel}")
	private String wechatSubChannel;

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IMessageService iMessageService;

	/**
	 * 发送邮件
	 * 
	 * @param message
	 */
	public void sendEmailMessage(String message) {
		try {
			Date date = new Date();
			log.info("发送邮件开始=====>" + DateUtil.formatDate(date, DateUtil.fullFormat));
			log.info("邮件基本信息=====>emailSendEnable:{} emailAddress:{} emailPort:{} emailUserName:{} emailFromAddress:{} message:{}", 
					emailSendEnable, emailAddress, emailPort, emailUserName, emailFromAddress, message);
			if (emailSendEnable && StringUtils.isNotBlank(message)) {//是否开启邮件发送
				TblMessageQueue queue = new TblMessageQueue();
				if (StringUtil.isNotEmpty(message)) {
					queue = JSONObject.parseObject(message, TblMessageQueue.class);
				}
				
				//获取发送的对象：用户ID信息
				String[] userIdArr = queue.getMessageReceiver().split(",");
				List<Long> idList = new ArrayList<Long>();
				for (String userId : userIdArr) {
					if (StringUtil.isNotEmpty(userId.trim())) {
						idList.add(Long.parseLong(userId));
					}
				}
				//通过用户ID信息，获取对应的email信息
				String emails = "";
				List<TblUserInfo> userList = iUserService.selectBatchIds(idList);
				for (TblUserInfo tblUserInfo : userList) {
					if (StringUtil.isNotEmpty(tblUserInfo.getEmail())) {
						emails += tblUserInfo.getEmail() + ",";
					}
				}
				
				if (StringUtil.isNotEmpty(emails)) {
					log.info("发送邮件目标=====>{}", emails);
					emails = emails.substring(0, emails.length() - 1);
					EmailVO emailVO = new EmailVO();
					emailVO.setToAddress(emails);
					emailVO.setSubject(queue.getMessageTitle());
					emailVO.setContent(queue.getMessageContent());
					// 2.调用发送邮件的方法，发送邮件
					boolean success = SendEmailUtils.getInstance(emailAddress, emailPort, emailUserName, emailFromAddress).sendEmail(emailVO);
					iMessageService.saveMessageData(success, queue);
				}

			}

		} catch (Exception e) {
			// 异常情况，如收件人地址、抄送人地址为空，修改数据库处理状态,不再继续发送
			e.printStackTrace();
			log.info("发送邮件失败,发送对象Json:" + message);
			log.info("发送邮件失败:" + e.getMessage());
		}
	}

	/**
	 * 发送微信
	 * 
	 * @param message
	 */
	public void sendWeChatMessage(String message) {

		try {
			Date date = new Date();
			log.info("发送微信开始=====>" + DateUtil.formatDate(date, DateUtil.fullFormat));
			log.info("微信基本信息=====>emailSendEnable:{} wechatESBRestUrl:{} wechatTimeout:{} wechatConsumerID:{} wechatChannel:{} wechatSubChannel:{} ", 
					emailSendEnable, wechatESBRestUrl, wechatTimeout, wechatConsumerID, wechatChannel, wechatSubChannel);
			if (wechatSendEnable && StringUtils.isNotBlank(message)) {//是否开启微信发送
				TblMessageQueue queue = new TblMessageQueue();
				if (StringUtil.isNotEmpty(message)) {
					queue = JSONObject.parseObject(message, TblMessageQueue.class);
				}

				//获取微信发送的对象，用户ID
				String[] userIdArr = queue.getMessageReceiver().split(",");
				List<Long> idList = new ArrayList<Long>();
				for (String userId : userIdArr) {
					if (StringUtil.isNotEmpty(userId.trim())) {
						idList.add(Long.parseLong(userId));
					}
				}
				
				//通过用户ID去获取用户账号（和企业微信的账号一致）
				List<String> targetList = new ArrayList<String>();
				List<TblUserInfo> userList = iUserService.selectBatchIds(idList);
				for (TblUserInfo tblUserInfo : userList) {
					if (StringUtil.isNotEmpty(tblUserInfo.getUserAccount())) {
						targetList.add(tblUserInfo.getUserAccount());
					}
				}
				
				if (targetList.size() > 0) {
					log.info("发送微信目标=====>{}", JSON.toJSONString(targetList));
					String content = queue.getMessageTitle() + queue.getMessageContent();
					WechatUtil wechat = new WechatUtil();
					WeChatRequestDTO requestDTO = wechat.createWechat(wechatESBRestUrl, wechatTimeout, 
							wechatConsumerID, wechatChannel, wechatSubChannel, targetList, content);
					boolean success = wechat.sendWeChat(requestDTO);
					iMessageService.saveMessageData(success, queue);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("发送邮件失败,发送对象Json:" + message);
			log.info("发送邮件失败:" + e.getMessage());
		}
	}

//	public static void main(String[] args) {
//		EmailVO emailVO = new EmailVO();
//		emailVO.setToAddress("liugang@pioneerservice.cn,zhoudu@pioneerservice.cn");
//		emailVO.setSubject("aaaaaaa");
//		emailVO.setContent("bbbbbbb");
//		// 2.调用发送邮件的方法，发送邮件
//		try {
//			boolean success = SendEmailUtils.getInstance("10.1.22.166", "25", "IT开发测试管理系统", "devops@ccic-net.com.cn").sendEmail(emailVO);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		List<String> targetList = new ArrayList<String>();
//		targetList.add("oper15sd08");//zhoudu
//		targetList.add("oper15sczz");//liugang
//		targetList.add("8000544000");//8000544000
//		String wechatESBRestUrl = "http://10.1.11.115:8080/daditestgroup/ccicsit/sendsimplemessageservicerest/Restful";
//		String wechatTimeout = "11000";
//		String wechatConsumerID = "itmgr";
//		String wechatChannel = "weixin";
//		String wechatSubChannel = "018";
//		try {
//			WechatUtil wechat = new WechatUtil();
//			WeChatRequestDTO requestDTO = wechat.createWechat(wechatESBRestUrl, wechatTimeout, 
//					wechatConsumerID, wechatChannel, wechatSubChannel, targetList, "IT开发管理平台测试数据");
//			wechat.sendWeChat(requestDTO);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
