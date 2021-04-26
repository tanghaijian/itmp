package cn.pioneeruniverse.common.dto;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.Date;
import java.util.Set;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 系统用户DTO类
 * @Date: Created in 16:25 2018/12/13
 * @Modified By:
 */
public class TblUserInfoDTO extends BaseEntity {


    private static final long serialVersionUID = -3964598288460722398L;
  //账号
    private String userAccount; 
    //密码
    private String userPassword;
  //姓名
    private String userName; 
  //工号
    private String employeeNumber; 
  //邮箱
    private String email; 
  //性别
    private String gender; 
  //生日
    private Date birthday; 
  //人员类型（1:内部人员，2:外部人员）
    private Integer userType; 
  //公司ID
    private Long companyId; 
  //入职日期
    private Date entryDate; 
  //离职日期
    private Date leaveDate; 
  //部门ID
    private Long deptId; 
  //用户状态（1：正常、2：无效）
    private Integer userStatus; 
  //（字段未用）
    private Integer isAllowed; 
  //仓库账号
    private String userScmAccount;
  //仓库密码
    private String userScmPassword;


    //非数据表字段
  //公司名
    private String companyName; 
  //角色名
    private String roleName; 
  //svn权限
    private String svnAuthority; 
  //gitlab权限级别
    private Integer gitLabAccessLevel; 
  //部门名
    private String deptName; 
  //排除的用户ID，一般用于选择人员排除已选择的
    private String excludeUserId; 
  //是否移出人员（未用）
    private Boolean removeExcludeUser; 
  //项目小组
    private String projectGroupId; 
    //本人项目小组ID
    private String myProjectGroupId;
  //本人项目小组名
    private String myProjectGroupName; 
  //本人项目名
    private String myProjectName;
  //用户拥有的所有权限
    private Set<String> stringPermissions; 
  //用户拥有的所有角色
    private Set<String> roles;
  //用户拥有的所有权限的url
    private Set<String> stringPermissionUrls; 
  //项目ID
    private Long projectId; 
  //项目岗位
    private Integer userPost;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(Integer isAllowed) {
        this.isAllowed = isAllowed;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSvnAuthority() {
        return svnAuthority;
    }

    public void setSvnAuthority(String svnAuthority) {
        this.svnAuthority = svnAuthority;
    }

    public String getUserScmPassword() {
        return userScmPassword;
    }

    public void setUserScmPassword(String userScmPassword) {
        this.userScmPassword = userScmPassword;
    }

    public String getExcludeUserId() {
        return excludeUserId;
    }

    public void setExcludeUserId(String excludeUserId) {
        this.excludeUserId = excludeUserId;
    }

    public String getProjectGroupId() {
        return projectGroupId;
    }

    public void setProjectGroupId(String projectGroupId) {
        this.projectGroupId = projectGroupId;
    }

    public String getUserScmAccount() {
        return userScmAccount;
    }

    public void setUserScmAccount(String userScmAccount) {
        this.userScmAccount = userScmAccount;
    }

    public String getMyProjectGroupId() {
        return myProjectGroupId;
    }

    public void setMyProjectGroupId(String myProjectGroupId) {
        this.myProjectGroupId = myProjectGroupId;
    }

    public String getMyProjectGroupName() {
        return myProjectGroupName;
    }

    public void setMyProjectGroupName(String myProjectGroupName) {
        this.myProjectGroupName = myProjectGroupName;
    }

    public Set<String> getStringPermissions() {
        return stringPermissions;
    }

    public void setStringPermissions(Set<String> stringPermissions) {
        this.stringPermissions = stringPermissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getStringPermissionUrls() {
        return stringPermissionUrls;
    }

    public void setStringPermissionUrls(Set<String> stringPermissionUrls) {
        this.stringPermissionUrls = stringPermissionUrls;
    }

    public Integer getGitLabAccessLevel() {
        return gitLabAccessLevel;
    }

    public void setGitLabAccessLevel(Integer gitLabAccessLevel) {
        this.gitLabAccessLevel = gitLabAccessLevel;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getRemoveExcludeUser() {
        return removeExcludeUser;
    }

    public void setRemoveExcludeUser(Boolean removeExcludeUser) {
        this.removeExcludeUser = removeExcludeUser;
    }

    public Integer getUserPost() {
        return userPost;
    }

    public void setUserPost(Integer userPost) {
        this.userPost = userPost;
    }

    public String getMyProjectName() {
        return myProjectName;
    }

    public void setMyProjectName(String myProjectName) {
        this.myProjectName = myProjectName;
    }
}
