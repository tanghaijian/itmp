package cn.pioneeruniverse.system.service.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.entity.BootStrapTablePage;

import com.baomidou.mybatisplus.service.IService;

import cn.pioneeruniverse.system.entity.TblDeptInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.entity.TblUserRole;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;

public interface IUserService extends IService<TblUserInfo> {

    void insertUser(TblUserInfo user, List<TblUserRole> list);

    void updateUser(TblUserInfo user, List<TblUserRole> list);

    TblUserInfo getUserAcc(String userAccount);

    TblUserInfo findUserById(Long id);

    TblUserInfo getUserRoleByAcc(String userAccount);

    void updatePassword(TblUserInfo user);

    void resetPassword(TblUserInfo user);

    Long getPreAccUser(String userAccount);

    void delUser(List<Long> userIds, Long lastUpdateBy);

    String selectMaxEmpNo();

    //获取所有数据
    List<TblUserInfo> getExcelAllUser(TblUserInfo user);

    //获取用户信息
    Map getAllUser(TblUserInfo user, Integer pageIndex, Integer PageSize);

    List<TblUserInfo> getUser();

    List<Map<String, Object>> itmpInnerUserData(String userList, String password);

    List<Map<String, Object>> itmpExtUserData(String userList, String password);

    /**
     * @param userScmAccount
     * @return cn.pioneeruniverse.common.dto.TblUserInfoDTO
     * @Description 通过SVN账号获取svn用户详情,参数支持多个
     * @MethodName getSvnUserDetailByUserAccount
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 14:46
     */
    List<TblUserInfoDTO> getCodeBaseUserDetailByUserScmAccount(String userScmAccounts);

    /**
     * @param bootStrapTablePage
     * @param tblUserInfoDTO
     * @return cn.pioneeruniverse.common.entity.BootStrapTablePage<cn.pioneeruniverse.common.dto.TblUserInfoDTO>
     * @Description 获取代码库用户分页数据（bootstraptable分页)
     * @MethodName getUserPageForSvn
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 14:47
     */
    BootStrapTablePage<TblUserInfoDTO> getUserPageForCodeBase(BootStrapTablePage<TblUserInfoDTO> bootStrapTablePage, TblUserInfoDTO tblUserInfoDTO);

    /**
     * @param bootStrapTablePage
     * @param tblUserInfoDTO
     * @param currentUserId
     * @return cn.pioneeruniverse.common.entity.BootStrapTablePage<cn.pioneeruniverse.common.dto.TblUserInfoDTO>
     * @Description 获取代码评审管理页面开发人员列表分页数据
     * @MethodName getDevUserPageForCodeReview
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/3/27 14:08
     */
    BootStrapTablePage<TblUserInfoDTO> getDevUserPageForCodeReview(BootStrapTablePage<TblUserInfoDTO> bootStrapTablePage, TblUserInfoDTO tblUserInfoDTO, Long currentUserId);

    /**
     * @param devUserId
     * @param codeReviewUserIds
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Description 获取代码评审开发人员名称及评审人名称
     * @MethodName getDevUserNameAndReviewersNameForCodeReivew
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/23 17:41
     */
    Map<String, String> getDevUserNameAndReviewersNameForCodeReivew(Long devUserId, String codeReviewUserIds);

    BootStrapTablePage<TblUserInfo> getAllUserModal(BootStrapTablePage<TblUserInfo> bootStrapTablePage, TblUserInfo userInfo, Long systemId, Long notUserID, String projectIds, String userPost);


    /**
     * @param userAccount
     * @param password
     * @return cn.pioneeruniverse.system.entity.TblUserInfo
     * @Description 登录校验
     * @MethodName checkLogin
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 14:45
     */
    TblUserInfo checkLogin(String userAccount, String password) throws AccountException, CredentialException;

    /**
     * @param user
     * @return java.lang.Object
     * @Description 系统登录验证后操作(1.绑定菜单, 角色权限 2.生成token)
     * @MethodName afterLoginCheck
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 16:20
     */
    Object afterLoginCheck(TblUserInfo user);

    /**
     * @param userCode
     * @param userName
     * @return java.lang.Object
     * @Description 单点登录验证成功后操作
     * @MethodName afterCasLogin
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/3/14 15:01
     */
    Object afterCasLogin(String userCode, String userName) throws AccountException;

    List<Map<String, Object>> getAllDevUser(String deptName, String companyName, String userName, Integer notWithUserID, Integer devID, Integer pageNumber, Integer pageSize);

    List<Map<String, Object>> getAllTestUser(TblUserInfo tblUserInfo, Integer notWithUserID, Integer devID, Integer pageNumber, Integer pageSize);

    int tmpUserData(TblUserInfo tblUserInfo);

    /**
     * 查询项目组用户
     *
     * @param tblUserInfo
     * @param notWithUserID
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectById(String deptName, String companyName, String userName, Long userId, Long notWithUserID, String status,Integer pageNumber, Integer pageSize);

    /**
     * 根据项目组ID查询
     *
     * @param tblUserInfo
     * @param notWithUserID
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> selectByProjectId(TblUserInfo tblUserInfo, Long notWithUserID, Integer pageNumber, Integer pageSize);

    List<TblUserInfo> getAllUserModal2(TblUserInfo userInfo, String projectIds, Integer pageNumber, Integer pageSize);

    List<TblDeptInfo> getAllDept(TblDeptInfo tblDeptInfo, Integer pageNumber, Integer pageSize);

    Map<String, String> createSvnAccountPassword(Long userId);

    Integer getAllUserModalCount2(TblUserInfo userInfo, String projectIds);

    /**
     * @param oldPassword
     * @param currentUserId
     * @return java.lang.Boolean
     * @Description 检测svn旧密码
     * @MethodName checkMyOldSvnPassword
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/29 16:24
     */
    Boolean checkMyOldSvnPassword(String oldPassword, Long currentUserId) throws IOException, NoSuchAlgorithmException;

    void updateMySvnPassword(Long currentUserId, String userScmAccount, String newPassword, HttpServletRequest request) throws NoSuchAlgorithmException, AccountException;

    List<TblRoleInfo> getRoles();

    Long getIdByNum(TblUserInfo user);

    Long getIdByUserAccount(TblUserInfo user);

    void insertTmpUser(TblUserInfo user, List<TblUserRole> list);

    void updateTmpUser(TblUserInfo user, List<TblUserRole> list);

    List<Map<String, Object>> getAllproject();

    BootStrapTablePage<TblUserInfo> getAllUserModal2(BootStrapTablePage<TblUserInfo> bootStrapTablePage, TblUserInfo userInfo, String projectIds);

    List<TblUserInfoDTO> getBatchUserInfoByIds(List<Long> ids);

	BootStrapTablePage<TblUserInfo> getAllUserModal3(BootStrapTablePage bootStrapTablePage, TblUserInfo userInfo,
			String projectIds, String ids);

	void updateGitPassword(Long userId, String userScmAccount, String svnDefaultPassword, HttpServletRequest request) throws AccountException;

	void updateMySvnAndGitPassword(Long currentUserId, String userScmAccount,String oldPassword, String newPassword,
			HttpServletRequest request) throws AccountException;
	
	/**
	 * 
	 * @Title: getUserByNameOrACC
	 * @Description: 根据用户名或账号模糊查询人员信息
	 * @param id
	 * @param userName
	 * @return
	 * @author wangwei
	 * @date 2020年8月14日
	 */
	List<TblUserInfo> getUserByNameOrACC(Long id,String userName);
}
