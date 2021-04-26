package cn.pioneeruniverse.zuul.config;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import com.ccic.cas.client.util.CasUtils;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:41 2019/8/15
 * @Modified By:
 */
public class SystemLogoutRedirectToCasLogoutFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String serviceUrl = CommonUtil.urlReplaceWithOutParam(request.getRequestURL().toString(), Constants.System.DEFAULT_URL[0]);
        String casLogOutUrl = new StringBuilder(CasUtils.getCasServerLogoutUrl()).append("?service=").append(serviceUrl).toString();
        request.getSession().invalidate();
        response.sendRedirect(casLogOutUrl);
    }

    @Override
    public void destroy() {

    }

}
