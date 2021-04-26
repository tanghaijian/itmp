package cn.pioneeruniverse.common.bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccic.cas.client.util.CasUtils;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 单点登录session拦截器
 * @Date: Created in 16:44 2019/8/16
 * @Modified By:
 */
public class SessionTimeOutFilterForCas implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionTimeOutFilter.class);

    private List<String> excludeUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String casExcludeUrlPatterns = filterConfig.getInitParameter("casExcludeUrlPatterns");
        if (StringUtils.isNotEmpty(casExcludeUrlPatterns)) {
            this.excludeUrls.addAll(Arrays.asList(casExcludeUrlPatterns.split("\\s+")));
            this.excludeUrls.addAll(Arrays.asList(Constants.System.DEFAULT_URL));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!CommonUtil.handleExcludeURL(this.excludeUrls, request.getServletPath())) {
            if (CasUtils.isCasLoggedIn(request)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(999);
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter writer = response.getWriter();
                String logoutUrl = CommonUtil.urlReplaceWithOutParam(request.getRequestURL().toString(), Constants.System.LOGOUT_URL);
                response.setHeader("logoutUrl", logoutUrl);
                writer.write("<script type = \"text/javascript\"> window.parent.location.href = \"" + logoutUrl + "\";</script>");
                writer.flush();
                writer.close();
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
