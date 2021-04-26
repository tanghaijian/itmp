package cn.pioneeruniverse.dev.entity;

import java.util.*;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_user_info")
public class TblUserInfo extends BaseEntity {

    private static final long serialVersionUID = -1421203092465310527L;
    /**
     * 用户账号
     **/
    private String userAccount; //用户账号

    /**
     * 用户密码
     **/
    private String userPassword; //用户密码

    /**
     * 用户姓名
     **/
    private String userName; //用户姓名

    /**
     * 工号
     **/
    private String  employeeNumber; //工号

    /**
     * 公司
     **/
    private Long companyId; //公司



    /**
     * 用户状态（1：正常、2：无效）
     **/
    private Integer userStatus; //用户状态（1：正常、2：无效）

    /**
     * 部门ID
     **/
    private Long deptId; //部门ID


    private String email;
    private String gender;
    private Date birthday;
    private Integer userType;

    private Date entryDate;
    private Date leaveDate;

    private Integer isAllowed;
    private String userScmPassword;


    @TableField(exist = false)
    private String roleName;
    /**
     * 公司名
     **/
    @TableField(exist = false)
    private String companyName;


    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String userRfid;


    @TableField(exist = false)
    private List<Long> userIds;

    @TableField(exist = false)
    private Set<String> stringPermissions;

    @TableField(exist = false)
    private Set<String> roles;


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


    public String getUserScmPassword() {
        return userScmPassword;
    }

    public void setUserScmPassword(String userScmPassword) {
        this.userScmPassword = userScmPassword;
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
