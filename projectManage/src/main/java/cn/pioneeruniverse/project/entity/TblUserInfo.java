package cn.pioneeruniverse.project.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblUserInfo
 * @Description: 用户实体类
 * @author author
 * @date 2020年7月31日 下午7:27:12
 *
 */
@TableName("tbl_user_info")
public class TblUserInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	//用户账号
	private String userAccount;
	//用户密码
	private String userPassword;
	//用户名
	private String userName;
	//工号
	private String  employeeNumber;
	//邮箱
	private String email;
	//性别
	private String gender;
	//生日
	private Date birthday;
	//用户类型：（1:内部人员，2:外部人员）
	private Integer userType;
	//公司id
	private Long companyId;
	//入职日期
	private Date entryDate;
	//离职日期
	private Date leaveDate;
	//部门id
	private Integer deptId;
	//人员状态
	private Integer userStatus;
	//不用字段
	private Integer isAllowed;

	//部门名
	@TableField(exist = false)
	private String deptName;
    //公司名
	@TableField(exist = false)
	private String companyName;
	
	//姓名或账号，用于模糊查询
	@TableField(exist = false)
	private String userAndAccount;
    //多个用户id
	@TableField(exist = false)
	private Long [] ids;


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

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserAndAccount() {
		return userAndAccount;
	}
	public void setUserAndAccount(String userAndAccount) {
		this.userAndAccount = userAndAccount;
	}

	public Long[] getIds() {
		return ids;
	}
	public void setIds(Long[] ids) {
		this.ids = ids;
	}
}
