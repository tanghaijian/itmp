package cn.pioneeruniverse.system.feignInterface.user;

import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.ResultDataDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.system.feignFallback.user.UserFallback;
import cn.pioneeruniverse.system.vo.company.Company;
import cn.pioneeruniverse.system.vo.dept.TblDeptInfo;
import cn.pioneeruniverse.system.vo.user.TblUserInfo;

/**
 * 
* @ClassName: UserInterface
* @Description: systemui模块和system模块间user通信接口
* @author author
* @date 2020年9月4日 上午10:36:34
*
 */
@FeignClient(value = "system", fallbackFactory = UserFallback.class)
public interface UserInterface {

	/**
	 * @deprecated
	* @Title: getCompanyOpt
	* @Description: 
	* @author author
	* @return Map<String,Object>
	 */
    @RequestMapping(value = "user/getCompanyOpt", method = RequestMethod.POST)
    Map<String, Object> getCompanyOpt();

    /**
     * @deprecated
    * @Title: isBindUserHF
    * @Description: 
    * @author author
    * @param userId
    * @param cardUID
    * @return String
     */
    @RequestMapping(value = "user/isBindUserHF", method = RequestMethod.POST)
    String isBindUserHF(@RequestParam("userId") Long userId, @RequestParam("cardUID") String cardUID);

    /**
     * @deprecated
    * @Title: updateBindHF
    * @Description: 
    * @author author
    * @param userId
    * @param writeUID
    * @return String
     */
    @RequestMapping(value = "user/updateBindHF", method = RequestMethod.POST)
    String updateBindHF(@RequestParam("userId") Long userId, @RequestParam("writeUID") String writeUID);

    /**
     * @deprecated
    * @Title: isBindUser
    * @Description: 
    * @author author
    * @param userId
    * @param cardUID
    * @return String
     */
    @RequestMapping(value = "user/isBindUser", method = RequestMethod.POST)
    String isBindUser(@RequestParam("userId") Long userId, @RequestParam("cardUID") String cardUID);

    /**
     * @deprecated
    * @Title: updateBind
    * @Description: 
    * @author author
    * @param userId
    * @param writeUID
    * @return String
     */
    @RequestMapping(value = "user/updateBind", method = RequestMethod.POST)
    String updateBind(@RequestParam("userId") Long userId, @RequestParam("writeUID") String writeUID);


    /**
     * @deprecated
    * @Title: saveUser
    * @Description:
    * @author author
    * @param newUser
    * @param roleIds
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/saveUser", method = RequestMethod.POST)
    Map<String, Object> saveUser(@RequestParam("newUser") String newUser, @RequestParam("roleIds") String roleIds);

    /**
     * @deprecated
    * @Title: updateUser
    * @Description: 
    * @author author
    * @param newUser
    * @param roleIds
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/updateUser", method = RequestMethod.POST)
    Map<String, Object> updateUser(@RequestParam("newUser") String newUser, @RequestParam("roleIds") String roleIds);

    /**
     * @deprecated
    * @Title: findUserById
    * @Description: 
    * @author author
    * @param userId
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/findUserById", method = RequestMethod.POST)
    Map<String, Object> findUserById(@RequestParam("userId") Long userId);

    /**
     * @deprecated
    * @Title: getUserWithRoles
    * @Description: 
    * @author author
    * @param userAccount
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/getUserWithRoles", method = RequestMethod.POST)
    Map<String, Object> getUserWithRoles(@RequestParam("userAccount") String userAccount);


    /**
     * @deprecated
    * @Title: updatePassword
    * @Description: 
    * @author author
    * @param user
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/updatePassword", method = RequestMethod.POST)
    Map<String, Object> updatePassword(@RequestBody TblUserInfo user);


    /**
     * @deprecated
    * @Title: resetPassword
    * @Description: 
    * @author author
    * @param user
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/resetPassword", method = RequestMethod.POST)
    Map<String, Object> resetPassword(@RequestBody TblUserInfo user);

    /*//获取列表信息
    @RequestMapping(value="user/getAllUser",method=RequestMethod.POST)
    Map getAllUser(@RequestParam("FindUser") String FindUser, @RequestParam("page") Integer pageIndex, @RequestParam("rows") Integer pageSize);
    */
    
    /**
     * @deprecated
    * @Title: getCompany
    * @Description: 
    * @author author
    * @return List<Company>
     */
    @RequestMapping(value = "user/getCompany", method = RequestMethod.POST)
    List<Company> getCompany();

    
    /**
     * @deprecated
    * @Title: getDept
    * @Description: 
    * @author author
    * @return List<TblDeptInfo>
     */
    @RequestMapping(value = "user/getDept", method = RequestMethod.POST)
    List<TblDeptInfo> getDept();

    /**
     * @deprecated
    * @Title: getPreUser
    * @Description: 
    * @author author
    * @param userAccount
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/getPreUser", method = RequestMethod.POST)
    Map<String, Object> getPreUser(@RequestParam("userAccount") String userAccount);

    /**
     * @deprecated
    * @Title: delUser
    * @Description: 
    * @author author
    * @param userIds
    * @param lastUpdateBy
    * @return Map<String,Object>
     */
    @RequestMapping(value = "user/delUser", method = RequestMethod.POST)
    Map<String, Object> delUser(@RequestParam("userIds") String userIds, @RequestParam("lastUpdateBy") Long lastUpdateBy);

    /**
     * 
    * @Title: login
    * @Description: 普通登录方式
    * @author author
    * @param userAccount 账号
    * @param password 密码
    * @return ResultDataDTO 用户信息
     */
    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    ResultDataDTO login(@RequestParam("userAccount") String userAccount, @RequestParam("password") String password);


    /**
     * 
    * @Title: afterCasLogin
    * @Description: cas登录后续操作
    * @author author
    * @param userCode 账号
    * @param userName 姓名
    * @return ResultDataDTO 返回生成的token值
     */
    @RequestMapping(value = "user/afterCasLogin", method = RequestMethod.POST)
    ResultDataDTO afterCasLogin(@RequestParam("userCode") String userCode, @RequestParam("userName") String userName);


}
