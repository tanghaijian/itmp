package cn.pioneeruniverse.common.config;

import cn.pioneeruniverse.common.bean.SessionListener;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;

import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:12 2019/5/14
 * @Modified By:
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 6 * 60 * 60,redisFlushMode = RedisFlushMode.IMMEDIATE)
public class RedisSessionConfig {

    @Bean
    @ConditionalOnProperty(prefix = "cas.config", name = "open", havingValue = "true")
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter() {
        List<HttpSessionListener> httpSessionListeners = new ArrayList<>();
        httpSessionListeners.add(new SingleSignOutHttpSessionListener());
        httpSessionListeners.add(new SessionListener());
        return new SessionEventHttpSessionListenerAdapter(httpSessionListeners);
    }
}
