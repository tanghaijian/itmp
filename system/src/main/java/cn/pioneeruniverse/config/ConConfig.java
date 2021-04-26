package cn.pioneeruniverse.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import cn.pioneeruniverse.common.constants.Constants;

/**
 * 
* @ClassName: ConConfig
* @Description: TODO(配置redis订阅与发布模式)
* @author author
* @date 2020年7月29日 上午9:51:06
*
 */
@Configuration
public class ConConfig {

	//邮件发送适配器
	@Resource(name = "sendEmailAdapter")
	private MessageListenerAdapter sendEmailAdapter;
	
	//企业微信发送适配器
	@Resource(name = "sendWeChatAdapter")
	private MessageListenerAdapter sendWeChatAdapter;
	
	/**
	 * 创建连接工厂
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		// 关联topic和适配器，可以添加多个
		//发送邮件的listener和队列
		container.addMessageListener(sendEmailAdapter, new PatternTopic(Constants.SEND_EMAIL_MESSAGE));
		//发送企业微信的Listener和队列
		container.addMessageListener(sendWeChatAdapter, new PatternTopic(Constants.SEND_WECHAT_MESSAGE));
		return container;
	}

}
