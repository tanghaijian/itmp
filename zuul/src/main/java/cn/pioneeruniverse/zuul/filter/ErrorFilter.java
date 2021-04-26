package cn.pioneeruniverse.zuul.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 路由请求发生错误拦截器（SendErrorFilter会优先拦截）
 * @Date: Created in 13:41 2018/12/13
 * @Modified By:
 */
public class ErrorFilter extends ZuulFilter {
    Logger log = LoggerFactory.getLogger(ErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getThrowable() != null && !ctx.getBoolean("sendErrorFilter.ran", false);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("网关ErrorFilter拦截请求路径:" + request.getRequestURL());
        return null;
    }
}
