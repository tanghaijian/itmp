package cn.pioneeruniverse.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: cookie工具类
 * @Date: Created in 14:25 2019/5/10
 * @Modified By:
 */
public class CookieUtils {


    /**
     * @param request
     * @param cookieName
     * @return java.lang.Boolean
     * @Description 判断是否存在指定名称的cookie
     * @MethodName hasCookie
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/10 15:35
     */
    public static Boolean hasCookie(HttpServletRequest request, String cookieName) {
        return ReadCookieMap(request).containsKey(cookieName);
    }

    /**
     * @param request
     * @param cookieName
     * @return javax.servlet.http.Cookie
     * @Description 根据cookie名称获取cookie
     * @MethodName getCookieByName
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/10 15:40
     */
    public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(cookieName)) {
            return cookieMap.get(cookieName);
        } else {
            return null;
        }
    }

    /**
     * @param request
     * @param cookieName
     * @return java.lang.String
     * @Description 根据cookie名称获取cookie的值
     * @MethodName getCookieValueByName
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/10 15:48
     */
    public static String getCookieValueByName(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookieByName(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * @param response
     * @param name
     * @param value
     * @param path
     * @param maxAge
     * @return void
     * @Description 设置cookie
     * @MethodName setCookie
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/10 15:43
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge, Boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }


    /**
     * @param request
     * @return java.util.Map<java.lang.String,javax.servlet.http.Cookie>
     * @Description 用Map封装请求头中的cookie
     * @MethodName ReadCookieMap
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/10 15:29
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }


}
