package cn.pioneeruniverse.dev.feignInterface;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.dev.feignFallback.DevManageToSystemFallback;

/**
 * 
* @ClassName: DevManageToSystemInterface
* @Description: 开发和系统关联调用模块
* @author author
* @date 2020年8月12日 下午9:19:14
*
 */
@FeignClient(value = "system", fallbackFactory = DevManageToSystemFallback.class)
public interface DevManageToSystemInterface {

	/**
	 * 
	* @Title: getCodeBaseUsersDetail
	* @Description: 获取代码库的用户信息 （未用）
	* @author author
	* @param svnUserAuth 
	* @return
	* @throws
	 */
    @RequestMapping(value = "/user/getCodeBaseUsersDetail", method = RequestMethod.POST)
    List<TblUserInfoDTO> getCodeBaseUsersDetail(@RequestParam("svnUserAuth") Map<String, String> svnUserAuth);

    /**
     * 
    * @Title: getCodeBaseUserDetailByUserScmAccount
    * @Description: 通过仓库账号获取用户信息
    * @author author
    * @param userScmAccount 仓库账号
    * @return TblUserInfoDTO
    * @throws
     */
    @RequestMapping(value = "/user/getCodeBaseUserDetailByUserScmAccount", method = RequestMethod.POST)
    TblUserInfoDTO getCodeBaseUserDetailByUserScmAccount(@RequestParam("userScmAccount") String userScmAccount);
    
    /**
     * 
    * @Title: getCodeBaseUserDetailByUserScmAccountList
    * @Description: 通过仓库账号列表获取用户
    * @author author
    * @param userScmAccounts 账号列表1,2,3类似
    * @return
    * @throws
     */
    @RequestMapping(value = "/user/getCodeBaseUserDetailByUserScmAccountList", method = RequestMethod.POST)
    List<TblUserInfoDTO> getCodeBaseUserDetailByUserScmAccountList(@RequestParam("userScmAccounts") String userScmAccounts);

    /**
     * 
    * @Title: getDevUserNameAndReviewersNameForCodeReivew
    * @Description: 获取代码评审时的开发人员和评审人员信息
    * @author author
    * @param devUserId 开发人员ID
    * @param codeReviewUserIds 多位评审人员ID
    * @return map key -devUserName:开发人员姓名
    *                  codeReviewUserNames：代码评审人员
    * @throws
     */
    @RequestMapping(value = "/user/getDevUserNameAndReviewersNameForCodeReivew", method = RequestMethod.POST)
    Map<String, String> getDevUserNameAndReviewersNameForCodeReivew(@RequestParam("devUserId") Long devUserId, @RequestParam("codeReviewUserIds") String codeReviewUserIds);

    /**
     * 
    * @Title: findUserById
    * @Description: 通过ID获取用户信息
    * @author author
    * @param userId 用户ID
    * @return map key1 status 1正常返回，2异常返回
    *             key2 errorMessage：错误消息
    *             data 返回数据
    * @throws
     */
    @RequestMapping(value = "user/findUserById", method = RequestMethod.POST)
    Map<String, Object> findUserById(@RequestParam("userId") Long userId);

    /**
     * 
    * @Title: selectDeptById （未用）
    * @Description: 通过部门ID获取部门信息
    * @author author
    * @param id 部门ID
    * @return map Key：status=1 正常返回 ，2异常返回
	*                  data返回的数据
    * @throws
     */
    @RequestMapping(value = "dept/selectDeptById", method = RequestMethod.POST)
    Map<String, Object> selectDeptById(@RequestParam("id") Long id);

    /**
     * 
    * @Title: findRolesByUserID （未用）
    * @Description: 获取用户角色
    * @author author
    * @param currentUserId 用户ID
    * @return map key status:1正常，2异常
	*                 roles：角色信息
    * @throws
     */
    @RequestMapping(value = "role/findRolesByUserID", method = RequestMethod.POST)
    Map<String, Object> findRolesByUserID(Long currentUserId);

    /**
     * 
    * @Title: findFieldByTableName
    * @Description: 通过表名获取自定义属性
    * @author author
    * @param tableName 表名
    * @return map key: type-->map1 数据字典TBL_CUSTOM_FIELD_TEMPLATE_TYPE ，fieldTemp-->fieldTemp:数据库默认字段
    * @throws
     */
    @RequestMapping(value = "/fieldTemplate/findFieldByTableName", method = RequestMethod.POST)
    Map<String, Object> findFieldByTableName(@RequestParam("tableName") String tableName);

    /**
     * 
    * @Title: createSvnAccountPassword
    * @Description: 创建SVN账号
    * @author author
    * @param userId 用户ID
    * @return map key username:svn用户名，password：svn密码
    * @throws
     */
    @RequestMapping(value = "/user/createSvnAccountPassword", method = RequestMethod.POST)
    Map<String, String> createSvnAccountPassword(@RequestParam("userId") Long userId);

    /**
     * 
    * @Title: insertMessage
    * @Description: 插入消息
    * @author author
    * @param messageJson TblMessageInfo json化字符串
    * @return map Key status 1正常，2异常
    * @throws
     */
    @RequestMapping(value = "/message/insertMessage", method = RequestMethod.POST)
    Map<String, String> insertMessage(@RequestParam("messageJson") String messageJson);
    
    /**
     * 
    * @Title: sendMessage
    * @Description: 发送消息
    * @author author
    * @param messageJson  TblMessageInfo json化字符串
    * @return map key -status=1正常，2异常
    * @throws
     */
    @RequestMapping(value = "/message/sendMessage", method = RequestMethod.POST)
    Map<String, String> sendMessage(@RequestParam("messageJson") String messageJson);

    /**
     * 
    * @Title: getMenuByCode
    * @Description: 获取菜单按钮信息
    * @author author
    * @param menuButtonCode 菜单按钮编码
    * @return TblMenuButtonInfo 的key-value
    * @throws
     */
    @RequestMapping(value = "/menu/getMenuByCode", method = RequestMethod.POST)
    Map<String, String> getMenuByCode(@RequestParam("menuButtonCode") String menuButtonCode);

}
