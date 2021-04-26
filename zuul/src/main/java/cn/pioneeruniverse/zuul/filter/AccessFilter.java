package cn.pioneeruniverse.zuul.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.common.utils.*;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.zuul.constants.ExceptionConstants;
import cn.pioneeruniverse.zuul.exception.InvalidateRequestException;


/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 请求被路由前拦截器（判断当前请求是否可以被路由）
 * @Date: Created in 13:41 2018/12/13
 * @Modified By:
 */
public class AccessFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Value("#{'${system.not.intercept.url}'.split('\\s+')}")
    private List<String> systemNotInterceptUrl;

    @Value("#{'${system.outer.interface.url}'.split('\\s+')}")
    private List<String> systemOutInterfaceUrl;

    @Value("${cas.config.open}")
    private Boolean casConfigOpen;


    @Autowired
    RedisUtils redisUtils;

    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        //数字越大，优先级越低
        return 0;
    }

    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        PathMatcher matcher = new AntPathMatcher();
        for (String url : systemNotInterceptUrl) {
            if (matcher.match(url, request.getRequestURI())) {
                return false;
            }
        }
        //系统对外接口现在默认不拦截，不做安全校验
        for (String url : systemOutInterfaceUrl) {
            if (matcher.match(url, request.getRequestURI())) {
                return false;
            }
        }
        return true;
    }

    public Object run() {
        if (this.casConfigOpen != null && this.casConfigOpen) {
            handleRequestUrlInterceptForCas();
        } else {
            handleRequestUrlIntercept();
        }
        return null;
    }

    private void handleRequestUrlIntercept() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            HttpServletRequest request = ctx.getRequest();
            log.info("网关AccessFilter拦截请求路径:" + request.getRequestURL());
            log.info("请求头Cookie信息:" + request.getHeader("Cookie"));
            String cookieToken = CommonUtil.getToken(request);
            if (StringUtils.isEmpty(cookieToken)) {
                ctx.setSendZuulResponse(false);
                throw new InvalidateRequestException(ExceptionConstants.ITMP_EXCEPTION_INVALIDATE_TOKEN, "token为空");
            }
            Map<String, Object> claims = JWTTokenUtils.parserJavaWebToken(cookieToken);
            if (claims == null || claims.isEmpty()) {
                ctx.setSendZuulResponse(false);
                throw new InvalidateRequestException(ExceptionConstants.ITMP_EXCEPTION_INVALIDATE_TOKEN, "非法token");
            }
            String userAccount = claims.get("userAccount") == null ? null : claims.get("userAccount").toString();
            if (StringUtils.isEmpty(userAccount)) {
                ctx.setSendZuulResponse(false);//不对当前请求路由
                throw new InvalidateRequestException(ExceptionConstants.ITMP_EXCEPTION_INVALIDATE_TOKEN, "非法token");
            }
            Map redisUser = (Map) redisUtils.get(cookieToken);
            if (redisUser == null || redisUser.isEmpty()) {
                ctx.setSendZuulResponse(false);
                throw new InvalidateRequestException(ExceptionConstants.ITMP_EXCEPTION_INVALIDATE_TOKENTIMEOUT_CODE, "token已过期");
            }
            if (!StringUtils.equals(userAccount, (String) redisUser.get("userAccount"))) {
                ctx.setSendZuulResponse(false);
                throw new InvalidateRequestException(ExceptionConstants.ITMP_EXCEPTION_INVALIDATE_TOKEN, "非法token");
            }
            String currentSessionUser = request.getSession().getAttribute(Constants.CURRENT_SESSION_USER) == null?"":request.getSession().getAttribute(Constants.CURRENT_SESSION_USER).toString();
            if (StringUtils.isBlank(currentSessionUser)||!StringUtils.equals(userAccount, currentSessionUser)) {
                ctx.setSendZuulResponse(false);
                throw new InvalidateRequestException(ExceptionConstants.ITMP_EXCEPTION_INVALIDATE_TOKEN, "非法token");
            }
            if (!authticateUrl(request, redisUser)) {
                ctx.setSendZuulResponse(false);
                throw new ZuulRuntimeException(new ZuulException(new NoPermissionException(), HttpServletResponse.SC_UNAUTHORIZED, "无访问权限"));//转发至网关SendErrorFilter,ErrorHandlerController
            }
            //重新刷新过期时间(过期时间默认30分钟)
            redisUtils.set(cookieToken, redisUser, Constants.ITMP_TOKEN_TIMEOUT);
        } catch (InvalidateRequestException e) {
            try {
                HttpServletResponse response = ctx.getResponse();
                response.sendRedirect(Constants.System.LOGOUT_URL);
            } catch (IOException e1) {
                log.error("zuul accessFilter redirect error！cause:" + e1.getMessage(), e1);
                throw new ZuulRuntimeException(new ZuulException(e1, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "zuul accessFilter redirect error！cause:" + e.getMessage()));
            }
        }
    }

    private void handleRequestUrlInterceptForCas() {
        //集成cas情况下不对token做校验
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("网关AccessFilter拦截请求路径:" + request.getRequestURL());
        String sessionToken = CommonUtil.getToken(request);
        Map redisUser = (Map) redisUtils.get(sessionToken);
        if (!authticateUrl(request, redisUser)) {
            ctx.setSendZuulResponse(false);
            throw new ZuulRuntimeException(new ZuulException(new NoPermissionException(), HttpServletResponse.SC_UNAUTHORIZED, "无访问权限"));//转发至网关SendErrorFilter,ErrorHandlerController
        }
        //重新刷新过期时间(过期时间默认30分钟)
        redisUtils.set(sessionToken, redisUser, Constants.ITMP_TOKEN_TIMEOUT);
    }


    /**
     * @param request
     * @param redisUserMap
     * @return java.lang.Boolean
     * @Description url鉴权
     * @MethodName authticateUrl
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/3/25 15:06
     */
    private Boolean authticateUrl(HttpServletRequest request, Map redisUserMap) {
        if (redisUserMap == null || redisUserMap.isEmpty()) {
            return false;
        }
        ArrayList<String> allMenuUrl = (ArrayList<String>) redisUtils.get("allMenuUrl");
        String uri = request.getRequestURI();
        if (CollectionUtil.isNotEmpty(allMenuUrl) && allMenuUrl.contains(uri)) {
            ArrayList<String> permissionUrls = (ArrayList<String>) redisUserMap.get("stringPermissionUrls");
            if (CollectionUtil.isNotEmpty(permissionUrls) && permissionUrls.contains(uri)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}