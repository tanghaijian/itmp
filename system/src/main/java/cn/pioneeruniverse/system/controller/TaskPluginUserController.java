package cn.pioneeruniverse.system.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.service.user.ITaskPluginUserService;
import cn.pioneeruniverse.system.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: eclipse任务管理插件请求接口
 * @Date: Created in 12:16 2018/11/21
 * @Modified By:
 */
@RestController
@RequestMapping("/taskPlugin/user")
public class TaskPluginUserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ITaskPluginUserService iTaskPluginUserService;

    @Value("${task.plugin.esb.login.check}")
    private Boolean taskPluginEsbLoginCheck;


    private static final Logger logger = LoggerFactory.getLogger(TaskPluginUserController.class);


    /**
     * @param username
     * @param password
     * @param loginIp
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Description任务管理插件登录请求
     * @MethodName login
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/21 15:04
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDataDTO login(String username, String password, String loginIp) {
        TblUserInfo user;
        try {
            if (taskPluginEsbLoginCheck) {
                //验证通过大地接口来完成
                user = iTaskPluginUserService.checkLogin(username, password);
            } else {
                //验证通过平台用户表来完成
                user = iUserService.checkLogin(username, password);
            }
            Map<String, Object> dataMap = (Map<String, Object>) iTaskPluginUserService.afterLoginCheck(user, password, loginIp);
            return ResultDataDTO.SUCCESS(Constants.TaskPlugin.SUCCESS_CODE, "登录成功", dataMap);
        } catch (LoginException e) {
            return ResultDataDTO.FAILURE(Constants.TaskPlugin.FAILURE_CODE, "登录失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("登录异常，服务器端异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL(Constants.TaskPlugin.ABNORMAL_CODE, "登录异常，服务器端异常原因：" + e.getMessage());
        }
    }

    /**
     * @param username
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description 任务管理插件注销请求
     * @MethodName logout
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/22 13:41
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultDataDTO logout(String username) {
        try {
            iTaskPluginUserService.logout(username);
            return ResultDataDTO.SUCCESS(Constants.TaskPlugin.SUCCESS_CODE, "注销成功");
        } catch (Exception e) {
            logger.error("注销异常，服务器端异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL(Constants.TaskPlugin.ABNORMAL_CODE, "注销异常，服务器端异常原因：" + e.getMessage());
        }
    }

    /**
     * @param username
     * @param userToken
     * @param loginIp
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Description 登录状态校验请求
     * @MethodName checkLogined
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/22 15:08
     */
    @RequestMapping(value = "/checkLogined", method = RequestMethod.POST)
    public ResultDataDTO checkLogined(String username, String userToken, String loginIp) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) iTaskPluginUserService.loginStatusCheck(username, userToken, loginIp);
            return ResultDataDTO.SUCCESS(Constants.TaskPlugin.SUCCESS_CODE, "验证成功", dataMap);
        } catch (LoginException e) {
            return ResultDataDTO.FAILURE(Constants.TaskPlugin.FAILURE_CODE, "验证失败," + e.getMessage());
        } catch (Exception e) {
            logger.error("验证异常，服务器端异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL(Constants.TaskPlugin.ABNORMAL_CODE, "验证异常，服务器端异常原因：" + e.getMessage());
        }
    }

}
