package cn.pioneeruniverse.common.velocity.tag;

import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 页面自定义函数auth实现类
 * @Date: Created in 18:19 2018/12/6
 * @Modified By:
 */
public class VelocityAuthority {

    private static RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);
    private static final Logger logger = LoggerFactory.getLogger(VelocityAuthority.class);

    /**
     * 
    * @Title: hasPermission
    * @Description: 判断某人是否有某个按钮权限
    * @author author
    * @param token 登录后的token
    * @param permission 按钮编码
    * @return
    * @throws
     */
    public boolean hasPermission(String token, String permission) {
        boolean hasPermission = false;
        try {
            Object redisUser = redisUtils.get(token);
            if (redisUser != null) {
                Map redisUserMap = (Map) redisUser;
                ArrayList<String> stringPermissions = (ArrayList<String>) redisUserMap.get("stringPermissions");
                hasPermission = stringPermissions.contains(permission);
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
        }
        return hasPermission;
    }


    /**
     * 
    * @Title: hasAnyPermission
    * @Description: 判断某人具有一系权限中的某个权限
    * @author author
    * @param token 登陆后的token
    * @param permissions 一系列按钮编码
    * @return
    * @throws
     */
    public boolean hasAnyPermission(String token, String... permissions) {
        boolean hasAnyPermission = false;
        try {
            Object redisUser = redisUtils.get(token);
            if (redisUser != null) {
                Map redisUserMap = (Map) redisUser;
                ArrayList<String> stringPermissions = (ArrayList<String>) redisUserMap.get("stringPermissions");
                for (String permission : permissions) {
                    if (stringPermissions.contains(permission)) {
                        hasAnyPermission = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
        }
        return hasAnyPermission;
    }


    /**
     * 
    * @Title: hasAllPermission
    * @Description: 判断某人具有列出的所有权限
    * @author author
    * @param token 登录token
    * @param permissions 一系列按钮编码组合
    * @return
    * @throws
     */
    public boolean hasAllPermission(String token, String... permissions) {
        boolean hasAnyPermission = false;
        try {
            Object redisUser = redisUtils.get(token);
            if (redisUser != null) {
                Map redisUserMap = (Map) redisUser;
                ArrayList<String> stringPermissions = (ArrayList<String>) redisUserMap.get("stringPermissions");
                if (stringPermissions.containsAll(Arrays.asList(permissions))) {
                    hasAnyPermission = true;
                }
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
        }
        return hasAnyPermission;
    }


    /**
     * 
    * @Title: lacksPermission
    * @Description: 判断某人是否不具有某个权限
    * @author author
    * @param token 登陆后的token
    * @param permission 按钮编码
    * @return
    * @throws
     */
    public boolean lacksPermission(String token, String permission) {
        boolean lacksPermission = false;
        try {
            Object redisUser = redisUtils.get(token);
            if (redisUser != null) {
                Map redisUserMap = (Map) redisUser;
                ArrayList<String> stringPermissions = (ArrayList<String>) redisUserMap.get("stringPermissions");
                lacksPermission = !stringPermissions.contains(permission);
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
        }
        return lacksPermission;
    }

    public boolean hasRole(String token, String role) {
        boolean hasRole = false;
        try {
            Object redisUser = redisUtils.get(token);
            if (redisUser != null) {
                Map redisUserMap = (Map) redisUser;
                ArrayList<String> roles = (ArrayList<String>) redisUserMap.get("roles");
                hasRole = roles.contains(role);
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
        }
        return hasRole;
    }


}
