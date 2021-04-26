package cn.pioneeruniverse.system.vo.user;

import java.util.Date;
import java.util.List;

import cn.pioneeruniverse.system.vo.BaseVo;
import cn.pioneeruniverse.system.vo.role.TblRoleInfo;

/**
 * 
* @ClassName: TblUserInfo
* @Description: 用户信息
* @author author
* @date 2020年9月4日 上午10:51:13
*
 */
public class TblUserInfo extends BaseVo {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 720187800927749405L;

	//账号
	private String userAccount;
	//密码
	private String userPassword;
	//姓名
	private String userName;
	//工号
	private String  employeeNumber;
	//用户状态
	private Integer userStatus;
	//人员类型（1:内部人员，2:外部人员）
	private Integer userType;
	//公司ID
	private Long companyId;
	//公司名
	private String companyName;
	//部门ID
	private Long deptId;
	//部门名
	private String deptName;
	//是否第一次登录（废弃）
	private Integer isAllowed;
	//rfid卡号（废弃）
	private String userRfid;
	//该用户所拥有的角色列表
	private List<TblRoleInfo> userRoles;
	//角色名
	private String roleName;
	//多个用户id，一般在弹框时查询所用
	private List<Long> userIds;
	//入职日期
	private Date entryDate;
	//离职日期
	private Date leaveDate;
	

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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Object get(String string) {
		return null;
	}


}
