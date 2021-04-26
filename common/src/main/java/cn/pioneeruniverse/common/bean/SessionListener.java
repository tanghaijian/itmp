package cn.pioneeruniverse.common.bean;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:17 2019/8/15
 * @Modified By:
 */
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        Object token = session.getAttribute(Constants.System.TOKEN_NAME);
        if (token != null) {
            session.removeAttribute(Constants.System.TOKEN_NAME);
            SpringContextHolder.getBean(RedisUtils.class).remove((String) token);
        }
    }
}
