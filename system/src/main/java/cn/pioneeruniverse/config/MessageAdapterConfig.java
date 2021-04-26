package cn.pioneeruniverse.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 
* @ClassName: MessageAdapterConfig
* @Description: TODO(配置redis订阅模式消息适配器)
* @author author
* @date 2020年7月29日 上午9:52:15
*
 */
@Configuration
public class MessageAdapterConfig {

	//引入处理器
	@Resource(name="messageHandler")
	private MessageHandler messageHandler;
	
	//邮件发送消息适配器与处理器关联
	@Bean
	public MessageListenerAdapter sendEmailAdapter(){
		return new MessageListenerAdapter(messageHandler,"sendEmailMessage");
	}
	
	//企业微信消息适配器与处理器关联
	@Bean
	public MessageListenerAdapter sendWeChatAdapter(){
		return new MessageListenerAdapter(messageHandler,"sendWeChatMessage");
	}
	

}
