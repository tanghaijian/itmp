package cn.pioneeruniverse.zuul.config;

import cn.pioneeruniverse.common.constants.Constants;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:21 2019/8/15
 * @Modified By:
 */
public class RedirectToSystemIndexFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletResponse)servletResponse).sendRedirect(Constants.System.INDEX_URL);
    }

    @Override
    public void destroy() {

    }
}
