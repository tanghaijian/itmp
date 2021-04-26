package cn.pioneeruniverse.system.service.user;

import cn.pioneeruniverse.system.entity.TblUserInfo;

import javax.security.auth.login.LoginException;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:06 2019/1/4
 * @Modified By:
 */
public interface ITaskPluginUserService {

    /**
     * @param user
     * @param loginIp
     * @param password
     * @return java.lang.Object
     * @Description 任务管理插件验证登录成功后操作
     * @MethodName afterLoginCheck
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 15:10
     */
    Object afterLoginCheck(TblUserInfo user, String password, String loginIp);

    /**
     * @param username
     * @return void
     * @Description 任务管理插件登出
     * @MethodName logout
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 15:30
     */
    void logout(String username);

    /**
     * @param userAccount
     * @param password
     * @return cn.pioneeruniverse.system.entity.TblUserInfo
     * @Description 任务管理插件验证登录
     * @MethodName checkLogin
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/10 13:52
     */
    TblUserInfo checkLogin(String userAccount, String password) throws LoginException;


    /**
     * @param username
     * @param userToken
     * @param loginIp
     * @return java.lang.Object
     * @Description 任务管理插件登录状态校验
     * @MethodName loginStatusCheck
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 15:34
     */
    Object loginStatusCheck(String username, String userToken, String loginIp) throws LoginException;

}
