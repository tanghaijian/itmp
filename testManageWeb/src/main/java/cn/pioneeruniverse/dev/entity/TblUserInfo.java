package cn.pioneeruniverse.dev.entity;

import java.util.List;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 *
 * @ClassName: TblDeptInfo
 * @Description: 用户表
 * @author author
 * @date 2020年8月27日 15:12:25
 *
 */
public class TblUserInfo extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1421203092465310527L;
	private String userAccount; //用户账号
	private String userPassword;//用户密码
	private String userName; //用户姓名
	private String  employeeNumber; //工号
	private Integer companyId; //所属公司ID
	private String  companyName; //所属公司名称
	private Integer userStatus; //用户状态（在职、离职）
	private Long deptId; //所在部门ID
	private String deptName; //所在部门名称
	private Integer isAllowed; //是否允许访问（主要是判断是否已经修改过密码0：未，1：是）
	//private List<TblRoleInfo> userRoles;
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

	

	/*public List<TblRoleInfo> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<TblRoleInfo> userRoles) {
		this.userRoles = userRoles;
	}*/

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
