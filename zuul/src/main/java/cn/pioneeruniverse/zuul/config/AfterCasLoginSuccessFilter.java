package cn.pioneeruniverse.zuul.config;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import com.ccic.cas.client.util.CasUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:23 2019/8/15
 * @Modified By:
 */
public class AfterCasLoginSuccessFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AfterCasLoginSuccessFilter.class);

    private List<String> excludeUrls = new ArrayList<>();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String casExcludeUrlPatterns = filterConfig.getInitParameter("casExcludeUrlPatterns");
        if (StringUtils.isNotEmpty(casExcludeUrlPatterns)) {
            excludeUrls.addAll(Arrays.asList(casExcludeUrlPatterns.split("\\s+")));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!CommonUtil.handleExcludeURL(excludeUrls, request.getServletPath())) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String currentUserAccount = CasUtils.getUserCode(request);//大地员工号即平台账号
            String currentUserName = CasUtils.getUserName(request);
            if (CasUtils.isCasLoggedIn(request)) {
                if (CommonUtil.systemTokenCheckForCas(request, currentUserAccount)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    RestTemplate restTemplate = SpringContextHolder.getBean(RestTemplate.class);
                    MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
                    paramMap.add("currentUserAccount", currentUserAccount);
                    paramMap.add("currentUserName", currentUserName);
                    AjaxModel ajaxModel = restTemplate.postForObject("http://SYSTEMUI/login", paramMap, AjaxModel.class);
                    if (ajaxModel == null) {
                        throw new ServletException("after cas login handle error:token create failure");
                    } else {
                        if (ajaxModel.getFlag()) {
                            request.getSession().setAttribute(Constants.System.TOKEN_NAME, ajaxModel.getData().toString());
                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
                            throw new ServletException("after cas login handle error:" + ((Map<String, String>) ajaxModel.getData()).get("message"));
                        }
                    }
                }
            } else {
                String logoutUrl = CommonUtil.urlReplaceWithOutParam(request.getRequestURL().toString(), Constants.System.LOGOUT_URL);
                response.sendRedirect(logoutUrl);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
