package cn.pioneeruniverse.system.controller;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.dto.ResultDataDTO;
import cn.pioneeruniverse.common.dto.TblUserInfoDTO;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.ExcelUtil;
import cn.pioneeruniverse.system.entity.Company;
import cn.pioneeruniverse.system.entity.TblDeptInfo;
import cn.pioneeruniverse.system.entity.TblRoleInfo;
import cn.pioneeruniverse.system.entity.TblUserInfo;
import cn.pioneeruniverse.system.entity.TblUserRole;
import cn.pioneeruniverse.system.service.company.ICompanyService;
import cn.pioneeruniverse.system.service.dept.IDeptService;
import cn.pioneeruniverse.system.service.user.IUserService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
* @ClassName: UserController
* @Description: 菜单：用户管理对应的Controller
* @author author
* @date 2020年7月31日 下午2:18:43
*
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICompanyService iCompanyService;
    @Autowired
    private IDeptService iDeptService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //默认初始密码
    private final static String DEFAULT_PASSWORD = "123456";

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    //默认SVN的初始密码
    @Value("${svn.default.password}")
    private String svnDefaultPassword;


    /**
     * @param userAccount
     * @param password
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description 正真的用户登录验证请求处理(验证走数据库)
     * @MethodName login
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/4 16:10
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResultDataDTO login(String userAccount, String password) {
        try {
        	//校验用户信息
            TblUserInfo user = iUserService.checkLogin(userAccount, password);
            //生成token
            String token = (String) iUserService.afterLoginCheck(user);
            return ResultDataDTO.SUCCESSWITHDATA("登录成功", token);
        } catch (LoginException e) {
            return ResultDataDTO.FAILURE("登录失败，" + e.getMessage());
        } catch (Exception e) {
            logger.error("验证登录异常，异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL("验证登录异常，异常原因：" + e.getMessage());
        }
    }


    /**
     * @param userCode
     * @param userName
     * @return cn.pioneeruniverse.common.dto.ResultDataDTO
     * @Description 单点登录验证成功后操作
     * @MethodName afterCasLogin
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/3/14 14:58
     */
    @RequestMapping(value = "afterCasLogin", method = RequestMethod.POST)
    public ResultDataDTO afterCasLogin(String userCode, String userName) {
        try {
            String token = (String) iUserService.afterCasLogin(userCode, userName);
            return ResultDataDTO.SUCCESSWITHDATA("操作成功", token);
        } catch (Exception e) {
            logger.error("处理单点登录成功后操作程序出现异常，异常原因：" + e.getMessage(), e);
            return ResultDataDTO.ABNORMAL("处理单点登录成功后操作程序出现异常，异常原因：" + e.getMessage());
        }
    }


    /**
     * 
    * @Title: getAllUser
    * @Description: 获取用户信息。前端查询用户时根据查询条件进行查询获取
    * @author author
    * @param FindUser 封装的查询条件
    * @param rows 每页数量
    * @param page 第几页
    * @return map key1 rows:查询数据
    *             key2 records:总条数
    *             key3 total:总页数
    *             key4 page：第几页
    * @throws
     */
    @RequestMapping(value = "getAllUser", method = RequestMethod.POST)
    public Map getAllUser(String FindUser, Integer rows, Integer page) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            TblUserInfo user = new TblUserInfo();
            if (StringUtils.isNotBlank(FindUser))
                user = JSONObject.parseObject(FindUser, TblUserInfo.class);
            result = iUserService.getAllUser(user, page, rows);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }
        return result;
    }

   /**
    * 
   * @Title: getCompany
   * @Description: 获取所有公司，前端下拉列表需要
   * @author author
   * @return
   * @throws
    */
    @RequestMapping(value = "getCompany", method = RequestMethod.POST)
    public List<Company> getCompany() {
        List<Company> list = null;
        try {
            list = iCompanyService.getCompany();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }
        return list;
    }

    /**
     * 
    * @Title: getDept
    * @Description: 获取部门列表，前端部门下拉框需要
    * @author author
    * @return 部门列表
    * @throws
     */
    @RequestMapping(value = "getDept", method = RequestMethod.POST)
    public List<TblDeptInfo> getDept() {
        List<TblDeptInfo> list = null;
        try {
            list = iDeptService.getAllDept();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }
        return list;
    }

    /**
     * 
    * @Title: getRoles
    * @Description: 获取所有角色列表，前端角色下拉框需要
    * @author author
    * @return
    * @throws
     */
    @RequestMapping(value = "getRoles", method = RequestMethod.POST)
    public List<TblRoleInfo> getRoles() {
        List<TblRoleInfo> list = null;
        try {
            list = iUserService.getRoles();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }
        return list;
    }

    /**
     * 
    * @Title: getUser
    * @Description: 通过用户账号和用户密码获取用户信息
    * @author author
    * @param userAccount：用户账号
    * @param userPassword：用户密码
    * @return map key1:data 获取的数据
    *             key2：status 1正常返回，2异常返回
    *             key3: errorMessage 返回错误信息
    * @throws
     */
    @RequestMapping(value = "getUser", method = RequestMethod.POST)
    public Map<String, Object> getUser(String userAccount, String userPassword) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            TblUserInfo user = iUserService.getUserAcc(userAccount);
            if (user != null) {
                result.put("data", JSONObject.toJSONString(user));
            } else {
                result.put("status", Constants.ITMP_RETURN_FAILURE);
                result.put("errorMessage", "用户/密码错误");
                return result;
            }
        } catch (Exception e) {
            return handleException(e, "通过帐号和密码获取用户信息失败");
        }

        return result;
    }

    /**
     * 
    * @Title: getPreUser
    * @Description: 通过账号信息获取用户数量，唯一性校验
    * @author author
    * @param userAccount 用户账号
    * @return map key1:data返回的查找用户结果数量
    *             status：1正常返回，2异常返回
    * @throws
     */
    @RequestMapping(value = "getPreUser", method = RequestMethod.POST)
    public Map<String, Object> getPreUser(String userAccount) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            Long count = iUserService.getPreAccUser(userAccount);
            result.put("data", count == null ? 0l : count);
        } catch (Exception e) {
            return handleException(e, "获取账户信息失败");
        }

        return result;
    }

    /**
     * 
    * @Title: saveUser
    * @Description: 保存用户
    * @author author
    * @param user 用户信息
    * @param roleIdsArray 用户的所有角色ID
    * @param newBirthday 生日
    * @param newEntryDate 入职日期
    * @param request
    * @return Map key1:status 1正常返回，2异常返回，repeat工号重复，repeat2用户名重复 
    *             key2:errorMessage异常描述
    * @throws
     */
    @RequestMapping(value = "saveUser", method = RequestMethod.POST)
    public Map<String, Object> saveUser( TblUserInfo user,  String roleIdsArray,String newBirthday,String newEntryDate, HttpServletRequest request) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
          //  TblUserInfo user = JSONObject.parseObject(newUser, TblUserInfo.class);
            if(roleIdsArray!=null && !roleIdsArray.equals("")){

                List<Long> rids= Arrays.stream(roleIdsArray.split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
                user.setRoleIds(rids);
            }
            if(newBirthday!=null && !newBirthday.equals("")){
                user.setBirthday(DateUtil.parseDate(newBirthday,DateUtil.format));
            }
            if(newEntryDate!=null && !newEntryDate.equals("")){
                user.setEntryDate(DateUtil.parseDate(newEntryDate,DateUtil.format));
            }
            if (user.getEmployeeNumber() != null) {
                Long id = iUserService.getIdByNum(user);
                if (id != null) {
                    result.put("status", "repeat");
                    return result;
                }
            }
            if (user.getUserAccount() != null) {
                Long id = iUserService.getIdByUserAccount(user);
                if (id != null) {
                    result.put("status", "repeat2");
                    return result;
                }
            }

//            user.setUserPassword(passwordEncoder.encode(Md5Utils.md5(DEFAULT_PASSWORD)));
            user.setUserPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            user.setStatus(1);
            
            //组装角色信息
            List<TblUserRole> list = new ArrayList<TblUserRole>();
            List<Long> roleIds = user.getRoleIds();
            if (roleIds != null && roleIds.size() != 0) {
                for (Long roleId : roleIds) {
                    TblUserRole userRole = new TblUserRole();
                    userRole.setCreateBy(CommonUtil.getCurrentUserId(request));
                    userRole.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    userRole.setRoleId(roleId);
                    userRole.setStatus(1);
                    list.add(userRole);
                }
            }
            iUserService.insertUser(user, list);
        } catch (Exception e) {
            return handleException(e, "新增用户失败");
        }

        return result;
    }

    /**
     * 
    * @Title: updateUser
    * @Description: 更新用户信息
    * @author author
    * @param user 封装的用户信息
    * @param roleIdsArray 用户的所有角色ID
    * @param newBirthday 生日
    * @param newEntryDate 入职日期
    * @param request
    * @returnMap key1:status 1正常返回，2异常返回，repeat工号重复，repeat2用户名重复 
    *            key2:errorMessage异常描述
    * @throws
     */
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public Map<String, Object> updateUser(TblUserInfo user,  String roleIdsArray,String newBirthday,String newEntryDate,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if(roleIdsArray!=null && !roleIdsArray.equals("")){

                List<Long> rids= Arrays.stream(roleIdsArray.split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
                user.setRoleIds(rids);
            }
            if(newBirthday!=null && !newBirthday.equals("")){
                user.setBirthday(DateUtil.parseDate(newBirthday,DateUtil.format));
            }
            if(newEntryDate!=null && !newEntryDate.equals("")){
                user.setEntryDate(DateUtil.parseDate(newEntryDate,DateUtil.format));
            }
            //TblUserInfo user = JSONObject.parseObject(newUser, TblUserInfo.class);
            if (user.getEmployeeNumber() != null) {
                Long id = iUserService.getIdByNum(user);
                if (id != null) {
                    result.put("status", "repeat");
                    return result;
                }
            }
            if (user.getUserAccount() != null) {
                Long id = iUserService.getIdByUserAccount(user);
                if (id != null) {
                    result.put("status", "repeat2");
                    return result;
                }
            }

            this.setUserScmPassword(user);

            List<TblUserRole> list = new ArrayList<TblUserRole>();
            List<Long> roleIds = user.getRoleIds();
            if (roleIds != null && roleIds.size() != 0) {
                for (Long roleId : roleIds) {
                    TblUserRole userRole = new TblUserRole();
                    userRole.setCreateBy(CommonUtil.getCurrentUserId(request));
                    userRole.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    userRole.setRoleId(roleId);
                    userRole.setStatus(1);
                    list.add(userRole);
                }
            }
            iUserService.updateUser(user, list);
        } catch (Exception e) {
            return handleException(e, "更新用户失败");
        }

        return result;
    }

    private void setUserScmPassword(TblUserInfo user){
        TblUserInfo savedUser = iUserService.findUserById(user.getId());
        if(!Objects.equals(user.getUserScmAccount(),savedUser.getUserScmAccount())){
            //密码写死默认的
            user.setUserScmPassword("HyG0HX+XBwPgUSo8XOcRCA==");
        }
    }
	/**
	 * 
	* @Title: delUser
	* @Description: 删除用户
	* @author author
	* @param userIds 选择的用户ID
	* @param lastUpdateBy
	* @return
	* @throws
	 */
    @RequestMapping(value = "delUser", method = RequestMethod.POST)
    public Map<String, Object> delUser(String userIds, Long lastUpdateBy) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            if (StringUtils.isNotBlank(userIds)) {
                String[] ids = userIds.split(",");
                List<Long> list = new ArrayList<Long>();
                for (String id : ids) {
                    list.add(Long.valueOf(id));
                }
                iUserService.delUser(list, lastUpdateBy);
            }
        } catch (Exception e) {
            return handleException(e, "删除用户失败");
        }

        return result;
    }

    /**
     * 
    * @Title: updatePassword
    * @Description: 修改密码
    * @author author
    * @param oldPass 旧密码
    * @param newPass 新密码
    * @param request 
    * @param response
    * @return map key1 status 1正常返回，2异常返回
    *             errorMessage：错误消息
    * @throws
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Map<String, Object> updatePassword(String oldPass, String newPass, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            Long id = CommonUtil.getCurrentUserId(request);
            TblUserInfo tui = iUserService.findUserById(id);
            if (tui != null) {
                //String md5Pass=Md5Utils.md5(oldPass);
                //if (!passwordEncoder.matches(md5Pass, tui.getUserPassword())) {
                if (!passwordEncoder.matches(oldPass, tui.getUserPassword())) {
                    result.put("status", Constants.ITMP_RETURN_FAILURE);
                    result.put("errorMessage", "旧密码输入错误");
                    return result;
                }
                TblUserInfo newUser = new TblUserInfo();
                newUser.setId(id);
                newUser.setLastUpdateBy(id);
                newUser.setUserPassword(passwordEncoder.encode(newPass));
                iUserService.updatePassword(newUser);
            } else {
                result.put("status", Constants.ITMP_RETURN_FAILURE);
                result.put("errorMessage", "用户信息不存在");
                return result;
            }
        } catch (Exception e) {
            return handleException(e, "密码修改失败");
        }

        return result;
    }

    /**
     * 
    * @Title: resetPassword
    * @Description: 重置密码
    * @author author
    * @param id
    * @param request
    * @return map key1 status 1正常返回，2异常返回
    *             errorMessage：错误消息
    * @throws
     */
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    public Map<String, Object> resetPassword(Long id, HttpServletRequest request) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            TblUserInfo user = new TblUserInfo();
            Long lastUpdateId = CommonUtil.getCurrentUserId(request);
            //user.setUserPassword(passwordEncoder.encode(Md5Utils.md5(DEFAULT_PASSWORD)));
            user.setUserPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            user.setId(id);
            user.setLastUpdateBy(lastUpdateId);
            iUserService.resetPassword(user);
        } catch (Exception e) {
            return handleException(e, "重置密码失败");
        }

        return result;
    }

    /**
     * 
    * @Title: findUserById
    * @Description: 通过用户ID查找用户，返回用户json字符串
    * @author author
    * @param userId 用户id
    * @return map key1 status 1正常返回，2异常返回
    *             key2 errorMessage：错误消息
    *             data 返回数据
    * @throws
     */
    @RequestMapping(value = "findUserById", method = RequestMethod.POST)
    public Map<String, Object> findUserById(Long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            TblUserInfo user = iUserService.findUserById(userId);
            result.put("data", JSONObject.toJSONString(user));
        } catch (Exception e) {
            return handleException(e, "获取用户失败");
        }
        return result;
    }

    /**
     * 
    * @Title: findUserById1
    * @Description: 通过用户ID查找用户，此处返回用户对象
    * @author author
    * @param userId
    * @return map key1 status 1正常返回，2异常返回
    *             key2 errorMessage：错误消息
    *             key3 data 数据
    * @throws
     */
    @RequestMapping(value = "findUserById1", method = RequestMethod.POST)
    public Map<String, Object> findUserById1(Long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            TblUserInfo user = iUserService.findUserById(userId);
            result.put("data", user);
        } catch (Exception e) {
            return handleException(e, "获取用户失败");
        }
        return result;
    }

    /**
     * 
    * @Title: getUserWithRoles
    * @Description: 通过账户返回用户，此处返回的用户带有角色
    * @author author
    * @param userAccount 用户账号
    * @return map key1 status 1正常返回，2异常返回
    *             key2 errorMessage：错误消息
    *             key3 data 数据
    * @throws
     */
    @RequestMapping(value = "getUserWithRoles", method = RequestMethod.POST)
    public Map<String, Object> getUserWithRoles(String userAccount) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            logger.info("server.port=8093");
            TblUserInfo user = iUserService.getUserRoleByAcc(userAccount);
            result.put("data", JSONObject.toJSONString(user));
        } catch (Exception e) {
            return handleException(e, "获取用户失败");
        }
        return result;
    }

    /**
     * 
    * @Title: getUser
    * @Description: 返回所有status=1正常的用户
    * @author author
    * @return
    * @throws
     */
    @RequestMapping(value = "getUserNoCon", method = RequestMethod.POST)
    public List<TblUserInfo> getUser() {
        return iUserService.getUser();
    }

    /**
     * 
    * @Title: getExcelUser
    * @Description: 导出人员
    * @author author
    * @param excelData 查询封装
    * @param response
    * @param request
    * @throws
     */
    @ResponseBody
    @RequestMapping(value = "getExcelAllUser")
    public void getExcelUser(String excelData, HttpServletResponse response, HttpServletRequest request) {
        List<TblUserInfo> list = new ArrayList<TblUserInfo>();
        TblUserInfo tblUserInfo = new TblUserInfo();
        try {
            Long Userd = CommonUtil.getCurrentUserId(request);
            if (StringUtils.isNotBlank(excelData))
                tblUserInfo = JSONObject.parseObject(excelData, TblUserInfo.class);
            tblUserInfo.setId(Userd);
            list = iUserService.getExcelAllUser(tblUserInfo);

            String[] title = {"姓名", "用户名", "所属处室", "在职状态", "角色", "入职日期"};
            //excel文件名
            SimpleDateFormat sformat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
            long now = System.currentTimeMillis();
            String day = sformat.format(now);
            String fileName = "用户表" + day + ".xls";
            //sheet名
            String sheetName = "用户表";
            String[][] content = new String[list.size()][];
            SimpleDateFormat simdate = new SimpleDateFormat("yyyy-MM-dd");
            if (list.size() > 0 && list != null) {
                for (int i = 0; i < list.size(); i++) {
                    content[i] = new String[title.length];
                    content[i][0] = list.get(i).getUserName();
                    content[i][1] = list.get(i).getUserAccount();
                    content[i][2] = list.get(i).getDeptName();
                    Integer taskStatus = list.get(i).getUserStatus();
                    content[i][3] = taskStatus == 1 ? "在职" : "离职";
                    List<TblRoleInfo> role = list.get(i).getUserRoles();
                    String roleName = "";
                    for (int j = 0; j < role.size(); j++) {
                        roleName += role.get(j).getRoleName() + ",";
                    }
                    if (roleName.length() > 0) {
                        roleName = roleName.substring(0, roleName.length() - 1);
                    }

                    content[i][4] = roleName;
                    Date date = list.get(i).getEntryDate();
                    if (null != date) {
                        content[i][5] = simdate.format(list.get(i).getEntryDate());
                    }
                }
            }
            //创建HSSFWorkbook
            HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }
    }
    //发送响应流方法

    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }

    /**
     * 
    * @Title: getCodeBaseUserDetailByUserScmAccount
    * @Description: 通过仓库账号获取用户信息
    * @author author
    * @param userScmAccount 仓库账号
    * @return
    * @throws
     */
    @RequestMapping(value = "getCodeBaseUserDetailByUserScmAccount", method = RequestMethod.POST)
    public TblUserInfoDTO getCodeBaseUserDetailByUserScmAccount(String userScmAccount) {
    	List<TblUserInfoDTO> list = iUserService.getCodeBaseUserDetailByUserScmAccount(userScmAccount);
    	if (CollectionUtil.isNotEmpty(list)) {
    		return list.get(0);
    	}
        return null;
    }
    
    /**
     * 
    * @Title: getCodeBaseUserDetailByUserScmAccountList
    * @Description: 通过仓库账号列表返回用户列表
    * @author author
    * @param userScmAccounts 仓库账号列表
    * @return
    * @throws
     */
    @RequestMapping(value = "getCodeBaseUserDetailByUserScmAccountList", method = RequestMethod.POST)
    public List<TblUserInfoDTO> getCodeBaseUserDetailByUserScmAccountList(String userScmAccounts) {
        return iUserService.getCodeBaseUserDetailByUserScmAccount(userScmAccounts);
    }

    /**
     * 
    * @Title: getUserPageForCodeBase
    * @Description: 分页获取代码仓库的人员信息
    * @author author
    * @param tblUserInfoDTO 封装的查询信息
    * @param request
    * @param response
    * @return
    * @throws
     */
    @RequestMapping(value = "getUserPageForCodeBase", method = RequestMethod.POST)
    public BootStrapTablePage<TblUserInfoDTO> getUserPageForCodeBase(TblUserInfoDTO tblUserInfoDTO, HttpServletRequest request, HttpServletResponse response) {
        BootStrapTablePage<TblUserInfoDTO> bootStrapTablePage = iUserService.getUserPageForCodeBase(new BootStrapTablePage<>(request, response), tblUserInfoDTO);
        return bootStrapTablePage;
    }

    /**
     * 
    * @Title: getDevUserPageForCodeReview
    * @Description: 分页显示代码评审的与当前人员同项目小组的人员
    * @author author
    * @param tblUserInfoDTO
    * @param request
    * @param response
    * @return
    * @throws
     */
    @RequestMapping(value = "getDevUserPageForCodeReview", method = RequestMethod.POST)
    public BootStrapTablePage<TblUserInfoDTO> getDevUserPageForCodeReview(TblUserInfoDTO tblUserInfoDTO, HttpServletRequest request, HttpServletResponse response) {
        BootStrapTablePage<TblUserInfoDTO> bootStrapTablePage = iUserService.getDevUserPageForCodeReview(new BootStrapTablePage<>(request, response), tblUserInfoDTO, CommonUtil.getCurrentUserId(request));
        return bootStrapTablePage;
    }

    /**
     * 
    * @Title: getDevUserNameAndReviewersNameForCodeReivew
    * @Description: 获取代码评审的开发人员和评审人员
    * @author author
    * @param devUserId 开发人员ID
    * @param codeReviewUserIds 多位评审人员ID
    * @return map key -devUserName:开发人员姓名
    *                  codeReviewUserNames：代码评审人员
    * @throws
     */
    @RequestMapping(value = "getDevUserNameAndReviewersNameForCodeReivew", method = RequestMethod.POST)
    public Map<String, String> getDevUserNameAndReviewersNameForCodeReivew(Long devUserId, String codeReviewUserIds) {
        return iUserService.getDevUserNameAndReviewersNameForCodeReivew(devUserId, codeReviewUserIds);
    }

    /**
     * 查询所有的用户信息，bootstrap格式
     *
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "getAllUserModal", method = RequestMethod.POST)
    public BootStrapTablePage<TblUserInfo> getAllUserModal(TblUserInfo userInfo,
                                                           Long notWithUserID,
                                                           Long systemId,
                                                           String projectIds,
                                                           String userPost,
                                                           HttpServletRequest request, HttpServletResponse response
                                             /*  @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize*/) {
      /*  Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = iUserService.getAllUserModal(userInfo, systemId, notWithUserID, projectIds,userPost, pageNumber, pageSize);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return handleException(e, "获取用户失败");
        }
        return result;*/
        BootStrapTablePage<TblUserInfo> bootStrapTablePage = iUserService.getAllUserModal(new BootStrapTablePage<>(request, response), userInfo, systemId, notWithUserID, projectIds, userPost);
        return bootStrapTablePage;
    }

    /**
     * 查询所有的用户信息，bootstrap格式
     * 部门和公司模糊查询
     */
    @RequestMapping(value = "getAllUserModal2", method = RequestMethod.POST)
    public BootStrapTablePage<TblUserInfo> getAllUserModal2(TblUserInfo userInfo,
                                                            String projectIds,
                                              /*  @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,*/
                                                            HttpServletRequest request, HttpServletResponse response) {
       /* Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblUserInfo> userList = iUserService.getAllUserModal2(userInfo, projectIds, pageNumber, pageSize);
            Integer count = iUserService.getAllUserModalCount2(userInfo, projectIds);

            result.put("rows", userList);
            result.put("total", count);
        } catch (Exception e) {
            return handleException(e, "获取用户失败");
        }
        return result;*/

        BootStrapTablePage<TblUserInfo> bootStrapTablePage = iUserService.getAllUserModal2(new BootStrapTablePage<>(request, response), userInfo, projectIds);
        return bootStrapTablePage;
    }
    
    /**
     * 查询所有的用户信息，bootstrap格式
     * 部门和公司模糊查询 排除已选的多个
     */
    @RequestMapping(value = "getAllUserModal3", method = RequestMethod.POST)
    public BootStrapTablePage<TblUserInfo> getAllUserModal3(TblUserInfo userInfo,
                                                            String projectIds,
                                                            String ids,
                                                        HttpServletRequest request, HttpServletResponse response) {
        BootStrapTablePage<TblUserInfo> bootStrapTablePage = iUserService.getAllUserModal3(new BootStrapTablePage<>(request, response), userInfo, projectIds,ids);
        return bootStrapTablePage;
    }


    @RequestMapping(value = "getAllDevUser")
    public Map<String, Object> getAllDevUser(String deptName, String companyName, String userName, Integer devID, Integer notWithUserID, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        Long id = CommonUtil.getCurrentUserId(request);
        //tblDevTask.setId(id);
        try {
            List<Map<String, Object>> list = iUserService.getAllDevUser(deptName, companyName, userName, notWithUserID, devID, pageNumber, pageSize);
            List<Map<String, Object>> list2 = iUserService.getAllDevUser(deptName, companyName, userName, notWithUserID, devID, null, null);
            result.put("rows", list);
            result.put("total", list2.size());
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
            return handleException(e, "获取系统信息失败");
        }
        return result;

    }

    @RequestMapping(value = "getAllTextUser")
    public Map<String, Object> getAllTextUser(TblUserInfo tblDevTask, Integer devID, Integer notWithUserID, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        Long id = CommonUtil.getCurrentUserId(request);
        tblDevTask.setId(id);
        try {
            List<Map<String, Object>> list = iUserService.getAllTestUser(tblDevTask, notWithUserID, devID, pageNumber, pageSize);
            List<Map<String, Object>> list2 = iUserService.getAllTestUser(tblDevTask, notWithUserID, devID, null, null);
            result.put("rows", list);
            result.put("total", list2.size());
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
            return handleException(e, "获取系统信息失败");
        }
        return result;


    }

    /**
     *@Description 同步内部用户信息
     *@Date 2020/7/21
     *@Param [userData] 报文
     *@return
     **/
    @RequestMapping(value = "updateInnerUserData", method = RequestMethod.POST, produces = "application/json;utf-8")
    public void updateInnerUserData(@RequestBody String userData, HttpServletResponse response) {
        Integer status = 0;
        logger.info("开始同步内部用户数据" + userData);
        try {
            if (StringUtils.isNotBlank(userData)) {
                String password = passwordEncoder.encode(DEFAULT_PASSWORD);
                List<Map<String, Object>> listMap = iUserService.itmpInnerUserData(userData, password);
                for (Map<String, Object> result : listMap) {
                    String user = result.get("userInfo").toString();
                    TblUserInfo userInfo = JSON.parseObject(user, TblUserInfo.class);
                    status = iUserService.tmpUserData(userInfo);
                }
                if (status > 0) {
                    Map<String, Object> head = new HashMap<>();
                    Map<String, Object> map = new HashMap<>();
                    map.put("consumerSeqNo", "itmgr");
                    map.put("status", 0);
                    map.put("seqNo", "");
                    map.put("providerSeqNo", "");
                    map.put("esbCode", "");
                    map.put("esbMessage", "");
                    map.put("appCode", "0");
                    map.put("appMessage", "同步内部用户信息成功");
                    head.put("responseHead", map);

                    PrintWriter writer = response.getWriter();
                    writer.write(new JSONObject(head).toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步内部用户信息" + ":" + e.getMessage(), e);
        }
    }

    /**
     *@Description 同步外部用户信息
     *@Date 2020/7/21
     *@Param [userData] 报文
     *@return
     **/
    @RequestMapping(value = "updateExtUserData", method = RequestMethod.POST, produces = "application/json;utf-8")
    public void updateExtUserData(@RequestBody String userData, HttpServletResponse response) {
        Integer status = 0;
        logger.info("开始同步外部用户数据" + userData);
        try {
            if (StringUtils.isNotBlank(userData)) {
                String password = passwordEncoder.encode(DEFAULT_PASSWORD);
                List<Map<String, Object>> listMap = iUserService.itmpExtUserData(userData, password);
                for (Map<String, Object> result : listMap) {
                    String user = result.get("userInfo").toString();
                    TblUserInfo userInfo = JSON.parseObject(user, TblUserInfo.class);
                    status = iUserService.tmpUserData(userInfo);
                }
                if ((listMap.size() > 0 && status > 0) || (listMap.size() == 0 && status == 0)) {
                    Map<String, Object> head = new HashMap<>();
                    Map<String, Object> map = new HashMap<>();
                    map.put("consumerSeqNo", "itmgr");
                    map.put("status", 0);
                    map.put("seqNo", "");
                    map.put("providerSeqNo", "");
                    map.put("esbCode", "");
                    map.put("esbMessage", "");
                    map.put("appCode", "0");
                    map.put("appMessage", "同步外部用户信息成功");
                    head.put("responseHead", map);

                    PrintWriter writer = response.getWriter();
                    writer.write(new JSONObject(head).toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("同步外部用户信息" + ":" + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "selectById")
    public Map<String, Object> selectById(String deptName, String companyName, String userName, Long notWithUserID, Integer pageNumber, Integer pageSize,String userStatus, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        Long userID = CommonUtil.getCurrentUserId(request);
        try {

            List<Map<String, Object>> list = iUserService.selectById(deptName, companyName, userName, userID, notWithUserID,userStatus, pageNumber, pageSize);
            List<Map<String, Object>> list2 = iUserService.selectById(deptName, companyName, userName, userID, notWithUserID,userStatus, null, null);
            result.put("rows", list);
            result.put("total", list2.size());
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
            return handleException(e, "获取系统信息失败");
        }
        return result;
    }


    /**
     * 查询所有部门bootstrap格式
     * 部门和公司模糊查询
     */
    @RequestMapping(value = "getAllDept", method = RequestMethod.POST)
    public Map<String, Object> getAllUserModal2(TblDeptInfo tblDeptInfo,
                                                @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblDeptInfo> deptList = iUserService.getAllDept(tblDeptInfo, pageNumber, pageSize);
            List<TblDeptInfo> List = iUserService.getAllDept(tblDeptInfo, null, null);
            result.put("rows", deptList);
            result.put("total", List.size());
        } catch (Exception e) {
            return handleException(e, "获取部门信息失败");
        }
        return result;
    }

    /**
     * @param userId
     * @return map key username:svn用户名，password：svn密码
     * @Description 生成svn账号请求
     * @MethodName createSvnAccount
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/9 15:47
     */
    @RequestMapping(value = "createSvnAccountPassword", method = RequestMethod.POST)
    public Map<String, String> createSvnAccountPassword(Long userId) {
        return iUserService.createSvnAccountPassword(userId);
    }


    /**
     * @param oldPassword
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 检测当前用户svn的旧密码是否存在
     * @MethodName checkMyOldSvnPassword
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/29 13:45
     */
    @RequestMapping(value = "checkMyOldSvnPassword", method = RequestMethod.POST)
    public AjaxModel checkMyOldSvnPassword(@RequestParam("oldPassword") String oldPassword, HttpServletRequest request) {
        try {
            return AjaxModel.SUCCESS(iUserService.checkMyOldSvnPassword(oldPassword, Long.valueOf(CommonUtil.getCurrentUser(request).get("id").toString())));
        } catch (Exception e) {
            logger.error("检测用户旧SVN密码有效性异常，异常原因：" + e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param newPassword
     * @param request
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 修改svn密码 和git密码
     * @MethodName updateMySvnPassword
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/30 11:35
     */
    @RequestMapping(value = "updateMySvnAndGitPassword", method = RequestMethod.POST)
    public AjaxModel updateMySvnAndGitPassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword, HttpServletRequest request) {
        try {
        	 Map currentUser = CommonUtil.getCurrentUser(request);
             Long currentUserId = Long.valueOf(currentUser.get("id").toString());
             String userScmAccount = currentUser.get("userScmAccount").toString();
            
             iUserService.updateMySvnAndGitPassword(currentUserId,userScmAccount,oldPassword,newPassword, request);
            
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error("更新用户SVN密码异常，异常原因：" + e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
    /**
     * @param request
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 重置svn密码
     * @MethodName resetSvnPassword
     * @author tingting
     */
    @RequestMapping(value = "resetSvnPassword", method = RequestMethod.POST)
    public AjaxModel resetSvnPassword(Long userId,String userScmAccount, HttpServletRequest request) {
        try {
            iUserService.updateMySvnPassword(userId,userScmAccount,svnDefaultPassword, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error("重置用户svn密码异常，异常原因：" + e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }
   
    /**
     * @param request
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 重置git密码
     * @MethodName resetGitPassword
     * @author tingting
     */
    @RequestMapping(value = "resetGitPassword", method = RequestMethod.POST)
    public AjaxModel resetGitPassword(Long userId,String userScmAccount, HttpServletRequest request) {
        try {
            iUserService.updateGitPassword(userId,userScmAccount,svnDefaultPassword, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error("重置用户git密码异常，异常原因：" + e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    @RequestMapping(value = "getAllproject", method = RequestMethod.POST)
    public List<Map<String, Object>> getAllproject() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = iUserService.getAllproject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("mes:" + e.getMessage(), e);
        }
        return list;
    }

    @RequestMapping(value = "getUserInfoByUserIds", method = RequestMethod.POST)
    public List<TblUserInfoDTO> getUserInfoByUserIds(List<Long> userIds) {
        return iUserService.getBatchUserInfoByIds(userIds);
    }
    
  //模糊查询获取用户信息
  	@PostMapping(value = "getUserByNameOrACC")
  	public Map<String, Object> getUserByNameOrACC(String userName,HttpServletRequest request) {
  		Map<String, Object> result = new HashMap<>();
  		try {
  			Long uid = CommonUtil.getCurrentUserId(request);
  			List<TblUserInfo> userInfo = iUserService.getUserByNameOrACC(uid,userName);
  			result.put("status",1);
  			result.put("userInfo",userInfo);
  		} catch (Exception e) {
  			return super.handleException(e, "查询失败");
  		}
  		return  result;
  	}
    
}
