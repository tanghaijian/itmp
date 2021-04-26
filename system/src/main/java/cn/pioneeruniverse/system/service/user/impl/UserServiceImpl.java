package cn.pioneeruniverse.system.service.user.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.JWTTokenUtils;
import cn.pioneeruniverse.common.utils.PageWithBootStrap;
import cn.pioneeruniverse.common.utils.PasswordUtil;
import cn.pioneeruniverse.common.utils.PinYinUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.system.dao.mybatis.dept.DeptDao;
import cn.pioneeruniverse.system.dao.mybatis.role.RoleDao;
import cn.pioneeruniverse.system.dao.mybatis.role.UserRoleDao;
import cn.pioneeruniverse.system.dao.mybatis.user.UserDao;
import cn.pioneeruniverse.system.entity.TblDeptInfo;
import cn.pioneeruniverse.system.entity.TblMenuButtonInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.entity.TblUserRole;
import cn.pioneeruniverse.system.feignInterface.SystemToDevManageInterface;
import cn.pioneeruniverse.system.service.menu.IMenuService;
import cn.pioneeruniverse.system.service.role.IRoleService;
import cn.pioneeruniverse.system.service.role.IUserRoleService;
import cn.pioneeruniverse.system.service.user.IUserService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("iUserService")
public class UserServiceImpl extends ServiceImpl<UserDao, TblUserInfo> implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserDao userDao;

    @Autowired
    private DeptDao deptDao;
    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private SystemToDevManageInterface systemToDevManageInterface;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Value("${svn.default.password}")
    private String svnDefaultPassword;


    private static Pattern urlPattern = Pattern.compile("[/[\\w]{1,}]{1,}(\\?.+?)*");

    /**
     * 
    * @Title: getAllUser
    * @Description: 获取用户管理查询列表
    * @author author
    * @param user 封装的查询条件
    * @param pageIndex 第几页
    * @param pageSize 每页大小
    * @return map  key:rows       value:List<TblUserInfo>用户列表信息
    *                  records          总条数
    *                  total            总页数
    *                  page             第几页
     */
    @Override
    public Map getAllUser(TblUserInfo user, Integer pageIndex, Integer pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (pageIndex != null && pageSize != null) {

            PageHelper.startPage(pageIndex, pageSize);
            List<TblUserInfo> list = userDao.getAllUser(user);
            PageInfo<TblUserInfo> pageInfo = new PageInfo<TblUserInfo>(list);
            result.put("rows", list);
            result.put("records", pageInfo.getTotal());
            result.put("total", pageInfo.getPages());
            result.put("page", pageIndex < pageInfo.getPages() ? pageIndex : pageInfo.getPages());
            return result;

        } else {
            result.put("rows", userDao.getAllUser(user));
        }

        return result;
    }


    /**
     * 
    * @Title: getExcelAllUser
    * @Description: 获取需要导出的人员信息
    * @author author
    * @param user 查询条件
    * @return List<TblUserInfo>用户列表
     */
    @Override
    public List<TblUserInfo> getExcelAllUser(TblUserInfo user) {
        List<TblUserInfo> list = userDao.getAllUser(user);
        return list;
    }


    /**
     * 
    * @Title: updateUser
    * @Description: 开发库更新用户
    * @author author
    * @param user 需要更新的用户信息
    * @param list 需要更新的用户角色关联信息
     */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUser(TblUserInfo user, List<TblUserRole> list) {
        userDao.updateUser(user);
        //更新用户角色关联信息前先删除原有的用户角色关联信息
        iUserRoleService.delUserRoleByUserId(user.getId());
        if (list != null && !list.isEmpty()) {
            for (TblUserRole userRole : list) {
                userRole.setUserId(user.getId());
            }
            iUserRoleService.insertUserRole(list);
        }
        //更新测试库的用户
        SpringContextHolder.getBean(IUserService.class).updateTmpUser(user, list);
    }

    /**
     * 
    * @Title: updateTmpUser
    * @Description: 更新测试库的用户信息
    * @author author
    * @param user 需要更新的用户信息
    * @param list 需要更新的用户角色关联信息
     */
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateTmpUser(TblUserInfo user, List<TblUserRole> list) {
        userDao.updateUser(user);
        //更新用户角色关联信息前先删除原有的用户角色关联信息
        iUserRoleService.delUserRoleByUserId(user.getId());
        if (list != null && !list.isEmpty()) {
            for (TblUserRole userRole : list) {
                userRole.setUserId(user.getId());
            }
            iUserRoleService.insertUserRole(list);
        }
    }

    /**
     * 
    * @Title: getUserAcc
    * @Description: 通过用户账号获取用户信息
    * @author author
    * @param userAccount 用户账号
    * @return TblUserInfo用户信息
     */
    @Override
    public TblUserInfo getUserAcc(String userAccount) {
        return userDao.getUserAcc(userAccount);
    }

    /**
     * 
    * @Title: findUserById
    * @Description: 通过ID获取用户，包含用户的角色
    * @author author
    * @param id 用户ID
    * @return TblUserInfo用户信息
     */
    @Override
    public TblUserInfo findUserById(Long id) {
        TblUserInfo user = userDao.findUserById(id);
        List<Long> roleIds = userRoleDao.findRoleIdByUserId(id);
        user.setRoleIds(roleIds);
        return user;
    }
    
    /**
     * 
    * @Title: getUserRoleByAcc
    * @Description: 通过用户账号获取用户角色
    * @author author
    * @param userAccount 用户账号
    * @return TblUserInfo用户信息
     */
    @Override
    public TblUserInfo getUserRoleByAcc(String userAccount) {
        return userDao.getUserRoleByAcc(userAccount);
    }

    /**
     * 
    * @Title: updatePassword
    * @Description: 更新密码
    * @author author
    * @param user 用户信息
     */
    @Override
    @Transactional(readOnly = false)
    public void updatePassword(TblUserInfo user) {
        userDao.updatePassword(user);
    }

    /**
     * 
    * @Title: resetPassword
    * @Description: 重置密码
    * @author author
    * @param user 用户信息
     */
    @Override
    @Transactional(readOnly = false)
    public void resetPassword(TblUserInfo user) {
        userDao.resetPassword(user);
    }

    /**
     * 
    * @Title: getPreAccUser
    * @Description: 获取帐号为同样前缀的用户，用于判断是否存在相同的用户名
    * @author author
    * @param userAccount 用户账号
    * @return Long 相同用户账号或前缀相同的用户数量
     */
    @Override
    public Long getPreAccUser(String userAccount) {
        return userDao.getPreAccUser(userAccount);
    }


    /**
     * 
    * @Title: insertUser
    * @Description: 开发库新增用户
    * @author author
    * @param user 需要新增的用户
    * @param list 用户角色关联信息
     */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertUser(TblUserInfo user, List<TblUserRole> list) {
//        String maxEmpNo = this.selectMaxEmpNo();
//        String empNo = Constants.ITMP_EMP_PREFIX + "00001";
//        if (StringUtils.isNotBlank(maxEmpNo)) {
//            Integer no = Integer.valueOf(maxEmpNo.substring(Constants.ITMP_EMP_PREFIX.length())) + 1;
//            empNo = Constants.ITMP_EMP_PREFIX + String.format("%05d", no);
//        }
//        user.setEmployeeNumber(empNo);
        userDao.insertUser(user);
        //新增用户角色关联信息
        if (list != null && !list.isEmpty()) {
            for (TblUserRole userRole : list) {
                userRole.setUserId(user.getId());
            }
            iUserRoleService.insertUserRole(list);
        }
        //同步测试库
        SpringContextHolder.getBean(IUserService.class).insertTmpUser(user, list);
    }

    
    /**
     * 
    * @Title: insertTmpUser
    * @Description: 新增测试库的用户
    * @author author
    * @param user 用户信息
    * @param list 用户角色关联信息
     */
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertTmpUser(TblUserInfo user, List<TblUserRole> list) {
        userDao.insertUser(user);
        //新增用户角色关联信息
        if (list != null && !list.isEmpty()) {
            for (TblUserRole userRole : list) {
                userRole.setUserId(user.getId());
            }
            iUserRoleService.insertUserRole(list);
        }
    }


    /**
     * 
    * @Title: delUser
    * @Description: 删除用户
    * @author author
    * @param userIds 需要删除的用户
    * @param lastUpdateBy 操作人
     */
    @Override
    @Transactional(readOnly = false)
    public void delUser(List<Long> userIds, Long lastUpdateBy) {
    	//删除用户
        userDao.delUserByIds(userIds, lastUpdateBy);
        //删除用户角色关联信息
        iUserRoleService.delUserRoleByUserIds(userIds, lastUpdateBy);
    }

    /**
     * 
    * @Title: selectMaxEmpNo
    * @Description: 获取最大的员工号，用于生成下一个员工号
    * @author author
    * @return String 员工号
     */
    @Override
    public String selectMaxEmpNo() {
        return userDao.selectMaxEmpNo();
    }

    /**
     * 
    * @Title: getUser
    * @Description: 获取所有有效员工
    * @author author
    * @return List<TblUserInfo> 员工信息列表
     */
    @Override
    public List<TblUserInfo> getUser() {
        return userDao.getUser();
    }

    /**
     * 
    * @Title: getCodeBaseUserDetailByUserScmAccount
    * @Description: 通过SVN/GIT账号获取用户信息，代码库配置中使用
    * @author author
    * @param userScmAccounts SVN/GIT账号信息，以,隔开
    * @return List<TblUserInfoDTO>用户信息列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblUserInfoDTO> getCodeBaseUserDetailByUserScmAccount(String userScmAccounts) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userScmAccounts", userScmAccounts);
    	return userDao.getCodeBaseUserDetailByUserScmAccount(map);
    }

    
    /**
     * 
    * @Title: getUserPageForCodeBase
    * @Description: 代码库添加人员时查询人员列表
    * @author author
    * @param bootStrapTablePage 组装返回bootstrapTable格式的数据
    * @param tblUserInfoDTO 封装的查询条件
    * @return BootStrapTablePage<TblUserInfoDTO> 以bootstrapTable分页格式封装的用户信息
     */
    @Override
    @Transactional(readOnly = true)
    public BootStrapTablePage<TblUserInfoDTO> getUserPageForCodeBase(BootStrapTablePage<TblUserInfoDTO> bootStrapTablePage, TblUserInfoDTO tblUserInfoDTO) {
        PageHelper.startPage(bootStrapTablePage.getPageNumber(), bootStrapTablePage.getPageSize());
        List<TblUserInfoDTO> userInfoDTOList = userDao.getUserListForCodeBase(tblUserInfoDTO);
        PageInfo<TblUserInfoDTO> pageInfo = new PageInfo<TblUserInfoDTO>(userInfoDTOList);
        bootStrapTablePage.setTotal((int) pageInfo.getTotal());
        bootStrapTablePage.setRows(pageInfo.getList());
        return bootStrapTablePage;
    }

    
    /**
     * 
    * @Title: getDevUserPageForCodeReview
    * @Description: 代码评审获取开发人员列表
    * @author author
    * @param bootStrapTablePage  组装返回bootstrapTable格式的数据
    * @param tblUserInfoDTO 封装的查询条件
    * @param currentUserId 当前用户
    * @return BootStrapTablePage<TblUserInfoDTO> 以bootstrapTable分页格式封装的用户信息
     */
    @Override
    @Transactional(readOnly = true)
    public BootStrapTablePage<TblUserInfoDTO> getDevUserPageForCodeReview(BootStrapTablePage<TblUserInfoDTO> bootStrapTablePage, TblUserInfoDTO tblUserInfoDTO, Long currentUserId) {
        PageHelper.startPage(bootStrapTablePage.getPageNumber(), bootStrapTablePage.getPageSize());
        List<TblUserInfoDTO> userInfoDTOList = userDao.getUserListForCodeReview(tblUserInfoDTO, currentUserId);
        PageInfo<TblUserInfoDTO> pageInfo = new PageInfo<TblUserInfoDTO>(userInfoDTOList);
        bootStrapTablePage.setTotal((int) pageInfo.getTotal());
        bootStrapTablePage.setRows(pageInfo.getList());
        return bootStrapTablePage;
    }

    /**
     * 
    * @Title: getDevUserNameAndReviewersNameForCodeReivew
    * @Description: 代码评审时获取开发人员姓名和评审人员姓名
    * @author author
    * @param devUserId 开发人员ID
    * @param codeReviewUserIds 评审人员ID，以,隔开
    * @return map key :devUserName  开发人员姓名
    *                  codeReviewUserNames  评审人员姓名
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getDevUserNameAndReviewersNameForCodeReivew(Long devUserId, String codeReviewUserIds) {
        Map<String, String> result = new HashMap<>();
        if (devUserId != null) {
            result.put("devUserName", userDao.getUserNameById(devUserId));
        }
        if (StringUtils.isNotEmpty(codeReviewUserIds)) {
            result.put("codeReviewUserNames", userDao.getUserNamesByUserIds(codeReviewUserIds));
        }
        return result;
    }

    /**
     * 
    * @Title: getAllUserModal
    * @Description: 前端页面采用bootstrapTable时，人员弹框用户查询
    * @author author
    * @param bootStrapTablePage 封装用户用以返回
    * @param userInfo 封装的查询条件
    * @param systemId 系统ID
    * @param notWithUserID 排除的用户ID
    * @param projectIds 项目ID
    * @param userPost 人员岗位
    * @return BootStrapTablePage<TblUserInfo> 封装返回的用户信息
     */
    @Override
    @Transactional(readOnly = true)
    public BootStrapTablePage<TblUserInfo> getAllUserModal(BootStrapTablePage<TblUserInfo> bootStrapTablePage, TblUserInfo userInfo, Long systemId, Long notWithUserID, String projectIds, String userPost) {
        Map<String, Object> map = new HashMap<>();
        map.put("notWithUserID", notWithUserID);
        map.put("user", userInfo);
        map.put("systemId", systemId);
        map.put("projectIds", projectIds);
        map.put("userPost", userPost);
        PageHelper.startPage(bootStrapTablePage.getPageNumber(), bootStrapTablePage.getPageSize());
        List<TblUserInfo> userInfoDTOList = userDao.getAllUserModal(map);
        PageInfo<TblUserInfo> pageInfo = new PageInfo<TblUserInfo>(userInfoDTOList);
        bootStrapTablePage.setTotal((int) pageInfo.getTotal());
        bootStrapTablePage.setRows(pageInfo.getList());
        return bootStrapTablePage;
    }


    /**
     * 
    * @Title: checkLogin
    * @Description: 登录时校验用户信息
    * @author author
    * @param userAccount 账号
    * @param password 密码
    * @return
    * @throws AccountException
    * @throws CredentialException
     */
    @Override
    @Transactional(readOnly = true)
    public TblUserInfo checkLogin(String userAccount, String password) throws AccountException, CredentialException {
        TblUserInfo user = userDao.getUserAcc(userAccount);
        if (user == null) {
            throw new AccountException("用户名不存在");
        }
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new CredentialException("密码错误");
        }
        return user;
    }

    
    /**
     * 
    * @Title: afterLoginCheck
    * @Description: 系统内用户登录成功后处理动作，包括生成jwttoken,以及获取该用户的角色权限信息并将其放于redis
    *               redis格式： key ： jwttoken
    *                          value:TblUserInfo的json对象：封装了按钮权限，角色信息等

    * @author author
    * @param user 登录用户
    * @return Object  JWTToken
     */
    @Override
    @Transactional(readOnly = true)
    public Object afterLoginCheck(TblUserInfo user) {
        //生成当前已验证登录成功用户token
        String token = JWTTokenUtils.createJavaWebToken(new HashMap<String, Object>() {{
            put("userAccount", user.getUserAccount());
            put("userName", user.getUserName());
            put("id", user.getId());
            put("uuId", UUID.randomUUID().toString().replaceAll("-", ""));
        }});
        //绑定菜单,角色权限
        //获取用户所有菜单权限
        user.addStringPermissionUrls(Constants.System.INDEX_URL);
        List<TblMenuButtonInfo> menuList = iMenuService.getUserAllMenuButton(user.getId());
        if (CollectionUtil.isNotEmpty(menuList)) {
            for (TblMenuButtonInfo menuButtonInfo : menuList) {
                if (StringUtils.isNotBlank(menuButtonInfo.getMenuButtonCode())) {
                    for (String permission : StringUtils.split(menuButtonInfo.getMenuButtonCode(), ",")) {
                        user.addStringPermissions(permission);
                    }
                }

                if (StringUtils.isNotBlank(menuButtonInfo.getUrl())) {
                    for (String url : StringUtils.split(menuButtonInfo.getUrl(), ",")) {
                        if (StringUtils.isNotBlank(url) && urlPattern.matcher(url.substring(2)).matches()) {
                            user.addStringPermissionUrls(url.substring(2));
                        }
                    }
                }
            }
        }
        user.addRoles("user");
        //获取用户所有角色
        List<TblRoleInfo> roleList = iRoleService.getUserAllRole(user.getId());
        if (CollectionUtil.isNotEmpty(roleList)) {
            for (TblRoleInfo roleInfo : roleList) {
                if (StringUtils.isNotBlank(roleInfo.getRoleCode())) {
                    user.addRoles(roleInfo.getRoleCode());
                }
            }
        }
        redisUtils.set(token, user, Constants.ITMP_TOKEN_TIMEOUT);
        return token;
    }

    
    /**
     * 
    * @Title: afterCasLogin
    * @Description: 单点登录成功后续操作，包括生成jwttoken,以及获取该用户的角色权限信息并将其放于redis
    *               redis格式： key ： jwttoken
    *                          value:TblUserInfo的json对象：封装了按钮权限，角色信息等
    * @author author
    * @param userCode 用户账号
    * @param userName 用户姓名
    * @return Object jwttoken
    * @throws AccountException
     */
    @Override
    @Transactional(readOnly = true)
    public Object afterCasLogin(String userCode, String userName) throws AccountException {
    	//大地人员工号即平台登录账号
        TblUserInfo user = userDao.getUserAcc(userCode);
        if (user == null) {
            throw new AccountException("无法找到账号(" + userCode + "),用户名(" + userName + ")对应的用户");
        }
        String token = JWTTokenUtils.createJavaWebToken(new HashMap<String, Object>() {{
            put("userAccount", userCode);
            put("userName", userName);
            put("id", user.getId());
            put("uuId", UUID.randomUUID().toString().replaceAll("-", ""));
        }});

        //绑定菜单,角色权限
        //获取用户所有菜单权限
        user.addStringPermissionUrls(Constants.System.INDEX_URL);
        List<TblMenuButtonInfo> menuList = iMenuService.getUserAllMenuButton(user.getId());
        if (CollectionUtil.isNotEmpty(menuList)) {
            for (TblMenuButtonInfo menuButtonInfo : menuList) {
                if (StringUtils.isNotBlank(menuButtonInfo.getMenuButtonCode())) {
                    for (String permission : StringUtils.split(menuButtonInfo.getMenuButtonCode(), ",")) {
                        user.addStringPermissions(permission);
                    }
                }
                if (StringUtils.isNotBlank(menuButtonInfo.getUrl())) {
                    for (String url : StringUtils.split(menuButtonInfo.getUrl(), ",")) {
                        if (StringUtils.isNotBlank(url) && urlPattern.matcher(url.substring(2)).matches()) {
                            user.addStringPermissionUrls(url.substring(2));
                        }
                    }
                }
            }
        }
        user.addRoles("user");
        //获取用户所有角色
        List<TblRoleInfo> roleList = iRoleService.getUserAllRole(user.getId());
        if (CollectionUtil.isNotEmpty(roleList)) {
            for (TblRoleInfo roleInfo : roleList) {
                if (StringUtils.isNotBlank(roleInfo.getRoleCode())) {
                    user.addRoles(roleInfo.getRoleCode());
                }
            }
        }
        redisUtils.set(token, user, Constants.ITMP_TOKEN_TIMEOUT);
        return token;
    }


    /**
     * 
    * @Title: getAllDevUser
    * @Description: 查询所有人员,在开发工作任务管理中，人员弹窗显示人员信息
    * @author author
    * @param deptName 部门名
    * @param companyName 公司名
    * @param userName  姓名
    * @param notWithUserID 排除的用户ID
    * @param devID 开发工作任务ID
    * @param pageNumber 第几页
    * @param pageSize 每页大小
    * @return map列表 key: USER_ID:用户ID
    *                     USER_NAME：姓名
    *                     COMPANY_NAME：公司名
    *                     DEPT_NAME：部门名
     */
    @Override
    public List<Map<String, Object>> getAllDevUser(String deptName, String companyName, String userName, Integer notWithUserID, Integer devID, Integer pageNumber, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("deptName", deptName);
        map.put("companyName", companyName);
        map.put("userName", userName);
        map.put("notWithUserID", notWithUserID);
        map.put("devID", devID);
        return userDao.getAllDevUser(map);
    }

    /**
     * 
    * @Title: itmpInnerUserData
    * @Description:同步内部人员信息
    * @author author
    * @param userData 需要同步的人员信息
    * @param password 初始密码
    * @return List<Map<String, Object>> map key:userInfo value:用户的json字符串
     */
    @Override
    @Transactional(readOnly = false)
    public List<Map<String, Object>> itmpInnerUserData(String userData, String password) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> result1 = new HashMap<>();

        Map<String, Object> result = jsonToMap(userData);
        /*String requestBody = result.get("requestBody").toString();
        List<Map<String,Object>> listObjectSec = JSONArray.parseObject(requestBody,List.class);
    	for(Map<String,Object> map:listObjectSec) {*/
        TblUserInfo userInfo = new TblUserInfo();
        String userName = result.get("USERNAME").toString();
        String userCode = result.get("USERCODE").toString();
        String deptCode = result.get("COMCODE").toString();
        String status = result.get("VALIDSTATUS").toString();
        // 部门：根据部门编码找到id
        TblDeptInfo deptInfo = deptDao.getDeptByCode(deptCode);
        //根据编码找到id
        TblUserInfo user = userDao.getAllUserRoleByAcc(userCode);

        userInfo.setUserAccount(userCode);
        userInfo.setUserName(userName);
        userInfo.setEmployeeNumber(userCode);
        userInfo.setDeptId(deptInfo == null ? null : deptInfo.getId());
        userInfo.setUserType(1);
        userInfo.setUserStatus(status.equals("0") ? 2 : 1);
        userInfo.setLastUpdateBy(1L);
        userInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));

        if (user == null) {
            userInfo.setCreateBy(1L);
            userInfo.setUserPassword(password);
            userInfo.setCreateDate(new Timestamp(new Date().getTime()));
            userDao.insertUser(userInfo);
        } else {
            userInfo.setId(user.getId());
            userDao.updateById(userInfo);
        }
        result1.put("userInfo", JSONObject.toJSONString(userInfo));
        listMap.add(result1);
        /*}*/
        return listMap;
    }


    /**
     * 
    * @Title: itmpExtUserData
    * @Description: 同步外部用户信息
    * @author author
    * @param userData 需要同步的用户
    * @param password 初始密码
    * @return List<Map<String, Object>> map key:userInfo value:用户的json字符串
     */
    @Override
    @Transactional(readOnly = false)
    public List<Map<String, Object>> itmpExtUserData(String userData, String password) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> result1 = new HashMap<>();
        Map<String, Object> result = jsonToMap(userData);

        TblUserInfo userInfo = new TblUserInfo();
        String userName = result.get("USERNAME").toString();
        String userCode = result.get("USERCODE").toString();
        String deptCode = result.get("COMCODE").toString();
        String status = result.get("VALIDSTATUS").toString();
        //有选择性同步人员
        if (userCode != null && (userCode.substring(0, 4).equals("itop") || userCode.substring(0, 2).equals("gd")||
                userCode.substring(0, 4).equals("oper") || userCode.substring(0, 4).equals("sqxm"))) {
            // 部门：根据部门编码找到id
            TblDeptInfo deptInfo = deptDao.getDeptByCode(deptCode);
            //根据编码找到id
            TblUserInfo user = userDao.getAllUserRoleByAcc(userCode);

            userInfo.setUserAccount(userCode);
            userInfo.setUserName(userName);
            userInfo.setEmployeeNumber(userCode);
            userInfo.setDeptId(deptInfo == null ? null : deptInfo.getId());
            userInfo.setUserType(2);
            userInfo.setUserStatus(status.equals("0") ? 2 : 1);
            userInfo.setLastUpdateBy(1L);
            userInfo.setLastUpdateDate(new Timestamp(new Date().getTime()));

            if (user == null) {
                userInfo.setCreateBy(1L);
                userInfo.setUserPassword(password);
                userInfo.setCreateDate(new Timestamp(new Date().getTime()));
                userDao.insertUser(userInfo);
            } else {
                userInfo.setId(user.getId());
                userDao.updateById(userInfo);
            }
            result1.put("userInfo", JSONObject.toJSONString(userInfo));
            listMap.add(result1);
        }
        return listMap;
    }

    //json转map
    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> mapTypes = JSON.parseObject(str);
        return mapTypes;
    }

    
    /**
     * 
    * @Title: getAllTestUser
    * @Description: 测试工作任务管理中，人员弹窗获取人员信息
    * @author author
    * @param tblUserInfo 封装的查询条件
    * @param notWithUserID 排除的用户ID，即不查询
    * @param devID 测试工作任务ID
    * @param pageNumber 第几页
    * @param pageSize 每页数量
    * @return map 列表  key :USER_ID      用户ID
    *                      USER_NAME     姓名
    *                      COMPANY_NAME  公司名
    *                      DEPT_NAME     部门名
     */
    @Override
    public List<Map<String, Object>> getAllTestUser(TblUserInfo tblUserInfo, Integer notWithUserID, Integer devID,
                                                    Integer pageNumber, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("tblUserInfo", tblUserInfo);
        map.put("notWithUserID", notWithUserID);
        map.put("devID", devID);
        return userDao.getAllTestUser(map);
    }

    /**
     * 
    * @Title: tmpUserData
    * @Description: 新增或修改测试库的用户信息
    * @author author
    * @param tblUserInfo 用户信息
    * @return int 影响的条数
     */
    @Override
    @DataSource(name = "tmpDataSource")
    @Transactional(readOnly = false)
    public int tmpUserData(TblUserInfo tblUserInfo) {
        int status = 0;
        TblUserInfo user = userDao.findUserById(tblUserInfo.getId());
        if (user == null) {
            status = userDao.insert(tblUserInfo);
        } else {
            status = userDao.updateById(tblUserInfo);
        }
        return status;
    }

    /**
     * 
    * @Title: selectById
    * @Description: 查询用户
    * @author author
    * @param deptName 部门名
    * @param companyName 公司名
    * @param userName 姓名
    * @param userId   用户ID
    * @param notWithUserID 排除的用户ID
    * @param userStatus    用户状态
    * @param pageNumber  第几页
    * @param pageSize    每页数量
    * @return   map列表    key :USER_ID     用户ID
                             USER_NAME    用户姓名
                             USER_ACCOUNT 用户账号
                             COMPANY_NAME 公司名
                             DEPT_NAME    部门名
                             STATUS       用户状态
     */
    @Override
    public List<Map<String, Object>> selectById(String deptName, String companyName, String userName, Long userId, Long notWithUserID, String userStatus,Integer pageNumber, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("deptName", deptName);
        map.put("companyName", companyName);
        map.put("userName", userName);
        map.put("id", userId);
        if(userStatus!=null && !userStatus.equals("")){
            map.put("userStatus", userStatus);
        }
        map.put("notWithUserID", notWithUserID);
        return userDao.selectById(map);
    }

    
    
    /**
     * 
     * @deprecated
    * @Title: selectByProjectId
    * @Description: 废弃未用
    * @author author
    * @param tblUserInfo
    * @param notWithUserID
    * @param pageNumber
    * @param pageSize
    * @return
     */
    @Override
    public List<Map<String, Object>> selectByProjectId(TblUserInfo tblUserInfo, Long notWithUserID, Integer pageNumber, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map = PageWithBootStrap.setPageNumberSize(map, pageNumber, pageSize);
        map.put("tblUserInfo", tblUserInfo);
        map.put("notWithUserID", notWithUserID);
        return userDao.selectByProjectId(map);
    }

    /**
     * 
    * @Title: getAllUserModal2
    * @Description: 弹框查询用户信息
    * @author author
    * @param userInfo 封装的查询条件
    * @param projectIds 项目查询条件
    * @param pageNumber 第几页
    * @param pageSize 每页数量
    * @return List<TblUserInfo> 用户列表信息
     */
    @Override
    public List<TblUserInfo> getAllUserModal2(TblUserInfo userInfo, String projectIds, Integer pageNumber,
                                              Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (pageNumber != null && pageSize != null) {
            map.put("start", (pageNumber - 1) * pageSize);
            map.put("pageSize", pageSize);
        } else {
            map.put("start", null);
            map.put("pageSize", null);
        }
        map.put("projectIds", projectIds);
        map.put("user", userInfo);
        return userDao.getAllUserModal2(map);
    }

    /**
     * 
    * @Title: getAllUserModalCount2
    * @Description: 获取用户数量
    * @author author
    * @param userInfo 封装的用户查询条件
    * @param projectIds 项目查询条件
    * @return  Integer 用户数量
     */
    @Override
    public Integer getAllUserModalCount2(TblUserInfo userInfo, String projectIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectIds", projectIds);
        map.put("user", userInfo);
        return userDao.getAllUserModalCount2(map);
    }

    /**
     * 
    * @Title: checkMyOldSvnPassword
    * @Description: 校验SVN原有密码
    * @author author
    * @param oldPassword 原有的密码
    * @param currentUserId 当前用户
    * @return Boolean true有效，false无效
    * @throws IOException
    * @throws NoSuchAlgorithmException
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean checkMyOldSvnPassword(String oldPassword, Long currentUserId) throws IOException, NoSuchAlgorithmException {
        return PasswordUtil.validateForAES(oldPassword, userDao.getMySvnPassword(currentUserId));
    }

    /**
     * 
    * @Title: updateMySvnPassword
    * @Description: 更新SVN密码
    * @author author
    * @param currentUserId 当前用户ID
    * @param userScmAccount SVN账号
    * @param newPassword 新密码
    * @param request
    * @throws NoSuchAlgorithmException
    * @throws AccountException
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateMySvnPassword(Long currentUserId, String userScmAccount,String newPassword, HttpServletRequest request) throws NoSuchAlgorithmException, AccountException {
        String entryptPassword = PasswordUtil.encryptForAES(newPassword);
        if (StringUtils.isEmpty(userScmAccount)) {
            throw new AccountException("您还未生成svn账号，无法更新密码！");
        }
        if (userDao.updateMySvnPassword(entryptPassword, currentUserId) == 1) {
            //接口请求devManage,更改svn配置文件
            ResultDataDTO resultDataDTO = systemToDevManageInterface.modifySvnPassword(currentUserId, userScmAccount, newPassword, entryptPassword);
            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_ABNORMAL_CODE)) {
                throw new RuntimeException(resultDataDTO.getResDesc());
            }
        }
    }
    

    /**
     * 
    * @Title: getAllDept
    * @Description: 获取所有部门
    * @author author
    * @param tblDeptInfo 封装的部门查询条件
    * @param pageNumber 第几页
    * @param pageSize   每页数量
    * @return List<TblDeptInfo> 部门列表信息
     */
    @Override
    public List<TblDeptInfo> getAllDept(TblDeptInfo tblDeptInfo, Integer pageNumber, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (pageNumber != null && pageSize != null) {
            map.put("start", (pageNumber - 1) * pageSize);
            map.put("pageSize", pageSize);
        } else {
            map.put("start", null);
            map.put("pageSize", null);
        }

        map.put("dept", tblDeptInfo);
        return deptDao.getAllDeptByPage(map);
    }


    /**
     * @param userId
     * @return java.lang.String
     * @Description 生成svn账号
     * @MethodName createSvnAccount
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/9 15:50
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, String> createSvnAccountPassword(Long userId) {
        Map<String, String> result = new HashMap<>();
        try {
            TblUserInfo user = userDao.getUserDetailByUserIdForSvnAccountPasswordCreate(userId);
            if (user != null) {
                //创建svn账户
                if (StringUtils.isEmpty(user.getUserScmAccount())) {
                    //创建svn账号(用户姓名全拼-公司简称)
                    StringBuilder svnAccountBuilder = new StringBuilder(PinYinUtil.getFullSpell(user.getUserName()));
                    if (StringUtils.isNotEmpty(user.getCompanyShortName())) {
                        svnAccountBuilder.append("-").append(PinYinUtil.getFullSpell(user.getCompanyShortName()));
                    }
                    //将svn账号在库中查重校验
                    if (userDao.getSvnAccountCount(svnAccountBuilder.toString()) > 0) {
                        //重复则加上唯一Id
                        svnAccountBuilder.append(user.getId());
                    }
                    if (userDao.createSvnAccountByUserId(svnAccountBuilder.toString(), userId) == 1) {
                        result.put("username", svnAccountBuilder.toString());
                    }
                } else {
                    result.put("username", user.getUserScmAccount());
                }
                //创建svn密码(默认密码)
                if (StringUtils.isEmpty(user.getUserScmPassword())) {
                    String entryptPassword = PasswordUtil.encryptForAES(svnDefaultPassword);
                    if (userDao.createSvnPasswordByUserId(entryptPassword, userId) == 1) {
                        result.put("password", entryptPassword);
                    }
                } else {
                    result.put("password", user.getUserScmPassword());
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("创建SVN账号异常，异常原因:" + e.getMessage(), e);
            return null;
        }
    }

    
    /**
     * 
    * @Title: getRoles
    * @Description: 获取所有角色
    * @author author
    * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblRoleInfo> getRoles() {
        return roleDao.getRoles();
    }

    /**
     * 
    * @Title: getIdByNum
    * @Description: 通过员工号获取用户ID
    * @author author
    * @param user  封装员工号
    * @return
     */
    @Override
    public Long getIdByNum(TblUserInfo user) {
        return userDao.getIdByNum(user);
    }

    /**
     * 
    * @Title: getIdByUserAccount
    * @Description: 通过账号获取用户ID
    * @author author
    * @param user 封装账号
    * @return
     */
    @Override
    public Long getIdByUserAccount(TblUserInfo user) {
        return userDao.getIdByUserAccount(user);
    }

    /**
     * 
    * @Title: getAllproject
    * @Description: 获取未结项有效的项目
    * @author author
    * @return map key:id  项目ID
    *                 projectName 项目名
     */
    @Override
    public List<Map<String, Object>> getAllproject() {
        return userDao.getAllproject();
    }


    /**
     * 
    * @Title: getAllUserModal2
    * @Description: 弹窗查询用户
    * @author author
    * @param bootStrapTablePage  封装返回的数据
    * @param userInfo 封装的用户查询条件
    * @param projectIds 项目查询条件
    * @return BootStrapTablePage<TblUserInfo> 用户分页信息
     */
    @Override
    @Transactional(readOnly = true)
    public BootStrapTablePage<TblUserInfo> getAllUserModal2(BootStrapTablePage<TblUserInfo> bootStrapTablePage, TblUserInfo userInfo, String projectIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectIds", projectIds);
        map.put("user", userInfo);
        PageHelper.startPage(bootStrapTablePage.getPageNumber(), bootStrapTablePage.getPageSize());
        List<TblUserInfo> userInfoDTOList = userDao.getAllUserModal2(map);
        PageInfo<TblUserInfo> pageInfo = new PageInfo<TblUserInfo>(userInfoDTOList);
        bootStrapTablePage.setTotal((int) pageInfo.getTotal());
        bootStrapTablePage.setRows(pageInfo.getList());
        return bootStrapTablePage;

    }

    
    /**
     * 
    * @Title: getBatchUserInfoByIds
    * @Description: 批量获取用户
    * @author author
    * @param ids 用户ID，以,隔开
    * @return List<TblUserInfoDTO>用户列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblUserInfoDTO> getBatchUserInfoByIds(List<Long> ids) {
        return userDao.getBatchUserDetailByIds(ids);
    }


    /**
     * 
    * @Title: getAllUserModal3
    * @Description: 弹窗用户查询
    * @author author
    * @param bootStrapTablePage 封装返回的数据
    * @param userInfo 封装的用户查询条件
    * @param projectIds 项目查询条件
    * @param ids 用户ID查询条件
    * @return BootStrapTablePage<TblUserInfo> 用户分页信息
     */
	@Override
	public BootStrapTablePage<TblUserInfo> getAllUserModal3(BootStrapTablePage bootStrapTablePage, TblUserInfo userInfo,
			String projectIds, String ids) {
		 Map<String, Object> map = new HashMap<>();
        map.put("projectIds", projectIds);
        map.put("user", userInfo);
        map.put("ids", ids);
        PageHelper.startPage(bootStrapTablePage.getPageNumber(), bootStrapTablePage.getPageSize());
        List<TblUserInfo> userInfoDTOList = userDao.getAllUserModal3(map);
        PageInfo<TblUserInfo> pageInfo = new PageInfo<TblUserInfo>(userInfoDTOList);
        bootStrapTablePage.setTotal((int) pageInfo.getTotal());
        bootStrapTablePage.setRows(pageInfo.getList());
        return bootStrapTablePage;
	}

	/**
	 * 
	* @Title: updateGitPassword
	* @Description: 更新git密码
	* @author author
	* @param userId 用户ID
	* @param userScmAccount git账号
	* @param svnDefaultPassword 默认密码
	* @param request
	* @throws AccountException
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateGitPassword(Long userId, String userScmAccount, String svnDefaultPassword,
			HttpServletRequest request) throws AccountException{
		String entryptPassword = PasswordUtil.encryptForAES(svnDefaultPassword);
        if (StringUtils.isEmpty(userScmAccount)) {
            throw new AccountException("您还未生成git账号，无法重置密码！");
        }
        if (userDao.updateMySvnPassword(entryptPassword, userId) == 1) {
            //接口请求devManage,更改git服务端
            ResultDataDTO resultDataDTO = systemToDevManageInterface.modifyGitPassword(userId, userScmAccount, svnDefaultPassword, entryptPassword);
            if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_ABNORMAL_CODE)) {
                throw new RuntimeException(resultDataDTO.getResDesc());
            }
        }
		
	}


	/**
	 * 
	* @Title: updateMySvnAndGitPassword
	* @Description: 更新仓库密码
	* @author author
	* @param currentUserId 当前登陆人
	* @param userScmAccount 仓库账号
	* @param oldPassword 原密码
	* @param newPassword 新密码
	* @param request
	* @throws AccountException
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateMySvnAndGitPassword(Long currentUserId, String userScmAccount,String oldPassword, String newPassword,
			HttpServletRequest request) throws AccountException{
		String entryptNewPassword = PasswordUtil.encryptForAES(newPassword);
		String entryptOldPassword = PasswordUtil.encryptForAES(oldPassword);
		
		 int flag1 = 0;
		  ResultDataDTO resultDataDTO = systemToDevManageInterface.modifySvnPassword(currentUserId, userScmAccount, newPassword, entryptNewPassword);
        //修改密码失败
		  if (StringUtils.equals(resultDataDTO.getResCode(), ResultDataDTO.DEFAULT_ABNORMAL_CODE)) {
            flag1 = 1;
        	 throw new RuntimeException(resultDataDTO.getResDesc());
         }
         int flag2 = 0;
        ResultDataDTO resultDataDTO2 = systemToDevManageInterface.modifyGitPassword(currentUserId, userScmAccount,newPassword , entryptNewPassword);
       //修改密码失败
        if (StringUtils.equals(resultDataDTO2.getResCode(), ResultDataDTO.DEFAULT_ABNORMAL_CODE)) {
        	 flag2 = 1;
            throw new RuntimeException(resultDataDTO2.getResDesc());
        }
        
        //修改svn和git仓库密码成功时更新系统相关信息
        if(flag1 == 0 && flag2 == 0) {
        	userDao.updateMySvnPassword(entryptNewPassword, currentUserId);
        }else if(flag1 == 1 && flag2 == 0){
        	systemToDevManageInterface.modifyGitPassword(currentUserId, userScmAccount, oldPassword , entryptOldPassword);
        }else if(flag1 == 0 && flag2 == 1) {
        	systemToDevManageInterface.modifySvnPassword(currentUserId, userScmAccount, oldPassword, entryptOldPassword);
        }
	}

	/**
	 * 
	* @Title: getUserByNameOrACC
	* @Description: 通过用户姓名和ID获取用户
	* @author author
	* @param id  用户ID
	* @param userName姓名
	* @return List<TblUserInfo>用户列表
	 */
	@Override
	public List<TblUserInfo> getUserByNameOrACC(Long id,String userName) {
		return userDao.getUserByNameOrACC(id,userName);
	}


}