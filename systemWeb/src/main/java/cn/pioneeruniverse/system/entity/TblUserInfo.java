package cn.pioneeruniverse.system.entity;

import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblUserInfo extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1421203092465310527L;
	//账号
	private String userAccount;
	//密码
	private String userPassword;
	//姓名
	private String userName;
	//工号
	private String  employeeNumber;
	//公司
	private Integer companyId;
	//公司名
	private String  companyName;
	//用户状态（1：正常、2：无效）
	private Integer userStatus;
	//部门ID
	private Long deptId;
	//部门名
	private String deptName;
	//是否第一次登录（废弃不用）
	private Integer isAllowed;
	//该用户所拥有的角色列表
	private List<TblRoleInfo> userRoles;
	//多个用户id列表，一般在弹窗选择多个用户的时候使用
	private List<Long> userIds;
	
	
	
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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

	public Integer getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(Integer isAllowed) {
		this.isAllowed = isAllowed;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	
	
	

}
