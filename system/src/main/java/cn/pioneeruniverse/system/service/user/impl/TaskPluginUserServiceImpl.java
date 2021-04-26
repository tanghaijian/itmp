package cn.pioneeruniverse.system.service.user.impl;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.*;
import cn.pioneeruniverse.system.dao.mybatis.user.UserDao;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.service.user.ITaskPluginUserService;
import cn.pioneeruniverse.system.service.user.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName: TaskPluginUserServiceImpl
* @Description: eclipse插件回调类
* @author author
* @date 2020年9月3日 上午10:09:37
*
 */
@Service("iTaskPluginUserService")
public class TaskPluginUserServiceImpl implements ITaskPluginUserService {

    private static final Logger logger = LoggerFactory.getLogger(TaskPluginUserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IUserService iUserService;

    //插件登录验证接口
    @Value("${task.plugin.esb.service.addr}")
    private String taskPluginEsbServiceAddr;

    //是否通过esb登录，true是，false直接通过系统内部登录
    @Value("${task.plugin.esb.login.check}")
    private Boolean taskPluginEsbLoginCheck;

    //接口consumerId
    @Value("${task.plugin.esb.login.consumer.id}")
    private String taskPluginEsbLoginConsumerId;

    /**
     * 
    * @Title: afterLoginCheck
    * @Description: 登录成功后操作：生成jwttoken
    * @author author
    * @param user 登录用户
    * @param password 密码
    * @param loginIp ip
    * @return map  key realName :姓名
    *                  userName :账号
    *                  id:          用户ID
    *                  userToken:   jwtToken
     */
    @Override
    public Object afterLoginCheck(TblUserInfo user, String password, String loginIp) {
        //将用户ip地址填写至redis中
        redisUtils.setForHash(Constants.TaskPlugin.REDIS_SUFFIX + user.getUserAccount(), "loginIp", loginIp);
        //生成当前已验证登录成功用户token
        String token = JWTTokenUtils.createJavaWebToken(new HashMap<String, Object>() {{
            put("userAccount", user.getUserAccount());
            put("userName", user.getUserName());
            put("userPassword", password);
        }});
        Map<String, Object> dataMap = new HashMap<String, Object>() {{
            put("id", user.getId());
            put("userName", user.getUserAccount());
            put("realName", user.getUserName());
            put("userToken", token);
        }};
        return dataMap;
    }

    /**
     * 
    * @Title: logout
    * @Description: 登出
    * @author author
    * @param username 账号
     */
    @Override
    public void logout(String username) {
        //清空redis中loginIp
        redisUtils.delForHash(Constants.TaskPlugin.REDIS_SUFFIX + username, "loginIp");
    }

    /**
     * 
    * @Title: checkLogin
    * @Description: 同esb通信登录
    * @author author
    * @param userAccount 账号
    * @param password 密码
    * @return TblUserInfo 用户信息
    * @throws LoginException
     */
    @Override
    @Transactional(readOnly = true)
    public TblUserInfo checkLogin(String userAccount, String password) throws LoginException {
    	//报文头
        Map<String, String> requestHead = new HashMap<String, String>() {{
            put("seqNo", "");
            put("consumerSeqNo", "");
            put("consumerID", taskPluginEsbLoginConsumerId);
            put("providerID", "");
            put("classCode", "");
            put("riskCode", "");
            put("regionCode", "");
            put("version", "");
        }};
        
        //报文体
        Map<String, String> requestBody = new HashMap<String, String>() {{
            put("usercode", userAccount);
            put("password", password);
            put("authtype", "passwordAuth");
            put("ip", "");
            put("mac", "");
        }};
        
        //完整报文
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>() {{
            put("requestHead", requestHead);
            put("requestBody", requestBody);
        }};
        String resultJson = HttpUtil.doPost(taskPluginEsbServiceAddr, JsonUtil.toJson(map), "UTF-8");
        Map<String, Map<String, String>> result = JsonUtil.fromJson(resultJson, HashMap.class);
        if (result == null || result.isEmpty()) {
            logger.error("插件登录验证接口返回结果为空");
            throw new LoginException("验证未通过");
        }
        Map<String, String> responseHead = result.get("responseHead");
        if (!StringUtils.equals(responseHead.get("status"), "0")) {
            logger.error("插件登录验证接口返回失败，status:" + responseHead.get("status") + ";esbMessage:" + requestHead.get("esbMessage") + ";appMessage:" + requestHead.get("appMessage"));
            throw new LoginException("验证未通过");
        } else {
            return userDao.getUserAcc(userAccount);
        }
    }

    /**
     * 
    * @Title: loginStatusCheck
    * @Description: 登录
    * @author author
    * @param username 账号
    * @param userToken 加密后的token，主要取密码
    * @param loginIp 登录IP
    * @return Object：如果是esb登录，则为TblUserInfo，否则为map
    * @throws LoginException
     */
    @Override
    public Object loginStatusCheck(String username, String userToken, String loginIp) throws LoginException {
        TblUserInfo user;
        String password = JWTTokenUtils.parserJavaWebToken(userToken).get("userPassword").toString();
       //esb方式登录
        if (taskPluginEsbLoginCheck) {
            user = this.checkLogin(username, password);
        } else {
        	//系统内登录
            user = iUserService.checkLogin(username, password);
        }
        //校验ip地址与redis中是否依然匹配
        String loginedIp = (String) redisUtils.getForHash(Constants.TaskPlugin.REDIS_SUFFIX + user.getUserAccount(), "loginIp");
        if (StringUtils.isEmpty(loginedIp) || !StringUtils.equals(loginIp, loginedIp)) {
            throw new LoginException("ip地址不匹配");
        }
        return this.afterLoginCheck(user, password, loginIp);
    }

}

