package cn.pioneeruniverse.system.entity;

import java.util.*;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("tbl_user_info")
public class TblUserInfo extends BaseEntity {

    private static final long serialVersionUID = -1421203092465310527L;
    //用户账号
    private String userAccount;
    //用户密码
    private String userPassword;
    //姓名
    private String userName;
    //工号
    private String employeeNumber;
    //邮箱
    private String email;
    //性别(1:男，2:女)
    private String gender;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    //生日
    private Date birthday;
    //人员类型（1:内部人员，2:外部人员）
    private Integer userType;
    //公司
    private Long companyId;
    
    //入职日期
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date entryDate;
    //离职日期
    private Date leaveDate;
    //部门ID
    private Long deptId;
    //用户状态（1：正常、2：无效）
    private Integer userStatus;
    //是否允许访问（主要是判断是否已经修改过密码0：未，1：是）
    private Integer isAllowed;
    //用户代码仓库账号
    private String userScmAccount;
    //用户代码仓库密码
    private String userScmPassword;

    //角色名称
    @TableField(exist = false)
    private String roleName;
    //公司名称
    @TableField(exist = false)
    private String companyName;
   //公司简称
    @TableField(exist = false)
    private String companyShortName;
   //部门名称
    @TableField(exist = false)
    private String deptName;
    //暂无用
    @TableField(exist = false)
    private String userRfid;
   //用户名下所有角色
    @TableField(exist = false)
    private List<TblRoleInfo> userRoles;
   //用户IDS
    @TableField(exist = false)
    private List<Long> userIds;
   //用户权限
    @TableField(exist = false)
    private Set<String> stringPermissions;
   //用户所有角色名
    @TableField(exist = false)
    private Set<String> roles;
   //权限对应的URL
    @TableField(exist = false)
    private Set<String> stringPermissionUrls;
   //所属项目小组ID
    @TableField(exist = false)
    private String myProjectGroupId;
    //所属项目小组名
    @TableField(exist = false)
    private String myProjectGroupName;
    //用户所有角色ID
    @TableField(exist = false)
    private List<Long> roleIds;
   //用户所属项目组ID
    @TableField(exist = false)
    private String myProjectId;
    //用户所有的项目组ID
    @TableField(exist = false)
    private Long[] projectIds;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


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

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(Integer isAllowed) {
        this.isAllowed = isAllowed;
    }

    public String getUserRfid() {
        return userRfid;
    }

    public void setUserRfid(String userRfid) {
        this.userRfid = userRfid;
    }

    public List<TblRoleInfo> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<TblRoleInfo> userRoles) {
        this.userRoles = userRoles;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public void addStringPermissions(String permission) {
        if (this.stringPermissions == null) {
            this.stringPermissions = new HashSet();
        }
        this.stringPermissions.add(permission);
    }

    public void addStringPermissions(Collection<String> permissions) {
        if (this.stringPermissions == null) {
            this.stringPermissions = new HashSet<>();
        }
        this.stringPermissions.addAll(permissions);
    }

    public void addRoles(String role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void addRoles(Collection<String> roles) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.addAll(roles);
    }

    public void addStringPermissionUrls(String permissionUrl) {
        if (this.stringPermissionUrls == null) {
            this.stringPermissionUrls = new HashSet<>();
        }
        this.stringPermissionUrls.add(permissionUrl);
    }

    public void addStringPermissionUrls(Collection<String> permissionUrls) {
        if (this.stringPermissionUrls == null) {
            this.stringPermissionUrls = new HashSet<>();
        }
        this.stringPermissionUrls.addAll(permissionUrls);
    }

    public String getUserScmAccount() {
        return userScmAccount;
    }

    public void setUserScmAccount(String userScmAccount) {
        this.userScmAccount = userScmAccount;
    }

    public String getUserScmPassword() {
        return userScmPassword;
    }

    public void setUserScmPassword(String userScmPassword) {
        this.userScmPassword = userScmPassword;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
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

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

    public String getMyProjectId() {
        return myProjectId;
    }

    public void setMyProjectId(String myProjectId) {
        this.myProjectId = myProjectId;
    }

    public Long[] getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(Long[] projectIds) {
		this.projectIds = projectIds;
	}

	@Override
    public String toString() {
        return "TblUserInfo{" +
                "userAccount='" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", userType=" + userType +
                ", companyId=" + companyId +
                ", entryDate=" + entryDate +
                ", leaveDate=" + leaveDate +
                ", deptId=" + deptId +
                ", userStatus=" + userStatus +
                ", isAllowed=" + isAllowed +
                ", userScmPassword='" + userScmPassword + '\'' +
                '}';
    }
}
