package cn.pioneeruniverse.common.config.xss;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 拦截防止xss注入, 通过Jsoup过滤请求参数内的特定字符
 * @Date: Created in 17:50 2019/5/15
 * @Modified By:
 */
public class XssFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XssFilter.class);

    private List<String> excludeUrls = new ArrayList<String>();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String temp = filterConfig.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(temp)) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludeUrls.add(url[i]);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (handleExcludeURL(req)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(xssRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }


    private boolean handleExcludeURL(HttpServletRequest request) {
        if (CollectionUtil.isEmpty(excludeUrls)) {
            return false;
        }
        String url = request.getServletPath();
        PathMatcher matcher = new AntPathMatcher();
        for (String exUrl : excludeUrls) {
            if (matcher.match(exUrl, url)) {
                return true;
            }
        }
        return false;
    }
}
