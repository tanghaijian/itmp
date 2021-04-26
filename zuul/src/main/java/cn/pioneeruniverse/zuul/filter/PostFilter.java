package cn.pioneeruniverse.zuul.filter;

import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 在route和error过滤器之后的过滤器(可以做系统日志)
 * @Date: Created in 13:41 2018/12/13
 * @Modified By:
 */
public class PostFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(PostFilter.class);

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        PathMatcher matcher = new AntPathMatcher();
        return !matcher.match("/*/static/**", request.getRequestURI()) &&
                !matcher.match("/*/druid/**", request.getRequestURI());//静态资源,druid监控不做拦截
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("网关PostFilter处理请求路径:" + request.getRequestURL());
        return null;
    }
}
